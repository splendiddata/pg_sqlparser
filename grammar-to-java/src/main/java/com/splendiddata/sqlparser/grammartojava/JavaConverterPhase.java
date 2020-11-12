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

package com.splendiddata.sqlparser.grammartojava;

/**
 * Defines the section in the generated Java source file that we're in.
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public enum JavaConverterPhase {
    DETERMINING_CLASS_NAME,
    IN_YYACTION,
    NORMAL_PROCESSING,
    IN_LARGE_TABLE_SECTION,
    IN_TRAILER_SECTION
}
