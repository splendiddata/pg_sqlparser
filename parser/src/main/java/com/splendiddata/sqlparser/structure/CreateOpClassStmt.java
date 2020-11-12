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
public class CreateOpClassStmt extends Node {

    /** qualified name (list of Value strings) */
    @XmlElementWrapper(name = "opclassname")
    @XmlElement(name = "opClassNameNode")
    public List<Value> opclassname;

    /** qualified name (ditto); NIL if omitted */
    @XmlElementWrapper(name = "opfamilyname")
    @XmlElement(name = "familyNameNode")
    public List<Value> opfamilyname;

    /** name of index AM opclass is for */
    @XmlAttribute
    public String amname;

    /** datatype of indexed column */
    @XmlElement
    public TypeName datatype;

    /** List of CreateOpClassItem nodes */
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    public List<CreateOpClassItem> items;

    /** Should be marked as default for type? */
    @XmlAttribute
    public boolean isDefault;

    /**
     * Constructor
     */
    public CreateOpClassStmt() {
        super(NodeTag.T_CreateOpClassStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateOpClassStmt to copy
     */
    public CreateOpClassStmt(CreateOpClassStmt original) {
        super(original);
        if (original.opclassname != null) {
            this.opclassname = original.opclassname.clone();
        }
        if (original.opfamilyname != null) {
            this.opfamilyname = original.opfamilyname.clone();
        }
        this.amname = original.amname;
        if (original.datatype != null) {
            this.datatype = original.datatype.clone();
        }
        if (original.items != null) {
            this.items = original.items.clone();
        }
        this.isDefault = original.isDefault;
    }

    @Override
    public CreateOpClassStmt clone() {
        CreateOpClassStmt clone = (CreateOpClassStmt) super.clone();
        if (opclassname != null) {
            clone.opclassname = opclassname.clone();
        }
        if (opfamilyname != null) {
            clone.opfamilyname = opfamilyname.clone();
        }
        if (datatype != null) {
            clone.datatype = datatype.clone();
        }
        if (items != null) {
            clone.items = items.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create operator class ").append(ParserUtil.nameToSql(opclassname));

        if (isDefault) {
            result.append(" default");
        }

        result.append(" for type ").append(datatype).append(" using ").append(ParserUtil.identifierToSql(amname));

        if (opfamilyname != null) {
            result.append(" family ").append(ParserUtil.nameToSql(opfamilyname));
        }

        String separator = " as ";
        for (CreateOpClassItem item : items) {
            result.append(separator).append(item);
            separator = ", ";
        }

        return result.toString();
    }
}
