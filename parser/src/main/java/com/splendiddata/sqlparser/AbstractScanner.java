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
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.splendiddata.sqlparser.enums.Severity;
import com.splendiddata.sqlparser.structure.ErrCode;
import com.splendiddata.sqlparser.structure.ErrDetail;
import com.splendiddata.sqlparser.structure.Location;
import com.splendiddata.sqlparser.structure.Position;
import com.splendiddata.sqlparser.structure.core_yyscan_t;
import com.splendiddata.sqlparser.structure.core_yy_extra_type;

/**
 * Base class for the generated {@link com.splendiddata.sqlparser.SqlScanner} class.
 * <p>
 * The lex config from which the SqlScanner class is generated is originally intended to generate C code. This class
 * provides some methods and fields so that the Java world can live with it.
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public abstract class AbstractScanner extends AbstractCProgram implements com.splendiddata.sqlparser.PgSqlParser.Lexer {
    private final Logger log = LogManager.getLogger(getClass());

    /**
     * as defined in /postgresql-9.3.4/src/include/pg_config_manual.h
     */
    public static final int NAMEDATALEN = 64;
    /**
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     */
    public static final int YY_END_OF_BUFFER_CHAR = 0;

    /**
     * Default pattern for USCONST and UIDENT values
     */
    private static final Pattern UNICODE_ESCAPE_PATTTERN = Pattern
            .compile("(\\\\\\\\|\\\\\\p{XDigit}{4}|\\\\\\+\\p{XDigit}{6})");
    private static final String UNICODE_ESCAPE_TEMPLATE = "(EE|E\\p{XDigit}{4}|E\\+\\p{XDigit}{6})";

    /**
     * Characters that should be escaped when used in a pattern
     */
    private static final Pattern PATTERN_ESCAPE_CHAR = Pattern
            .compile("[\\\\\\\"\\(\\)\\+\\-\\$\\^\\?\\!\\<\\>\\|\\,\\{\\}\\*]");

    public core_yy_extra_type yyextra = new core_yy_extra_type();
    public int yyloc = 0;
    public boolean escape_string_warning = true;
    public Object yylval;
    Position position;

    /**
     * Sometimes we try to combine two tokens into one. If that attempt fails, the second token is still valid and must
     * be returned on the next invocation of yylex(). So if nextToken != 0, a prior invocation failed to translate a
     * second token into something useful in the context of that invocation.
     */
    private int nextToken = 0;
    private Object nextyylval;
    private Position nextPosition;

    /**
     * @see com.splendiddata.sqlparser.PgSqlParser.Lexer#yylex()
     *      <p>
     *      This method is a wrapper around the actual parser's yylex() method. It tricks some translation into the
     *      result - copied from src/backend/parser/parser.c method base_yylex().
     *
     * @return int the next token
     * @throws IOException
     *             from the input file
     */
    @Override
    public int yylex() throws IOException {
        yyextra.initialize();
        yylval = null;
        int result;
        if (nextToken == 0) {
            result = scan();
        } else {
            result = nextToken;
            nextToken = 0;
            yylval = nextyylval;
            nextyylval = null;
            position = nextPosition;
            nextPosition = null;
        }
        ScanKeyword keyword = ScanKeyword.fromValue(result);

        /*
         * Copied from: /postgresql-9.3.4/src/backend/parser/parser.c
         * 
         * Intermediate filter between parser and core lexer (core_yylex in scan.l).
         * 
         * The filter is needed because in some cases the standard SQL grammar requires more than one token lookahead.
         * We reduce these cases to one-token lookahead by combining tokens here, in order to keep the grammar LALR(1).
         * 
         * Using a filter is simpler than trying to recognize multiword tokens directly in scan.l, because we'd have to
         * allow for comments between the words. Furthermore it's not clear how to do it without re-introducing scanner
         * backtrack, which would cost more performance than this filter layer does.
         * 
         * The filter also provides a convenient place to translate between the core_YYSTYPE and YYSTYPE representations
         * (which are really the same thing anyway, but notationally they're different).
         */
        if (keyword != null) {
            switch (keyword) {
            case NOT:
                Object currentyylval = yylval;
                Position currentPosition = position;
                nextToken = scan();
                ScanKeyword nextKeyword = ScanKeyword.fromValue(nextToken);
                if (nextKeyword == null) {
                    nextPosition = position;
                    position = currentPosition;
                    nextyylval = yylval;
                    yylval = currentyylval;
                } else {
                    switch (nextKeyword) {
                    case BETWEEN:
                    case IN_P:
                    case LIKE:
                    case ILIKE:
                    case SIMILAR:
                        keyword = ScanKeyword.NOT_LA;
                        result = keyword.getValue();
                        position = currentPosition;
                        nextToken = nextKeyword.value;
                        break;
                    default:
                        nextPosition = position;
                        position = currentPosition;
                        nextyylval = yylval;
                        yylval = currentyylval;
                        break;
                    }
                }
                break;
            case NULLS_P:
                currentyylval = yylval;
                currentPosition = position;
                nextToken = scan();
                nextKeyword = ScanKeyword.fromValue(nextToken);
                if (nextKeyword == null) {
                    nextPosition = position;
                    position = currentPosition;
                    nextyylval = yylval;
                    yylval = currentyylval;
                } else {
                    switch (nextKeyword) {
                    case FIRST_P:
                    case LAST_P:
                        keyword = ScanKeyword.NULLS_LA;
                        result = keyword.getValue();
                        position = currentPosition;
                        break;
                    default:
                        nextPosition = position;
                        position = currentPosition;
                        nextyylval = yylval;
                        yylval = currentyylval;
                        break;
                    }
                }
                break;
            case WITH:
                currentyylval = yylval;
                currentPosition = position;
                nextToken = scan();
                nextKeyword = ScanKeyword.fromValue(nextToken);
                if (nextKeyword == null) {
                    nextPosition = position;
                    position = currentPosition;
                    nextyylval = yylval;
                    yylval = currentyylval;
                } else {
                    switch (nextKeyword) {
                    case TIME:
                    case ORDINALITY:
                        keyword = ScanKeyword.WITH_LA;
                        result = keyword.getValue();
                        position = currentPosition;
                        break;
                    case UNIQUE:
                        keyword = ScanKeyword.WITH_LA_UNIQUE;
                        result = keyword.getValue();
                        position = currentPosition;
                        break;
                    default:
                        nextPosition = position;
                        position = currentPosition;
                        nextyylval = yylval;
                        yylval = currentyylval;
                        break;
                    }
                }
                break;

            case WITHOUT:
                /* Replace WITHOUT by WITHOUT_LA if it's followed by TIME */
                currentyylval = yylval;
                currentPosition = position;
                nextToken = scan();
                nextKeyword = ScanKeyword.fromValue(nextToken);
                if (nextKeyword == null) {
                    nextPosition = position;
                    position = currentPosition;
                    nextyylval = yylval;
                    yylval = currentyylval;
                } else {
                    switch (nextKeyword) {
                    case TIME:
                        keyword = ScanKeyword.WITHOUT_LA;
                        result = keyword.getValue();
                        position = currentPosition;
                        //                        nextToken = 0;
                        break;
                    default:
                        nextPosition = position;
                        position = currentPosition;
                        nextyylval = yylval;
                        yylval = currentyylval;
                        break;
                    }
                }
                break;
            case Op:
                if ("op".equalsIgnoreCase(yylval.toString())) {
                    keyword = ScanKeyword.IDENT;
                    result = keyword.getValue();
                }
                break;
            case USCONST:
            case UIDENT:
                /*
                 * Since 8.0 - Postgres version 13
                 */
                if (ScanKeyword.USCONST.equals(keyword)) {
                    result = ScanKeyword.SCONST.getValue();
                } else {
                    result = ScanKeyword.IDENT.getValue();
                }

                Pattern p = UNICODE_ESCAPE_PATTTERN;
                String escapeChar = "\\\\";

                currentyylval = yylval;
                currentPosition = position;
                nextToken = scan();
                nextPosition = position;
                nextyylval = yylval;
                nextKeyword = ScanKeyword.fromValue(nextToken);
                if (ScanKeyword.UESCAPE.equals(nextKeyword)) {
                    nextToken = scan();
                    nextKeyword = ScanKeyword.fromValue(nextToken);
                    if (ScanKeyword.SCONST.equals(nextKeyword)) {
                        if (yylval.toString().length() == 1) {
                            if (yylval.toString().matches("[0-9A-Fa-f\\+\\\\\\\"\\s]")) {
                                yyerror(new Location(position), "Invalid unicode escape character: \"" + yylval + "\"");
                            } else {
                                escapeChar = escapeForPattern(yylval.toString());
                                p = Pattern.compile(UNICODE_ESCAPE_TEMPLATE.replace("E", escapeChar));
                            }
                        } else {
                            yyerror(new Location(position), "Invalid unicode escape character: \"" + yylval + "\"");
                        }
                        nextToken = scan();
                        nextPosition = position;
                        nextyylval = yylval;
                    } else {
                        yyerror(new Location(position), "UESCAPE must be followed by a simple string literal");
                    }
                }

                Matcher m = p.matcher(currentyylval.toString());
                StringBuilder sb = new StringBuilder();
                while (m.find()) {
                    String gr = m.group();
                    if (gr.length() == 2) {
                        m.appendReplacement(sb, escapeChar);
                    } else {
                        try {
                            m.appendReplacement(sb, Character.toString(Integer.parseInt(gr.substring(1), 16)));
                        } catch (IllegalArgumentException e) {
                            yyerror(new Location(currentPosition), e.getMessage());
                            m.appendReplacement(sb, Pattern.quote(gr));
                        }
                    }
                }
                m.appendTail(sb);
                yylval = sb.toString();
                position = currentPosition;
                break;
            default:
                break;
            }
        }

        if (yylval == null) {
            if (keyword == null) {
                yylval = Character.valueOf((char) result);
            } else {
                yylval = keyword;
            }
        }

        if (log.isDebugEnabled()) {
            StringBuilder msg = new StringBuilder("@yylex() = ").append(result).append(" = ");
            if (keyword == null) {
                msg.append('\'').append((char) result).append('\'');
            } else {
                msg.append(keyword);
            }
            msg.append(", lvalue = ").append(yylval);
            log.debug(msg.toString());
        }
        return result;
    }

    /**
     * Adds a backslash in front of the escapeChar if necessary when used in a regular expression
     *
     * @param escapeChar
     *            The character to check
     * @return String The original escapeChar or the escapeChar preceded with a backslash if a regular expression would
     *         like it that way, think of "\", "+", "-", "(", ")" ...
     */
    private String escapeForPattern(String escapeChar) {
        if (PATTERN_ESCAPE_CHAR.matcher(escapeChar).matches()) {
            return "\\" + escapeChar;
        }
        return escapeChar;
    }

    /**
     * To be implemented by the generated scanner
     *
     * @return int token value
     * @throws IOException
     *             from the reader
     */
    abstract int scan() throws IOException;

    /**
     * Returns the read value
     * 
     * @see com.splendiddata.sqlparser.PgSqlParser.Lexer#getLVal()
     *
     * @return Object
     */
    @Override
    public Object getLVal() {
        return yylval;
    }

    /**
     * @see com.splendiddata.sqlparser.PgSqlParser.Lexer#getStartPos()
     *
     * @return Position the start position of the last read token
     */
    @Override
    public Position getStartPos() {
        return position;
    }

    /**
     * The scanner doesn't keep an end position. So getEndPos will always return null
     * 
     * @see com.splendiddata.sqlparser.PgSqlParser.Lexer#getEndPos()
     *
     * @return null
     */
    @Override
    public Position getEndPos() {
        return null;
    }

    /**
     * @see com.splendiddata.sqlparser.PgSqlParser.Lexer#yyerror(Location, String)
     *
     * @param loc
     *            The error location
     * @param errorText
     *            The text to be reported
     */
    @Override
    public void yyerror(Location loc, String errorText) {
        SqlParserErrorData errorData = new SqlParserErrorData().setErrorText(errorText);
        if (loc != null && loc.begin != null) {
            errorData.setErrorOffset(Long.valueOf(loc.begin.getOffset()));
        }
        getErrorReporter().reportError(errorData);
    }

    /**
     * scanner_errposition Report a lexer or grammar error cursor position, if possible.
     * <p>
     * This is expected to be used within an ereport() call. The return value is a dummy (always 0, in fact).
     * </p>
     * <p>
     * Note that this can only be used for messages emitted during raw parsing (essentially, scan.l and gram.y), since
     * it requires the yyscanner struct to still be available.
     * </p>
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     * 
     * @param location
     * @param yyscanner
     *            unused
     * @return int length (counted in wchars) of a multibyte string (not necessarily NULL terminated)
     */
    int scanner_errposition(int location, core_yyscan_t yyscanner) {
        if (location < 0) {
            return 0; /* no-op if location is unknown */
        }

        /* Convert byte offset to character number */
        return pg_mbstrlen_with_len(yyextra.scanbuf, location) + 1;
    }

    /**
     * scanner_yyerror Report a lexer or grammar error.
     * <p>
     * The message's cursor position is whatever YYLLOC was last set to, ie, the start of the current token if called
     * within core_yylex(), or the most recently lexed token if called from the grammar. This is OK for syntax error
     * messages from the Bison parser, because Bison parsers report error as soon as the first unparsable token is
     * reached. Beware of using yyerror for other purposes, as the cursor position might be misleading!
     * </p>
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     * 
     * @param message
     * @param yyscanner
     *            unused
     */
    void scanner_yyerror(String message, core_yyscan_t yyscanner) {

        if (yyloc > yyextra.scanbuf.length) {
            ereport(Severity.ERROR, ErrCode.ERRCODE_SYNTAX_ERROR,
                    /* translator: %s is typically the translation of "syntax error" */
                    errmsg("%s at end of input", message), lexer_errposition());
        } else {
            ereport(Severity.ERROR, ErrCode.ERRCODE_SYNTAX_ERROR,
                    /* translator: first %s is typically the translation of "syntax error" */
                    errmsg("%s at or near \"%s\"", message, Integer.valueOf(yyloc)), lexer_errposition());
        }
    }

    /**
     * Apparently not actually used. Perhaps because the input reader already returns UTF-16 characters instead of
     * bytes. The current implementation just throws {@link java.lang.UnsupportedOperationException}
     *
     * @return Location
     * @throws java.lang.UnsupportedOperationException.UnsupportedOperationException
     */
    com.splendiddata.sqlparser.structure.Location lexer_errposition() {
        return new Location(position);
    }

    /**
     * Called after parsing is done to clean up after scanner_init(). The current implementation does nothing.
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     * 
     * @param yyscanner
     *            unused
     */
    void scanner_finish(core_yyscan_t yyscanner) {
        // empty
    }

    /**
     * Adds a character to the literal buffer
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     *
     * @param ychar
     *            the character to be added
     * @param yyscanner
     *            unused
     */
    void addlitchar(char ychar, core_yyscan_t yyscanner) {
        yyextra.literalbuf.append(ychar);
    }

    /**
     * Return the current content of the literal buffer
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     * 
     * @param yyscanner
     *            unused
     * @return String the content of the literal buffer
     */
    String litbufdup(core_yyscan_t yyscanner) {
        return yyextra.literalbuf.toString();
    }

    /**
     * Checks if a numeric string is small enough to put it into a 32-bit integer or not. If it is smll enough,
     * {@link com.splendiddata.sqlparser.ScanKeyword#ICONST} will be returned. Otherwise
     * {@link com.splendiddata.sqlparser.ScanKeyword#FCONST}
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     *
     * @param text
     *            Numeric text
     * @return int ScanKeyword.ICONST.value or ScanKeyword.FCONST.value depending on the actual numeric value of text
     */
    int process_integer_literal(String text) {
        yylval = text;

        BigInteger val = new BigInteger(text);
        if (val.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            return ScanKeyword.FCONST.value;
        }
        yylval = Integer.valueOf(val.intValue());
        return ScanKeyword.ICONST.value;
    }

    /**
     * Returns the integer value of the character interpreted hexadecimal. The character must be one of 0..9, A..F or
     * a..f.
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     *
     * @param c
     * @return int The integer value of the hexadecimally interpreted character
     */
    int hexval(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 0xA;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 0xA;
        }
        ereport(Severity.ERROR, "invalid hexadecimal digit");
        return 0; /* not reached */
    }

    /**
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     *
     * @param c
     * @return
     */
    static boolean is_utf16_surrogate_first(char c) {
        return (c >= 0xD800 && c <= 0xDBFF);
    }

    /**
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     *
     * @param c
     * @return
     */
    static boolean is_utf16_surrogate_second(char c) {
        return (c >= 0xDC00 && c <= 0xDFFF);
    }

    /**
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     *
     * @param first
     * @param second
     * @return
     */
    static char surrogate_pair_to_codepoint(int first, char second) {
        return (char) ((((char) first & 0x3FF) << 10) + 0x10000 + (second & 0x3FF));
    }

    /**
     * Adds a character to yyextra.literalbuf
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     *
     * @param c
     *            The character to add
     * @param yyscanner
     *            unused
     */
    void addunicode(char c, core_yyscan_t yyscanner) {
        yyextra.literalbuf.append(c);
    }

    /**
     * is 'escape' acceptable as Unicode escape character (UESCAPE syntax) ?
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     * 
     * @param escape
     * @return boolean
     */
    boolean check_uescapechar(char escape) {
        if (isxdigit(escape) || escape == '+' || escape == '\'' || escape == '"' || scanner_isspace(escape)) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if the escape char is a hexadecimal digit (0..9, A..F or a..f)
     *
     * @param escape
     * @return boolean true if escape is a hexadecimal digit
     */
    private static boolean isxdigit(char escape) {
        switch (escape) {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 7:
        case 8:
        case 9:
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f':
            return true;
        default:
            return false;
        }
    }

    /**
     * like litbufdup, but handle unicode escapes. Well, in fact just returns whatever is in the escape argument.
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     * 
     * @param escape
     *            The character to be returned
     * @param yyscanner
     *            unused
     * @return String escape
     */
    static String litbuf_udeescape(String escape, core_yyscan_t yyscanner) {
        return escape;
    }

    /**
     * Returns the escaped value of character c.
     * <ul>
     * <li>'b' translates to '\b'</li>
     * <li>'f' translates to '\f'</li>
     * <li>'n' translates to '\n'</li>
     * <li>'r' translates to '\r'</li>
     * <li>etcetera</li>
     * </ul>
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     *
     * @param c
     *            The character to interpret
     * @param yyscanner
     *            unused
     * @return char The escaped value of the character.
     */
    char unescape_single_char(char c, core_yyscan_t yyscanner) {
        switch (c) {
        case 'b':
            return '\b';
        case 'f':
            return '\f';
        case 'n':
            return '\n';
        case 'r':
            return '\r';
        case 't':
            return '\t';
        default:
            /* check for backslash followed by non-7-bit-ASCII */
            if (c == '\0' || IS_HIGHBIT_SET(c)) {
                yyextra.saw_non_ascii = true;
            }

            return c;
        }
    }

    /**
     * <p>
     * copied from /postgresql-9.3.4/src/backend/parser/scan.c
     * </p>
     *
     * @param ychar
     * @param yyscanner
     *            unused
     */
    void check_string_escape_warning(char ychar, core_yyscan_t yyscanner) {
        if (ychar == '\'') {
            if (yyextra.warn_on_first_escape && escape_string_warning)
                ereport(Severity.WARNING, ErrCode.ERRCODE_NONSTANDARD_USE_OF_ESCAPE_CHARACTER,
                        errmsg("nonstandard use of \\' in a string literal"),
                        errhint("Use '' to write quotes in strings, or use the escape string syntax (E'...')."),
                        lexer_errposition());
            yyextra.warn_on_first_escape = false; /* warn only once per string */
        } else if (ychar == '\\') {
            if (yyextra.warn_on_first_escape && escape_string_warning)
                ereport(Severity.WARNING, ErrCode.ERRCODE_NONSTANDARD_USE_OF_ESCAPE_CHARACTER,
                        errmsg("nonstandard use of \\\\ in a string literal"),
                        errhint("Use the escape string syntax for backslashes, e.g., E'\\\\'."), lexer_errposition());
            yyextra.warn_on_first_escape = false; /* warn only once per string */
        } else
            check_escape_warning(yyscanner);
    }

    /**
     * returns the length (counted in wchars) of a multibyte string (not necessarily NULL terminated). Well, just
     * returns limit.
     * <p>
     * copied from /postgresql-9.3.4/src/backend/utils/mb/mbutils.c
     * </p>
     * 
     * @param scanbuf
     *            unused
     * @param limit
     *            will be returned
     * @return int limit
     */
    int pg_mbstrlen_with_len(char[] scanbuf, int limit) {
        return limit;
    }

    /**
     * Probably unused because the lexer returns UTF-16 characters
     *
     * @param yyscanner
     *            unsued
     */
    void check_escape_warning(core_yyscan_t yyscanner) {
        if (yyextra.warn_on_first_escape && escape_string_warning)
            ereport(Severity.WARNING, ErrCode.ERRCODE_NONSTANDARD_USE_OF_ESCAPE_CHARACTER,
                    errmsg("nonstandard use of escape in a string literal"),
                    errhint("Use the escape string syntax for escapes, e.g., E'\\r\\n'."), lexer_errposition());
        yyextra.warn_on_first_escape = false; /* warn only once per string */
    }

    /**
     * scanner_isspace() --- return TRUE if flex scanner considers char whitespace
     * <p>
     * This should be used instead of the potentially locale-dependent isspace() function when it's important to match
     * the lexer's behavior.
     * </p>
     * <p>
     * In principle we might need similar functions for isalnum etc, but for the moment only isspace seems needed.
     * </p>
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/parser/scansup.c
     * </p>
     * 
     * @param ch
     *            the character to check
     * @return true if ch appears to be a whitespace character (' ', '\t', '\n', '\r', '\f')
     */
    boolean scanner_isspace(char ch) {
        /* This must match scan.l's list of {space} characters */
        if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r' || ch == '\f')
            return true;
        return false;
    }

    /**
     * Checks if the topmost bit in character c is set
     *
     * @param c
     * @return boolean true if c < 0 (meaning high-bit set)
     */
    boolean IS_HIGHBIT_SET(char c) {
        return (c & 0x80) != 0;
    }

    /**
     * Just wraps the character into a String
     *
     * @param escape
     * @param yyscanner
     *            unsused
     * @return String String.valueOf(escape)
     */
    String litbuf_udeescape(char escape, core_yyscan_t yyscanner) {
        return String.valueOf(escape);
    }

    /**
     * Resets yyextra.literalbuf
     */
    void startlit() {
        yyextra.literalbuf.setLength(0);
    }

    /**
     * Truncates the ident to the maximum identifier length that is supported by Postgres, as defined in NAMEDATALEN (=
     * 64) in /postgresql-9.3.4/src/include/pg_config_manual.h
     *
     * @param ident
     * @return String the possibly truncated ident
     */
    String truncate_identifier(String ident) {
        if (ident.length() <= NAMEDATALEN) {
            return ident;
        }
        return ident.substring(0, NAMEDATALEN);
    }

    /**
     * Report a syntax error with the specified message
     *
     * @param message
     *            The message that is to be reported
     */
    void yyerror(String message) {
        ereport(Severity.ERROR, ErrCode.ERRCODE_SYNTAX_ERROR, errmsg(message), lexer_errposition());
    }

    /**
     * Unused. Would be used if we were working on a byte[] instead of char[]
     *
     * @param i
     */
    void ADVANCE_YYLLOC(int i) {
        if (log.isDebugEnabled()) {
            log.debug("ADVANCE_YYLLOC(i=" + i + ")");
        }
    }

    /**
     * Converts the input text to lower case. If truncate, the text is truncated to NAMEDATALEN (=64) characters.
     * <p>
     * Freely interpreted from /postgresql-9.3.4/src/backend/parser/scansup.c#downcase_truncate_identifier
     * </p>
     *
     * @param text
     * @param trucate
     * @return String The text converted to lower case
     */
    String downcase_truncate_identifier(String text, boolean truncate) {
        if (truncate && text.length() > NAMEDATALEN) {
            return text.toLowerCase().substring(0, NAMEDATALEN);
        }
        return text.toLowerCase();
    }

    /**
     * Would be used if byte arrays would have been used instead of character arrays
     *
     * @param literalbuf
     * @param literallen
     * @param b
     */
    void pg_verifymbstr(StringBuilder literalbuf, int literallen, boolean b) {
        throw new UnsupportedOperationException(
                "com.splendiddata.sqlparser.AbstractScanner.pg_verifymbstr(StringBuilder, int, boolean)");
    }

    /**
     * Wraps the detail in an ErrDetail object
     *
     * @param detail
     *            String to wrap into the ErrDetail
     * @return ErrDetail with the wrapped detail
     */
    ErrDetail errdetail(String detail) {
        return new ErrDetail(detail);
    }

    /**
     * Just returns the eof token value
     * 
     * @return int ScanKeyword.EOF.getValue() (= 0)
     */
    int yyterminate() {
        // return ScanKeyword.EOF.getValue();
        /* 
         * Since Bison 3.6.0 ScanKeyword.EOF is called ScanKeyword.YYEOF,
         * so returning the actual value zero now as conditional compilation
         * is not supported in Java 
         */
        return 0;
    }

    /**
     * Translates the text in the buffer that starts at startOffset with the specified length into a ScannerKeyword, or
     * null if not found
     *
     * @param keywrd
     * @return ScannerKeyword The keyword that is identified by the string or null if not found.
     */
    ScanKeyword ScanKeywordLookup(String keyword) {
        return ScanKeyword.fromName(keyword);
    }

    /**
     * Just returns a zero
     *
     * @return int 0
     */
    int pg_get_client_encoding() {
        log.debug("pg_get_client_encoding() just returns zero");
        return 0;
    }

    /**
     * Just returns true
     *
     * @param pg_get_client_encoding
     * @return boolean true
     * @throws UnsupportedOperationException
     */
    boolean PG_ENCODING_IS_CLIENT_ONLY(int pg_get_client_encoding) {
        log.debug(() -> "PG_ENCODING_IS_CLIENT_ONLY(pg_get_client_encoding=" + pg_get_client_encoding + ")");
        return true;
    }

    /**
     * Adds the text to yyextra.literalbuf
     *
     * @param text
     */
    void addlit(String text) {
        yyextra.literalbuf.append(text);
    }
}
