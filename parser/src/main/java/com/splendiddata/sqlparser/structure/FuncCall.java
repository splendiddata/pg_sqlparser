/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2024
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

import java.util.Iterator;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.CoercionForm;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 * <p>
 * FuncCall - a function or aggregate invocation
 * </p>
 * <p>
 * agg_order (if not NIL) indicates we saw 'foo(... ORDER BY ...)', or if agg_within_group is true, it was 'foo(...)
 * WITHIN GROUP (ORDER BY ...)'. agg_star indicates we saw a 'foo(*)' construct, while agg_distinct indicates we saw
 * 'foo(DISTINCT ...)'. In any of these cases, the construct *must* be an aggregate call. Otherwise, it might be either
 * an aggregate or some other kind of function. However, if FILTER or OVER is present it had better be an aggregate or
 * window function.
 * </p>
 * <p>
 * Normally, you'd initialize this via makeFuncCall() and then only change the parts of the struct its defaults don't
 * match afterwards, as needed.
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 2.1.0
 */
@XmlRootElement(namespace = "parser")
public class FuncCall extends Expr {

    /** qualified name of function */
    @XmlElementWrapper(name = "funcname")
    @XmlElement(name = "nameNode")
    public List<Value> funcname;

    /** the arguments (list of exprs) */
    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    public List<Node> args;

    /** ORDER BY (list of SortBy) */
    @XmlElementWrapper(name = "agg_order")
    @XmlElement(name = "orderBy")
    public List<Node> agg_order;

    /** FILTER clause, if any */
    @XmlElement
    public Node agg_filter;

    /** ORDER BY appeared in WITHIN GROUP */
    @XmlAttribute
    public boolean agg_within_group;

    /** argument was really '*' */
    @XmlAttribute
    public boolean agg_star;

    /** arguments were labeled DISTINCT */
    @XmlAttribute
    public boolean agg_distinct;

    /** last argument was labeled VARIADIC */
    @XmlAttribute
    public boolean func_variadic;

    /** OVER clause, if any */
    @XmlElement
    public WindowDef over;
    
    /**
     * how to display this node
     * @since 14
     */
    @XmlAttribute
    public CoercionForm funcformat;
    
    /**
     * Constructor
     */
    public FuncCall() {
        super(NodeTag.T_FuncCall);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The FuncCall to copy
     */
    public FuncCall(FuncCall toCopy) {
        super(toCopy);
        if (toCopy.agg_filter != null) {
            this.agg_filter = agg_filter.clone();
        }
        if (toCopy.funcname != null) {
            this.funcname = toCopy.funcname.clone();
        }
        if (toCopy.args != null) {
            this.args = toCopy.args.clone();
        }
        if (toCopy.agg_order != null) {
            this.agg_order = toCopy.agg_order.clone();
        }
        if (toCopy.over != null) {
            this.over = toCopy.over.clone();
        }
        this.agg_within_group = toCopy.agg_within_group;
        this.agg_star = toCopy.agg_star;
        this.agg_distinct = toCopy.agg_distinct;
        this.func_variadic = toCopy.func_variadic;
        this.funcformat = toCopy.funcformat;
    }

    /**
     * @return String [schemaName.]functionName
     */
    public String getQualifiedName() {
        if (funcname == null) {
            return "";
        }
        if (funcname.size() == 1) {
            return funcname.get(0).val.str;
        }
        return funcname.get(0).val.str + '.' + funcname.get(1).val.str;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        String sep = "";
        if (funcname != null) {
            for (Value nameLevel : funcname) {
                result.append(sep).append(ParserUtil.identifierToSql(nameLevel.toString()));
                sep = ".";
            }
        }

        result.append("(");

        if (agg_distinct) {
            result.append("distinct ");
        }

        if (args != null) {
            sep = "";
            Iterator<Node> it = args.iterator();
            while (it.hasNext()) {
                Node arg = it.next();
                // If the function is marked as func_variadic, this means that its last argument must be marked as variadic.
                if (!func_variadic || it.hasNext()) {
                    result.append(sep).append(arg);
                } else {
                    result.append(sep).append("variadic ").append(arg);
                }
                sep = ", ";
            }
        }
        if (agg_star) {
            result.append("*");
        }

        if (agg_within_group) {
            result.append(") within group (");
        }

        if (agg_order != null) {
            result.append(" order by");
            sep = " ";
            for (Node order : agg_order) {
                result.append(sep).append(order);
                sep = ", ";
            }
        }

        result.append(")");

        if (agg_filter != null) {
            result.append(" filter (where ").append(agg_filter).append(')');
        }

        if (over != null) {
            result.append(" over ");
            if (over.name == null) {
                result.append('(').append(over).append(')');
            } else {
                result.append(over.name);
            }
        }

        return result.toString();
    }

    @Override
    public FuncCall clone() {
        FuncCall clone = (FuncCall) super.clone();
        if (agg_filter != null) {
            clone.agg_filter = agg_filter.clone();
        }
        if (funcname != null) {
            clone.funcname = funcname.clone();
        }
        if (args != null) {
            clone.args = args.clone();
        }
        if (agg_order != null) {
            clone.agg_order = agg_order.clone();
        }
        if (over != null) {
            clone.over = over.clone();
        }
        return clone;
    }
}
