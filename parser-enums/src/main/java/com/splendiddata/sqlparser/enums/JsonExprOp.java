/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2024
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
 * JsonExprOp -<br>
 * enumeration of JSON functions using JSON path
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/primenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 15
 */
public enum JsonExprOp {
    /** JSON_VALUE() */
    JSON_VALUE_OP,
    /** JSON_QUERY() */
    JSON_QUERY_OP,
    /** JSON_EXISTS() */
    JSON_EXISTS_OP,
    /** JSON_TABLE() */
    JSON_TABLE_OP;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    public String toString() {
        switch (this) {
        case JSON_EXISTS_OP:
            return "json_exists";
        case JSON_QUERY_OP:
            return "json_query";
        case JSON_TABLE_OP:
            return "json_table";
        case JSON_VALUE_OP:
            return "json_value";
        default:
            return "????? Please implement " + this.name() + " in " + this.getClass().getName() + ".toString() ?????";
        }
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (JsonExprOp type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
