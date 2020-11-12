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
 * BoolExpr - expression node for the basic Boolean operators AND, OR, NOT
 * <p>
 * Notice the arguments are given as a List. For NOT, of course the list must always have exactly one element. For AND
 * and OR, there can be two or more arguments.
 * </p>
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/primnodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */

public enum BoolExprType {
    AND_EXPR(" and "),
    OR_EXPR(" or "),
    NOT_EXPR("not ");

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    private final String toString;
    
    /**
     * Constructor
     *
     * @param toString The String that is to be returned on invocation of method toString()
     */
    private BoolExprType(String toString) {
        this.toString = toString;
    }

    @Override
    public String toString() {
        return toString;
    }
    
    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (BoolExprType type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
