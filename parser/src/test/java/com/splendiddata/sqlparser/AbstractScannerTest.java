/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2025
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

import com.splendiddata.sqlparser.structure.ErrorSaveContext;

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

    @Test
    public void testPg_strtoint_safe() {
        Assertions.assertEquals(0, pg_strtoint_safe("0", new ErrorSaveContext()), "0");
        Assertions.assertEquals(-1, pg_strtoint_safe("-1", new ErrorSaveContext()), "-1");
        Assertions.assertEquals(Integer.MAX_VALUE, pg_strtoint_safe("" + Integer.MAX_VALUE, new ErrorSaveContext()), "" + Integer.MAX_VALUE);
        Assertions.assertEquals(Integer.MIN_VALUE, pg_strtoint_safe("" + Integer.MIN_VALUE, new ErrorSaveContext()), "" + Integer.MIN_VALUE);
        Assertions.assertEquals(2748, pg_strtoint_safe("0xABC", new ErrorSaveContext()), "0xABC");
        Assertions.assertEquals(-43, pg_strtoint_safe("-0X2b", new ErrorSaveContext()), "-0X2b");
        Assertions.assertEquals(83, pg_strtoint_safe("0o123", new ErrorSaveContext()), "0o123");
        Assertions.assertEquals(-83, pg_strtoint_safe("-0O123", new ErrorSaveContext()), "-0O123");
        Assertions.assertEquals(219, pg_strtoint_safe("0B11011011", new ErrorSaveContext()), "0B11011011"); 
        Assertions.assertEquals(-219, pg_strtoint_safe("-0b11_011_011", new ErrorSaveContext()), "-0b11_011_011");
        setErrorReported(false);
        pg_strtoint_safe("abc def", new ErrorSaveContext());
        Assertions.assertTrue(isErrorReported(), "expection error to be reported for \"abc def\"");
        setErrorReported(false);
        pg_strtoint_safe("9876543210", new ErrorSaveContext());
        Assertions.assertTrue(isErrorReported(), "expection error to be reported for \"9876543210\"");
        setErrorReported(false);
        pg_strtoint_safe("abcdef", new ErrorSaveContext());
        Assertions.assertTrue(isErrorReported(), "expection error to be reported for \"abcdef\"");
        setErrorReported(false);
        pg_strtoint_safe("0Xabcdefg", new ErrorSaveContext());
        Assertions.assertTrue(isErrorReported(), "expection error to be reported for \"0Xabcdefg\"");
        setErrorReported(false);
        pg_strtoint_safe("0B123", new ErrorSaveContext());
        Assertions.assertTrue(isErrorReported(), "expection error to be reported for \"0B123\"");
    }
}
