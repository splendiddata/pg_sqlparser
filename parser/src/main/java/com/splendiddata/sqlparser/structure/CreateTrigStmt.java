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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CreateTrigStmt extends Node {

    /**
     * Copied from /postgresql-9.4.1/src/include/catalog/pg_trigger.h
     */
    public static final int TRIGGER_TYPE_ROW = 1 << 0;

    /**
     * Copied from /postgresql-9.4.1/src/include/catalog/pg_trigger.h
     */
    public static final int TRIGGER_TYPE_BEFORE = 1 << 1;

    /**
     * Copied from /postgresql-9.4.1/src/include/catalog/pg_trigger.h
     */
    public static final int TRIGGER_TYPE_INSERT = 1 << 2;

    /**
     * Copied from /postgresql-9.4.1/src/include/catalog/pg_trigger.h
     */
    public static final int TRIGGER_TYPE_DELETE = 1 << 3;

    /**
     * Copied from /postgresql-9.4.1/src/include/catalog/pg_trigger.h
     */
    public static final int TRIGGER_TYPE_UPDATE = 1 << 4;

    /**
     * Copied from /postgresql-9.4.1/src/include/catalog/pg_trigger.h
     */
    public static final int TRIGGER_TYPE_TRUNCATE = 1 << 5;

    /**
     * Copied from /postgresql-9.4.1/src/include/catalog/pg_trigger.h
     */
    public static final int TRIGGER_TYPE_INSTEAD = 1 << 6;

    /**
     * replace trigger if already exists
     * 
     * @since 14.0
     */
    @XmlAttribute
    public boolean replace;

    /** TRIGGER's name */
    @XmlAttribute
    public String trigname;

    /** relation trigger is on */
    @XmlElement
    public RangeVar relation;

    /** qual. name of function to call */
    @XmlElementWrapper(name = "funcname")
    @XmlElement(name = "nameNode")
    public List<Value> funcname;

    /** list of (T_String) Values or NIL */
    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    public List<Value> args;

    /** ROW/STATEMENT */
    @XmlAttribute
    public boolean row;

    /** BEFORE, AFTER, or INSTEAD */
    @XmlAttribute
    public int timing;

    /** "OR" of INSERT/UPDATE/DELETE/TRUNCATE */
    @XmlAttribute
    public int events;

    /** column names, or NIL for all columns */
    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    public List<Value> columns;

    /** qual expression, or NULL if none */
    @XmlElement
    public Node whenClause;

    /** This is a constraint trigger */
    @XmlAttribute
    public boolean isconstraint;

    /**
     * explicitly named transition data
     * <p>
     * TriggerTransition nodes, or NIL if none
     * </p>
     * 
     * @since 5.0
     */
    @XmlElementWrapper(name = "transitionRels")
    @XmlElement(name = "transitionRel")
    public List<TriggerTransition> transitionRels;

    /** [NOT] DEFERRABLE */
    @XmlAttribute
    public boolean deferrable;

    /** INITIALLY {DEFERRED|IMMEDIATE} */
    @XmlAttribute
    public boolean initdeferred;

    /** opposite relation, if RI trigger */
    @XmlElement
    public RangeVar constrrel;

    /**
     * Constructor
     */
    public CreateTrigStmt() {
        super(NodeTag.T_CreateTrigStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            the CreateTrigStmt to copy
     */
    public CreateTrigStmt(CreateTrigStmt original) {
        super(original);
        this.replace = original.replace;
        this.trigname = original.trigname;
        if (original.relation != null) {
            this.relation = original.relation.clone();
        }
        if (original.funcname != null) {
            this.funcname = original.funcname.clone();
        }
        if (original.args != null) {
            this.args = original.args.clone();
        }
        this.row = original.row;
        this.timing = original.timing;
        this.events = original.events;
        if (original.columns != null) {
            this.columns = original.columns.clone();
        }
        if (original.whenClause != null) {
            this.whenClause = original.whenClause.clone();
        }
        this.isconstraint = original.isconstraint;
        if (original.transitionRels != null) {
            this.transitionRels = original.transitionRels.clone();
        }
        this.deferrable = original.deferrable;
        if (original.constrrel != null) {
            this.constrrel = original.constrrel.clone();
        }
        this.initdeferred = original.initdeferred;
    }

    @Override
    public CreateTrigStmt clone() {
        CreateTrigStmt clone = (CreateTrigStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (funcname != null) {
            clone.funcname = funcname.clone();
        }
        if (args != null) {
            clone.args = args.clone();
        }
        if (columns != null) {
            clone.columns = columns.clone();
        }
        if (whenClause != null) {
            clone.whenClause = whenClause.clone();
        }
        if (constrrel != null) {
            clone.constrrel = constrrel.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create");

        if (replace) {
            result.append(" or replace");
        }

        if (isconstraint) {
            result.append(" constraint");
        }

        result.append(" trigger ").append(ParserUtil.identifierToSql(trigname));

        switch (timing) {
        case TRIGGER_TYPE_BEFORE:
            result.append(" before");
            break;
        case TRIGGER_TYPE_INSTEAD:
            result.append(" instead of");
            break;
        default:
            result.append(" after");
            break;
        }

        String separator = " ";
        if ((events & TRIGGER_TYPE_INSERT) == TRIGGER_TYPE_INSERT) {
            result.append(" insert");
            separator = " or ";
        }
        if ((events & TRIGGER_TYPE_UPDATE) == TRIGGER_TYPE_UPDATE) {
            result.append(separator).append("update");
            if (columns != null) {
                separator = " of ";
                for (Value column : columns) {
                    result.append(separator).append(ParserUtil.identifierToSql(column.toString()));
                    separator = ", ";
                }
            }
            separator = " or ";
        }
        if ((events & TRIGGER_TYPE_DELETE) == TRIGGER_TYPE_DELETE) {
            result.append(separator).append("delete");
            separator = " or ";
        }
        if ((events & TRIGGER_TYPE_TRUNCATE) == TRIGGER_TYPE_TRUNCATE) {
            result.append(separator).append("truncate");
            separator = " or ";
        }

        result.append(" on ").append(relation);

        if (constrrel != null) {
            result.append(" from ").append(constrrel);
        }

        if (deferrable) {
            result.append(" deferrable");
        }

        if (initdeferred) {
            result.append(" initially deferred");
        }

        if (transitionRels != null) {
            String sep = " referencing ";
            for (TriggerTransition transition : transitionRels) {
                result.append(sep).append(transition);
                sep = " ";
            }
        }

        if (row) {
            result.append(" for each row");
        } else {
            result.append(" for each statement");
        }

        if (whenClause != null) {
            result.append(" when (").append(whenClause).append(')');
        }

        result.append(" execute procedure ").append(ParserUtil.nameToSql(funcname));

        if (args == null) {
            result.append("()");
        } else {
            result.append('(');
            separator = "";
            for (Value arg : args) {
                result.append(separator).append(ParserUtil.toSqlTextString(arg));
                separator = ", ";
            }
            result.append(')');
        }

        return result.toString();
    }
}
