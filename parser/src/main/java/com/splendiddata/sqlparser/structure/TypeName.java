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

import java.util.Iterator;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.TemporalWord;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class TypeName extends Node {

    /** qualified name (list of Value strings) */
    @XmlElementWrapper(name = "names")
    @XmlElement(name = "name")
    public List<Value> names;

    /** is a set? */
    @XmlAttribute
    public boolean setof;

    /** %TYPE specified? */
    @XmlAttribute
    public boolean pct_type;

    /** type modifier expression(s) */
    @XmlElementWrapper(name = "typmods")
    @XmlElement(name = "typmod")
    public List<A_Const> typmods;

    /** array bounds */
    @XmlElementWrapper(name = "arrayBounds")
    @XmlElement(name = "arrayBound")
    public List<Value> arrayBounds;

    /**
     * Constructor
     */
    public TypeName() {
        super(NodeTag.T_TypeName);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The TypeName to copy
     */
    public TypeName(TypeName toCopy) {
        super(toCopy);
        if (toCopy.names != null) {
            this.names = toCopy.names.clone();
        }
        this.setof = toCopy.setof;
        this.pct_type = toCopy.pct_type;
        if (toCopy.typmods != null) {
            this.typmods = toCopy.typmods.clone();
        }
        if (toCopy.arrayBounds != null) {
            this.arrayBounds = toCopy.arrayBounds.clone();
        }
    }

    @Override
    public String toString() {
        if (names == null) {
            return ParserUtil.stmtToXml(this);
        }

        boolean isPgCatalog = false;

        StringBuilder result = new StringBuilder();
        if (names != null) {
            Iterator<Value> it = names.iterator();
            if (it.hasNext()) {
                Value name = it.next();
                if ("pg_catalog".equals(name.val.str)) {
                    /*
                     * The parser does not understand schema pg_catalog, so if there is another level of classification
                     * let's skip this level
                     */
                    isPgCatalog = true;
                    if (it.hasNext()) {
                        name = it.next();
                        switch (name.val.str) {
                        case "int4":
                            result.append("integer");
                            break;
                        case "varbit":
                            result.append("bit varying");
                            break;
                        case "bpchar":
                            result.append("char");
                            break;
                        default:
                            if (ParserUtil.STANDARD_IDENTIFIER_PATTERN.matcher(name.val.str).matches()) {
                                result.append(name.val.str);
                            } else {
                                result.append('"').append(name.val.str).append('"');
                            }
                            break;
                        }
                    } else {
                        result.append("\"pg_catalog\"");
                    }
                } else if ("any".equals(name.val.str)) {
                    result.append("\"any\"");
                } else if ("char".equals(name.val.str)) {
                    result.append("\"char\"");
                } else if (ParserUtil.STANDARD_IDENTIFIER_PATTERN.matcher(name.val.str).matches()) {
                    result.append(name.val.str);
                } else {
                    result.append('"').append(name.val.str).append('"');
                }

                while (it.hasNext()) {
                    name = it.next();
                    result.append('.');
                    if (ParserUtil.STANDARD_IDENTIFIER_PATTERN.matcher(name.val.str).matches()) {
                        result.append(name.val.str);
                    } else {
                        result.append('"').append(name.val.str).append('"');
                    }
                }
                if (pct_type) {
                    result.append("%TYPE");
                }
            }
        }

        String typeName = result.toString();

        if (setof) {
            return "setof " + typeName;
        }

        if (typmods != null) {
            if (isPgCatalog && "interval".equals(typeName)) {
                result.append(' ').append(timeIntervalToString());
            } else {
                result.append(typmods);
            }
        }

        if (arrayBounds != null) {
            for (Value arrayBound : arrayBounds) {
                if (arrayBound.val.ival < 0) {
                    result.append("[]");
                } else {
                    result.append('[').append(arrayBound.val.ival).append(']');
                }
            }
        }

        return result.toString();
    }

    /**
     * Interprets the typmod to interval syntax
     *
     * @return String The String value of the typmod
     */
    public String timeIntervalToString() {
        if (typmods == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        /*
         * The first typmod holds ored temporal words
         */
        Iterator<A_Const> it = typmods.iterator();
        A_Const typmod = it.next();
        int intValue = typmod.val.val.ival;

        for (TemporalWord word : TemporalWord.values()) {
            if ((intValue & word.CONSTANT) == word.CONSTANT) {
                /*
                 * Add the found word
                 */
                result.append(word);

                if ((intValue ^ word.CONSTANT) != 0 && !TemporalWord.RESERV.equals(word)) {
                    /*
                     * The found word is not the whole story. Scan the TemporalWords backward to find the smallest
                     * interval applicable and add that as "to" argument.
                     */
                    for (int i = TemporalWord.values().length - 1; i >= 0; i--) {
                        word = TemporalWord.values()[i];
                        if ((intValue & word.CONSTANT) == word.CONSTANT) {
                            result.append(" to ").append(word);
                            break;
                        }
                    }
                }
                break;
            }
        }

        /*
         * The second typmod (if present) holds the precision
         */
        if (it.hasNext()) {
            result.append('(').append(it.next()).append(')');
        }

        return result.toString();
    }

    @Override
    public TypeName clone() {
        TypeName clone = (TypeName) super.clone();
        if (names != null) {
            clone.names = names.clone();
        }
        if (typmods != null) {
            clone.typmods = typmods.clone();
        }
        if (arrayBounds != null) {
            clone.arrayBounds = arrayBounds.clone();
        }
        return clone;
    }
}
