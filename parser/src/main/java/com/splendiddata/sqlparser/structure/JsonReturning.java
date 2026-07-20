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
 * JsonReturning - transformed representation of JSON RETURNING clause
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonReturning extends Node {

    /** output JSON format */
    @XmlElement
    public JsonFormat format;

    /** target type Oid */
    @XmlElement
    public Oid typid;

    /** target type modifier */
    @XmlAttribute
    public int typmod;

    /**
     * Constructor
     */
    public JsonReturning() {
        super(NodeTag.T_JsonReturning);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonReturning(JsonReturning original) {
        super(original);
        if (original.format != null) {
            this.format = original.format.clone();
        }
        if (original.typid != null) {
            this.typid = original.typid.clone();
        }
        this.typmod = original.typmod;

    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public JsonReturning clone() {
        JsonReturning clone = (JsonReturning) super.clone();
        if (format != null) {
            clone.format = format.clone();
        }
        if (typid != null) {
            clone.typid = typid.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";
        if (typmod != 0) {
            result.append('(').append(typmod).append(')');
            separator = " ";
        }
        if (format != null && !format.toString().isBlank()) {
            result.append(separator).append(format);
        }

        return result.toString();
    }
}
