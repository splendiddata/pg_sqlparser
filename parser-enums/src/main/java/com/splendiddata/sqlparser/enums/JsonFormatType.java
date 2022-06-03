/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2022
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
 * JsonBehaviorType -<br>
 * enumeration of behavior types used in JSON ON ... BEHAVIOR clause
 * <p>
 * If enum members are reordered, get_json_behavior() from ruleutils.c must be updated accordingly.
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/primenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 15
 */
public enum JsonFormatType {
    /** unspecified */
    JS_FORMAT_DEFAULT,
    /** FORMAT JSON [ENCODING ...] */
    JS_FORMAT_JSON,
    /** implicit internal format for RETURNING jsonb */
    JS_FORMAT_JSONB;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (JsonFormatType type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
