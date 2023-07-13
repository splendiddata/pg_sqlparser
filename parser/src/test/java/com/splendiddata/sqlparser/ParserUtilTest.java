/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2023
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

package com.splendiddata.sqlparser;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.splendiddata.sqlparser.structure.Node;
import com.splendiddata.sqlparser.structure.SelectStmt;

class ParserUtilTest {
    @Test
    void testIdentifierToString() {
        String identifier = "identifier";
        String expected = "identifier";
        String actual = ParserUtil.identifierToSql(identifier);
        Assertions.assertEquals(expected, actual);

        identifier = "IdEnTiFiEr";
        expected = "\"IdEnTiFiEr\"";
        actual = ParserUtil.identifierToSql(identifier);
        Assertions.assertEquals(expected, actual);

        identifier = "Iden\"ti\"fier";
        expected = "\"Iden\"\"ti\"\"fier\"";
        actual = ParserUtil.identifierToSql(identifier);
        Assertions.assertEquals(expected, actual);

        identifier = "Iden \"ti\" fier";
        expected = "\"Iden \"\"ti\"\" fier\"";
        actual = ParserUtil.identifierToSql(identifier);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testStmtToXml() {
        Node stmt = new SelectStmt();
        String xml = ParserUtil.stmtToXml(stmt);
        Assertions.assertEquals(
                "<ns2:selectStmt all=\"false\" nodeType=\"T_SelectStmt\" class=\"SelectStmt\" xmlns:ns2=\"parser\">\n"
                        + "    <groupDistinct>false</groupDistinct>\n" + "</ns2:selectStmt>",
                xml, "stmtToXml: " + stmt);
    }

    @Test
    public void filterTest() {
        List<String> src = Arrays.asList("a", "bb", "\\copy bladibla from 'stdin;", "f1 f2", "f3 f4", "\\.",
                "after copy");
        Predicate<String> filter = new Predicate<String>() {
            private boolean inCopyStatement = false;

            @Override
            public boolean test(String line) {
                if (inCopyStatement) {
                    if (line.startsWith("\\.")) {
                        inCopyStatement = false;
                    }
                    return false;
                }
                inCopyStatement = line.matches("(?i)^\\\\COPY");
                return true;
            }

        };
        Assertions.assertEquals(7, src.size(), "Expecting 7 src elements in " + src);
        List<String> tgt = src.stream().filter(line -> filter.test(line)).collect(Collectors.toList());
        Assertions.assertEquals(7, src.size(), "Expecting 7 tgt elements in " + tgt);
    }
}
