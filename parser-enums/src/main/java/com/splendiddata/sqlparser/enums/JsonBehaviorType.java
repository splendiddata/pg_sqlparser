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
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 15
 */
public enum JsonBehaviorType {
    JSON_BEHAVIOR_NULL,
    JSON_BEHAVIOR_ERROR,
    JSON_BEHAVIOR_EMPTY,
    JSON_BEHAVIOR_TRUE,
    JSON_BEHAVIOR_FALSE,
    JSON_BEHAVIOR_UNKNOWN,
    JSON_BEHAVIOR_EMPTY_ARRAY,
    JSON_BEHAVIOR_EMPTY_OBJECT,
    JSON_BEHAVIOR_DEFAULT;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;
    
    public String toString() {
        switch (this) {
        case JSON_BEHAVIOR_DEFAULT:
            return "default";
        case JSON_BEHAVIOR_EMPTY:
            return "empty";
        case JSON_BEHAVIOR_EMPTY_ARRAY:
            return "????? please implement " + this.name() + " in " + this.getClass().getName() + ".toString() ?????";
        case JSON_BEHAVIOR_EMPTY_OBJECT:
            return "????? please implement " + this.name() + " in " + this.getClass().getName() + ".toString() ?????";
        case JSON_BEHAVIOR_ERROR:
            return "error";
        case JSON_BEHAVIOR_FALSE:
            return "false";
        case JSON_BEHAVIOR_NULL:
            return "null";
        case JSON_BEHAVIOR_TRUE:
            return "true";
        case JSON_BEHAVIOR_UNKNOWN:
            return "????? please implement " + this.name() + " in " + this.getClass().getName() + ".toString() ?????";
        default:
            return "????? please implement " + this.name() + " in " + this.getClass().getName() + ".toString() ?????";
        }
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (JsonBehaviorType type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
