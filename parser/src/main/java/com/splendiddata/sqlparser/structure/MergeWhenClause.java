/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2025
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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.CmdType;
import com.splendiddata.sqlparser.enums.MergeMatchKind;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.OverridingKind;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * MergeWhenClause -<br>
 * raw parser representation of a WHEN clause in a MERGE statement
 * <p>
 * In Postgres this is transformed into MergeAction by parse analysis
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 15
 */
@XmlRootElement(namespace = "parser")
public class MergeWhenClause extends Node {

    /**
     * true=MATCHED, false=NOT MATCHED
     * 
     * @deprecated since 17.0 - please use matchKind instead
     */
    @Deprecated(forRemoval = true)
    public boolean matched;

    /**
     * MATCHED/NOT MATCHED BY SOURCE/TARGET
     * 
     * @since Postgres 17
     */
    @XmlAttribute
    public MergeMatchKind matchKind;

    /** INSERT/UPDATE/DELETE/DO NOTHING */
    @XmlAttribute
    public CmdType commandType;

    /** OVERRIDING clause */
    @XmlAttribute
    public OverridingKind override;

    /** WHEN conditions (raw parser) */
    @XmlElement
    public Node condition;

    /** INSERT/UPDATE targetlist */
    @XmlElementWrapper(name = "targetList")
    @XmlElement(name = "target")
    public List<ResTarget> targetList;

    /* the following members are only used in INSERT actions */
    /** VALUES to INSERT, or NULL */
    @XmlElementWrapper(name = "values")
    @XmlElement(name = "value")
    public List<Value> values;

    /**
     * Constructor
     */
    public MergeWhenClause() {
        super(NodeTag.T_MergeWhenClause);
    }

    /**
     * Shallow copy constructor
     *
     * @param original
     *            The MergeStmt to copy
     */
    public MergeWhenClause(MergeWhenClause original) {
        super(original);
        this.matched = original.matched;
        this.matchKind = original.matchKind;
        this.commandType = original.commandType;
        this.override = original.override;
        if (original.condition != null) {
            this.condition = original.condition.clone();
        }
        if (original.targetList != null) {
            this.targetList = original.targetList.clone();
        }
        if (original.values != null) {
            this.values = original.values.clone();
        }
    }

    @Override
    public MergeWhenClause clone() {
        MergeWhenClause clone = (MergeWhenClause) super.clone();
        if (condition != null) {
            clone.condition = condition.clone();
        }
        if (targetList != null) {
            clone.targetList = targetList.clone();
        }
        if (values != null) {
            clone.values = values.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(matchKind).append(" then ");
        switch (commandType) {
        case CMD_DELETE:
            result.append("delete");
            break;
        case CMD_INSERT:
            result.append("insert");
            if (targetList != null) {
                result.append(' ').append(targetList);
            }
            switch (override) {
            case OVERRIDING_NOT_SET:
                break;
            case OVERRIDING_SYSTEM_VALUE:
                result.append(" overriding system value");
                break;
            case OVERRIDING_USER_VALUE:
                result.append(" overriding user value");
                break;
            default:
                result.append(ParserUtil.reportUnknownValue("ovverride", override.name(), getClass()));
                break;
            }
            if (values == null) {
                result.append(" default values");
            } else {
                result.append(" values ").append(values);
            }
            break;
        case CMD_UPDATE:
            result.append("update set");
            if (targetList != null && !targetList.isEmpty()) {
                String separator = " ";

                for (ResTarget tgt : targetList) {
                    result.append(separator).append(ParserUtil.identifierToSql(tgt.name)).append(" = ").append(tgt.val);
                    separator = ", ";
                }
            }
            break;
        case CMD_NOTHING:
            result.append("do nothing");
            break;
        case CMD_SELECT:
        case CMD_UNKNOWN:
        case CMD_UTILITY:
        default:
            result.append(ParserUtil.reportUnknownValue("commandType", commandType.name(), getClass()));
            break;
        }

        return result.toString();
    }
}
