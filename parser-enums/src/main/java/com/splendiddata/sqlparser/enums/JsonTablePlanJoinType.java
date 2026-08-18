/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2026
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
 * JsonTablePlanJoinType -<br>
 * flags for JSON_TABLE join types representation
 * <p>
 * This is NOT a normal enum as values may be ORed together. So please use the defined integer values as such.
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 15
 */
public class JsonTablePlanJoinType {
    /**
     * JSTPJ_INNER = 0x01
     * 
     * @deprecated since Postgres19beta3, please use {@link #JSTP_JOIN_INNER instead
     */
    @Deprecated(since = "Postgres 19beta3", forRemoval = true)
    public static final int JSTPJ_INNER = 0x01;
    /**
     * JSTPJ_OUTER = 0x02
     * 
     * @deprecated since Postgres19beta3, please use {@link #JSTP_JOIN_OUTER instead
     */
    @Deprecated(since = "Postgres 19beta3", forRemoval = true)
    public static final int JSTPJ_OUTER = 0x02;
    /**
     * JSTPJ_CROSS = 0x04
     * 
     * @deprecated since Postgres19beta3, please use {@link #JSTP_JOIN_CROSS instead
     */
    @Deprecated(since = "Postgres 19beta3", forRemoval = true)
    public static final int JSTPJ_CROSS = 0x04;
    /**
     * JSTPJ_UNION = 0x08
     * 
     * @deprecated since Postgres19beta3, please use {@link #JSTP_JOIN_UNION instead
     */
    @Deprecated(since = "Postgres 19beta3", forRemoval = true)
    public static final int JSTPJ_UNION = 0x08;

    /**
     * JSTP_JOIN_INNER = 0x01
     * 
     * @since Postgres 19beta3
     */
    public static final int JSTP_JOIN_INNER = 0x01;

    /**
     * JSTP_JOIN_OUTER = 0x02
     * 
     * @since Postgres 19beta3
     */
    public static final int JSTP_JOIN_OUTER = 0x02;

    /**
     * JSTP_JOIN_CROSS = 0x04
     * 
     * @since Postgres 19beta3
     */
    public static final int JSTP_JOIN_CROSS = 0x04;

    /**
     * JSTP_JOIN_UNION = 0x08
     * 
     * @since Postgres 19beta3
     */
    public static final int JSTP_JOIN_UNION = 0x08;

    public final int INT_VALUE;

    private JsonTablePlanJoinType(int intValue) {
        INT_VALUE = intValue;
    }

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART = "JSTP_JOIN_INNER|JSTP_JOIN_OUTER|JSTP_JOIN_CROSS|JSTP_JOIN_UNION";

    /**
     * @throws UnsupportedOperationException
     *             in all cases
     */
    private JsonTablePlanJoinType() {
        throw new UnsupportedOperationException(
                "JsonTablePlanJoinType acts as a replacement for an enum - ise the integer values instead.");
    }

    /**
     * As the values may be ored together, this toString function shows which values were ored together, like
     * "JSTP_JOIN_INNER|JSTP_JOIN_OUTER".
     *
     * @param intVal
     *            The value to interpret
     * @return The String interpretation of the intVal
     */
    public static String toString(int intVal) {
        StringBuilder result = new StringBuilder();
        String separator = "";
        if ((intVal & JSTP_JOIN_INNER) != 0) {
            result.append("JSTP_JOIN_INNER");
            separator = "|";
        }
        if ((intVal & JSTP_JOIN_OUTER) != 0) {
            result.append(separator).append("JSTP_JOIN_OUTER");
            separator = "|";
        }
        if ((intVal & JSTP_JOIN_CROSS) != 0) {
            result.append(separator).append("JSTP_JOIN_CROSS");
            separator = "|";
        }
        if ((intVal & JSTP_JOIN_UNION) != 0) {
            result.append(separator).append("JSTP_JOIN_UNION");
        }
        return result.toString();
    }
}
