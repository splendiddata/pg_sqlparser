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

import com.splendiddata.sqlparser.structure.Node;

public class SqlParserTest {
    private static final Logger log = LogManager.getLogger(SqlParserTest.class);

    @Test
    void test1() throws IOException {
        String sql = "select column_a, column_b, count(*) as nr_rows, sum(column_d) as total from a_table "
                + "where column_c = 'some text' group by column_a, column_b order by column_a, column_b";
        log.debug("source sql = " + sql);
        SqlParser parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        for (Node obj : parser.getResult()) {
            log.debug("parsed sql = " + obj);
            log.trace(ParserUtil.stmtToXml(obj));
        }
    }

    @Test
    public void test2() throws IOException {
        String sql = "select column_a, (select max(colx) from table_x), count(*)\r\nfrom a_table";
        log.debug("source sql = " + sql);
        SqlParser parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        for (Node obj : parser.getResult()) {
            log.debug("parsed sql = " + obj);
            log.trace(ParserUtil.stmtToXml(obj));
        }
    }

    @Test
    void test3() throws IOException {
        String sql = "CREATE FOREIGN TABLE ft1 ( " + "c1 integer OPTIONS (\"param 1\" 'val1') NOT NULL, "
                + "c2 text OPTIONS (param2 'val2', param3 'val3'), "
                + "c3 date) SERVER s0 OPTIONS (delimiter ',', quote '\"', \"be quoted\" 'value');";
        log.debug("source sql = " + sql);
        SqlParser parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        for (Node obj : parser.getResult()) {
            log.debug("parsed sql = " + obj);
            log.trace(ParserUtil.stmtToXml(obj));
        }
    }

    @Test
    void typeCasts() throws IOException {
        String sql = "select '2014-09-18'::date; ";
        log.debug("source sql = " + sql);
        SqlParser parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        for (Node obj : parser.getResult()) {
            log.debug("parsed sql = " + obj);
            log.trace(ParserUtil.stmtToXml(obj));
        }
    }

    @Test
    void joins() throws IOException {
        String sql = "select a.column_a as \"A\", b.column_a as from_b, c.column_a as \"Col A From C\", d.column_a "
                + "from table_a a " + "left join table_b b on a.some_key = b.some_key and a.subkey = b.subkey "
                + "natural join table_c c " + "right outer join table_d d on d.some_key = a.some_key";
        log.debug("source sql = " + sql);
        SqlParser parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        for (Node obj : parser.getResult()) {
            log.debug("parsed sql = " + obj);
            log.trace(ParserUtil.stmtToXml(obj));
        }
    }

    @Test
    void comments() throws IOException {
        String sql = "-- select outcommented";
        log.debug("source sql = " + sql);
        SqlParser parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        Assertions.assertNull(parser.getResult());

        sql = "/* Just a lot of /* nested \r\n comment */ over\r\n\r\nseveral\rlines. */";
        log.debug("source sql = " + sql);
        parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        Assertions.assertNull(parser.getResult());

        sql = "-- select outcommented\r\nselect not_outcommented\r\n-- and outcommented again";
        log.debug("source sql = " + sql);
        parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        Assertions.assertNotNull(parser.getResult());
        for (Node obj : parser.getResult()) {
            log.debug("parsed sql = " + obj);
            log.trace(ParserUtil.stmtToXml(obj));
        }

        sql = "select col_a, -- end of line comment with /* \r\n\tcol_b -- more comment";
        log.debug("source sql = " + sql);
        parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        Assertions.assertNotNull(parser.getResult());
        for (Node obj : parser.getResult()) {
            log.debug("parsed sql = " + obj);
            log.trace(ParserUtil.stmtToXml(obj));
        }

        sql = "select '-- comment in a text literal /* is not comment' as \"/* quoted text */ -- is allowed of course\" ; -- Rest of the line ignored";
        log.debug("source sql = " + sql);
        parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        Assertions.assertNotNull(parser.getResult());
        for (Node obj : parser.getResult()) {
            log.debug("parsed sql = " + obj);
            log.trace(ParserUtil.stmtToXml(obj));
        }

        sql = "select a */* comment directy after * */ b";
        log.debug("source sql = " + sql);
        parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        Assertions.assertNotNull(parser.getResult());
        for (Node obj : parser.getResult()) {
            log.debug("parsed sql = " + obj);
            log.trace(ParserUtil.stmtToXml(obj));
        }

        sql = "select a /* comment directy before * */* b";
        log.debug("source sql = " + sql);
        parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        Assertions.assertNotNull(parser.getResult());
        for (Node obj : parser.getResult()) {
            log.debug("parsed sql = " + obj);
            log.trace(ParserUtil.stmtToXml(obj));
        }

        sql = "select a/* comment directy before + */+ b";
        log.debug("source sql = " + sql);
        parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        Assertions.assertNotNull(parser.getResult());
        for (Node obj : parser.getResult()) {
            log.debug("parsed sql = " + obj);
            log.trace(ParserUtil.stmtToXml(obj));
        }

        sql = "select a-/* some comment\n*/b";
        log.debug("source sql = " + sql);
        parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        Assertions.assertNotNull(parser.getResult());
        for (Node obj : parser.getResult()) {
            log.debug("parsed sql = " + obj);
            log.trace(ParserUtil.stmtToXml(obj));
        }
    }

    @Test
    void postgres13unicode() throws IOException {
        String sql = "SELECT U&'\\0061\\0308bc'";
        log.debug("source sql = " + sql);
        SqlParser parser = new SqlParser(new StringReader(sql));
        Assertions.assertTrue(parser.parse());
        for (Node obj : parser.getResult()) {
            log.debug("parsed sql = " + obj);
            log.trace(ParserUtil.stmtToXml(obj));
        }
    }
}
