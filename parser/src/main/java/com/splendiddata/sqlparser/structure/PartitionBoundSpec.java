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
import com.splendiddata.sqlparser.enums.PartitionStrategy;

/**
 * PartitionBoundSpec - a partition bound specification
 * <p>
 * This represents the portion of the partition key space assigned to a particular partition. These are stored on disk
 * in pg_class.relpartbound.
 * </p>
 * <p>
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class PartitionBoundSpec extends Node {

    @XmlAttribute
    public PartitionStrategy strategy;
    
    /**
     * is it a default partition bound?
     * 
     * @since 6.0 - Postgres version 11
     */
    @XmlAttribute
    public boolean is_default;

    /* Partitioning info for HASH strategy: */
    /** @since 6.0 - Postgres version 11 */
    @XmlAttribute
    public int modulus;
    /** @since 6.0 - Postgres version 11 */
    @XmlAttribute
    public int remainder;

    /**
     * Partitioning info for LIST strategy: List of Consts (or A_Consts in raw tree)
     */
    @XmlElementWrapper(name = "listdatums")
    @XmlElement(name = "listdatum")
    public List<A_Const> listdatums;

    /**
     * Partitioning info for RANGE strategy: List of PartitionRangeDatums
     */
    @XmlElementWrapper(name = "lowerdatums")
    @XmlElement(name = "lowerdatum")
    public List<PartitionRangeDatum> lowerdatums;

    @XmlElementWrapper(name = "upperdatums")
    @XmlElement(name = "upperdatum")
    public List<PartitionRangeDatum> upperdatums;

    /**
     * Constructor
     */
    public PartitionBoundSpec() {
        super(NodeTag.T_PartitionBoundSpec);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public PartitionBoundSpec(PartitionBoundSpec original) {
        super(original);
        this.strategy = original.strategy;
        this.is_default = original.is_default;
        this.modulus = original.modulus;
        this.remainder = original.remainder;
        if (original.listdatums != null) {
            this.listdatums = original.listdatums.clone();
        }
        if (original.lowerdatums != null) {
            this.lowerdatums = original.lowerdatums.clone();
        }
        if (original.upperdatums != null) {
            this.upperdatums = original.upperdatums.clone();
        }
    }

    @Override
    public PartitionBoundSpec clone() {
        PartitionBoundSpec clone = (PartitionBoundSpec) super.clone();
        if (listdatums != null) {
            clone.listdatums = listdatums.clone();
        }
        if (lowerdatums != null) {
            clone.lowerdatums = lowerdatums.clone();
        }
        if (upperdatums != null) {
            clone.upperdatums = upperdatums.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        if (is_default) {
            return " default";
        }
        if (strategy != null) {
            switch (strategy) {
            case PARTITION_STRATEGY_HASH:
                return new StringBuilder().append(" for values with (modulus ").append(modulus).append(", remainder ")
                        .append(remainder).append(")").toString();
            case PARTITION_STRATEGY_LIST:
                return " for values in " + listdatums.toString();
            case PARTITION_STRATEGY_RANGE:
                return new StringBuilder().append(" for values from ").append(lowerdatums).append(" to ").append(upperdatums)
                        .toString();
            default:
                return ("Unknown " + PartitionStrategy.class.getName() + "." + strategy + " found in "
                        + ParserUtil.stmtToXml(this));
            }
        }
        return ("PartitionBoundSpec: no strategy present in "
                + ParserUtil.stmtToXml(this));
    }
}
