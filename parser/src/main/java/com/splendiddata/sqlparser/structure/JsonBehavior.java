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

import com.splendiddata.sqlparser.enums.JsonBehaviorType;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonBehavior -<br>
 * representation of JSON ON ... BEHAVIOR clause
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/primenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonBehavior extends Node {

    /** behavior type */
    @XmlAttribute
    public JsonBehaviorType btype;

    /** default expression, if any */
    @XmlElement
    public Node default_expr;

    /**
     * Constructor
     */
    public JsonBehavior() {
        super(NodeTag.T_JsonBehavior);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonBehavior to copy
     */
    public JsonBehavior(JsonBehavior orig) {
        super(orig);
        this.btype = orig.btype;
        if (orig.default_expr != null) {
            this.default_expr = orig.default_expr.clone();
        }
    }

    @Override
    public JsonBehavior clone() {
        JsonBehavior clone = (JsonBehavior) super.clone();
        if (default_expr != null) {
            clone.default_expr = default_expr.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        switch (btype) {
        case JSON_BEHAVIOR_DEFAULT:
            result.append("default");
            if (default_expr != null) {
                result.append(' ').append(default_expr);
            }
            break;
        case JSON_BEHAVIOR_EMPTY:
            result.append("empty");
            break;
        case JSON_BEHAVIOR_EMPTY_ARRAY:
            result.append("empty array");
            break;
        case JSON_BEHAVIOR_EMPTY_OBJECT:
            result.append("empty object");
            break;
        case JSON_BEHAVIOR_ERROR:
            result.append("error");
            break;
        case JSON_BEHAVIOR_FALSE:
            result.append("false");
            break;
        case JSON_BEHAVIOR_NULL:
            result.append("null");
            break;
        case JSON_BEHAVIOR_TRUE:
            result.append("true");
            break;
        case JSON_BEHAVIOR_UNKNOWN:
            result.append("unknown");
            break;
        default:
            result.append("????? please implement btype ").append(btype.getClass().getName()).append('.').append(btype.name())
            .append(" in ").append(getClass().getName()).append(".toString() ?????");
            break;
        }

        return result.toString();
    }
}
