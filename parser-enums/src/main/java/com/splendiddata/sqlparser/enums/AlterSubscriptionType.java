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
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 */
public enum AlterSubscriptionType {
    ALTER_SUBSCRIPTION_OPTIONS,
    /**
     * @since Postgres 19beta1
     */
    ALTER_SUBSCRIPTION_SERVER,
    ALTER_SUBSCRIPTION_CONNECTION,
    /**
     * @since 14.0
     */
    ALTER_SUBSCRIPTION_SET_PUBLICATION,
    /**
     * @since 14.0
     */
    ALTER_SUBSCRIPTION_ADD_PUBLICATION,
    /**
     * @since 14.0
     */
    ALTER_SUBSCRIPTION_DROP_PUBLICATION,
    /**
     * @deprecated since Postgres 19beta1
     */
    @Deprecated(since="Postgres 19beta1", forRemoval=true)
    ALTER_SUBSCRIPTION_PUBLICATION,
    /**
     * @deprecated since Postgres 19beta1 replaced by ALTER_SUBSCRIPTION_REFRESH_PUBLICATION or ALTER_SUBSCRIPTION_REFRESH_SEQUENCES
     */
    @Deprecated(since="Postgres 19beta1", forRemoval=true)
    ALTER_SUBSCRIPTION_REFRESH,
    /**
     * @since Postgres 19beta1
     */
    ALTER_SUBSCRIPTION_REFRESH_PUBLICATION,
    /**
     * @since Postgres 19beta1
     */
    ALTER_SUBSCRIPTION_REFRESH_SEQUENCES,
    ALTER_SUBSCRIPTION_ENABLED,
    /** @since Postgres 15 */
    ALTER_SUBSCRIPTION_SKIP;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (AlterSubscriptionType type : values()) {
            format.append(separator).append(type);
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
