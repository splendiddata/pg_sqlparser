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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class UpdateStmt extends Node {

    /** relation to update */
    @XmlElement
    public RangeVar relation;

    /** the target list (of ResTarget) */
    @XmlElementWrapper(name = "targetList")
    @XmlElement(name = "resTarget")
    public List<ResTarget> targetList;

    /** qualifications */
    @XmlElement
    public Node whereClause;

    /** optional from clause for more tables */
    @XmlElementWrapper(name = "fromClause")
    @XmlElement(name = "from")
    public List<Node> fromClause;

    /** list of expressions to return */
    @XmlElementWrapper(name = "returningList")
    @XmlElement(name = "returning")
    public List<ResTarget> returningList;

    /** WITH clause */
    @XmlElement
    public WithClause withClause;

    /**
     * Constructor
     */
    public UpdateStmt() {
        super(NodeTag.T_UpdateStmt);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The UpdateStmt to copy
     */
    public UpdateStmt(UpdateStmt orig) {
        super(orig);
        if (orig.relation != null) {
            this.relation = orig.relation.clone();
        }
        if (orig.targetList != null) {
            this.targetList = orig.targetList.clone();
        }
        if (orig.whereClause != null) {
            this.whereClause = orig.whereClause.clone();
        }
        if (orig.fromClause != null) {
            this.fromClause = orig.fromClause.clone();
        }
        if (orig.returningList != null) {
            this.returningList = orig.returningList.clone();
        }
        if (orig.withClause != null) {
            this.withClause = orig.withClause.clone();
        }
    }

    @Override
    public UpdateStmt clone() {
        UpdateStmt clone = (UpdateStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (targetList != null) {
            clone.targetList = targetList.clone();
        }
        if (whereClause != null) {
            clone.whereClause = whereClause.clone();
        }
        if (fromClause != null) {
            clone.fromClause = fromClause.clone();
        }
        if (returningList != null) {
            clone.returningList = returningList.clone();
        }
        if (withClause != null) {
            clone.withClause = withClause.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (withClause != null) {
            result.append(withClause).append(' ');
        }

        result.append("update ").append(relation);

        /*
         * Walk through the target list. MultipleAssigment lists will be (re)combined.
         */
        MultiAssignRef activeMultiAssingment = null;
        String separator = " set ";
        for (ResTarget target : targetList) {
            if (target.val instanceof MultiAssignRef) {
                MultiAssignRef m = (MultiAssignRef) target.val;
                if (m.colno == 1) {
                    // colno 1 starts a new MultiAssingmentRef list
                    if (activeMultiAssingment != null) {
                        result.append(") = ").append(activeMultiAssingment);
                    }
                    activeMultiAssingment = m;
                    result.append(separator);
                    separator = "(";
                }
                result.append(separator).append(ParserUtil.identifierToSql(target.name));
                separator = ", ";
            } else {
                if (activeMultiAssingment != null) {
                    result.append(") = ").append(activeMultiAssingment);
                    activeMultiAssingment = null;
                    separator = ", ";
                }
                result.append(separator).append(ParserUtil.identifierToSql(target.name));
                if (target.indirection != null && !target.indirection.isEmpty()) {
                    for (Node ind : target.indirection) {
                        if (!NodeTag.T_A_Indices.equals(ind.type)) {
                            result.append('.');
                        }
                        result.append(ind);
                    }
                }
                result.append(" = ").append(target.val);
                separator = ", ";
            }
        }
        if (activeMultiAssingment != null) {
            result.append(") = ").append(activeMultiAssingment);
            activeMultiAssingment = null;

        }

        if (fromClause != null) {
            separator = " from ";
            for (Node from : fromClause) {
                result.append(separator).append(from);
                separator = ", ";
            }
        }

        if (whereClause != null) {
            result.append(" where ").append(whereClause);
        }

        if (returningList != null) {
            separator = " returning ";
            for (Node returning : returningList) {
                result.append(separator).append(returning);
                separator = ", ";
            }
        }

        return result.toString();
    }
}
