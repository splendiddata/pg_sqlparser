/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2024
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

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * PartitionDesc - info about single partition for ALTER TABLE SPLIT PARTITION command
 * <p>
 * Copied from postgresql-17beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 17
 */
@XmlRootElement(namespace = "parser")
public class SinglePartitionSpec extends Node {
    /** name of partition */
    @XmlElement
    public RangeVar name;

    /** FOR VALUES, if attaching */
    @XmlElement
    public PartitionBoundSpec bound;

    /**
     * Constructor
     */
    public SinglePartitionSpec() {
        super(NodeTag.T_SinglePartitionSpec);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public SinglePartitionSpec(SinglePartitionSpec original) {
        super(original);
        if (original.name != null) {
            this.name = original.name.clone();
        }
        if (original.bound != null) {
            this.bound = original.bound.clone();
        }
    }

    @Override
    public SinglePartitionSpec clone() {
        SinglePartitionSpec clone = (SinglePartitionSpec) super.clone();
        if (name != null) {
            clone.name = name.clone();
        }
        if (bound != null) {
            clone.bound = bound.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("partition ").append(name);
        if (bound == null) {
            result.append(" default");
        } else {
            result.append(' ').append(bound);
        }
        return result.toString();
    }
}
