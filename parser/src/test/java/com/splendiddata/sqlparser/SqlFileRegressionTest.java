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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.splendiddata.sqlparser.enums.Severity;
import com.splendiddata.sqlparser.structure.Node;
import com.splendiddata.sqlparser.structure.Value;

/**
 * This class walks directories "src/test/resources/commands" and "src/test/resources/postgres/test/regress/sql" and
 * tries to parse every file that is in there. On every parsed statement toString() is invoked and the result of that is
 * parsed again. Then on the re-parsed statement toString() is invoked again and compared with the result of the first
 * parse-toString() action. The two should match exactly.
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public class SqlFileRegressionTest {
    private static final Logger log = LogManager.getLogger(SqlFileRegressionTest.class);

    private static final class CopyStmtFilter implements Predicate<String> {

        /**
         * Lines that start with \copy
         */
        private static final Pattern COPY_STMT_PATTERN = Pattern.compile("(?i)^\\\\COPY");

        /**
         * True for lines between \copy and \.
         */
        private boolean inCopyStatement = false;

        /**
         * @see java.util.function.Predicate#test(java.lang.Object)
         *
         * @param line
         *            The line to check
         * @return false for lines between \copy and \., true for all other lines
         */
        @Override
        public boolean test(String line) {
            if (inCopyStatement) {
                if (line.startsWith("\\.")) {
                    inCopyStatement = false;
                }
                return false;
            }
            inCopyStatement = COPY_STMT_PATTERN.matcher(line).matches();
            return true;
        }

        void reset() {
            inCopyStatement = false;
        }
    };

    private static final CopyStmtFilter COPY_STMT_FILTER = new CopyStmtFilter();

    private List<SqlParserErrorData> parserErrors = new ArrayList<>();
    private static Path projectDirectory;
    private static Path inputTestResourcesPath;
    private static Path outputTestResourcesPath;

    /**
     * Returns all sql files that are to be tested
     * <p>
     * The files are preprocessed and copied to the target/test/resources directory. Preprocessing consists of:
     * <ul>
     * <li>All lines that are starting with a backslash (\) or a colon (:) are commented out
     * <li>'\;' is replaced by just a semi colon (;)
     * <li>Everywhere the literal '\gset' is replaced by '; -- '
     * <li>The colon (:) character is removed everywhere a single colon precedes an identifier or a text literal
     * <li>Data for PSQL \COPY statements is filtered out
     * </ul>
     *
     * @return Stream<Path> with all files to be tested
     */
    private static Stream<Path> getSqlTestFiles() {
        List<Path> paths = new ArrayList<>();
        FileVisitor<Path> fileVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (attrs.isRegularFile() && !file.getFileName().toString().startsWith(".")) {
                    Path relativePath = inputTestResourcesPath.relativize(file);
                    Path preprocessedFile = Paths.get(outputTestResourcesPath.toString(), relativePath.toString())
                            .toAbsolutePath();
                    log.debug(() -> "Preprocessing " + relativePath + " to " + preprocessedFile);
                    try {
                        Files.createDirectories(preprocessedFile.getParent());
                        try (PrintWriter pw = new PrintWriter(
                                Files.newBufferedWriter(preprocessedFile, StandardCharsets.UTF_8));
                                BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                            COPY_STMT_FILTER.reset();
                            br.lines().filter(line -> COPY_STMT_FILTER.test(line))
                                    .map(line -> line.replaceAll("(?i)\\\\crosstabview", "; -- \\crosstabview")
                                            .replaceAll("(?i)\\\\gset", "; -- \\gset")
                                            .replaceAll("(?i)\\\\gexec", "; -- \\gexec").replace("\\;", ";")
                                            .replaceAll("([^:\\d]):(['\\w^\\d])", "$1$2").replaceAll("^\\\\", "-- \\\\")
                                            .replaceAll("^:", "-- :"))
                                    .forEach(line -> pw.println(line));
                        }
                        paths.add(preprocessedFile);
                    } catch (MalformedInputException e) {
                        // Probably the collate.windows.win1252.sql file, which is not UTF-8 encoded 
                        try {
                            log.info(e.getMessage() + "  - just copying " + file);
                            Files.copy(file, preprocessedFile);
                            paths.add(preprocessedFile);
                        } catch (IOException e1) {
                            log.error(e, e);
                        }
                    } catch (UncheckedIOException | IOException e) {
                        log.warn(e, e);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        };

        projectDirectory = Paths.get(".");
        try {
            projectDirectory = projectDirectory.toAbsolutePath().getParent();
            if (!projectDirectory.toAbsolutePath().endsWith("parser")) {
                /*
                 * Sometimes Maven is executed on the main project (sqlparser), sometimes on the current project
                 * (sqlparser/parser) To make them both work, make sure the path will start at the current (parser)
                 * project.
                 */
                projectDirectory = Paths.get(projectDirectory.toAbsolutePath().toString(), "parser");
            }
            inputTestResourcesPath = Paths.get(projectDirectory.toString(), "src/test/resources");
            outputTestResourcesPath = Paths.get(projectDirectory.toString(), "target/test/resources");

            Files.walkFileTree(Paths.get(inputTestResourcesPath.toString(), "commands"), fileVisitor);
            Files.walkFileTree(Paths.get(inputTestResourcesPath.toString(), "postgres/test/regress/sql"), fileVisitor);
        } catch (Exception e) {
            log.error("getSqlTestFiles()->failed, base directory = " + projectDirectory.toAbsolutePath(), e);
        }

        return paths.stream().sorted();
    }

    @ParameterizedTest
    @MethodSource("getSqlTestFiles")
    void testSqlFile(Path file) throws IOException {
        String fileName = file.getFileName().toString();
        parserErrors.clear();

        log.info("start " + fileName);
        if (log.isTraceEnabled()) {
            StringWriter str = new StringWriter();
            try (PrintWriter out = new PrintWriter(str);
                    Reader reader = new FileReader(file.toFile());
                    BufferedReader in = new BufferedReader(reader)) {
                for (String line = in.readLine(); line != null; line = in.readLine()) {
                    out.println(line);
                }
            }
            log.trace(str.toString());
        }

        try (Reader reader = Files.newBufferedReader(file)) {
            SqlParser parser = new SqlParser(reader, error -> parserErrors.add(error));
            Assertions.assertTrue(parser.parse(), "Parser error in: " + fileName + "\n" + parserErrors);
            Assertions.assertTrue(
                    parserErrors.isEmpty()
                            || parserErrors.stream().noneMatch(err -> Severity.ERROR.equals(err.getSeverity())),
                    "parser.parse() returned true, but parser errors exist:\n" + parserErrors);
            Assertions.assertNotNull(parser.getResult(), "Expected non-null result: " + fileName);
            for (Node originallyParsedStatement : parser.getResult()) {
                log.debug("parsed sql = " + originallyParsedStatement);
                log.trace(ParserUtil.stmtToXml(originallyParsedStatement));

                verifyNodeTagIsFilledIn(originallyParsedStatement);

                /*
                 * Check the toString method by executing toString() on the returned statement and passing the result to
                 * the parser again. The resulting statement from the parser must be the same as the result of
                 * toString(). Of course this does not check if the statement is correctly parsed,but it does give a
                 * hint on the quality of the toString() method - which is important as well.
                 */
                String sql = originallyParsedStatement.toString();
                SqlParser parserAgain = new SqlParser(new StringReader(sql));
                Assertions.assertTrue(parserAgain.parse(),
                        "Incorrect implementation of toString() of " + ParserUtil.stmtToXml(originallyParsedStatement)
                                + "\nin file: " + file + "\nparsed statement toString() = " + sql);
                for (Node reparsedStatement : parserAgain.getResult()) {
                    Assertions.assertEquals(sql, reparsedStatement.toString(),
                            "The parsed and reparsed statement " + ParserUtil.stmtToXml(originallyParsedStatement)
                                    + " should give the same result in: " + fileName);
                }

                /*
                 * Test clone
                 */
                Node clonedStatement = originallyParsedStatement.clone();
                Assertions.assertEquals(sql, clonedStatement.toString(),
                        "The cloned statement " + ParserUtil.stmtToXml(originallyParsedStatement)
                                + " should give the same result in: " + fileName);

                /*
                 * Test copy constructor
                 */
                Node copiedStatement = originallyParsedStatement.getClass()
                        .getConstructor(originallyParsedStatement.getClass()).newInstance(originallyParsedStatement);
                Assertions.assertEquals(sql, copiedStatement.toString(),
                        "The copied statement " + ParserUtil.stmtToXml(originallyParsedStatement)
                                + " should give the same result in: " + fileName);
            }
        } catch (Throwable e) {
            log.error("file: " + fileName, e);
            Assertions.fail("file: " + fileName + " threw:" + e);
        }

        log.info("finish " + fileName);
    }

    /**
     * Reflectively and recursively checks that every Node has its type tag filled in.
     *
     * @param node
     *            The node to check. All of its fields will be checked as well.
     */
    private static void verifyNodeTagIsFilledIn(Node node) {
        Assertions.assertNotNull(node.type,
                node.getClass().getSimpleName() + " has no type filled in: " + ParserUtil.stmtToXml(node));
        switch (node.type) {
        case T_String:
        case T_Integer:
        case T_Float:
        case T_Null:
        case T_Boolean:
        case T_BitString:
            Assertions.assertEquals(Value.class, node.getClass(),
                    "Value must be T_String, T_Integer, T_FLoat, T_Boolean, T_BitString or T_Null in: " + node);
            break;
        default:
            Assertions.assertEquals("T_" + node.getClass().getSimpleName(), node.type.name(),
                    "Node type: " + node.type.name() + " not correct in " + node.getClass().getSimpleName() + ": "
                            + ParserUtil.stmtToXml(node));
        }

        for (Field field : node.getClass().getFields()) {
            if (Object.class.isAssignableFrom(field.getType())) {
                try {
                    Object fieldContent = field.get(node);
                    if (fieldContent instanceof Node) {
                        verifyNodeTagIsFilledIn((Node) fieldContent);
                    }
                } catch (Exception e) {
                    log.error(new StringBuilder("verifyNodeTagIsFilledIn(node=").append(node.getClass().getSimpleName())
                            .append(": ").append(ParserUtil.stmtToXml(node)).append(")->failed)"), e);
                }
            }
        }
    }
}
