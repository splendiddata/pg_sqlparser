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

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * RangeTableFunc - raw form of "table functions" such as XMLTABLE
 * <p>
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class RangeTableFunc extends Node {
    /** does it have LATERAL prefix? */
    @XmlAttribute
    public boolean lateral;

    /** document expression */
    @XmlElement
    public Node docexpr;

    /** row generator expression */
    @XmlElement
    public Node rowexpr;

    /** list of namespaces as ResTarget */
    @XmlElementWrapper(name = "namespaces")
    @XmlElement(name = "namespace")
    public List<ResTarget> namespaces;

    /** list of RangeTableFuncCol */
    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    public List<RangeTableFuncCol> columns;

    /** table alias &amp; optional column aliases */
    public Alias alias;

    /**
     * Constructor
     */
    public RangeTableFunc() {
        super(NodeTag.T_RangeTableFunc);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            the RangeTableFunc to copy
     */
    public RangeTableFunc(RangeTableFunc original) {
        super(original);
        this.lateral = original.lateral;
        if (original.docexpr != null) {
            this.docexpr = original.docexpr.clone();
        }
        if (original.rowexpr != null) {
            this.rowexpr = original.rowexpr.clone();
        }
        if (original.namespaces != null) {
            this.namespaces = original.namespaces.clone();
        }
        if (original.columns != null) {
            this.columns = original.columns.clone();
        }
        if (original.alias != null) {
            this.alias = original.alias.clone();
        }
    }

    @Override
    public RangeTableFunc clone() {
        RangeTableFunc clone = (RangeTableFunc) super.clone();
        if (docexpr != null) {
            clone.docexpr = docexpr.clone();
        }
        if (rowexpr != null) {
            clone.rowexpr = rowexpr.clone();
        }
        if (namespaces != null) {
            clone.namespaces = namespaces.clone();
        }
        if (columns != null) {
            clone.columns = columns.clone();
        }
        if (docexpr != null) {
            clone.docexpr = docexpr.clone();
        }
        if (alias != null) {
            clone.alias = alias.clone();
        }

        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String space = "";
        if (lateral) {
            result.append("lateral ");
        }
        result.append("xmltable(");
        if (namespaces != null && !namespaces.isEmpty()) {
            result.append("xmlnamespaces(");
            String comma = "";
            for (ResTarget namespace : namespaces) {
                result.append(comma);
                comma = ", ";
                if (namespace.name == null) {
                    result.append("default ");
                }
                result.append(namespace);
            }
            result.append("),");
            space = " ";
        }
        if (rowexpr != null) {
            result.append(space);
            if (rowexpr instanceof A_Expr) {
                result.append('(').append(rowexpr).append(')');
            } else {
                result.append(rowexpr);
            }
            space = " ";
        }
        if (docexpr != null) {
            result.append(space).append("passing ").append(docexpr);
            space = " ";
        }

        if (columns != null) {
            String comma = " columns ";
            for (RangeTableFuncCol column : columns) {
                result.append(comma).append(column);
                comma = ", ";
            }
        }
        result.append(')');
        if (alias != null) {
            result.append(" as ").append(alias);
        }
        return result.toString();
    }
}
