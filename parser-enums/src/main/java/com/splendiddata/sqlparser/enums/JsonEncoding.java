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
 * JsonEncoding -<br>
 * representation of JSON ENCODING clause
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/primenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 15
 */
public enum JsonEncoding {
    /** unspecified */
    JS_ENC_DEFAULT(""),
    JS_ENC_UTF8("encoding UTF8"),
    JS_ENC_UTF16("encoding UTF16"),
    JS_ENC_UTF32("encoding UTF32");

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;
    
    private final String text; 

    private JsonEncoding(String text) {
         this.text=text;
    }
    
    public String toString() {
        return text;
    }
    
    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (JsonEncoding type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
