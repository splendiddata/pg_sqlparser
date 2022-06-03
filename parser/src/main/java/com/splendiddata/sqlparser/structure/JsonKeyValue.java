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

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonKeyValue -<br>
 * untransformed representation of JSON object key-value pair for JSON_OBJECT() and JSON_OBJECTAGG()
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
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
     * @param orig
     *            The JsonKeyValue to copy
     */
    public JsonKeyValue(JsonKeyValue orig) {
        super(orig);
        if (orig.key != null) {
            this.key = orig.key.clone();
        }
        if (orig.value != null) {
            this.value = orig.value.clone();
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

        result.append(key).append(": ").append(value);

        return result.toString();
    }
}
