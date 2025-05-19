/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2023
 *
 * This program is free software: You may redistribute and/or modify under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at Client's option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, Client should
 * obtain one via www.gnu.org/licenses/.
 */

package com.splendiddata.sqlparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.splendiddata.sqlparser.enums.Keyword;
import com.splendiddata.sqlparser.structure.Node;
import com.splendiddata.sqlparser.structure.TypeName;
import com.splendiddata.sqlparser.structure.Value;

/**
 * Some utility routines for the parser
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public final class ParserUtil {
    private static final Logger log = LogManager.getLogger(ParserUtil.class);

    private static final ThreadLocal<Reference<Marshaller>> marshallerCache = new ThreadLocal<>();

    /**
     * The parser normalises identifiers to lower case and without spaces. So if an identifier does not comply to that,
     * it should be between double quotes when used n SQL (again).
     */
    public static final Pattern STANDARD_IDENTIFIER_PATTERN = Pattern.compile("[a-z_][a-z0-9_]*|\\*");

    private static final Pattern OPERATOR_PATTERN = Pattern.compile("[+-/<>=~!@#%^&|`?\\*]+");

    /**
     * Utility class - null instances
     */
    private ParserUtil() {
        throw new RuntimeException("utility class - no instances");
    }

    /**
     * Marshals the statement to an xml representation. This may be helpful in debugging the parser. The marshaller is
     * cached
     *
     * @param statement
     *            The statement node that is to be presented in an XML format
     * @return String The statement in an xml format.
     */
    public static String stmtToXml(Object statement) {
        StringWriter writer = new StringWriter();

        Reference<Marshaller> ref = marshallerCache.get();
        Marshaller marshaller = null;
        if (ref != null) {
            marshaller = ref.get();
        }
        if (marshaller == null) {
            try {
                JAXBContext context = JAXBContext.newInstance(
                        "com.splendiddata.sqlparser.structure:com.splendiddata.sqlparser.structure",
                        new ClassLoader(ParserUtil.class.getClassLoader()) {

                            @Override
                            public InputStream getResourceAsStream(String name) {
                                URL url = getClass().getResource(name);
                                if (url == null) {
                                    url = getClass().getResource("/" + name);
                                }
                                if (url == null) {
                                    return null;
                                }
                                InputStream result = null;
                                try {
                                    result = url.openStream();
                                } catch (IOException e) {
                                    log.error("Error in getResourceAsStream(name=" + name + ")", e);
                                }
                                if (log.isDebugEnabled()) {
                                    log.debug("getResourceAsStream(name=" + name + "), url = " + url + ", result = "
                                            + result);
                                }
                                return result;
                            }
                        });
                marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                marshaller.setEventHandler(new ValidationEventHandler() {

                    @Override
                    public boolean handleEvent(ValidationEvent event) {
                        LogManager.getLogger(getClass()).warn("JAXB marshaller event occurred: " + event);
                        return true;
                    }

                });
                marshallerCache.set(new SoftReference<>(marshaller));
            } catch (JAXBException e) {
                log.error("stmtToString(statement=" + statement + ")->failed", e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        try {
            marshaller.marshal(statement, writer);
        } catch (JAXBException e) {
            log.error("stmtToString(statement=" + statement + ")->failed", e);
            throw new RuntimeException(e.getMessage(), e);
        }
        return writer.toString();
    }

    /**
     * If an identifier does not appear to exist solely of lower case letters, underscores and numbers, it must be
     * quoted i an sql statement. This method checks the identifier and quotes it if necessary.
     * <p>
     * For example: column_a will be returned as column_a, but Column B will be returned as "Column B".
     * </p>
     *
     * @param identifier
     *            The identifier that is to be presented in an SQL format
     * @return String The identifier, double-quoted if necessary
     */
    public static String identifierToSql(String identifier) {
        if (identifier == null) {
            return "null";
        }
        if (STANDARD_IDENTIFIER_PATTERN.matcher(identifier).matches()) {
            if (!"old".equalsIgnoreCase(identifier) && !"new".equalsIgnoreCase(identifier)
                    && Keyword.fromName(identifier) != null) {
                /*
                 * The identifier is a keyword, so needs quotation
                 */
                return '"' + identifier + '"';
            }
            return identifier;
        }
        return '"' + identifier.replace("\"", "\"\"") + '"';
    }

    /**
     * Transforms the name to a properly escaped period separated qualified name string.
     *
     * @param name
     *            as interpreted by the sql parser
     * @return String period separated qualified name
     * @since 5.0
     */
    @SuppressWarnings("unchecked")
    public static String nameToSql(Node name) {
        if (name == null) {
            return "null";
        }
        switch (name.type) {
        case T_String:
            return identifierToSql(name.toString());
        case T_A_Star:
            return "*";
        case T_TypeName:
            return nameListToSql(((TypeName) name).names);
        case T_List:
            return nameListToSql((java.util.List<? extends Node>) name);
        case T_ObjectWithArgs:
        case T_Integer:
            return name.toString();
        default:
            return new StringBuilder().append("???  field ").append(name.getClass().getName()).append(": \"")
                    .append(name)
                    .append("\" not supported in com.splendiddata.sqlparser.ParserUtil.nameToSql(List<Node>) ???")
                    .toString();
        }
    }

    /**
     * Transforms the name to a properly escaped period separated qualified name string.
     *
     * @param fields
     *            as interpreted by the sql parser
     * @return String period separated qualified name
     */
    public static String nameListToSql(java.util.List<? extends Node> fields) {
        if (fields == null) {
            return "null";
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Node namePart : fields) {
            if (first) {
                first = false;
            } else {
                result.append('.');
            }
            result.append(nameToSql(namePart));
        }
        return result.toString();
    }

    /**
     * Transforms the name to a properly escaped period separated qualified name string.
     *
     * @param name
     *            as interpreted by the sql parser
     * @return String period separated qualified name
     */
    public static String operatorNameToSql(List<Value> name) {
        if (name == null) {
            return "null";
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Value namePart : name) {
            if (first) {
                first = false;
            } else {
                result.append('.');
            }

            if (OPERATOR_PATTERN.matcher(namePart.val.str).matches()) {
                result.append(namePart);
            } else {
                result.append(identifierToSql(namePart.toString()));
            }
        }
        return result.toString();
    }

    /**
     * Tries to convert the objargs to a properly escaped argument list
     *
     * @param objargs
     *            The argument list
     * @return String The comma separated argument list in brackets
     */
    public static String argumentsToSql(List<? extends Node> objargs) {
        if (objargs == null) {
            return "";
        }

        StringBuilder result = new StringBuilder("(");
        String separator = "";
        for (Node arg : objargs) {
            result.append(separator);
            if (arg instanceof Value) {
                result.append(identifierToSql(arg.toString()));
            } else {
                result.append(arg);
            }
            separator = ", ";
        }

        return result.append(')').toString();
    }

    /**
     * Indicates something that is not (currently) understood so we can find out what is still to be implemented
     *
     * @param what
     *            Specified the type or the value that is in trouble
     * @param value
     *            The value that is not understood
     * @param inClass
     *            The class that reports the problem
     * @return String a string that indicates the error
     */
    public static String reportUnknownValue(String what, Object value, Class<?> inClass) {
        return new StringBuilder().append("??????? unknown ").append(what).append(": ").append(value).append(" in ")
                .append(inClass.getName()).append(" ???????").toString();
    }

    /**
     * Wraps the text in single quotes and escapes inner single quotes with an extra single quote
     *
     * @param text
     *            The text that is to be put between quoted
     * @return String 'text'
     */
    public static String toSqlTextString(Object text) {
        if (text == null) {
            return "null";
        }
        return new StringBuilder().append('\'').append(text.toString().replace("'", "''")).append('\'').toString();
    }

    /**
     * Returns parser version "16"
     *
     * @return String "16"
     */
    public static String getParserVersion() {
        return "16";
    }

    /**
     * Return the major version of postgresql.
     * <p>
     * Please use the literals that are defined in {@link com.splendiddata.sqlparser.PostgresVersionMagicInteger} to
     * compare, so that Postgres version dependent features can be tracked in the future.
     *
     * @return The major version of postgresql.
     */
    public static int getPostgresMajorVersion() {
        return PostgresVersionMagicInteger.VERSION_SIXTEEN;
    }

    /**
     * Returns a String specifying for which Postgres version the parser is intended. In this version: "PostgreSQL 15".
     *
     * @return String "PostgreSQL 15"
     */
    public static String getForPosgresVersion() {
        return "PostgreSQL 16";
    }

    /**
     * Compares the current postgres version to the supplied version. The version consists of a major and a minor
     * version.
     * 
     * @param majorVersion
     *            The major version.
     * @return Integer.compare(getPostgresMajorVersion(), majorVersion)
     */
    public static int compareCurrentPostgresVersionTo(final int majorVersion) {
        return Integer.compare(getPostgresMajorVersion(), majorVersion);
    }

    /**
     * Makes a name in a form like <i>&lt;element_name&gt;</i> on <i>&lt;qualified.object_name&gt;</i>
     *
     * @param nameList
     *            A list of which the first part is the qualified name of the object that owns the element, and the last
     *            node contains the element name
     * @param objectElementSeparator
     *            The word that is to be put between the element and the qualified object name. Think of "on" for a rule
     *            or a trigger, or "on domain" for a constraint on a domain
     * @return String The name in an sql format
     * @since 5.0
     */
    public static String elementOnObjectNameToSql(List<Node> nameList, String objectElementSeparator) {
        if (nameList == null) {
            return "null";
        }
        switch (nameList.size()) {
        case 0:
            return "empty";
        case 1:
            return nameToSql(nameList.get(0));
        case 2:
            return new StringBuilder().append(nameToSql(nameList.get(1))).append(' ').append(objectElementSeparator)
                    .append(' ').append(nameToSql(nameList.get(0))).toString();
        default:
            break;
        }
        int n = nameList.size() - 1;
        StringBuilder result = new StringBuilder().append(nameToSql(nameList.get(n))).append(' ')
                .append(objectElementSeparator);
        char separator = ' ';
        for (int i = 0; i < n; i++) {
            result.append(separator).append(nameToSql(nameList.get(i)));
            separator = '.';
        }
        return result.toString();
    }

    /**
     * Checks if the coll is null or empty
     *
     * @param coll The {@link Collection} to check
     * @return boolean true if null or empty
     */
    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }
}
