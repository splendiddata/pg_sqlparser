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

package com.splendiddata.sqlparser.grammartojava;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reads a Java source file and keeps track of some qualities
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public class JavaSourceReader implements Closeable {
    private final BufferedReader in;
    private String currentLine = null;
    private String priorLine;
    private int braceLevelAtLineStart;
    private int braceLevelAtLineEnd = 0;
    private boolean eof = false;
    private boolean isComment = false;
    private int parentheseLevelAtLineStart;
    private int parentheseLevelAtLineEnd = 0;

    /**
     * Constructor
     *
     * @param file The Java source file to read
     * @throws IOException if there is a problem opening the file
     */
    public JavaSourceReader(Path file) throws IOException {
        in = Files.newBufferedReader(file, Charset.forName("UTF-8"));
    }

    /**
     * Reads and interprets the next line
     *
     * @return String the next line or null at end of file
     * @throws IOException from the reader
     */
    public String readLine() throws IOException {
        if (eof) {
            return null;
        }
        
        priorLine = currentLine;
        braceLevelAtLineStart = braceLevelAtLineEnd;
        parentheseLevelAtLineStart = parentheseLevelAtLineEnd;
        
        currentLine = in.readLine();
        if (currentLine == null) {
            eof = true;
            return null;
        }
        
        int priorByte = 0;
        boolean inTextLiteral = false;
        loop: for (int b : currentLine.getBytes()) {
            if (b == '"' && priorByte != '\\') {
                inTextLiteral = !inTextLiteral;
            } else {
                if (!inTextLiteral) {
                    switch (b) {
                    case '{':
                        if (!isComment) {
                            braceLevelAtLineEnd++;
                        }
                        break;
                    case '}':
                        if (!isComment) {
                            braceLevelAtLineEnd--;
                        }
                        break;
                    case '(':
                        if (!isComment) {
                            parentheseLevelAtLineEnd++;
                        }
                        break;
                    case ')':
                        if (!isComment) {
                            parentheseLevelAtLineEnd--;
                        }
                        break;
                    case '/':
                        if (priorByte == '/') {
                            // end of line comment -> don't interpret the rest
                            break loop;
                        }
                        if (priorByte == '*') {
                            // end of comment
                            isComment = false;
                        }
                        break;
                    case '*':
                        if (priorByte == '/') {
                            isComment = true;
                        }
                        break;
                    default:
                        break;
                    }
                }
            }
            priorByte = b;
        }
        return  currentLine;
    }

    /**
     * @return String the currentLine
     */
    public final String getCurrentLine() {
        return currentLine;
    }

    /**
     * @return String the priorLine
     */
    public final String getPriorLine() {
        return priorLine;
    }

    /**
     * @return int the braceLevelAtLineStart
     */
    public final int getBraceLevelAtLineStart() {
        return braceLevelAtLineStart;
    }

    /**
     * @return int the braceLevelAtLineEnd
     */
    public final int getBraceLevelAtLineEnd() {
        return braceLevelAtLineEnd;
    }

    /**
     * @return boolean the eof
     */
    public final boolean isEof() {
        return eof;
    }

    /**
     * @return boolean the isComment
     */
    public final boolean isComment() {
        return isComment;
    }

    /**
     * @return int the parentheseLevelAtLineStart
     */
    public final int getParentheseLevelAtLineStart() {
        return parentheseLevelAtLineStart;
    }

    /**
     * @return int the parentheseLevelAtLineEnd
     */
    public final int getParentheseLevelAtLineEnd() {
        return parentheseLevelAtLineEnd;
    }

    /**
     * @see java.io.Closeable#close()     */
    @Override
    public void close() throws IOException {
        in.close();
    }
}
