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
 * JsonValueExpr -<br>
 * representation of JSON value expression (expr [FORMAT json_format])
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
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
     * @param orig
     *            The JsonTablePlan to copy
     */
    public JsonValueExpr(JsonValueExpr orig) {
        super(orig);
        if (orig.raw_expr != null) {
            this.raw_expr = orig.raw_expr.clone();
        }
        if (orig.formatted_expr != null) {
            this.formatted_expr = orig.formatted_expr.clone();
        }
        if (orig.format != null) {
            this.format = orig.format.clone();
        }
    }

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

        if (raw_expr != null) {
            result.append(raw_expr);
        }
        if (formatted_expr != null) {
            result.append("????? What to do with formatted_expr ").append(getClass().getName())
                    .append(".toString() ?????");
        }
        if (format != null) {
            result.append(format);
        }
        return result.toString();
    }
}
