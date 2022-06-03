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

import com.splendiddata.sqlparser.enums.JsonValueType;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonIsPredicate -<br>
 * representation of IS JSON predicate
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/primenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonIsPredicate extends Node {

    /** subject expression */
    @XmlElement
    public Node expr;

    /** FORMAT clause, if specified */
    @XmlElement
    public JsonFormat format;

    /** JSON item type */
    @XmlAttribute
    public JsonValueType item_type;

    /** check key uniqueness? */
    @XmlAttribute
    public boolean unique_keys;

    /**
     * Constructor
     */
    public JsonIsPredicate() {
        super(NodeTag.T_JsonIsPredicate);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonIsPredicate to copy
     */
    public JsonIsPredicate(JsonIsPredicate orig) {
        super(orig);
        if (orig.expr != null) {
            this.expr = orig.expr.clone();
        }
        if (orig.format != null) {
            this.format = orig.format.clone();
        }
        this.item_type = orig.item_type;
        this.unique_keys = orig.unique_keys;
    }

    @Override
    public JsonIsPredicate clone() {
        JsonIsPredicate clone = (JsonIsPredicate) super.clone();
        if (expr != null) {
            clone.expr = expr.clone();
        }
        if (format != null) {
            clone.format = format.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(expr);

        if (format != null) {
            result.append(format);
        }

        result.append(" is json");
        switch (item_type) {
        case JS_TYPE_ANY:
            break;
        case JS_TYPE_ARRAY:
            result.append(" array");
            break;
        case JS_TYPE_OBJECT:
            result.append(" object");
            break;
        case JS_TYPE_SCALAR:
            result.append(" scalar");
            break;
        default:
            result.append("????? please implement ").append(item_type.getClass().getName()).append('.')
                    .append(item_type.name()).append(" in ").append(getClass().getName()).append(".toString() ?????");
            break;
        }

        if (unique_keys) {
            result.append(" with unique keys");
        }

        return result.toString();
    }
}
