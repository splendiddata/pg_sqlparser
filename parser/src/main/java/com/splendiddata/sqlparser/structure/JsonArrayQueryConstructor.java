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
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonArrayQueryConstructor -<br>
 * untransformed representation of JSON_ARRAY(subquery) constructor
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
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
     * @param orig
     *            The JsonArrayQueryConstructor to copy
     */
    public JsonArrayQueryConstructor(JsonArrayQueryConstructor orig) {
        super(orig);
        if (orig.query != null) {
            this.query = orig.query.clone();
        }
        if (orig.output != null) {
            this.output = orig.output.clone();
        }
        if (orig.format != null) {
            this.format = orig.format.clone();
        }
        this.absent_on_null = orig.absent_on_null;
    }

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
        StringBuilder result = new StringBuilder();

        result.append("json_array(").append(query);

        if (!absent_on_null) {
            result.append(" null on null");
        }

        if (output != null) {
            result.append(" returning ").append(output);
        }

        if (format != null) {
            result.append(format);
        }

        result.append(')');

        return result.toString();
    }
}
