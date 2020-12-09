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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CompositeTypeStmt extends Node {

    /** the composite type to be created */
    @XmlElement
    public RangeVar typevar;

    /** list of ColumnDef nodes */
    @XmlElementWrapper(name = "coldeflist")
    @XmlElement(name = "coldef")
    public List<ColumnDef> coldeflist;

    /**
     * Constructor
     */
    public CompositeTypeStmt() {
        super(NodeTag.T_CompositeTypeStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CompositeTypeStmt to copy
     */
    public CompositeTypeStmt(CompositeTypeStmt original) {
        super(original);
        if (original.typevar != null) {
            this.typevar = original.typevar.clone();
        }
        if (original.coldeflist != null) {
            this.coldeflist = original.coldeflist.clone();
        }
    }

    @Override
    public CompositeTypeStmt clone() {
        CompositeTypeStmt clone = (CompositeTypeStmt) super.clone();
        if (typevar != null) {
            clone.typevar = typevar.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create type ").append(typevar);

        if (coldeflist == null) {
            result.append(" as ()");
        } else {
            result.append(" as ").append(coldeflist);
        }

        return result.toString();
    }
}
