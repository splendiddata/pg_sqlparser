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
 * Inspired on /postgresql-9.4.1/src/include/catalog/pg_class.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 2.1.0
 */
public enum ReplicaIdentityType {

    /** default selection for replica identity (primary key or nothing) */
    REPLICA_IDENTITY_DEFAULT('d'),
    /** no replica identity is logged for this relation */
    REPLICA_IDENTITY_NOTHING('n'),
    /** all columns are loged as replica identity */
    REPLICA_IDENTITY_FULL('f'),
    /**
     * an explicitly chosen candidate key's columns are used as identity; will still be set if the index has been
     * dropped, in that case it has the same meaning as 'd'
     */
    REPLICA_IDENTITY_INDEX('i');
    
    /**
     * In Postgres only the char value is used
     */
    public final char originalCharacterValue;

    private ReplicaIdentityType(char originalCharacterValue) {
        this.originalCharacterValue = originalCharacterValue;
    }

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (ReplicaIdentityType type : values()) {
            format.append(separator).append(type);
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
