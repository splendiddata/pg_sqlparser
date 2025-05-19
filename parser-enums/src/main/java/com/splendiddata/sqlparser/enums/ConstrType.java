/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2025
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
 * Copied from /postgresql-12beta2/src/include/nodes/parsenodes.h
 * <p>
 * ----------<br>
 * Definitions for constraints in CreateStmt
 * </p>
 * <p>
 * Note that column defaults are treated as a type of constraint, even though that's a bit odd semantically.
 * </p>
 * <p>
 * For constraints that use expressions (CONSTR_CHECK, CONSTR_DEFAULT) we may have the expression in either "raw" form
 * (an untransformed parse tree) or "cooked" form (the nodeToString representation of an executable expression tree),
 * depending on how this Constraint node was created (by parsing, or by inheritance from an existing relation). We
 * should never have both in the same node!
 * </p>
 * <p>
 * FKCONSTR_ACTION_xxx values are stored into pg_constraint.confupdtype and pg_constraint.confdeltype columns;
 * FKCONSTR_MATCH_xxx values are stored into pg_constraint.confmatchtype. Changing the code values may require an
 * initdb!
 * </p>
 * <p>
 * If skip_validation is true then we skip checking that the existing rows in the table satisfy the constraint, and just
 * install the catalog entries for the constraint. A new FK constraint is marked as valid iff initially_valid is true.
 * (Usually skip_validation and initially_valid are inverses, but we can set both true if the table is known empty.)
 * </p>
 * <p>
 * Constraint attributes (DEFERRABLE etc) are initially represented as separate Constraint nodes for simplicity of
 * parsing. parse_utilcmd.c makes a pass through the constraints list to insert the info into the appropriate Constraint
 * node.<br>
 * ----------
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */

public enum ConstrType {
    /**
     * not standard SQL, but a lot of people expect it
     */
    CONSTR_NULL,
    CONSTR_NOTNULL,
    CONSTR_DEFAULT,
    /** @since 5.0 */
    CONSTR_IDENTITY,
    /** @since 7.0 - Postgres 12 */
    CONSTR_GENERATED,
    CONSTR_CHECK,
    CONSTR_PRIMARY,
    CONSTR_UNIQUE,
    CONSTR_EXCLUSION,
    CONSTR_FOREIGN,
    /** attributes for previous constraint node */
    CONSTR_ATTR_DEFERRABLE,
    CONSTR_ATTR_NOT_DEFERRABLE,
    CONSTR_ATTR_DEFERRED,
    CONSTR_ATTR_IMMEDIATE,
    /** @since Postgres 18 */
    CONSTR_ATTR_ENFORCED,
    /** @since Postgres 18 */
    CONSTR_ATTR_NOT_ENFORCED;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (ConstrType type : values()) {
            format.append(separator).append(type);
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
