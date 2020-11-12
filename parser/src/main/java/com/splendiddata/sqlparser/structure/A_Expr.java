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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.A_Expr_Kind;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Expression from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class A_Expr extends ConditionalExpr {
    @XmlAttribute
    public A_Expr_Kind kind;

    /** possibly-qualified name of operator */
    @XmlElementWrapper(name = "name")
    @XmlElement(name = "nameNode")
    public List<Value> name;

    /** left argument, or NULL if none */
    @XmlElement
    public Node lexpr;

    /** right argument, or NULL if none */
    @XmlElement
    public Node rexpr;

    /**
     * Constructor
     */
    public A_Expr() {
        super(NodeTag.T_A_Expr);
    }

    /**
     * <u>Shallow</u> copy constructor
     *
     * @param toCopy
     *            The epression to copy
     */
    public A_Expr(A_Expr toCopy) {
        super(toCopy);
        this.kind = toCopy.kind;
        this.name = toCopy.name;
        this.lexpr = toCopy.lexpr;
        this.rexpr = toCopy.rexpr;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        switch (kind) {
        case AEXPR_NULLIF:
            return result.append("nullif(").append(lexpr).append(", ").append(rexpr).append(')').toString();
        case AEXPR_IN:
            result.append(lexpr);
            if (name != null) {
                Value nameValue = name.get(0);
                if (nameValue != null && "<>".equals(nameValue.val.str)) {
                    result.append(" not");
                }
            }
            return result.append(" in ").append(rexpr).toString();

        case AEXPR_OP:
        case AEXPR_DISTINCT:
        case AEXPR_NOT_DISTINCT:
        case AEXPR_OF:
        case AEXPR_OP_ANY:
        case AEXPR_OP_ALL:
        case AEXPR_ILIKE:
            break;
        case AEXPR_BETWEEN:
            return result.append(lexpr).append(" between ").append(((List<Node>) rexpr).get(0)).append(" and ")
                    .append(((List<Node>) rexpr).get(1)).toString();
        case AEXPR_LIKE:
            result.append(lexpr);
            if (name.get(0).val.str.startsWith("!")) {
                result.append(" not");
            }
            result.append(" like ").append(rexpr);
            return result.toString();
        case AEXPR_NOT_BETWEEN:
            return result.append(lexpr).append(" not between ").append(((List<Node>) rexpr).get(0)).append(" and ")
                    .append(((List<Node>) rexpr).get(1)).toString();
        case AEXPR_SIMILAR:
            result.append(lexpr).append(" similar to ");
            if (rexpr != null && NodeTag.T_FuncCall.equals(rexpr.type)
                    && "pg_catalog.similar_to_escape".equals(ParserUtil.nameToSql(((FuncCall) rexpr).funcname))) {
                result.append(((FuncCall) rexpr).args.get(0));
                if (((FuncCall) rexpr).args.size() > 1) {
                    result.append(" escape ").append(((FuncCall) rexpr).args.get(1));
                }
            } else {
                result.append(rexpr);
            }
            return result.toString();
        case AEXPR_BETWEEN_SYM:
            return result.append(lexpr).append(" between symmetric ").append(((List<Node>) rexpr).get(0)).append(" and ")
                    .append(((List<Node>) rexpr).get(1)).toString();
        case AEXPR_NOT_BETWEEN_SYM:
            return result.append(lexpr).append(" not between symmetric ").append(((List<Node>) rexpr).get(0)).append(" and ")
                    .append(((List<Node>) rexpr).get(1)).toString();
        case AEXPR_PAREN:
        default:
            throw new AssertionError("Unsupported A_Expr_Kind: " + kind);
        }

        String leadingSpace = "";
        if (lexpr != null) {
            addExpression(result, leadingSpace, lexpr);
            leadingSpace = " ";
        }

        switch (kind) {
        case AEXPR_DISTINCT:
            result.append(leadingSpace).append("is distinct from");
            break;
        case AEXPR_NOT_DISTINCT:
            result.append(leadingSpace).append("is not distinct from");
            break;
        case AEXPR_OF:
            result.append(leadingSpace).append("is of");
            break;
        case AEXPR_IN:
            result.append(leadingSpace).append("in");
            break;
        case AEXPR_OP:
            addOperator(result, leadingSpace);
            break;
        case AEXPR_OP_ALL:
            addOperator(result, leadingSpace);
            result.append(' ').append("all");
            return result.append(" (").append(rexpr).append(')').toString();
        case AEXPR_OP_ANY:
            addOperator(result, leadingSpace);
            result.append(' ').append("any");
            return result.append(" (").append(rexpr).append(')').toString();
        case AEXPR_ILIKE:
            result.append(leadingSpace).append("ilike");
            break;
        default:
            throw new AssertionError("Unsupported A_Expr_Kind: " + kind);
        }

        leadingSpace = " ";

        return addExpression(result, leadingSpace, rexpr).toString();
    }

    @Override
    public A_Expr clone() {
        A_Expr clone = (A_Expr) super.clone();
        if (name != null) {
            clone.name = name.clone();
        }
        if (lexpr != null) {
            clone.lexpr = lexpr.clone();
        }
        if (rexpr != null) {
            clone.rexpr = rexpr.clone();
        }
        return clone;
    }

    /**
     * Returns the result StringBuilder with the expression added. If brackets are needed around the expression, they
     * will be added as well.
     *
     * @param result
     * @param leadingSpace
     * @param expression
     * @return result with the expression added
     */
    private static StringBuilder addExpression(StringBuilder result, String leadingSpace, Node expression) {
        if (expression != null) {
            if (expression.type == null) {
                if (expression instanceof GroupingFunc) {
                    return result.append(leadingSpace).append(expression);
                }
                result.append(" ??? expression.type == null in ").append(expression).append(" ???");
                return result;
            }
            switch (expression.type) {
            case T_ColumnRef:
            case T_A_Const:
            case T_FuncCall:
            case T_TypeCast:
            case T_SubLink:
            case T_A_Indirection:
            case T_RowExpr:
            case T_List:
            case T_SQLValueFunction:
                return result.append(leadingSpace).append(expression);
            case T_A_Expr:
                if (((A_Expr) expression).lexpr == null || ((A_Expr) expression).rexpr == null) {
                    return result.append(leadingSpace).append(expression);
                }
                return result.append(leadingSpace).append('(').append(expression).append(')');
            default:
                return result.append(leadingSpace).append('(').append(expression).append(')');
            }
        }
        return result;
    }

    /**
     * Adds the operator to the result StringBuilder
     *
     * @param result
     *            The StringBuilder to which to add the operator
     * @param leadingSpace
     *            An empty string for a right-unary operator, a space for a left-unary operator or a "normal" operator
     */
    private void addOperator(StringBuilder result, String leadingSpace) {
        if (name == null || name.isEmpty()) {
            return;
        }

        if (name.size() == 1) {
            result.append(leadingSpace).append(name.get(0).val.str);
            return;
        }

        result.append(leadingSpace).append("operator(").append(ParserUtil.identifierToSql(name.get(0).toString()))
                .append('.').append(name.get(1)).append(')');
    }
}
