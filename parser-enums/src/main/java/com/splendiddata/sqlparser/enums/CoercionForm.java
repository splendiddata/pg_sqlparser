/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
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

package com.splendiddata.sqlparser.enums;

/**
 * CoercionForm - how to display a FuncExpr or related node
 * <p>
 * "Coercion" is a bit of a misnomer, since this value records other special syntaxes besides casts, but for now we'll
 * keep this naming.
 * </p>
 * <p>
 * NB: equal() ignores CoercionForm fields, therefore this *must* not carry any semantically significant information. We
 * need that behavior so that the planner will consider equivalent implicit and explicit casts to be equivalent. In
 * cases where those actually behave differently, the coercion function's arguments will be different.
 * </p>
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/primnodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
public enum CoercionForm {
    /** display as a function call */
    COERCE_EXPLICIT_CALL,
    /** display as an explicit cast */
    COERCE_EXPLICIT_CAST,
    /** implicit cast, so hide it */
    COERCE_IMPLICIT_CAST,
    /**
     * display with SQL-mandated special syntax
     * 
     * @since 14.0
     */
    COERCE_SQL_SYNTAX;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (CoercionForm type : values()) {
            format.append(separator).append(type);
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
