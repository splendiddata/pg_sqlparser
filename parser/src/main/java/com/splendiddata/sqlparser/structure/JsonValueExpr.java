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
 * JsonValueExpr - representation of JSON value expression (expr [FORMAT JsonFormat])
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonValueExpr extends Node {

    /** raw expression */
    @XmlElement
    public Expr raw_expr;

    /** formatted expression or NULL */
    @XmlElement
    public Expr formatted_expr;

    /** FORMAT clause, if specified */
    @XmlElement
    public JsonFormat format;

    /**
     * Constructor
     */
    public JsonValueExpr() {
        super(NodeTag.T_JsonValueExpr);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonValueExpr(JsonValueExpr original) {
        super(original);
        if (original.raw_expr != null) {
            this.raw_expr = original.raw_expr.clone();
        }
        if (original.formatted_expr != null) {
            this.formatted_expr = original.formatted_expr.clone();
        }
        if (original.format != null) {
            this.format = original.format.clone();
        }
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public JsonValueExpr clone() {
        JsonValueExpr clone = (JsonValueExpr) super.clone();
        if (raw_expr != null) {
            clone.raw_expr = raw_expr.clone();
        }
        if (formatted_expr != null) {
            clone.formatted_expr = formatted_expr.clone();
        }
        if (format != null) {
            clone.format = format.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";
        if (formatted_expr != null) {
            result.append(formatted_expr);
            separator = " ";
        } else if (raw_expr != null) {
            result.append(raw_expr);
            separator = " ";
        }
        if (format != null && !format.toString().isBlank()) {
            result.append(separator).append(format);
        }
        return result.toString();
    }

}
