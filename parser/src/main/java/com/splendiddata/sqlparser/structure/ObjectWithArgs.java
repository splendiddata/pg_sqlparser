/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2021
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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * ObjectWithArgs represents a function/procedure/operator name plus parameter identification.
 * <p>
 * objargs includes only the types of the input parameters of the object. In some contexts, that will be all we have,
 * and it's enough to look up objects according to the traditional Postgres rules (i.e., when only input arguments
 * matter).
 * </p>
 * <p>
 * objfuncargs, if not NIL, carries the full specification of the parameter list, including parameter mode annotations.
 * </p>
 * <p>
 * Some grammar productions can set args_unspecified = true instead of providing parameter info. In this case, lookup
 * will succeed only if the object name is unique. Note that otherwise, NIL parameter lists mean zero arguments.
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

    /** list of Typename nodes (input args only) */
    @XmlElementWrapper(name = "objargs")
    @XmlElement(name = "objarg")
    public List<TypeName> objargs;

    /**
     * list of FunctionParameter nodes
     * 
     * @since 14.0
     */
    @XmlElementWrapper(name = "objfuncargs")
    @XmlElement(name = "objfuncarg")
    public List<FunctionParameter> objfuncargs;

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
        if (original.objfuncargs != null) {
            this.objfuncargs = original.objfuncargs.clone();
        }
        this.args_unspecified = original.args_unspecified;
    }

    @Override
    public ObjectWithArgs clone() {
        ObjectWithArgs clone =  (ObjectWithArgs) super.clone();
        if(objname != null) {
            clone.objname = objname.clone();
        }
        if (objargs != null) {
            clone.objargs = objargs.clone();
        }
        if (objfuncargs != null) {
            clone.objfuncargs = objfuncargs.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (objname != null) {
            result.append(ParserUtil.operatorNameToSql(objname));
        }
        if (objfuncargs != null) {
            char separator = '(';
            for (FunctionParameter arg : objfuncargs) {
                result.append(separator);
                separator = ',';
                if (arg == null) {
                    result.append("none");
                } else {
                    result.append(arg);
                }
            }
            result.append(')');
        } else if (objargs != null) {
            char separator = '(';
            for (TypeName arg : objargs) {
                result.append(separator);
                separator = ',';
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
