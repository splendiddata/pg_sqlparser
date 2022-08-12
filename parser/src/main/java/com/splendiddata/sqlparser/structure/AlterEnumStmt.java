/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2022
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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Initially copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class AlterEnumStmt extends Node {
    /** qualified name (list of Value strings) */
    @XmlTransient
    public List<Value> typeName;

    /**
     * old enum value's name, if renaming
     * 
     * @since 5.0
     */
    @XmlAttribute
    public String oldVal;

    /** new enum value's name */
    @XmlAttribute
    public String newVal;

    /** neighboring enum value, if specified */
    @XmlAttribute
    public String newValNeighbor;

    /** place new enum value after neighbor? */
    @XmlAttribute
    public boolean newValIsAfter;

    /**
     * no error if new already exists
     * 
     * @since 5.0
     */
    @XmlAttribute
    public boolean skipIfNewValExists;

    public AlterEnumStmt() {
        super(NodeTag.T_AlterEnumStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterEnumStmt to copy
     */
    public AlterEnumStmt(AlterEnumStmt original) {
        super(original);
        if (original.typeName != null) {
            this.typeName = original.typeName.clone();
        }
        this.oldVal = original.oldVal;
        this.newVal = original.newVal;
        this.newValNeighbor = original.newValNeighbor;
        this.newValIsAfter = original.newValIsAfter;
        this.skipIfNewValExists = original.skipIfNewValExists;
    }

    @Override
    public AlterEnumStmt clone() {
        AlterEnumStmt clone = (AlterEnumStmt) super.clone();
        if (typeName != null) {
            clone.typeName = typeName.clone();
        }
        return clone;
    }

    @XmlAttribute(name = "typeName")
    private String getQualifiedTypeName() {
        return ParserUtil.nameToSql(typeName);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter type ").append(ParserUtil.nameToSql(typeName));
        if (oldVal == null) {
            result.append(" add value");

            if (newVal != null) {
                result.append(" '").append(newVal).append('\'');
            }

            if (newValNeighbor != null) {
                if (newValIsAfter) {
                    result.append(" after ");
                } else {
                    result.append(" before ");
                }
                result.append('\'').append(newValNeighbor).append('\'');
            }
        } else {
            result.append(" rename value '").append(oldVal).append("' to '").append(newVal).append('\'');
        }

        return result.toString();
    }
}
