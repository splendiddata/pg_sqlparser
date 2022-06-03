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
 * JsonArrayAgg -<br>
 * untransformed representation of JSON_ARRRAYAGG()
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonArrayAgg extends Expr {

    /** common fields */
    @XmlElement
    public JsonAggConstructor constructor;

    /** array element expression */
    @XmlElement
    public JsonValueExpr arg;

    /** skip NULL values? */
    @XmlAttribute
    public boolean absent_on_null;

    /**
     * Constructor
     */
    public JsonArrayAgg() {
        super(NodeTag.T_JsonArrayAgg);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonArrayAgg to copy
     */
    public JsonArrayAgg(JsonArrayAgg orig) {
        super(orig);
        if (orig.constructor != null) {
            this.constructor = orig.constructor.clone();
        }
        if (orig.arg != null) {
            this.arg = orig.arg.clone();
        }
        this.absent_on_null = orig.absent_on_null;
    }

    @Override
    public JsonArrayAgg clone() {
        JsonArrayAgg clone = (JsonArrayAgg) super.clone();
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

        result.append("json_arrayagg(");

        if (arg != null) {
            result.append(arg);
            separator = " ";
        }

        if (!absent_on_null) {
            result.append(separator).append("null on null");
            separator = " ";
        }

        if (constructor != null) {
            result.append(constructor);
        } else {
            result.append(')');
        }

        return result.toString();
    }
}
