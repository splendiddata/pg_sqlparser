/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2023
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

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ObjectType;

/**
 * Copied from /postgresql-12beta2/src/include/nodes/parsenodes.h
 * <p>
 * ----------------------<br>
 * Create {Aggregate|Operator|Type} Statement<br>
 * ----------------------
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class DefineStmt extends Node {
    /** aggregate, operator, type */
    @XmlAttribute
    public ObjectType kind;

    /** hack to signal old CREATE AGG syntax */
    @XmlAttribute
    public boolean oldstyle;

    /** qualified name (list of Value strings) */
    @XmlElementWrapper(name = "defnames")
    @XmlAnyElement
    public List<Value> defnames;

    /** a list of TypeName (if needed) */
    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    public List<Node> args;

    /** a list of DefElem */
    @XmlElementWrapper(name = "definition")
    @XmlElement(name = "definitionNode")
    public List<DefElem> definition;

    /**
     * just do nothing if it already exists?
     * 
     * @since 5.0
     */
    @XmlAttribute
    public boolean if_not_exists;

    /**
     * replace if already exists?
     * 
     * @since 7.0 - Postgres 12
     */
    @XmlAttribute
    public boolean replace;

    /**
     * Constructor
     *
     */
    public DefineStmt() {
        super(NodeTag.T_DefineStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The DefineStmt to copy
     */
    public DefineStmt(DefineStmt original) {
        super(original);
        this.kind = original.kind;
        this.oldstyle = original.oldstyle;
        if (original.defnames != null) {
            this.defnames = original.defnames.clone();
        }
        if (original.args != null) {
            this.args = original.args.clone();
        }
        if (original.definition != null) {
            this.definition = original.definition.clone();
        }
        this.if_not_exists = original.if_not_exists;
        this.replace = original.replace;
    }

    @Override
    public DefineStmt clone() {
        DefineStmt clone = (DefineStmt) super.clone();
        if (defnames != null) {
            clone.defnames = defnames.clone();
        }
        if (args != null) {
            clone.args = args.clone();
        }
        if (definition != null) {
            clone.definition = definition.clone();
        }
        return clone;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create ");
        if (replace) {
            result.append("or replace ");
        }
        result.append(kind);

        if (if_not_exists) {
            result.append(" if not exists");
        }

        if (defnames != null) {
            if (ObjectType.OBJECT_OPERATOR.equals(kind)) {
                result.append(' ').append(ParserUtil.operatorNameToSql(defnames));
            } else {
                result.append(' ').append(ParserUtil.nameToSql(defnames));
            }
        }

        if (!oldstyle && args != null) {
            Node arg0 = args.get(0);
            if (arg0 instanceof Value) {
                result.append(ParserUtil.identifierToSql(arg0.toString()));
            } else if (arg0 == null) {
                result.append(" (*)");
            } else if (arg0 instanceof List) {
                int orderByOffset = -1;
                if (args.size() > 1) {
                    Node arg1 = args.get(1);
                    if (NodeTag.T_Integer.equals(arg1.type)) {
                        orderByOffset = ((Value) arg1).val.ival;
                    }
                }

                result.append('(');
                int i = 0;
                for (Node arg : (List<Node>) arg0) {
                    if (i == orderByOffset) {
                        if (i != 0) {
                            result.append(' ');
                        }
                        result.append("order by ");
                    } else {
                        if (i != 0) {
                            result.append(", ");
                        }
                    }
                    result.append(arg);
                    i++;
                }
                result.append(')');
            } else {
                result.append(arg0);
            }
        }

        if (definition != null) {
            if (ObjectType.OBJECT_COLLATION.equals(kind) && definition != null && definition.size() == 1
                    && "from".equals(definition.get(0).defname)) {
                result.append(" from ").append(ParserUtil.nameToSql(definition.get(0).arg));
            } else {
                result.append(" (");
                String separator = "";
                for (DefElem def : definition) {
                                        switch (def.defname.toLowerCase()) {
                    case "delimiter":
                    case "initcond1":
                    case "initcond":
                    case "minitcond":
                        result.append(separator).append(def.defname.toLowerCase()).append(" = ").append(ParserUtil.toSqlTextString(def.arg));
                        break;
                    case "basetype":
                        result.append(separator).append(def.defname.toLowerCase());
                        if ("ANY".equals(def.arg.toString())) {
                            result.append(" = 'ANY'");
                        } else {
                            result.append(" = ").append(ParserUtil.toSqlTextString(def.arg));
                        }
                        break;
                    case "sortop":
                    case "locale":
                        result.append(separator).append(def.defname.toLowerCase());
                        switch (def.arg.type) {
                        case T_TypeName:
                            result.append(" = ").append(ParserUtil.nameToSql(def.arg));
                            break;
                        case T_List:
                            result.append(" = ").append(ParserUtil.nameToSql(def.arg));
                            break;
                        case T_String:
                        default:
                            result.append(" = '").append(def.arg.toString()).append('\'');
                            break;
                        }
                        break;
                    case "lc_collate":
                    case "rules":
                        result.append(separator).append(def.defname.toLowerCase());
                        if (def.arg instanceof Value) {
                            // uc_EN is case sensitive, so take the literal string
                            result.append(" = '").append(((Value) def.arg).val.str).append('\'');
                        } else {
                            result.append(" = ").append(def.arg);
                        }
                        break;
                    default:
                        result.append(separator).append('"').append(def.defname.toLowerCase()).append('"');
                        if (def.arg != null) {
                            result.append(" = ");
                            if (def.arg instanceof List) {
                                switch (def.defname) {
                                case "commutator":
                                case "negator":
                                    result.append("operator(")
                                            .append(ParserUtil.operatorNameToSql((List<Value>) def.arg)).append(')');
                                    break;
                                default:
                                    result.append(ParserUtil.nameToSql(def.arg));
                                    break;
                                }
                            } else {
                                result.append(def.arg);
                            }
                        }
                        break;
                    }

                    separator = ", ";
                }
                result.append(')');
            }
        }

        return result.toString();
    }
}
