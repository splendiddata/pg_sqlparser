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
    /** JSTPJ_INNER = 0x01 */
    public static final int JSTPJ_INNER = 0x01;
    /** JSTPJ_OUTER = 0x02 */
    public static final int JSTPJ_OUTER = 0x02;
    /** JSTPJ_CROSS = 0x04 */
    public static final int JSTPJ_CROSS = 0x04;
    /** JSTPJ_UNION = 0x08 */
    public static final int JSTPJ_UNION = 0x08;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART = "JSTPJ_INNER|JSTPJ_OUTER|JSTPJ_CROSS|JSTPJ_UNION";

    /**
     * @throws UnsupportedOperationException
     *             in all cases
     */
    private JsonTablePlanJoinType() {
        throw new UnsupportedOperationException(
                "JsonTablePlanJoinType acts as a replacement for an enum - ise the integer values instead.");
    }
}
