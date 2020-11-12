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
import com.splendiddata.sqlparser.enums.CTEMaterialize;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-12beta2/src/include/nodes/parsenodes.h, but the unused fields left out.
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CommonTableExpr extends Node {
    /** query name (never qualified) */
    @XmlAttribute
    public String ctename;

    /** optional list of column names */
    @XmlElementWrapper(name = "aliascolnames")
    @XmlElement(name = "aliascolname")
    public List<Value> aliascolnames;

    /**
     * is this an optimization fence?
     * 
     * @since 7.0 - Postgres 12
     */
    @XmlAttribute
    public CTEMaterialize ctematerialized;

    /** the CTE's subquery */
    @XmlElement
    public Node ctequery;

    /**
     * Constructor
     *
     */
    public CommonTableExpr() {
        super(NodeTag.T_CommonTableExpr);
    }

    /**
     * Shallow copy constructor
     *
     * @param other
     *            The CommonTableExpr to copy
     */
    public CommonTableExpr(CommonTableExpr other) {
        super(other);
        this.ctename = other.ctename;
        this.aliascolnames = other.aliascolnames;
        this.ctequery = other.ctequery;
        this.ctematerialized = other.ctematerialized;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(ParserUtil.identifierToSql(ctename));
        String separator = "";
        if (aliascolnames != null) {
            result.append('(');
            for (Value aliasColName : aliascolnames) {
                result.append(separator).append(ParserUtil.identifierToSql(aliasColName.toString()));
                separator = ", ";
            }
            result.append(')');
        }
        result.append(" as ");

        if (ctematerialized != null) {
            switch (ctematerialized) {
            case CTEMaterializeAlways:
                result.append("materialized ");
            case CTEMaterializeDefault:
                break;
            case CTEMaterializeNever:
                result.append("not materialized ");
                break;
            default:
                result.append(" !!! ").append(CTEMaterialize.class.getName()).append(" option ").append(ctematerialized)
                        .append(" is not (yet) implemented in ").append(this.getClass().getName())
                        .append(".toString() !!!");
            }
        }

        result.append('(').append(ctequery).append(')');
        return result.toString();
    }

    @Override
    public CommonTableExpr clone() {
        CommonTableExpr clone = (CommonTableExpr) super.clone();
        if (aliascolnames != null) {
            clone.aliascolnames = aliascolnames.clone();
        }
        if (ctequery != null) {
            clone.ctequery = ctequery.clone();
        }
        return clone;
    }
}
