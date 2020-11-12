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
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CreateCastStmt extends Node {
    @XmlElement
    public TypeName sourcetype;

    @XmlElement
    public TypeName targettype;

    @XmlElement
    public ObjectWithArgs func;

    @XmlAttribute
    public CoercionContext context;

    @XmlAttribute
    public boolean inout;

    /**
     * Constructor
     */
    public CreateCastStmt() {
        super(NodeTag.T_CreateCastStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateCastStmt to copy
     */
    public CreateCastStmt(CreateCastStmt original) {
        super(original);
        if (original.sourcetype != null) {
            this.sourcetype = original.sourcetype.clone();
        }
        if (original.targettype != null) {
            this.targettype = original.targettype.clone();
        }
        if (original.func != null) {
            this.func = original.func.clone();
        }
        this.context = original.context;
        this.inout = original.inout;
    }

    @Override
    public CreateCastStmt clone() {
        CreateCastStmt clone = (CreateCastStmt) super.clone();
        if (sourcetype != null) {
            clone.sourcetype = sourcetype.clone();
        }
        if (targettype != null) {
            clone.targettype = targettype.clone();
        }
        if (func != null) {
            clone.func = func.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create cast (").append(sourcetype).append(" as ").append(targettype).append(')');

        if (inout) {
            result.append(" with inout");
        } else {
            if (func == null) {
                result.append(" without function");
            } else {
                result.append(" with function ").append(func);
            }
        }

        if (CoercionContext.COERCION_ASSIGNMENT.equals(context)) {
            result.append(" as assignment");
        } else {
            result.append(" as implicit");
        }

        return result.toString();
    }
}
