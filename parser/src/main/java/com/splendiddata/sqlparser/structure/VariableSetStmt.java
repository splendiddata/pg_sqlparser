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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.VariableSetKind;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class VariableSetStmt extends Node {

    @XmlAttribute
    public VariableSetKind kind;

    /** variable to be set */
    @XmlAttribute
    public String name;

    /** List of A_Const nodes */
    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    public List<Node> args;

    /** SET LOCAL? */
    @XmlAttribute
    public boolean is_local;

    /**
     * Constructor
     */
    public VariableSetStmt() {
        super(NodeTag.T_VariableSetStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The VariableSetStmt to copy
     */
    public VariableSetStmt(VariableSetStmt original) {
        super(original);
        this.kind = original.kind;
        this.name = original.name;
        if (original.args != null) {
            this.args = original.args.clone();
        }
        this.is_local = original.is_local;
    }

    @Override
    public VariableSetStmt clone() {
        VariableSetStmt clone = (VariableSetStmt) super.clone();
        if (args != null) {
            clone.args = args.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        switch (kind) {
        case VAR_RESET:
            result.append("reset");
            break;
        case VAR_RESET_ALL:
            return "reset all";
        case VAR_SET_CURRENT:
        case VAR_SET_DEFAULT:
        case VAR_SET_MULTI:
        case VAR_SET_VALUE:
            result.append("set");
            break;
        default:
            result.append(ParserUtil.reportUnknownValue("VariableSetKind", ParserUtil.stmtToXml(kind), getClass()));
            break;
        }

        if (is_local) {
            result.append(" local");
        }

        switch (name) {
        case "role":
            result.append(" role");
            if (VariableSetKind.VAR_SET_DEFAULT.equals(kind)) {
                result.append(" to default");
            } else if (args != null) {
                String roleName = args.get(0).toString();
                if ("'none'".equals(roleName)) {
                    result.append(" none");
                } else {
                    result.append(' ').append(roleName.replace("'", ""));
                }
            }
            break;
        case "session_authorization":
            result.append(" session authorization");
            if (args == null) {
                if (!VariableSetKind.VAR_RESET.equals(kind)) {
                    result.append(" default");
                }
            } else {
                result.append(' ').append(args.get(0));
            }
            break;
        case "SESSION CHARACTERISTICS":
            result.append(" session characteristics as transaction");
            if (args == null) {
                if (!VariableSetKind.VAR_RESET.equals(kind)) {
                    result.append(" default");
                }
            } else {
                printTransactionSpecifics(result);
            }
            break;
        case "TRANSACTION":
            result.append(" transaction");
            printTransactionSpecifics(result);
            break;
        case "TRANSACTION SNAPSHOT":
            result.append(" transaction snapshot ").append(args.get(0));
            break;
        default:
            result.append(' ').append(ParserUtil.identifierToSql(name));

            switch (kind) {
            case VAR_RESET:
            case VAR_RESET_ALL:
                break;
            case VAR_SET_CURRENT:
                result.append(" from current");
                break;
            case VAR_SET_DEFAULT:
                result.append(" to default");
                break;
            case VAR_SET_MULTI:
            case VAR_SET_VALUE:
                if (args != null) {
                    String separator = " to ";
                    for (Node arg : args) {
                        if (arg instanceof DefElem) {
                            // TODO: Properly resolve.
                            result.append(ParserUtil.reportUnknownValue("arg", ParserUtil.stmtToXml(arg), getClass()));
                        } else {
                            result.append(separator).append(arg);
                        }
                        separator = ", ";
                    }
                }
                break;
            default:
                result.append(ParserUtil.reportUnknownValue("VariableSetKind", ParserUtil.stmtToXml(kind), getClass()));
                break;
            }
            break;
        }

        return result.toString();
    }

    /**
     * Adds transaction setting specifics to the result
     *
     * @param result
     */
    private void printTransactionSpecifics(StringBuilder result) {
        String separator = " ";
        for (Node arg : args) {
            DefElem d = (DefElem) arg;
            switch (d.defname) {
            case "transaction_isolation":
                result.append(separator).append("isolation level ").append(((A_Const) d.arg).val);
                break;
            case "transaction_read_only":
                result.append(separator);
                if (((A_Const) d.arg).val.val.ival == 0) {
                    result.append("read write");
                } else {
                    result.append("read only");
                }
                break;
            case "transaction_deferrable":
                result.append(separator);
                if (((A_Const) d.arg).val.val.ival == 0) {
                    result.append("not deferrable");
                } else {
                    result.append("deferrable");
                }
                break;
            default:
                result.append(' ').append(ParserUtil.reportUnknownValue("argument.defname", d.defname, getClass()));
            }
            separator = ", ";
        }
    }
}
