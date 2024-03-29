/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2023
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
 * JsonValueType -<br>
 * representation of JSON item type in IS JSON predicate
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/primenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 15
 */
public enum JsonValueType {
    /** IS JSON [VALUE] */
    JS_TYPE_ANY(""),
    /** IS JSON OBJECT */
    JS_TYPE_OBJECT(" object"),
    /** IS JSON ARRAY */
    JS_TYPE_ARRAY(" array"),
    /** IS JSON SCALAR */
    JS_TYPE_SCALAR(" scalar");

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    /**
     * The string to display when regenerating statements
     */
    private final String text;

    /**
     * Constructor
     *
     * @param text
     *            The text for the toString() method
     */
    private JsonValueType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (JsonValueType type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
