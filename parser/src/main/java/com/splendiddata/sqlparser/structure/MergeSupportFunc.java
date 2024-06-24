/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2024
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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * MergeSupportFunc
 * <p>
 * A MergeSupportFunc is a merge support function expression that can only appear in the RETURNING list of a MERGE
 * command. It returns information about the currently executing merge action.
 * <p>
 * Currently, the only supported function is MERGE_ACTION(), which returns the command executed ("INSERT", "UPDATE", or
 * "DELETE").
 * <p>
 * Copied from postgresql-17beta1/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 17
 */
@XmlRootElement(namespace = "parser")
public class MergeSupportFunc extends Expr {

    @XmlElement
    public Expr xpr;

    /** type Oid of result */
    @XmlTransient
    public Oid msftype;

    /** OID of collation, or InvalidOid if none */
    @XmlTransient
    public Oid msfcollid;

    /**
     * Constructor
     */
    public MergeSupportFunc() {
        super(NodeTag.T_MergeSupportFunc);
    }

    /**
     * Shallow copy constructor
     *
     * @param original
     *            The MergeStmt to copy
     */
    public MergeSupportFunc(MergeSupportFunc original) {
        super(original);
        if (original.xpr != null) {
            this.xpr = original.xpr.clone();
        }
        if (original.msftype != null) {
            this.msftype = original.msftype;
        }
        if (original.msfcollid != null) {
            this.msfcollid = original.msfcollid.clone();
        }
    }

    @Override
    public MergeSupportFunc clone() {
        MergeSupportFunc clone = (MergeSupportFunc) super.clone();
        if (xpr != null) {
            clone.xpr = xpr.clone();
        }
        if (msfcollid != null) {
            clone.msfcollid = msfcollid.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        if (xpr != null) {
            return "????? Please implement xpr in " + this.getClass().getName() + ".toString() for "
                    + ParserUtil.stmtToXml(this) + " ?????";
        }
        return "merge_action()";
    }
}
