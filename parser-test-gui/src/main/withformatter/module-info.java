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

module com.splendiddata.sqlparser.testgui {
    exports com.splendiddata.sqlparser.testgui;
    opens com.splendiddata.sqlparser.testgui;
    
    requires java.base;
    requires transitive jakarta.xml.bind;
    requires transitive org.apache.logging.log4j;
    requires transitive com.splendiddata.sqlparser;
    requires transitive com.splendiddata.sqlparser.enums;
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive com.splendiddata.pgcode.formatter;
}