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
 * JsonTableColumnType -&lt;nr&gt;
 *      enumeration of JSON_TABLE column types
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 15
 */
public enum JsonTableColumnType {
    JTC_FOR_ORDINALITY,
    JTC_REGULAR,
    JTC_EXISTS,
    JTC_FORMATTED,
    JTC_NESTED;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;
    
    public String toString() {
        switch (this) {
        case JTC_EXISTS:
            return "exists";
        case JTC_FORMATTED:
            return "";
        case JTC_FOR_ORDINALITY:
            return "for ordinality";
        case JTC_NESTED:
            return "nested";
        case JTC_REGULAR:
            return "";
        default:
            return "????? please implement " + this.name() + " in " + this.getClass().getName() + ".toString() ?????";
        }
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (JsonTableColumnType type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
