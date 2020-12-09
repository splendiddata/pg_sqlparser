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
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.A_Expr_Kind;
import com.splendiddata.sqlparser.enums.BoolExprType;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Boolean expression, copied from /postgresql-9.5alpha2/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class BoolExpr extends ConditionalExpr {

    @XmlAttribute
    public BoolExprType boolop;

    /** arguments to this expression */
    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    public List<Node> args;
    
    /**
     * Constructor
     */
    public BoolExpr() {
        super(NodeTag.T_BoolExpr);
    }
    
    /**
     * Copy constructor
     *
     * @param toCopy The expression that is to be copied
     */
    public BoolExpr(BoolExpr toCopy) {
        super(toCopy);
        
        this.boolop = toCopy.boolop;
        this.args = toCopy.args.clone();
    }

    @Override
    public String toString() {
        if (BoolExprType.NOT_EXPR.equals(boolop)) {
            return notToString();
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Node arg : args) {
            if (first) {
                first = false;
            } else {
                result.append(boolop);
            }
            addArg(arg, result);
        }
        return result.toString();
    }

    @Override
    public BoolExpr clone() {
        BoolExpr clone = (BoolExpr) super.clone();
        if (args != null) {
            clone.args = args.clone();
        }
        return clone;
    }

    /**
     * Appends the arg to addTo. If necessary, it will be put in parenthesis
     *
     * @param arg
     *            The argument to be appended
     * @param addTo
     *            The StringBuilder to append to
     */
    private static void addArg(Node arg, StringBuilder addTo) {
        switch (arg.type) {
        case T_BoolExpr:
            addTo.append('(').append(arg).append(')');
            break;
        case T_A_Expr:
            addTo.append(arg);
            break;
        default:
            addTo.append(arg);
            break;
        }
    }

    /**
     * Returns a "NOT" expression in a String format
     *
     * @return String the not expression
     */
    private String notToString() {
        StringBuilder result = new StringBuilder();
        Node arg = args.get(0);
        
        if (arg instanceof A_Expr && A_Expr_Kind.AEXPR_DISTINCT.equals(((A_Expr) arg).kind)) {
            /*
             * Not distinct has its own special syntax
             */
            addArg(((A_Expr) arg).lexpr, result);
            result.append(" is not distinct from ");
            addArg(((A_Expr) arg).rexpr, result);
        } else {
            addArg(arg, result.append("not "));
        }

        return result.toString();
    }
}
