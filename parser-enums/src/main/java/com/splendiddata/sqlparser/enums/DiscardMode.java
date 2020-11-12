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
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 2.1.0
 */

public enum DiscardMode {
    DISCARD_ALL("all"),
    DISCARD_PLANS("plans"),
    DISCARD_TEMP("temporary"),
    DISCARD_SEQUENCES("sequences");

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    /**
     * String that is to be returned when the toString() method is invoked
     */
    private final String toString;

    /**
     * Constructor
     *
     * @param toString
     */
    private DiscardMode(String toString) {
        this.toString = toString;
    }

    @Override
    public String toString() {
        return toString;
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (DiscardMode type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
