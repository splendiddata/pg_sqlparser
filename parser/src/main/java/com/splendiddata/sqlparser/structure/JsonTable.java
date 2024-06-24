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

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonTable - untransformed representation of JSON_TABLE
 * 
 * <p>
 * Join expression. Copied from /postgresql-17beta1/src/include/nodes/primnodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since Postgres 17
 */
@XmlRootElement(namespace = "parser")
public class JsonTable extends Node {
    /** context item expression */
    @XmlElement
    public JsonValueExpr context_item;

    /** JSON path specification */
    @XmlElement
    public JsonTablePathSpec pathspec;

    /** list of PASSING clause arguments, if any */
    @XmlElementWrapper(name = "passingList")
    @XmlElement(name = "passing")
    public List<Node> passing;

    /** list of JsonTableColumn */
    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    public List<JsonTableColumn> columns;

    /** ON ERROR behavior */
    @XmlElement
    public JsonBehavior on_error;

    /** table alias in FROM clause */
    @XmlElement
    public Alias alias;

    /** does it have LATERAL prefix? */
    @XmlAttribute
    public boolean lateral;

    /**
     * Constructor
     */
    public JsonTable() {
        type = NodeTag.T_JsonTable;
    }

    /**
     * Shallow copy constructor
     *
     * @param other
     *            The JoinExpr to copy
     */
    public JsonTable(JsonTable other) {
        super(other);
        if (other.context_item != null) {
            this.context_item = other.context_item.clone();
        }
        if (other.pathspec != null) {
            this.pathspec = other.pathspec.clone();
        }
        if (other.passing != null) {
            this.passing = other.passing.clone();
        }
        if (other.columns != null) {
            this.columns = other.columns.clone();
        }
        if (other.on_error != null) {
            this.on_error = on_error.clone();
        }
        if (other.alias != null) {
            this.alias = other.alias.clone();
        }
        this.lateral = other.lateral;
    }

    @Override
    public JsonTable clone() {
        JsonTable clone = (JsonTable) super.clone();
        if (context_item != null) {
            clone.context_item = context_item.clone();
        }
        if (pathspec != null) {
            clone.pathspec = pathspec.clone();
        }
        if (passing != null) {
            clone.passing = passing.clone();
        }
        if (columns != null) {
            clone.columns = columns.clone();
        }
        if (on_error != null) {
            clone.on_error = on_error.clone();
        }
        if (alias != null) {
            clone.alias = alias.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";
        if (lateral) {
            result.append("lateral");
            separator = " ";
        }
        result.append(separator).append("json_table (");
        if (context_item != null) {
            result.append(context_item);
            separator = ", ";
        }
        if (pathspec != null) {
            result.append(separator).append(pathspec);
            separator = " ";
        }
        if (passing != null) {
            separator += "passing";
            for (Node node : passing) {
                result.append(separator).append(node);
                separator = ", ";
            }
            separator = " ";
        }
        if (columns != null) {
            result.append(separator).append("columns ").append(columns);
            separator = " ";
        }
        if (on_error != null) {
            result.append(separator).append(on_error).append(" on error");
            separator = " ";
        }
        result.append(')');
        if (alias != null) {
            result.append(" as ").append(alias);
        }

        return result.toString();
    }
}
