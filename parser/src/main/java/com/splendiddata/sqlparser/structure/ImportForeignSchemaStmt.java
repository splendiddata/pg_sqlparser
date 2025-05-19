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

package com.splendiddata.sqlparser.structure;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.ImportForeignSchemaType;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

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
    public List<RangeVar> table_list;

    /** list of options to pass to FDW */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     */
    public ImportForeignSchemaStmt() {
        super(NodeTag.T_ImportForeignSchemaStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The ImportForeignSchemaStmt to copy
     */
    public ImportForeignSchemaStmt(ImportForeignSchemaStmt original) {
        super(original);
        this.server_name = original.server_name;
        this.remote_schema = original.remote_schema;
        this.local_schema = original.local_schema;
        this.list_type = original.list_type;
        if (original.table_list != null) {
            this.table_list = original.table_list.clone();
        }
        if (original.options != null) {
            this.options = original.options.clone();
        }

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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("import foreign schema ").append(ParserUtil.identifierToSql(remote_schema));
        String sep = "";
        switch (list_type) {
        case FDW_IMPORT_SCHEMA_ALL:
            break;
        case FDW_IMPORT_SCHEMA_EXCEPT:
            sep = " except";
            break;
        case FDW_IMPORT_SCHEMA_LIMIT_TO:
            sep = " limit to";
            break;
        default:
            result.append(ParserUtil.reportUnknownValue("list_tyope", list_type.name(), getClass()));
            break;
        }
        if (table_list != null && !table_list.isEmpty()) {
            sep += " (";
            for (RangeVar table : table_list) {
                result.append(sep).append(table);
                sep = ", ";
            }
            result.append(')');
        }
        result.append(" from server ").append(ParserUtil.identifierToSql(server_name)).append(" into ")
                .append(ParserUtil.identifierToSql(local_schema));
        if (options != null && !options.isEmpty()) {
            sep = " options (";
            for (DefElem option : options) {
                result.append(sep).append(option.defname);
                sep = ", ";
                if (option.arg != null) {
                    result.append(" '").append(option.arg).append('\'');
                }
            }
            result.append(')');
        }
        return result.toString();
    }
}
