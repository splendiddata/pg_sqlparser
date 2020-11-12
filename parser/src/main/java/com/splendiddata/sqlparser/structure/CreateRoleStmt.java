/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
 *
 * This program is free software: You may redistribute and/or modify under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at Client's option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, Client should obtain one via www.gnu.org/licenses/.
 */

package com.splendiddata.sqlparser.structure;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.RoleStmtType;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CreateRoleStmt extends Node {
    /** ROLE/USER/GROUP */
    @XmlAttribute
    public RoleStmtType stmt_type;

    /**
     * role name
     * <p>
     * The parser hasn't made up its mine yet whether role is a RoleSpec or a String. Until then the only thing we can
     * do here is define it as Object. And because JAXB does not like Objects (throws null pointers at them), we'll make
     * it mlTransient here and create getter or it.
     * </p>
     */
    @XmlTransient
    public Object role;

    /** List of DefElem nodes */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     */
    public CreateRoleStmt() {
        super(NodeTag.T_CreateRoleStmt);
    }

    /**
     * Copy constructor
     * 
     * @param toCopy
     *            The CreateRoleStmt to copy
     */
    public CreateRoleStmt(CreateRoleStmt toCopy) {
        super(toCopy);
        this.stmt_type = toCopy.stmt_type;
        this.role = toCopy.role;
        if (toCopy.options != null) {
            this.options = toCopy.options.clone();
        }
    }

    @Override
    public CreateRoleStmt clone() {
        CreateRoleStmt clone = (CreateRoleStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create ").append(stmt_type).append(' ');
        if (role instanceof RoleSpec) {
            result.append(role);
        } else {
            result.append(ParserUtil.identifierToSql(getRole()));
        }

        if (options != null) {
            String separator = " with ";
            for (DefElem option : options) {
                if (option == null) {
                    /*
                     * An unencrypted password may have caused this, Just skip it.
                     */
                    continue;
                }
                switch (option.defname) {
                case "superuser":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(separator).append("nosuperuser");
                    } else {
                        result.append(separator).append("superuser");
                    }
                    break;
                case "createdb":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(separator).append("nocreatedb");
                    } else {
                        result.append(separator).append("createdb");
                    }
                    break;
                case "createrole":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(separator).append("nocreaterole");
                    } else {
                        result.append(separator).append("createrole");
                    }
                    break;
                case "inherit":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(separator).append("noinherit");
                    } else {
                        result.append(separator).append("inherit");
                    }
                    break;
                case "canlogin":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(separator).append("nologin");
                    } else {
                        result.append(separator).append("login");
                    }
                    break;
                case "password":
                    result.append(separator).append("password");
                    if (option.arg == null) {
                        result.append(" null");
                    } else {
                        result.append(' ').append(ParserUtil.toSqlTextString(option.arg));
                    }
                    break;
                case "encryptedPassword":
                    result.append(separator).append("encrypted password ")
                            .append(ParserUtil.toSqlTextString(option.arg));
                    break;
                case "unencryptedPassword":
                    result.append(separator).append("unencrypted password ")
                            .append(ParserUtil.toSqlTextString(option.arg));
                    break;
                case "validUntil":
                    result.append(separator).append("valid until ").append(ParserUtil.toSqlTextString(option.arg));
                    break;
                case "addroleto":
                    rolesToString(" in role ", (List<RoleSpec>) option.arg, result);
                    break;
                case "rolemembers":
                    rolesToString(" role ", (List<RoleSpec>) option.arg, result);
                    break;
                case "adminmembers":
                    rolesToString(" admin ", (List<RoleSpec>) option.arg, result);
                    break;
                case "sysid":
                    result.append(" sysid ").append(option.arg);
                    break;
                case "isreplication":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(separator).append("noreplication");
                    } else {
                        result.append(separator).append("replication");
                    }
                    break;
                case "connectionlimit":
                    result.append(" connection limit ").append(option.arg);
                    break;
                case "bypassrls":
                    result.append(" bypassrls");
                    break;
                default:
                    result.append(ParserUtil.reportUnknownValue("option.defname", option.defname, getClass()));
                    break;
                }
                separator = " ";
            }
        }

        return result.toString();
    }

    /**
     * Adds the subcommand and the roles to the result
     *
     * @param subcommand
     *            Must start and end with a space as it is inserted directly into the result
     * @param roles
     *            List of roles to insert after the subcommand
     * @param result
     *            The StringBuilder to which the subcommand and roles will be written
     */
    private static void rolesToString(String subcommand, List<RoleSpec> roles, StringBuilder result) {
        if (roles != null) {
            String separator = subcommand;
            for (RoleSpec member : roles) {
                result.append(separator).append(member);
                separator = ", ";
            }
        }
    }

    /**
     * returns the role in a String format
     * <p>
     * role needs to be an Object now because the parser sometimes returns it as RoleSpec and sometimes as String. But
     * JAXB does not like Objects (returns null pointers for that at
     * com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor.get(TransducedAccessor.java:152). So the
     * getRole() method solely exists to keep JAXB happy until the parser is updated to return the right result
     * (probably RoleSpec).
     * </p>
     *
     * @return String The role name
     */
    @XmlAttribute
    public String getRole() {
        return role.toString();
    }
}
