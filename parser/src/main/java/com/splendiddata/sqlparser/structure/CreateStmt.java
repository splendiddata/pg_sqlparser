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
import com.splendiddata.sqlparser.enums.OnCommitAction;

/**
 * Copied from /postgresql-12beta2/src/include/nodes/parsenodes.h
 * <p>
 * ----------------------<br>
 * Create Table Statement
 * </p>
 * <p>
 * NOTE: in the raw gram.y output, ColumnDef and Constraint nodes are intermixed in tableElts, and constraints is NIL.
 * After parse analysis, tableElts contains just ColumnDefs, and constraints contains just Constraint nodes (in fact,
 * only CONSTR_CHECK nodes, in the present implementation).<br>
 * ----------------------
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CreateStmt extends Node {

    /** relation to create */
    @XmlElement
    public RangeVar relation;

    /** column definitions (list of ColumnDef) */
    @XmlElementWrapper(name = "tableElts")
    @XmlElement(name = "tableElt")
    public List<ColumnDef> tableElts;

    /**
     * relations to inherit from (list of inhRelation)
     */
    @XmlElementWrapper(name = "inhRelations")
    @XmlElement(name = "inhRelation")
    public List<RangeVar> inhRelations;

    /**
     * FOR VALUES clause
     * 
     * @since 5.0
     */
    public PartitionBoundSpec partbound;

    /**
     * PARTITION BY clause
     * 
     * @since 5.0
     */
    public PartitionSpec partspec;

    /** OF typename */
    @XmlElement
    public TypeName ofTypename;

    /** constraints (list of Constraint nodes) */
    @XmlElementWrapper(name = "constraints")
    @XmlElement(name = "constraint")
    public List<Node> constraints;

    /** options from WITH clause */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /** what do we do at COMMIT? */
    @XmlAttribute
    public OnCommitAction oncommit;

    /** table space to use, or NULL */
    @XmlAttribute
    public String tablespacename;

    /** table access method @since version 7.0 - Postgres 12 */
    @XmlAttribute
    public String accessMethod;

    /** just do nothing if it already exists? */
    @XmlAttribute
    public boolean if_not_exists;

    /**
     * Constructor
     */
    public CreateStmt() {
        super(NodeTag.T_CreateStmt);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The original
     */
    public CreateStmt(CreateStmt toCopy) {
        super(toCopy);
        if (toCopy.relation != null) {
            this.relation = toCopy.relation.clone();
        }
        if (toCopy.tableElts != null) {
            this.tableElts = toCopy.tableElts.clone();
        }
        if (toCopy.inhRelations != null) {
            this.inhRelations = toCopy.inhRelations.clone();
        }
        if (toCopy.partbound != null) {
            this.partbound = toCopy.partbound.clone();
        }
        if (toCopy.partspec != null) {
            this.partspec = toCopy.partspec.clone();
        }
        if (toCopy.ofTypename != null) {
            this.ofTypename = toCopy.ofTypename.clone();
        }
        if (toCopy.constraints != null) {
            this.constraints = toCopy.constraints.clone();
        }
        if (toCopy.options != null) {
            this.options = toCopy.options.clone();
        }
        this.oncommit = toCopy.oncommit;
        this.tablespacename = toCopy.tablespacename;
        this.accessMethod = toCopy.accessMethod;
        this.if_not_exists = toCopy.if_not_exists;
    }

    @Override
    public CreateStmt clone() {
        CreateStmt clone = (CreateStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (tableElts != null) {
            clone.tableElts = tableElts.clone();
        }
        if (inhRelations != null) {
            clone.inhRelations = inhRelations.clone();
        }
        if (partbound != null) {
            clone.partbound = partbound.clone();
        }
        if (partspec != null) {
            clone.partspec = partspec.clone();
        }
        if (ofTypename != null) {
            clone.ofTypename = ofTypename.clone();
        }
        if (constraints != null) {
            clone.constraints = constraints.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return "create" + toStringStartingWithTable();
    }

    /**
     * toString() except the "create" verb.
     * <p>
     * This methi=od is created so support the
     * {@link com.splendiddata.sqlparser.structure.CreateForeignTableStmt#toString()} method, which uses this class but
     * with "create foreign table" syntax instead of "create table".
     * </p>
     *
     * @return String
     */
    public String toStringStartingWithTable() {
        StringBuilder result = new StringBuilder();

        switch (relation.relpersistence) {
        case RELPERSISTENCE_PERMANENT:
            break;
        case RELPERSISTENCE_TEMP:
            result.append(" temporary");
            break;
        case RELPERSISTENCE_UNLOGGED:
            result.append(" unlogged");
            break;
        default:
            result.append(
                    ParserUtil.reportUnknownValue("RangeVar.relpersistence", relation.relpersistence, getClass()));
        }

        result.append(" table ");
        if (if_not_exists) {
            result.append("if not exists ");
        }

        result.append(relation);

        if (ofTypename != null) {
            result.append(" of ").append(ofTypename);
            if (tableElts != null) {
                result.append(" ");
                result.append(tableElts);
            }
        } else if (partbound != null) {
            result.append(" partition of ").append(inhRelations.get(0));
            if (tableElts != null) {
                result.append(" ");
                result.append(tableElts);
            }
            result.append(partbound);
        } else if (tableElts == null) {
            result.append("()");
        } else {
            result.append(tableElts);
        }

        if (partbound == null && inhRelations != null) {
            result.append(" inherits (");
            String separator = "";
            for (RangeVar rel : inhRelations) {
                result.append(separator).append(rel);
                separator = ", ";
            }
            result.append(')');
        }
        
        if (partspec != null) {
            result.append(' ').append(partspec);
        }

        if (options != null) {
            if (options.size() == 1 && "oids".equals(options.get(0).defname.toLowerCase())) {
                Value val = (Value) options.get(0).arg;
                if (val == null || val.val.ival == 0) {
                    result.append(" without oids");
                } else {
                    result.append(" with oids");
                }
            } else {
                result.append(" with (");
                String separator = "";
                for (DefElem option : options) {
                    result.append(separator).append(option.defname.toLowerCase());
                    if (option.arg != null) {
                        if (NodeTag.T_Integer.equals(option.arg.type)) {
                            result.append(" = ").append(option.arg);
                        } else {
                            result.append(" = ").append(ParserUtil.toSqlTextString(option.arg.toString()));
                        }
                    }
                    separator = ", ";
                }
                result.append(')');
            }
        }

        if (oncommit != null) {
            switch (oncommit) {
            case ONCOMMIT_DELETE_ROWS:
                result.append(" on commit delete rows");
                break;
            case ONCOMMIT_DROP:
                result.append(" on commit drop");
                break;
            case ONCOMMIT_PRESERVE_ROWS:
                result.append(" on commit preserve rows");
                break;
            case ONCOMMIT_NOOP:
                break;
            default:
                result.append(ParserUtil.reportUnknownValue("OnCommitAction", oncommit, getClass()));
                break;
            }
        }

        if (tablespacename != null) {
            result.append(" tablespace ").append(ParserUtil.identifierToSql(tablespacename));
        }

        if (accessMethod != null) {
            result.append(" using ").append(ParserUtil.identifierToSql(accessMethod));
        }
        
        return result.toString();
    }
}
