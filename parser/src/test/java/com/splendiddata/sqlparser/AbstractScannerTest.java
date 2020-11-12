/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
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

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Some test cases that ca be handled by just the AbstractScanner
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public class AbstractScannerTest extends AbstractScanner {

    @Test
    public void testStrtoul() {
        Assertions.assertEquals(strtoul("abc   1234 ".toCharArray(), 4, 0), 1234, "strtoul(\"abc   1234 \", 4, 0)");
        Assertions.assertEquals(strtoul("-0xfF".toCharArray(), 0, 0), -255, "strtoul(\"-0xfF\", 0, 0)");
        Assertions.assertEquals(strtoul("012387".toCharArray(), 0, 0), 0123, "strtoul(\"012387\", 0, 0)");
        Assertions.assertEquals(strtoul("123456789abcdefg".toCharArray(), 0, 16), 0x123456789abcdefL,
                "strtoul(\"123456789abcdefg\", 0, 16)");
    }

    /**
     * Doesn't do anything here.
     * 
     * @see com.splendiddata.sqlparser.enums.SqlParser.Lexer#yylex()
     *
     * @return
     * @throws IOException
     */
    @Override
    public int scan() throws IOException {
        return 0;
    }

}
