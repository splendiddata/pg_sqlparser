/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2022
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

package com.splendiddata.sqlparser.structure;

import java.util.regex.Pattern;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Just a value, copied from /postgresql-9.3.4/src/include/nodes/value.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class Value extends Node {
    private static final Pattern FLOAT_NEEDS_PARENS_PATTERN = Pattern.compile("^-\\d*\\.\\d*+e-?\\d+$",
            Pattern.CASE_INSENSITIVE);

    /** tag appropriately (eg. T_String) */
    @XmlElement
    public ValUnion val = new ValUnion();

    /**
     * Constructor
     */
    public Value() {
        // empty
    }

    /**
     * Constructor
     *
     * @param type
     *            Must be one of
     *            <ul>
     *            <li>{@link com.splendiddata.sqlparser.enums.NodeTag#T_Value}</li>
     *            <li>{@link com.splendiddata.sqlparser.enums.NodeTag#T_Integer}</li>
     *            <li>{@link com.splendiddata.sqlparser.enums.NodeTag#T_Float}</li>
     *            <li>{@link com.splendiddata.sqlparser.enums.NodeTag#T_Boolean}</li>
     *            <li>{@link com.splendiddata.sqlparser.enums.NodeTag#T_String}</li>
     *            <li>{@link com.splendiddata.sqlparser.enums.NodeTag#T_BitString}</li>
     *            <li>{@link com.splendiddata.sqlparser.enums.NodeTag#T_Null}</li>
     *            </ul>
     */
    public Value(NodeTag type) {
        super(type);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The Value to copy
     */
    public Value(Value toCopy) {
        super(toCopy);
        val = new ValUnion(toCopy.val);
    }

    @Override
    public String toString() {
        if (NodeTag.T_Float.equals(type)) {
            String result = val.toString();
            if (FLOAT_NEEDS_PARENS_PATTERN.matcher(result).matches()) {
                return new StringBuilder().append('(').append(result).append(')').toString();
            }
            return result;
        }
        if (NodeTag.T_Boolean.equals(type)) {
            return Boolean.toString(val.boolval);
        }
        return val.toString();
    }

    @Override
    public Value clone() {
        Value clone = (Value) super.clone();
        clone.val = val.clone();
        return clone;
    }
}
