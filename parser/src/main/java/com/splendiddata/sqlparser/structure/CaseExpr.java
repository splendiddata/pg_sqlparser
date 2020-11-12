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

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Case when... expression. Copied from /postgresql-9.3.4/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CaseExpr extends Expr {

    /** type of expression result */
    @XmlElement
    public Oid casetype;

    /** implicit equality comparison argument */
    @XmlElement
    public Expr arg;

    /** the arguments (list of WHEN clauses) */
    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    public List<CaseWhen> args;

    /** the default result (ELSE clause) */
    @XmlElement
    public Expr defresult;

    /**
     * Constructor
     */
    public CaseExpr() {
        super(NodeTag.T_CaseExpr);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The CaseExpr to copy
     */
    public CaseExpr(CaseExpr toCopy) {
        super(toCopy);
        this.casetype = toCopy.casetype;
        if (toCopy.arg != null) {
            this.arg = toCopy.arg.clone();
        }
        if (toCopy.args != null) {
            this.args = new List<>();
            for (CaseWhen w : toCopy.args) {
                this.args.add(w.clone());
            }
        }
        if (toCopy.defresult != null) {
            this.defresult = toCopy.defresult.clone();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("case ");
        if (arg != null) {
            result.append(arg).append(' ');
        }
        if (args != null) {
            for (CaseWhen when : args) {
                result.append(when).append(' ');
            }
        }
        if (defresult != null) {
            result.append(" else ").append(defresult);
        }
        result.append(" end");
        return result.toString();
    }

    @Override
    public CaseExpr clone() {
        CaseExpr clone = (CaseExpr) super.clone();
        if (casetype != null) {
            clone.casetype = casetype.clone();
        }
        if (arg != null) {
            clone.arg = arg.clone();
        }
        if (args != null) {
            clone.args = args.clone();
        }
        if (defresult != null) {
            clone.defresult = defresult.clone();
        }
        return clone;
    }
}
