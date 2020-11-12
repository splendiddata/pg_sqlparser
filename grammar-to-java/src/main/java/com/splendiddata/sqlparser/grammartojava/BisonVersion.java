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

import java.io.PrintWriter;

/**
 * Applies some bison-version specific changes
 *
 * @author Splendid Data Product Development B.V.
 * @since 2.1
 */
public enum BisonVersion {
    BISON_VERSION_2 {
        @Override
        public boolean replacePrefixLine(String prefixLine, PrintWriter out) {
            return false;
        }

        @Override
        public void insertHeadingLines(PrintWriter out) {
            out.println("%define parser_class_name PgSqlParser");
            out.println("%define api.position.type Position");
            out.println("%define api.location.type Location");
            out.println("%define extends AbstractParser");
            out.println("%define public");
        }

        @Override
        public void initYYActionBlock(PrintWriter out, int submethodNumber) {
            out.println();
            out.print("  private int yyaction_");
            out.print(submethodNumber);
            out.println("(int yyn, YYStack yystack, int yylen) {");
            out.println("    Object yyval;");
            out.println("    Location yyloc = yylloc (yystack, yylen);");
            out.println();
            out.println("    /* If YYLEN is nonzero, implement the default value of the action:");
            out.println("       `$$ = $1'.  Otherwise, use the top of the stack.");
            out.println();
            out.println("        Otherwise, the following line sets YYVAL to garbage.");
            out.println("        This behavior is undocumented and Bison");
            out.println("        users should not rely upon it.  */");
            out.println("    if (yylen > 0) {");
            out.println("      yyval = yystack.valueAt (yylen - 1);");
            out.println("    } else {");
            out.println("       yyval = yystack.valueAt (0);");
            out.println("    }");
            out.println();
            out.println("     yy_reduce_print (yyn, yystack);");
            out.println();
            out.println("    switch (yyn) {");
        }

        @Override
        public void finishYYActionBlock(PrintWriter out) {
            out.println("    }");
            out.println();
            out.println("    yy_symbol_print (\"-> $$ =\", yyr1_[yyn], yyval, yyloc);");
            out.println();
            out.println("    yystack.pop (yylen);");
            out.println("    yylen = 0;");
            out.println();
            out.println("    /* Shift the result of the reduction.  */");
            out.println("    yyn = yyr1_[yyn];");
            out.println("    int yystate = yypgoto_[yyn - yyntokens_] + yystack.stateAt (0);");
            out.println("    if (0 <= yystate && yystate <= yylast_");
            out.println("        && yycheck_[yystate] == yystack.stateAt (0)) {");
            out.println("      yystate = yytable_[yystate];");
            out.println("    } else {");
            out.println("      yystate = yydefgoto_[yyn - yyntokens_];");
            out.println("    }");
            out.println("    yystack.push (yystate, yyval, yyloc);");
            out.println("    return YYNEWSTATE;");
            out.println("  }");
        }
    },
    BISON_VERSION_3 {
        @Override
        public boolean replacePrefixLine(String line, PrintWriter out) {
            if ("%name-prefix=\"base_yy\"".equals(line)) {
                out.println("%name-prefix \"base_yy\"");
                return true;
            }
            if ("%pure-parser".equals(line)) {
                // Parsers in Java are always "pure", so skip the line
                return true;
            }
            return false;
        }

        @Override
        public void insertHeadingLines(PrintWriter out) {
            out.println("%define parser_class_name {PgSqlParser}");
            out.println("%define api.position.type {Position}");
            out.println("%define api.location.type {Location}");
            out.println("%define extends {AbstractParser}");
            out.println("%define public");
        }

        @Override
        public void initYYActionBlock(PrintWriter out, int submethodNumber) {
            out.println();
            out.print("  private int yyaction_");
            out.print(submethodNumber);
            out.println("(int yyn, YYStack yystack, int yylen) {");
            out.println("      Object yyval;");
            out.println("      Location yyloc = yylloc (yystack, yylen);");
            out.println();
            out.println("      /* If YYLEN is nonzero, implement the default value of the action:");
            out.println("         '$$ = $1'.  Otherwise, use the top of the stack.");
            out.println();
            out.println("         Otherwise, the following line sets YYVAL to garbage.");
            out.println("         This behavior is undocumented and Bison");
            out.println("          users should not rely upon it.  */");
            out.println("      if (yylen > 0) {");
            out.println("            yyval = yystack.valueAt (yylen - 1);");
            out.println("      } else {");
            out.println("            yyval = yystack.valueAt (0);");
            out.println("      }");
            out.println();
            out.println("      yy_reduce_print (yyn, yystack);");
            out.println();
            out.println("      switch (yyn) {");
        }

        @Override
        public void finishYYActionBlock(PrintWriter out) {
            out.println("    }");
            out.println();
            out.println("    yy_symbol_print (\"-> $$ =\", yyr1_[yyn], yyval, yyloc);");
            out.println();
            out.println("    yystack.pop (yylen);");
            out.println("    yylen = 0;");
            out.println();
            out.println("    /* Shift the result of the reduction.  */");
            out.println("    int yystate = yy_lr_goto_state_ (yystack.stateAt (0), yyr1_[yyn]);");
            out.println("    yystack.push (yystate, yyval, yyloc);");
            out.println("    return YYNEWSTATE;");
            out.println("  }");
        }
    },
    BISON_VERSION_3_3 {
        @Override
        public boolean replacePrefixLine(String line, PrintWriter out) {
            if ("%name-prefix=\"base_yy\"".equals(line)) {
                out.println("%define api.prefix {base_yy}");
                return true;
            }
            if ("%pure-parser".equals(line)) {
                // Parsers in Java are always "pure", so skip the line
                return true;
            }
            return false;
        }

        @Override
        public void insertHeadingLines(PrintWriter out) {
            out.println("%define api.parser.class {PgSqlParser}");
            out.println("%define api.position.type {Position}");
            out.println("%define api.location.type {Location}");
            out.println("%define api.parser.extends {AbstractParser}");
            out.println("%define api.parser.public");
        }

        @Override
        public void initYYActionBlock(PrintWriter out, int submethodNumber) {
            out.println();
            out.print("  private int yyaction_");
            out.print(submethodNumber);
            out.println("(int yyn, YYStack yystack, int yylen) {");
            out.println("      Object yyval;");
            out.println("      Location yyloc = yylloc (yystack, yylen);");
            out.println();
            out.println("      /* If YYLEN is nonzero, implement the default value of the action:");
            out.println("         '$$ = $1'.  Otherwise, use the top of the stack.");
            out.println();
            out.println("         Otherwise, the following line sets YYVAL to garbage.");
            out.println("         This behavior is undocumented and Bison");
            out.println("          users should not rely upon it.  */");
            out.println("      if (yylen > 0) {");
            out.println("            yyval = yystack.valueAt (yylen - 1);");
            out.println("      } else {");
            out.println("            yyval = yystack.valueAt (0);");
            out.println("      }");
            out.println();
            out.println("      yy_reduce_print (yyn, yystack);");
            out.println();
            out.println("      switch (yyn) {");
        }

        @Override
        public void finishYYActionBlock(PrintWriter out) {
            out.println("    }");
            out.println();
            out.println("    yy_symbol_print (\"-> $$ =\", yyr1_[yyn], yyval, yyloc);");
            out.println();
            out.println("    yystack.pop (yylen);");
            out.println("    yylen = 0;");
            out.println();
            out.println("    /* Shift the result of the reduction.  */");
            out.println("    int yystate = yy_lr_goto_state_ (yystack.stateAt (0), yyr1_[yyn]);");
            out.println("    yystack.push (yystate, yyval, yyloc);");
            out.println("    return YYNEWSTATE;");
            out.println("  }");
        }
    };

