/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
 *
 * This program is free software: You may redistribute and/or modify under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at Client's option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, Client should obtain one via www.gnu.org/licenses/.
 */

package com.splendiddata.sqlparser.structure;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * <p>
 * RawStmt --- container for any one statement's raw parse tree
 * </p>
 * <p>
 * Parse analysis converts a raw parse tree headed by a RawStmt node into an analyzed statement headed by a Query node.
 * For optimizable statements, the conversion is complex. For utility statements, the parser usually just transfers the
 * raw parse tree (sans RawStmt) into the utilityStmt field of the Query node, and all the useful work happens at
 * execution time.
 * </p>
 * <p>
 * stmt_location/stmt_len identify the portion of the source text string containing this raw statement (useful for
 * multi-statement strings).
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class RawStmt extends Node {

    /** raw parse tree */
    @XmlElement
    public Node stmt;

    /** start location, or null if unknown */
    @XmlElement
    public Location stmt_location;

    /** length in bytes; 0 means "rest of string" */
    @XmlAttribute
    public long stmt_len;

    /**
     * Constructor
     */
    public RawStmt() {
        super(NodeTag.T_RawStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The RawStmt to copy
     */
    public RawStmt(RawStmt original) {
        super(original);
        if (original.stmt != null) {
            this.stmt = original.stmt.clone();
        }
        this.stmt_location = original.stmt_location;
        this.stmt_len = original.stmt_len;
    }

    @Override
    public RawStmt clone() {
        RawStmt clone = (RawStmt) super.clone();
        if (stmt != null) {
            clone.stmt = stmt.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        if (stmt == null) {
            return "RawStmt.stmt is null";
        }
        return stmt.toString();
    }
}
