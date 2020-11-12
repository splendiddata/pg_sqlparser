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

package com.splendiddata.sqlparser.enums;

/**
 * JoinTypes from /postgresql-9.3.4/src/include/nodes/nodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public enum JoinType {
    /*
     * The canonical kinds of joins according to the SQL JOIN syntax. Only these codes can appear in parser output
     * (e.g., JoinExpr nodes).
     */
    /** matching tuple pairs only */
    JOIN_INNER,
    /** pairs + unmatched LHS tuples */
    JOIN_LEFT,
    /** pairs + unmatched LHS + unmatched RHS */
    JOIN_FULL,
    /** pairs + unmatched RHS tuples */
    JOIN_RIGHT,

    /*
     * Semijoins and anti-semijoins (as defined in relational theory) do not appear in the SQL JOIN syntax, but there
     * are standard idioms for representing them (e.g., using EXISTS). The planner recognizes these cases and converts
     * them to joins. So the planner and executor must support these codes. NOTE: in JOIN_SEMI output, it is unspecified
     * which matching RHS row is joined to. In JOIN_ANTI output, the row is guaranteed to be null-extended.
     */
    /** 1 copy of each LHS row that has match(es) */
    JOIN_SEMI,
    /** 1 copy of each LHS row that has no match */
    JOIN_ANTI,

    /*
     * These codes are used internally in the planner, but are not supported by the executor (nor, indeed, by most of
     * the planner).
     */
    /** LHS path must be made unique */
    JOIN_UNIQUE_OUTER,
    /** RHS path must be made unique */
    JOIN_UNIQUE_INNER;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (JoinType type : values()) {
            format.append(separator).append(type);
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
