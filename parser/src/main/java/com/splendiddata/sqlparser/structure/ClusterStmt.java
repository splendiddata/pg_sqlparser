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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-12beta2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class ClusterStmt extends Node {

    /** relation being indexed, or NULL if all */
    @XmlElement
    public RangeVar relation;

    /** original index defined */
    @XmlAttribute
    public String indexname;

    /**
     * print progress info
     * 
     * @deprecated since 7.0 - Postgres 12. There is an options integer now that takes it's place
     */
    @Deprecated
    public boolean verbose;

    /**
     * OR of ClusterOption flags
     * 
     * @since 7.0 - postgres 12
     */
    @XmlAttribute
    public int options;

    /*
     * Copied from ClusterOption in /postgresql-12beta2/src/include/nodes/parsenodes.h
     */
    /**
     * recheck relation state
     * 
     * @since 7.0 - Postgres 12
     */
    public static final int CLUOPT_RECHECK = 1 << 0;
    /**
     * print progress info
     * 
     * @since 7.0 - Postgres 12
     */
    public static final int CLUOPT_VERBOSE = 1 << 1;

    /**
     * String containing a regular expression that will identify all CLUOPT_... options for parser generation
     * 
     * @since 7.0 - Postgres 12
     */
    public static final String REPLACEMENT_REGEXP_PART = "CLUOPT_.+";

    /**
     * Constructor
     */
    public ClusterStmt() {
        super(NodeTag.T_ClusterStmt);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The original
     */
    public ClusterStmt(ClusterStmt toCopy) {
        super(toCopy);
        if (toCopy.relation != null) {
            this.relation = toCopy.relation.clone();
        }
        this.indexname = toCopy.indexname;
        this.options = toCopy.options;
    }

    @Override
    public ClusterStmt clone() {
        ClusterStmt clone = (ClusterStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("cluster");

        if ((options & CLUOPT_RECHECK) != 0) {
            result.append(" recheck");
        }
        if ((options & CLUOPT_VERBOSE) != 0) {
            result.append(" verbose");
        }

        if (relation != null) {
            result.append(' ').append(relation);
        }

        if (indexname != null) {
            result.append(" using ").append(ParserUtil.identifierToSql(indexname));
        }

        return result.toString();
    }

}
