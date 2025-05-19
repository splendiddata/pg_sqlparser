/*
 * Copyright (c) Splendid Data Product Development B.V. 2025
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
 * Copied from /postgresql-18beta1/src/include/nodes/parsenodes.h
 * <p>
 * ReturningOptionKind - Possible kinds of option in RETURNING WITH(...) list
 * <p>
 *
 * Currently, this is used only for specifying OLD/NEW aliases.
 *
 * @author Splendid Data Product Development B.V.
 * @since 18.0
 */
public enum ReturningOptionKind {
    /** specify alias for OLD in RETURNING */
    RETURNING_OPTION_OLD("old"),
    /** specify alias for NEW in RETURNING */
    RETURNING_OPTION_NEW("new");

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    /**
     * The character value that is used in Postgres internally
     */
    public final String VALUE;

    /**
     * Constructor
     * 
     * @param value
     *            The value in Postgres
     * @param toString
     */
    private ReturningOptionKind(String value) {
        this.VALUE = value;
    }

    @Override
    public String toString() {
        return VALUE;
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (ReturningOptionKind type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
