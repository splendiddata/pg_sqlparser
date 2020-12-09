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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;

/**
 * TableSampleClause - TABLESAMPLE appearing in a transformed FROM clause
 * <p>
 * Unlike RangeTableSample, this is a subnode of the relevant RangeTblEntry.
 * </p>
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class RangeTableSample extends Node {
    /** relation to be sampled */
    @XmlElement
    public Node relation;

    /** sampling method name (possibly qualified) */
    @XmlElementWrapper(name = "method")
    @XmlElement(name = "methodNameNode")
    public List<Node> method;

    /** tablesample argument expression(s) */
    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    public List<DefElem> args;

    /** REPEATABLE expression, or NULL if none */
    @XmlElement
    public Node repeatable;

    @Override
    public RangeTableSample clone() {
        RangeTableSample clone = (RangeTableSample) super.clone();
        if (relation != null) {
            clone.relation = relation;
        }
        if (method != null) {
            clone.method = method;
        }
        if (args != null) {
            clone.args = args.clone();
        }
        if (repeatable != null) {
            clone.repeatable = repeatable.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(relation).append(" tablesample");

        if (method != null) {
            result.append(' ').append(ParserUtil.nameToSql(method));
            if (args != null) {
                result.append(args);
            }
        }

        if (repeatable != null) {
            result.append(" repeatable(").append(repeatable).append(')');
        }

        return result.toString();
    }
}
