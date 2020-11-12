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

package com.splendiddata.sqlparser;

import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.splendiddata.sqlparser.enums.A_Expr_Kind;
import com.splendiddata.sqlparser.enums.AttributeIdentity;
import com.splendiddata.sqlparser.enums.BoolExprType;
import com.splendiddata.sqlparser.enums.DefElemAction;
import com.splendiddata.sqlparser.enums.GroupingSetKind;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.RoleSpecType;
import com.splendiddata.sqlparser.enums.Severity;
import com.splendiddata.sqlparser.enums.TemporalWord;
import com.splendiddata.sqlparser.plumming.base_yy_extra_type;
import com.splendiddata.sqlparser.structure.A_Const;
import com.splendiddata.sqlparser.structure.A_Expr;
import com.splendiddata.sqlparser.structure.A_Indirection;
import com.splendiddata.sqlparser.structure.Alias;
import com.splendiddata.sqlparser.structure.BoolExpr;
import com.splendiddata.sqlparser.structure.CollateClause;
import com.splendiddata.sqlparser.structure.ColumnDef;
import com.splendiddata.sqlparser.structure.ColumnRef;
import com.splendiddata.sqlparser.structure.Constraint;
import com.splendiddata.sqlparser.structure.CreateDomainStmt;
import com.splendiddata.sqlparser.structure.CreateTrigStmt;
import com.splendiddata.sqlparser.structure.DefElem;
import com.splendiddata.sqlparser.structure.ErrCode;
import com.splendiddata.sqlparser.structure.FuncCall;
import com.splendiddata.sqlparser.structure.GroupingSet;
import com.splendiddata.sqlparser.structure.List;
import com.splendiddata.sqlparser.structure.ListCell;
import com.splendiddata.sqlparser.structure.Location;
import com.splendiddata.sqlparser.structure.Node;
import com.splendiddata.sqlparser.structure.Oid;
import com.splendiddata.sqlparser.structure.RangeVar;
import com.splendiddata.sqlparser.structure.RoleSpec;
import com.splendiddata.sqlparser.structure.TypeCast;
import com.splendiddata.sqlparser.structure.TypeName;
import com.splendiddata.sqlparser.structure.VacuumRelation;
import com.splendiddata.sqlparser.structure.Value;
import com.splendiddata.sqlparser.structure.core_yyscan_t;

