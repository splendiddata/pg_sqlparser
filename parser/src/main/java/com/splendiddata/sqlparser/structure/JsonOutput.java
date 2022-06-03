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
 * JsonOutput -<br>
 * representation of JSON output clause (RETURNING type [FORMAT format])
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonOutput extends Node {

    /** RETURNING type name, if specified */
    @XmlElement
    public TypeName typeName;

    /** RETURNING FORMAT clause and type Oids */
    @XmlElement
    public JsonReturning returning;

    /**
     * Constructor
     */
    public JsonOutput() {
        super(NodeTag.T_JsonOutput);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonOutput to copy
     */
    public JsonOutput(JsonOutput orig) {
        super(orig);
        if (orig.typeName != null) {
            this.typeName = orig.typeName.clone();
        }
        if (orig.returning != null) {
            this.returning = orig.returning.clone();
        }
    }

    @Override
    public JsonOutput clone() {
        JsonOutput clone = (JsonOutput) super.clone();
        if (typeName != null) {
            clone.typeName = typeName.clone();
        }
        if (returning != null) {
            clone.returning = returning.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(typeName);
        
        if (returning != null) {
            result.append(returning);
        }

        return result.toString();
    }
}
