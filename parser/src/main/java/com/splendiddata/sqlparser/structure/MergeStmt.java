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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Merge Statement
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 15
 */
@XmlRootElement(namespace = "parser")
public class MergeStmt extends Node {

    /** target relation to merge into */
    @XmlElement
    public RangeVar relation;

    /** source relation */
    @XmlElement
    public Node sourceRelation;

    /** join condition between source and target */
    @XmlElement
    public Node joinCondition;

    /** list of MergeWhenClause(es) */
    @XmlElementWrapper(name = "indirection")
    @XmlElement(name = "indirectionNode")
    public List<MergeWhenClause> mergeWhenClauses;

    /** WITH clause */
    @XmlElement
    public WithClause withClause;

    /**
     * Constructor
     */
    public MergeStmt() {
        super(NodeTag.T_MergeStmt);
    }

    /**
     * Shallow copy constructor
     *
     * @param original
     *            The MergeStmt to copy
     */
    public MergeStmt(MergeStmt original) {
        super(original);
        if (original.relation != null) {
            this.relation = original.relation.clone();
        }
        if (original.sourceRelation != null) {
            this.sourceRelation = original.sourceRelation.clone();
        }
        if (original.joinCondition != null) {
            this.joinCondition = original.joinCondition.clone();
        }
        if (original.mergeWhenClauses != null) {
            this.mergeWhenClauses = original.mergeWhenClauses.clone();
        }
        if (original.withClause != null) {
            this.withClause = original.withClause.clone();
        }
    }

    @Override
    public MergeStmt clone() {
        MergeStmt clone = (MergeStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (sourceRelation != null) {
            clone.sourceRelation = sourceRelation.clone();
        }
        if (joinCondition != null) {
            clone.joinCondition = joinCondition.clone();
        }
        if (mergeWhenClauses != null) {
            clone.mergeWhenClauses = mergeWhenClauses.clone();
        }
        if (withClause != null) {
            clone.withClause = withClause.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (this.withClause != null) {
            result.append(withClause).append(' ');
        }
        result.append("merge into ").append(relation).append(" using ").append(sourceRelation).append(" on ")
                .append(joinCondition);
        if (mergeWhenClauses != null) {
            for (MergeWhenClause mergeWhenClause : mergeWhenClauses) {
                result.append(' ').append(mergeWhenClause);
            }
        }

        return result.toString();
    }
}
