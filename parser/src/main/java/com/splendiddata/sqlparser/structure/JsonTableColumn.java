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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.JsonTableColumnType;
import com.splendiddata.sqlparser.enums.JsonWrapper;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonTableColumn -<br>
 * untransformed representation of JSON_TABLE column
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
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

    /** path specification, if any */
    @XmlAttribute
    public String pathspec;

    /** path name, if any */
    @XmlAttribute
    public String pathname;

    /** JSON format clause, if specified */
    @XmlElement
    public JsonFormat format;

    /** WRAPPER behavior for formatted columns */
    @XmlAttribute
    public JsonWrapper wrapper;

    /** omit or keep quotes on scalar strings? */
    @XmlAttribute
    public boolean omit_quotes;

    /** nested columns */
    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    public List<ColumnDef> columns;

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
        super(NodeTag.T_JsonTableColumn);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonTableColumn to copy
     */
    public JsonTableColumn(JsonTableColumn orig) {
        super(orig);
        this.coltype = orig.coltype;
        this.name = orig.name;
        if (orig.typeName != null) {
            this.typeName = orig.typeName.clone();
        }
        this.pathspec = orig.pathspec;
        this.pathname = orig.pathname;
        if (orig.format != null) {
            this.format = orig.format.clone();
        }
        this.wrapper = orig.wrapper;
        this.omit_quotes = orig.omit_quotes;
        if (orig.columns != null) {
            this.columns = orig.columns.clone();
        }
        if (orig.on_error != null) {
            this.on_error = orig.on_error.clone();
        }
        if (orig.on_empty != null) {
            this.on_empty = orig.on_empty.clone();
        }
        if (orig.on_error != null) {
            this.on_error = orig.on_error.clone();
        }
    }

    @Override
    public JsonTableColumn clone() {
        JsonTableColumn clone = (JsonTableColumn) super.clone();
        if (typeName != null) {
            clone.typeName = typeName.clone();
        }
        if (format != null) {
            clone.format = format.clone();
        }
        if (columns != null) {
            clone.columns = columns.clone();
        }
        if (on_error != null) {
            clone.on_error = on_error.clone();
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

        switch (coltype) {
        case JTC_EXISTS:
            result.append(ParserUtil.identifierToSql(name)).append(' ').append(typeName).append(" exists");
            break;
        case JTC_FORMATTED:
            result.append(ParserUtil.identifierToSql(name)).append(' ').append(typeName);
            break;
        case JTC_FOR_ORDINALITY:
            result.append(ParserUtil.identifierToSql(name)).append(" for ordinality");
            break;
        case JTC_NESTED:
            result.append("nested");
            break;
        case JTC_REGULAR:
            result.append(ParserUtil.identifierToSql(name)).append(' ').append(typeName);
            break;
        default:
            result.append("????? please implement coltype ").append(coltype.getClass().getName()).append('.')
                    .append(coltype.name()).append(" in ").append(getClass().getName()).append(".toString() ?????");
            break;
        }

        if (format != null) {
            result.append(format);
        }

        if (pathspec != null) {
            result.append(" path '").append(pathspec).append('\'');
            if (pathname != null) {
                result.append(" as ").append(ParserUtil.identifierToSql(pathname));
            }
        }

        if (wrapper != null) {
            switch (wrapper) {
            case JSW_CONDITIONAL:
                result.append(" with conditional wrapper");
                break;
            case JSW_NONE:
                break;
            case JSW_UNCONDITIONAL:
                result.append(" with wrapper");
                break;
            default:
                result.append("????? please implement wrapper ").append(wrapper.getClass().getName()).append('.')
                        .append(wrapper.name()).append(" in ").append(getClass().getName()).append(".toString() ?????");
                break;
            }
        }

        if (omit_quotes) {
            result.append(" omit quotes");
        }

        if (columns != null && !columns.isEmpty()) {
            result.append(" columns ").append(columns);
        }

        if (on_empty != null) {
            result.append(' ').append(on_empty).append(" on empty");
        }

        if (on_error != null) {
            result.append(' ').append(on_error).append(" on error");
        }

        return result.toString();
    }
}
