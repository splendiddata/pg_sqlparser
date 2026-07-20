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
 * JsonArrayQueryConstructor - untransformed representation of JSON_ARRAY(subquery) constructor
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonArrayQueryConstructor extends Node {

    /** subquery */
    @XmlElement
    public Node query;

    /** RETURNING clause, if specified */
    @XmlElement
    public JsonOutput output;

    /** FORMAT clause for subquery, if specified */
    @XmlElement
    public JsonFormat format;

    /** skip NULL elements? */
    @XmlAttribute
    public boolean absent_on_null;

    /**
     * Constructor
     */
    public JsonArrayQueryConstructor() {
        super(NodeTag.T_JsonArrayQueryConstructor);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonArrayQueryConstructor(JsonArrayQueryConstructor original) {
        super(original);
        if (original.query != null) {
            this.query = original.query.clone();
        }
        if (original.output != null) {
            this.output = original.output.clone();
        }
        if (original.format != null) {
            this.format = original.format.clone();
        }
        this.absent_on_null = original.absent_on_null;
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public JsonArrayQueryConstructor clone() {
        JsonArrayQueryConstructor clone = (JsonArrayQueryConstructor) super.clone();
        if (query != null) {
            clone.query = query.clone();
        }
        if (output != null) {
            clone.output = output.clone();
        }
        if (format != null) {
            clone.format = format.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("json_array");
        String separator = "(";
        if (query != null) {
            result.append(separator).append(query);
            separator = " ";
        }
        if (!absent_on_null) {
            result.append(separator).append("null on null");
            separator = " ";
        }
        if (output != null) {
            result.append(separator).append(output);
            separator = " ";
        }
        if (format != null && !format.toString().isBlank()) {
            result.append(separator).append(format);
        }
        result.append(")");
        return result.toString();
    }
}
