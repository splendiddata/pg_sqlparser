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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.splendiddata.sqlparser.enums.Severity;
import com.splendiddata.sqlparser.structure.ErrCode;
import com.splendiddata.sqlparser.structure.ErrDetail;
import com.splendiddata.sqlparser.structure.ErrHint;
import com.splendiddata.sqlparser.structure.Location;

/**
 * Abstract base class for programs that were supposed to run in C. It contains some methods that normally would be
 * present in the C compiler.
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public class AbstractCProgram implements SqlParserErrorReporter {
    private static final Logger log = LogManager.getLogger(AbstractCProgram.class);
    public static final int ERRORDATA_STACK_SIZE = 5;
    public int errordata_stack_depth = 0;

    private SqlParserErrorReporter errorReporter = this;

    long strtoul(char[] buffer, int startOffset, int radix) {
        StringBuilder str = new StringBuilder();

        int i = startOffset;
        int effectiveRadix = radix;

        /*
         * skip leading whitespace
         */
        while (i < buffer.length) {
            switch (buffer[i]) {
            case ' ':
            case '\n':
            case '\t':
            case 0x0B:
            case '\f':
            case '\r':
                i++;
                continue;
            default:
                break;
            }
            break;
        }

        /*
         * Check for a sign
         */
        if (i >= buffer.length) {
            return 0L;
        }
        if (buffer[i] == '-' || buffer[i] == '+') {
            str.append(buffer[i++]);
        }

        /*
         * radix 0 and 16 have a special meaning: if the input starts with '0x' the effective radix will be 16, if the
         * input starts with '0', the effective radix will be 8, otherwise the effective radix will be 10.
         */
        if (radix == 0 || radix == 16) {
            /*
             * Do we have a leading zero?
             */
            if (i >= buffer.length) {
                return 0L;
            }
            if (buffer[i] == '0') {
                /*
                 * Is the leading zero followed by 'x'
                 */
                if (++i >= buffer.length) {
                    return 0L;
                }
                if (buffer[i] == 'x' || buffer[i] == 'X') {
                    /*
                     * The string starts with "0x". So if the effectiveRadix will be 16.
                     */
                    effectiveRadix = 16;
                    i++;
                } else {
                    /*
                     * A leading zero is not followed by an 'x', so the effective radix will be 8
                     */
                    effectiveRadix = 8;
                }
            }
        }
        if (effectiveRadix == 0) {
            effectiveRadix = 10;
        }

        while (i < buffer.length && ((effectiveRadix <= 10 && buffer[i] >= '0' && buffer[i] < '0' + effectiveRadix)
                || (effectiveRadix > 10 && ((buffer[i] >= '0' && buffer[i] <= '9')
                        || (buffer[i] >= 'A' && buffer[i] < 'A' + effectiveRadix - 10)
                        || (buffer[i] >= 'a' && buffer[i] < 'a' + effectiveRadix - 10))))) {
            str.append(buffer[i++]);
        }

        return Long.parseLong(str.toString(), effectiveRadix);
    }

    /**
     * Returns a formatted message
     *
     * @param format
     * @param args
     * @return String
     */
    static String errmsg(String format, Object... args) {
        return String.format(format, args);
    }

    /**
     * Report an error
     *
     * @param error
     * @param errcodeSyntaxError
     * @param errmsg
     * @param objects
     */
    void ereport(Severity error, Object... objects) {
        SqlParserErrorData errorData = new SqlParserErrorData().setSeverity(error);
        for (Object obj : objects) {
            if (obj instanceof ErrCode) {
                errorData.setErrorCode((ErrCode) obj);
            } else if (obj instanceof String) {
                errorData.setErrorText((String) obj);
            } else if (obj instanceof Location) {
                if (((Location) obj).begin != null) {
                    errorData.setErrorOffset(Long.valueOf(((Location) obj).begin.getOffset()));
                }
            } else if (obj instanceof ErrHint) {
                errorData.setErrorHint((ErrHint) obj);
            } else if (obj instanceof ErrDetail) {
                errorData.setErrorDetail((ErrDetail) obj);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("report error: " + errorData);
        }
        errorReporter.reportError(errorData);
    }

    public ErrHint errhint(String fmt, Object... args) {
        return new ErrHint(fmt, args);
    }

    /**
     * Returns the offset of arg at or after the startOffset in the buffer or -1 if not found.
     *
     * @param buffer
     * @param arg
     * @return int the offset of arg in the specified text or -1 if not found
     */
    int strstr(char[] buffer, int startOffset, String arg) {
        int n = buffer.length - arg.length();
        char[] chars = arg.toCharArray();
        loop: for (int i = startOffset; i < n; i++) {
            for (int j = 0; j < chars.length; j++) {
                if (buffer[i + j] != chars[j]) {
                    continue loop;
                }
            }
            return i;
        }
        return -1;
    }

    /**
     * Translates to return arg1.compareTo(arg2);
     *
     * @param arg1
     * @param arg2
     * @return int see {@link String#compareTo(String)}
     */
    int strcmp(String arg1, String arg2) {
        return arg1.compareTo(arg2);
    }

    /**
     * @return SqlParserErrorReporter the errorReporter
     */
    public SqlParserErrorReporter getErrorReporter() {
        return errorReporter;
    }

    /**
     * @param errorReporter
     *            the errorReporter to set
     */
    public void setErrorReporter(SqlParserErrorReporter errorReporter) {
        if (errorReporter == null) {
            throw new IllegalArgumentException("errorReporter may not be set to null");
        }
        this.errorReporter = errorReporter;
    }

    /**
     * @see com.splendiddata.sqlparser.SqlParserErrorReporter#reportError(com.splendiddata.sqlparser.SqlParserErrorData)
     *
     * @param toReport
     *            The error that is to be reported
     */
    @Override
    public void reportError(SqlParserErrorData toReport) {
        if (log.isDebugEnabled()) {
            log.error(toReport, new Exception("stacktrace"));
        } else {
            log.error(toReport);
        }
    }
}
