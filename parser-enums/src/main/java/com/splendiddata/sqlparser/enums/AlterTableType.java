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

package com.splendiddata.sqlparser.enums;

/**
 * Copied from /postgresql-9.5beta2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
public enum AlterTableType {
    /** add column */
    AT_AddColumn,
    /** internal to commands/tablecmds.c */
    AT_AddColumnRecurse,
    /** implicitly via CREATE OR REPLACE VIEW */
    AT_AddColumnToView,
    /** alter column default */
    AT_ColumnDefault,
    /** alter column drop not null */
    AT_DropNotNull,
    /** alter column set not null */
    AT_SetNotNull,
    /**
     * alter column set expression
     * 
     * @since Postgres 17
     */
    AT_SetExpression,
    /**
     * alter column drop expression
     * 
     * @since 8.0 - Postgres 13
     */
    AT_DropExpression,
    /** alter column set statistics */
    AT_SetStatistics,
    /** alter column set ( options ) */
    AT_SetOptions,
    /** alter column reset ( options ) */
    AT_ResetOptions,
    /** alter column set storage */
    AT_SetStorage,
    /**
     * alter column set compression
     * 
     * @since 14.0
     */
    AT_SetCompression,
    /** drop column */
    AT_DropColumn,
    /** internal to commands/tablecmds.c */
    AT_DropColumnRecurse,
    /** add index */
    AT_AddIndex,
    /** internal to commands/tablecmds.c */
    AT_ReAddIndex,
    /** add constraint */
    AT_AddConstraint,
    /** internal to commands/tablecmds.c */
    AT_AddConstraintRecurse,
    /** internal to commands/tablecmds.c */
    AT_ReAddConstraint,
    /** alter constraint */
    AT_AlterConstraint,
    /** validate constraint */
    AT_ValidateConstraint,
    /** internal to commands/tablecmds.c */
    AT_ValidateConstraintRecurse,
    /**
     * pre-processed add constraint (local in parser/parse_utilcmd.c)
     */
    AT_ProcessedConstraint,
    /** add constraint using existing index */
    AT_AddIndexConstraint,
    /** drop constraint */
    AT_DropConstraint,
    /** internal to commands/tablecmds.c */
    AT_DropConstraintRecurse,
    /** internal to commands/tablecmds.c */
    AT_ReAddComment,
    /** alter column type */
    AT_AlterColumnType,
    /** alter column OPTIONS (...) */
    AT_AlterColumnGenericOptions,
    /** change owner */
    AT_ChangeOwner,
    /** CLUSTER ON */
    AT_ClusterOn,
    /** SET WITHOUT CLUSTER */
    AT_DropCluster,
    /** SET LOGGED */
    AT_SetLogged,
    /** SET UNLOGGED */
    AT_SetUnLogged,
    /** SET WITH OIDS */
    AT_AddOids,
    /** internal to commands/tablecmds.c */
    AT_AddOidsRecurse,
    /** SET WITHOUT OIDS */
    AT_DropOids,
    /**
     * SET ACCESS METHOD
     * 
     * @since Postgres 15
     */
    AT_SetAccessMethod,
    /** SET TABLESPACE */
    AT_SetTableSpace,
    /** SET (...) -- AM specific parameters */
    AT_SetRelOptions,
    /** RESET (...) -- AM specific parameters */
    AT_ResetRelOptions,
    /** replace reloption list in its entirety */
    AT_ReplaceRelOptions,
    /** ENABLE TRIGGER name */
    AT_EnableTrig,
    /** ENABLE ALWAYS TRIGGER name */
    AT_EnableAlwaysTrig,
    /** ENABLE REPLICA TRIGGER name */
    AT_EnableReplicaTrig,
    /** DISABLE TRIGGER name */
    AT_DisableTrig,
    /** ENABLE TRIGGER ALL */
    AT_EnableTrigAll,
    /** DISABLE TRIGGER ALL */
    AT_DisableTrigAll,
    /** ENABLE TRIGGER USER */
    AT_EnableTrigUser,
    /** DISABLE TRIGGER USER */
    AT_DisableTrigUser,
    /** ENABLE RULE name */
    AT_EnableRule,
    /** ENABLE ALWAYS RULE name */
    AT_EnableAlwaysRule,
    /** ENABLE REPLICA RULE name */
    AT_EnableReplicaRule,
    /** DISABLE RULE name */
    AT_DisableRule,
    /** INHERIT parent */
    AT_AddInherit,
    /** NO INHERIT parent */
    AT_DropInherit,
    /** OF &lt;type_name&gt; */
    AT_AddOf,
    /** NOT OF */
    AT_DropOf,
    /** REPLICA IDENTITY */
    AT_ReplicaIdentity,
    /** ENABLE ROW SECURITY */
    AT_EnableRowSecurity,
    /** DISABLE ROW SECURITY */
    AT_DisableRowSecurity,
    /** FORCE ROW SECURITY */
    AT_ForceRowSecurity,
    /** NO FORCE ROW SECURITY */
    AT_NoForceRowSecurity,
    /** OPTIONS (...) */
    AT_GenericOptions,
    /**
     * ATTACH PARTITION
     * 
     * @since 5.0
     */
    AT_AttachPartition,
    /**
     * DETACH PARTITION
     * 
     * @since 5.0
     */
    AT_DetachPartition,
    /**
     * DETACH PARTITION FINALIZE
     * 
     * @since 14.0
     */
    AT_DetachPartitionFinalize,
    /**
     * SPLIT PARTITION
     * 
     * @since Postgres 17
     */
    AT_SplitPartition,
    /**
     * MERGE PARTITIONS
     * 
     * @since Postgres 17
     */
    AT_MergePartitions,
    /**
     * ADD IDENTITY
     * 
     * @since 5.0
     */
    AT_AddIdentity,
    /**
     * SET identity column options
     * 
     * @since 5.0
     */
    AT_SetIdentity,
    /**
     * DROP IDENTITY
     * 
     * @since 5.0
     */
    AT_DropIdentity;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (AlterTableType type : values()) {
            format.append(separator).append(type);
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
