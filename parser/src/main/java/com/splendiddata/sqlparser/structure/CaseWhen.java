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
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * The "when" part in a case clause. Copied from /postgresql-9.3.4/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CaseWhen extends Node {

    /** condition expression */
    @XmlElement
    public Expr expr;

    /** substitution result */
    @XmlElement
    public Expr result;

    @Override
    public String toString() {
        return new StringBuilder("when ").append(expr).append(" then ").append(result).toString();
    }

    /**
     * Constructor
     */
    public CaseWhen() {
        super(NodeTag.T_CaseWhen);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The CaseWhen to copy
     */
    public CaseWhen(CaseWhen toCopy) {
        super(toCopy);
        if (toCopy.expr != null) {
            this.expr = toCopy.expr.clone();
        }
        if (toCopy.result != null) {
            this.result = toCopy.result.clone();
        }
    }

    @Override
    public CaseWhen clone() {
        CaseWhen clone = (CaseWhen) super.clone();
        if (expr != null) {
            clone.expr = expr.clone();
        }
        if (result != null) {
            clone.result = result.clone();
        }
        return clone;
    }
}
