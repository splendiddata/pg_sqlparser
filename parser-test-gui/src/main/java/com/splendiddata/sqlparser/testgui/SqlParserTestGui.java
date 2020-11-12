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

import com.splendiddata.sqlparser.ParserUtil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SqlParserTestGui extends Application {

    @Override
    public void start(Stage theStage) throws Exception {

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        //set Stage boundaries to visible bounds of the main screen
        theStage.setWidth(primaryScreenBounds.getWidth() * .8);
        theStage.setHeight(primaryScreenBounds.getHeight() * .8);
        theStage.setX(primaryScreenBounds.getWidth() * .1);
        theStage.setY(primaryScreenBounds.getHeight() * .1);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SqlParserTestGui.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/SqlParserTestGui.css").toExternalForm());
        theStage.setTitle("SQL parser test UI " + ParserUtil.getParserVersion());
        theStage.setScene(scene);
        theStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application. main() serves only as fallback in case the
     * application can not be launched through deployment artifacts, e.g., in IDEs with limited FX support. NetBeans
     * ignores main().
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
