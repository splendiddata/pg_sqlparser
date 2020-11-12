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
 * Inheritance options obtained from: /postgresql-9.3.4/src/include/nodes/primnodes.h#InhOption
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 * @deprecated This enum is not used any more since version 5 (Postgres version 10)
 */
@Deprecated
public enum InhOption {
    
    /** Do NOT scan child tables */
    INH_NO,
    
    /** DO scan child tables */
    INH_YES,
    
    /** Use current SQL_inheritance option */
    INH_DEFAULT;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (InhOption type : values()) {
            format.append(separator).append(type);
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
