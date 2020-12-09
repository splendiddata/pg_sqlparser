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
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CollateClause extends Node {

    /** input expression */
    @XmlElement
    public Node arg;

    /** possibly-qualified collation name */
    @XmlElementWrapper(name = "collname")
    @XmlElement(name = "nameNode")
    public List<Value> collname;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (arg != null) {
            if (com.splendiddata.sqlparser.enums.NodeTag.T_A_Expr.equals(arg.type)) {
                result.append('(').append(arg).append(')');
            } else {
                result.append(arg);
            }
        }
        result.append(" collate");
        char separator = ' ';
        for (Value coll : collname) {
            result.append(separator).append(ParserUtil.identifierToSql(coll.toString()));
            separator = '.';
        }
        return result.toString();
    }

    @Override
    public CollateClause clone() {
        CollateClause clone = (CollateClause) super.clone();
        if (arg != null) {
            clone.arg = arg.clone();
        }
        if (collname != null) {
            clone.collname = collname.clone();
        }
        return clone;
    }
}
