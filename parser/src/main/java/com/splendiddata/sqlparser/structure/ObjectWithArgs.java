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
import jakarta.xml.bind.annotation.XmlTransient;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * TriggerTransition - representation of transition row or table naming clause
 * <p>
 * Only transition tables are initially supported in the syntax, and only for AFTER triggers, but other permutations are
 * accepted by the parser so we can give a meaningful message from C code.
 * </p>
 * <p>
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class ObjectWithArgs extends Node {
    /** qualified name of function/operator */
    @XmlTransient
    public List<Value> objname;

    /** list of Typename nodes */
    @XmlElementWrapper(name = "objargs")
    @XmlElement(name = "objarg")
    public List<TypeName> objargs;

    /**
     * argument list was omitted, so name must be unique (note that objargs == NIL means zero args)
     */
    @XmlAttribute
    public boolean args_unspecified;

    /**
     * Constructor
     */
    public ObjectWithArgs() {
        super(NodeTag.T_ObjectWithArgs);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The TriggerTransition to copy
     */
    public ObjectWithArgs(ObjectWithArgs original) {
        super(original);
        if (original.objname != null) {
            this.objname = original.objname.clone();
        }
        if (original.objargs != null) {
            this.objargs = original.objargs.clone();
        }
        this.args_unspecified = original.args_unspecified;
    }

    @Override
    public ObjectWithArgs clone() {
        return (ObjectWithArgs) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (objname != null) {
            result.append(ParserUtil.operatorNameToSql(objname));
        }
        if (objargs != null) {
            char separator = '(';
            for (TypeName arg : objargs) {
                result.append(separator); separator=',';
                if (arg == null) {
                    result.append("none");
                } else {
                    result.append(arg);
                }
            }
            result.append(')');
        }
        return result.toString();
    }

    /**
     * Returns the objname as qualified name
     *
     * @return String the (qualified) name or null if not filled
     */
    @XmlAttribute(name = "objname")
    private String getQualifiedName() {
        return ParserUtil.operatorNameToSql(objname);
    }
}
