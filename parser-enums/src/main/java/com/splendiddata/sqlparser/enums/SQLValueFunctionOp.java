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
 * SQLValueFunction - parameterless functions with special grammar productions
 * <p>
 * The SQL standard categorizes some of these as &lt;datetime value function&gt; and others as &lt;general value specification&gt;.
 * We call 'em SQLValueFunctions for lack of a better term. We store type and typmod of the result so that some code
 * doesn't need to know each function individually, and because we would need to store typmod anyway for some of the
 * datetime functions. Note that currently, all variants return non-collating datatypes, so we do not need a collation
 * field; also, all these functions are stable.
 * </p>
 * <p>
 * Copied from: /postgresql-10rc1/src/include/nodes/primnodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
public enum SQLValueFunctionOp {
    SVFOP_CURRENT_DATE("current_date"),
    SVFOP_CURRENT_TIME("current_time"),
    SVFOP_CURRENT_TIME_N("current_time"),
    SVFOP_CURRENT_TIMESTAMP("current_timestamp"),
    SVFOP_CURRENT_TIMESTAMP_N("current_timestamp"),
    SVFOP_LOCALTIME("local_time"),
    SVFOP_LOCALTIME_N("local_time"),
    SVFOP_LOCALTIMESTAMP("local_timestamp"),
    SVFOP_LOCALTIMESTAMP_N("local_time"),
    SVFOP_CURRENT_ROLE("current_role"),
    SVFOP_CURRENT_USER("current_user"),
    SVFOP_USER("user"),
    SVFOP_SESSION_USER("session_user"),
    SVFOP_CURRENT_CATALOG("current_catalog"),
    SVFOP_CURRENT_SCHEMA("current_schema");

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    /**
     * The look if the value in sql
     */
    private final String stringRepresentation;

    /**
     * Constructor
     *
     * @param stringRepresentation
     *            to be used when re-generating sql
     */
    private SQLValueFunctionOp(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    /**
     * @see java.lang.Enum#toString()
     *
     * @return String the enum value in the way it is supposed to be used in sql
     */
    @Override
    public String toString() {
        return stringRepresentation;
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (SQLValueFunctionOp op : values()) {
            format.append(separator).append(op.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
