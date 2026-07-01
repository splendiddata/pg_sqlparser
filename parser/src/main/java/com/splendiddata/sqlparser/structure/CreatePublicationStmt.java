/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2026
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
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.PublicationObjSpecType;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * 
 * @author Splendid Data Product Development B.V.
 */
@XmlRootElement(namespace = "parser")
public class CreatePublicationStmt extends AbstractPublicationStmt {

    /**
     * Optional list of tables to add
     * 
     * @deprecated since Postgres 15 - is replaced by @{link #pubobjects}
     */
    @XmlElementWrapper(name = "tables")
    @XmlElement(name = "table")
    @Deprecated(since = "Postgres 15", forRemoval = true)
    public List<RangeVar> tables;

    /**
     * Constructor
     */
    public CreatePublicationStmt() {
        super(NodeTag.T_CreatePublicationStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterPublicationStmt to copy
     */
    public CreatePublicationStmt(CreatePublicationStmt original) {
        super(original);
        if (original.tables != null) {
            this.tables = original.tables.clone();
        }
    }

    @Override
    public CreatePublicationStmt clone() {
        CreatePublicationStmt clone = (CreatePublicationStmt) super.clone();
        if (tables != null) {
            clone.tables = tables.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder().append("create publication ")
                .append(ParserUtil.identifierToSql(pubname));
        if (for_all_tables) {
            result.append(" for all tables");
        } else if (for_all_sequences) {
            result.append(" for all sequences");
        } else if (tables != null && !tables.isEmpty()) {
            String separator = " for table ";
            for (RangeVar table : tables) {
                result.append(separator).append(table);
                separator = ", ";
            }
        }
        if (pubobjects != null && !pubobjects.isEmpty()) {
            String sep = " for ";
            List<PublicationObjSpec> exclTables = new List<>();
            for (PublicationObjSpec pubObject : pubobjects) {
                if (PublicationObjSpecType.PUBLICATIONOBJ_EXCEPT_TABLE.equals(pubObject.pubobjtype)) {
                    exclTables.add(pubObject);
                } else {
                    result.append(sep).append(pubObject);
                    sep = ", ";
                }
            }
            if (!exclTables.isEmpty()) {
                result.append(" except ").append(exclTables);
            }
        }
        if (options != null && !options.isEmpty()) {
            String separator = " with (";
            for (DefElem opt : options) {
                result.append(separator).append(opt.defname);
                if (opt.arg != null) {
                    result.append(" = '").append(opt.arg).append('\'');
                }
                separator = ", ";
            }
            result.append(')');
        }
        return result.toString();
    }
}
