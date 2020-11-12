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
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class RangeSubselect extends Node {

    /** does it have LATERAL prefix? */
    @XmlAttribute
    public boolean lateral;

    /** the untransformed sub-select clause */
    @XmlElement
    public Node subquery;

    /** table alias &amp; optional column aliases */
    @XmlElement
    public Alias alias;

    /**
     * Constructor
     */
    public RangeSubselect() {
        super(NodeTag.T_RangeSubselect);
    }

    /**
     * Shallow copy constructor
     *
     * @param other
     *            The RangeSubselect to copy
     */
    public RangeSubselect(RangeSubselect other) {
        super(other);
        this.lateral = other.lateral;
        this.subquery = other.subquery;
        this.alias = other.alias;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (lateral) {
            result.append("lateral ");
        }
        result.append("(").append(subquery).append(")");
        if (alias != null) {
            result.append(' ').append(alias);
        }
        return result.toString();
    }

    @Override
    public RangeSubselect clone() {
        RangeSubselect clone = (RangeSubselect) super.clone();
        if (subquery != null) {
            clone.subquery = subquery.clone();
        }
        if (alias != null) {
            clone.alias = alias.clone();
        }
        return clone;
    }
}
