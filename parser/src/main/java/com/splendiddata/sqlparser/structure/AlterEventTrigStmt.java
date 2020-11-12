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
public class AlterEventTrigStmt extends Node {
    /** TRIGGER's name */
    @XmlAttribute
    public String trigname;

    /**
     * trigger's firing configuration WRT session_replication_role
     * <p>
     * from /postgresql-9.4.1/src/include/commands/trigger.h:<br>
     * States at which a trigger can be fired. These are the possible values for pg_trigger.tgenabled.
     * <ul>
     * <li>'O': TRIGGER_FIRES_ON_ORIGIN</li>
     * <li>'A': TRIGGER_FIRES_ALWAYS</li>
     * <li>'R': TRIGGER_FIRES_ON_REPLICA</li>
     * <li>'D': TRIGGER_DISABLED</li>
     * </ul>
     */
    @XmlAttribute
    public char tgenabled;
    
    /**
     * Constructor
     */
    public AlterEventTrigStmt() {
        super(NodeTag.T_AlterEventTrigStmt);
    }

    /**
     * Copy constructor
     *
     * @param original The AlterEventTrigStmt to copy
     */
    public AlterEventTrigStmt(AlterEventTrigStmt original) {
        super(original);
        this.trigname = original.trigname;
        this.tgenabled = original.tgenabled;
    }

    @Override
    public AlterEventTrigStmt clone() {
        return (AlterEventTrigStmt) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter event trigger ").append(ParserUtil.identifierToSql(trigname));
        switch (tgenabled) {
        case 'D':
            result.append(" disable");
            break;
        case 'A':
            result.append(" enable always");
            break;
        case 'R':
            result.append(" enable replica");
            break;
        case 'O':
            result.append(" enable");
            break;
        default:
            result.append("??????? unknown tgenabled: ").append(tgenabled).append(" in ").append(getClass().getName())
                    .append(" ???????");
            break;
        }

        return result.toString();
    }
}
