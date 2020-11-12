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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * RangeTableFunc - raw form of "table functions" such as XMLTABLE
 * <p>
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class RangeTableFuncCol extends Node {

    /** name of generated column */
    @XmlAttribute
    public String colname;

    /** type of generated column */
    @XmlElement
    public TypeName typeName;

    /** does it have FOR ORDINALITY? */
    @XmlAttribute
    public boolean for_ordinality;

    /** does it have NOT NULL? */
    @XmlAttribute
    public boolean is_not_null;

    /** column filter expression */
    @XmlElement
    public Node colexpr;

    /** column default value expression */
    @XmlElement
    public Node coldefexpr;

    /** table alias &amp; optional column aliases */
    @XmlElement
    public Alias alias;

    /**
     * Constructor
     */
    public RangeTableFuncCol() {
        super(NodeTag.T_RangeTableFuncCol);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            the RangeTableFunc to copy
     */
    public RangeTableFuncCol(RangeTableFuncCol original) {
        super(original);
        this.colname = original.colname;
        if (original.typeName != null) {
            this.typeName = original.typeName.clone();
        }
        this.for_ordinality = original.for_ordinality;
        this.is_not_null = original.is_not_null;
        if (original.colexpr != null) {
            this.colexpr = original.colexpr;
        }
        if (original.coldefexpr != null) {
            this.coldefexpr = original.coldefexpr;
        }
        if (original.alias != null) {
            this.alias = original.alias.clone();
        }
    }

    @Override
    public RangeTableFuncCol clone() {
        RangeTableFuncCol clone = (RangeTableFuncCol) super.clone();
        if (typeName != null) {
            clone.typeName = typeName.clone();
        }
        if (colexpr != null) {
            clone.colexpr = colexpr.clone();
        }
        if (coldefexpr != null) {
            clone.coldefexpr = coldefexpr.clone();
        }
        if (alias != null) {
            clone.alias = alias.clone();
        }

        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder().append(ParserUtil.identifierToSql(colname));
        if (typeName != null) {
            result.append(' ').append(typeName);
        }
        if (colexpr != null) {
            result.append(" path ").append(colexpr);
        }
        if (coldefexpr != null) {
            result.append(" default ").append(coldefexpr);
        }
        if (is_not_null) {
            result.append(" not null");
        }
        if (for_ordinality) {
            result.append(" for ordinality");
        }

        return result.toString();
    }
}
