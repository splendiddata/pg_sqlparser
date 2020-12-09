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
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.PartitionRangeDatumKind;

/**
 * 
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
public class PartitionRangeDatum extends Node {

    @XmlAttribute
    public PartitionRangeDatumKind kind;

    /**
     * Const (or A_Const in raw tree), if kind is PARTITION_RANGE_DATUM_VALUE, else NULL
     */
    @XmlElement
    public Node value;

    /**
     * Constructor
     */
    public PartitionRangeDatum() {
        super(NodeTag.T_PartitionRangeDatum);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public PartitionRangeDatum(PartitionRangeDatum original) {
        super(original);
        this.kind = original.kind;
        if (original.value != null) {
            this.value = original.value.clone();
        }
    }

    @Override
    public PartitionRangeDatum clone() {
        PartitionRangeDatum clone = (PartitionRangeDatum) super.clone();
        if (value != null) {
            clone.value = value.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        if (kind != null) {
            switch (kind) {
            case PARTITION_RANGE_DATUM_MAXVALUE:
                return "maxvalue";
            case PARTITION_RANGE_DATUM_MINVALUE:
                return "minvalue";
            case PARTITION_RANGE_DATUM_VALUE:
                return value.toString();
            default:
                return "Unknown " + PartitionRangeDatumKind.class.getName() + "." + kind + " in " + getClass().getName() + ".kind";
            }
        }
        return ("Please implement " + getClass().getName() + ".toString()");
    }
}
