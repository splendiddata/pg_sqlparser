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
 * JsonSerializeExpr -<br>
 * untransformed representation of JSON_SERIALIZE() function
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonSerializeExpr extends Node {

    /** json value expression */
    @XmlElement
    public JsonValueExpr expr;

    /** RETURNING clause, if specified */
    @XmlElement
    public JsonOutput output;

    /**
     * Constructor
     */
    public JsonSerializeExpr() {
        super(NodeTag.T_JsonSerializeExpr);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonSerializeExpr to copy
     */
    public JsonSerializeExpr(JsonSerializeExpr orig) {
        super(orig);
        if (orig.expr != null) {
            this.expr = orig.expr.clone();
        }
        if (orig.output != null) {
            this.output = orig.output.clone();
        }
    }

    @Override
    public JsonSerializeExpr clone() {
        JsonSerializeExpr clone = (JsonSerializeExpr) super.clone();
        if (expr != null) {
            clone.expr = expr.clone();
        }
        if (output != null) {
            clone.output = output.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("json_serialize(");

        if (expr != null) {
            result.append(expr);
        }

        if (output != null) {
            result.append(" returning ").append(output);
        }

        result.append(')');

        return result.toString();
    }
}
