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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonParseExpr - untransformed representation of JSON()
 * <p>
 * Copied from postgresql-17beta1/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 17
 */
@XmlRootElement(namespace = "parser")
public class JsonParseExpr extends Expr {

    /** string expression */
    @XmlElement
    public JsonValueExpr expr;

    /** RETURNING clause, if specified */
    @XmlElement
    public JsonOutput output;

    /** WITH UNIQUE KEYS? */
    @XmlAttribute
    public boolean unique_keys;

    /**
     * Constructor
     */
    public JsonParseExpr() {
        super(NodeTag.T_JsonScalarExpr);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonParseExpr(JsonParseExpr original) {
        super(original);
        if (original.expr != null) {
            this.expr = original.expr.clone();
        }
        if (original.output != null) {
            this.output = original.output.clone();
        }
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     *
     * @return JsonFormat the clone
     */
    @Override
    public JsonParseExpr clone() {
        JsonParseExpr clone = (JsonParseExpr) super.clone();
        if (expr != null) {
            clone.expr = expr.clone();
        }
        if (output != null) {
            clone.output = output.clone();
        }
        return clone;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";
        result.append("json(");
        if (expr != null) {
            result.append(expr);
            separator = " ";
        }
        if (output != null) {
            result.append(separator).append(output);
            separator = " ";
        }
        if (unique_keys) {
            result.append(separator).append("with unique keys");
        }
        result.append(")");
        return result.toString();
    }
}
