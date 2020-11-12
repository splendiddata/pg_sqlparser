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

package com.splendiddata.sqlparser.structure;


/**
 * Some error codes that appeared to be used in the parser
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */

public enum ErrCode {
    ERRCODE_SYNTAX_ERROR,
    ERRCODE_FEATURE_NOT_SUPPORTED,
    ERRCODE_INVALID_PARAMETER_VALUE,
    ERRCODE_WINDOWING_ERROR,
    ERRCODE_NONSTANDARD_USE_OF_ESCAPE_CHARACTER,
    ERRCODE_INVALID_ESCAPE_SEQUENCE,
    ERRCODE_RESERVED_NAME,
    /** @since 6.0 - Postgres version 11 */
    ERRCODE_DUPLICATE_OBJECT
}
