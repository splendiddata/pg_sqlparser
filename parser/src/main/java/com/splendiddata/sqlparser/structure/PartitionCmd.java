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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * PartitionCmd - info for ALTER TABLE ATTACH/DETACH PARTITION commands
 * <p>
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class PartitionCmd extends Node {
    /** name of partition to attach/detach */
    @XmlElement
    public RangeVar name;

    /** FOR VALUES, if attaching */
    @XmlElement
    public PartitionBoundSpec bound;

    /**
     * Constructor
     */
    public PartitionCmd() {
        super(NodeTag.T_PartitionCmd);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public PartitionCmd(PartitionCmd original) {
        super(original);
        if (original.name != null) {
            this.name = original.name.clone();
        }
        if (original.bound != null) {
            this.bound = original.bound.clone();
        }
    }

    @Override
    public PartitionCmd clone() {
        PartitionCmd clone = (PartitionCmd) super.clone();
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
        if (bound == null) {
            return name.toString();
        }
        return new StringBuilder().append(name).append(bound).toString();
    }
}
