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
 * Date and time words.
 * <p>
 * Loosely inspired upon /postgresql-9.3.4/src/include/utils/datetime.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public enum TemporalWord {
    RESERV(""),
    YEAR("year"),
    MONTH("month"),
    DAY("day"),
    JULIAN("julian"),
    TZ("timezome"),
    DTZ("timezome dst"),
    DTZMOD("???????   dtzmod   ???????"),
    IGNORE_DTF("???????   ignore_dtf   ???????"),
    AMPM("???????   ampm   ???????"),
    HOUR("hour"),
    MINUTE("minute"),
    SECOND("second"),
    MILLISECOND("millisecond"),
    MICROSECOND("microsecond"),
    DOY("day of year"),
    DOW("day of week"),
    UNITS("???????   units   ???????"),
    ADBC("???????   adbc   ???????"),
    /** these are only for relative dates */
    AGO("???????   ago   ???????"),
    ABS_BEFORE("???????   abs_before   ???????"),
    ABS_AFTER("???????   abs_after   ???????"),
    /** generic fields to help with parsing */
    ISODATE("???????   isodate   ???????"),
    ISOTIME("???????   isotime   ???????"),
    /** these are only for parsing intervals */
    WEEK("week"),
    DECADE("decade"),
    CENTURY("century"),
    MILLENNIUM("millenium"),
    /** reserved for unrecognized string values */
    UNKNOWN_FIELD("???????   unknown_field   ???????");

    /**
     * An integer value that is supposed to remain constant over releases
     */
    public final int CONSTANT;

    private final String toString;

    /**
     * Constructor
     *
     * @param constant
     */
    private TemporalWord(String stringValue) {
        this.CONSTANT = 1 << ordinal();
        toString = stringValue;
    }

    /**
     * Returns the CONSTANT value
     *
     * @return int
     */
    public int getConstant() {
        return CONSTANT;
    }

    @Override
    public String toString() {
        return toString;
    }

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (TemporalWord type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
