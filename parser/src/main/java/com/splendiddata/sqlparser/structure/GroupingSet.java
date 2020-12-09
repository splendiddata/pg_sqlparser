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

import com.splendiddata.sqlparser.enums.GroupingSetKind;

/**
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class GroupingSet extends Node {
    @XmlAttribute
    public GroupingSetKind kind;

    @XmlElementWrapper(name = "content")
    @XmlElement(name = "contentNode")
    public List<Node> content;

    @Override
    public GroupingSet clone() {
        GroupingSet clone = (GroupingSet) super.clone();
        if (content != null) {
            clone.content = content.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        switch (kind) {
        case GROUPING_SET_CUBE:
            return "cube " + content;
        case GROUPING_SET_EMPTY:
            return "()";
        case GROUPING_SET_ROLLUP:
            return "rollup " + content;
        case GROUPING_SET_SETS:
            return "grouping sets " + content;
        default:
            return "??? implement GroupingSetKind " + kind + " in " + getClass().getName() + " ???";

        }
    }
}
