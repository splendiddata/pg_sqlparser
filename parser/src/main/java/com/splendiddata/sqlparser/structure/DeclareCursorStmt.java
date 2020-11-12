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
import javax.xml.bind.annotation.XmlElement;
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
public class DeclareCursorStmt extends Node {
    /*
     * From /postgresql-9.4.1/src/include/nodes/parsenodes.h ---------------------- Declare Cursor Statement
     * 
     * Note: the "query" field of DeclareCursorStmt is only used in the raw grammar output. After parse analysis it's
     * set to null, and the Query points to the DeclareCursorStmt, not vice versa. ----------------------
     */
    /** BINARY */
    private static final int CURSOR_OPT_BINARY = 0x0001;
    /** SCROLL explicitly given */
    private static final int CURSOR_OPT_SCROLL = 0x0002;
    /** NO SCROLL explicitly given */
    private static final int CURSOR_OPT_NO_SCROLL = 0x0004;
    /** INSENSITIVE */
    private static final int CURSOR_OPT_INSENSITIVE = 0x0008;
    /** WITH HOLD */
    private static final int CURSOR_OPT_HOLD = 0x0010;

    /** name of the portal (cursor) */
    @XmlAttribute
    public String portalname;

    /** bitmask of options (see above) */
    @XmlAttribute
    public int options;

    /** the raw SELECT query */
    @XmlElement
    public Node query;

    /**
     * Constructor
     */
    public DeclareCursorStmt() {
        super(NodeTag.T_DeclareCursorStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The DeclareCursorStmt to copy
     */
    public DeclareCursorStmt(DeclareCursorStmt original) {
        super(original);
        this.portalname = original.portalname;
        this.options = original.options;
        if (original.query != null) {
            this.query = original.query.clone();
        }
    }

    @Override
    public DeclareCursorStmt clone() {
        DeclareCursorStmt clone = (DeclareCursorStmt) super.clone();
        if (query != null) {
            clone.query = query.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("declare ").append(ParserUtil.identifierToSql(portalname));

        if ((options & CURSOR_OPT_BINARY) == CURSOR_OPT_BINARY) {
            result.append(" binary");
        }

        if ((options & CURSOR_OPT_INSENSITIVE) == CURSOR_OPT_INSENSITIVE) {
            result.append(" insensitive");
        }

        if ((options & CURSOR_OPT_SCROLL) == CURSOR_OPT_SCROLL) {
            result.append(" scroll");
        }

        if ((options & CURSOR_OPT_NO_SCROLL) == CURSOR_OPT_NO_SCROLL) {
            result.append(" no scroll");
        }

        result.append(" cursor");

        if ((options & CURSOR_OPT_HOLD) == CURSOR_OPT_HOLD) {
            result.append(" with hold");
        }

        result.append(" for ").append(query);

        return result.toString();
    }
}
