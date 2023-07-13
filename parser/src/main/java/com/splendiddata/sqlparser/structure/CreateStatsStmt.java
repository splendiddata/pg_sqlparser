/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2023
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
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Create Statistics Statement
 * <p>
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class CreateStatsStmt extends Node {
    /** qualified name (list of Value strings) */
    @XmlTransient
    public List<Value> defnames;

    /** stat types (list of Value strings) */
    @XmlElementWrapper(name = "stat_types")
    @XmlElement(name = "stat_type")
    public List<Value> stat_types;

    /** expressions to build statistics on */
    @XmlElementWrapper(name = "exprs")
    @XmlElement(name = "expr")
    public List<StatsElem> exprs;

    /** rels to build stats on (list of RangeVar) */
    @XmlElementWrapper(name = "relations")
    @XmlElement(name = "relation")
    public List<RangeVar> relations;

    /**
     * comment to apply to stats, or NULL
     * 
     * @since 6.0 - Postgres version 11
     */
    @XmlAttribute
    public String stxcomment;

    /** do nothing if stats name already exists */
    @XmlAttribute
    public boolean if_not_exists;

    /**
     * Constructor
     */
    public CreateStatsStmt() {
        super(NodeTag.T_CreateStatsStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public CreateStatsStmt(CreateStatsStmt original) {
        super(original);
        if (original.defnames != null) {
            this.defnames = original.defnames.clone();
        }
        if (original.stat_types != null) {
            this.stat_types = original.stat_types.clone();
        }
        if (original.exprs != null) {
            this.exprs = original.exprs.clone();
        }
        if (original.relations != null) {
            this.relations = original.relations.clone();
        }
        this.stxcomment = original.stxcomment;
        this.if_not_exists = original.if_not_exists;
    }

    @Override
    public CreateStatsStmt clone() {
        CreateStatsStmt clone = (CreateStatsStmt) super.clone();
        if (stat_types != null) {
            clone.stat_types = stat_types.clone();
        }
        if (exprs != null) {
            clone.exprs = exprs.clone();
        }
        if (relations != null) {
            clone.relations = relations.clone();
        }
        return clone;
    }

    /**
     * Returns the qualified name for debugging purposes
     *
     * @return String the qualified name if specified
     */
    @XmlAttribute(name = "defnames")
    private String getQualifiedName() {
        if (defnames == null) {
            return null;
        }
        return ParserUtil.nameToSql(defnames);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder().append("create statistics");
        if (if_not_exists) {
            result.append(" if not exists");
        }
        if (defnames != null) {
            result.append(' ').append(getQualifiedName());
        }
        if (stat_types != null) {
            result.append(' ').append(stat_types);
        }
        if (exprs != null) {
            String separator = " on ";
            for (StatsElem expr : exprs) {
                result.append(separator).append(expr);
                separator = ", ";
            }
        }
        if (relations != null) {
            String separator = " from ";
            for (RangeVar relation : relations) {
                result.append(separator).append(relation);
                separator = ", ";
            }
        }
        return result.toString();
    }
}
