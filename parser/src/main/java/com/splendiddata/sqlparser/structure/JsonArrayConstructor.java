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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonArrayConstructor -<br>
 * untransformed representation of JSON_ARRAY(element,...) constructor
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
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
     * @param orig
     *            The JsonArrayConstructor to copy
     */
    public JsonArrayConstructor(JsonArrayConstructor orig) {
        super(orig);
        if (orig.exprs != null) {
            this.exprs = orig.exprs.clone();
        }
        if (orig.output != null) {
            this.output = orig.output.clone();
        }
        this.absent_on_null = orig.absent_on_null;
    }

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
        StringBuilder result = new StringBuilder();
        String separator = "";

        result.append("json_array(");

        if (exprs != null && !exprs.isEmpty()) {
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
            result.append(separator).append("returning ").append(output);
        }

        result.append(')');

        return result.toString();
    }
}
