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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class ColumnDef extends Node {

    /** name of column */
    @XmlAttribute
    public String colname;

    /** type of column */
    @XmlElement
    public TypeName typeName;

    /** number of times column is inherited */
    @XmlAttribute
    public int inhcount;

    /** column has local (non-inherited) def'n */
    @XmlAttribute
    public boolean is_local;

    /** NOT NULL constraint specified? */
    @XmlAttribute
    public boolean is_not_null;

    /** column definition came from table type */
    @XmlAttribute
    public boolean is_from_type;

    /**
     * column def came from partition parent
     * 
     * @since 5.0
     */
    @XmlAttribute
    public boolean is_from_parent;

    /** attstorage setting, or 0 for default */
    @XmlAttribute
    public char storage;

    /** default value (untransformed parse tree) */
    @XmlElement
    public Node raw_default;

    /** default value (transformed expr tree) */
    @XmlElement
    public Node cooked_default;

    /** untransformed COLLATE spec, if any */
    @XmlElement
    public CollateClause collClause;

    /** collation OID (InvalidOid if not set) */
    @XmlElement
    public Oid collOid;

    /** other constraints on column */
    @XmlElementWrapper(name = "constraints")
    @XmlElement(name = "constraint")
    public List<Constraint> constraints;

    /** per-column FDW options */
    @XmlElementWrapper(name = "fdwoptions")
    @XmlElement(name = "fdwoption")
    public List<DefElem> fdwoptions;

    /**
     * Constructor
     */
    public ColumnDef() {
        super(NodeTag.T_ColumnDef);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The ColumnDef to copy
     */
    public ColumnDef(ColumnDef original) {
        super(original);

        this.colname = original.colname;
        if (original.typeName != null) {
            this.typeName = original.typeName.clone();
        }
        this.inhcount = original.inhcount;
        this.is_local = original.is_local;
        this.is_not_null = original.is_not_null;
        this.is_from_type = original.is_from_type;
        this.is_from_parent = original.is_from_parent;
        this.storage = original.storage;
        if (original.collClause != null) {
            this.collClause = original.collClause.clone();
        }
        if (original.constraints != null) {
            this.constraints = original.constraints.clone();
        }
        if (original.fdwoptions != null) {
            this.fdwoptions = original.fdwoptions.clone();
        }
    }

    @Override
    public ColumnDef clone() {
        ColumnDef clone = (ColumnDef) super.clone();
        if (typeName != null) {
            clone.typeName = typeName.clone();
        }
        if (constraints != null) {
            clone.constraints = constraints.clone();
        }
        if (fdwoptions != null) {
            clone.fdwoptions = fdwoptions.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        String space = "";
        if (colname != null) {
            result.append(ParserUtil.identifierToSql(colname));
            space = " ";
        }

        if (typeName != null) {
            result.append(space).append(typeName);
            space = " ";
        }

        if (raw_default != null) {
            result.append(space).append("using ").append(raw_default);
            space = " ";
        }

        if (inhcount != 0) {
            result.append(space).append("??????? inhcount=").append(inhcount).append(" ???????");
            space = " ";
        }

        if (is_not_null) {
            result.append(space).append("not null");
            space = " ";
        }

        if (is_from_type) {
            result.append(space).append("??????? is_from_type ???????");
            space = " ";
        }

        if (is_from_parent) {
            result.append(space).append("??????? is_from_parent ???????");
            space = " ";
        }

        if (storage != 0) {
            result.append(space).append("??????? storage=").append(storage).append(" ???????");
            space = " ";
        }

        if (cooked_default != null) {
            result.append(space).append("??????? cooked_default=").append(cooked_default).append(" ???????");
            space = " ";
        }

        if (collOid != null) {
            result.append(space).append("??????? collOid=").append(collOid).append(" ???????");
            space = " ";
        }

        if (fdwoptions != null) {
            result.append(space).append("options (");
            space = " ";
            String separator = "";
            for (DefElem option : fdwoptions) {
                result.append(separator).append(ParserUtil.identifierToSql(option.defname)).append(' ')
                        .append(ParserUtil.toSqlTextString(option.arg));
                separator = ", ";
            }
            result.append(')');
        }

        if (collClause != null) {
            result.append(collClause);
        }

        if (constraints != null) {
            if (typeName == null) {
                result.append(space);
            }
            for (Constraint constraint : constraints) {
                result.append(' ').append(constraint);
            }
        }

        return result.toString();
    }
}
