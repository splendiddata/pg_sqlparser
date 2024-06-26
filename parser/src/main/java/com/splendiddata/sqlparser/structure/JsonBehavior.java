/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2024
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

import com.splendiddata.sqlparser.enums.JsonBehaviorType;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonBehavior
 * <p>
 * Specifications for ON ERROR / ON EMPTY behaviors of SQL/JSON query functions specified by a JsonExpr
 * <p>
 * 'expr' is the expression to emit when a given behavior (EMPTY or ERROR) occurs on evaluating the SQL/JSON query
 * function. 'coerce' is set to true if 'expr' isn't already of the expected target type given by JsonExpr.returning.
 * <p>
 * Join expression. Copied from /postgresql-17beta1/src/include/nodes/primnodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since Postgres 17
 */
@XmlRootElement(namespace = "parser")
public class JsonBehavior extends Node {

    @XmlAttribute
    public JsonBehaviorType btype;

    @XmlElement
    public Node expr;

    @XmlAttribute
    public boolean coerce;

    /**
     * Constructor
     */
    public JsonBehavior() {
        type = NodeTag.T_JsonBehavior;
    }

    /**
     * Shallow copy constructor
     *
     * @param other
     *            The JoinExpr to copy
     */
    public JsonBehavior(JsonBehavior other) {
        super(other);
        this.btype = other.btype;
        if (other.expr != null) {
            this.expr = other.expr.clone();
        }
        this.coerce = other.coerce;
    }

    @Override
    public JsonBehavior clone() {
        JsonBehavior clone = (JsonBehavior) super.clone();
        if (expr != null) {
            clone.expr = expr.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";
        if (btype != null) {
            result.append(btype);
            separator = " ";
        }
        if (expr != null) {
            result.append(separator).append(expr);
            separator = " ";
        }
        if (coerce) {
            result.append(separator).append("????? please implement coerce in ").append(this.getClass().getName())
                    .append(".toString() ?????");
            separator = " ";
        }

        return result.toString();
    }
}
