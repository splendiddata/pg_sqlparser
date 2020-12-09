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

package com.splendiddata.sqlparser.structure;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.ImportForeignSchemaType;

/**
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class ImportForeignSchemaStmt extends Node {
    /** FDW server name */
    @XmlAttribute
    public String server_name;

    /** remote schema name to query */
    @XmlAttribute
    public String remote_schema;

    /** local schema to create objects in */
    @XmlAttribute
    public String local_schema;

    /** type of table list */
    @XmlAttribute
    public ImportForeignSchemaType list_type;

    /** List of RangeVar */
    @XmlElementWrapper(name = "table_list")
    @XmlElement(name = "table")
    public List<String> table_list;

    /** list of options to pass to FDW */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    @Override
    public String toString() {
        return "TODO: implement com.splendiddata.sqlparser.structure.ImportForeignSchemaStmt.toString()";
    }

    @Override
    public ImportForeignSchemaStmt clone() {
        ImportForeignSchemaStmt clone = (ImportForeignSchemaStmt) super.clone();
        if (table_list != null) {
            clone.table_list = table_list.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }
}
