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
 * JsonObjectAgg -<br>
 * untransformed representation of JSON_OBJECTAGG()
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonObjectAgg extends Expr {

    /** common fields */
    @XmlElement
    public JsonAggConstructor constructor;

    /** object key-value pair */
    @XmlElement
    public JsonKeyValue arg;

    /** skip NULL values? */
    @XmlAttribute
    public boolean absent_on_null;

    /** check key uniqueness? */
    @XmlAttribute
    public boolean unique;

    /**
     * Constructor
     */
    public JsonObjectAgg() {
        super(NodeTag.T_JsonObjectAgg);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonObjectAgg to copy
     */
    public JsonObjectAgg(JsonObjectAgg orig) {
        super(orig);
        if (orig.constructor != null) {
            this.constructor = orig.constructor.clone();
        }
        if (orig.arg != null) {
            this.arg = orig.arg.clone();
        }
        this.absent_on_null = orig.absent_on_null;
        this.unique = orig.unique;
    }

    @Override
    public JsonObjectAgg clone() {
        JsonObjectAgg clone = (JsonObjectAgg) super.clone();
        if (constructor != null) {
            clone.constructor = constructor.clone();
        }
        if (arg != null) {
            clone.arg = arg.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";

        result.append("json_objectagg(");

        if (arg != null) {
            result.append(arg);
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

        if (constructor != null) {
            result.append(separator).append(constructor);
        }

        return result.toString();
    }
}
