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

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonKeyValue - untransformed representation of JSON object key-value pair for JSON_OBJECT() and JSON_OBJECTAGG()
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonKeyValue extends Node {

    /** key expression */
    @XmlElement
    public Expr key;

    /** JSON value expression */
    @XmlElement
    public JsonValueExpr value;

    /**
     * Constructor
     */
    public JsonKeyValue() {
        super(NodeTag.T_JsonKeyValue);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonKeyValue(JsonKeyValue original) {
        super(original);
        if (original.key != null) {
            this.key = original.key.clone();
        }
        if (original.value != null) {
            this.value = original.value.clone();
        }
    }

    @Override
    public JsonKeyValue clone() {
        JsonKeyValue clone = (JsonKeyValue) super.clone();
        if (key != null) {
            clone.key = key.clone();
        }
        if (value != null) {
            clone.value = value.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (key != null) {
            result.append(key);
        }
        result.append(": ");
        if (value == null) {
            result.append("null");
        } else {
            result.append(value);
        }
        return result.toString();
    }
}
