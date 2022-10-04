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

/**
 * Some grammar rules cannot be converted using generic conversions. This enum provides special processing for them. The
 * name of the enum value relates to the name of the rule in the gram.y file.
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public enum GrammarRuleSpecial implements GrammarRuleSpecialProcessing {
    DEFAULT(),
    
    parse_toplevel(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line.replace("(void) yynerrs;", "yynerrs = 0;");
        }
    }),

    /**
     * Replaces "if ($n)" constructs with "if ($n != null)".
     */
    CopyStmt(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * group 1 = (if\\s*\\(\\s*) group 2 = (\\$\\d+) group 3 = (\\s*\\))
                     */
                    .replaceAll("(if\\s*\\(\\s*)(\\$\\d+)(\\s*\\))", "if ($2 != null)");
        }
    }),

    ConstraintElem(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replaces "processCASbits(" with "processCASbits(n, ".
                     */
                    .replace("processCASbits(", "processCASbits(n, ");
        }
    }),

    CreateAssertStmt(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Adds the CreateTrigStmt to the processCASbits() method to avoid C-style indirections
                     */
                    .replace("processCASbits(", "processCASbits(n, ").replace("list_make1(", "list_make1((Value)");
        }
    }),

    CreateTrigStmt(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Adds the CreateTrigStmt to the processCASbits() method to avoid C-style indirections
                     */
                    .replace("processCASbits(", "processCASbits(n, ");
        }
    }),

    alter_generic_option_elem(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * $$->field replaced by ((defelt)$).field
                     */
                    .replace("$$->", "((defelt)$$).");
        }
    }),

    TriggerEvents(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "if (events1 & events2)" by "if ((events1 & events2) != 0)"
                     */
                    .replace("if (events1 & events2)", "if ((events1 & events2) != 0)");
        }
    }),

    privilege_target(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "PrivTarget *n = (PrivTarget *) palloc(sizeof(PrivTarget))" by "PrivTarget n =
                     * makeNode(PrivTarget.class)"
                     */
                    .replace("PrivTarget *n = (PrivTarget *) palloc(sizeof(PrivTarget))",
                            "PrivTarget n = new PrivTarget()");
        }
    }),

    VacuumStmt(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "if (n->options & VACOPT_FREEZE)" by "if ((n.options & VACOPT_FREEZE) != 0)"
                     */
                    .replace("if (n->options & VACOPT_FREEZE)", "if ((n.options & VACOPT_FREEZE) != 0)");
        }
    }),

    func_arg_with_default(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$->" by "((fun_param)$$)."
                     */
                    .replace("$$->", "((fun_param)$$).");
        }
    }),

    func_type(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$->" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).");
        }
    }),

    func_expr(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace -1 to null (-1 stands for "location unknown")
                     */
                    .replace("-1", "null");
        }
    }),

    single_set_clause(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$->" by "((target)$$)."
                     */
                    .replace("$$->", "((target)$$).");
        }
    }),

    table_ref(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$n)-&gt;valuesLists" by "$n).valuesLists != null"
                     */
                    .replaceAll("(\\$\\d*\\))->valuesLists", "$1.valuesLists != null")
                    /*
                     * replace very lsecond will return a List
                     */
                    .replace("lsecond", "(List)lsecond");
        }
    }),

    OptTempTableName(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$->" by "((range)$$)."
                     */
                    .replace("$$->", "((range)$$).");
        }
    }),

    relation_expr(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$->" by "((range)$$)."
                     */
                    .replace("$$->", "((range)$$).");
        }
    }),

    GenericType(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$->" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).");
        }
    }),

    Typename(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$->;" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).");
        }
    }),

    SimpleTypename(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$->" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).");
        }
    }),

    Numeric(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$->" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).");
        }
    }),

    ConstBit(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$->" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).");
        }
    }),

    BitWithLength(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$-&gt;" by "((typeName)$$)."
                     */
                    .replace("char *typname", "String typname")
                    /*
                     * replace "char * typname" by "String typname"
                     */
                    .replace("$$->", "((TypeName)$$).");
        }
    }),

    BitWithoutLength(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$-&gt;" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).");
        }
    }),

    ConstCharacter(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$->" by "((TypeName)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).");
        }
    }),

    CharacterWithLength(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Type-cast the result
                     */
                    .replace("char *type", "String type").replace("$$->", "((TypeName)$$).");
        }
    }),

    CharacterWithoutLength(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Type-cast the result
                     */
                    .replace("char *type", "String type").replace("$$->", "((TypeName)$$).");
        }
    }),

    ConstDatetime(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$-&gt;" by "((Iconst)$$)."
                     */
                    .replace("$$->", "((Iconst)$$).");
        }
    }),

    ConstInterval(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$-&gt;" by "((target)$$)."
                     */
                    .replace("$$->", "((TypeName)$$).");
        }
    }),

    opt_interval(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * "linitial" means something like replace the first element of the list
                     */
                    .replaceAll("linitial\\(\\$\\$\\) = (.*)", "((List)\\$\\$).set(0, $1").replace(", @1);", ", @1));");
        }
    }),

    a_expr(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "if ($3 == ANY_SUBLINK)" by "if (ANY_SUBLINK.equals($3))"
                     */
                    .replace("if ($3 == ANY_SUBLINK)", "if (ANY_SUBLINK.equals($3))");
        }
    }),

    c_expr(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * group 1 = (if\\s*\\(\\s*) group 2 = (\\$\\d+) group 3 = (\\s*\\))
                     */
                    .replaceAll("(if\\s*\\(\\s*)(\\$\\d+)(\\s*\\))", "if ($2 != null)");
        }
    }),

    opt_frame_clause(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Replace C's boolean interpretation of an int by a Java != 0 operator
                     */
                    .replace("ING))", "ING)))").replaceAll("(\\()(.*?frameOptions)([^)]*)(\\)?)", "(0 != ($2$3$4$4");
        }
    }),

    frame_extent(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Replace C's boolean interpretation of an int by a Java != 0 operator
                     */
                    .replace("_ROW)))", "_ROW))))")
                    .replaceAll("(\\()(.*?frameOptions)([^)]*)(\\)?)", "(0 != ($2$3$4$4");
        }
    }),

    qualified_name(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$$-&gt;" by "((RangeVar)$$)."
                     */
                    .replace("$$->", "((RangeVar)$$).");
        }
    }),

    set_rest_more(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * replace "$3 == XMLOPTION_DOCUMENT" by "$3.intValue() == XMLOPTION_DOCUMENT"
                     */
                    .replace("$3 == XMLOPTION_DOCUMENT", "$3.intValue() == XMLOPTION_DOCUMENT");
        }
    }),
    SignedIconst(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line

                    /*
                     * replace "$2" by "$2.intValue()"
                     */
                    .replace("$2", "$2.intValue()");
        }
    }),

    select_no_parens(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * The result of every list_nth() invocation needs a cast to Node
                     */
                    .replace("list_nth", "(Node)list_nth");
        }
    }),

    TableLikeOptionList(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Ands and ors are for integer values
                     */
                    .replace("$1", "$1.intValue()").replace("$3", "$3.intValue()");
        }
    }),

    vacuum_option_list(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Ands and ors are for integer values
                     */
                    .replace("$1 | $3", "$1.intValue() | $3.intValue()");
        }
    }),

    cursor_options(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Ands and ors are for integer values
                     */
                    .replace("$1", "$1.intValue()");
        }
    }),

    alter_table_cmd(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * In Java null is not the same as boolean false. So lets make proper booleans for the
                     * processCASbits method. Also provide the processCASBits method with the constraint in which the
                     * CASBits should be stored.
                     */
                    .replace("processCASbits(", "processCASbits(c, ")
                    .replace("NULL, NULL, yyscanner);", "false, false, yyscanner);");
        }
    }),

    columnDef(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * The SplitColQualList method needs replacement
                     */
                    .replace("SplitColQualList($5, &n->constraints, &n->collClause,", "splitColQualList($5, n,");
        }
    }),

    columnOptions(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * The SplitColQualList method needs replacement
                     */
                    .replace("SplitColQualList($2, &n->constraints, &n->collClause,", "splitColQualList($2, n,")
                    .replace("SplitColQualList($4, &n->constraints, &n->collClause,", "splitColQualList($4, n,");
        }
    }),

    CreateDomainStmt(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * The SplitColQualList method needs replacement
                     */
                    .replace("SplitColQualList($6, &n->constraints, &n->collClause,", "splitColQualList($6, n,");
        }
    }),

    RoleSpec(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * The RoleSpec might not be initialised
                     */
                    .replaceAll("RoleSpec\\s+\\*n;", "RoleSpec n = null;");
        }
    }),

    for_locking_item(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Repair the type cast to LockWaitpolicy
                     */
                    .replace("n->waitPolicy = $3;", "n.waitPolicy = (LockWaitPolicy)$3;");
        }
    }),

    import_qualification(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Repair the type cast to ImportForeignSchemaType
                     */
                    .replace("n->type = $1;", "n.type = (ImportForeignSchemaType)$1;");
        }
    }),

    ReindexStmt(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Repair the type cast to ReindexObjectType
                     */
                    .replace("kind = $", "kind = (ReindexObjectType)$");
        }
    }),

    reindex_option_list(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Repair the type cast to ReindexObjectType
                     */
                    .replace("$$ = $1 | $3", "$$ = (Integer)$1 | $3");
        }
    }),

    opt_column(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            /*
             * The COLUMN keyword is Lexer qualified in Bison version 3 upward
             */
            if (!BisonVersion.BISON_VERSION_2.equals(GrammarConverter.bisonVersion)) {
                return line.replace("COLUMN;", "Lexer.COLUMN;");
            }
            return line;
        }
    }),

    stmtmulti(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
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
                    .replace(", 0)", ", null)");
        }
    }),

    AlterTableStmt(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * A typecast to AlterTableCmd is necessary
                     */
                    .replace("list_make1(", "list_make1((AlterTableCmd)");
        }
    }),
    xmltable_column_el(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * int and boolean are not the same in Java
                     */
                    .replace("intVal(defel->arg)", "((Value)defel.arg).val.ival != 0");
        }
    }),
    RowSecurityDefaultPermissive(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Avoid a class cast exception from String to Boolean
                     */
                    .replaceAll("else$", "else\n                        \\$\\$ = Boolean.TRUE;");
        }
    }),
    AlterOptRoleElem(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * When the phrase "unencrypted password" is used, an error is reported, but the password is left in
                     * the stack as a String. But when the statement is put together, the password is blindly cast to
                     * DefElem - so a ClassCastException occurs. Better try to cast null than a String.
                     */
                    .replaceAll("(\\s*)\\{", "$1{\n$1    \\$\\$ = null;").replace("\\$\\$", "$$");
        }
    }),
    /** @since 6.0 - Postgres version 11 */
    analyze_option_list(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * The OR operation must be done on an integer scalar, not on the object
                     */
                    .replace("$3", "$3.intValue()");
        }
    }),
    /** @since 6.0 - Postgres version 11 */
    PartitionBoundSpec(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Let's not over-complicate a simple type cast
                     */
                    .replace("lfirst_node(DefElem, lc)", "(DefElem)lc.data");
        }
    }),
    /** @since 8.0 - Postgres version 13 */
    index_elem(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Type cast
                     */
                    .replace("$$->", "((IndexElem)$$).");
        }
    }),
    /** @since 8.0 - Postgres version 13 */
    select_limit(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Type cast
                     */
                    .replace("($$)->", "((SelectLimit)$$).");
        }
    }),
    /** @since 13 */
    create_extension_opt_list(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Temporary work around as the "from version" in the create extension statement is still known as
                     * an option but is not supported any more. A syntax error will be given, but the grammar now
                     * attempts to add a String to a List<DefElem>, which will not succeed of course.
                     */
                    .replace("{ $$ = lappend($1, $2); }",
                            "{ if (yystack.valueAt (0) instanceof DefElem) { $$ = lappend($1, $2); } }");
        }
    }),
    /** @since 14 */
    joined_table(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line
                    /*
                     * Repair some odd casts
                     */
                    .replaceAll("linitial_node\\(List, castNode\\(List\\.class, (\\$\\d)\\)\\)",
                            "(List)((List)$1).get(0)")
                    .replaceAll("lsecond_node\\(Alias, castNode\\(List\\.class, (\\$\\d)\\)\\)",
                            "(Alias)((List)$1).get(1)");
        }
    }),
    /** @since 14 */
    PLpgSQL_Expr(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line.replace("if ($9)", "if ($9 != null)").replace("if (!n->sortClause", "if (n.sortClause == null");
        }
    }),
    /** @since Postgres 15 */
    key_actions(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line.replaceAll("n->(\\w+)\\s*=\\s*palloc\\(sizeof\\((\\w+)\\)\\);", "n.$1 = new $2();");
        }
    }),
    /** @since Postgres 15 */
    NumericOnly(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line.replaceAll("(\\s+)Float(\\s+)", "$1Value$2");
        }
    }),
    /** @since Postgres 15 */
    key_update(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line.replace("->cols", "->cols != null");
        }
    }),
    /** @since Postgres 15 */
    PublicationObjSpec(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line.replace("if ($2 || $3)", "if ($2 != null || $3 != null)")
                    .replace("$$->name", "((PublicationObjSpec)$$).name")
                    .replace("$$->location", "((PublicationObjSpec)$$).location");
        }
    }),
    /** @since Postgres 15 */
    extended_relation_expr(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line.replace("$$->inh", "((RangeVar)$$).inh").replace("$$->alias", "((RangeVar)$$).alias");
        }
    }),
    /** @since Postgres 15 */
    zone_value(new GrammarRuleSpecialProcessing() {
        public String processLine(String line) {
            return line.replace(".ival.ival", ".val.ival");
        }
    }),
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
