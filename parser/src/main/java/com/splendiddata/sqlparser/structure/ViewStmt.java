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
import com.splendiddata.sqlparser.enums.ViewCheckOption;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class ViewStmt extends Node {

    /** the view to be created */
    @XmlElement
    public RangeVar view;

    /** target column names */
    @XmlElementWrapper(name = "aliases")
    @XmlElement(name = "alias")
    public List<Value> aliases;

    /** the SELECT query */
    @XmlElement
    public Node query;

    /** replace an existing view? */
    @XmlAttribute
    public boolean replace;

    /** options from WITH clause */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /* WITH CHECK OPTION */
    @XmlAttribute
    public ViewCheckOption withCheckOption;

    /**
     * Constructor
     */
    public ViewStmt() {
        super(NodeTag.T_ViewStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The ViewStmt to copy
     */
    public ViewStmt(ViewStmt original) {
        super(original);
        if (original.view != null) {
            this.view = original.view.clone();
        }
        if (original.aliases != null) {
            this.aliases = original.aliases.clone();
        }
        if (original.query != null) {
            this.query = original.query;
        }
        this.replace = original.replace;
        if (original.options != null) {
            this.options = original.options.clone();
        }
        this.withCheckOption = original.withCheckOption;
    }

    @Override
    public ViewStmt clone() {
        ViewStmt clone = (ViewStmt) super.clone();
        if (view != null) {
            clone.view = view.clone();
        }
        if (aliases != null) {
            clone.aliases = aliases.clone();
        }
        if (query != null) {
            clone.query = query.clone();
        }
        if (options != null) {
            clone.options = options.clone();
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

        switch (view.relpersistence) {
        case RELPERSISTENCE_TEMP:
            result.append(" temporary");
            break;
        case RELPERSISTENCE_UNLOGGED:
            result.append(" unlogged");
            break;
        case RELPERSISTENCE_PERMANENT:
            break;
        default:
            result.append(ParserUtil.reportUnknownValue("view.relpersistence", view.relpersistence, getClass()));
            break;
        }
        result.append(" view ").append(view);

        boolean cteExist = isRecursive();

        if (!cteExist && aliases != null) {
            result.append('(');
            String separator = "";
            for (Value alias : aliases) {
                result.append(separator).append(ParserUtil.identifierToSql(alias.toString()));
                separator = ", ";
            }
            result.append(')');
        }

        if (options != null) {
            result.append(" with (");
            String separator = "";
            for (DefElem option : options) {
                result.append(separator).append(option.defname);
                if (option.arg != null) {
                    result.append(" = ").append(option.arg);
                }
                separator = ", ";
            }
            result.append(')');
        }

        result.append(" as ").append(query);

        if (withCheckOption != null) {
            result.append(' ').append(withCheckOption);
        }

        return result.toString();
    }

    /**
     * Checks if the view query is recursive.
     * 
     * @return true if the view query is recursive.
     */
    private boolean isRecursive() {
        return ((SelectStmt) query).withClause != null && ((SelectStmt) query).withClause.recursive;
    }
}
