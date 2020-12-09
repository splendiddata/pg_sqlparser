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

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Insert Statement
 * <p>
 * The source expression is represented by SelectStmt for both the SELECT and VALUES cases. If selectStmt is NULL, then
 * the query is INSERT ... DEFAULT VALUES.
 * </p>
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class InsertStmt extends Node {
    /**
     * value 0
     * 
     * @since 5.0
     */
    public static final int OVERRIDING_NOT_SET = 0;
    /**
     * value 1
     * 
     * @since 5.0
     */
    public static final int OVERRIDING_USER_VALUE = 1;
    /**
     * value 2
     * 
     * @since 5.0
     */
    public static final int OVERRIDING_SYSTEM_VALUE = 2;

    /** relation to insert into */
    @XmlElement
    public RangeVar relation;

    /** optional: names of the target columns */
    @XmlElementWrapper(name = "cols")
    @XmlElement(name = "col")
    public List<ResTarget> cols;

    /** the source SELECT/VALUES, or NULL */
    @XmlElement
    public Node selectStmt;

    /** ON CONFLICT clause */
    @XmlElement
    public OnConflictClause onConflictClause;

    /** list of expressions to return */
    @XmlElementWrapper(name = "returningList")
    @XmlAnyElement
    public List<ResTarget> returningList;

    /** WITH clause */
    @XmlElement
    public WithClause withClause;

    /**
     * OVERRIDING clause Can be 0, 1 or 2 for resp. {@link #OVERRIDING_NOT_SET}, {@link #OVERRIDING_USER_VALUE} or
     * {@link #OVERRIDING_SYSTEM_VALUE}
     * 
     * @since 5.0
     */
    @XmlAttribute
    public int override;

    /**
     * Constructor
     */
    public InsertStmt() {
        super(NodeTag.T_InsertStmt);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The InsertStmt to copy
     */
    public InsertStmt(InsertStmt orig) {
        super(orig);
        if (orig.relation != null) {
            this.relation = orig.relation.clone();
        }
        if (orig.cols != null) {
            this.cols = orig.cols.clone();
        }
        if (orig.selectStmt != null) {
            this.selectStmt = orig.selectStmt.clone();
        }
        if (orig.onConflictClause != null) {
            this.onConflictClause = orig.onConflictClause.clone();
        }
        if (orig.returningList != null) {
            this.returningList = orig.returningList.clone();
        }
        if (orig.withClause != null) {
            this.withClause = orig.withClause.clone();
        }
        this.override = orig.override;
    }

    @Override
    public InsertStmt clone() {
        InsertStmt clone = (InsertStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (cols != null) {
            clone.cols = cols.clone();
        }
        if (selectStmt != null) {
            clone.selectStmt = selectStmt.clone();
        }
        if (onConflictClause != null) {
            clone.onConflictClause = onConflictClause.clone();
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

        result.append("insert into ").append(relation);

        /*
         * If no columns are specified and no select statement is provided, the row will be created using default
         * values.
         */
        boolean defaultValues = true;

        if (cols != null) {
            defaultValues = false;
            result.append(cols);
        }

        switch (override) {
        case OVERRIDING_NOT_SET:
            break;
        case OVERRIDING_SYSTEM_VALUE:
            result.append(" overriding system value");
            break;
        case OVERRIDING_USER_VALUE:
            result.append(" overriding user value");
            break;
        default:
            break;
        }

        if (selectStmt != null) {
            defaultValues = false;
            result.append(' ').append(selectStmt);
        }

        if (defaultValues) {
            result.append(" default values");
        }

        if (onConflictClause != null) {
            result.append(' ').append(onConflictClause);
        }

        if (returningList != null) {
            String separator = " returning ";
            for (Node returning : returningList) {
                result.append(separator).append(returning);
                separator = ", ";
            }
        }

        return result.toString();
    }
}
