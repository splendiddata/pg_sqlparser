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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Alter Type Set ( this-n-that )
 * <p>
 * Copied from postgresql-13beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 8.0
 */
@XmlRootElement(namespace = "parser")
public class AlterTypeStmt extends Node {

    /** type name (possibly qualified) */
    @XmlElementWrapper(name = "typeName")
    @XmlElement(name = "typeNameNode")
    public List<Value> typeName;

    /** List of DefElem nodes */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     *
     */
    public AlterTypeStmt() {
        super(NodeTag.T_AlterTypeStmt);
    }

    /**
     * Constructor
     *
     * @param original
     *            The AlterTypeStmt to copy
     */
    public AlterTypeStmt(AlterTypeStmt original) {
        super(original);
        if (original.typeName != null) {
            this.typeName = original.typeName.clone();
        }
        if (original.options != null) {
            this.options = original.options.clone();
        }
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public AlterTypeStmt clone() {
        AlterTypeStmt clone = (AlterTypeStmt) super.clone();
        if (this.typeName != null) {
            clone.typeName = this.typeName.clone();
        }
        if (this.options != null) {
            clone.options = this.options.clone();
        }
        return clone;
    }

    /**
     * @see java.lang.Object#toString()
     *
     * @return String The original statement
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("alter type ").append(ParserUtil.nameToSql(typeName));
        if (options != null) {
            String separator = " set (";
            for (DefElem option : options) {
                result.append(separator).append(option.defname);
                separator = ", ";
                if (option.arg != null) {
                    result.append(" = ").append(option.arg);
                }
            }
            result.append(')');
        }
        return result.toString();
    }

}
