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
 * @since 0.0.1
 */
public enum TransactionStmtKind {
    TRANS_STMT_BEGIN("begin transaction"),
    /** semantically identical to BEGIN */
    TRANS_STMT_START("start transaction"),
    TRANS_STMT_COMMIT("commit"),
    TRANS_STMT_ROLLBACK("rollback"),
    TRANS_STMT_SAVEPOINT("savepoint"),
    TRANS_STMT_RELEASE("release savepoint"),
    TRANS_STMT_ROLLBACK_TO("rollback to savepoint"),
    TRANS_STMT_PREPARE("prepare transaction"),
    TRANS_STMT_COMMIT_PREPARED("commit prepared"),
    TRANS_STMT_ROLLBACK_PREPARED("rollback prepared");

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    private final String toString;

    private TransactionStmtKind(String str) {
        this.toString = str;
    }

    @Override
    public String toString() {
        return toString;
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (TransactionStmtKind type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
