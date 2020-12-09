/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ObjectType;

/**
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * <p>
 * ----------------------<br>
 * CREATE TABLE AS Statement (a/k/a SELECT INTO)<br>
 * <br>
 * A query written as CREATE TABLE AS will produce this node type natively. A query written as SELECT ... INTO will be
 * transformed to this form during parse analysis. A query written as CREATE MATERIALIZED view will produce this node
 * type, during parse analysis, since it needs all the same data.<br>
 * <br>
 * The "query" field is handled similarly to EXPLAIN, though note that it can be a SELECT or an EXECUTE, but not other
 * DML statements.<br>
 * ----------------------
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class CreateTableAsStmt extends Node {

    /** the query (see comments above) */
    @XmlElement
    public Node query;

    /** destination table */
    @XmlElement
    public IntoClause into;

    /** OBJECT_TABLE or OBJECT_MATVIEW */
    @XmlElement
    public ObjectType relkind;

    /** it was written as SELECT INTO */
    @XmlAttribute
    public boolean is_select_into;

    /** just do nothing if it already exists? */
    @XmlAttribute
    public boolean if_not_exists;

    /**
     * Constructor
     */
    public CreateTableAsStmt() {
        super(NodeTag.T_CreateTableAsStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateTableAsStmt to copy
     */
    public CreateTableAsStmt(CreateTableAsStmt original) {
        super(original);
        if (original.query != null) {
            this.query = original.query.clone();
        }
        if (original.into != null) {
            this.into = original.into.clone();
        }
        this.relkind = original.relkind;
        this.is_select_into = original.is_select_into;
        this.if_not_exists = original.if_not_exists;
    }

    @Override
    public CreateTableAsStmt clone() {
        CreateTableAsStmt clone = (CreateTableAsStmt) super.clone();
        if (query != null) {
            clone.query = query.clone();
        }
        if (into != null) {
            clone.into = into.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (is_select_into) {
            result.append(query).append(" into ").append(into);
        } else {
            result.append("create ");

            if (into.rel != null) {
                switch (into.rel.relpersistence) {
                case RELPERSISTENCE_PERMANENT:
                    break;
                case RELPERSISTENCE_TEMP:
                    result.append("temporary ");
                    break;
                case RELPERSISTENCE_UNLOGGED:
                    result.append("unlogged ");
                    break;
                default:
                    result.append(ParserUtil.reportUnknownValue("RangeVar.relpersistence", into.rel.relpersistence,
                            getClass()));
                }
            }

            result.append(relkind);

            if (if_not_exists) {
                result.append(" if not exists");
            }

            result.append(' ').append(into);

            if (into.accessMethod != null) {
                result.append(" using ").append(ParserUtil.identifierToSql(into.accessMethod));
            }

            result.append(" as ").append(query);
            if (into.skipData) {
                result.append(" with no data");
            }
        }

        return result.toString();
    }
}
