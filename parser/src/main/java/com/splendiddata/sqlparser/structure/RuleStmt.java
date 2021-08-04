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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.CmdType;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class RuleStmt extends Node {

    /** relation the rule is for */
    @XmlElement
    public RangeVar relation;

    /** name of the rule */
    @XmlAttribute
    public String rulename;

    /** qualifications */
    @XmlElement
    public Node whereClause;

    /**
     * SELECT, INSERT, etc
     */
    @XmlAttribute
    public CmdType event;

    /** is a 'do instead'? */
    @XmlAttribute
    public boolean instead;

    /** the action statements */
    @XmlElementWrapper(name = "actions")
    @XmlElement(name = "action")
    public List<Node> actions;

    /** OR REPLACE */
    @XmlAttribute
    public boolean replace;

    /**
     * Constructor
     */
    public RuleStmt() {
        super(NodeTag.T_RuleStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            RuleStmt to copy
     */
    public RuleStmt(RuleStmt original) {
        super(original);
        if (original.relation != null) {
            this.relation = original.relation.clone();
        }
        this.rulename = original.rulename;
        if (original.whereClause != null) {
            this.whereClause = original.whereClause.clone();
        }
        this.event = original.event;
        this.instead = original.instead;
        if (original.actions != null) {
            this.actions = original.actions.clone();
        }
        this.replace = original.replace;
    }

    @Override
    public RuleStmt clone() {
        RuleStmt clone = (RuleStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (whereClause != null) {
            clone.whereClause = whereClause.clone();
        }
        if (actions != null) {
            clone.actions = actions.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create ");

        if (replace) {
            result.append("or replace ");
        }

        result.append("rule ").append(ParserUtil.identifierToSql(rulename)).append(" as on ").append(event)
                .append(" to ").append(relation);

        if (whereClause != null) {
            result.append(" where ").append(whereClause);
        }

        result.append(" do ");

        if (instead) {
            result.append("instead ");
        }

        if (actions == null) {
            result.append("nothing");
        } else if (actions.size() == 1) {
            result.append(actions.get(0));
        } else {
            result.append('(');
            String separator = "";
            for (Node action : actions) {
                result.append(separator).append(action);
                separator = "; ";
            }
            result.append(')');
        }

        return result.toString();
    }
}
