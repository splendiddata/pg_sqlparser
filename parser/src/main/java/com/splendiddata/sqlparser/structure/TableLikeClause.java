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

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-12beta2/src/include/nodes/parsenodes.h
 * <p>
 * Beware! In Postgres 11 all values have changed places! And in Postgres 12 some values are changed again.
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class TableLikeClause extends Node {

    /*
     * Copied from TableLikeOption in /postgresql-10rc1/src/include/nodes/parsenodes.h
     */
    public static final int CREATE_TABLE_LIKE_COMMENTS = 1 << 0;
    public static final int CREATE_TABLE_LIKE_CONSTRAINTS = 1 << 1;
    public static final int CREATE_TABLE_LIKE_DEFAULTS = 1 << 2;
    /** @since 7.0 - Postgres 12 */
    public static final int CREATE_TABLE_LIKE_GENERATED = 1 << 3;
    /**
     * @since 5.0
     */
    public static final int CREATE_TABLE_LIKE_IDENTITY = 1 << 4;
    public static final int CREATE_TABLE_LIKE_INDEXES = 1 << 5;
    /** @since 6.0 - Postgres version 11 */
    public static final int CREATE_TABLE_LIKE_STATISTICS = 1 << 6;
    public static final int CREATE_TABLE_LIKE_STORAGE = 1 << 7;
    public static final int CREATE_TABLE_LIKE_ALL = 0x7FFFFFFF;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART = "CREATE_TABLE_LIKE_.+";

    @XmlElement
    public RangeVar relation;

    /** OR of TableLikeOption flags */
    @XmlAttribute
    public int options;

    /**
     * Constructor
     */
    public TableLikeClause() {
        super(NodeTag.T_TableLikeClause);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public TableLikeClause(TableLikeClause original) {
        super(original);
        if (original.relation != null) {
            this.relation = original.relation.clone();
        }
        this.options = original.options;
    }

    @Override
    public TableLikeClause clone() {
        TableLikeClause clone = (TableLikeClause) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("like ").append(relation);

        if (options == CREATE_TABLE_LIKE_ALL) {
            result.append(" including all");
        } else {
            if ((options & CREATE_TABLE_LIKE_DEFAULTS) == CREATE_TABLE_LIKE_DEFAULTS) {
                result.append(" including defaults");
            }
            if ((options & CREATE_TABLE_LIKE_GENERATED) == CREATE_TABLE_LIKE_GENERATED) {
                result.append(" including generated");
            }
            if ((options & CREATE_TABLE_LIKE_IDENTITY) == CREATE_TABLE_LIKE_IDENTITY) {
                result.append(" including identity");
            }
            if ((options & CREATE_TABLE_LIKE_CONSTRAINTS) == CREATE_TABLE_LIKE_CONSTRAINTS) {
                result.append(" including constraints");
            }
            if ((options & CREATE_TABLE_LIKE_INDEXES) == CREATE_TABLE_LIKE_INDEXES) {
                result.append(" including indexes");
            }
            if ((options & CREATE_TABLE_LIKE_STORAGE) == CREATE_TABLE_LIKE_STORAGE) {
                result.append(" including storage");
            }
            if ((options & CREATE_TABLE_LIKE_COMMENTS) == CREATE_TABLE_LIKE_COMMENTS) {
                result.append(" including comments");
            }
        }

        return result.toString();
    }
}
