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

import java.io.IOException;
import java.io.Reader;
import java.util.ListIterator;

import com.splendiddata.sqlparser.structure.core_yyscan_t;
import com.splendiddata.sqlparser.structure.List;
import com.splendiddata.sqlparser.structure.Node;
import com.splendiddata.sqlparser.structure.RawStmt;

/**
 * Wrapper class around PgSqlParser, which demands an odd constructor
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public class SqlParser {
    private final PgSqlParser parser;

    /**
     * Constructor
     * <p>
     * Possible scanner or parser errors are logged into the log4j logger
     * </p>
     *
     * @param reader
     *            The Reader that will provide the sql statements
     */
    public SqlParser(Reader reader) {
        core_yyscan_t something = new core_yyscan_t();
        parser = new PgSqlParser(new SqlScanner(reader, something), something);
        //        parser.errorVerbose = true;
    }

    /**
     * Constructor
     *
     * @param reader
     *            The Reader that will provide the sql statements
     * @param errorReporter
     *            The SqlParserErrorReporter that will receive parser errors (if any)
     */
    public SqlParser(Reader reader, SqlParserErrorReporter errorReporter) {
        core_yyscan_t something = new core_yyscan_t();
        SqlScanner scanner = new SqlScanner(reader, something);
        scanner.setErrorReporter(errorReporter);
        parser = new PgSqlParser(scanner, something);
        //        parser.errorVerbose = true;
        parser.setErrorReporter(errorReporter);
    }

    /**
     * Parses the sql data that is passed in the reader in the constructor.
     * 
     * @return boolean true if everything worked fine.
     * @throws IOException
     *             if the reader threw one
     * @see com.splendiddata.sqlparser.PgSqlParser#parse()
     */
    public boolean parse() throws IOException {
        parser.setErrorReported(false);
        return parser.parse() && !parser.isErrorReported();
    }

    /**
     * Returns the parse result - a list of statements - after successful execution of the {@link #parse()} method.
     * 
     * @return List&lt;Node&gt; The parsed statements - one Node per statement
     * @see com.splendiddata.sqlparser.AbstractParser#getResult()
     */
    public List<Node> getResult() {
        List<Node> result = parser.getResult();
        if (result != null) {
            for (ListIterator<Node> it = result.listIterator(); it.hasNext();) {
                Node stmt = it.next();
                /*
                 * The RawStmt is just a wrapper that has no meaning for us. So let's strip it off.
                 */
                if (stmt instanceof RawStmt) {
                    it.set(((RawStmt)stmt).stmt);
                }
            }
        }
        return parser.getResult();
    }

    /**
     * @see com.splendiddata.sqlparser.ParserUtil#getParserVersion()
     * @return String the parser version
     */
    public static final String getParserVersion() {
        return ParserUtil.getParserVersion();
    }

    /**
     * @see com.splendiddata.sqlparser.ParserUtil#getForPosgresVersion()
     * @return String the Postgres version on which this parser version is based
     */
    public static String getForPosgresVersion() {
        return ParserUtil.getForPosgresVersion();
    }

    /**
     * @see com.splendiddata.sqlparser.ParserUtil#getPostgresMajorVersion()
     * @return int The major version of the Postgres version on which this parser version is based
     */
    public static int getPostgresMajorVersion() {
        return ParserUtil.getPostgresMajorVersion();
    }
}
