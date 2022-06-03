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

package com.splendiddata.sqlparser.grammartojava;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.splendiddata.sqlparser.enums.A_Expr_Kind;
import com.splendiddata.sqlparser.enums.AlterPublicationAction;
import com.splendiddata.sqlparser.enums.AlterSubscriptionType;
import com.splendiddata.sqlparser.enums.AlterTSConfigType;
import com.splendiddata.sqlparser.enums.AlterTableType;
import com.splendiddata.sqlparser.enums.AttributeIdentity;
import com.splendiddata.sqlparser.enums.BoolExprType;
import com.splendiddata.sqlparser.enums.BoolTestType;
import com.splendiddata.sqlparser.enums.CTEMaterialize;
import com.splendiddata.sqlparser.enums.CmdType;
import com.splendiddata.sqlparser.enums.CoercionForm;
import com.splendiddata.sqlparser.enums.ConstrType;
import com.splendiddata.sqlparser.enums.DefElemAction;
import com.splendiddata.sqlparser.enums.DiscardMode;
import com.splendiddata.sqlparser.enums.DropBehavior;
import com.splendiddata.sqlparser.enums.FetchDirection;
import com.splendiddata.sqlparser.enums.FkConstrMatch;
import com.splendiddata.sqlparser.enums.FrameOption;
import com.splendiddata.sqlparser.enums.GrantTargetType;
import com.splendiddata.sqlparser.enums.GroupingSetKind;
import com.splendiddata.sqlparser.enums.ImportForeignSchemaType;
import com.splendiddata.sqlparser.enums.JoinType;
import com.splendiddata.sqlparser.enums.JsonBehaviorType;
import com.splendiddata.sqlparser.enums.JsonEncoding;
import com.splendiddata.sqlparser.enums.JsonExprOp;
import com.splendiddata.sqlparser.enums.JsonFormatType;
import com.splendiddata.sqlparser.enums.JsonQuotes;
import com.splendiddata.sqlparser.enums.JsonTableColumnType;
import com.splendiddata.sqlparser.enums.JsonTablePlanJoinType;
import com.splendiddata.sqlparser.enums.JsonTablePlanType;
import com.splendiddata.sqlparser.enums.JsonValueType;
import com.splendiddata.sqlparser.enums.JsonWrapper;
import com.splendiddata.sqlparser.enums.LimitOption;
import com.splendiddata.sqlparser.enums.LockClauseStrength;
import com.splendiddata.sqlparser.enums.LockWaitPolicy;
import com.splendiddata.sqlparser.enums.MinMaxOp;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.NullTestType;
import com.splendiddata.sqlparser.enums.ObjectType;
import com.splendiddata.sqlparser.enums.OnCommitAction;
import com.splendiddata.sqlparser.enums.OnConflictAction;
import com.splendiddata.sqlparser.enums.OverridingKind;
import com.splendiddata.sqlparser.enums.PartitionRangeDatumKind;
import com.splendiddata.sqlparser.enums.PartitionStrategy;
import com.splendiddata.sqlparser.enums.PublicationObjSpecType;
import com.splendiddata.sqlparser.enums.ReindexObjectType;
import com.splendiddata.sqlparser.enums.RelPersistence;
import com.splendiddata.sqlparser.enums.ReplicaIdentityType;
import com.splendiddata.sqlparser.enums.RoleSpecType;
import com.splendiddata.sqlparser.enums.RoleStmtType;
import com.splendiddata.sqlparser.enums.SQLValueFunctionOp;
import com.splendiddata.sqlparser.enums.SetOperation;
import com.splendiddata.sqlparser.enums.SetQuantifier;
import com.splendiddata.sqlparser.enums.Severity;
import com.splendiddata.sqlparser.enums.SortByDir;
import com.splendiddata.sqlparser.enums.SortByNulls;
import com.splendiddata.sqlparser.enums.SubLinkType;
import com.splendiddata.sqlparser.enums.TemporalWord;
import com.splendiddata.sqlparser.enums.TransactionStmtKind;
import com.splendiddata.sqlparser.enums.VariableSetKind;
import com.splendiddata.sqlparser.enums.ViewCheckOption;
import com.splendiddata.sqlparser.enums.XmlExprOp;
import com.splendiddata.sqlparser.enums.XmlOptionType;
import com.splendiddata.sqlparser.enums.XmlStandaloneType;

