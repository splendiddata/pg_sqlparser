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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class ExplainStmt extends Node {

    /** the query (see comments above) */
    @XmlElement
    public Node query;

    /** list of DefElem nodes */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     */
    public ExplainStmt() {
        super(NodeTag.T_ExplainStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The ExplainStmt to copy
     */
    public ExplainStmt(ExplainStmt original) {
        super(original);
        if (original.query != null) {
            this.query = original.query.clone();
        }
        if (original.options != null) {
            this.options = original.options.clone();
        }
    }

    @Override
    public ExplainStmt clone() {
        ExplainStmt clone = (ExplainStmt) super.clone();
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

        result.append("explain");

        if (options != null) {
            result.append(" (");
            String separator = "";
            for (DefElem option : options) {
                result.append(separator).append(option.defname);
                separator = ", ";
                if ("format".equals(option.defname)) {
                    result.append(' ').append(option.arg);
                } else if (option.arg != null) {
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(" false");
                    } else {
                        result.append(" true");
                    }
                }

            }
            result.append(')');
        }

        result.append(' ').append(query);

        return result.toString();
    }
}
