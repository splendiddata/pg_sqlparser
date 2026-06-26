/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2026
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
import com.splendiddata.sqlparser.enums.BoolExprType;
import com.splendiddata.sqlparser.enums.GraphElementPatternKind;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-19beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 19beta1
 */
@XmlRootElement(namespace = "parser")
public class GraphElementPattern extends Node {

    @XmlAttribute
    public GraphElementPatternKind kind;

    @XmlAttribute
    public String variable;

    @XmlElement
    public Node labelexpr;

    @XmlElementWrapper(name = "subexpr")
    @XmlElement(name = "subexprElement")
    public List<GraphElementPattern> subexpr;

    @XmlElement
    public Node whereClause;

    @XmlElementWrapper(name = "quantifier")
    @XmlElement(name = "quantifierElement")
    public List<Node> quantifier;

    /**
     * Constructor
     */
    public GraphElementPattern() {
        super(NodeTag.T_GraphElementPattern);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            the GraphElementPattern to copy
     */
    public GraphElementPattern(GraphElementPattern original) {
        super(original);
        this.kind = original.kind;
        this.variable = original.variable;
        if (original.labelexpr != null) {
            this.labelexpr = original.labelexpr.clone();
        }
        if (original.subexpr != null) {
            this.subexpr = original.subexpr.clone();
        }
        if (original.whereClause != null) {
            this.whereClause = original.whereClause.clone();
        }
        if (original.quantifier != null) {
            this.quantifier = original.quantifier.clone();
        }
    }

    @Override
    public GraphElementPattern clone() {
        GraphElementPattern clone = (GraphElementPattern) super.clone();
        if (labelexpr != null) {
            clone.labelexpr = labelexpr.clone();
        }
        if (subexpr != null) {
            clone.subexpr = subexpr.clone();
        }
        if (whereClause != null) {
            clone.whereClause = whereClause.clone();
        }
        if (quantifier != null) {
            clone.quantifier = quantifier.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        //        if (labelexpr == null) {
        //            return "";
        //        }
        StringBuilder result = new StringBuilder();
        String separator = "";

        result.append(switch (kind) {
        case EDGE_PATTERN_ANY -> "-[";
        case EDGE_PATTERN_LEFT -> "<-[";
        case EDGE_PATTERN_RIGHT -> "-[";
        case VERTEX_PATTERN, PAREN_EXPR -> "(";
        default -> ParserUtil.reportUnknownValue("kind", kind, getClass());
        });

        if (variable != null) {
            result.append(separator).append(variable);
            separator = " ";
        }

        if (labelexpr != null) {
            result.append(separator).append("is ");
            if (labelexpr instanceof BoolExpr booleanExpression
                    && BoolExprType.OR_EXPR.equals(booleanExpression.boolop)) {
                /*
                 * An OR expression should be written as "a | b" instead of "a or b" in a graph context
                 */
                boolean first = true;
                for (Node arg : booleanExpression.args) {
                    if (first) {
                        first = false;
                    } else {
                        result.append('|');
                    }
                    switch (arg.type) {
                    case T_BoolExpr -> result.append('(').append(arg).append(')');
                    case T_A_Expr -> result.append(arg);
                    default -> result.append(arg);
                    }
                }

            } else {
                result.append(labelexpr);
            }
            separator = " ";
        }

        if (subexpr != null && !subexpr.isEmpty()) {
            separator = "";
            for (GraphElementPattern pattern : subexpr) {
                String str = pattern.toString();
                /* It looks like a subexpr can contain empty pattens */
                if (!("()".equals(str) || str.contains("[]"))) {
                    if (GraphElementPatternKind.VERTEX_PATTERN.equals(pattern.kind)) {
                        result.append(separator);
                    }
                    result.append(str);
                }
                separator = switch (pattern.kind) {
                case EDGE_PATTERN_ANY, EDGE_PATTERN_LEFT, PAREN_EXPR, VERTEX_PATTERN -> "-";
                case EDGE_PATTERN_RIGHT -> "->";
                default -> ParserUtil.reportUnknownValue("pattern.kind", pattern.kind, getClass());
                };
            }
        }

        if (whereClause != null) {
            result.append(" where ").append(whereClause);
        }

        if (quantifier != null && !quantifier.isEmpty()) {
            result.append(ParserUtil.reportUnknownValue("quantifier", quantifier, getClass()));
        }

        result.append(switch (kind) {
        case EDGE_PATTERN_ANY, EDGE_PATTERN_LEFT, EDGE_PATTERN_RIGHT -> "]";
        case VERTEX_PATTERN, PAREN_EXPR -> ")";
        default -> "";
        });
        return result.toString();
    }
}