    /**
     * Replaces some lines in the the prefix part of the grammar in the GrammarConverter. 
     *
     * @param prefixLine
     *            The line that is to be inspected
     * @param out
     *            The PrintWriter into which the altered line is to be inserted
     * @return boolean true if an altered line is entered into out, of false if the line is untouched.
     */
    public abstract boolean replacePrefixLine(String prefixLine, PrintWriter out);

    /**
     * Inserts some Bison version-specific lines into the out PrintWriter for the GrammarConverter
     *
     * @param out
     *            The PrintWriter into which the heading lines are to be inserted
     */
    public abstract void insertHeadingLines(PrintWriter out);

    /**
     * Prints the first part of initYYAction_nn() method in the JavaParserConverter
     *
     * @param out
     *            The PrintWriter to which to print the block
     * @param submethodNumber
     *            The number of the submethod to add to it's name
     */
    public abstract void initYYActionBlock(PrintWriter out, int submethodNumber);

    /**
     * Prints the first part of initYYAction_nn() method in the JavaParserConverter
     *
     * @param out
     *            The PrintWriter to which to print the block
     */
    public abstract void finishYYActionBlock(PrintWriter out);

    /**
     * Interprets the fist line of the "bison --version" response.
     *
     * @param firstLineOfResponse
     *            The first line of the response of the "bison --version" OS command
     * @return BisonVersion The correct BisonVersion or null if not found.
     */
    public static BisonVersion fromLine(String firstLineOfResponse) {
        if (firstLineOfResponse.matches("bison \\(GNU Bison\\) 3\\.(0|1|2)\\..*")) {
            return BISON_VERSION_3;
        }
        if (firstLineOfResponse.matches("bison \\(GNU Bison\\) 3\\..*")) {
            return BISON_VERSION_3_3;
        }
        if (firstLineOfResponse.matches("bison \\(GNU Bison\\) 2.*")) {
            return BISON_VERSION_2;
        }
        return null;
    }

    @Override
    public String toString() {
        return name().replace('_', ' ');
    }
}
