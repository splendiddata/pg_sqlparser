/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CreateFunctionStmt extends Node {
    /**
     * it's really CREATE PROCEDURE
     * 
     * @since 6.0 - Postgres version 11
     */
    @XmlAttribute
    public boolean is_procedure;

    /** T =&gt; replace if already exists */
    @XmlAttribute
    public boolean replace;

    /** qualified name of function to create */
    @XmlElementWrapper(name = "funcname")
    @XmlElement(name = "nameNode")
    public List<Value> funcname;

    /** a list of FunctionParameter */
    @XmlElementWrapper(name = "parameters")
    @XmlElement(name = "parameter")
    public List<FunctionParameter> parameters;

    /** the return type */
    @XmlElement
    public TypeName returnType;

    /** a list of DefElem */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * a list of DefElem
     * 
     * @deprecated The with clause in a function definition is no longer supported since 6.0 - Postgres 11
     */
    @Deprecated
    public List<DefElem> withClause;

    /**
     * Constructor
     */
    public CreateFunctionStmt() {
        super(NodeTag.T_CreateFunctionStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateFunctionStmt to copy
     */
    public CreateFunctionStmt(CreateFunctionStmt original) {
        super(original);
        this.is_procedure = original.is_procedure;
        this.replace = original.replace;
        if (original.funcname != null) {
            this.funcname = original.funcname.clone();
        }
        if (original.parameters != null) {
            this.parameters = original.parameters.clone();
        }
        if (original.returnType != null) {
            this.returnType = original.returnType.clone();
        }
        if (original.options != null) {
            this.options = original.options.clone();
        }
        if (original.withClause != null) {
            this.withClause = original.withClause.clone();
        }
    }

    @Override
    public CreateFunctionStmt clone() {
        CreateFunctionStmt clone = (CreateFunctionStmt) super.clone();
        if (funcname != null) {
            clone.funcname = funcname.clone();
        }
        if (parameters != null) {
            clone.parameters = parameters.clone();
        }
        if (returnType != null) {
            clone.returnType = returnType.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create ");

        if (replace) {
            result.append("or replace ");
        }

        if (is_procedure) {
            result.append("procedure ");
        } else {
            result.append("function ");
        }
        result.append(ParserUtil.nameToSql(funcname));

        List<FunctionParameter> resultingTableColumns = null;
        if (parameters == null) {
            result.append("()");
        } else {
            result.append('(');
            String separator = "";
            for (FunctionParameter parameter : parameters) {
                if (parameter.mode == 't') {
                    if (resultingTableColumns == null) {
                        resultingTableColumns = new List<>();
                    }
                    resultingTableColumns.add(parameter);
                } else {
                    result.append(separator).append(parameter);
                    separator = ", ";
                }
            }
            result.append(')');
        }

        if (returnType != null) {
            if (resultingTableColumns == null) {
                result.append(" returns ").append(returnType);
            } else {
                result.append(" returns table ").append(resultingTableColumns);
            }
        }

        if (options != null) {
            for (DefElem option : options) {
                switch (option.defname) {
                case "volatility":
                    result.append(' ').append(((Value) option.arg).val.str);
                    break;
                case "strict":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(" called on null input");
                    } else {
                        result.append(" strict");
                    }
                    break;
                case "leakproof":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(" not");
                    }
                    result.append(" leakproof");
                    break;
                case "security":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(" security invoker");
                    } else {
                        result.append(" security definer");
                    }
                    break;
                case "cost":
                    result.append(" cost ").append(option.arg);
                    break;
                case "rows":
                    result.append(" rows ").append(option.arg);
                    break;
                case "set":
                    result.append(' ').append(option.arg);
                    break;
                case "language":
                    result.append(" language ").append(ParserUtil.identifierToSql(option.arg.toString()));
                    break;
                case "window":
                    result.append(" window");
                    break;
                case "as":
                    String separator = " as ";
                    for (Node arg : (List<Node>) option.arg) {
                        String argString = arg.toString().replace("$$", "$");
                        String quote = "$$";
                        for (int i = 0; argString.contains(quote); i++) {
                            quote = "$_" + i + "_$";
                        }
                        result.append(separator).append(quote).append(argString).append(quote);
                        separator = ", ";
                    }
                    break;
                case "parallel":
                    result.append(" parallel ").append(((Value) option.arg).val.str);
                    break;
                case "support":
                    result.append(" support ").append(((List<Value>) option.arg).get(0));
                    break;
                default:
                    result.append(ParserUtil.reportUnknownValue("option", ParserUtil.stmtToXml(option), getClass()));
                    break;
                }
            }
        }

        return result.toString();
    }
}
