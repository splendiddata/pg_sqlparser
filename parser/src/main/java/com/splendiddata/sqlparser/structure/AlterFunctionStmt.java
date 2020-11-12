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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ObjectType;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class AlterFunctionStmt extends Node {

    /**
     * @since 6.0 - Postgres version 11
     */
    @XmlAttribute
    public ObjectType objtype;
    /**
     * name and args of function
     * <p>
     * This is an {@link ObjectWithArgs} in this version of the parser. But because in versions before 5.0 (Postgres
     * version 10) this was be a {@link FuncWithArgs}, this attribute is now registered as {@link Node}.
     * </p>
     * TODO: When version before 5.0 are no longer supported, please alter the type from Node to ObjectWithArgs
     */
    @XmlElement
    public Node func;

    /** list of DefElem */
    @XmlElementWrapper(name = "actions")
    @XmlElement(name = "action")
    public List<DefElem> actions;

    /**
     * Constructor
     */
    public AlterFunctionStmt() {
        super(NodeTag.T_AlterFunctionStmt);
    }

    /**
     * Copy constructor
     * 
     * @param toCopy
     *            the AlterFunctionStmt to copy
     */
    public AlterFunctionStmt(AlterFunctionStmt toCopy) {
        super(toCopy);
        this.objtype = toCopy.objtype;
        if (toCopy.func != null) {
            this.func = toCopy.func.clone();
        }
        if (toCopy.actions != null) {
            this.actions = toCopy.actions.clone();
        }
    }

    @Override
    public AlterFunctionStmt clone() {
        AlterFunctionStmt clone = (AlterFunctionStmt) super.clone();
        if (func != null) {
            clone.func = func.clone();
        }
        if (actions != null) {
            clone.actions = actions.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter ").append(objtype).append(' ').append(func);

        /** list of DefElem */
        if (actions != null) {
            for (DefElem action : actions) {
                addActionToString(action, result);
            }
        }

        return result.toString();
    }

    /**
     * Adds the action to the result sql string
     *
     * @param action
     * @param result
     */
    @SuppressWarnings("unchecked")
    private void addActionToString(DefElem action, StringBuilder result) {
        switch (action.defname) {
        case "volatility":
            result.append(' ').append(((Value) action.arg).val.str);
            break;
        case "strict":
            if (((Value) action.arg).val.ival == 0) {
                result.append(" called on null input");
            } else {
                result.append(" strict");
            }
            break;
        case "leakproof":
            if (((Value) action.arg).val.ival == 0) {
                result.append(" not");
            }
            result.append(" leakproof");
            break;
        case "security":
            if (((Value) action.arg).val.ival == 0) {
                result.append(" security invoker");
            } else {
                result.append(" security definer");
            }
            break;
        case "cost":
            result.append(" cost ").append(action.arg);
            break;
        case "rows":
            result.append(" rows ").append(action.arg);
            break;
        case "set":
            result.append(' ').append(action.arg);
            break;
        case "support":
            result.append(" support ").append(((List<Value>)action.arg).get(0));
            break;
        default:
            result.append(ParserUtil.reportUnknownValue("action", ParserUtil.stmtToXml(action), getClass()));
        }
    }
}
