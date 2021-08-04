/*
 * Copyright (c) Splendid Data Product Development B.V. 2021
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

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * PL/pgSQL Assignment Statement
 * <p>
 * Like SelectStmt, this is transformed into a SELECT Query.<br>
 * However, the targetlist of the result looks more like an UPDATE.
 * </p>
 * <p>
 * Copied from /postgresql-14beta2/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 14.0
 */
@XmlRootElement(namespace = "parser")
public class PLAssignStmt extends Node {

    /** initial column name */
    @XmlAttribute
    public String name;

    /** subscripts and field names, if any */
    @XmlElementWrapper(name = "indirection")
    @XmlElement(name = "indirectionNode")
    public List<Value> indirection;

    /** number of names to use in ColumnRef */
    @XmlAttribute
    public int nnames;

    /** the PL/pgSQL expression to assign */
    @XmlElement
    public SelectStmt val;

    /**
     * Constructor
     */
    public PLAssignStmt() {
        super(NodeTag.T_PLAssignStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public PLAssignStmt(PLAssignStmt original) {
        super(original);
        this.name = original.name;
        if (original.indirection != null) {
            this.indirection = original.indirection.clone();
        }
        this.nnames = original.nnames;
        if (original.val != null) {
            this.val = original.val.clone();
        }
    }

    @Override
    public PLAssignStmt clone() {
        PLAssignStmt clone = (PLAssignStmt) super.clone();
        if (indirection != null) {
            clone.indirection = indirection.clone();
        }
        if (val != null) {
            clone.val = val.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return "Please implement " + getClass().getName() + ".toString()";
    }
}
