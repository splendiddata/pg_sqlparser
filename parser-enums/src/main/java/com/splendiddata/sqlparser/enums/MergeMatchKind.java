/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2024
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
 * MergeAction
 * <p>
 * Transformed representation of a WHEN clause in a MERGE statement
 * <p>
 * Copied from /postgresql-17beta1/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 17
 */

public enum MergeMatchKind {
    MERGE_WHEN_MATCHED,
    MERGE_WHEN_NOT_MATCHED_BY_SOURCE,
    MERGE_WHEN_NOT_MATCHED_BY_TARGET;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    @Override
    public String toString() {
        switch (this) {
        case MERGE_WHEN_MATCHED:
            return "when matched";
        case MERGE_WHEN_NOT_MATCHED_BY_SOURCE:
            return "when not matched by source";
        case MERGE_WHEN_NOT_MATCHED_BY_TARGET:
            return "when not matched";
        default:
            return "????? Please implement " + this.getClass().getName() + ".toString() for value " + this.name()
                    + " ?????";
        }
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (MergeMatchKind type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
