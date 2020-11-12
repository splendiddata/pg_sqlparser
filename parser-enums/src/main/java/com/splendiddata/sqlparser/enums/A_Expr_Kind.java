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
 * A_Expr - infix, prefix, and postfix expressions
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */

public enum A_Expr_Kind {
    /** normal operator */
    AEXPR_OP,
    /** scalar op ANY (array) */
    AEXPR_OP_ANY,
    /** scalar op ALL (array) */
    AEXPR_OP_ALL,
    /** IS DISTINCT FROM - name must be "=" */
    AEXPR_DISTINCT,
    /** IS NOT DISTINCT FROM - name must be "=" */
    AEXPR_NOT_DISTINCT,         
    /** NULLIF - name must be "=" */
    AEXPR_NULLIF,
    /** IS [NOT] OF - name must be "=" or "&lt;&gt;" */
    AEXPR_OF,
    /** [NOT] IN - name must be "=" or "&lt;&gt;" */
    AEXPR_IN,
    /** [NOT] LIKE - name must be "~~" or "!~~" */
    AEXPR_LIKE,
    /** [NOT] ILIKE - name must be "~~*" or "!~~*" */
    AEXPR_ILIKE,
    /** [NOT] SIMILAR - name must be "~" or "!~" */
    AEXPR_SIMILAR,
    /** name must be "BETWEEN" */
    AEXPR_BETWEEN,
    /** name must be "NOT BETWEEN" */
    AEXPR_NOT_BETWEEN,
    /** name must be "BETWEEN SYMMETRIC" */
    AEXPR_BETWEEN_SYM,
    /** name must be "NOT BETWEEN SYMMETRIC" */
    AEXPR_NOT_BETWEEN_SYM,
    /** nameless dummy node for parentheses */
    AEXPR_PAREN;

    /**
     * The A_Expr_Kind enum appears not very stable over Postgres releases. To support more than one Postgres release in
     * the rules engine, somtimes if and switch statements make use of the name of the enum value instead of the actual
     * enum value. To keep track of the places where that happens, a NAME variable is created here (cross reference from
     * the specific NAME field is easier than from a very generic name() method).
     * <p>
     * So on every new Postgres release, find usages of the NAME field and deal with the differences.
     * </p>
     */
    public final String NAME = name();

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (A_Expr_Kind type : values()) {
            format.append(separator).append(type);
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
