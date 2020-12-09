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

package com.splendiddata.sqlparser.structure;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Create/Drop Table Space Statements
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class CreateTableSpaceStmt extends Node {
    @XmlAttribute
    public String tablespacename;

    @XmlElement
    public Node owner;

    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * This is the location on disk, not the location in the statement source
     */
    @SuppressWarnings("hiding")
    @XmlAttribute
    public String location;

    /**
     * Constructor
     */
    public CreateTableSpaceStmt() {
        super(NodeTag.T_CreateTableSpaceStmt);
    }

    /**
     * Copy onstructor
     *
     * @param original
     *            The CreateTableSpaceStmt to copy
     */
    public CreateTableSpaceStmt(CreateTableSpaceStmt original) {
        super(original);
        this.tablespacename = original.tablespacename;
        if (original.owner != null) {
            this.owner = original.owner.clone();
        }
        if (original.options != null) {
            this.options = original.options.clone();
        }
        this.location = original.location;
    }

    @Override
    public CreateTableSpaceStmt clone() {
        CreateTableSpaceStmt clone = (CreateTableSpaceStmt) super.clone();
        if (owner != null) {
            clone.owner = owner.clone();
        }
        if (this.options != null) {
            clone.options = this.options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create tablespace ").append(ParserUtil.identifierToSql(tablespacename));

        if (owner != null) {
            result.append(" owner ").append(owner);
        }

        result.append(" location ").append(ParserUtil.toSqlTextString(location));

        if (options != null) {
            result.append(" with (");
            String separator = "";
            for (DefElem option : options) {
                result.append(separator).append(option.defname);
                if (option.arg != null) {
                    result.append(" = ").append(option.arg);
                }
                separator = ", ";
            }
            result.append(')');
        }
        return result.toString();
    }
}
