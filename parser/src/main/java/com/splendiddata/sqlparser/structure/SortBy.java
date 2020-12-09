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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.SortByDir;
import com.splendiddata.sqlparser.enums.SortByNulls;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class SortBy extends Node {

    /** expression to sort on */
    @XmlElement
    public Node node;

    /** ASC/DESC/USING/default */
    @XmlAttribute
    public SortByDir sortby_dir;

    /** NULLS FIRST/LAST */
    @XmlAttribute
    public SortByNulls sortby_nulls;

    /** name of op to use, if SORTBY_USING */
    @XmlElementWrapper(name = "useOp")
    @XmlElement(name = "operator")
    public List<Value> useOp;

    /**
     * Constructor
     */
    public SortBy() {
        super(NodeTag.T_SortBy);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The SortBy to copy
     */
    public SortBy(SortBy original) {
        super(original);
        this.node = original.node.clone();
        this.sortby_dir = original.sortby_dir;
        this.sortby_nulls = original.sortby_nulls;
        if (original.useOp != null) {
            this.useOp = original.useOp.clone();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(node);
        switch (sortby_dir) {
        case SORTBY_ASC:
            result.append(" asc");
            break;
        case SORTBY_DEFAULT:
            break;
        case SORTBY_DESC:
            result.append(" desc");
            break;
        case SORTBY_USING:
            result.append(" using ");
            if (useOp != null) {
                if (useOp.size() == 1) {
                    result.append(useOp.get(0).val.str);
                } else {
                    result.append("operator(").append(ParserUtil.identifierToSql(useOp.get(0).toString())).append('.')
                            .append(useOp.get(1)).append(')');
                }
            }
            break;
        default:
            break;
        }

        switch (sortby_nulls) {
        case SORTBY_NULLS_DEFAULT:
            break;
        case SORTBY_NULLS_FIRST:
            result.append(" nulls first");
            break;
        case SORTBY_NULLS_LAST:
            result.append(" nulls last");
            break;
        default:
            break;
        }

        return result.toString();
    }

    @Override
    public SortBy clone() {
        SortBy clone = (SortBy) super.clone();
        if (node != null) {
            clone.node = node.clone();
        }
        if (useOp != null) {
            clone.useOp = useOp.clone();
        }
        return clone;
    }
}
