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

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CreateEventTrigStmt extends Node {

    /** TRIGGER's name */
    @XmlAttribute
    public String trigname;

    /** event's identifier */
    @XmlAttribute
    public String eventname;

    /** list of DefElems indicating filtering */
    @XmlElementWrapper(name = "whenclause")
    @XmlElement(name = "when")
    public List<DefElem> whenclause;

    /** qual. name of function to call */
    @XmlElementWrapper(name = "funcname")
    @XmlElement(name = "nameNode")
    public List<Value> funcname;

    /**
     * Constructor
     */
    public CreateEventTrigStmt() {
        super(NodeTag.T_CreateEventTrigStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateEventTrigStmt to copy
     */
    public CreateEventTrigStmt(CreateEventTrigStmt original) {
        super(original);
        this.trigname = original.trigname;
        this.eventname = original.eventname;
        if (original.whenclause != null) {
            this.whenclause = original.whenclause.clone();
        }
        if (original.funcname != null) {
            this.funcname = original.funcname.clone();
        }
    }

    @Override
    public CreateEventTrigStmt clone() {
        CreateEventTrigStmt clone = (CreateEventTrigStmt) super.clone();
        if (whenclause != null) {
            clone.whenclause = whenclause.clone();
        }
        if (funcname != null) {
            clone.funcname = funcname.clone();
        }
        return clone;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create event trigger ").append(ParserUtil.identifierToSql(trigname)).append(" on ")
                .append(eventname);

        if (whenclause != null) {
            String separator = " when ";
            for (DefElem when : whenclause) {
                result.append(separator).append(ParserUtil.identifierToSql(when.defname)).append(" in (");
                separator = "";
                for (Node value : (List<Node>) when.arg) {
                    result.append(separator).append(ParserUtil.toSqlTextString(value));
                    separator = ", ";
                }
                result.append(')');
                separator = " and ";
            }
        }

        result.append(" execute procedure ").append(ParserUtil.nameToSql(funcname)).append("()");

        return result.toString();
    }
}
