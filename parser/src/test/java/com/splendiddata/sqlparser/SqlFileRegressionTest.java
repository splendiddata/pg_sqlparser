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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

    private List<SqlParserErrorData> parserErrors = new ArrayList<>();

    /**
     * Returns all sql files that are to be tested
     *
     * @return Stream<Path> with all files to be tested
     */
    @SuppressWarnings("unused")
    private static Stream<Path> getSqlTestFiles() {
        List<Path> paths = new ArrayList<>();
        FileVisitor<Path> fileVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (attrs.isRegularFile() && !file.getFileName().toString().startsWith(".")) {
                    paths.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        };

        Path projectDirectory = Paths.get(".");
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
            Files.walkFileTree(Paths.get(projectDirectory.toString(), "src/test/resources/commands"), fileVisitor);
            Files.walkFileTree(Paths.get(projectDirectory.toString(), "src/test/resources/postgres/test/regress/sql"),
                    fileVisitor);
        } catch (Exception e) {
            log.error("getSqlTestFiles()->failed, basde directory = " + projectDirectory.toAbsolutePath(), e);
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

        try (Reader reader = new SqlTextReader(new FileReader(file.toFile()))) {
            SqlParser parser = new SqlParser(reader, error -> parserErrors.add(error));
            Assertions.assertTrue(parser.parse(), "Parser error in: " + fileName + "\n" + parserErrors);
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

    private static final class SqlTextReader extends BufferedReader {
        /**
         * Constructor
         *
         * @param in
         */
        SqlTextReader(Reader in) {
            super(in);
        }

        /**
         * @see java.io.BufferedReader#read(char[], int, int)
         *
         * @param cbuf
         * @param off
         * @param len
         * @return
         * @throws IOException
         */
        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            int result = super.read(cbuf, off, len);
            for (int i = 0; i < result; i++) {
                if (cbuf[off + i] == '\n' && i < result - 3) {
                    if (cbuf[off + i + 1] == '\\') {
                        cbuf[off + i + 1] = '-';
                        cbuf[off + i + 2] = '-';
                    }
                } else if (cbuf[off + i] == ':' && i < result - 1 && cbuf[off + i + 1] == '\'' && i > 0
                        && (cbuf[off + i - 1] == ' ' || cbuf[off + i - 1] == '(')) {
                    /*
                     * Remove the colon from :'some_filename' constructs
                     */
                    cbuf[off + i] = ' ';
                }
            }
            return result;
        }

    }
}
