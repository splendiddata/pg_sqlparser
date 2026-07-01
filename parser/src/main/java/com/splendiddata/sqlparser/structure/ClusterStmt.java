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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Cluster Statement (support pbrown's cluster index implementation)
 * <p>
 * Copied from /postgresql-12beta2/src/include/nodes/parsenodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @deprecated since Postgres 19beta1 - replaced by {@link com.splendiddata.sqlparser.structure.RepackStmt}
 */
@Deprecated(since = "Postgres 19beta1", forRemoval = true)
@XmlRootElement(namespace = "parser")
public class ClusterStmt extends Node {

    /** relation being indexed, or NULL if all */
    @XmlElement
    public RangeVar relation;

    /** original index defined */
    @XmlAttribute
    public String indexname;

    @XmlElementWrapper(name = "params")
    @XmlElement(name = "param")
    public List<DefElem> params;

    /*
     * Copied from ClusterOption in /postgresql-12beta2/src/include/nodes/parsenodes.h
     */

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
        this.params = toCopy.params;
    }

    @Override
    public ClusterStmt clone() {
        ClusterStmt clone = (ClusterStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (params != null) {
            clone.params = params.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("cluster");

        if (params != null) {
            if (params.size() == 1 && "verbose".equals(params.get(0).defname)) {
                result.append(" verbose");
            } else {
                result.append(" (");
                String separator = "";
                for (DefElem param : params) {
                    result.append(separator);
                    separator = ", ";
                    if ("verbose".equals(param.defname)) {
                        result.append("verbose true");
                    } else {
                        result.append(param); // This will not work, but will at least lead an error message here
                    }
                }
                result.append(")");
            }
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
