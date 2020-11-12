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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CreateOpFamilyStmt extends Node {
    /** qualified name (list of Value strings) */
    @XmlElementWrapper(name = "opfamilyname")
    @XmlElement(name = "nameNode")
    public List<Value> opfamilyname;

    /** name of index AM opfamily is for */
    @XmlAttribute
    public String amname;

    /**
     * Constructor
     */
    public CreateOpFamilyStmt() {
        super(NodeTag.T_CreateOpFamilyStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateOpFamilyStmt to copy
     */
    public CreateOpFamilyStmt(CreateOpFamilyStmt original) {
        super(original);
        if (original.opfamilyname != null) {
            this.opfamilyname = original.opfamilyname.clone();
        }
        this.amname = original.amname;
    }

    @Override
    public CreateOpFamilyStmt clone() {
        CreateOpFamilyStmt clone = (CreateOpFamilyStmt) super.clone();
        if (opfamilyname != null) {
            clone.opfamilyname = opfamilyname.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return new StringBuilder("create operator family ").append(ParserUtil.nameToSql(opfamilyname)).append(" using ")
                .append(ParserUtil.identifierToSql(amname)).toString();
    }
}
