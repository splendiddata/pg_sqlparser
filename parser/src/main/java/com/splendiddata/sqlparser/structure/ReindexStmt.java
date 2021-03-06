/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
 *
 * This program is free software: You may redistribute and/or modify under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at Client's option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, Client should obtain one via www.gnu.org/licenses/.
 */

package com.splendiddata.sqlparser.structure;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ReindexObjectType;

/**
 * Copied from /postgresql-12beta2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class ReindexStmt extends Node {

    /** OBJECT_INDEX, OBJECT_TABLE, etc. */
    @XmlAttribute
    public ReindexObjectType kind;

    /** Table or index to reindex */
    @XmlElement
    public RangeVar relation;

    /** name of database to reindex */
    @XmlAttribute
    public String name;

    /** Reindex options flags */
    @XmlAttribute
    public int options;

    /**
     * reindex concurrently?
     * 
     * @since 7.0 - Postgres 12
     */
    @XmlAttribute
    public boolean concurrent;

    /**
     * Constructor
     */
    public ReindexStmt() {
        super(NodeTag.T_ReindexStmt);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The original
     */
    public ReindexStmt(ReindexStmt toCopy) {
        super(toCopy);
        this.kind = toCopy.kind;
        if (toCopy.relation != null) {
            this.relation = toCopy.relation.clone();
        }
        this.name = toCopy.name;
        this.options = toCopy.options;
        this.concurrent = toCopy.concurrent;
    }

    @Override
    public ReindexStmt clone() {
        ReindexStmt clone = (ReindexStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("reindex ");

        if (options == 1) {
            result.append("(verbose) ");
        }

        switch (kind) {
        case REINDEX_OBJECT_SCHEMA:
        case REINDEX_OBJECT_DATABASE:
            result.append(kind).append(' ').append(ParserUtil.identifierToSql(name));
            break;
        case REINDEX_OBJECT_SYSTEM:
            result.append("system").append(' ').append(ParserUtil.identifierToSql(name));
            break;
        case REINDEX_OBJECT_TABLE:
        case REINDEX_OBJECT_INDEX:
            result.append(kind).append(' ').append(relation);
            break;
        default:
            result.append(ParserUtil.reportUnknownValue("kind", kind.name(), getClass()));
            break;
        }

        return result.toString();
    }
}
