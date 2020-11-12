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

package com.splendiddata.sqlparser.testgui;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.SqlParser;
import com.splendiddata.sqlparser.SqlParserErrorData;
import com.splendiddata.sqlparser.SqlParserErrorReporter;
import com.splendiddata.sqlparser.structure.Node;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class SqlParserTestGuiController implements Initializable {
    private static final Logger log = LogManager.getLogger(SqlParserTestGuiController.class);

    @FXML
    private TextArea sqlText;
    @FXML
    TextArea errorsText;
    @FXML
    ListView<Node> resultString;
    @FXML
    TextArea resultXML;
    @FXML
    Label caretPos;

    private static final String RESULT_CELL_FACTORT_CLASS_NAME = "com.splendiddata.sqlparser.testgui.ResultStringListitem";

    long errorOffset;
    ObservableList<Node> resultStringList = FXCollections.observableArrayList();

    private SqlParserErrorReporter errorHandler = new SqlParserErrorReporter() {
        @Override
        public void reportError(SqlParserErrorData toReport) {
            if (errorsText.getText().length() > 0) {
                errorsText.appendText("\n");
            }
            errorsText.appendText(toReport.toString());
            if (toReport.getErrorOffset() == null) {
                errorOffset = 0;
            } else {
                errorOffset = toReport.getErrorOffset().longValue();
            }
        }
    };

    double scrollPixel;

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try (InputStream iStream = getClass().getClassLoader()
                .getResourceAsStream(RESULT_CELL_FACTORT_CLASS_NAME.replace('.', '/') + ".class")) {
            if (iStream != null) {
                Constructor<?> cellFactoryConstructoronstructor = java.lang.Class
                        .forName(RESULT_CELL_FACTORT_CLASS_NAME).getConstructor();
                resultString.setCellFactory(cell -> {
                    try {
                        return (ListCell<Node>) cellFactoryConstructoronstructor.newInstance();
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | SecurityException e) {
                        log.error(e, e);
                    }
                    return null;
                });
            }
        } catch (IOException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            log.error(e, e);
        }
        resultString.setItems(resultStringList);
        resultString.getSelectionModel().selectedItemProperty()
                .addListener(obs -> resultXML.setText(resultString.getSelectionModel().getSelectedItem() == null ? ""
                        : ParserUtil.stmtToXml(resultString.getSelectionModel().getSelectedItem())));
        errorsText.setOnMouseClicked(event -> Platform.runLater(() -> {
            sqlText.positionCaret((int) errorOffset);
            sqlText.requestFocus();
        }));
        sqlText.caretPositionProperty()
                .addListener((observable, oldValue, newValue) -> caretPos.setText(newValue.toString()));
    }

    /**
     * This method process the sql query on fly.
     */
    @FXML
    private void processSqlQuery() {
        if (resultXML.getText() != null && !resultXML.getText().matches("\\s*")) {
            scrollPixel = resultXML.getScrollTop();
        }

        errorsText.clear();
        errorOffset = 0;
        resultStringList.clear();
        resultXML.clear();

        SqlParser parser = new SqlParser(new StringReader(sqlText.getText()), errorHandler);
        try {
            if (parser.parse()) {
                if (errorsText.getText().isEmpty()) {
                    errorsText.appendText("parsing ok");
                }
            }
        } catch (Exception e) {
            Writer sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            if (!errorsText.getText().isEmpty()) {
                pw.println();
            }
            pw.println(e.getMessage());
            pw.println();
            e.printStackTrace(pw);
            pw.println();
            errorsText.appendText(sw.toString());
        }
        if (parser.getResult() != null) {
            for (Node stmt : parser.getResult()) {
                if (stmt != null) {
                    resultStringList.add(stmt);
                }
            }

            if (!resultStringList.isEmpty()) {
                Platform.runLater(() -> resultString.getSelectionModel().select(0));
            }
        }
    }
}
