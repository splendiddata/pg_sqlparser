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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * 
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class AlterCollationStmt extends Node {
    @XmlTransient
    public List<Value> collname;

    /**
     * Constructor
     */
    public AlterCollationStmt() {
        super(NodeTag.T_AlterCollationStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterCollationStmt to copy
     */
    public AlterCollationStmt(AlterCollationStmt original) {
        super(original);
        if (original.collname != null) {
            this.collname = original.collname.clone();
        }
    }

    @Override
    public AlterCollationStmt clone() {
        AlterCollationStmt clone = (AlterCollationStmt) super.clone();
        if (collname != null) {
            clone.collname = collname.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return new StringBuilder("alter collation ").append(ParserUtil.nameToSql(collname)).append(" refresh version")
                .toString();
    }

    /**
     * Returns the qualified collname for debugging purposes
     *
     * @return String the (qualified) name
     */
    @XmlAttribute(name = "collname")
    private String getQualifiedCollationName() {
        if (collname == null) {
            return null;
        }
        return ParserUtil.nameToSql(collname);
    }
}
