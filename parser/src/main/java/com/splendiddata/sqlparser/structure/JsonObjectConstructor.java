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
 * JsonObjectConstructor -<br>
 * untransformed representation of JSON_OBJECT() constructor
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonObjectConstructor extends Expr {

    /** list of JsonKeyValue pairs */
    @XmlElementWrapper(name = "exprs")
    @XmlElement(name = "expr")
    public List<JsonKeyValue> exprs;

    /** RETURNING clause, if specified */
    @XmlElement
    public JsonOutput output;

    /** skip NULL values? */
    @XmlAttribute
    public boolean absent_on_null;

    /** check key uniqueness? */
    @XmlAttribute
    public boolean unique;

    /**
     * Constructor
     */
    public JsonObjectConstructor() {
        super(NodeTag.T_JsonObjectConstructor);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonObjectConstructor to copy
     */
    public JsonObjectConstructor(JsonObjectConstructor orig) {
        super(orig);
        if (orig.exprs != null) {
            this.exprs = orig.exprs.clone();
        }
        if (orig.output != null) {
            this.output = orig.output.clone();
        }
        this.absent_on_null = orig.absent_on_null;
        this.unique = orig.unique;
    }

    @Override
    public JsonObjectConstructor clone() {
        JsonObjectConstructor clone = (JsonObjectConstructor) super.clone();
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

        result.append("json_object(");
        if (exprs != null) {
            for (JsonKeyValue expr : exprs) {
                result.append(separator).append(expr);
                separator = ", ";
            }
            separator = " ";
        }

        if (absent_on_null) {
            result.append(separator).append("absent on null");
            separator = " ";
        }

        if (unique) {
            result.append(separator).append("with unique keys");
            separator = " ";
        }

        if (output != null) {
            result.append(separator).append("returning ").append(output);
        }

        result.append(')');

        return result.toString();
    }
}
