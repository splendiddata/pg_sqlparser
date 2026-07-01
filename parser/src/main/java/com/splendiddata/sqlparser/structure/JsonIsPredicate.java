/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2026
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
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * JsonIsPredicate - representation of IS JSON predicate
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
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
     * base type of the subject expression
     * 
     * @since Postgres 19beta1
     */
    @XmlTransient
    public Oid exprBaseType;

    public JsonIsPredicate() {
        super(NodeTag.T_JsonIsPredicate);
    }

    public JsonIsPredicate(JsonIsPredicate original) {
        super(original);
        if (original.expr != null) {
            this.expr = original.expr.clone();
        }
        if (original.format != null) {
            this.format = original.format.clone();
        }
        this.item_type = original.item_type;
        this.unique_keys = original.unique_keys;
        if (original.exprBaseType != null) {
            this.exprBaseType = original.exprBaseType.clone();
        }
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
        if (exprBaseType != null) {
            clone.exprBaseType = exprBaseType.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";
        if (expr != null) {
            result.append(expr);
            separator = " ";
        }
        result.append(separator).append("is json");
        separator = " ";
        if (item_type != null) {
            result.append(item_type);
        }
        if (unique_keys) {
            result.append(separator).append("with unique keys");
        }
        if (format != null) {
            String formatTxt = format.toString();
            if (!formatTxt.isBlank()) {
                result.append(separator).append(format);
                separator = " ";
            }
        }
        return result.toString();
    }
}
