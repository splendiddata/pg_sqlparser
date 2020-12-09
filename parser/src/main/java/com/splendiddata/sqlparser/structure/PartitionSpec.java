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

import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.PartitionStrategy;

/**
 * PartitionSpec - parse-time representation of a partition key specification
 * <p>
 * This represents the key space we will be partitioning on.
 * </p>
 * <p>
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class PartitionSpec extends Node {
    /**
     * partitioning strategy ('list' or 'range'). See {@link PartitionStrategy#PARTITION_STRATEGY_LIST} an
     * {@link PartitionStrategy#PARTITION_STRATEGY_RANGE}
     */
    @XmlAttribute
    public String strategy;

    /**
     * List of PartitionElems
     */
    @XmlElementWrapper(name = "partParams")
    @XmlElement(name = "partParam")
    public List<PartitionElem> partParams;

    /**
     * Constructor
     */
    public PartitionSpec() {
        super(NodeTag.T_PartitionSpec);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public PartitionSpec(PartitionSpec original) {
        super(original);
        this.strategy = original.strategy;
        if (original.partParams != null) {
            this.partParams = original.partParams.clone();
        }
    }

    @Override
    public PartitionSpec clone() {
        PartitionSpec clone = (PartitionSpec) super.clone();
        if (partParams != null) {
            clone.partParams = partParams.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("partition by ").append(strategy).append(' ').append(partParams);
        return result.toString();
    }
}
