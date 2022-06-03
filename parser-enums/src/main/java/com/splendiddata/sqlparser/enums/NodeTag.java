/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2022
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
 * Type of node, originally copied from /postgresql-9.3.4/src/include/nodes/nodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public enum NodeTag {
    T_Invalid,

    /*
     * TAGS FOR EXECUTOR NODES (execnodes.h)
     */
    T_IndexInfo,
    T_ExprContext,
    T_ProjectionInfo,
    T_JunkFilter,
    /** @since 6.0 - Postgres version 11 */
    T_OnConflictSetState,
    T_ResultRelInfo,
    T_EState,
    T_TupleTableSlot,

    /*
     * TAGS FOR PLAN NODES (plannodes.h)
     */
    T_Plan,
    T_Result,
    /** @since 6.0 - Postgres version 11 */
    T_ProjectSet,
    T_ModifyTable,
    T_Append,
    T_MergeAppend,
    T_RecursiveUnion,
    T_BitmapAnd,
    T_BitmapOr,
    T_Scan,
    T_SeqScan,
    /** @since 6.0 - Postgres version 11 */
    T_SampleScan,
    T_IndexScan,
    T_IndexOnlyScan,
    T_BitmapIndexScan,
    T_BitmapHeapScan,
    T_TidScan,
    T_SubqueryScan,
    T_FunctionScan,
    T_ValuesScan,
    /** @since 6.0 - Postgres version 11 */
    T_TableFuncScan,
    T_CteScan,
    /** @since 6.0 - Postgres version 11 */
    T_NamedTuplestoreScan,
    T_WorkTableScan,
    T_ForeignScan,
    /** @since 6.0 - Postgres version 11 */
    T_CustomScan,
    T_Join,
    T_NestLoop,
    T_MergeJoin,
    T_HashJoin,
    T_Material,
    T_Sort,
    T_Group,
    T_Agg,
    T_WindowAgg,
    T_Unique,
    /** @since 6.0 - Postgres version 11 */
    T_Gather,
    /** @since 6.0 - Postgres version 11 */
    T_GatherMerge,
    T_Hash,
    T_SetOp,
    T_LockRows,
    T_Limit,
    /* these aren't subclasses of Plan: */
    T_NestLoopParam,
    T_PlanRowMark,
    /** @since 6.0 - Postgres version 11 */
    T_PartitionPruneInfo,
    /** @since 6.0 - Postgres version 11 */
    T_PartitionedRelPruneInfo,
    /** @since 6.0 - Postgres version 11 */
    T_PartitionPruneStepOp,
    /** @since 6.0 - Postgres version 11 */
    T_PartitionPruneStepCombine,
    T_PlanInvalItem,

    /*
     * TAGS FOR PLAN STATE NODES (execnodes.h)
     *
     * These should correspond one-to-one with Plan node types.
     */
    T_PlanState,
    T_ResultState,
    /** @since 6.0 - Postgres version 11 */
    T_ProjectSetState,
    T_ModifyTableState,
    T_AppendState,
    T_MergeAppendState,
    T_RecursiveUnionState,
    T_BitmapAndState,
    T_BitmapOrState,
    T_ScanState,
    T_SeqScanState,
    /** @since 6.0 - Postgres version 11 */
    T_SampleScanState,
    T_IndexScanState,
    T_IndexOnlyScanState,
    T_BitmapIndexScanState,
    T_BitmapHeapScanState,
    T_TidScanState,
    T_SubqueryScanState,
    T_FunctionScanState,
    /** @since 6.0 - Postgres version 11 */
    T_TableFuncScanState,
    T_ValuesScanState,
    T_CteScanState,
    /** @since 6.0 - Postgres version 11 */
    T_NamedTuplestoreScanState,
    T_WorkTableScanState,
    T_ForeignScanState,
    /** @since 6.0 - Postgres version 11 */
    T_CustomScanState,
    T_JoinState,
    T_NestLoopState,
    T_MergeJoinState,
    T_HashJoinState,
    T_MaterialState,
    T_SortState,
    T_GroupState,
    T_AggState,
    T_WindowAggState,
    T_UniqueState,
    /** @since 6.0 - Postgres version 11 */
    T_GatherState,
    /** @since 6.0 - Postgres version 11 */
    T_GatherMergeState,
    T_HashState,
    T_SetOpState,
    T_LockRowsState,
    T_LimitState,

    /*
     * TAGS FOR PRIMITIVE NODES (primnodes.h)
     */
    T_Alias,
    T_RangeVar,
    /** @since 6.0 - Postgres version 11 */
    T_TableFunc,
    T_Expr,
    T_Var,
    T_Const,
    T_Param,
    T_Aggref,
    T_GroupingFunc,
    T_WindowFunc,
    T_ArrayRef,
    T_FuncExpr,
    T_NamedArgExpr,
    T_OpExpr,
    T_DistinctExpr,
    T_NullIfExpr,
    T_ScalarArrayOpExpr,
    T_BoolExpr,
    T_SubLink,
    T_SubPlan,
    T_AlternativeSubPlan,
    T_FieldSelect,
    T_FieldStore,
    T_RelabelType,
    T_CoerceViaIO,
    T_ArrayCoerceExpr,
    T_ConvertRowtypeExpr,
    T_CollateExpr,
    T_CaseExpr,
    T_CaseWhen,
    T_CaseTestExpr,
    T_ArrayExpr,
    T_RowExpr,
    T_RowCompareExpr,
    T_CoalesceExpr,
    T_MinMaxExpr,
    /** @since 5.0 */
    T_SQLValueFunction,
    T_XmlExpr,
    T_NullTest,
    T_BooleanTest,
    T_CoerceToDomain,
    T_CoerceToDomainValue,
    T_SetToDefault,
    T_CurrentOfExpr,
    /** @since 6.0 - Postgres version 11 */
    T_NextValueExpr,
    /** @since 6.0 - Postgres version 11 */
    T_InferenceElem,
    T_TargetEntry,
    T_RangeTblRef,
    T_JoinExpr,
    T_FromExpr,
    /** @since 6.0 - Postgres version 11 */
    T_OnConflictExpr,
    T_IntoClause,
    /** @since Postgres 15 */
    T_JsonFormat,
    /** @since Postgres 15 */
    T_JsonValueExpr,
    /** @since Postgres 15 */
    T_JsonScalarExpr,
    /** @since Postgres 15 */
    T_JsonSerializeExpr,
    /** @since Postgres 15 */
    T_JsonParseExpr,
    /** @since Postgres 15 */
    T_JsonReturning,

    /*
     * TAGS FOR EXPRESSION STATE NODES (execnodes.h)
     *
     * ExprState represents the evaluation state for a whole expression tree. Most Expr-based plan nodes do not have a
     * corresponding expression state node, they're fully handled within execExpr* - but sometimes the state needs to be
     * shared with other parts of the executor, as for example with AggrefExprState, which nodeAgg.c has to modify.
     */
    T_ExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_GenericExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_WholeRowVarExprState,
    T_AggrefExprState,
    T_WindowFuncExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_ArrayRefExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_FuncExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_ScalarArrayOpExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_BoolExprState,
    /** @since 6.0 - Postgres version 11 */
    T_SetExprState,
    T_SubPlanState,
    T_AlternativeSubPlanState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_FieldSelectState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_FieldStoreState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_CoerceViaIOState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_ArrayCoerceExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_ConvertRowtypeExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_CaseExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_CaseWhenState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_ArrayExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_RowExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_RowCompareExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_CoalesceExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_MinMaxExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_XmlExprState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_NullTestState,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_CoerceToDomainState,
    T_DomainConstraintState,

    /*
     * TAGS FOR PLANNER NODES (relation.h)
     */
    T_PlannerInfo,
    T_PlannerGlobal,
    T_RelOptInfo,
    T_IndexOptInfo,
    /** @since 6.0 - Postgres version 11 */
    T_ForeignKeyOptInfo,
    T_ParamPathInfo,
    T_Path,
    T_IndexPath,
    T_BitmapHeapPath,
    T_BitmapAndPath,
    T_BitmapOrPath,
    T_TidPath,
    /** @since 6.0 - Postgres version 11 */
    T_SubqueryScanPath,
    T_ForeignPath,
    /** @since 6.0 - Postgres version 11 */
    T_CustomPath,
    T_NestPath,
    T_MergePath,
    T_HashPath,
    T_AppendPath,
    T_MergeAppendPath,
    T_ResultPath,
    T_MaterialPath,
    T_UniquePath,
    /** @since 6.0 - Postgres version 11 */
    T_GatherPath,
    /** @since 6.0 - Postgres version 11 */
    T_GatherMergePath,
    /** @since 6.0 - Postgres version 11 */
    T_ProjectionPath,
    /** @since 6.0 - Postgres version 11 */
    T_ProjectSetPath,
    /** @since 6.0 - Postgres version 11 */
    T_SortPath,
    /** @since 6.0 - Postgres version 11 */
    T_GroupPath,
    /** @since 6.0 - Postgres version 11 */
    T_UpperUniquePath,
    /** @since 6.0 - Postgres version 11 */
    T_AggPath,
    /** @since 6.0 - Postgres version 11 */
    T_GroupingSetsPath,
    /** @since 6.0 - Postgres version 11 */
    T_MinMaxAggPath,
    /** @since 6.0 - Postgres version 11 */
    T_WindowAggPath,
    /** @since 6.0 - Postgres version 11 */
    T_SetOpPath,
    /** @since 6.0 - Postgres version 11 */
    T_RecursiveUnionPath,
    /** @since 6.0 - Postgres version 11 */
    T_LockRowsPath,
    /** @since 6.0 - Postgres version 11 */
    T_ModifyTablePath,
    /** @since 6.0 - Postgres version 11 */
    T_LimitPath,
    /* these aren't subclasses of Path: */
    T_EquivalenceClass,
    T_EquivalenceMember,
    T_PathKey,
    /** @since 6.0 - Postgres version 11 */
    T_PathTarget,
    T_RestrictInfo,
    T_PlaceHolderVar,
    T_SpecialJoinInfo,
    /** @deprecated Not in use any more since version 6.0 (Posgres version 11) */
    @Deprecated
    T_LateralJoinInfo,
    T_AppendRelInfo,
    T_PlaceHolderInfo,
    T_MinMaxAggInfo,
    T_PlannerParamItem,
    /** @since 6.0 - Postgres version 11 */
    T_RollupData,
    /** @since 6.0 - Postgres version 11 */
    T_GroupingSetData,
    /** @since 6.0 - Postgres version 11 */
    T_StatisticExtInfo,

    /*
     * TAGS FOR MEMORY NODES (memnodes.h)
     */
    T_MemoryContext,
    T_AllocSetContext,
    /** @since 6.0 - Postgres version 11 */
    T_SlabContext,
    /** @since 6.0 - Postgres version 11 */
    T_GenerationContext,

    /*
     * TAGS FOR VALUE NODES (value.h)
     */
    T_Value,
    T_Integer,
    T_Float,
    /** @since Postgres 15 */
    T_Boolean,
    T_String,
    T_BitString,
    T_Null,

    /*
     * TAGS FOR LIST NODES (pg_list.h)
     */
    T_List,
    T_IntList,
    T_OidList,

    /*
     * TAGS FOR EXTENSIBLE NODES (extensible.h)
     */
    T_ExtensibleNode,

    /*
     * TAGS FOR STATEMENT NODES (mostly in parsenodes.h)
     */
    /** @since 5.0 */
    T_RawStmt,
    T_Query,
    T_PlannedStmt,
    T_InsertStmt,
    T_DeleteStmt,
    T_UpdateStmt,
    /** @since Postgres 15 */
    T_MergeStmt,
    T_SelectStmt,
    /** @since 14.0 */
    T_ReturnStmt,
    /** @since 14.0 */
    T_PLAssignStmt,
    T_AlterTableStmt,
    T_AlterTableCmd,
    T_AlterDomainStmt,
    T_SetOperationStmt,
    T_GrantStmt,
    T_GrantRoleStmt,
    T_AlterDefaultPrivilegesStmt,
    T_ClosePortalStmt,
    T_ClusterStmt,
    T_CopyStmt,
    T_CreateStmt,
    T_DefineStmt,
    T_DropStmt,
    T_TruncateStmt,
    T_CommentStmt,
    T_FetchStmt,
    T_IndexStmt,
    T_CreateFunctionStmt,
    T_AlterFunctionStmt,
    T_DoStmt,
    T_RenameStmt,
    T_RuleStmt,
    T_NotifyStmt,
    T_ListenStmt,
    T_UnlistenStmt,
    T_TransactionStmt,
    T_ViewStmt,
    T_LoadStmt,
    T_CreateDomainStmt,
    T_CreatedbStmt,
    T_DropdbStmt,
    T_VacuumStmt,
    T_ExplainStmt,
    T_CreateTableAsStmt,
    T_CreateSeqStmt,
    T_AlterSeqStmt,
    T_VariableSetStmt,
    T_VariableShowStmt,
    T_DiscardStmt,
    T_CreateTrigStmt,
    T_CreatePLangStmt,
    T_CreateRoleStmt,
    T_AlterRoleStmt,
    T_DropRoleStmt,
    T_LockStmt,
    T_ConstraintsSetStmt,
    T_ReindexStmt,
    T_CheckPointStmt,
    T_CreateSchemaStmt,
    T_AlterDatabaseStmt,
    /** @since Postgres 15 */
    T_AlterDatabaseRefreshCollStmt,
    T_AlterDatabaseSetStmt,
    T_AlterRoleSetStmt,
    T_CreateConversionStmt,
    T_CreateCastStmt,
    T_CreateOpClassStmt,
    T_CreateOpFamilyStmt,
    T_AlterOpFamilyStmt,
    T_PrepareStmt,
    T_ExecuteStmt,
    T_DeallocateStmt,
    T_DeclareCursorStmt,
    T_CreateTableSpaceStmt,
    T_DropTableSpaceStmt,
    T_AlterObjectDependsStmt,
    T_AlterObjectSchemaStmt,
    T_AlterOwnerStmt,
    T_AlterOperatorStmt,
    /** @since 8.0 - Postgres vesion 13 */
    T_AlterTypeStmt,
    T_DropOwnedStmt,
    T_ReassignOwnedStmt,
    T_CompositeTypeStmt,
    T_CreateEnumStmt,
    T_CreateRangeStmt,
    T_AlterEnumStmt,
    T_AlterTSDictionaryStmt,
    T_AlterTSConfigurationStmt,
    T_CreateFdwStmt,
    T_AlterFdwStmt,
    T_CreateForeignServerStmt,
    T_AlterForeignServerStmt,
    T_CreateUserMappingStmt,
    T_AlterUserMappingStmt,
    T_DropUserMappingStmt,
    T_AlterTableSpaceOptionsStmt,
    T_AlterTableMoveAllStmt,
    T_SecLabelStmt,
    T_CreateForeignTableStmt,
    /** @since 6.0 - Postgres version 11 */
    T_ImportForeignSchemaStmt,
    T_CreateExtensionStmt,
    T_AlterExtensionStmt,
    T_AlterExtensionContentsStmt,
    T_CreateEventTrigStmt,
    T_AlterEventTrigStmt,
    T_RefreshMatViewStmt,
    T_ReplicaIdentityStmt,
    T_AlterSystemStmt,
    T_CreatePolicyStmt,
    T_AlterPolicyStmt,
    T_CreateTransformStmt,
    T_CreateAmStmt,
    /** @since 5.0 */
    T_CreatePublicationStmt,
    /** @since 5.0 */
    T_AlterPublicationStmt,
    /** @since 5.0 */
    T_CreateSubscriptionStmt,
    /** @since 5.0 */
    T_AlterSubscriptionStmt,
    /** @since 5.0 */
    T_DropSubscriptionStmt,
    /** @since 5.0 */
    T_CreateStatsStmt,
    /** @since 5.0 */
    T_AlterCollationStmt,
    /** @since 6.0 - Postgres version 11 */
    T_CallStmt,
    /** @since 8.0 - Postgres version 13 */
    T_AlterStatsStmt,

    /*
     * TAGS FOR PARSE TREE NODES (parsenodes.h)
     */
    T_A_Expr,
    T_ColumnRef,
    T_ParamRef,
    T_A_Const,
    T_FuncCall,
    T_A_Star,
    T_A_Indices,
    T_A_Indirection,
    T_A_ArrayExpr,
    T_ResTarget,
    /** @since 3.0 */
    T_MultiAssignRef,
    T_TypeCast,
    T_CollateClause,
    T_SortBy,
    T_WindowDef,
    T_RangeSubselect,
    T_RangeFunction,
    /** @since 6.0 - Postgres version 11 */
    T_RangeTableSample,
    /** @since 5.0 */
    T_RangeTableFunc,
    /** @since 5.0 */
    T_RangeTableFuncCol,
    T_TypeName,
    T_ColumnDef,
    T_IndexElem,
    /** @since 14.0 */
    T_StatsElem,
    T_Constraint,
    T_DefElem,
    T_RangeTblEntry,
    /** @since 6.0 - Postgres version 11 */
    T_RangeTblFunction,
    /** @since 6.0 - Postgres version 11 */
    T_TableSampleClause,
    /** @since 6.0 - Postgres version 11 */
    T_WithCheckOption,
    T_SortGroupClause,
    /** @since 6.0 - Postgres version 11 */
    T_GroupingSet,
    T_WindowClause,
    /** @since 5.0 */
    T_ObjectWithArgs,
    /** @deprecated Not in use any more since version 5.0 (Posgres version 10) */
    @Deprecated
    T_PrivGrantee,
    /**
     * @deprecated Not in use any more since version 5.0 (Posgres version 10). Probably replaced by
     *             {@link #T_ObjectWithArgs}
     */
    @Deprecated
    T_FuncWithArgs,
    T_AccessPriv,
    T_CreateOpClassItem,
    T_TableLikeClause,
    T_FunctionParameter,
    T_LockingClause,
    T_RowMarkClause,
    T_XmlSerialize,
    T_WithClause,
    T_InferClause,
    T_OnConflictClause,
    /** @since 14.0 */
    T_CTESearchClause,
    /** @since 14.0 */
    T_CTECycleClause,
    T_CommonTableExpr,
    /** @since Postgres 15 */
    T_MergeWhenClause,
    T_RoleSpec,
    /** @since 5.0 */
    T_TriggerTransition,
    /** @since 5.0 */
    T_PartitionElem,
    /** @since 5.0 */
    T_PartitionSpec,
    /** @since 5.0 */
    T_PartitionBoundSpec,
    /** @since 5.0 */
    T_PartitionRangeDatum,
    /** @since 5.0 */
    T_PartitionCmd,
    /** @since 6.0 - Postgres version 11 */
    T_VacuumRelation,
    /** @since Postgres 15 */
    T_PublicationObjSpec,
    /** @since Postgres 15 */
    T_JsonObjectConstructor,
    /** @since Postgres 15 */
    T_JsonArrayConstructor,
    /** @since Postgres 15 */
    T_JsonArrayQueryConstructor,
    /** @since Postgres 15 */
    T_JsonAggConstructor,
    /** @since Postgres 15 */
    T_JsonObjectAgg,
    /** @since Postgres 15 */
    T_JsonArrayAgg,
    /** @since Postgres 15 */
    T_JsonFuncExpr,
    /** @since Postgres 15 */
    T_PublicationTable,
    /** @since Postgres 15 */
    T_JsonIsPredicate,
    /** @since Postgres 15 */
    T_JsonTable,
    /** @since Postgres 15 */
    T_JsonTableColumn,
    /** @since Postgres 15 */
    T_JsonTablePlan,
    /** @since Postgres 15 */
    T_JsonCommon,
    /** @since Postgres 15 */
    T_JsonArgument,
    /** @since Postgres 15 */
    T_JsonKeyValue,
    /** @since Postgres 15 */
    T_JsonBehavior,
    /** @since Postgres 15 */
    T_JsonOutput,

    /*
     * TAGS FOR REPLICATION GRAMMAR PARSE NODES (replnodes.h)
     */
    T_IdentifySystemCmd,
    T_BaseBackupCmd,
    /** @since 6.0 - Postgres version 11 */
    T_CreateReplicationSlotCmd,
    /** @since 6.0 - Postgres version 11 */
    T_DropReplicationSlotCmd,
    T_StartReplicationCmd,
    T_TimeLineHistoryCmd,
    /** @since 6.0 - Postgres version 11 */
    T_SQLCmd,

    /*
     * TAGS FOR RANDOM OTHER STUFF
     *
     * These are objects that aren't part of parse/plan/execute node tree structures, but we give them NodeTags anyway
     * for identification purposes (usually because they are involved in APIs where we want to pass multiple object
     * types through the same pointer).
     */
    /** in commands/trigger.h */
    T_TriggerData,
    /** in commands/event_trigger.h */
    T_EventTriggerData,
    /** in nodes/execnodes.h */
    T_ReturnSetInfo,
    /** private in nodeWindowAgg.c */
    T_WindowObjectData,
    /** in nodes/tidbitmap.h */
    T_TIDBitmap,
    /** in nodes/parsenodes.h */
    T_InlineCodeBlock,
    /** in foreign/fdwapi.h */
    T_FdwRoutine,
    /**
     * in access/amapi.h
     * 
     * @since 6.0 - Postgres version 11
     */
    T_IndexAmRoutine,
    /**
     * in access/tsmapi.h
     * 
     * @since 6.0 - Postgres version 11
     */
    T_TsmRoutine,
    /**
     * in utils/rel.h
     * 
     * @since 6.0 - Postgres version 11
     */
    T_ForeignKeyCacheInfo,
    /**
     * in nodes/parsenodes.h
     * 
     * @since 6.0 - Postgres version 11
     */
    T_CallContext;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART = "T_\\p{Upper}\\w*";

}
