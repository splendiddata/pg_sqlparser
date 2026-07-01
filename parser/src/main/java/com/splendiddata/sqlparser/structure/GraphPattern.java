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
import com.splendiddata.sqlparser.enums.GraphElementPatternKind;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-Postgres 19beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 19beta1
 */
@XmlRootElement(namespace = "parser")
public class GraphPattern extends Node {

    @XmlElementWrapper(name = "path_pattern_list")
    @XmlElement(name = "path_pattern")
    public List<List<GraphElementPattern>> path_pattern_list;

    @XmlElement
    public Node whereClause;

    /**
     * Constructor
     */
    public GraphPattern() {
        super(NodeTag.T_GraphPattern);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The GraphPattern to copy
     */
    public GraphPattern(GraphPattern original) {
        super(original);
        if (original.path_pattern_list != null) {
            this.path_pattern_list = original.path_pattern_list.clone();
        }
        if (original.whereClause != null) {
            this.whereClause = original.whereClause.clone();
        }
    }

    @Override
    public GraphPattern clone() {
        GraphPattern clone = (GraphPattern) super.clone();
        if (path_pattern_list != null) {
            clone.path_pattern_list = path_pattern_list.clone();
        }
        if (whereClause != null) {
            clone.whereClause = whereClause.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("match "); 
        int initialLength = result.length();
        String separator = "";

        if (path_pattern_list != null && !path_pattern_list.isEmpty()) {
            for (List<GraphElementPattern> path_pattern : path_pattern_list) {
                for (GraphElementPattern pattern : path_pattern) {
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
                separator = " ";
            }
        }
        if (result.length() == initialLength) {
            result.append("()");
        }
        if (whereClause != null) {
            result.append(" where ").append(whereClause);
        }
        return result.toString();
    }

}
