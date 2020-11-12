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
 * Copied from /postgresql-12beta2/src/include/nodes/parsenodes.h
 * <p>
 * CommonTableExpr - representation of WITH list element
 * </p>
 * <p>
 * We don't currently support the SEARCH or CYCLE clause.
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 7.0 - Postgres 12
 */

public enum CTEMaterialize {
    /** no option specified */
    CTEMaterializeDefault,
    /** MATERIALIZED */
    CTEMaterializeAlways,
    /** NOT MATERIALIZED */
    CTEMaterializeNever;

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
        for (CTEMaterialize type : values()) {
            format.append(separator).append(type);
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
