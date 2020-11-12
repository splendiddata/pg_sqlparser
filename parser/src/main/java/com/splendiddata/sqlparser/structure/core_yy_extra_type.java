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

import com.splendiddata.sqlparser.enums.BackslashQuoteType;

/**
 * Copied from /postgresql-9.3.4/src/include/parser/scanner.h#core_yy_extra_type
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public class core_yy_extra_type {
    /**
     * The string the scanner is physically scanning. We keep this mainly so that we can cheaply compute the offset of
     * the current token (yytext).
     */
    public char[] scanbuf;
    public int scanbuflen;

    /**
     * literalbuf is used to accumulate literal values when multiple rules are needed to parse a single literal. Call
     * startlit() to reset buffer to empty, addlit() to add text. NOTE: the string in literalbuf is NOT necessarily
     * null-terminated, but there always IS room to add a trailing null at offset literallen. We store a null only when
     * we need it.
     */
    public StringBuilder literalbuf = new StringBuilder();
    /**
     * start cond. before end quote
     * 
     * @since 8.0 - Postgres version 13
     */
    public int state_before_str_stop;
    /** depth of nesting in slash-star comments */
    public int xcdepth;
    /** current $foo$ quote start string */
    public String dolqstart;
    /**
     * one-element stack for PUSH_YYLLOC()
     * 
     * @since 8.0 - Postgres version 13
     */
    public long save_yylloc;

    /** first part of UTF16 surrogate pair for Unicode escapes */
    public int utf16_first_part;

    /** state variables for literal-lexing warnings */
    public boolean warn_on_first_escape;
    public boolean saw_non_ascii;

    public static final boolean standard_conforming_strings = true;
    public static final BackslashQuoteType backslash_quote = BackslashQuoteType.BACKSLASH_QUOTE_SAFE_ENCODING;

    /**
     * TODO Please insert some explanation here
     *
     */
    public void initialize() {
        saw_non_ascii = false;
        warn_on_first_escape = false;
        utf16_first_part = 0;
        dolqstart = null;
        xcdepth = 0;
        literalbuf.setLength(0);
    }
}
