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
 * @deprecated since 5.0: Please use {@link ObjectWithArgs} instead
 */
@Deprecated
@XmlRootElement(namespace = "parser")
public class FuncWithArgs extends Node {

    /** qualified name of function */
    @XmlElementWrapper(name = "funcname")
    @XmlElement(name = "nameNode")
    public List<Value> funcname;

    /** list of Typename nodes */
    @XmlElementWrapper(name = "funcargs")
    @XmlElement(name = "funcarg")
    public List<Node> funcargs;

    @Override
    public FuncWithArgs clone() {
        FuncWithArgs clone = (FuncWithArgs) super.clone();
        if (funcname != null) {
            clone.funcname = funcname.clone();
        }
        if (funcargs != null) {
            clone.funcargs = funcargs.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        if (funcargs == null) {
            return ParserUtil.nameToSql(funcname) + "()";
        }
        return ParserUtil.nameToSql(funcname) + funcargs;
    }
}
