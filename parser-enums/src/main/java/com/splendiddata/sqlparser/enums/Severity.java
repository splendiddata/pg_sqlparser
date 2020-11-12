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
 * Logging severities obtained from /postgresql-9.3.4/src/include/utils/elog.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public enum Severity {
    /**
     * Debugging messages, in categories of decreasing detail.
     */
    DEBUG5(10),
    DEBUG4(11),
    DEBUG3(12),
    DEBUG2(13),
    /** used by GUC debug_* variables */
    DEBUG1(14),
    /**
     * Server operational messages; sent only to server log by default.
     */
    LOG(15),
    /**
     * Client communication problems; same as LOG for server reporting, but never sent to client.
     */
    COMMERROR(16),
    /**
     * Messages specifically requested by user (eg VACUUM VERBOSE output); always sent to client regardless of
     * client_min_messages, but by default not sent to server log.
     */
    INFO(17),
    /**
     * Helpful messages to users about query operation; sent to client and server log by default.
     */
    NOTICE(18),
    /**
     * Warnings. NOTICE is for expected messages like implicit sequence creation by SERIAL. WARNING is for unexpected
     * messages.
     */
    WARNING(19),
    /**
     * user error - abort transaction; return to known state
     */
    ERROR(20);

    public final int CONSTANT;

    /**
     * Constructor
     *
     * @param constant
     */
    private Severity(int constant) {
        CONSTANT = constant;
    }

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (Severity type : values()) {
            format.append(separator).append(type);
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
