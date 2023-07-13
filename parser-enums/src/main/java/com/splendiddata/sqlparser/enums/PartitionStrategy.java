/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2023
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
 * Internal codes for partitioning strategies
 * <p>
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
public enum PartitionStrategy {
    /**
     * @since 6.0 - Postgres version 11
     */
    PARTITION_STRATEGY_HASH('h'),
    PARTITION_STRATEGY_LIST('l'),
    PARTITION_STRATEGY_RANGE('r');

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    /**
     * The character value that is used in Postgres internally
     */
    public final char VALUE;

    /**
     * Constructor
     * 
     * @param value
     *            The value in Posgres
     * @param toString
     */
    private PartitionStrategy(char value) {
        this.VALUE = value;
    }
    
    public String toString() {
        switch (this) {
        case PARTITION_STRATEGY_HASH:
            return "hash";
        case PARTITION_STRATEGY_LIST:
            return "list";
        case PARTITION_STRATEGY_RANGE:
            return "range";
        default:
            return "????? Please implement " + this.getClass().getName() + ".toString() value " + this.name() + " ??????";
        }
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (PartitionStrategy type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
