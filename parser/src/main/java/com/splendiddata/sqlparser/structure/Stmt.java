/*
 * Copyright (c) Splendid Data Product Development B.V. 2025
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
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * Base class for statements as in Postgres 18 statements apparently use stmt_location instead of location.
 *
 * @author Splendid Data Product Development B.V.
 * @since 18.0
 */
public class Stmt extends Node {

    /**
     * The offset of this Stmt relative to the start of the sql stream
     */
    @XmlTransient
    public Location stmt_location;

    /**
     * length in bytes; 0 means "rest of string"
     */
    @XmlAttribute
    public long stmt_len;

    /**
     * Constructor
     */
    public Stmt() {
        super();
    }

    /**
     * Copy onstructor
     *
     * @param original
     *            The Stmt to copy
     */
    public Stmt(Stmt original) {
        super(original);
        if (original.stmt_location != null) {
            this.stmt_location = original.stmt_location.clone();
        }
        this.stmt_len = original.stmt_len;
    }

    /**
     * Constructor
     *
     * @param type
     *            The type of the node to construct
     */
    public Stmt(NodeTag type) {
        super(type);
    }

    @Override
    public Stmt clone() {
        Stmt clone = (Stmt) super.clone();
        if (stmt_location != null) {
            clone.stmt_location = stmt_location.clone();
        }
        return clone;
    }

    /**
     * Returns the start offset of the node in the sql statement
     *
     * @return long the start offset of this node or -1 if unknown
     */
    @Override
    @XmlTransient
    public final long getStartOffset() {
        if (stmt_location == null || stmt_location.begin == null) {
            return super.getStartOffset();
        }
        return stmt_location.begin.getOffset();
    }

    /**
     * @return String returns the stmt_location as String to be represented in an XML structure for debugging purposes
     */
    @XmlAttribute(name = "stmt_location")
    private String getLocationString() {
        if (stmt_location == null) {
            if (location == null) { // For backward compatibility since Postgres 18
                return null;
            }
            return location.toString();
        }
        return stmt_location.toString();
    }
}
