/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2026
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
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class DeleteStmt extends Stmt {
    /** relation to delete from */
    @XmlElement
    public RangeVar relation;

    /** optional using clause for more tables */
    @XmlElementWrapper(name = "usingClause")
    @XmlElement(name = "using")
    public List<Node> usingClause;

    /** qualifications */
    @XmlElement
    public Node whereClause;

    /**
     * list of expressions to return
     * 
     * @deprecated since Postgres 18: Use {@link #returningClause} instead
     */
    @Deprecated(since = "Postgres 18", forRemoval = true)
    @XmlElementWrapper(name = "returninglist")
    @XmlElement(name = "returning")
    public List<ResTarget> returningList;

    /**
     * RETURNING clause
     * 
     * @since Postgres 18
     */
    @XmlElement
    public ReturningClause returningClause;

    /** WITH clause */
    @XmlElement
    public WithClause withClause;

    /**
     * FOR PORTION OF clause
     * 
     * @since 19beta1
     */
    @XmlElement
    public ForPortionOfClause forPortionOf;

    /**
     * Constructor
     */
    public DeleteStmt() {
        super(NodeTag.T_DeleteStmt);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The DeleteStmt to copy
     */
    public DeleteStmt(DeleteStmt orig) {
        super(orig);
        if (orig.relation != null) {
            this.relation = orig.relation.clone();
        }
        if (orig.usingClause != null) {
            this.usingClause = orig.usingClause.clone();
        }
        if (orig.whereClause != null) {
            this.whereClause = orig.whereClause.clone();
        }
        if (orig.returningList != null) {
            this.returningList = orig.returningList.clone();
        }
        if (orig.returningClause != null) {
            this.returningClause = orig.returningClause.clone();
        }
        if (orig.withClause != null) {
            this.withClause = orig.withClause.clone();
        }
        if (orig.forPortionOf != null) {
            this.forPortionOf = orig.forPortionOf.clone();
        }
    }

    @Override
    public DeleteStmt clone() {
        DeleteStmt clone = (DeleteStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (usingClause != null) {
            clone.usingClause = usingClause.clone();
        }
        if (whereClause != null) {
            clone.whereClause = whereClause.clone();
        }
        if (returningList != null) {
            clone.returningList = returningList.clone();
        }
        if (returningClause != null) {
            clone.returningClause = returningClause.clone();
        }
        if (withClause != null) {
            clone.withClause = withClause.clone();
        }
        if (forPortionOf != null) {
            clone.forPortionOf = forPortionOf.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (withClause != null) {
            result.append(withClause).append(' ');
        }

        result.append("delete");
        if (relation != null) {
            result.append(" from ").append(relation.toString(forPortionOf));
        }

        if (usingClause != null) {
            String separator = " using ";
            for (Node tableExpression : usingClause) {
                result.append(separator).append(tableExpression);
                separator = ", ";
            }
        }

        if (whereClause != null) {
            result.append(" where ").append(whereClause);
        }

        if (returningList != null) {
            String separator = " returning ";
            for (ResTarget returning : returningList) {
                result.append(separator).append(returning);
                separator = ", ";
            }
        }

        if (returningClause != null) {
            result.append(' ').append(returningClause);
        }

        return result.toString();
    }
}
