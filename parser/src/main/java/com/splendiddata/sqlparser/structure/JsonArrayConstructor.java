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
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonArrayConstructor - untransformed representation of JSON_ARRAY(element,...) constructor
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonArrayConstructor extends Expr {

    /** list of JsonValueExpr elements */
    @XmlElementWrapper(name = "exprs")
    @XmlElement(name = "expr")
    public List<JsonValueExpr> exprs;

    /** RETURNING clause, if specified */
    @XmlElement
    public JsonOutput output;

    /** skip NULL elements? */
    @XmlAttribute
    public boolean absent_on_null;

    /**
     * Constructor
     */
    public JsonArrayConstructor() {
        super(NodeTag.T_JsonArrayConstructor);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonArrayConstructor(JsonArrayConstructor original) {
        super(original);
        if (original.exprs != null) {
            this.exprs = original.exprs.clone();
        }
        if (original.output != null) {
            this.output = original.output.clone();
        }
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public JsonArrayConstructor clone() {
        JsonArrayConstructor clone = (JsonArrayConstructor) super.clone();
        if (exprs != null) {
            clone.exprs = exprs.clone();
        }
        if (output != null) {
            clone.output = output.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("json_array(");
        String separator = "";
        if (exprs != null) {
            for (JsonValueExpr expr : exprs) {
                result.append(separator).append(expr);
                separator = ", ";
            }
            separator = " ";
        }
        if (!absent_on_null) {
            result.append(separator).append("null on null");
            separator = " ";
        }
        if (output != null) {
            result.append(separator).append(output);
        }
        result.append(")");
        return result.toString();
    }
}