/**
 * Converts the original grammar (/postgresql-9.3.4/src/backend/parser/gram.y), which was intended to generate C code,
 * to a version that generates a grammar that will generate Java-like code. The generated code must be converted again
 * by {@link com.splendiddata.sqlparser.grammartojava.JavaParserConverter} to get something compilable.
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@Mojo(name = "convertGrammar", executionStrategy = "always", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
@Execute(goal = "convertGrammar", phase = LifecyclePhase.GENERATE_SOURCES)
public class GrammarConverter extends AbstractMojo implements FileVisitor<Path> {
    private Log log;

    /**
     * Where are we in the grammar file
     */
    private GrammarPhase phase;

    /**
     * Special processing for the current grammar rule. Most rules will be DEFAULT.
     */
    private GrammarRuleSpecial specialProcessing = GrammarRuleSpecial.DEFAULT;

    /**
     * Pattern to recognise the start of a grammar rule. The name of the rule will be used to lookup a specialProcessing
     * value.
     */
    private static final Pattern GRAMMAR_RULE_START_PATTERN = Pattern.compile("\\s*(\\w*)\\s*(:.*)");

    /**
     * Group number for the name of a rule in the {@link #GRAMMAR_RULE_START_PATTERN}
     */
    private static final int RULE_NAME_GROUP = 1;

    /**
     * Group number in the {@link #GRAMMAR_RULE_START_PATTERN} of text after the rule name, that is to be copied as is
     * into the converted grammar file
     */
    private static final int RULE_NAME_REMAINING_TEXT_GROUP = 2;

    /**
     * Pattern to identify and extract the return type of an epilog function, for example "static Node *" or "static
     * void".
     */
    private static final Pattern EPILOG_RETURN_TYPE_PATTERN = Pattern.compile("^(?:static\\s+)?(\\w+)\\s*\\*?\\s*$");

    /**
     * A pattern to identify the function name in a function definition in the epilog section
     */
    private static final Pattern EPILOG_FUNCTION_DEFINITION_PATTERN = Pattern.compile("^(\\w+)\\(.*");

    /**
     * Pattern to interpret the first line of the bison --version response
     */
    private static final Pattern BISON_VERSION_PATTERN = Pattern.compile("bison \\(GNU Bison\\) ([\\d.]+).*");
    /**
     * What Bison version are we working with?
     */
    static BisonVersion bisonVersion;

    /**
     * Are we in some special block?
     */
    private SpecialGrammarBlock specialBlock = SpecialGrammarBlock.NO;

    /**
     * The return type of the current function in the epilog section
     */
    private String epilogFunctionReturnType;

    /**
     * The name of the current function in the epilog section
     */
    private String epilogFunctionName = "";

    /**
     * The package name to be provided to the grammar file so that Bison can specify a package clause in the generated
     * Java source
     */
    @Parameter(property = "packageName", defaultValue = "com.spendiddata.sqlparser")
    private String packageName;

    /**
     * Where to find the input grammar. The source directory will be scanned for it.
     */
    @Parameter(property = "sourceDir", defaultValue = "src/main/postgres")
    private String sourceDir;

    /**
     * The directory under which the convert grammar will be stored. The directory structure of the generated file will
     * be the same as the source file under sourceDir. Needless to say of course that sourceDir and targetDir must not
     * be the same.
     */
    @Parameter(property = "targetDir", defaultValue = "target/generated_sources/converted.grammar")
    private String targetDir;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        log = getLog();
        log.info(getClass().getName() + ".execute()");
        log.info("packageName =\"" + packageName + "\"");
        log.info("sourceDir =\"" + sourceDir + "\"");
        log.info("targetDir =\"" + targetDir + "\"");

        if (sourceDir.equals(targetDir)) {
            throw new MojoFailureException("sourceDir and targetDir must be different");
        }

        try {
            Files.createDirectories(Paths.get(targetDir));
            Files.walkFileTree(Paths.get(sourceDir), this);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    /**
     * @see java.nio.file.FileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     *
     * @param file
     *            The file to visit
     * @param attrs
     *            File attributes
     * @return FileVisitResult.CONTINUE
     * @throws IOException
     *             From the reader
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        File outputFile = new File(targetDir, file.toFile().getName());
        if (outputFile.exists() && outputFile.lastModified() > file.toFile().lastModified()) {
            log.info("File " + file + " skipped: already processed");
            return FileVisitResult.CONTINUE;
        }

        log.info(getClass().getName() + ".execute()");
        log.info("packageName =\"" + packageName + "\"");
        log.info("sourceDir =\"" + sourceDir + "\"");
        log.info("targetDir =\"" + targetDir + "\"");
        determineBisionVersion();

        log.info("Process file " + file);

        phase = GrammarPhase.START;
        specialProcessing = GrammarRuleSpecial.DEFAULT;
        specialBlock = SpecialGrammarBlock.NO;

        try (BufferedReader in = Files.newBufferedReader(file, Charset.forName("UTF-8"));
                PrintWriter out = new PrintWriter(outputFile, "UTF-8")) {
            writeHeading(out);

            int bracketsRemoved = 0;

            for (String line = in.readLine(); line != null; line = in.readLine()) {
                if (log.isDebugEnabled()) {
                    log.debug("in: \"" + line + "\"");
                }
                switch (phase) {
                case START:
                    if (line.startsWith("%{")) {
                        log.debug("found %{: phase := IN_PROLOGUE, line skipped");
                        phase = GrammarPhase.IN_PROLOGUE;
                    }
                    continue;
                case IN_PROLOGUE:
                    if (line.startsWith("%}")) {
                        log.debug("found %}: phase := IN_DECLARATIONS, line skipped");
                        phase = GrammarPhase.IN_DECLARATIONS;
                    }
                    continue;
                case IN_DECLARATIONS:
                    if (bisonVersion.replacePrefixLine(line, out)) {
                        continue;
                    } else if (line.startsWith("%%")) {
                        log.debug("found %%: phase := IN_GRAMMAR_RULES");
                        phase = GrammarPhase.IN_GRAMMAR_RULES;
                    } else if (line.startsWith("%parse-param")) {
                        continue;
                    } else if (line.startsWith("%lex-param")) {
                        continue;
                    }
                    String convertedLine = line.replace("%type <node>", "%type <Node>")
                            .replaceAll("%type <ival>\\s+defacl_privilege_target",
                                    "%type <ObjectType> defacl_privilege_target")
                            .replaceAll("%type <ival>\\s+document_or_content",
                                    "%type <XmlOptionType> document_or_content")
                            .replaceAll("%type <ival>\\s+add_drop opt_asc_desc opt_nulls_order",
                                    "%type <Integer> add_drop\n%type <SortByDir> opt_asc_desc\n%type <SortByNulls> opt_nulls_order")
                            .replaceAll("%type <ival>\\s+sub_type", "%type <SubLinkType> sub_type")
                            .replaceAll("%type <ival>\\s+for_locking_strength",
                                    "%type <LockClauseStrength> for_locking_strength")
                            .replaceAll("%type <ival>\\s+OptNoLog", "%type <RelPersistence> OptNoLog")
                            .replaceAll("%type <ival>\\s+opt_column event cursor_options opt_hold opt_set_data",
                                    "%type <CmdType> event"
                                            + "\n%type <Integer>  opt_column cursor_options opt_hold opt_set_data")
                            .replaceAll("%type <ival>\\s+OptTemp", "%type <RelPersistence> OptTemp")
                            .replaceAll("%type <ival>\\s+generated_when override_kind",
                                    "%type <AttributeIdentity> generated_when\n%type <OverridingKind> override_kind")
                            .replace("%type <list>", "%type <List>").replace("%type <ival>", "%type <Integer>")
                            .replace("%type <defelt>", "%type <DefElem>")
                            .replace("%type <dbehavior>", "%type <DropBehavior>")
                            .replace("%type <str>", "%type <String>").replace("%type <value>", "%type <Value>")
                            .replace("%type <chr>", "%type <Character>").replace("%type <range>", "%type <RangeVar>")
                            .replace("%type <rolespec>", "%type <RoleSpec>")
                            .replace("%type <accesspriv>", "%type <AccessPriv>")
                            .replace("%type <privtarget>", "%type <PrivTarget>")
                            .replace("%type <objwithargs>", "%type <ObjectWithArgs>")
                            .replace("%type <importqual>", "%type <ImportQual>")
                            .replace("%type <fun_param>", "%type <FunctionParameter>")
                            .replace("%type <fun_param_mode>", "%type <Character>")
                            .replace("%type <typnam>", "%type <TypeName>")
                            .replace("%type <oncommit>", "%type <OnCommitAction>")
                            .replace("%type <jtype>", "%type <JoinType>")
                            .replace("%type <objtype>", "%type <ObjectType>")
                            .replace("%type <istmt>", "%type <InsertStmt>")
                            .replace("%type <infer>", "%type <InferClause>")
                            .replace("%type <onconflict>", "%type <OnConflictClause>")
                            .replace("%type <vsetstmt>", "%type <VariableSetStmt>")
                            .replace("%type <sortby>", "%type <SortBy>").replace("%type <ielem>", "%type <IndexElem>")
                            .replace("%type <jexpr>", "%type <JoinExpr>").replace("%type <target>", "%type <ResTarget>")
                            .replace("%type <windef>", "%type <WindowDef>")
                            .replace("%type <partspec>", "%type <PartitionSpec>")
                            .replace("%type <partelem>", "%type <PartitionElem>")
                            .replace("%type <partboundspec>", "%type <PartitionBoundSpec>").replace("", "")
                            .replace("%type <keyword>", "%type <String>").replace("%token <str>", "%token <String>")
                            .replace("%token <ival>", "%token <Integer>").replace("%token <keyword>", "%token <String>")
                            .replace("%type <SubLinkType> sub_type opt_materialized",
                                    "%type <SubLinkType> sub_type\n%type <CTEMaterialize> opt_materialized")
                            .replace("%type <selectlimit>", "%type <SelectLimit>")
                            .replace("%type <selem>", "%type <StatsElem>")
                            .replace("%type <setquantifier>", "%type <SetQuantifier>")
                            .replace("%type <groupclause>", "%type <GroupClause>")
                            .replace("%type <alias>", "%type <Alias>").replace("%type <into>", "%type <IntoClause>")
                            .replace("%type <with>", "%type <WithClause>")
                            .replace("%type <keyaction>", "%type <KeyAction>")
                            .replace("%type <keyactions>", "%type <KeyActions>")
                            .replace("%type <mergewhen>", "%type <MergeWhenClause>")
                            .replace("%type <publicationobjectspec>", "%type <PublicationObjSpec>")
                            .replaceAll("%type\\s<Integer>\\s+key_match", "%type <FkConstrMatch> key_match")
                            .replaceAll("%type\\s<Integer>\\s+json_encoding", "%type <JsonEncoding> json_encoding")
                            .replaceAll("^\\s+json_table_default_plan_choices",
                                    "%type <Integer> json_table_default_plan_choices")
                            .replaceAll("^\\s+json_predicate_type_constraint_opt",
                                    "%type <JsonValueType> json_predicate_type_constraint_opt")
                            .replace("%type <on_behavior>", "%type <YYSTypeJsonOnBehaviour>")
                            .replace("%type <jsbehavior>", "%type <JsonBehavior>")
                            .replace("%type <js_quotes>", "%type <JsonQuotes>")
                            .replaceAll("^\\s+json_wrapper_clause_opt", "%type <JsonWrapper> json_wrapper_clause_opt")
                            .replaceAll("^\\s+json_conditional_or_unconditional_opt",
                                    "%type <Integer> json_conditional_or_unconditional_opt")

                    ;
                    /*
                     * declaration lines are written unchanged
                     */
                    out.println(convertedLine);
                    continue;
                default:
                    if (line.startsWith("%%")) {
                        log.debug("found %%: phase := IN_EPILOG");
                        phase = GrammarPhase.IN_EPILOGUE;
                        specialProcessing = GrammarRuleSpecial.IN_EPILOG;
                        out.println(line);
                        continue;
                    }
                }

                /*
                 * Replace some enumerated types
                 */
                String convertedLine = line
                        /*
                         * RoleSpecType from enum
                         */
                        .replaceAll("(makeRoleSpec\\(|= )(" + RoleSpecType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1RoleSpecType.$2$3")
                        /*
                         * StatementTypes from enum
                         */
                        .replaceAll("(\\W)(" + RoleStmtType.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1RoleStmtType.$2$3")
                        /*
                         * DiscardMode from enum
                         */
                        .replaceAll("(\\W)(" + DiscardMode.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1DiscardMode.$2$3")
                        /*
                         * GrantTargetType from enum
                         */
                        .replaceAll("(\\W)(" + GrantTargetType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1GrantTargetType.$2$3")
                        /*
                         * ObjectType from enum
                         */
                        .replaceAll("(\\W)(" + ObjectType.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1ObjectType.$2$3")
                        /*
                         * FetchDirection from enum
                         */
                        .replaceAll("(\\W)(" + FetchDirection.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1FetchDirection.$2$3")
                        /*
                         * LockClauseStrength from enum
                         */
                        .replaceAll("(\\W)(" + LockClauseStrength.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1LockClauseStrength.$2$3")
                        /*
                         * ConstrType from enum
                         */
                        .replaceAll("(\\W)(" + ConstrType.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1ConstrType.$2$3")
                        /*
                         * AlterTableType from enum
                         */
                        .replaceAll("(\\W)(" + AlterTableType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1AlterTableType.$2$3")
                        /*
                         * TemporalWord from enum
                         */
                        .replaceAll("(\\W)(" + TemporalWord.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1TemporalWord.$2$3")
                        /*
                         * VariableSetKind from enum
                         */
                        .replaceAll("(\\W)(" + VariableSetKind.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1VariableSetKind.$2$3")
                        /*
                         * TransactionStmtKind from enum
                         */
                        .replaceAll("(\\W)(" + TransactionStmtKind.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1TransactionStmtKind.$2$3")
                        /*
                         * A_Expr_Kind from enum
                         */
                        .replaceAll("(\\W)(" + A_Expr_Kind.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1A_Expr_Kind.$2$3")
                        /*
                         * SortByType from enum
                         */
                        .replaceAll("(\\W)(" + SortByDir.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1SortByDir.$2$3")
                        /*
                         * SortByNulls from enum
                         */
                        .replaceAll("(\\W)(" + SortByNulls.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1SortByNulls.$2$3")
                        /*
                         * BoolTestType from enum
                         */
                        .replaceAll("(\\W)(" + BoolTestType.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1BoolTestType.$2$3")
                        /*
                         * MinMaxOp from enum
                         */
                        .replaceAll("(\\W)(" + MinMaxOp.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1MinMaxOp.$2$3")
                        /*
                         * JoinType from enum
                         */
                        .replaceAll("(\\W)(" + JoinType.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1JoinType.$2$3")
                        /*
                         * NullTestType from enum
                         */
                        .replaceAll("(\\W)(" + NullTestType.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1NullTestType.$2$3")
                        /*
                         * CmdType from enum
                         */
                        .replaceAll("(\\W)(" + CmdType.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1CmdType.$2$3")
                        /*
                         * SubLinkType from enum
                         */
                        .replaceAll("(\\W)(" + SubLinkType.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1SubLinkType.$2$3")
                        /*
                         * SetOperation from enum
                         */
                        .replaceAll("(\\W)(" + SetOperation.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1SetOperation.$2$3")
                        /*
                         * XmlExprOp from enum
                         */
                        .replaceAll("(\\W)(" + XmlExprOp.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1XmlExprOp.$2$3")
                        /*
                         * XmlOptionType from enum
                         */
                        .replaceAll("(\\W)(" + XmlOptionType.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1XmlOptionType.$2$3")
                        /*
                         * Severity from enum
                         */
                        .replaceAll("(\\W)(" + Severity.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1Severity.$2$3")
                        /*
                         * NodeTag from enum
                         */
                        .replaceAll("(\\W)(" + NodeTag.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1NodeTag.$2$3")
                        /*
                         * OnCommitAction from enum
                         */
                        .replaceAll("(\\W)(" + OnCommitAction.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1OnCommitAction.$2$3")
                        /*
                         * DefElemAction from enum
                         */
                        .replaceAll("(\\W)(" + DefElemAction.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1DefElemAction.$2$3")
                        /*
                         * XmlStandaloneType from enum
                         */
                        .replaceAll("(\\W)(" + XmlStandaloneType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1XmlStandaloneType.$2$3")
                        /*
                         * DropBehavior from enum
                         */
                        .replaceAll("(\\W)(" + DropBehavior.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1DropBehavior.$2$3")
                        /*
                         * ReplicaIdentityType from enum
                         */
                        .replaceAll("(\\W)(" + ReplicaIdentityType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1ReplicaIdentityType.$2$3")
                        /*
                         * ViewCheckOption from enum
                         */
                        .replaceAll("(\\W)(" + ViewCheckOption.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1ViewCheckOption.$2$3")
                        /*
                         * ImportForeignSchemaType from enum
                         */
                        .replaceAll("(\\W)(" + ImportForeignSchemaType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1ImportForeignSchemaType.$2$3")
                        /*
                         * ReindexObjectType from enum
                         */
                        .replaceAll("(\\W)(" + ReindexObjectType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1ReindexObjectType.$2$3")
                        /*
                         * AlterTSConfigType from enum
                         */
                        .replaceAll("(\\W)(" + AlterTSConfigType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1AlterTSConfigType.$2$3")
                        /*
                         * OnConflictAction from enum
                         */
                        .replaceAll("(\\W)(" + OnConflictAction.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1OnConflictAction.$2$3")
                        /*
                         * GroupingSetKind from enum
                         */
                        .replaceAll("(\\W)(" + GroupingSetKind.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1GroupingSetKind.$2$3")
                        /*
                         * LockWaitPolicy from enum
                         */
                        .replaceAll("(\\W)(" + LockWaitPolicy.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1LockWaitPolicy.$2$3")
                        /*
                         * BoolExprType from enum
                         */
                        .replaceAll("(\\W)(" + BoolExprType.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1BoolExprType.$2$3")
                        /*
                         * CoercionForm from enum
                         */
                        .replaceAll("(\\W)(" + CoercionForm.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1CoercionForm.$2$3")
                        /*
                         * PartitionRangeDatumKind from enum.
                         */
                        .replaceAll("(\\W)(" + PartitionRangeDatumKind.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1PartitionRangeDatumKind.$2$3")
                        /*
                         * AlterSubscriptionType from enum.
                         */
                        .replaceAll("(\\W)(" + AlterSubscriptionType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1AlterSubscriptionType.$2$3")
                        /*
                         * SQLValueFunctionOp from enum.
                         */
                        .replaceAll("(\\W)(" + SQLValueFunctionOp.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1SQLValueFunctionOp.$2$3")
                        /*
                         * RelPersistence from enum.
                         */
                        .replaceAll("(\\W)(" + RelPersistence.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1RelPersistence.$2$3")
                        /*
                         * AttributeIdentity from enum.
                         */
                        .replaceAll("(\\W)(" + AttributeIdentity.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1AttributeIdentity.$2$3")
                        /*
                         * FkConstrMatch from enum.
                         */
                        .replaceAll("(\\W)(" + FkConstrMatch.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1FkConstrMatch.$2$3")
                        /*
                         * PartitionStrategy from enum.
                         */
                        .replaceAll("(\\W)(" + PartitionStrategy.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1PartitionStrategy.$2$3")
                        /*
                         * FrameOption from "enum".
                         */
                        .replaceAll("(\\W)(" + FrameOption.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1FrameOption.$2$3")
                        /*
                         * CTEMaterialize from "enum".
                         */
                        .replaceAll("(\\W)(" + CTEMaterialize.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1CTEMaterialize.$2$3")
                        /*
                         * LimitOption from "enum".
                         */
                        .replaceAll("(\\W)(" + LimitOption.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1LimitOption.$2$3")
                        /*
                         * SetQuantifier from "enum".
                         */
                        .replaceAll("(\\W)(" + SetQuantifier.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1SetQuantifier.$2$3")
                        /*
                         * SetQuantifier from "enum".
                         */
                        .replaceAll("(\\W)(" + OverridingKind.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1OverridingKind.$2$3")
                        /*
                         * JsonWrapper from "enum".
                         */
                        .replaceAll("(\\W)(" + JsonWrapper.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1JsonWrapper.$2$3")
                        /*
                         * JsonQuotes from "enum".
                         */
                        .replaceAll("(\\W)(" + JsonQuotes.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1JsonQuotes.$2$3")
                        /*
                         * JsonTableColumnType from "enum".
                         */
                        .replaceAll("(\\W)(" + JsonTableColumnType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1JsonTableColumnType.$2$3")
                        /*
                         * JsonFormatType from "enum".
                         */
                        .replaceAll("(\\W)(" + JsonFormatType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1JsonFormatType.$2$3")
                        /*
                         * JsonEncoding from "enum".
                         */
                        .replaceAll("(\\W)(" + JsonEncoding.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1JsonEncoding.$2$3")
                        /*
                         * JsonTablePlanType from "enum".
                         */
                        .replaceAll("(\\W)(" + JsonTablePlanType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1JsonTablePlanType.$2$3")
                        /*
                         * JsonTablePlanJoinType from "enum".
                         */
                        .replaceAll("(\\W)(" + JsonTablePlanJoinType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1JsonTablePlanJoinType.$2$3")
                        /*
                         * PublicationObjSpecType from "enum".
                         */
                        .replaceAll("(\\W)(" + PublicationObjSpecType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1PublicationObjSpecType.$2$3")
                        /*
                         * AlterPublicationAction from "enum".
                         */
                        .replaceAll("(\\W)(" + AlterPublicationAction.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1AlterPublicationAction.$2$3")
                        /*
                         * JsonBehaviorType from "enum".
                         */
                        .replaceAll("(\\W)(" + JsonBehaviorType.REPLACEMENT_REGEXP_PART + ")(\\W)",
                                "$1JsonBehaviorType.$2$3")
                        /*
                         * JsonValueType from "enum".
                         */
                        .replaceAll("(\\W)(" + JsonValueType.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1JsonValueType.$2$3")
                        /*
                         * JsonExprOp from "enum".
                         */
                        .replaceAll("(\\W)(" + JsonExprOp.REPLACEMENT_REGEXP_PART + ")(\\W)", "$1JsonExprOp.$2$3");
                /*
                 * Replace some constants that are defined in the type that uses them
                 */
                convertedLine = convertedLine
                        .replaceAll("(?<=\\W)(CREATE_TABLE_LIKE_\\w+)(?=\\W)", "TableLikeClause.$1")
                        .replaceAll("(?<=\\W)(FKCONSTR_ACTION_\\w+)(?=\\W)", "Constraint.$1")
                        .replaceAll("(?<=\\W)(CLUOPT_\\w+)(?=\\W)", "ClusterStmt.$1")
                        .replaceAll("(?<=\\W)(AMTYPE_\\w+)(?=\\W)", "CreateAmStmt.$1");

                /*
                 * And a few common replacements
                 */
                convertedLine = convertedLine
                        /*
                         * "castNode(SomeClass," to "castNode(SomeClass.class,"
                         */
                        .replaceAll("(\\WcastNode\\(\\w+),", "$1.class,")
                        /*
                         * bool is called boolean in Java
                         */
                        .replaceAll("(\\W)bool\\s+", "$1boolean ");

                if (GrammarPhase.IN_EPILOGUE.equals(phase)) {
                    convertedLine = performEpilogProcessing(convertedLine);
                } else {
                    /*
                     * Some rules are implemented in a very C-like way. These need to be treated specially to generate
                     * more or less readable java code. Here the rules are identified.
                     */
                    Matcher matcher = GRAMMAR_RULE_START_PATTERN.matcher(convertedLine);
                    if (matcher.matches()) {
                        String ruleName = matcher.group(RULE_NAME_GROUP);
                        convertedLine = ruleName + matcher.group(RULE_NAME_REMAINING_TEXT_GROUP);
                        try {
                            specialProcessing = GrammarRuleSpecial.valueOf(ruleName);
                            if (log.isDebugEnabled()) {
                                log.debug("Special processing enabled for " + ruleName);
                            }
                        } catch (IllegalArgumentException e) {
                            if (log.isDebugEnabled()) {
                                log.debug(ruleName + " not found in GrammarRuleSpecial, DEFAULT assumed");
                            }
                            specialProcessing = GrammarRuleSpecial.DEFAULT;
                        }
                    }
                }
                if (convertedLine != null) {
                    convertedLine = specialProcessing.processLine(convertedLine);
                }

                if (convertedLine != null) {
                    if (SpecialGrammarBlock.IN_EREPORT.equals(specialBlock)) {
                        if (convertedLine.matches("\\s*\\(errcode.*")) {
                            convertedLine = convertedLine.replace("(errcode(", " ErrCode.").replace(")", "");
                            bracketsRemoved++;
                        } else if (convertedLine.matches("\\s*errcode.*")) {
                            convertedLine = convertedLine.replace("errcode(", " ErrCode.").replace(")", "");
                        } else if (convertedLine.matches("\\s*\\(errmsg.*")) {
                            convertedLine = convertedLine.replace("(errmsg", " errmsg");
                            bracketsRemoved++;
                        }
                        if (convertedLine.matches(".*\\);.*")) {
                            convertedLine = convertedLine.replaceAll("\\){" + bracketsRemoved + "};", ";");
                            specialBlock = SpecialGrammarBlock.NO;
                        }
                    } else if (SpecialGrammarBlock.IN_YYVAL_DIRECT.equals(specialBlock)) {
                        if (convertedLine.matches(".*?}.*")) {
                            specialBlock = SpecialGrammarBlock.NO;
                            out.println(convertedLine);
                            log.debug("back to normal");
                            continue;
                        }
                        convertedLine = convertedLine.replace("$$", "nd");
                    } else if (convertedLine.matches("\\s*\\$\\$\\s*=\\s*makeNode.*")) {
                        specialBlock = SpecialGrammarBlock.IN_YYVAL_DIRECT;
                        log.debug("direct reference of yyval, pase := IN_YYVAL_DIRECT");
                        convertedLine = convertedLine.replaceAll("(\\s*)(\\$\\$\\s*=\\s*makeNode\\s*\\(\\s*)(\\w*).*",
                                "$1$3 nd = makeNode($3.class);\r\n$1\\$\\$ = nd;");
                        out.println(convertedLine);
                        continue;
                    } else if (convertedLine.matches("\\s*ereport\\(.*")) {
                        log.debug("changed to: " + convertedLine + ", phase := IN_EREPORT");
                        specialBlock = SpecialGrammarBlock.IN_EREPORT;
                        bracketsRemoved = 0;
                    } else if (convertedLine.matches(".*?palloc.*")) {
                        convertedLine = convertedLine.replaceAll("(\\s*)(\\w*)(\\s*\\*\\s*)(\\w*).*",
                                "$1$2 $4 = new $2();");
                        log.debug("changed to: " + convertedLine);
                        out.println(convertedLine);
                        continue;
                    }
                }
                if (convertedLine != null) {
                    /*
                     * Change the c "->" pointer constructs to "."
                     */
                    convertedLine = convertedLine.replace("->", ".")
                            /*
                             * But i->yystack.valueAt(i) is intended to be a closure, so repair that. 
                             */
                            .replaceAll("(\\w+)\\.yystack\\.valueAt\\(\\1\\)", "$1->yystack.valueAt($1)")
                            /*
                             * Replace all char* by String
                             */
                            .replaceAll("char\\s*\\*", "String ")
                            /*
                             * alter the C by reference passing by Java style argument passing
                             */
                            .replaceAll("(\\,\\s*|^\\s*)\\&", "$1")
                            /*
                             * Change the c (Foo *) pointer casts to class casts : (Foo)
                             */
                            .replaceAll("(\\(\\s*)(\\w*)(\\s*\\*\\s*\\))", "($2)")
                            /*
                             * Remove the rest of the c pointer constructs: Foo *bar becomes Foo bar (and let's hope no
                             * multiplications are used anywhere.
                             */
                            .replaceAll("(\\w+)(\\s*\\*\\s*)(\\w+)", "$1 $3")
                            /*
                             * Array declaration Example char buf [64] becomes char[] buf = new char[64]
                             */
                            .replaceAll("(char)(\\s*)(\\w*)\\[(\\d*)\\]", "$1[]$2$3 = new $1[$4]")
                            /*
                             * Add ".class" in the class argument of makeNode
                             */
                            .replaceAll("(makeNode\\()(\\w*)\\)", "makeNode($2.class)")
                            /*
                             * Replace sprintf invocation by String.format(...
                             */
                            .replaceAll("(\\s*)(sprintf\\s*\\(\\s*)(\\w+|\\$\\$)(\\s*\\,\\s*)(\".*?\")\\s*\\,\\s*",
                                    "$1$3 = String.format($5, ")
                            /*
                             * Replace psprintf invocation by String.format(...
                             */
                            .replaceAll("(\\s*)psprintf\\s*\\(\\s*", "$1String.format(")
                            /*
                             * Replace cpy invocation by String.format(...
                             */
                            .replaceAll("(strcpy\\s*\\(\\s*)(\\w+|\\$\\$|\\$\\d*)(\\s*,\\s*)(.*?)\\)\\s*;",
                                    "$2 = \"\" + $4;")
                            /*
                             * Replace sprintf invocation by String.format(...
                             */
                            .replaceAll("(strcat\\s*\\(\\s*)(\\w+|\\$\\$|\\$\\d*)(\\s*,\\s*)(.*?)\\)\\s*;",
                                    "$2 = $2 + $4;")
                            /*
                             * Replace snprintf invocation by String.format(...
                             */
                            .replaceAll(
                                    "(\\s*)(snprintf\\s*\\(\\s*)(\\w+|\\$\\$)(\\s*\\,\\s*)(\\S*)(\\s*\\,\\s*)(\".*?\")(\\s*\\,.*?)(\\)\\s*;)",
                                    "$1$3 = String.format($7$8).toCharArray();")
                            /*
                             * Alter IsA(value, type) into isA(
                             */
                            .replaceAll("(IsA\\()(.*?)(\\,\\s*)(\\w*)(\\s*\\))",
                                    "isA($2, " + NodeTag.class.getSimpleName() + ".T_$4)")
                            /*
                             * Change NULL to null
                             */
                            .replaceAll("(\\W)(NULL)(\\W)", "$1null$3")
                            /*
                             * In Java pointers and integers are different things. So the location cannot be < 0.
                             */
                            .replace("if (@$ < 0)", "if (@$ == null)")
                            /*
                             * A pointer is not an integer in Java. So don't put -1 in location, but a null pointer
                             * instead.
                             */
                            .replaceAll("location\\s*=\\s*-1", "location = null")
                            /*
                             * macro expansion, obtained from /postgresql-9.3.4/src/include/nodes/pg_list.h
                             */
                            .replaceAll("(forboth\\(\\s*)([^,]*),([^,]*),([^,]*),([^)]*)\\)",
                                    "for ($2 = list_head($3), $4 = list_head($5); $2 != NULL && $4 != NULL; $2 = lnext($2), $4 = lnext($4))")
                            /*
                             * macro expansion, obtained from /postgresql-9.3.4/src/include/nodes/pg_list.h
                             */
                            .replaceAll("(foreach\\s*\\(\\s*)([^,]*),([^)]*)\\)",
                                    "for ($2 = list_head($3); $2 != NULL; $2 = lnext($2))")
                            /*
                             * Argument passing by reference does not work in Java. So translate foo(&bar) to foo(var).
                             */
                            .replaceAll("(\\(|,\\s?)\\s*\\&(\\w+)", "$1$2")
                            /*
                             * Cast needed
                             */
                            .replace("doNegateFloat($$);", " doNegateFloat((Value)$$);")
                            /*
                             * The CoercionContext is passed as integer. This is legal in C, but not in Java
                             */
                            .replaceAll("\\(CoercionContext\\)\\s*(\\$\\d+)", "CoercionContext.values()[$1]")
                            /*
                             * Replace location -1 in makeStringConstCast with null
                             */
                            .replaceAll("(makeStringConstCast\\s*\\(.*?\\,\\s*)-1", "$1null")
                            /*
                             * Replace location -1 in makeBoolAConst with null
                             */
                            .replaceAll("(makeBoolAConst\\s*\\(.*?\\,\\s*)-1", "$1null")

                            .replaceAll("(\\W)int32(\\W)", "$1int$2").replace("(int16)", "(int)")
                            /*
                             * makeRawStmt(whatever, 0) becomes makeRawStatement(whatever, null)
                             */
                            .replaceAll("makeRawStmt\\(([^\\,]+)\\,\\s*0\\)", "makeRawStmt($1, null)");
                }
                if (log.isDebugEnabled()) {
                    if (convertedLine == null) {
                        log.debug("removed");
                    } else if (line.equals(convertedLine)) {
                        log.debug("write unchanged");
                    } else {
                        log.debug("changed to: " + convertedLine);
                    }
                }
                if (convertedLine != null) {
                    out.println(convertedLine);
                }
            }
        }
        log.debug("@<visitFile(file=" + file + ", attrs=" + attrs + ") = " + FileVisitResult.CONTINUE);
        return FileVisitResult.CONTINUE;
    }

    /**
     * TODO Please insert some explanation here
     *
     * @param input
     *            An input line in the epilog section
     * @return String The line as it should be
     */
    private String performEpilogProcessing(String input) {
        Matcher m = EPILOG_RETURN_TYPE_PATTERN.matcher(input);
        if (m.matches()) {
            epilogFunctionReturnType = m.group(1);
            // remove this line - the return type will be added in front of the function name one line later
            return null;
        }

        String convertedLine;
        m = EPILOG_FUNCTION_DEFINITION_PATTERN.matcher(input);
        if (m.matches()) {
            epilogFunctionName = m.group(1);
            switch (epilogFunctionName) {
            case "makeColumnRef":
                convertedLine = "Node " + input;
                break;
            case "makeTypeCast":
                convertedLine = "TypeCast " + input;
                break;
            case "makeStringConst":
                convertedLine = "A_Const " + input;
                break;
            case "makeStringConstCast":
                convertedLine = "TypeCast " + input;
                break;
            case "makeIntConst":
                convertedLine = "A_Const " + input;
                break;
            case "makeFloatConst":
                convertedLine = "A_Const " + input;
                break;
            case "makeBitStringConst":
                convertedLine = "A_Const " + input;
                break;
            case "makeNullAConst":
                convertedLine = "A_Const " + input;
                break;
            case "makeAConst":
                convertedLine = "A_Const " + input;
                break;
            case "makeBoolAConst":
                convertedLine = "A_Const " + input;
                break;
            case "makeSetOp":
                convertedLine = "SelectStmt " + input;
                break;
            case "makeAndExpr":
                convertedLine = "BoolExpr " + input;
                break;
            case "makeOrExpr":
                convertedLine = "BoolExpr " + input;
                break;
            case "makeNotExpr":
                convertedLine = "BoolExpr " + input;
                break;
            case "makeAArrayExpr":
                convertedLine = "A_ArrayExpr " + input;
                break;
            case "makeSQLValueFunction":
                convertedLine = "SQLValueFunction " + input;
                break;
            case "makeXmlExpr":
                convertedLine = "XmlExpr " + input;
                break;
            case "makeRecursiveViewSelect":
                convertedLine = "SelectStmt " + input;
                break;
            case "doNegateFloat":
                convertedLine = "void " + input.replace("(Float", "(Value");
                break;
            case "SplitColQualList":
                /*
                 * The SplitColQualList function is to be removed completely
                 */
                return null;
            case "processCASbits":
                /*
                 * The processCASbits function is to be removed completely.
                 */
                return null;
            default:
                convertedLine = epilogFunctionReturnType + ' ' + input;
                break;
            }
        } else {
            switch (epilogFunctionName) {
            case "updateRawStmtEnd":
                /*
                 * Properly subtract positions
                 */
                convertedLine = input.replace("rs->stmt_len = end_location - rs->stmt_location;",
                        "if (end_location != null && end_location.end != null && rs.stmt_location != null && rs.stmt_location.begin != null) {"
                                + "\n        rs.stmt_len = end_location.end.getOffset() - rs.stmt_location.begin.getOffset();\n    }");
                break;
            case "makeAConst":
                /*
                 * The return type is A_Const, so better define it that way
                 */
                convertedLine = input.replaceAll("Node\\s+\\*n", "A_Const n")
                        .replaceAll("castNode\\(Float.class,\\s*v\\)->fval", "((Value)v).val.str")
                        .replaceAll("castNode\\(Integer.class,\\s*v\\)->ival", "((Value)v).val.ival");
                break;
            case "makeRangeVarFromQualifiedName":
                /*
                 * The makeRangeVarFromQualifiedName itself is not changed, but the comment following it belongs to
                 * SplitColQualList, which is to be removed completely
                 */
                convertedLine = input.replaceAll("^\\/\\*.*$", "");
                break;
            case "SplitColQualList":
                /*
                 * The SplitColQualList function is to be removed completely
                 */
                return null;
            case "processCASbits":
                /*
                 * The processCASbits function is to be removed completely. But the comment following it is for the
                 * following function, so let's keep that
                 */
                if (input.startsWith("/*") || input.startsWith(" *")) {
                    return input;
                }
                return null;
            case "doNegateFloat":
                /*
                 * Make the donegateFloat a bit simpler
                 */
                if (input.contains("oldval = ")) {
                    return "    v.val.str = Float.toString(-Float.parseFloat(v.val.str));";
                }
                if ("{".equals(input)) {
                    return input;
                }
                if ("}".equals(input)) {
                    epilogFunctionName = "";
                    return input;
                }
                return null;
            case "insertSelectOptions":
                convertedLine = input.replace("(limitClause &&", "(limitClause != null &&")
                        .replace("&& limitClause->limitOffset)", "&& limitClause.limitOffset != null)")
                        .replace("&& limitClause->limitCount)", "&& limitClause.limitCount != null)")
                        .replace("(!stmt->sortClause &&", "(stmt.sortClause == null &&")
                        .replace("&& stmt->lockingClause)", "&& stmt->lockingClause != null)")
                        .replace("lfirst_node(LockingClause, lc)", "(LockingClause)lc.data");
                break;
            case "makeOrderedSetArgs":
                convertedLine = input.replaceAll("(\\s)Integer(\\s)", "$1Value$2");
                break;
            case "doNegate":
                convertedLine = input.replace("con->val.fval", "con.val").replaceAll("^\\{",
                        "{\n    if (n instanceof TypeCast)\n    {\n        ((TypeCast)n).arg = doNegate(((TypeCast)n).arg, location);\n        return n;\n    }");
                break;
            case "preprocess_pubobj_list":
                convertedLine = input.replaceAll("!(\\w[\\w\\.\\->]*)", "$1 == null");
                break;
            default:
                convertedLine = input;
                break;
            }
        }
        convertedLine = convertedLine
                /*
                 * Pointers to values (*foo becomes just foo)
                 */
                .replaceAll("\\*+(\\w)", "$1")
                /*
                 * Location is not an int
                 */
                .replaceAll("int\\s+(\\w*)location", "Location $1location")
                /*
                 * Location is not an int, even if it is called position
                 */
                .replace("int position", "Location position")
                /*
                 * Lines that start with # are for C, not for Java
                 */
                .replaceAll("^#.*", "")
                /*
                 * Restore too eager replacement of enum constant;
                 */
                .replaceAll("case NodeTag\\.T_", "case T_")
                /*
                 * cost has no meaning in Java
                 */
                .replaceAll("(\\W)const(\\W)", "$1$2")
                /*
                 * Asserting that an input argument is of the specified class is done by the compiler.
                 */
                .replaceAll("\\s*Assert.*", "")
                /*
                 * If statements on boolean variables are allowed even in Java. But as they cannot be distinguished from
                 * non-boolean arguments by this converter, I'll check equality to true here.
                 */
                .replaceAll("if\\s*\\(\\s*(deferrable|initdeferred|not_valid|no_inherit)\\s*\\)", "if ($1 == true)")
                /*
                 * In Java zero or null is not the same as boolean false
                 */
                .replaceAll("if\\s*\\(\\s*(\\w*|\\w*->\\w*)\\s*\\)", "if ($1 != null)")
                .replaceAll("if\\s*\\(\\s*!(\\w*|\\w*->\\w*)\\s*\\)", "if ($1 == null)")
                .replaceAll("if\\s*\\(\\s*(\\w*|\\w*->\\w*)\\s*&&\\s*(\\w*|\\w*(->\\w*)+)\\)",
                        "if ($1 != null && $2 != null)")
                /*
                 * End condition in a for loop
                 */
                .replace("; cell;", "; cell != null;")
                /*
                 * Char variables that should have been char pointers (char*) should still be altered to String
                 */
                .replaceAll("char\\s+(\\w*)", "String $1")
                /*
                 * The C boolean - integer mixture again
                 */
                .replaceAll("if \\(cas_bits(.*)", "if (0 != (cas_bits$1)")
                /*
                 * -1 is used for non-existing locations. They must be null
                 */
                .replaceAll("\\,\\s*-1", ", null")
                /*
                 * A statement we want to get rid of
                 */
                .replace("oldval++;", "if (true) {} ")
                /*
                 * Another statement that we don't need
                 */
                .replace("v->val.str = psprintf(\"-%s\", oldval);", ";")
                /*
                 * And some more that is to replaced by nothing
                 */
                .replaceAll(".*(oldval|newval).*", "")
                /*
                 * elog(.....(int) n.type); : n.type is not an int
                 */
                .replaceAll("elog\\((.*)\\(int\\)\\s*", "elog($1")
                /*
                 * Return type Node is a bit too common
                 */
                .replaceAll("^\\s*Node\\s*$", "")
                /*
                 * Remove the cast to (Node) from the return statement
                 */
                .replaceAll("return\\s+\\(Node\\s*\\*\\s*\\)\\s*", "return ")
                /*
                 * 
                 */
                .replace(".ival.ival", ".val.ival").replace(".sval.sval", ".val.str").replace(".fval.fval", ".val.str")
                .replace(".boolval.boolval", ".val.boolval").replace(".bsval.bsval", ".val.str")
                .replaceAll("->val.\\w+val.type", "->val.type");
        return convertedLine;
    }

    /**
     * Writes a few lines before the first line that is read from the input file *
     *
     * @param out
     *            The output file to write to
     */
    private void writeHeading(PrintWriter out) {
        bisonVersion.insertHeadingLines(out, packageName);
        out.println("%parse-param   {core_yyscan_t yyscanner}");
        out.println("%lex-param   {SqlScanner yyscanner}");
        out.println("%code imports {");
        out.println("import java.io.InputStream;");
        out.println("import java.io.ObjectInputStream;");
        out.println();
        out.println("import com.splendiddata.sqlparser.enums.*;");
        out.println("import com.splendiddata.sqlparser.plumbing.base_yy_extra_type;");
        out.println("import com.splendiddata.sqlparser.plumbing.YYLTYPE;");
        out.println("import com.splendiddata.sqlparser.plumbing.YYSTypeJsonOnBehaviour;");
        out.println("import com.splendiddata.sqlparser.structure.*;");
        out.println("}");
        out.println("%code {");
        out.println("  private static final int COERCION_IMPLICIT = 0;");
        out.println("  private static final int COERCION_ASSIGNMENT = 1;");
        out.println("  private static final int COERCION_EXPLICIT = 2;");
        out.println("  private static final Object NULL = null;");
        out.println("  private static final List NIL = null;");
        out.println("  private static final boolean TRUE = true;");
        out.println("  private static final boolean FALSE = false;");
        out.println("  private static final Oid InvalidOid = null;");
        out.println();
        out.println("   /* Copied from /postgresql/src/include/nodes/parsenodes.h */");
        out.println("  /** BINARY */");
        out.println("  private static final int CURSOR_OPT_BINARY       = 0x0001;");
        out.println("  /** SCROLL explicitly given */");
        out.println("  private static final int CURSOR_OPT_SCROLL       = 0x0002;");
        out.println("  /** NO SCROLL explicitly given */");
        out.println("  private static final int CURSOR_OPT_NO_SCROLL    = 0x0004;");
        out.println("  /** INSENSITIVE */");
        out.println("  private static final int CURSOR_OPT_INSENSITIVE  = 0x0008;");
        out.println("  /** ");
        out.println("   * ASENSITIVE");
        out.println("   * @since 14.0");
        out.println("   */");
        out.println("  private static final int CURSOR_OPT_ASENSITIVE   = 0x0010;");
        out.println("  /** WITH HOLD */");
        out.println("  private static final int CURSOR_OPT_HOLD         = 0x0020;");
        out.println("  /** ");
        out.println("   * prefer fast-start plan");
        out.println("   * @since 14.0");
        out.println("   */");
        out.println("  private static final int CURSOR_OPT_FAST_PLAN    = 0x0100;");
        out.println();
        out.println("  /* Copied from /postgresql/src/include/nodes/parsenodes.h */");
        out.println("  /** input only */");
        out.println("  private static final char FUNC_PARAM_IN          = 'i';");
        out.println("  /** output only */");
        out.println("  private static final char FUNC_PARAM_OUT         = 'o';");
        out.println("  /** both */");
        out.println("  private static final char FUNC_PARAM_INOUT       = 'b';");
        out.println("  /** variadic (always input) */");
        out.println("  private static final char FUNC_PARAM_VARIADIC    = 'v';");
        out.println("  /** table function output column */");
        out.println("  private static final char FUNC_PARAM_TABLE       = 't';");
        out.println("  /**");
        out.println("   * this is not used in pg_proc:<br>");
        out.println("   * default; effectively same as IN");
        out.println("   * @since 14.0");
        out.println("   */");
        out.println("  private static final char FUNC_PARAM_DEFAULT     = 'd';");
        out.println("  private static final int OPCLASS_ITEM_OPERATOR      = 1;");
        out.println("  private static final int OPCLASS_ITEM_FUNCTION      = 2;");
        out.println("  private static final int OPCLASS_ITEM_STORAGETYPE   = 3;");
        out.println("  private static final long FETCH_ALL = Long.MAX_VALUE;");
        out.println();
        out.println("  /*");
        out.println("   * obtained from /postgresql-9.3.4/src/include/catalog/index.h");
        out.println("   */");
        out.println("  private static final String DEFAULT_INDEX_TYPE = \"btree\";");
        out.println();
        out.println("  /*");
        out.println("   * obtained from /postgresql-9.3.4/src/include/catalog/pg_trigger.h");
        out.println("   */");
        out.println("  private static final int TRIGGER_TYPE_AFTER              = 0;");
        out.println("  private static final int TRIGGER_TYPE_BEFORE             = 1 << 1;");
        out.println("  private static final int TRIGGER_TYPE_INSERT             = 1 << 2;");
        out.println("  private static final int TRIGGER_TYPE_DELETE             = 1 << 3;");
        out.println("  private static final int TRIGGER_TYPE_UPDATE             = 1 << 4;");
        out.println("  private static final int TRIGGER_TYPE_TRUNCATE           = 1 << 5;");
        out.println("  private static final int TRIGGER_TYPE_INSTEAD            = 1 << 6;");
        out.println();
        out.println("  /**");
        out.println("   * obtained from /postgresql-9.3.4/src/include/commands/trigger.h");
        out.println("   */");
        out.println("  private static final char TRIGGER_FIRES_ON_ORIGIN            = 'O';");
        out.println("  private static final char TRIGGER_FIRES_ALWAYS               = 'A';");
        out.println("  private static final char TRIGGER_FIRES_ON_REPLICA           = 'R';");
        out.println("  private static final char TRIGGER_DISABLED                   = 'D';");
        out.println();
        out.println("  /**");
        out.println("   * obtained from /postgresql-9.3.4/src/include/utils/timestamp.h");
        out.println("   */");
        out.println("  private static final int INTERVAL_FULL_RANGE            = 0x7FFF;");
        out.println();
        out.println("  /*");
        out.println("   * obtained from /postgresql-9.3.4/src/include/storage/lock.h");
        out.println("   */");
        out.println();
        out.println("  /** SELECT */");
        out.println("  private static final int AccessShareLock          = 1;");
        out.println("  /** SELECT FOR UPDATE/FOR SHARE */");
        out.println("  private static final int RowShareLock             = 2;");
        out.println("  /** INSERT, UPDATE, DELETE */");
        out.println("  private static final int RowExclusiveLock         = 3;");
        out.println("  /** VACUUM (non-FULL),ANALYZE, CREATE INDEX CONCURRENTLY */");
        out.println("  private static final int ShareUpdateExclusiveLock = 4;");
        out.println("  /** CREATE INDEX (WITHOUT CONCURRENTLY) */");
        out.println("  private static final int ShareLock                = 5;");
        out.println("  /** like EXCLUSIVE MODE, but allows ROW SHARE */");
        out.println("  private static final int ShareRowExclusiveLock    = 6;");
        out.println("  /** blocks ROW SHARE/SELECT...FOR UPDATE */");
        out.println("  private static final int ExclusiveLock            = 7;");
        out.println("  /**  ALTER TABLE, DROP TABLE, VACUUM FULL, and unqualified LOCK TABLE */");
        out.println("  private static final int AccessExclusiveLock      = 8;");
        out.println();
        out.println("  /*");
        out.println("   * obtained from postgresql-11beta4/src/include/c.h");
        out.println("   */");
        out.println("  private static final int PG_INT16_MAX             = 0x7FFF;");
        out.println();
        out.println("  /**");
        out.println("   * obtained from postgresql-13beta1/src/include/c.h");
        out.println("   * @since 8.0 - Postgres 13");
        out.println("   */");
        out.println("  private static final int InvalidSubTransactionId  = 0;");
        out.println("}");
    }

    /**
     * @see java.nio.file.FileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     * 
     * @return FileVisitResult.CONTINUE
     */
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        return FileVisitResult.CONTINUE;
    }

    /**
     * @see java.nio.file.FileVisitor#visitFileFailed(java.lang.Object, java.io.IOException)
     * 
     * @return FileVisitResult.CONTINUE
     */
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    /**
     * @see java.nio.file.FileVisitor#postVisitDirectory(java.lang.Object, java.io.IOException)
     * 
     * @return FileVisitResult.CONTINUE
     */
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    /**
     * Determines the Bison version that will be used by executing a "bison --version" OS command and interpreting the
     * result
     */
    private void determineBisionVersion() {
        try {
            Process bisonProcess = new ProcessBuilder("bison", "--version").start();
            try (InputStream is = bisonProcess.getInputStream();
                    InputStreamReader bis = new InputStreamReader(is);
                    BufferedReader bisonOutput = new BufferedReader(bis)) {
                String lf = System.getProperty("line.separator");
                StringBuilder msg = new StringBuilder();
                msg.append("Bison version: ").append(lf);
                String line = bisonOutput.readLine();

                String usingBisonVersionLine;
                Matcher matcher = BISON_VERSION_PATTERN.matcher(line);
                if (matcher.matches()) {
                    bisonVersion = BisonVersion.fromVersionString(matcher.group(1));
                }
                if (bisonVersion == null) {
                    usingBisonVersionLine = "No Bison version found, assuming version 2";
                    bisonVersion = BisonVersion.BISON_VERSION_2;
                } else {
                    usingBisonVersionLine = "Using " + bisonVersion;
                }

                for (; line != null; line = bisonOutput.readLine()) {
                    msg.append(line).append(lf);
                }
                log.info(msg);
                log.info(usingBisonVersionLine);
            }
        } catch (IOException e) {
            log.error("determineBisionVersion()->failed", e);
        }
    }
}