/**
 * Just a base class for the PgSqlParser, implementing some functions that are not generated.
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public class AbstractParser extends AbstractCProgram {
    /*
     * obtained from /postgresql-9.3.4/src/backend/parser/gram.y
     */
    static final int CAS_NOT_DEFERRABLE = 0x01;
    static final int CAS_DEFERRABLE = 0x02;
    static final int CAS_INITIALLY_IMMEDIATE = 0x04;
    static final int CAS_INITIALLY_DEFERRED = 0x08;
    static final int CAS_NOT_VALID = 0x10;
    static final int CAS_NO_INHERIT = 0x20;

    private static final Logger log = LogManager.getLogger(AbstractParser.class);
    private base_yy_extra_type yyExtraType = new base_yy_extra_type();

    /**
     * Creates an integer value
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/nodes/value.c
     * </p>
     *
     * @param i
     *            The integer value of which to create an IntegerValue
     * @return Value The created value
     */
    static Value makeInteger(long i) {
        Value v = new Value();

        v.type = NodeTag.T_Integer;
        v.val.ival = (int) i;
        return v;
    }

    /**
     * Creates an integer value from the boolean. In C integers and booleans are the same (well, in fact in Java as
     * well, but we don't want to know). This method makes a one for true and zero for false.
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/nodes/value.c
     * </p>
     *
     * @param b
     *            The boolean content of the new Value
     * @return Value The value containing the boolean content (as integer, 0 = false, not 0 is true)
     */
    static Value makeInteger(boolean b) {
        Value v = new Value();

        v.type = NodeTag.T_Integer;
        v.val.ival = b ? 1 : 0;
        return v;
    }

    /**
     * Creates a T_Integer Value with the character value of the AttributeIdentity
     *
     * @param attrIdentity
     *            of which VALUE will be taken to use as int value
     * @return Value The value containing the character value as int
     */
    static Value makeInteger(AttributeIdentity attrIdentity) {
        Value v = new Value();

        v.type = NodeTag.T_Integer;
        v.val.ival = attrIdentity.VALUE;
        return v;
    }

    /**
     * In C pointers and integers are the same. So to create a constant with no location, the C code invokes the method
     * as makeIntConst(val, -1). In Java the -1 will be translated into null
     *
     * @param val
     * @param location
     *            - ignored
     * @return A_Const containing the value
     */
    static A_Const makeIntConst(int val, int location) {
        A_Const n = makeNode(A_Const.class);

        n.val.type = NodeTag.T_Integer;
        n.val.val.ival = val;

        return n;
    }

    /**
     * Integers can be seen as booleans in C. Java doesn't have that feature so this extra method is made. The intended
     * method was {@link com.splendiddata.sqlparser.SqlParser#makeIntConst(int, Location)}.
     *
     * @param val
     *            The boolean that will be put into the ival of the returned A_Const's value
     * @param location
     *            The location of the node in the source file
     * @return A_Const The constant with a val with an ival of 0 or 1
     */
    static A_Const makeIntConst(boolean val, Location location) {
        A_Const n = makeNode(A_Const.class);

        n.val.type = NodeTag.T_Integer;
        n.val.val.ival = val ? 1 : 0;
        n.location = location;

        return n;
    }

    /**
     * Instantiate an object with the specified class
     * 
     * @param<_T> The
     *                return type
     * 
     * @param clss
     *            The class for which to create an instance
     * @return &lt;_T&gt; An instance of the specified class
     */
    static <T extends Node> T makeNode(Class<T> clss) {

        try {
            return clss.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            log.error("makeNode(class=" + clss + ")", e);
            return null;
        }
    }

    /**
     * Replacement for a macro that just does a type cast in C
     *
     * @param <T>
     *            The class to cast to
     * @param clss
     *            The class to which to cast
     * @param object
     *            The object to cast
     * @return &lt;_T&gt; The "result" of the type cast
     */
    @SuppressWarnings("unchecked")
    static <T extends Node> T castNode(Class<T> clss, Object object) {
        return (T) object;
    }

    /**
     * Creates a boolean expression
     *
     * @param boolop
     *            Boolean expression type
     * @param args
     *            Arguments
     * @param location
     *            The location of the statement
     * @return BoolExpr The constructed BoolExpr
     */
    static BoolExpr makeBoolExpr(BoolExprType boolop, List<Node> args, Location location) {
        BoolExpr result = makeNode(BoolExpr.class);
        result.boolop = boolop;
        result.args = args;
        result.location = location;
        return result;
    }

    /**
     * Creates a FuncCall object
     * <p>
     * Copied from /postgresql-9.4.1/src/backend/nodes/makefuncs.c
     * </p>
     * 
     * @param name
     *            The List&lt;Value&gt; that represents the function's (qualified) name
     * @param args
     *            The List&lt;Node&gt; that represents the arguments of the function call
     * @param locationAt
     *            The Location of the node in the source file
     * @return FuncCall The constructed FuncCall
     */
    static FuncCall makeFuncCall(List<Value> name, List<Node> args, Location locationAt) {
        FuncCall n = makeNode(FuncCall.class);

        n.funcname = name;
        n.args = args;
        n.agg_order = null;
        n.agg_filter = null;
        n.agg_within_group = false;
        n.agg_star = false;
        n.agg_distinct = false;
        n.func_variadic = false;
        n.over = null;
        n.location = locationAt;
        return n;
    }

    /**
     * Appends the node to the list. A new List is created if list is null. The (modified) list is returned.
     * <p>
     * copied from /postgresql-9.3.4/src/backend/nodes/list.c
     * </p>
     * 
     * @param list
     *            The list that is to be appended or null if a new List is to be created
     * @param value
     *            The value that is to be appended to the list
     * @return The list with the value appended.
     */
    static <T> List<T> lappend(List<T> list, T value) {
        List<T> result;
        if (list == null) {
            result = new List<>();
        } else {
            result = list;
        }
        result.add(value);
        return result;
    }

    /**
     * Just returns the argument. This is nonsense of course, but the parser demands it
     *
     * @param locationAt
     *            The location that is to be returned
     * @return Location the locationAt
     */
    static Location parser_errposition(Location locationAt) {
        return locationAt;
    }

    /**
     * Return a shallow copy of the specified list, without the first N elements.
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/nodes/list.c
     * <p>
     *
     * @param oldlist
     * @param nskip
     * @return List<T>
     */
    @SuppressWarnings("unused")
    static <T> List<T> list_copy_tail(List<T> oldlist, int nskip) {
        List<T> newlist;
        ListCell<T> newlist_prev;
        ListCell<T> oldlist_cur;

        if (nskip < 0) {
            nskip = 0; /* would it be better to elog? */
        }

        if (oldlist == null || nskip >= oldlist.size()) {
            return null;
        }

        newlist = new List<>();
        newlist.type = oldlist.type;

        /*
         * Skip over the unwanted elements.
         */
        oldlist_cur = oldlist.getHead();
        while (nskip-- > 0) {
            oldlist_cur = oldlist_cur.next;
        }

        /*
         * And copy the rest
         */
        for (; oldlist_cur != null; oldlist_cur = oldlist_cur.next) {
            newlist.add(oldlist_cur.data);
        }

        return newlist;
    }

    /**
     * Creates a List. If value is not null, it will be added as first cell
     * 
     * @param <T>
     *            the element type
     * @param value
     *            content for the first cell
     * @return List a new List with one cell, containing the value
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    static <T> List<T> list_make1(T value) {
        List theList = new List();
        theList.type = NodeTag.T_List;
        if (value != null) {
            theList.add(value);
        }
        return theList;
    }

    /**
     * Creates a List and adds the two values
     *
     * @param value1
     * @param value2
     * @return List a new List with one cell, containing the values
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    static List list_make2(Object value1, Object value2) {
        List theList = new List();
        theList.type = NodeTag.T_List;
        theList.add(value1);
        theList.add(value2);
        return theList;
    }

    /**
     * Creates a List and adds the two values
     *
     * @param value1
     * @param value2
     * @param value3
     * @return List a new List with one cell, containing the values
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    static List list_make3(Object value1, Object value2, Object value3) {
        List theList = new List();
        theList.type = NodeTag.T_List;
        theList.add(value1);
        theList.add(value2);
        theList.add(value3);
        return theList;
    }

    /**
     * Creates a List and adds the two values
     *
     * @param value1
     * @param value2
     * @param value3
     * @param value4
     * @return List a new List with one cell, containing the values
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    static List list_make4(Object value1, Object value2, Object value3, Object value4) {
        List theList = new List();
        theList.type = NodeTag.T_List;
        theList.add(value1);
        theList.add(value2);
        theList.add(value3);
        theList.add(value4);
        return theList;
    }

    /**
     * TODO Please insert some explanation here
     *
     * @param names2
     * @return
     */
    @SuppressWarnings("rawtypes")
    static Object lthird(List names2) {
        throw new UnsupportedOperationException();
    }

    /**
     * Truncates the list to the specified size. Returns null if the list becomes empty
     * 
     * @see com.splendiddata.sqlparser.structure.List#list_truncate(int)
     *
     * @param list
     *            The list to reduce
     * @param nRemainingEntries
     *            The number of entries that should remain in the list
     * @return List<T> list or null if the list becomes empty
     */
    static <T> List<T> list_truncate(List<T> list, int nRemainingEntries) {
        return list.list_truncate(nRemainingEntries);
    }

    /**
     * Returns the value of the first list cell
     *
     * @param list
     *            The list that will provide its first element. May be null.
     * @return Object whatever is in the first cell's data or null if there isn't a first cell
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    static <_T extends Object> _T linitial(List list) {
        if (list.isEmpty()) {
            return null;
        }
        return (_T) list.get(0);
    }

    /**
     * Returns the lenght of a list
     *
     * @param list
     * @return int The number of elements in the list
     */
    static int list_length(List<?> list) {
        return list.size();
    }

    /**
     * makeTypeNameFromNameList - build a TypeName node for a String list representing a qualified name.
     * <p>
     * typmod is defaulted, but can be changed later by caller.
     * </p>
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/nodes/makefuncs.c
     * </p>
     * 
     * @param names
     *            The list of nodes that will be used as (qualified) name
     * @return TypeName with the names List in it's names property
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    static TypeName makeTypeNameFromNameList(List names) {
        TypeName n = new TypeName();
        n.names = names;
        return n;
    }

    /**
     * Creates a String Value from the specified string
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/nodes/value.c
     * </p>
     * 
     * @param string
     *            The text for the newly created Value
     * @return Value A new Value with type {@link com.splendiddata.sqlparser.enums.NodeTag#T_String}
     */
    static Value makeString(String string) {
        Value value = new Value();
        value.type = NodeTag.T_String;
        value.val.str = string;
        return value;
    }

    /**
     * Adds the node as first in the list. If list is null, a new List will be created. Otherwise the node will just be
     * added to the existing list
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/nodes/list.c
     * </p>
     *
     * @param node
     *            The node to add to the front of the list
     * @param list
     *            The list where to the node is to be added or null if a new List is desired.
     * @return List the list argument with the node added to it's front or a new list with just the node if the list
     *         argument was null.
     */
    @SuppressWarnings("unchecked")
    static <T extends Node> List<T> lcons(Node node, List<T> list) {
        List<T> result;
        if (list == null) {
            result = new List<>();
        } else {
            result = list;
        }
        result.add(0, (T) node);
        return result;
    }

    /**
     * Creates a Value with type T_Float and with the numericStr stored in Value.val.str
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/nodes/value.c
     * </p>
     *
     * @param numericStr
     * @return Value with the float value in String format.
     */
    static Value makeFloat(String numericStr) {
        Value v = new Value();

        v.type = NodeTag.T_Float;
        v.val.str = numericStr;
        return v;
    }

    /**
     * Returns the content of the second list cell
     * <p>
     * See /postgresql-9.3.4/src/include/nodes/pg_list.h
     * </p>
     *
     * @param <_T>
     *            the type of list element to return
     * @param list
     *            May be null
     * @return T the content of the second list cell or null if there isn't any
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    static <_T extends Object> _T lsecond(List list) {
        if (list == null || list.size() < 2) {
            return null;
        }
        return (_T) list.get(1);
    }

    /**
     * Extracts the integer value from linitial
     *
     * @param linitial
     *            The Value of which the val.ival will be returned
     * @return int linitial.val.val.ival;
     */
    static int intVal(Value linitial) {
        return linitial.val.ival;
    }

    /**
     * Adds addFrom to addTo and returns addTo. If one of the lists is null, the non-null list will be returned. If they
     * both are null, null will be returned.
     * <p>
     * loosely copied from /postgresql-9.3.4/src/backend/nodes/list.c
     * </p>
     *
     * @param addTo
     *            List to which addFrom will be added and which will be returned if both lists are not null
     * @param addFrom
     *            List from which all entries will be copied to addTo. If addTo is null, addFrom will be returned
     *            unchanged.
     * @return The list containing the sum of the two arguments or null of both arguments are null
     */
    static <T> List<T> list_concat(List<T> addTo, List<T> addFrom) {
        if (addTo == null) {
            return addFrom;
        }
        if (addFrom == null) {
            return addTo;
        }
        addTo.addAll(addFrom);
        return addTo;
    }

    /**
     * Just returns the keyword
     *
     * @param keyword
     * @return String keyword
     */
    static String pstrdup(String keyword) {
        return keyword;
    }

    /**
     * Just logs the string and throws an UnsupportedOperationException
     *
     * @param string
     *            The message to log
     * @throws UnsupportedOperationException
     *             in all cases
     */
    static void parser_yyerror(String string) {
        log.error("parser_yyerror(string=" + string + ")");
        throw new UnsupportedOperationException();

    }

    /**
     * Sets the deferrable, initdeferred, skip_validation and / or is_no_inherit booleans in the Constraint depending on
     * the content of the cas_bits integer.
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/parser/gram.c
     * </p>
     *
     * @param constraint
     *            The Constraint in which to set the deferrable, initdeferred, skip_validation and / or is_no_inherit
     * @param cas_bits
     *            Integer of witch to copy the CAS_DEFERRABLE, CAS_INITIALLY_DEFERRED, CAS_NOT_VALID and CAS_NO_INHERIT
     *            bit state
     * @param location
     * @param constrType
     *            Not used - leftover from a C implementation
     * @param deferrable
     *            Not used - leftover from a C implementation
     * @param initdeferred
     *            Not used - leftover from a C implementation
     * @param not_valid
     *            Not used - leftover from a C implementation
     * @param no_inherit
     *            Not used - leftover from a C implementation
     * @param yyscanner
     *            Not used - leftover from a C implementation
     */
    static void processCASbits(Constraint constraint, int cas_bits, Location location, String constrType,
            Boolean deferrable, Boolean initdeferred, Boolean not_valid, Boolean no_inherit, core_yyscan_t yyscanner) {
        if (log.isTraceEnabled()) {
            log.trace(new StringBuilder("@>processCASbits(constraint=").append(constraint).append(", cas_bits=")
                    .append(cas_bits).append(", location=").append(location).append(", constrType=").append(constrType)
                    .append(")").toString());
        }

        constraint.deferrable = ((cas_bits & (PgSqlParser.CAS_DEFERRABLE | PgSqlParser.CAS_INITIALLY_DEFERRED)) != 0);
        constraint.initdeferred = ((cas_bits & PgSqlParser.CAS_INITIALLY_DEFERRED) != 0);
        constraint.skip_validation = ((cas_bits & PgSqlParser.CAS_NOT_VALID) != 0);
        constraint.is_no_inherit = ((cas_bits & PgSqlParser.CAS_NO_INHERIT) != 0);
    }

    /**
     * Process result of ConstraintAttributeSpec, and set appropriate bool flags in the output command node. Pass NULL
     * for any flags the particular command doesn't support.
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/parser/gram.c
     * </p>
     *
     * @param createTriggerStmt
     * @param cas_bits
     * @param location
     * @param constrType
     *            Not used - leftover from a C implementation
     * @param deferrable
     *            Not used - leftover from a C implementation
     * @param initdeferred
     *            Not used - leftover from a C implementation
     * @param not_valid
     *            Not used - leftover from a C implementation
     * @param no_inherit
     *            Not used - leftover from a C implementation
     * @param yyscanner
     *            Not used - leftover from a C implementation
     */
    static void processCASbits(CreateTrigStmt createTriggerStmt, int cas_bits, Location location, String constrType,
            Boolean deferrable, Boolean initdeferred, Boolean not_valid, Boolean no_inherit, core_yyscan_t yyscanner) {
        if (log.isTraceEnabled()) {
            log.trace(new StringBuilder("@>processCASbits(createTriggerStmt=").append(createTriggerStmt)
                    .append(", cas_bits=").append(cas_bits).append(", location=").append(location)
                    .append(", constrType=").append(")").toString());
        }

        createTriggerStmt.deferrable = ((cas_bits
                & (PgSqlParser.CAS_DEFERRABLE | PgSqlParser.CAS_INITIALLY_DEFERRED)) != 0);
        createTriggerStmt.initdeferred = ((cas_bits & PgSqlParser.CAS_INITIALLY_DEFERRED) != 0);
    }

    /**
     * Returns yyExtraType
     *
     * @param yyscanner2
     *            unused
     * @return base_yy_extra_type yyExtraType
     */
    base_yy_extra_type pg_yyget_extra(core_yyscan_t yyscanner2) {
        return yyExtraType;
    }

    /**
     * Returns the parse result after invocation of parse()
     *
     * @return List the parsetree
     */
    public List<Node> getResult() {
        return yyExtraType.parsetree;
    }

    /**
     * Creates a TypeName
     *
     * @param name
     *            The name to be wrapped into a TypeName object
     * @return TypeName with the (unqualified) provided name
     */
    static TypeName makeTypeName(String name) {
        Value v = new Value();
        v.type = NodeTag.T_String;
        v.val.str = name;

        TypeName tn = new TypeName();
        tn.names = new List<>();
        tn.names.add(v);
        return tn;
    }

    /**
     * Returns the cell's data pointer
     *
     * @param cell
     * @return Object whatever is in the cell's data
     */
    static <T> T lfirst(ListCell<T> cell) {
        return cell.data;
    }

    /**
     * Returns the defined CONSTANT of the timeword
     *
     * @param timeWord
     * @return int
     */
    static int INTERVAL_MASK(TemporalWord timeWord) {
        return timeWord.getConstant();
    }

    /**
     * Creates an A_Expr
     *
     * @param kind
     *            The expression kind
     * @param name
     *            to be put into a List as T_String Value as name
     * @param lexpr
     *            The left expression
     * @param rexpr
     *            The right expression
     * @param location
     *            Position within the statement
     * @return A_Expr just created
     */
    static A_Expr makeSimpleA_Expr(A_Expr_Kind kind, String name, Node lexpr, Node rexpr, Location location) {
        A_Expr a = new A_Expr();
        a.kind = kind;
        a.name = list_make1(makeString(name));
        a.lexpr = lexpr;
        a.rexpr = rexpr;
        a.location = location;
        return a;
    }

    /**
     * Creates a TypeCast with no location.
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/parser/gram.c
     * <p>
     *
     * @param arg
     * @param typename
     * @param ignoredLocation
     *            Ignored
     * @return TypeCast
     */
    static Node makeTypeCast(Node arg, TypeName typename, int ignoredLocation) {
        TypeCast n = new TypeCast();
        n.arg = arg;
        n.typeName = typename;

        return n;
    }

    /**
     * Creates an A_Expr
     * <p>
     * copied from /postgresql-9.3.4/src/backend/nodes/makefuncs.c
     * </p>
     *
     * @param kind
     * @param name
     * @param lexpr
     * @param rexpr
     * @param location
     * @return A_Expr
     */
    static A_Expr makeA_Expr(A_Expr_Kind kind, List<Value> name, Node lexpr, Node rexpr, Location location) {
        A_Expr a = new A_Expr();

        a.kind = kind;
        a.name = name;
        a.lexpr = lexpr;
        a.rexpr = rexpr;
        a.location = location;
        return a;
    }

    /**
     * Creates a RangeVar with the specified arguments
     *
     * @param schemaName
     * @param relName
     * @param location
     * @return RangeVar
     */
    static RangeVar makeRangeVar(String schemaName, String relName, Location location) {
        RangeVar result = new RangeVar();
        result.schemaname = schemaName;
        result.relname = relName;
        result.location = location;
        return result;
    }

    /**
     * Returns a period separated String from the list
     *
     * @param list
     * @return String A period separated representation of the content of the list.
     */
    static String NameListToString(List<?> list) {
        StringBuilder result = new StringBuilder();
        String separator = "";
        for (Object obj : list) {
            result.append(separator).append(obj);
            separator = ".";
        }
        return result.toString();
    }

    /**
     * Returns the data entry from the last element in the list
     *
     * @param list
     * @return T The value of the last entry in then list or null if list is null or empty
     */
    static <T> T llast(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.getTail().data;
    }

    /**
     * Returns src.toString() or null if src is null
     *
     * @param src
     * @return String src.toString()
     */
    static String strVal(Object src) {
        if (src == null) {
            return null;
        }
        return src.toString();
    }

    /**
     * Returns the next ListCell in the list.
     *
     * @param col_cell
     * @return ListCell<T> the next ListCell or null if there isn't any
     */
    static <T> ListCell<T> lnext(ListCell<T> col_cell) {
        return col_cell.next;
    }

    /**
     * Returns the next ListCell in the list.
     * 
     * @param lst
     *            Unused: The list in which the next cell should reside
     * @param col_cell
     *            The ListCell that has a pointer to the next cell (or null if there isn't any)
     * @return ListCell<T> the next ListCell or null if there isn't any
     * @since 8.0 - Postgres version 13
     */
    static <T> ListCell<T> lnext(List<T> lst, ListCell<T> col_cell) {
        return col_cell.next;
    }

    /**
     * Returns the first cell of the list (if any)
     *
     * @param list
     * @return ListCell The first cell of the list or null if there isn't any
     */
    static <T> ListCell<T> list_head(List<T> list) {
        if (list == null) {
            return null;
        }
        return list.getHead();
    }

    /**
     * Integers and pointer can be intermixed in C. If no location is available, then the parser tries to out -1 into
     * it. Of course this cannot happen in Java, so this method stands in for
     * {@link com.splendiddata.sqlparser.SqlParser#makeNullAConst(Location)}
     *
     * @param location
     * @return
     */
    static Node makeNullAConst(int location) {
        A_Const n = makeNode(A_Const.class);

        n.val.type = NodeTag.T_Null;
        n.location = null;

        return n;
    }

    /**
     * Copies the TypeName
     *
     * @param toCopy
     *            The TypeName to copy
     * @return TypeName A clone of toCopy
     */
    static TypeName copyObject(TypeName toCopy) {
        return toCopy.clone();
    }

    /**
     * Just returns the location of the node
     *
     * @param limitOffset
     * @return Location
     */
    static Location exprLocation(Node node) {
        return node.location;
    }

    /**
     * Creates a RoleSpec with a null location
     *
     * @param type
     *            The type of role spec
     * @param nullLocation
     *            unused invalid loaction
     * @return RoleSpec The jsut created RoleSpec
     */
    static RoleSpec makeRoleSpec(RoleSpecType type, int nullLocation) {
        RoleSpec spec = makeNode(RoleSpec.class);
        spec.roletype = type;
        return spec;
    }

    /**
     * Creates a GroupingSet
     *
     * @param kind
     *            The kind of GroupingSet
     * @param content
     *            The content of the GroupingSet
     * @param location
     *            the location of the GroupingSet
     * @return GroupingSet the just created GroupingSet
     */
    static GroupingSet makeGroupingSet(GroupingSetKind kind, List<Node> content, Location location) {
        GroupingSet n = makeNode(GroupingSet.class);

        n.kind = kind;
        n.content = content;
        n.location = location;
        return n;
    }

    /**
     * Copied from /postgresql-9.3.4/src/backend/parser/gram.c
     *
     * @param colname
     *            The simple name of the column reference
     * @param indirection
     *            A List&lt;Node&gt representing the indirection. If indirection is null or empty, then a ColumnRef will
     *            be returned. Otherwise an A_Indirection.
     * @param location
     *            The location of the node in the source file
     * @param yyscanner
     *            unused - but the generated parser wants this field in
     * @return Node may be a ColumnRef or an A_Indirection
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    Node makeColumnRef(String colname, List<Node> indirection, Location location, int yyscanner) {
        /*
         * Generate a ColumnRef node, with an A_Indirection node added if there is any subscripting in the specified
         * indirection list. However, any field selection at the start of the indirection list must be transposed into
         * the "fields" part of the ColumnRef node.
         */
        ColumnRef c = new ColumnRef();
        int nfields = 0;

        c.location = location;
        for (ListCell<Node> l = list_head(indirection); (l) != null; (l) = lnext(l))

        {
            if (isA(lfirst(l), NodeTag.T_A_Indices)) {
                A_Indirection i = new A_Indirection();

                if (nfields == 0) {
                    /* easy case - all indirection goes to A_Indirection */
                    c.fields = list_make1(makeString(colname));
                    i.indirection = (List) check_indirection(indirection, null);
                } else {
                    /* got to split the list in two */
                    i.indirection = (List) check_indirection(list_copy_tail(indirection, nfields), null);
                    indirection = list_truncate(indirection, nfields);
                    c.fields = lcons(makeString(colname), indirection);
                }
                i.arg = c;
                return i;
            } else if (isA(lfirst(l), NodeTag.T_A_Star)) {
                /* We only allow '*' at the end of a ColumnRef */
                if (lnext(l) != null) {
                    parser_yyerror("improper use of \"*\"");
                }
            }
            nfields++;
        }
        /* No subscripting, so all indirection gets added to field list */
        c.fields = lcons(makeString(colname), indirection);
        return c;
    }

    /**
     * check_indirection --- check the result of indirection production
     * <p>
     * We only allow '*' at the end of the list, but it's hard to enforce that in the grammar, so do it here.
     * </p>
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/parser/gram.c
     * </p>
     * 
     * @param indirection
     *            The List&lt;T&gt; to check
     * @param yyscanner
     *            Not used, but the generated parser wants this in
     * @return List&lt;T&gt; The provided indirection
     */
    <T extends Node> List<T> check_indirection(List<T> indirection, core_yyscan_t yyscanner) {

        for (ListCell<T> l = list_head(indirection); (l) != null; (l) = lnext(l)) {
            if (isA(lfirst(l), NodeTag.T_A_Star)) {
                if (lnext(l) != null) {
                    parser_yyerror("improper use of \"*\"");
                }
            }
        }
        return indirection;
    }

    /**
     * Checks if the specified node has the specified tag
     *
     * @param node
     * @param tag
     * @return boolean true if tag.equals(node.type)
     */
    static boolean isA(Object node, NodeTag tag) {
        if (node instanceof Node) {
            return tag.equals(((Node) node).type);
        }
        return false;
    }

    /**
     * Test for equality
     *
     * @param o1
     * @param o2
     * @return boolean if o1 and o2 are equal or bth null
     */
    static boolean equal(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }
        return o1.equals(o2);
    }

    /**
     * Creates an Alias node
     *
     * @param aliasname
     *            Name of the alias
     * @param colnames
     *            Column names (if any)
     * @return Alias the created Alias
     */
    static Alias makeAlias(String aliasname, List<Value> colnames) {
        Alias result = makeNode(Alias.class);
        result.aliasname = aliasname;
        result.colnames = colnames;
        return result;
    }

    /**
     * Separate Constraint nodes from COLLATE clauses in a qualList and copy them to the stmt
     *
     * @param qualList
     *            The list to split
     * @param stmt
     *            The statement in which the {@link com.splendiddata.sqlparser.structure.CreateDomainStmt#constraints}
     *            and {@link com.splendiddata.sqlparser.structure.CreateDomainStmt#collClause} will be filled if data is
     *            present for them in the qualList
     * @param yyscanner
     *            Unused
     */
    protected final void splitColQualList(List<Node> qualList, CreateDomainStmt stmt, core_yyscan_t yyscanner) {
        if (qualList == null) {
            return;
        }

        for (Node n : qualList) {
            if (NodeTag.T_Constraint.equals(n.type)) {
                if (stmt.constraints == null) {
                    stmt.constraints = new List<>();
                }
                stmt.constraints.add(n);
            } else if (NodeTag.T_CollateClause.equals(n.type)) {

                if (stmt.collClause != null)
                    ereport(Severity.ERROR, ErrCode.ERRCODE_SYNTAX_ERROR,
                            errmsg("multiple COLLATE clauses not allowed"), parser_errposition(n.location));
                stmt.collClause = (CollateClause) n;
            } else {
                log.error(new StringBuffer().append("unexpected node type ").append(n.type)
                        .append(" in splitColQualList(qualList").append(ParserUtil.toSqlTextString(qualList))
                        .append(", stmt=").append(ParserUtil.toSqlTextString(stmt)).append(")"));
            }
        }
    }

    /**
     * Separate Constraint nodes from COLLATE clauses in a qualList and copy them to the stmt
     *
     * @param qualList
     *            The list to split
     * @param columnDef
     *            The columnDef in which the {@link com.splendiddata.sqlparser.structure.CreateDomainStmt#constraints}
     *            and {@link com.splendiddata.sqlparser.structure.CreateDomainStmt#collClause} will be filled if data is
     *            present for them in the qualList
     * @param yyscanner
     *            Unused
     */
    protected final void splitColQualList(List<Node> qualList, ColumnDef columnDef, core_yyscan_t yyscanner) {
        if (qualList == null) {
            return;
        }

        for (Node n : qualList) {
            if (NodeTag.T_Constraint.equals(n.type)) {
                if (columnDef.constraints == null) {
                    columnDef.constraints = new List<>();
                }
                columnDef.constraints.add((Constraint) n);
            } else if (NodeTag.T_CollateClause.equals(n.type)) {

                if (columnDef.collClause != null)
                    ereport(Severity.ERROR, ErrCode.ERRCODE_SYNTAX_ERROR,
                            errmsg("multiple COLLATE clauses not allowed"), parser_errposition(n.location));
                columnDef.collClause = (CollateClause) n;
            } else {
                log.error(new StringBuffer().append("unexpected node type ").append(n.type)
                        .append(" in splitColQualList(qualList").append(ParserUtil.toSqlTextString(qualList))
                        .append(", columnDef=").append(ParserUtil.toSqlTextString(columnDef)).append(")"));
            }
        }
    }

    /**
     * makeDefElem - build a DefElem node
     * <p>
     * This is sufficient for the "typical" case with an unqualified option name and no special action.
     * </p>
     * <p>
     * Copied from /postgresql-10rc1/src/backend/nodes/makefuncs.c
     * </p>
     * 
     * @param name
     *            The defname
     * @param arg
     *            The argument (if any)
     * @param Location
     *            the location within the query
     * @return DefElem created from the arguments
     * @since 5.0
     */
    protected DefElem makeDefElem(String name, Node arg, Location location) {
        DefElem res = new DefElem();

        res.defname = name;
        res.arg = arg;
        res.defaction = DefElemAction.DEFELEM_UNSPEC;
        res.location = location;

        return res;
    }

    /**
     * makeDefElemExtended - build a DefElem node with all fields available to be specified
     * <p>
     * Copied from /postgresql-10rc1/src/backend/nodes/makefuncs.c
     * </p>
     * 
     * @param nameSpace
     *            The name space for the def element
     * @param name
     *            The name for the def element
     * @param arg
     *            The argument node
     * @param defaction
     *            DefElemAction
     * @param Location
     *            the location within the query
     * @return DefElem freshly created
     * @since 5.0
     */
    protected DefElem makeDefElemExtended(String nameSpace, String name, Node arg, DefElemAction defaction,
            Location location) {
        DefElem res = new DefElem();

        res.defnamespace = nameSpace;
        res.defname = name;
        res.arg = arg;
        res.defaction = defaction;
        res.location = location;

        return res;
    }

    /**
     * Extract an int32 value from a DefElem.
     * <p>
     * Copied from postgresql-11beta4/src/backend/commands/define.c
     * </p>
     * 
     * @param def
     *            The DefElem to extract an integer from
     * @return int The integer value of the def
     * @since 6.0 - Postgres version 11
     */
    protected int defGetInt32(DefElem def) {
        if (def.arg == null) {
            ereport(Severity.ERROR, ErrCode.ERRCODE_SYNTAX_ERROR, errmsg(def.defname + " requires an integer value"),
                    parser_errposition(def.location));
        }
        switch (def.arg.type) {
        case T_Integer:
            return intVal((Value) def.arg);
        default:
            ereport(Severity.ERROR, ErrCode.ERRCODE_SYNTAX_ERROR, errmsg(def.defname + " requires an integer value"),
                    parser_errposition(def.location));
        }
        return 0; /* keep compiler quiet */
    }

    /**
     * makeVacuumRelation - create a VacuumRelation node
     * <p>
     * Copied from postgresql-11beta4/src/backend/nodes/makefuncs.c
     * </p>
     * 
     * @param relation
     *            The RangeVar for which to make this VacuumRelation
     * @param oid
     *            Some Oid, probably the one that would identify the relation, but that shouldn't be part of the parser.
     *            Not used here.
     * @param va_cols
     *            List of column names
     * @return VacuumRelation freshly created
     * @since 6.0 - Postgres version 11
     */
    protected VacuumRelation makeVacuumRelation(RangeVar relation, Oid oid, List<Value> va_cols) {
        VacuumRelation v = new VacuumRelation();

        v.relation = relation;
        v.va_cols = va_cols;
        return v;
    }
}
