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

import java.util.Iterator;

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 2.1.0
 */
@XmlRootElement(namespace = "parser")
public class RangeFunction extends Node {
    /** does it have LATERAL prefix? */
    @XmlAttribute
    public boolean lateral;

    /** does it have WITH ORDINALITY suffix? */
    @XmlAttribute
    public boolean ordinality;

    /** is result of ROWS FROM() syntax? */
    @XmlAttribute
    public boolean is_rowsfrom;

    /** per-function information, see above */
    @XmlElementWrapper(name = "functions")
    @XmlElement(name = "function")
    public List<List<Node>> functions;

    /** table alias &amp; optional column aliases */
    @XmlElement
    public Alias alias;

    /**
     * list of ColumnDef nodes to describe result of function returning RECORD
     */
    @XmlElementWrapper(name = "coldeflist")
    @XmlAnyElement
    public List<ColumnDef> coldeflist;

    /**
     * Constructor
     */
    public RangeFunction() {
        super(NodeTag.T_RangeFunction);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The RangeFunction to copy
     */
    public RangeFunction(RangeFunction toCopy) {
        super(toCopy);
        this.lateral = toCopy.lateral;
        this.ordinality = toCopy.ordinality;
        this.is_rowsfrom = toCopy.is_rowsfrom;
        if (toCopy.functions != null) {
            this.functions = toCopy.functions.clone();
        }
        if (toCopy.alias != null) {
            this.alias = toCopy.alias.clone();
        }
        if (toCopy.coldeflist != null) {
            this.coldeflist = toCopy.coldeflist.clone();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (lateral) {
            result.append("lateral ");
        }
        if (is_rowsfrom) {
            result.append("rows from ");
        }
        if (functions != null) {
            int n = 0;
            for (Node function : functions) {
                if (function != null) {
                    n++;
                }
            }

            boolean addBrackets = is_rowsfrom || n > 1;
            String separator = addBrackets ? "(" : "";

            for (List<Node> function : functions) {
                result.append(separator);
                separator = ", ";

                Iterator<Node> it = function.iterator();
                if (it.hasNext()) {
                    result.append(it.next());
                }

                if (it.hasNext()) {
                    Node next = it.next();
                    if (next != null) {
                        result.append(" as ").append(next);
                    }
                }
            }

            if (addBrackets) {
                result.append(')');
            }
        }

        if (ordinality) {
            result.append(" with ordinality");
        }

        String separator = " as ";
        if (alias != null) {
            result.append(separator).append(alias);
            separator = " ";
        }

        if (coldeflist != null) {
            result.append(separator).append(coldeflist);
        }

        return result.toString();
    }

    @Override
    public RangeFunction clone() {
        RangeFunction clone = (RangeFunction) super.clone();
        if (this.functions != null) {
            clone.functions = this.functions.clone();
        }
        if (alias != null) {
            clone.alias = alias.clone();
        }
        if (coldeflist != null) {
            clone.coldeflist = coldeflist.clone();
        }
        return clone;
    }
}
