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

package com.splendiddata.sqlparser.grammartojava;

/**
 * Some identifiers in the scan.l file need special treatment. The identifier name relates to the enum value name.
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public enum LexRuleSpecial implements GrammarRuleSpecialProcessing {
    operator("{operator}", new GrammarRuleSpecialProcessing() {

        /**
         * Java and C have different opinions on exchangeability of pointers, integers character arrays an Strings. So
         * some code is altered here
         *
         * @param line
         * @return String (modified?) line
         */
        @Override
        public String processLine(String line) {
            return line.replace("yyleng", "yytext().length()")
                    .replaceAll("strchr\\(\\s*(\"[^\"]*\")\\s*\\,\\s*([^)]*)", "0 <= $1.indexOf($2")
                    .replaceAll("char\\s*\\*(slashstar|dashdash)\\s*=\\s*strstr\\(yytext\\,\\s*([^)]*)\\)",
                            "int $1 = yytext().indexOf($2)")
                    .replace("if (slashstar && dashdash)", "if (slashstar >= 0 && dashdash >= 0)")
                    .replace("if (!slashstar)", "if (slashstar < 0)").replace("if (slashstar)", "if (slashstar >= 0)")
                    .replace("nchars = slashstar - yytext;", "nchars = slashstar;");
        }
    }),
    xnstart("{xnstart}", new GrammarRuleSpecialProcessing() {

        /**
         * Why lookup the keyword when we already know which one we want?
         *
         * @param line
         * @return String (modified?) line
         */
        @Override
        public String processLine(String line) {
            return line.replaceAll("int\\s+kwnum", "ScanKeyword kwnum")
                    .replace("ScanKeywordLookup(\"nchar\",", "ScanKeyword.NCHAR;").replace("yyextra->keywordlist);", "")
                    .replace("kwnum >= 0", "kwnum != null").replace("GetScanKeyword(kwnum,", "kwnum.getName();")
                    .replace("yyextra->keyword_tokens[kwnum]", "kwnum.getValue()");
        }
    }),
    identifier("{identifier}", new GrammarRuleSpecialProcessing() {

        /**
         * Replaces character array processing by String processing
         *
         * @param line
         * @return String (modified?) line
         */
        @Override
        public String processLine(String line) {
            return line.replaceAll("int\\s+kwnum", "\t\tScanKeyword kwnum")
                    .replace("ScanKeywordLookup(yytext,", "ScanKeywordLookup(yytext());")
                    .replace("yyextra->keywordlist);", "")
                    .replace("yylval->keyword = GetScanKeyword(kwnum,", "yylval = kwnum.getName();")
                    .replace("kwnum >= 0", "kwnum != null")
                    .replace("yyextra->keyword_tokens[kwnum]", "kwnum.getValue()")
                    .replace("downcase_truncate_identifier(yytext, yyleng, true)",
                            "downcase_truncate_identifier(yytext(), true)");
        }
    }),
    xufailed("{xufailed}", new GrammarRuleSpecialProcessing() {

        /**
         * In Java String manipulations will do.
         *
         * @param line
         * @return String (modified?) line
         */
        @Override
        public String processLine(String line) {
            return line.replace("downcase_truncate_identifier(yytext, yyleng, true)",
                    "downcase_truncate_identifier(yytext(), true)");
        }
    }),
    xdolq_dolqdelim("<xdolq>{dolqdelim}", new GrammarRuleSpecialProcessing() {

        /**
         * pfree is for C. Java's got a garbage collector for that
         *
         * @param line
         * @return String (modified?) line
         */
        @Override
        public String processLine(String line) {
            return line.replace("pfree(yyextra->dolqstart);", "");
        }
    }),
    param("{param}", new GrammarRuleSpecialProcessing() {

        /**
         * pfree is for C. Java's got a garbage collector for that
         *
         * @param line
         * @return String (modified?) line
         */
        @Override
        public String processLine(String line) {
            return line.replace("yylval->ival = atol(yytext + 1);", "yylval = Integer.valueOf(yytext().substring(1));")
                    .replaceAll("\\bint32\\b", "int").replace("(Node *) &escontext", "escontext")
                    .replace("{T_ErrorSaveContext}", "new ErrorSaveContext()");
        }
    }),

    /**
     * Default matches all, so must be last.
     */
    DEFAULT("", null);

    private final GrammarRuleSpecialProcessing specialProcessing;
    private final String lineIdentifier;

    /**
     * Constructor
     *
     * @param lineIdentifier
     * @param specialProcessing
     */
    private LexRuleSpecial(String lineIdentifier, GrammarRuleSpecialProcessing specialProcessing) {
        this.specialProcessing = specialProcessing;
        this.lineIdentifier = lineIdentifier;
    }

    private boolean lineMatches(String line) {
        return line.startsWith(lineIdentifier);
    }

    /**
     * @see com.splendiddata.sqlparser.grammartojava.GrammarRuleSpecialProcessing#processLine(java.lang.String)
     *
     * @param line
     *            The line from the grammar
     * @return String the processed
     */
    @Override
    public String processLine(String line) {
        if (specialProcessing == null) {
            return line;
        }
        return specialProcessing.processLine(line);
    }

    /**
     * Identifies the LexSpecialRule that is to be started with this line.
     *
     * @param line
     *            The line to be checked
     * @return LexRuleSpecial The LexRuleSpecial that is to be used from this line on
     */
    public static LexRuleSpecial identifySpecialRule(String line) {
        for (LexRuleSpecial specialRule : LexRuleSpecial.values()) {
            if (specialRule.lineMatches(line)) {
                return specialRule;
            }
        }
        return DEFAULT;
    }

}
