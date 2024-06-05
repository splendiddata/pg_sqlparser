/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2024
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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Converts the original grammar (/postgresql-9.3.4/src/backend/parser/gram.y), which was intended to generate C code,
 * to a version that generates a grammar that will generate Java-like code. The generated code must be converted again
 * by {@link com.splendiddata.sqlparser.grammartojava.JavaParserConverter} to get something compilable.
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@Mojo(name = "convertLex", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
@Execute(goal = "convertLex", phase = LifecyclePhase.GENERATE_SOURCES)
public class LexConverter extends AbstractMojo implements FileVisitor<Path> {
    private Log log;

    /**
     * A line that starts with a word is expected to start an identifier. This pattern is used to identify it. Resulting
     * group 1 will contain the name of the identifier - possibly relating to a LexRuleSpecial enum value. Group 2 will
     * contain "the rest of the line".
     */
    private static final Pattern IDENTIFIER_DEFINITION_PATTERN = Pattern.compile("^(\\w+)\\s(.*)");

    /**
     * Every line that starts with whitespace is considered a "standard" line - not one that defines an identifier.
     */
    private static final Pattern STANDARD_LINE_PATTERN = Pattern.compile("^$|^\\s.*");

    /**
     * The ereport C macro needs some special treatment.
     */
    private boolean inEreport = false;

    /**
     * Some identifiers need special treatment to convert their C code to proper Java.
     */
    private LexRuleSpecial specialRule = LexRuleSpecial.DEFAULT;

    /**
     * The package name that JFlex will use to generate the Java source file.
     */
    @Parameter(property = "packageName", defaultValue = "com.spendiddata.sqlparser")
    private String packageName;

    /**
     * Directory that will be searched for the *.l file
     */
    @Parameter(property = "sourceDir", defaultValue = "src/main/scanner")
    private String sourceDir;

    /**
     * Directory in which the converted scan.l file will be placed. The directory structure for the generated file will
     * be the same as the none found under the source directory. The may not overlap!
     */
    @Parameter(property = "targetDir", defaultValue = "target/generated_sources/converted.scanner")
    private String targetDir;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        log = getLog();
        log.info(getClass().getName() + ".execute()");
        log.info("packageName =\"" + packageName + "\"");
        log.info("sourceDir =\"" + sourceDir + "\"");
        log.info("targetDir =\"" + targetDir + "\"");

        if (sourceDir.equals(targetDir)) {
            throw new MojoFailureException("sourceDir and targetDir must be differrent");
        }
        try {
            Files.createDirectories(Paths.get(targetDir));
            Files.walkFileTree(Paths.get(sourceDir), this);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    /**
     * @see java.nio.file.FileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     * 
     * @return FileVisitResult.CONTINUE
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        log.info("Process file " + file);

        try (BufferedReader in = Files.newBufferedReader(file, Charset.forName("UTF-8"));
                PrintWriter out = new PrintWriter(new File(targetDir, file.toFile().getName() + "ex"), "UTF-8")) {
            writeHeading(out);

            int bracketsRemoved = 0;
            int pctPctCount = 0;
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                if (log.isDebugEnabled()) {
                    log.debug("in: \"" + line + "\"");
                }
                // In Postgres 9.5, the first block starts with '%{'. This has been replaced by 'top%{' in Postgres 9.6
                if (line.startsWith("%top{")) {
                    skipVerbatimLines(in);
                    continue;
                }
                if (line.startsWith("%%")) {
                    if (pctPctCount++ > 0) {
                        /*
                         * C code goes here. Skip it.
                         */
                        break;
                    }
                }
                if (line.startsWith("#")) {
                    continue;
                }
                if (line.startsWith("%option")) {
                    continue;
                }
                String convertedLine = line
                        /*
                         * replace yyerror("..."); by { yyerror("..."); return EOF;}
                         */
                        .replaceAll("(yyerror\\(\".*\"\\);)", "{ $1 yyclose(); return EOF;}");
                Matcher matcher;
                if (inEreport) {
                    if (convertedLine.matches("\\s*\\(errcode.*")) {
                        convertedLine = convertedLine.replace("(errcode(", " ErrCode.").replace(")", "");
                        bracketsRemoved++;
                    } else if (convertedLine.matches("\\s*\\(errmsg.*")) {
                        convertedLine = convertedLine.replace("(errmsg", " errmsg");
                        bracketsRemoved++;
                    }
                    if (convertedLine.matches(".*\\);.*")) {
                        convertedLine = convertedLine.replaceAll("\\){" + bracketsRemoved + "};", ";");
                        inEreport = false;
                    }
                } else if (convertedLine.matches("\\s*ereport\\(.*")) {
                    log.debug("changed to: " + convertedLine + ", phase := IN_EREPORT");
                    inEreport = true;
                    bracketsRemoved = 0;
                } else if (pctPctCount == 0
                        && (matcher = IDENTIFIER_DEFINITION_PATTERN.matcher(convertedLine)).matches()) {
                    convertedLine = matcher.group(1) + " =" + matcher.group(2);
                    convertedLine = convertedLine.replace("[^\"]+", "[^\\\"]+");
                } else if (STANDARD_LINE_PATTERN.matcher(line).matches()) {
                    convertedLine = specialRule.processLine(convertedLine);
                    convertedLine = convertedLine
                            /*
                             * The strlen function just returns the length of a character string
                             */
                            .replaceAll("(\\W+)strlen\\s*\\(\\s*([^)]*)\\)", "$1$2.length()")
                            /*
                             * Java cannot return anything through the parameters. So the trucate_identifier method must
                             * be altered.
                             */
                            .replaceAll("(\\s+)truncate_identifier\\s*\\(\\s*(\\w*)[^)]*\\)",
                                    "$1$2 = truncate_identifier($2)")
                            /*
                             * The length of a literal is in the literalbuf StringBuilder. So literallen is obsolete
                             * (and removed from yyextra).
                             */
                            .replace("yyextra->literallen", "yyextra.literalbuf.length()")
                            /*
                             * C's "->" pointer constructs are not supported by Java. Just use ".".
                             */
                            .replace("->", ".")
                            /*
                             * Character pointers are converted to String
                             */
                            .replaceAll("(\\W)(char)\\s*\\*", "$1String ")
                            /*
                             * No string is cast to ScanKeyword as ScanKeyword is no string.
                             */
                            .replaceAll("(\\W)(ScanKeyword)\\s*\\*", "$1ScanKeyword ")
                            /*
                             * Java doesn't know the "const" and "unsigned" keyword
                             */
                            .replaceAll("(\\W)(const|unsigned)\\s", "$1 ")
                            /*
                             * pg_wchar is just Postgres's variant of Java's char.
                             */
                            .replace("pg_wchar", "char")
                            /*
                             * String compares to Java style
                             */
                            .replaceAll("strcmp\\(yytext\\,\\s*([^)]*)\\)\\s*==\\s*0", "yytext().equals($1)")
                            /*
                             * Replaces strtoul to something that Java might implement
                             */
                            .replaceAll("strtoul\\(yytext([^,]*)\\,[^,]*\\,\\s*(\\d*)\\s*\\)",
                                    "strtoul(zzBuffer, zzStartRead$1, $2)")
                            /*
                             * char assignment from long is not allowed unless it is cast to char.
                             */
                            .replaceAll("char\\s*(\\w*)\\s*=\\s*strtoul", "char $1 = (char)strtoul")
                            /*
                             * yytext is for Postgres's flex scanner. We use jflex here.
                             */
                            .replaceAll("strstr\\(yytext, (\".*\")\\);", "strstr(zzBuffer, zzStartRead, $1);")
                            .replaceAll("yytext\\[(.*)\\]", "zzBuffer[zzStartRead + $1]")
                            /*
                             * The C variant of yyterminate() stops the scanner. The Java variant does not, so it
                             * returns -1 (= EOF)
                             */
                            .replace("yyterminate()", "return yyterminate()")
                            /*
                             * No C macros in Java
                             */
                            .replace("BEGIN", "yybegin")
                            /*
                             * Text s handled somewhat different in Javan than in C
                             */
                            .replace("process_integer_literal(yytext, yylval)", "process_integer_literal(yytext())")
                            /*
                             * We want the text here, not some pointer that points into some array
                             */
                            .replace("pstrdup(yytext)", "yytext()")
                            /*
                             * Strings are immutable in Java, so no need to duplicate them
                             */
                            .replaceAll("pstrdup\\((.*)\\);", "$1;")
                            /*
                             * yylval has no members
                             */
                            .replaceAll("yylval\\.\\w*", "yylval")
                            /*
                             * Again text is handled somewhat different in Java than in C
                             */
                            .replaceAll("addlit\\(.*\\)", "addlit(yytext())")
                            /*
                             * Flex and JFlex react differently on general rules. So for JFlex we'll add an initial
                             * context to find comment.
                             */
                            .replaceAll("^\\{xcstart\\}\\s*", "<YYINITIAL>{xcstart} ")
                            /*
                             * No NULL object pointer but just the null literal
                             */
                            .replaceAll("(\\W)NULL(\\W)", "$1null$2")
                            /*
                             * Text length
                             */
                            .replace("yyleng", "zzMarkedPos - zzStartRead")
                            /*
                             * indexed character in yytext
                             */
                            .replaceAll("yytext\\[([^\\]])\\]", "yycharat($1)")
                            /*
                             * @since 8.0 - Postgres version 13
                             */
                            .replace("YYSTATE", "yystate()")
                            .replaceAll("yytext(?!\\()", "yytext()");
                } else {
                    specialRule = LexRuleSpecial.identifySpecialRule(line);
                }

                if (log.isDebugEnabled()) {
                    if (line.equals(convertedLine)) {
                        log.debug("write unchanged");
                    } else {
                        log.debug("changed to: " + convertedLine);
                    }
                }
                out.println(convertedLine);
            }
        }
        log.debug("@<visitFile(file=" + file + ", attrs=" + attrs + ") = " + FileVisitResult.CONTINUE);
        return FileVisitResult.CONTINUE;
    }

    /**
     * Lines between "%{" and "%}" contain Postgres's C code and must not be included in our Java scan.l file.
     *
     * @param in
     * @throws IOException
     */
    private static void skipVerbatimLines(BufferedReader in) throws IOException {
        String line;
        do {
            line = in.readLine();
        } while (line != null && !line.startsWith("%}"));
    }

    /**
     * Writes a few lines before the first line that is read from the input file *
     *
     * @param out
     *            The output file to write to
     */
    private void writeHeading(PrintWriter out) {
        out.print("package ");
        out.print(packageName);
        out.println(";");
        out.println();
        out.println("import com.splendiddata.sqlparser.enums.BackslashQuoteType;");
        out.println("import com.splendiddata.sqlparser.enums.Severity;");
        out.println("import com.splendiddata.sqlparser.structure.*;");
        out.println();
        out.println("%%");
        out.println();
        out.println("%class PgSqlScanner");
        out.print("%extends ");
        out.print(packageName);
        out.println(".AbstractScanner");
        out.println("%function scan");
        out.println("%public");
        out.println();
        out.println("%char");
        out.println("%unicode");
        out.println();
        out.println("%{");
        out.println("  /**");
        out.println("   * From /postgresql-9.3.4/src/backend/parser/scan.c);");
        out.println("    */");
        out.println("  private static final int INITIAL = 0;");
        out.println();
        out.println("  private static final int BCONST = ScanKeyword.BCONST.value;");
        out.println("  private static final int COLON_EQUALS = ScanKeyword.COLON_EQUALS.value;");
        out.println("  private static final int DOT_DOT = ScanKeyword.DOT_DOT.value;");
        out.println("  private static final int EQUALS_GREATER = ScanKeyword.EQUALS_GREATER.value;");
        out.println("  private static final int FCONST = ScanKeyword.FCONST.value;");
        out.println("  private static final int GREATER_EQUALS = ScanKeyword.GREATER_EQUALS.value;");
        out.println("  private static final int IDENT = ScanKeyword.IDENT.value;");
        out.println("/** @since 8.0 - Postgres13 */");
        out.println("  private static final int UIDENT = ScanKeyword.UIDENT.value;");
        out.println("  private static final int LESS_EQUALS = ScanKeyword.LESS_EQUALS.value;");
        out.println("  private static final int NOT_EQUALS = ScanKeyword.NOT_EQUALS.value;");
        out.println("  private static final int Op = ScanKeyword.Op.value;");
        out.println("  private static final int PARAM = ScanKeyword.PARAM.value;");
        out.println("  private static final int SCONST = ScanKeyword.SCONST.value;");
        out.println("/** @since 8.0 - Postgres13 */");
        out.println("  private static final int USCONST = ScanKeyword.USCONST.value;");
        out.println("  private static final int TYPECAST = ScanKeyword.TYPECAST.value;");
        out.println("  private static final int XCONST = ScanKeyword.XCONST.value;");
        out.println("  private static final Severity ERROR = Severity.ERROR;");
        out.println(
                "  private static final BackslashQuoteType BACKSLASH_QUOTE_OFF = BackslashQuoteType.BACKSLASH_QUOTE_OFF;");
        out.println(
                "  private static final BackslashQuoteType BACKSLASH_QUOTE_SAFE_ENCODING = BackslashQuoteType.BACKSLASH_QUOTE_SAFE_ENCODING;");
        out.println();
        out.println("  private core_yyscan_t yyscanner;");
        out.println("%}");
        out.println();
        out.println("%byaccj");
        out.println();
        out.println("%ctorarg core_yyscan_t yyscanner");
        out.println("%init{");
        out.println("  this.yyscanner = yyscanner;");
        out.println("    yyextra.scanbuf = zzBuffer;");
        out.println("    yyextra.scanbuflen = zzBuffer.length;");
        out.println("%init}");
        out.println();
        out.println("%{");
        out.println();
        out.println("  /**");
        out.println("   * Reduces the lenght of the found text to n characters");
        out.println("   *");
        out.println("   * @param n");
        out.println("   */");
        out.println("  void yyless(int n) {");
        out.println("      zzMarkedPos = zzStartRead + n;");
        out.println("  }");
        out.println();
        out.println("  /**");
        out.println("   * Stores the positon");
        out.println("   */");
        out.println("  void SET_YYLLOC() {");
        out.println("      position = new Position(yychar);");
        out.println("  }");
        out.println();
        out.println("  /**");
        out.println("   * Sometimes, we do want yylloc to point into the middle of a token; this is");
        out.println("   * useful for instance to throw an error about an escape sequence within a");
        out.println("   * string literal.  But if we find no error there, we want to revert yylloc");
        out.println("   * to the token start, so that that's the location reported to the parser.");
        out.println("   * Use PUSH_YYLLOC/POP_YYLLOC to save/restore yylloc around such code.");
        out.println("   * (Currently the implied \"stack\" is just one location, but someday we might");
        out.println("   * need to nest these.)");
        out.println("   * <p>");
        out.println("   * copied from postgresql-13beta1/src/backend/parser/scan.c");
        out.println("   * @since 8.0 - Postgres version 13");
        out.println("   */");
        out.println("  void PUSH_YYLLOC() {");
        out.println("      yyextra.save_yylloc = yychar;");
        out.println("  }");
        out.println();
        out.println("  /**");
        out.println("   * @since 8.0 - Postgres version 13");
        out.println("   */");
        out.println("  void POP_YYLLOC() {");
        out.println("      yychar = yyextra.save_yylloc;");
        out.println("  }");
        out.println("%}");
    }

    /**
     * Just returns {@link java.nio.file.FileVisitResult#CONTINUE}.
     * 
     * @see java.nio.file.FileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     * 
     * @return FileVisitResult.CONTINUE
     */
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    /**
     * Just returns {@link java.nio.file.FileVisitResult#CONTINUE}.
     * 
     * @see java.nio.file.FileVisitor#visitFileFailed(java.lang.Object, java.io.IOException)
     * 
     * @return FileVisitResult.CONTINUE
     */
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    /**
     * Just returns {@link java.nio.file.FileVisitResult#CONTINUE}.
     * 
     * @see java.nio.file.FileVisitor#postVisitDirectory(java.lang.Object, java.io.IOException)
     * 
     * @return FileVisitResult.CONTINUE
     */
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}