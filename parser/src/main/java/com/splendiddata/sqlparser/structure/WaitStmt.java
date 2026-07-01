/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2026
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

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-Postgres 19beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 19beta1
 */
@XmlRootElement(namespace = "parser")
public class WaitStmt extends Node {

    /** LSN string from grammar */
    @XmlAttribute
    public String lsn_literal;

    /** List of DefElem nodes */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     */
    public WaitStmt() {
        super(NodeTag.T_WaitStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            the WaitStmt to copy
     */
    public WaitStmt(WaitStmt original) {
        super(original);
        this.lsn_literal = original.lsn_literal;
        if (original.options != null) {
            this.options = original.options.clone();
        }
    }

    public WaitStmt clone() {
        WaitStmt clone = (WaitStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

}
