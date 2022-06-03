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
 * JsonOutput -<br>
 * representation of JSON output clause (RETURNING type [FORMAT format])
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/primenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonReturning extends Node {

    /** output JSON format */
    @XmlElement
    public JsonFormat format;

    // Oid         typid;          /* target type Oid */

    /* target type modifier */
    @XmlAttribute
    public int typmod;

    /**
     * Constructor
     */
    public JsonReturning() {
        super(NodeTag.T_JsonOutput);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonReturning to copy
     */
    public JsonReturning(JsonReturning orig) {
        super(orig);
        if (orig.format != null) {
            this.format = orig.format.clone();
        }
        this.typmod = orig.typmod;
    }

    @Override
    public JsonReturning clone() {
        JsonReturning clone = (JsonReturning) super.clone();
        if (format != null) {
            clone.format = format.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (typmod != 0) {
            result.append('(').append(typmod).append(')');
        }
        if (format != null) {
            result.append(format);
        }

        return result.toString();
    }
}
