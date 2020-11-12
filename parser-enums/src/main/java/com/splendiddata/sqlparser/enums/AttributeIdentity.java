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
 * Copied from: /postgresql-10rc1/src/include/catalog/pg_attribute.h
 * 
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
public enum AttributeIdentity {
    ATTRIBUTE_IDENTITY_ALWAYS('a'),
    ATTRIBUTE_IDENTITY_BY_DEFAULT('d');

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    public final char VALUE;

    private AttributeIdentity(char value) {
        this.VALUE = value;
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (AttributeIdentity type : values()) {
            format.append(separator).append(type);
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
