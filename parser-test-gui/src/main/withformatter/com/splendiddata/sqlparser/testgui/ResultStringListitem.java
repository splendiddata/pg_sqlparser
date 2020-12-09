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

package com.splendiddata.sqlparser.testgui;

import java.io.StringReader;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.splendiddata.pgcode.formatter.CodeFormatter;
import com.splendiddata.pgcode.formatter.FormatConfiguration;
import com.splendiddata.pgcode.formatter.configuration.xml.v1_0.Configuration;
import com.splendiddata.sqlparser.structure.Node;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * ListCell&lt;Node&gt; for the parsed and rendered sql statements
 *
 * @author Splendid Data Product Development B.V.
 * @since 8.0 - Postgres version 13
 */
public class ResultStringListitem extends ListCell<Node> {
    private static final Logger log = LogManager.getLogger(ResultStringListitem.class);

    private static final FormatConfiguration formatConfig = new FormatConfiguration((Configuration) null);

    private final TextArea content = new TextArea();
    private Node stmt = null;

    /**
     * Constructor
     */
    public ResultStringListitem() {
        this.setPadding(new Insets(0, 0, 0, 0));
        this.autosize();
        content.autosize();
        content.setWrapText(true);
        content.setEditable(false);
        content.setMinWidth(50);
        content.setPrefWidth(80);
        setGraphic(content);
        content.setOnMouseClicked((event) -> {
            Platform.runLater(() -> getListView().getSelectionModel().select(stmt));
        });
        this.setOnMouseClicked((event) -> {
            Platform.runLater(() -> getListView().getSelectionModel().select(stmt));
        });

        content.setContextMenu(buildContextMenu());
    }

    /**
     * Builds the context menu for the content TextArea
     *
     * @return ContextMenu The created context menu
     */
    private ContextMenu buildContextMenu() {
        MenuItem menuItemCopy = new MenuItem("Copy");
        menuItemCopy.setOnAction(event -> {
            String selectedText = content.getSelectedText();
            if (selectedText.length() > 0) {
                final ClipboardContent content = new ClipboardContent();
                content.putString(selectedText);
                Clipboard.getSystemClipboard().setContent(content);
            }
        });

        MenuItem menuItemSelectAll = new MenuItem("SelectAll");
        menuItemSelectAll.setOnAction(event -> content.selectAll());

        MenuItem menyItemFormat = new MenuItem("Format");
        menyItemFormat.setOnAction(event -> {
            boolean selected = content.getSelectedText().length() > 0;
            try {
                content.setText(CodeFormatter.toStringResults(new StringReader(content.getText()), formatConfig)
                        .collect(Collectors.joining("")));
            } catch (Exception e) {
                log.error(e + "\n on " + stmt, e);
            }
            if (selected) {
                content.selectAll();
            }
        });

        ContextMenu contextMenu = new ContextMenu(menuItemCopy, new SeparatorMenuItem(), menuItemSelectAll,
                new SeparatorMenuItem(), menyItemFormat);

        contextMenu.setOnShowing(event -> {
            int selectedTextLength = content.getSelectedText().length();
            menuItemCopy.setDisable(selectedTextLength == 0);
            menyItemFormat.setDisable(selectedTextLength > 0 && selectedTextLength < content.getText().length());
        });

        return contextMenu;
    }

    /**
     * Renders the statement
     * 
     * @see javafx.scene.control.Cell#updateItem(java.lang.Object, boolean)
     */
    @Override
    protected void updateItem(Node stmt, boolean empty) {
        super.updateItem(stmt, empty);
        this.stmt = stmt;
        if (empty || stmt == null) {
            content.setText("");
        } else {
            content.setText(stmt.toString() + ";");
        }
    }
}
