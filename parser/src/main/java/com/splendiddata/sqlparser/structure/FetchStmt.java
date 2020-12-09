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
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.FetchDirection;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class FetchStmt extends Node {
    @XmlAttribute
    public FetchDirection direction;

    /** number of rows, or position argument */
    @XmlAttribute
    public long howMany;

    /** name of portal (cursor) */
    @XmlAttribute
    public String portalname;

    /** TRUE if MOVE */
    @XmlAttribute
    public boolean ismove;

    /**
     * Constructor
     */
    public FetchStmt() {
        super(NodeTag.T_FetchStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The FetchStmt to copy
     */
    public FetchStmt(FetchStmt original) {
        super(original);
        this.direction = original.direction;
        this.howMany = original.howMany;
        this.portalname = original.portalname;
        this.ismove = original.ismove;
    }

    @Override
    public FetchStmt clone() {
        return (FetchStmt) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (ismove) {
            result.append("move");
        } else {
            result.append("fetch");
        }

        switch (direction) {
        case FETCH_ABSOLUTE:
            if (howMany == 1) {
                result.append(" first");
            } else if (howMany == Long.MAX_VALUE) {
                result.append(" last");
            } else {
                result.append(" absolute ").append(howMany);
            }
            break;
        case FETCH_BACKWARD:
            if (howMany == 1) {
                result.append(" prior");
            } else if (howMany == Long.MAX_VALUE) {
                result.append(" backward all");
            } else {
                result.append(" backward ").append(howMany);
            }
            break;
        case FETCH_FORWARD:
            if (howMany == 1) {
                result.append(" next");
            } else if (howMany == Long.MAX_VALUE) {
                result.append(" all");
            } else {
                result.append(' ').append(howMany);
            }
            break;
        case FETCH_RELATIVE:
            result.append(" relative ").append(howMany);
            break;
        default:
            result.append(ParserUtil.reportUnknownValue("FetchDirection", direction, getClass()));
            break;
        }

        result.append(" from ").append(ParserUtil.identifierToSql(portalname));

        return result.toString();
    }
}
