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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Alter Operator Set Restrict, Join
 * <p>
 * Copied from /postgresql-9.6beta1/src/include/nodes/parsenodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 4.0.0
 */
@XmlRootElement(namespace = "parser")
public class AlterOperatorStmt extends Node {

    /**
     * operator name
     * <p>
     * In this version the opername will be an ObjectWithNames. But if previous versions it is a List&lt;Value&gt;. So
     * please cast to {@link ObjectWithArgs} where applicable.
     * </p>
     * TODO: Make the type ObjectWithArgs when all versions before 5.0 have become obsolete
     */
    @XmlElement
    public Node opername;

    /**
     * operator's argument TypeNames.
     * 
     * @deprecated since 5.0 - The {@link #opername} takes it all now
     */
    @XmlElementWrapper(name = "operargs")
    @XmlElement(name = "operarg")
    @Deprecated
    public List<TypeName> operargs;

    /** List of DefElem nodes. */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    public AlterOperatorStmt() {
        super(NodeTag.T_AlterOperatorStmt);
    }

    public AlterOperatorStmt(AlterOperatorStmt original) {
        super(original);
        if (original.opername != null) {
            this.opername = original.opername.clone();
        }
        if (original.operargs != null) {
            throw new IllegalArgumentException(AlterExtensionContentsStmt.class.getName()
                    + ".operargs is not supported any more since version 5.0 (Postgres version 10)), contains: "
                    + original.operargs);
        }
        if (original.options != null) {
            this.options = original.options.clone();
        }
    }

    @Override
    public AlterOperatorStmt clone() {
        AlterOperatorStmt clone = (AlterOperatorStmt) super.clone();
        if (opername != null) {
            clone.opername = opername.clone();
        }
        if (operargs != null) {
            clone.operargs = operargs.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter operator ");

        result.append(ParserUtil.operatorNameToSql(((ObjectWithArgs) opername).objname));

        if (((ObjectWithArgs) opername).objargs != null) {

            String separator = " (";
            for (TypeName operarg : ((ObjectWithArgs) opername).objargs) {
                result.append(separator);
                separator = ", ";
                if (operarg == null) {
                    result.append("none");
                } else {
                    result.append(operarg.toString());
                }
            }
            result.append(") ");
        }

        if (options != null) {
            result.append("set");
            String separator = " (";
            for (DefElem option : options) {
                result.append(separator);
                result.append(option.defname.toLowerCase());
                separator = ", ";
                switch (option.defname.toLowerCase()) {
                case "restrict":
                case "join":
                    if (option.arg == null) {
                        result.append(" = ").append("none");
                    } else {
                        result.append(" = ").append(ParserUtil.nameToSql(option.arg));
                    }
                    break;
                case "commutator":
                case "negator":
                    if (option.arg instanceof List) {
                        result.append(" = ").append("operator(")
                                .append(ParserUtil.operatorNameToSql((List<Value>) option.arg)).append(')');
                    } else {
                        result.append(" = ").append(option.arg);
                    }
                    break;
                default:
                    result.append(" = ").append(option.arg);
                    break;
                }
            }
            result.append(')');
        }

        return result.toString();
    }
}
