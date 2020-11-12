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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Alter Statistics Statement
 * <p>
 * Copied from postgresql-13beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 8.0
 */
@XmlRootElement(namespace = "parser")
public class AlterStatsStmt extends Node {

    /** qualified name (list of Value strings) */
    @XmlElementWrapper(name = "defnames")
    @XmlElement(name = "defname")
    public List<Value> defnames;

    /** statistics target */
    @XmlAttribute
    public int stxstattarget;

    /** skip error if statistics object is missing */
    @XmlAttribute
    public boolean missing_ok;

    /**
     * Constructor
     */
    public AlterStatsStmt() {
        super(NodeTag.T_AlterStatsStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterStatsStmt to copy
     */
    public AlterStatsStmt(AlterStatsStmt original) {
        super(original);
        if (original.defnames != null) {
            this.defnames = original.defnames.clone();
        }
        this.stxstattarget = original.stxstattarget;
        this.missing_ok = original.missing_ok;
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     *
     * @return AlterStatsStmt A deep copied clone
     */
    @Override
    public AlterStatsStmt clone() {
        AlterStatsStmt clone = (AlterStatsStmt) super.clone();
        if (this.defnames != null) {
            clone.defnames = this.defnames.clone();
        }
        return clone;
    }

    /**
     * @see java.lang.Object#toString()
     *
     * @return String the original statement
     */
    @Override
    public String toString() {
        return new StringBuilder("alter statistics ").append(ParserUtil.nameToSql(defnames)).append(" set statistics ")
                .append(stxstattarget).toString();
    }

}
