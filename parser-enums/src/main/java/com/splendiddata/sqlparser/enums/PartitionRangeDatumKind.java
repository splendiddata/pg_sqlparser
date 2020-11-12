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
 * PartitionRangeDatum - one of the values in a range partition bound
 * <p>
 * This can be MINVALUE, MAXVALUE or a specific bounded value.
 * </p>
 * <p>
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
public enum PartitionRangeDatumKind {
    /** less than any other value */
    PARTITION_RANGE_DATUM_MINVALUE(-1, "for values from"),

    /** a specific (bounded) value */
    PARTITION_RANGE_DATUM_VALUE(0, "for values in"),

    /** greater than any other value */
    PARTITION_RANGE_DATUM_MAXVALUE(1, "to");

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    public final int VALUE;

    /**
     * This will be returned by toString()
     */
    private final String toString;

    /**
     * Constructor
     * 
     * @param value
     *            The int value
     * @param toString
     */
    private PartitionRangeDatumKind(int value, String toString) {
        this.VALUE = value;
        this.toString = toString;
    }

    @Override
    public String toString() {
        return toString;
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (PartitionRangeDatumKind type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
