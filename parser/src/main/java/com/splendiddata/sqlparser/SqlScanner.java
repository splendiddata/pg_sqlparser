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

package com.splendiddata.sqlparser;

import java.io.Reader;

import com.splendiddata.sqlparser.structure.core_yyscan_t;

/**
 * Wrapper class around the postgres sql scanner
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
public class SqlScanner extends PgSqlScanner {
    /**
     * The EOF literal in the Lexer appears to depend on the Bison version. So lets define the literal here just to be sure.
     */
    @SuppressWarnings("hiding")
    public static final int EOF = 0;

    /**
     * Constructor
     *
     * @param in
     *            The reader that is supposed to read sql statements
     */
    public SqlScanner(Reader in) {
        super(in, new core_yyscan_t());
    }

    /**
     * Constructor
     *
     * @param in
     *            Reader providing sql statements in a text format
     * @param yyscanner
     *            Internal parser thing that is needed in the C implementation of the parser. In Java it will not be
     *            used, but it is referred to in several locations in the parser
     */
    public SqlScanner(Reader in, core_yyscan_t yyscanner) {
        super(in, yyscanner);
    }

}
