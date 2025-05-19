/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2025
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

/**
 * Some grammar rules cannot be converted using generic conversions. This enum provides special processing for them. The
 * name of the enum value relates to the name of the rule in the gram.y file.
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public enum GrammarRuleSpecial implements GrammarRuleSpecialProcessing {
    DEFAULT(),

    parse_toplevel(line -> {
        switch (GrammarConverter.bisonVersion) {
        case BISON_VERSION_2:
        case BISON_VERSION_3:
        case BISON_VERSION_3_3:
        case BISON_VERSION_3_5:
            return line.replace("(void) yynerrs;", "// yynerrs = 0;");
        default:
            return line.replace("(void) yynerrs;", "yynerrs = 0;");
        }
    }),

    /**
     * Replaces "if ($n)" constructs with "if ($n != null)".
     */
    CopyStmt(
            line -> line
                    /*
                     * group 1 = (if\\s*\\(\\s*) group 2 = (\\$\\d+) group 3 = (\\s*\\))
                     */
                    .replaceAll("(if\\s*\\(\\s*)(\\$\\d+)(\\s*\\))", "if ($2 != null)")),

    ConstraintElem(
            line -> line
                    /*
                     * replaces "processCASbits(" with "processCASbits(n, ".
                     */
                    .replace("processCASbits(", "processCASbits(n, ").replace("if ($5)", "if ($5 != null)")
                    .replace("if (lsecond($9))", "if (lsecond($9) != null)").replace(
                            "n->fk_attrs = lappend(n->fk_attrs, $5);", "n.fk_attrs = lappend(n.fk_attrs, (Value)$5);")),

    CreateAssertStmt(
            line -> line
                    /*
                     * Adds the CreateTrigStmt to the processCASbits() method to avoid C-style indirections
                     */
                    .replace("processCASbits(", "processCASbits(n, ").replace("list_make1(", "list_make1((Value)")),

    CreateTrigStmt(
            line -> line
                    /*
                     * Adds the CreateTrigStmt to the processCASbits() method to avoid C-style indirections
                     */
                    .replace("processCASbits(", "processCASbits(n, ")),

    alter_generic_option_elem(
            line -> line
                    /*
                     * $$->field replaced by ((defelt)$).field
                     */
                    .replace("$$->", "((defelt)$$).")),

    TriggerEvents(
            line -> line
                    /*
                     * replace "if (events1 & events2)" by "if ((events1 & events2) != 0)"
                     */
                    .replace("if (events1 & events2)", "if ((events1 & events2) != 0)")),

    privilege_target(
            line -> line
                    /*
                     * replace "PrivTarget *n = (PrivTarget *) palloc(sizeof(PrivTarget))" by "PrivTarget n =
                     * makeNode(PrivTarget.class)"
                     */
                    .replace("PrivTarget *n = (PrivTarget *) palloc(sizeof(PrivTarget))",
                            "PrivTarget n = new PrivTarget()")),

    VacuumStmt(
            line -> line
                    /*
                     * replace "if (n->options & VACOPT_FREEZE)" by "if ((n.options & VACOPT_FREEZE) != 0)"
                     */
                    .replace("if (n->options & VACOPT_FREEZE)", "if ((n.options & VACOPT_FREEZE) != 0)")),

    func_arg_with_default(
            line -> line
                    /*
                     * replace "$$->" by "((fun_param)$$)."
                     */
                    .replace("$$->", "((fun_param)$$).")),

    func_type(
            line -> line
                    /*
                     * replace "$$->" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).")),

    func_expr(
            line -> line
                    /*
                     * replace -1 to null (-1 stands for "location unknown")
                     */
                    .replace("-1", "null")),

    single_set_clause(
            line -> line
                    /*
                     * replace "$$->" by "((target)$$)."
                     */
                    .replace("$$->", "((target)$$).")),

    table_ref(
            line -> line
                    /*
                     * replace "$n)-&gt;valuesLists" by "$n).valuesLists != null"
                     */
                    .replaceAll("(\\$\\d*\\))->valuesLists", "$1.valuesLists != null")
                    /*
                     * replace very lsecond will return a List
                     */
                    .replace("lsecond", "(List)lsecond")),

    OptTempTableName(
            line -> line
                    /*
                     * replace "$$->" by "((range)$$)."
                     */
                    .replace("$$->", "((range)$$).")),

    relation_expr(
            line -> line
                    /*
                     * replace "$$->" by "((range)$$)."
                     */
                    .replace("$$->", "((range)$$).")),

    GenericType(
            line -> line
                    /*
                     * replace "$$->" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).")),

    Typename(
            line -> line
                    /*
                     * replace "$$->;" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).")),

    SimpleTypename(
            line -> line
                    /*
                     * replace "$$->" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).")),

    Numeric(
            line -> line
                    /*
                     * replace "$$->" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).")),

    ConstBit(
            line -> line
                    /*
                     * replace "$$->" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).")),

    BitWithLength(
            line -> line
                    /*
                     * replace "$$-&gt;" by "((typeName)$$)."
                     */
                    .replace("char *typname", "String typname")
                    /*
                     * replace "char * typname" by "String typname"
                     */
                    .replace("$$->", "((TypeName)$$).")),

    BitWithoutLength(
            line -> line
                    /*
                     * replace "$$-&gt;" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).")),

    ConstCharacter(
            line -> line
                    /*
                     * replace "$$->" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).")),

    CharacterWithLength(
            line -> line
                    /*
                     * Type-cast the result
                     */
                    .replace("char *type", "String type").replace("$$->", "((TypeName)$$).")),

    CharacterWithoutLength(
            line -> line
                    /*
                     * Type-cast the result
                     */
                    .replace("char *type", "String type").replace("$$->", "((TypeName)$$).")),

    ConstDatetime(
            line -> line
                    /*
                     * replace "$$-&gt;" by "((Iconst)$$)."
                     */
                    .replace("$$->", "((Iconst)$$).")),

    ConstInterval(
            line -> line
                    /*
                     * replace "$$-&gt;" by "((target)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).")),

    opt_interval(
            line -> line
                    /*
                     * "linitial" means something like replace the first element of the list
                     */
                    .replaceAll("linitial\\(\\$\\$\\) = (.*)", "((List)\\$\\$).set(0, $1")
                    .replace(", @1);", ", @1));")),

    a_expr(
            line -> line
                    /*
                     * replace "if ($3 == ANY_SUBLINK)" by "if (ANY_SUBLINK.equals($3))"
                     */
                    .replace("if ($3 == ANY_SUBLINK)", "if (ANY_SUBLINK.equals($3))")),

    c_expr(
            line -> line
                    /*
                     * group 1 = (if\\s*\\(\\s*) group 2 = (\\$\\d+) group 3 = (\\s*\\))
                     */
                    .replaceAll("(if\\s*\\(\\s*)(\\$\\d+)(\\s*\\))", "if ($2 != null)")),

    opt_frame_clause(
            line -> line
                    /*
                     * Replace C's boolean interpretation of an int by a Java != 0 operator
                     */
                    .replace("ING))", "ING)))").replaceAll("(\\()(.*?frameOptions)([^)]*)(\\)?)", "(0 != ($2$3$4$4")),

    frame_extent(
            line -> line
                    /*
                     * Replace C's boolean interpretation of an int by a Java != 0 operator
                     */
                    .replace("_ROW)))", "_ROW))))")
                    .replaceAll("(\\()(.*?frameOptions)([^)]*)(\\)?)", "(0 != ($2$3$4$4")),

    qualified_name(
            line -> line
                    /*
                     * replace "$$-&gt;" by "((RangeVar)$$)."
                     */
                    .replace("$$->", "((RangeVar)$$).")),

    set_rest_more(
            line -> line
                    /*
                     * replace "$3 == XMLOPTION_DOCUMENT" by "$3.intValue() == XMLOPTION_DOCUMENT"
                     */
                    .replace("$3 == XMLOPTION_DOCUMENT", "$3.intValue() == XMLOPTION_DOCUMENT")),
    SignedIconst(
            line -> line

                    /*
                     * replace "$2" by "$2.intValue()"
                     */
                    .replace("$2", "$2.intValue()")),

    select_no_parens(
            line -> line
                    /*
                     * The result of every list_nth() invocation needs a cast to Node
                     */
                    .replace("list_nth", "(Node)list_nth")),

    TableLikeOptionList(
            line -> line
                    /*
                     * Ands and ors are for integer values
                     */
                    .replace("$1", "$1.intValue()").replace("$3", "$3.intValue()")),

    vacuum_option_list(
            line -> line
                    /*
                     * Ands and ors are for integer values
                     */
                    .replace("$1 | $3", "$1.intValue() | $3.intValue()")),

    cursor_options(
            line -> line
                    /*
                     * Ands and ors are for integer values
                     */
                    .replace("$1", "$1.intValue()")),

    alter_table_cmd(
            line -> line
                    /*
                     * In Java null is not the same as boolean false. So lets make proper booleans for the
                     * processCASbits method. Also provide the processCASBits method with the constraint in which the
                     * CASBits should be stored.
                     */
                    .replace("processCASbits(", "processCASbits(c, ")
                    .replace("NULL, NULL, yyscanner);", "false, false, yyscanner);")
                    .replace("if ($4 & ", "if (0 != ($4.intValue() & ").replaceAll(("(\\bCAS_.*\\))$"), "$1)")),

    columnDef(
            line -> line
                    /*
                     * The SplitColQualList method needs replacement
                     */
                    .replace("SplitColQualList($6, &n->constraints, &n->collClause,", "splitColQualList($6, n,")),

    columnOptions(
            line -> line
                    /*
                     * The SplitColQualList method needs replacement
                     */
                    .replace("SplitColQualList($2, &n->constraints, &n->collClause,", "splitColQualList($2, n,")
                    .replace("SplitColQualList($4, &n->constraints, &n->collClause,", "splitColQualList($4, n,")),

    CreateDomainStmt(
            line -> line
                    /*
                     * The SplitColQualList method needs replacement
                     */
                    .replace("SplitColQualList($6, &n->constraints, &n->collClause,", "splitColQualList($6, n,")),

    RoleSpec(
            line -> line
                    /*
                     * The RoleSpec might not be initialised
                     */
                    .replaceAll("RoleSpec\\s+\\*n;", "RoleSpec n = null;")),

    for_locking_item(
            line -> line
                    /*
                     * Repair the type cast to LockWaitpolicy
                     */
                    .replace("n->waitPolicy = $3;", "n.waitPolicy = (LockWaitPolicy)$3;")),

    import_qualification(
            line -> line
                    /*
                     * Repair the type cast to ImportForeignSchemaType
                     */
                    .replace("n->type = $1;", "n.type = (ImportForeignSchemaType)$1;")),

    ReindexStmt(
            line -> line
                    /*
                     * Repair the type cast to ReindexObjectType
                     */
                    .replace("kind = $", "kind = (ReindexObjectType)$")),

    reindex_option_list(
            line -> line
                    /*
                     * Repair the type cast to ReindexObjectType
                     */
                    .replace("$$ = $1 | $3", "$$ = (Integer)$1 | $3")),

    opt_column(line -> {
        /*
         * The COLUMN keyword is Lexer qualified in Bison version 3 upward
         */
        if (!BisonVersion.BISON_VERSION_2.equals(GrammarConverter.bisonVersion)) {
            return line.replace("COLUMN;", "Lexer.COLUMN;");
        }
        return line;
    }),

    stmtmulti(
            line -> line
                    /*
                     * Just a simple type cast will do fine
                     */
                    .replace("llast_node(RawStmt, $1)", "(RawStmt)$1.getTail().data")
                    /*
                     * location is not an int in Java
                     */
                    .replace("@2 + 1",
                            "\n                                @2.end == null ? new Location(@2.begin, new Position(@2.begin.getOffset() + 1))"
                                    + "\n                                               : new Location(@2.begin, new Position(@2.end.getOffset() + 1))")
                    /*
                     * the 0 stands for "no location", so let's make it null
                     */
                    .replace(", 0)", ", null)")),

    AlterTableStmt(
            line -> line
                    /*
                     * A typecast to AlterTableCmd is necessary
                     */
                    .replace("list_make1(", "list_make1((AlterTableCmd)")),
    xmltable_column_el(
            line -> line
                    /*
                     * int and boolean are not the same in Java
                     */
                    .replace("intVal(defel->arg)", "((Value)defel.arg).val.ival != 0")),
    RowSecurityDefaultPermissive(
            line -> line
                    /*
                     * Avoid a class cast exception from String to Boolean
                     */
                    .replaceAll("else$", "else {\n                        \\$\\$ = Boolean.TRUE;")
                    .replaceAll("^(\\s*)}\\s*$", "                    }\n$1}")),
    AlterOptRoleElem(
            line -> line
                    /*
                     * When the phrase "unencrypted password" is used, an error is reported, but the password is left in
                     * the stack as a String. But when the statement is put together, the password is blindly cast to
                     * DefElem - so a ClassCastException occurs. Better try to cast null than a String.
                     */
                    .replaceAll("(\\s*)\\{", "$1{\n$1    \\$\\$ = null;").replace("\\$\\$", "$$")),
    /** @since 6.0 - Postgres version 11 */
    analyze_option_list(
            line -> line
                    /*
                     * The OR operation must be done on an integer scalar, not on the object
                     */
                    .replace("$3", "$3.intValue()")),
    /** @since 6.0 - Postgres version 11 */
    PartitionBoundSpec(
            line -> line
                    /*
                     * Let's not over-complicate a simple type cast
                     */
                    .replace("lfirst_node(DefElem, lc)", "(DefElem)lc.data")),
    /** @since 8.0 - Postgres version 13 */
    index_elem(
            line -> line
                    /*
                     * Type cast
                     */
                    .replace("$$->", "((IndexElem)$$).")),
    /** @since 8.0 - Postgres version 13 */
    select_limit(
            line -> line
                    /* Type cast */
                    .replace("($$)->", "((SelectLimit)$$).")
                    /* since Postgres 18 */
                    .replace("Loc = -1", "Loc = null")),
    /** @since 13 */
    create_extension_opt_list(
            line -> line
                    /*
                     * Temporary work around as the "from version" in the create extension statement is still known as
                     * an option but is not supported any more. A syntax error will be given, but the grammar now
                     * attempts to add a String to a List<DefElem>, which will not succeed of course.
                     */
                    .replace("{ $$ = lappend($1, $2); }",
                            "{ if (yystack.valueAt (0) instanceof DefElem) { $$ = lappend($1, $2); } }")),
    /** @since 14 */
    joined_table(
            line -> line
                    /*
                     * Repair some odd casts
                     */
                    .replaceAll("linitial_node\\(List, castNode\\(List\\.class, (\\$\\d)\\)\\)",
                            "(List)((List)$1).get(0)")
                    .replaceAll("lsecond_node\\(Alias, castNode\\(List\\.class, (\\$\\d)\\)\\)",
                            "(Alias)((List)$1).get(1)")),
    /** @since 14 */
    PLpgSQL_Expr(
            line -> line.replace("if ($9)", "if ($9 != null)").replace("if (!n->sortClause",
                    "if (n.sortClause == null")),
    /** @since Postgres 15 */
    key_actions(line -> line.replaceAll("n->(\\w+)\\s*=\\s*palloc\\(sizeof\\((\\w+)\\)\\);", "n.$1 = new $2();")),
    /** @since Postgres 15 */
    NumericOnly(line -> line.replaceAll("(\\s+)Float(\\s+)", "$1Value$2")),
    /** @since Postgres 15 */
    key_update(line -> line.replace("->cols", "->cols != null")),
    /** @since Postgres 15 */
    PublicationObjSpec(
            line -> line.replace("if ($2 || $3)", "if ($2 != null || $3 != null)")
                    .replace("$$->name", "((PublicationObjSpec)$$).name")
                    .replace("$$->location", "((PublicationObjSpec)$$).location")),
    /** @since Postgres 15 */
    extended_relation_expr(
            line -> line.replace("$$->inh", "((RangeVar)$$).inh").replace("$$->alias", "((RangeVar)$$).alias")),
    /** @since Postgres 15 */
    zone_value(line -> line.replace(".ival.ival", ".val.ival")),
    /** @since Postgres 16 */
    NonReservedWord(line -> line.replace("pstrdup($1)", "$1.toString()")),
    /** @since Postgres 17 */
    DomainConstraintElem(line -> line.replace("processCASbits(", "processCASbits(n, ")),
    /** @since Postgres 17 */
    JsonType(line -> line.replace("$$->location", "((TypeName)$$).location")),
    /** @since Postgres 17 */
    json_format_clause(
            line -> line.replaceAll("int\\s+encoding;", "JsonEncoding encoding = JsonEncoding.JS_ENC_DEFAULT;")
                    .replace("!pg_strcasecmp", "0 == pg_strcasecmp")),
    /** @since Postgres 17 */
    json_table(
            line -> line.replace("castNode(A_Const.class, $5)->val.sval.sval", "$5->toString()")
                    .replace("val.node.type", "val.type")),
    /** @since Postgres 18 */
    select_with_parens(line -> line.replace("@3 - @2", "@3.getOffset() - @2.getOffset()")),
    /** @since Postgres 18 */
    limit_clause(line -> line.replace("Loc = -1", "Loc = null")),
    //
    // Example of injecting dumpYystack
    //    /**
    //     * Dump the yystack to debug
    //     */
    //    json_query_expr(new GrammarRuleSpecialProcessing() {
    //        public String processLine(String line) {
    //            return line.replaceAll("^(\\s*)\\{", "$1{\n$1    dumpYystack(\"json_query_expr:\", yystack.height, i->yystack.valueAt(i));");
    //        }
    //    }),
    /**
     * We're in the epilog section
     */
    IN_EPILOG;

    private final GrammarRuleSpecialProcessing specialProcessing;

    /**
     * Constructor
     *
     */
    private GrammarRuleSpecial() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param specialProcessing
     */
    private GrammarRuleSpecial(GrammarRuleSpecialProcessing specialProcessing) {
        this.specialProcessing = specialProcessing;
    }

    /**
     * @see com.splendiddata.sqlparser.grammartojava.GrammarRuleSpecialProcessing#processLine(java.lang.String)
     *
     * @param line
     *            The line from the grammar
     * @return String the processed
     */
    @Override
    public String processLine(String line) {
        if (specialProcessing == null) {
            return line;
        }
        return specialProcessing.processLine(line);
    }

}
