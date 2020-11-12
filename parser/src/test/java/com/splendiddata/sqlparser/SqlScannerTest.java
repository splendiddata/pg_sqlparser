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
import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.splendiddata.sqlparser.structure.core_yyscan_t;

/**
 * Tests some aspects of the SqlScanner
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public class SqlScannerTest {
    private static final Logger log = LogManager.getLogger(SqlScannerTest.class);

    @Test
    void test1() throws IOException {
        String sql = "select * from table_a";
        SqlScanner scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());

        checkToken(ScanKeyword.SELECT, scanner.yylex());
        checkToken('*', scanner.yylex());
        checkToken(ScanKeyword.FROM, scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("table_a", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());
    }

    @Test
    public void test2() throws IOException {
        String sql = "select col_a, COL_b, tb.col_c from table_a ta, table_b tb where ta.col_c = tb.col_c";
        SqlScanner scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());

        checkToken(ScanKeyword.SELECT, scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("col_a", scanner.getLVal().toString());
        checkToken(',', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("col_b", scanner.getLVal().toString());
        checkToken(',', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("tb", scanner.getLVal().toString());
        checkToken('.', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("col_c", scanner.getLVal().toString());
        checkToken(ScanKeyword.FROM, scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("table_a", scanner.getLVal().toString());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("ta", scanner.getLVal().toString());
        checkToken(',', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("table_b", scanner.getLVal().toString());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("tb", scanner.getLVal().toString());
        checkToken(ScanKeyword.WHERE, scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("ta", scanner.getLVal().toString());
        checkToken('.', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("col_c", scanner.getLVal().toString());
        checkToken('=', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("tb", scanner.getLVal().toString());
        checkToken('.', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("col_c", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());
    }

    @Test
    void test3() throws IOException {
        String sql = "select col_a, COL_b, tb.col_c \r\nfrom table_a ta, \n\ttable_b tb where ta.col_c = tb.col_c";
        SqlScanner scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());

        checkToken(ScanKeyword.SELECT, scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("col_a", scanner.getLVal().toString());
        checkToken(',', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("col_b", scanner.getLVal().toString());
        checkToken(',', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("tb", scanner.getLVal().toString());
        checkToken('.', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("col_c", scanner.getLVal().toString());
        checkToken(ScanKeyword.FROM, scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("table_a", scanner.getLVal().toString());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("ta", scanner.getLVal().toString());
        checkToken(',', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("table_b", scanner.getLVal().toString());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("tb", scanner.getLVal().toString());
        checkToken(ScanKeyword.WHERE, scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("ta", scanner.getLVal().toString());
        checkToken('.', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("col_c", scanner.getLVal().toString());
        checkToken('=', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("tb", scanner.getLVal().toString());
        checkToken('.', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("col_c", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());
    }

    @Test
    void singleQuoteLiteral() throws IOException {
        /* With a trailing space */
        String sql = "'a' ";
        SqlScanner scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());

        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("a", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        /* Ending in a literal */
        sql = "'a'";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("a", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        /* Single quote escape */
        sql = "'with ''ESCAPED'' text'";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("with 'ESCAPED' text", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        /* two subsquent literals */
        sql = "'a' 'b'";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("a", scanner.getLVal().toString());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("b", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());
    }

    @Test
    void unicodeEscaped() throws IOException {
        String sql = "U&'\\0061\\0308bc\\\\defg'";
        SqlScanner scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("äbc\\defg", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        sql = "U&'\\0441\\043B\\043E\\043D'";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("слон", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        sql = "U&'d\\0061t\\+000061'";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("data", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        sql = "U&'dx0061xxx+000061' uescape 'x'";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("daxa", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        sql = "U&'dx0061xxx+000061' uescape 'x' U&'d|0061|||+000061' uescape '|'";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("daxa", scanner.getLVal().toString());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("da|a", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        sql = "U&'dx0061xxx+000061' uescape 'xy' U&'d|0061|||+000061' uescape '|'";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("da|a", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        sql = "SELECT U&'d\\0061t\\+000061' AS U&\"d\\0061t\\+000061\"";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SELECT, scanner.yylex());
        Assertions.assertEquals("select", scanner.getLVal());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("data", scanner.getLVal().toString());
        checkToken(ScanKeyword.AS, scanner.yylex());
        Assertions.assertEquals("as", scanner.getLVal().toString());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("data", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        sql = "SELECT U&'wrong: +0061' UESCAPE '+'";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SELECT, scanner.yylex());
        Assertions.assertEquals("select", scanner.getLVal());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        checkToken(ScanKeyword.EOF, scanner.yylex());
        
        sql = "U&'wrong: \\+2FFFFF'";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("wrong: Q+2FFFFFE", scanner.getLVal());
        checkToken(ScanKeyword.EOF, scanner.yylex());
    }

    @Test
    void doubleQuoteLiteral() throws IOException {
        /* With a trailing space */
        String sql = "\"a\" ";
        SqlScanner scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());

        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("a", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        /* Ending in a literal */
        sql = "\"a\"";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("a", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        /* Single quote escape */
        sql = "\"with \"\"ESCAPED\"\" text\"";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("with \"ESCAPED\" text", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        /* two subsquent literals */
        sql = "\"a\" \"b\"";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("a", scanner.getLVal().toString());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("b", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());
    }

    @Test
    void testSlashStartComent() throws IOException {
        /*
         * Normal comment
         */
        String sql = "/* just comment */";
        SqlScanner scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        /*
         * Apparently Postgres supports nested slash-star comment
         */
        sql = "/* some /* nested */ comment */";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        /*
         * Newlines should't make a difference in slash-star comment
         */
        sql = "/* comment \r\n with linefeed */";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        /*
         * Divide by slash immediately followed by slash-start
         */
        sql = "a//* comment */b";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("a", scanner.getLVal().toString());
        checkToken('/', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("b", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        /*
         * Divide by slash immediately followed by slash-start
         */
        sql = "a//* /* nested */ comment */b";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("a", scanner.getLVal().toString());
        checkToken('/', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("b", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        /*
         * Sslash-start comment immediately followed by "divide by slash"
         */
        sql = "a/* comment *//b";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("a", scanner.getLVal().toString());
        checkToken('/', scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("b", scanner.getLVal().toString());
        checkToken(ScanKeyword.EOF, scanner.yylex());
    }

    @Test
    void operators() throws IOException {
        String sql = "a != 1";
        SqlScanner scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        checkToken(ScanKeyword.NOT_EQUALS, scanner.yylex());
        checkToken(ScanKeyword.ICONST, scanner.yylex());
        Assertions.assertEquals(1, scanner.getLVal());
        checkToken(ScanKeyword.EOF, scanner.yylex());

        sql = "a >= .25 and b <= \"C\"";
        scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("a", scanner.getLVal());
        checkToken(ScanKeyword.GREATER_EQUALS, scanner.yylex());
        checkToken(ScanKeyword.FCONST, scanner.yylex());
        Assertions.assertEquals(".25", scanner.getLVal());
        checkToken(ScanKeyword.AND, scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("b", scanner.getLVal());
        checkToken(ScanKeyword.LESS_EQUALS, scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("C", scanner.getLVal());
        checkToken(ScanKeyword.EOF, scanner.yylex());
    }

    @Test
    void times() throws IOException {
        String sql = "SELECT timestamp with time zone '20011227 040506+08'";
        SqlScanner scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SELECT, scanner.yylex());
        checkToken(ScanKeyword.TIMESTAMP, scanner.yylex());
        checkToken(ScanKeyword.WITH_LA, scanner.yylex());
        checkToken(ScanKeyword.TIME, scanner.yylex());
        checkToken(ScanKeyword.ZONE, scanner.yylex());
        checkToken(ScanKeyword.SCONST, scanner.yylex());
        Assertions.assertEquals("20011227 040506+08", scanner.getLVal());
        checkToken(ScanKeyword.EOF, scanner.yylex());
    }

    @Test
    void opAlias() throws IOException {
        String sql = "select a as op from table_a op";
        SqlScanner scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        checkToken(ScanKeyword.SELECT, scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("a", scanner.getLVal());
        checkToken(ScanKeyword.AS, scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("op", scanner.getLVal());
        checkToken(ScanKeyword.FROM, scanner.yylex());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("table_a", scanner.getLVal());
        checkToken(ScanKeyword.IDENT, scanner.yylex());
        Assertions.assertEquals("op", scanner.getLVal());
        checkToken(ScanKeyword.EOF, scanner.yylex());
    }

    @Test
    void justChecking() throws IOException {
        String sql = "select 1, 1234567890123456, \"Some Case Sensitive Text\", some_function(col), aDate::date \r\n" //
                + ", 'ŵǎʇ ᴚᶛᶉḙ ṫẻꝅễṉṥ' " + "from who_knows \r\n" //
                + "/* with some/* -- nested -- */ and \r\n" //
                + "multiline comment */" //
                + "where a =< 1 " //
                + "and (select max(b) from c) </* comment */ n;" //
                + "\r\n" //
                + "\r\n" //
                + "-- some start of line comment with a /* just to annoy the scanner\r\n" //
                + "select case when a = 5 then 2 default 3 end case, timestamp with timezone '2014-09-10 15:53:26';";
        SqlScanner scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        for (int token = scanner.yylex(); token > 0; token = scanner.yylex()) {
            log.debug(token + " = "
                    + (ScanKeyword.fromValue(token) == null ? "'" + (char) token + "'" : ScanKeyword.fromValue(token))
                    + ", lvalue = " + scanner.getLVal());
        }
    }

    @Test
    void postgres13unicode() throws IOException {
        String sql = "SELECT U&'\\0061\\0308bc'";
        SqlScanner scanner = new SqlScanner(new StringReader(sql), new core_yyscan_t());
        for (int token = scanner.yylex(); token > 0; token = scanner.yylex()) {
            log.debug(token + " = "
                    + (ScanKeyword.fromValue(token) == null ? "'" + (char) token + "'" : ScanKeyword.fromValue(token))
                    + ", lvalue = " + scanner.getLVal());
        }
    }

    private static void checkToken(ScanKeyword expected, int actual) {
        ScanKeyword got = ScanKeyword.fromValue(actual);
        if (!expected.equals(got)) {
            StringBuilder msg = new StringBuilder().append("expected ScanKeyword.").append(expected).append(" (=")
                    .append(expected.getValue()).append(") but got: ");
            if (got == null) {
                msg.append(actual).append(" (='").append((char) actual).append("').");
            } else {
                msg.append("ScanKeyword.").append(got).append(" (=").append(got.getValue()).append(")");
            }
            Assertions.fail(msg.toString());
        }
    }

    private static void checkToken(char expected, int actual) {
        if (expected != actual) {
            ScanKeyword got = ScanKeyword.fromValue(actual);
            StringBuilder msg = new StringBuilder().append("expected '").append(expected).append("' but got: ");
            if (got == null) {
                msg.append(actual).append(" (='").append((char) actual).append("').");
            } else {
                msg.append("ScanKeyword.").append(got).append(" (=").append(got.getValue()).append(")");
            }
            Assertions.fail(msg.toString());
        }
    }
}
