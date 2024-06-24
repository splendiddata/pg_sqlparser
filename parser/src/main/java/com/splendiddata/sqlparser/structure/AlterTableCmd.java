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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.AlterTableType;
import com.splendiddata.sqlparser.enums.AttributeIdentity;
import com.splendiddata.sqlparser.enums.DropBehavior;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * one subcommand of an ALTER TABLE
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class AlterTableCmd extends Node {
    /** Type of table alteration to apply */
    @XmlAttribute
    public AlterTableType subtype;

    /**
     * column, constraint, or trigger to act on, or new owner or tablespace
     */
    @XmlAttribute
    public String name;

    /**
     * attribute number for columns referenced by number
     * 
     * @since 6.0, Postgres version 11
     */
    @XmlAttribute
    public int num;

    /**
     * RoleSpec
     * 
     * @since 3.0, Postgres version 9.5
     */
    @XmlElement
    public Node newowner;

    /**
     * definition of new column, index, constraint, or parent table
     */
    @XmlElement
    public Node def;

    /** RESTRICT or CASCADE for DROP cases */
    @XmlAttribute
    public DropBehavior behavior;

    /** skip error if missing? */
    @XmlAttribute
    public boolean missing_ok;

    /**
     * Constructor
     */
    public AlterTableCmd() {
        super(NodeTag.T_AlterTableCmd);
    }

    /**
     * Copy constructor
     * 
     * @param toCopy
     *            the AlterTableCmd to copy
     */
    public AlterTableCmd(AlterTableCmd toCopy) {
        super(toCopy);
        this.subtype = toCopy.subtype;
        this.name = toCopy.name;
        this.num = toCopy.num;
        if (toCopy.newowner != null) {
            this.newowner = toCopy.newowner.clone();
        }
        if (toCopy.def != null) {
            this.def = toCopy.def.clone();
        }
        this.behavior = toCopy.behavior;
        this.missing_ok = toCopy.missing_ok;
    }

    @Override
    public AlterTableCmd clone() {
        AlterTableCmd clone = (AlterTableCmd) super.clone();
        if (newowner != null) {
            clone.newowner = newowner;
        }
        if (def != null) {
            clone.def = def.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return toString("column");
    }

    /**
     * For some statements a column is not called "column" but "attribute". To distinct between the two, this method has
     * been created.
     *
     * @param columnOrAttribute
     *            "column" or "attribute"
     * @return String the content in an SQL string
     */
    @SuppressWarnings("unchecked")
    public String toString(String columnOrAttribute) {
        switch (subtype) {
        case AT_AddColumn:
            StringBuilder result = new StringBuilder().append("add ").append(columnOrAttribute);
            if (missing_ok) {
                result.append(" if not exists");
            }
            result.append(' ').append(def);
            if (DropBehavior.DROP_CASCADE.equals(behavior)) {
                result.append(" cascade");
            }
            return result.toString();
        case AT_AddConstraint:
            return new StringBuilder().append("add ").append(def).toString();
        case AT_AddIndex:
            return new StringBuilder().append("add index ").append(ParserUtil.identifierToSql(name)).append(' ')
                    .append(def).toString();
        case AT_AddIndexConstraint:
            return new StringBuilder().append(ParserUtil.identifierToSql(name)).append(' ').append(def).toString();
        case AT_AddInherit:
            return "inherit " + def;
        case AT_AddOf:
            return new StringBuilder().append("of ").append(def).toString();
        case AT_AddOids:
            return "set with oids";
        case AT_AlterColumnGenericOptions:
            result = new StringBuilder().append("alter column ").append(ParserUtil.identifierToSql(name))
                    .append(" options (");
            String separator = "";
            for (DefElem opt : (List<DefElem>) def) {
                result.append(separator).append(opt.defaction).append(ParserUtil.identifierToSql(opt.defname));
                if (opt.arg != null) {
                    result.append(' ').append(ParserUtil.toSqlTextString(opt.arg));
                }
                separator = ", ";
            }
            result.append(')');
            return result.toString();
        case AT_AlterColumnType:
            result = new StringBuilder().append("alter ").append(columnOrAttribute).append(' ')
                    .append(ParserUtil.identifierToSql(name)).append(" set data type ").append(def);
            if (DropBehavior.DROP_CASCADE.equals(behavior)) {
                result.append(" cascade");
            }
            return result.toString();
        case AT_AlterConstraint:
            return "alter " + def;
        case AT_ChangeOwner:
            return new StringBuilder().append("owner to ").append(newowner).toString();
        case AT_ClusterOn:
            return new StringBuilder().append("cluster on ").append(ParserUtil.identifierToSql(name)).toString();
        case AT_ColumnDefault:
            if (def == null) {
                return new StringBuilder().append("alter column ").append(ParserUtil.identifierToSql(name))
                        .append(" drop default").toString();
            }
            return new StringBuilder().append("alter column ").append(ParserUtil.identifierToSql(name))
                    .append(" set default ").append(def).toString();
        case AT_DisableRule:
            return new StringBuilder().append("disable rule ").append(ParserUtil.identifierToSql(name)).toString();
        case AT_DisableTrig:
            return new StringBuilder().append("disable trigger ").append(ParserUtil.identifierToSql(name)).toString();
        case AT_DisableTrigAll:
            return "disable trigger all";
        case AT_DisableTrigUser:
            return "disable trigger user";
        case AT_DropCluster:
            return "set without cluster";
        case AT_DropColumn:
            result = new StringBuilder("drop ").append(columnOrAttribute);
            if (missing_ok) {
                result.append(" if exists");
            }
            result.append(' ').append(ParserUtil.identifierToSql(name));
            if (DropBehavior.DROP_CASCADE.equals(behavior)) {
                result.append(" cascade");
            }
            return result.toString();
        case AT_DropConstraint:
            result = new StringBuilder("drop constraint ");
            if (missing_ok) {
                result.append("if exists ");
            }
            result.append(ParserUtil.identifierToSql(name));
            return result.toString();
        case AT_DropInherit:
            return "no inherit " + def;
        case AT_DropNotNull:
            return new StringBuilder().append("alter column ").append(ParserUtil.identifierToSql(name))
                    .append(" drop not null").toString();
        case AT_DropOf:
            return "not of";
        case AT_DropOids:
            return "set without oids";
        case AT_EnableAlwaysRule:
            return new StringBuilder().append("enable always rule ").append(ParserUtil.identifierToSql(name))
                    .toString();
        case AT_EnableAlwaysTrig:
            return new StringBuilder().append("enable always trigger ").append(ParserUtil.identifierToSql(name))
                    .toString();
        case AT_EnableReplicaRule:
            return new StringBuilder().append("enable replica rule ").append(ParserUtil.identifierToSql(name))
                    .toString();
        case AT_EnableReplicaTrig:
            return new StringBuilder().append("enable replica trigger ").append(ParserUtil.identifierToSql(name))
                    .toString();
        case AT_EnableRule:
            return new StringBuilder().append("enable rule ").append(ParserUtil.identifierToSql(name)).toString();
        case AT_EnableTrig:
            return new StringBuilder().append("enable trigger ").append(ParserUtil.identifierToSql(name)).toString();
        case AT_EnableTrigAll:
            return "enable trigger all";
        case AT_EnableTrigUser:
            return "enable trigger user";
        case AT_GenericOptions:
            result = new StringBuilder("options (");
            separator = "";
            for (DefElem opt : (List<DefElem>) def) {
                result.append(separator).append(opt.defaction).append(ParserUtil.identifierToSql(opt.defname));
                if (opt.arg != null) {
                    result.append(' ').append(ParserUtil.toSqlTextString(opt.arg));
                }
                separator = ", ";
            }
            result.append(')');
            return result.toString();
        case AT_ReplaceRelOptions:
            return new StringBuilder().append(ParserUtil.identifierToSql(name)).append(' ').append(def).toString();
        case AT_ReplicaIdentity:
            return def.toString();
        case AT_ResetOptions:
        case AT_ResetRelOptions:
            result = new StringBuilder("reset (");
            if (def != null) {
                separator = "";
                for (Node node : (List<Node>) def) {
                    result.append(separator);
                    separator = ", ";
                    if (node instanceof DefElem) {
                        result.append(' ').append(ParserUtil.identifierToSql(((DefElem) node).defname));
                    }
                }
            }
            result.append(')');
            return result.toString();
        case AT_SetNotNull:
            return new StringBuilder().append("alter column ").append(ParserUtil.identifierToSql(name))
                    .append(" set not null").toString();
        case AT_SetOptions:
        case AT_SetRelOptions:
            result = new StringBuilder("set (");
            if (def != null) {
                separator = "";
                for (Node node : (List<Node>) def) {
                    result.append(separator);
                    separator = ", ";
                    if (node instanceof DefElem) {
                        result.append(' ').append(ParserUtil.identifierToSql(((DefElem) node).defname));
                        if (((DefElem) node).arg != null) {
                            result.append(" = ").append(((DefElem) node).arg);
                        }
                    }
                }
            }
            result.append(')');
            return result.toString();
        case AT_DropExpression:
            result = new StringBuilder(" alter column ").append(name).append(" drop expression");
            if (missing_ok) {
                result.append(" if exists");
            }
            return result.toString();
        case AT_SetStatistics:
            if (name == null) {
                return new StringBuilder().append("alter column ").append(num).append(" set statistics ").append(def)
                        .toString();
            }
            return new StringBuilder().append("alter column ").append(ParserUtil.identifierToSql(name))
                    .append(" set statistics ").append(def).toString();
        case AT_SetStorage:
            return new StringBuilder().append("alter column ").append(ParserUtil.identifierToSql(name))
                    .append(" set storage ").append(def).toString();
        case AT_SetCompression:
            return new StringBuilder().append("alter column ").append(ParserUtil.identifierToSql(name))
                    .append(" set compression ").append(def).toString();
        case AT_SetTableSpace:
            return new StringBuilder().append("set tablespace ").append(ParserUtil.identifierToSql(name)).toString();
        case AT_ValidateConstraint:
            return new StringBuilder().append("validate constraint ").append(ParserUtil.identifierToSql(name))
                    .toString();
        case AT_DisableRowSecurity:
            return "disable row level security";
        case AT_EnableRowSecurity:
            return "enable row level security";
        case AT_SetLogged:
            return "set logged";
        case AT_SetUnLogged:
            return "set unlogged";
        case AT_ForceRowSecurity:
            return "force row level security";
        case AT_NoForceRowSecurity:
            return "no force row level security";
        case AT_AttachPartition:
            return "attach partition " + def;
        case AT_DetachPartition:
            return "detach partition " + def;
        case AT_DetachPartitionFinalize:
            return "detach partition " + def + " finalize";
        case AT_SplitPartition:
            PartitionCmd partCmd = (PartitionCmd) def;
            return new StringBuilder("split partition ").append(partCmd.name).append(" into ").append(partCmd.partlist)
                    .toString();
        case AT_MergePartitions:
            partCmd = (PartitionCmd) def;
            return new StringBuilder("merge partitions ").append(partCmd.partlist).append(" into ").append(partCmd.name)
                    .toString();
        case AT_AddIdentity:
            return new StringBuilder(" alter column ").append(name).append(" add ").append(def).toString();
        case AT_DropIdentity:
            result = new StringBuilder(" alter column ").append(name).append(" drop identity");
            if (missing_ok) {
                result.append(" if exists");
            }
            return result.toString();
        case AT_SetIdentity:
            result = new StringBuilder(" alter column ").append(name);
            for (DefElem elem : (List<DefElem>) def) {
                result.append(" set ").append(elem.defname);
                if ("generated".equals(elem.defname)) {
                    if (((Value) elem.arg).val.ival == AttributeIdentity.ATTRIBUTE_IDENTITY_ALWAYS.VALUE) {
                        result.append(" always");
                    } else if (((Value) elem.arg).val.ival == AttributeIdentity.ATTRIBUTE_IDENTITY_BY_DEFAULT.VALUE) {
                        result.append(" by default");
                    } else {
                        return ParserUtil.reportUnknownValue("arg in DefElem " + ParserUtil.stmtToXml(elem), elem.arg,
                                getClass());
                    }
                } else if (elem.arg != null) {
                    result.append(' ').append(elem.arg);
                }
            }
            return result.toString();
        case AT_SetAccessMethod:
            if (name == null) {
                return " set access method default";
            }
            return " set access method " + ParserUtil.identifierToSql(name);
        case AT_SetExpression:
            return new StringBuilder(" alter column ").append(name).append(" set expression as (").append(def)
                    .append(')').toString();
        default:
            return ParserUtil.reportUnknownValue("subtype", subtype, getClass());
        }
    }
}
