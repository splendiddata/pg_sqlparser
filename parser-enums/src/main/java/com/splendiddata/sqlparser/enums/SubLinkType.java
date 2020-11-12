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

package com.splendiddata.sqlparser.enums;

/**
 * <p>
 * SubLink
 * </p>
 * <table>
 * <caption style="text-align:left">A SubLink represents a subselect appearing in an expression, and in some cases also
 * the combining operator(s) just above it. The subLinkType indicates the form of the expression represented:</caption>
 * <tr>
 * <th style="text-align:left">sublink type</th>
 * <th style="text-align:left">expression represented</th>
 * </tr>
 * <tr>
 * <td>EXISTS_SUBLINK</td>
 * <td>EXISTS(SELECT ...)</td>
 * </tr>
 * <tr>
 * <td>ALL_SUBLINK</td>
 * <td>(lefthand) op ALL (SELECT ...)</td>
 * </tr>
 * <tr>
 * <td>ANY_SUBLINK</td>
 * <td>(lefthand) op ANY (SELECT ...)</td>
 * </tr>
 * <tr>
 * <td>ROWCOMPARE_SUBLINK</td>
 * <td>(lefthand) op (SELECT ...)</td>
 * </tr>
 * <tr>
 * <td>EXPR_SUBLINK</td>
 * <td>(SELECT with single targetlist item ...)</td>
 * </tr>
 * <tr>
 * <td>MULTIEXPR_SUBLINK</td>
 * <td>(SELECT with multiple targetlist items ...)</td>
 * </tr>
 * <tr>
 * <td>ARRAY_SUBLINK</td>
 * <td>ARRAY(SELECT with single targetlist item ...)</td>
 * </tr>
 * <tr>
 * <td>CTE_SUBLINK</td>
 * <td>WITH query (never actually part of an expression)</td>
 * </tr>
 * </table>
 * <p>
 * For ALL, ANY, and ROWCOMPARE, the lefthand is a list of expressions of the same length as the subselect's targetlist.
 * ROWCOMPARE will *always* have a list with more than one entry; if the subselect has just one target then the parser
 * will create an EXPR_SUBLINK instead (and any operator above the subselect will be represented separately).
 * ROWCOMPARE, EXPR, and MULTIEXPR require the subselect to deliver at most one row (if it returns no rows, the result
 * is NULL). ALL, ANY, and ROWCOMPARE require the combining operators to deliver boolean results. ALL and ANY combine
 * the per-row results using AND and OR semantics respectively. ARRAY requires just one target column, and creates an
 * array of the target column's type using any number of rows resulting from the subselect.
 * </p>
 * <p>
 * SubLink is classed as an Expr node, but it is not actually executable; it must be replaced in the expression tree by
 * a SubPlan node during planning.
 * </p>
 * <p>
 * NOTE: in the raw output of gram.y, testexpr contains just the raw form of the lefthand expression (if any), and
 * operName is the String name of the combining operator. Also, subselect is a raw parsetree. During parse analysis, the
 * parser transforms testexpr into a complete boolean expression that compares the lefthand value(s) to PARAM_SUBLINK
 * nodes representing the output columns of the subselect. And subselect is transformed to a Query. This is the
 * representation seen in saved rules and in the rewriter.
 * </p>
 * <p>
 * In EXISTS, EXPR, MULTIEXPR, and ARRAY SubLinks, testexpr and operName are unused and are always null.
 * </p>
 * <p>
 * subLinkId is currently used only for MULTIEXPR SubLinks, and is zero in other SubLinks. This number identifies
 * different multiple-assignment subqueries within an UPDATE statement's SET list. It is unique only within a particular
 * targetlist. The output column(s) of the MULTIEXPR are referenced by PARAM_MULTIEXPR Params appearing elsewhere in the
 * tlist.
 * </p>
 * <p>
 * The CTE_SUBLINK case never occurs in actual SubLink nodes, but it is used in SubPlans generated for WITH subqueries.
 * </p>
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/primnodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */

public enum SubLinkType {
    EXISTS_SUBLINK,
    ALL_SUBLINK,
    ANY_SUBLINK,
    ROWCOMPARE_SUBLINK,
    EXPR_SUBLINK,
    MULTIEXPR_SUBLINK,
    ARRAY_SUBLINK,
    /** for SubPlans only */
    CTE_SUBLINK;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (SubLinkType type : values()) {
            format.append(separator).append(type);
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
