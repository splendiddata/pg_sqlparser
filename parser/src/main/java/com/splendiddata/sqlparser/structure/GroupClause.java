/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
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

/**
 * Private struct for the result of group_clause production
 * <p>
 * Copied from /postgresql-14beta2/src/backend/parser/gram.c
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 14.0
 */
@XmlRootElement(namespace = "parser")
public class GroupClause {

    @XmlAttribute
    public boolean distinct;

    @XmlElementWrapper(name = "list")
    @XmlElement(name = "elem")
    public List<Node> list;

    /**
     * Constructor
     */
    public GroupClause() {
        super();
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The GroupingFunc to copy
     */
    public GroupClause(GroupClause orig) {
        this.distinct = orig.distinct;
        if (orig.list != null) {
            this.list = orig.list.clone();
        }
    }

    @Override
    public GroupClause clone() {
        try {
            GroupClause clone = (GroupClause) super.clone();
            if (list != null) {
                clone.list = list.clone();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("com.splendiddata.sqlparser.structure.Node.clone()->failed", e);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("grouping sets");

        if (list != null) {
            String separator = "  ";
            for (Node elem : list) {
                result.append(separator).append(elem);
                separator = ", ";
            }
        }

        return result.toString();
    }
}
