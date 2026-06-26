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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-19beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 19beta1
 */
@XmlRootElement(namespace = "parser")
public class ForPortionOfClause extends Node {

    /** column name of the range/multirange */
    @XmlAttribute
    public String range_name;

    /** token location, or -1 if unknown */
    @XmlElement
    public Location target_location;

    /** Expr from FOR PORTION OF col (...) syntax */
    @XmlElement
    public Node target;

    /** Expr from FROM ... TO ... syntax */
    @XmlElement
    public Node target_start;

    /** Expr from FROM ... TO ... syntax */
    @XmlElement
    public Node target_end;

    /**
     * Constructor
     */
    public ForPortionOfClause() {
        super(NodeTag.T_ForPortionOfClause);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The ForPortionOfClause to copy
     */
    public ForPortionOfClause(ForPortionOfClause original) {
        super(original);
        this.range_name = original.range_name;
        if (original.target_location != null) {
            this.target_location = original.target_location.clone();
        }
        if (original.target != null) {
            this.target = original.target.clone();
        }
        if (original.target_start != null) {
            this.target_start = original.target_start.clone();
        }
        if (original.target_end != null) {
            this.target_end = original.target_end.clone();
        }
    }

    @Override
    public ForPortionOfClause clone() {
        ForPortionOfClause clone = (ForPortionOfClause) super.clone();
        if (target_location != null) {
            clone.target_location = target_location.clone();
        }
        if (target != null) {
            clone.target = target.clone();
        }
        if (target_start != null) {
            clone.target_start = target_start.clone();
        }
        if (target_end != null) {
            clone.target_end = target_end.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("for portion of");
        if (range_name != null) {
            result.append(" ").append(ParserUtil.identifierToSql(range_name));
        }
        if (target != null) {
            result.append(" (").append(target).append(")");
        }
        if (target_start != null) {
            result.append(" from ").append(target_start);
        }
        if (target_end != null) {
            result.append(" to ").append(target_end);
        }
        return result.toString();
    }
}
