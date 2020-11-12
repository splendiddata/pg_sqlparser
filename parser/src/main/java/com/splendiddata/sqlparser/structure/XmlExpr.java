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

import java.util.Iterator;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.XmlExprOp;
import com.splendiddata.sqlparser.enums.XmlOptionType;
import com.splendiddata.sqlparser.enums.XmlStandaloneType;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class XmlExpr extends Node {

    /** xml function ID */
    @XmlAttribute
    public XmlExprOp op;

    /** name in xml(NAME foo ...) syntaxes */
    @XmlAttribute
    public String name;

    /** non-XML expressions for xml_attributes */
    @XmlElementWrapper(name = "named_args")
    @XmlElement(name = "named_arg")
    public List<Node> named_args;

    /** parallel list of Value strings */
    @XmlElementWrapper(name = "arg_names")
    @XmlElement(name = "arg_name")
    public List<Value> arg_names;

    /** list of expressions */
    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    public List<Node> args;

    /**
     * DOCUMENT or CONTENT
     */
    @XmlAttribute
    public XmlOptionType xmloption;

    /** target type/typmod for XMLSERIALIZE */
    @SuppressWarnings("hiding")
    @XmlElement
    public Oid type;

    @XmlAttribute
    public int typmod;

    @Override
    public XmlExpr clone() {
        XmlExpr clone = (XmlExpr) super.clone();
        if (named_args != null) {
            clone.named_args = named_args.clone();
        }
        if (arg_names != null) {
            clone.arg_names = arg_names.clone();
        }
        if (args != null) {
            clone.args = args.clone();
        }
        if (type != null) {
            clone.type = type.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (XmlExprOp.IS_DOCUMENT.equals(op)) {
            if (args != null) {
                for (Node arg : args) {
                    result.append(' ').append(arg);
                }
            }
            result.append(" is document");
            return result.toString();
        }

        if (op != null) {
            result.append(op);
        }

        String separator = " (";

        if (name != null) {
            result.append(separator).append("name ").append(ParserUtil.identifierToSql(name));
            separator = ", ";
        }

        if (named_args != null) {
            if (XmlExprOp.IS_XMLELEMENT.equals(op)) {
                result.append(separator).append("xmlattributes(");
                separator = "";
            }
            for (Node arg : named_args) {
                result.append(separator).append(arg);
                separator = ", ";
            }
            if (XmlExprOp.IS_XMLELEMENT.equals(op)) {
                result.append(')');
                separator = ", ";
            }
        }

        if (args != null) {
            switch (op) {
            case IS_XMLPARSE:
                if (xmloption != null) {
                    switch (xmloption) {
                    case XMLOPTION_DOCUMENT:
                        result.append(separator).append("document ");
                        break;
                    case XMLOPTION_CONTENT:
                        result.append(separator).append("content ");
                        break;
                    default:
                        result.append(separator)
                                .append(ParserUtil.reportUnknownValue("xmloption", xmloption, getClass()));
                    }
                }
                separator = ", ";
                if (args.size() > 0) {
                    result.append(args.get(0));
                }
                break;
            case IS_XMLROOT:
                Iterator<Node> it = args.iterator();
                if (it.hasNext()) {
                    result.append(separator).append(it.next());
                    separator = ", ";
                }
                if (it.hasNext()) {
                    Node n = it.next();
                    if (NodeTag.T_Null.equals(n.type)) {
                        result.append(separator).append("version no value");
                    } else {
                        result.append(separator).append("version ").append(n);
                    }
                }
                if (it.hasNext()) {
                    int option = ((A_Const) it.next()).val.val.ival;
                    switch (option) {
                    case XmlStandaloneType.XML_STANDALONE_YES:
                        result.append(separator).append("standalone yes");
                        break;
                    case XmlStandaloneType.XML_STANDALONE_NO:
                        result.append(separator).append("standalone no");
                        break;
                    case XmlStandaloneType.XML_STANDALONE_NO_VALUE:
                        result.append(separator).append("standalone no value");
                        break;
                    case XmlStandaloneType.XML_STANDALONE_OMITTED:
                        break;
                    default:
                        result.append(separator).append(ParserUtil.reportUnknownValue("standalone option",
                                Integer.valueOf(option), getClass()));
                        break;
                    }

                }
                break;
            case IS_DOCUMENT:
                for (Node arg : args) {
                    result.append(separator).append(arg);
                    separator = ", ";
                }
                result.append(" is document");
                break;
            case IS_XMLCONCAT:
            case IS_XMLELEMENT:
            case IS_XMLFOREST:
            case IS_XMLPI:
            case IS_XMLSERIALIZE:
                for (Node arg : args) {
                    result.append(separator).append(arg);
                    separator = ", ";
                }
                break;
            default:
                result.append(separator).append(ParserUtil.reportUnknownValue("XmlExprOp", op, getClass()));
                break;
            }
        }

        if (!" (".equals(separator)) {
            result.append(')');
        }

        return result.toString();
    }
}
