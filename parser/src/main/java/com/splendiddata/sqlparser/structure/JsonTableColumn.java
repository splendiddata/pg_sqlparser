/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2025
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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.JsonQuotes;
import com.splendiddata.sqlparser.enums.JsonTableColumnType;
import com.splendiddata.sqlparser.enums.JsonWrapper;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonTableColumn
 * <p>
 * untransformed representation of JSON_TABLE column
 * <p>
 * Copied from /postgresql-17beta1/src/include/nodes/primnodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since Postgres 17
 */
@XmlRootElement(namespace = "parser")
public class JsonTableColumn extends Node {

    /** column type */
    @XmlAttribute
    public JsonTableColumnType coltype;

    /** column name */
    @XmlAttribute
    public String name;

    /** column type name */
    @XmlElement
    public TypeName typeName;

    /** JSON path specification */
    @XmlElement
    public JsonTablePathSpec pathspec;

    /** JSON format clause, if specified */
    @XmlElement
    public JsonFormat format;

    /** WRAPPER behavior for formatted columns */
    @XmlAttribute
    public JsonWrapper wrapper;

    /** omit or keep quotes on scalar strings? */
    @XmlAttribute
    public JsonQuotes quotes;

    /** nested columns */
    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    public List<Node> columns;

    /** ON EMPTY behavior */
    @XmlElement
    public JsonBehavior on_empty;

    /** ON ERROR behavior */
    @XmlElement
    public JsonBehavior on_error;

    /**
     * Constructor
     */
    public JsonTableColumn() {
        type = NodeTag.T_JsonTableColumn;
    }

    /**
     * Shallow copy constructor
     *
     * @param other
     *            The JoinExpr to copy
     */
    public JsonTableColumn(JsonTableColumn other) {
        super(other);
        this.coltype = other.coltype;
        this.name = other.name;
        if (other.typeName != null) {
            this.typeName = other.typeName.clone();
        }
        if (other.pathspec != null) {
            this.pathspec = other.pathspec.clone();
        }
        if (other.format != null) {
            this.format = other.format.clone();
        }
        this.wrapper = other.wrapper;
        this.quotes = other.quotes;
        if (other.columns != null) {
            this.columns = other.columns.clone();
        }
        if (other.on_empty != null) {
            this.on_empty = other.on_empty.clone();
        }
        if (other.on_error != null) {
            this.on_error = other.on_error.clone();
        }
    }

    @Override
    public JsonTableColumn clone() {
        JsonTableColumn clone = (JsonTableColumn) super.clone();
        if (typeName != null) {
            clone.typeName = typeName.clone();
        }
        if (pathspec != null) {
            clone.pathspec = pathspec.clone();
        }
        if (format != null) {
            clone.format = format.clone();
        }
        if (columns != null) {
            clone.columns = columns.clone();
        }
        if (on_empty != null) {
            clone.on_empty = on_empty.clone();
        }
        if (on_error != null) {
            clone.on_error = on_error.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";
        if (name != null) {
            result.append(ParserUtil.identifierToSql(name));
            separator = " ";
        }
        if (typeName != null) {
            result.append(separator).append(typeName);
            separator = " ";
        }
        if (coltype != null && !JsonTableColumnType.JTC_REGULAR.equals(coltype)
                && !JsonTableColumnType.JTC_FORMATTED.equals(coltype)) {
            result.append(separator).append(coltype);
            separator = " ";
        }
        if (format != null) {
            result.append(separator).append(format);
            separator = " ";
        }
        if (pathspec != null) {
            result.append(separator).append("path ").append(pathspec);
            separator = " ";
        }
        if (wrapper != null) {
            switch (wrapper) {
            case JSW_CONDITIONAL:
                result.append(separator).append("with conditional wrapper");
                separator = " ";
                break;
            case JSW_NONE:
                break;
            case JSW_UNCONDITIONAL:
                result.append(separator).append("with wrapper");
                separator = " ";
                break;
            case JSW_UNSPEC:
                break;
            default:
                result.append(separator).append(ParserUtil.reportUnknownValue("wrapper", wrapper.name(), getClass()));
                separator = " ";
                break;
            }
        }

        if (quotes != null) {
            switch (quotes) {
            case JS_QUOTES_KEEP:
                result.append(separator).append("keep quotes");
                separator = " ";
                break;
            case JS_QUOTES_OMIT:
                result.append(separator).append("omit quotes");
                separator = " ";
                break;
            case JS_QUOTES_UNSPEC:
                break;
            default:
                result.append(separator).append(ParserUtil.reportUnknownValue("quotes", quotes.name(), getClass()));
                separator = " ";
                break;
            }
        }
        if (columns != null) {
            result.append(separator).append("columns").append(columns);
            separator = " ";
        }
        if (on_empty != null) {
            result.append(separator).append(on_empty).append(" on empty");
            separator = " ";
        }
        if (on_error != null) {
            result.append(separator).append(on_error).append(" on error");
        }
        return result.toString();
    }
}
