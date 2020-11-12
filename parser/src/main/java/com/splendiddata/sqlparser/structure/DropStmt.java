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

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.DropBehavior;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ObjectType;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class DropStmt extends Node {

    /** list of sublists of names (as Values) */
    @XmlElementWrapper(name = "objects")
    @XmlElement(name = "object")
    public List<Node> objects;

    /**
     * list of sublists of arguments (as Values)
     * 
     * @deprecated since 5.0 - the arguments are in the objects list arguments
     */
    @XmlElementWrapper(name = "arguments")
    @XmlElement(name = "argument")
    @Deprecated
    public List<List<?>> arguments;

    /** object type */
    @XmlAttribute
    public ObjectType removeType;

    /** RESTRICT or CASCADE behaviour */
    @XmlAttribute
    public DropBehavior behavior;

    /** skip error if object is missing? */
    @XmlAttribute
    public boolean missing_ok;

    /** drop index concurrently? */
    @XmlAttribute
    public boolean concurrent;

    public DropStmt() {
        super(NodeTag.T_DropStmt);
    }

    public DropStmt(DropStmt original) {
        super(original);
        if (original.objects != null) {
            this.objects = original.objects.clone();
        }
        if (original.arguments != null) {
            throw new IllegalArgumentException(AlterExtensionContentsStmt.class.getName()
                    + ".arguments is not supported any more since version 5.0 (Postgres version 10)), contains: "
                    + original.arguments);
        }
        this.removeType = original.removeType;
        this.behavior = original.behavior;
        this.missing_ok = original.missing_ok;
        this.concurrent = original.concurrent;
    }

    @Override
    public DropStmt clone() {
        DropStmt clone = (DropStmt) super.clone();
        if (objects != null) {
            clone.objects = objects.clone();
        }
        if (arguments != null) {
            clone.arguments = arguments.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        switch (removeType) {
        case OBJECT_AGGREGATE:
            return dropAggregateToString();
        case OBJECT_CAST:
            return dropCastToString();
        case OBJECT_CONVERSION:
            return dropMultipleToString();
        case OBJECT_COLLATION:
        case OBJECT_FDW:
        case OBJECT_LANGUAGE:
            return dropSimpleToString();
        case OBJECT_DOMAIN:
            return dropDomainToString();
        case OBJECT_EXTENSION:
        case OBJECT_FOREIGN_TABLE:
        case OBJECT_INDEX:
        case OBJECT_MATVIEW:
        case OBJECT_SCHEMA:
        case OBJECT_SEQUENCE:
        case OBJECT_TABLE:
        case OBJECT_TYPE:
        case OBJECT_VIEW:
        case OBJECT_FUNCTION:
        case OBJECT_OPERATOR:
        case OBJECT_PROCEDURE:
        case OBJECT_PUBLICATION:
        case OBJECT_ROUTINE:
        case OBJECT_SUBSCRIPTION:
        case OBJECT_STATISTIC_EXT:
        case OBJECT_TSCONFIGURATION:
        case OBJECT_TSDICTIONARY:
        case OBJECT_TSPARSER:
        case OBJECT_TSTEMPLATE:
        case OBJECT_FOREIGN_SERVER:
        case OBJECT_EVENT_TRIGGER:
            return dropMultipleToString();
        case OBJECT_OPCLASS:
        case OBJECT_OPFAMILY:
            return dropOperatorClassFamToString();
        case OBJECT_RULE:
        case OBJECT_TRIGGER:
            return dropObjectOnTableToString();
        case OBJECT_POLICY:
            return dropPolicyToString();
        case OBJECT_TRANSFORM:
            return dropTransformToString();
        case OBJECT_ACCESS_METHOD:
            return dropMultipleToString();
        default:
            return ParserUtil.reportUnknownValue("removeType", removeType, getClass());
        }
    }

    /**
     * Prints out the drop policy statement
     *
     * @return String the rendered drop policy statement
     */
    private String dropPolicyToString() {
        StringBuilder result = new StringBuilder();

        result.append("drop policy ");

        if (missing_ok) {
            result.append("if exists ");
        }

        @SuppressWarnings("unchecked")
        LinkedList<Value> qualifiedName = new LinkedList<>((List<Value>) objects.get(0));

        /*
         * The last node of the qualified name is the policy name
         */
        result.append(ParserUtil.identifierToSql(qualifiedName.removeLast().val.str));

        result.append(" on ");
        result.append(ParserUtil.nameListToSql(qualifiedName));

        return result.toString();
    }

    private String dropObjectOnTableToString() {
        StringBuilder result = new StringBuilder();

        result.append("drop ").append(removeType);

        if (missing_ok) {
            result.append(" if exists");
        }

        /*
         * The full rule name is stored as <schema>.<table>.<rule> but must be presented as <rule> on <schema>.<table>
         */
        @SuppressWarnings("unchecked")
        List<Value> tableName = (List<Value>) objects.get(0).clone();
        result.append(' ').append(ParserUtil.identifierToSql(tableName.remove(tableName.size() - 1).toString()))
                .append(" on ").append(ParserUtil.nameListToSql(tableName));

        if (behavior != null) {
            result.append(behavior);
        }

        return result.toString();
    }

    @SuppressWarnings("unchecked")
    private String dropOperatorClassFamToString() {
        StringBuilder result = new StringBuilder();

        result.append("drop ").append(removeType);

        if (missing_ok) {
            result.append(" if exists");
        }

        LinkedList<Value> objectsList = new LinkedList<>((List<Value>) objects.get(0));
        Value using = objectsList.remove();

        result.append(' ').append(ParserUtil.nameListToSql(objectsList)).append(" using ")
                .append(ParserUtil.identifierToSql(using.val.str));

        if (behavior != null) {
            result.append(behavior);
        }

        return result.toString();
    }

    /**
     * Prints out the drop transform statement
     *
     * @return String the rendered drop transform statement
     */
    private String dropTransformToString() {
        StringBuilder result = new StringBuilder();

        result.append("drop ").append(removeType);

        if (missing_ok) {
            result.append(" if exists");
        }

        @SuppressWarnings("unchecked")
        List<Node> transformSpec = (List<Node>)objects.get(0);
        result.append(" for ").append(transformSpec.get(0)).append(" language ").append(ParserUtil.nameToSql(transformSpec.get(1)));

        if (behavior != null) {
            result.append(behavior);
        }

        return result.toString();
    }

    private String dropMultipleToString() {
        StringBuilder result = new StringBuilder();

        result.append("drop ").append(removeType);

        if (concurrent) {
            result.append(" concurrently");
        }

        if (missing_ok) {
            result.append(" if exists");
        }

        String separator = " ";
        for (Node name : objects) {
            result.append(separator).append(ParserUtil.nameToSql(name));
            separator = ", ";
        }

        if (behavior != null) {
            result.append(behavior);
        }

        return result.toString();
    }

    private String dropDomainToString() {
        StringBuilder result = new StringBuilder();

        result.append("drop ").append(removeType);

        if (concurrent) {
            result.append(" concurrently");
        }

        if (missing_ok) {
            result.append(" if exists");
        }

        String separator = " ";
        for (Node domain : objects) {
            result.append(separator).append(ParserUtil.nameToSql(domain));
            separator = ", ";
        }

        if (behavior != null) {
            result.append(behavior);
        }

        return result.toString();
    }

    private String dropSimpleToString() {
        StringBuilder result = new StringBuilder();

        result.append("drop ").append(removeType);

        if (missing_ok) {
            result.append(" if exists");
        }

        result.append(' ').append(ParserUtil.nameToSql(objects.get(0)));

        if (behavior != null) {
            result.append(behavior);
        }

        return result.toString();
    }

    @SuppressWarnings("unchecked")
    private String dropCastToString() {
        StringBuilder result = new StringBuilder();

        result.append("drop cast ");

        if (missing_ok) {
            result.append("if exists ");
        }

        List<TypeName> typeNames = (List<TypeName>) objects.get(0);
        result.append('(').append(ParserUtil.nameToSql(typeNames.get(0))).append(" as ")
                .append(ParserUtil.nameToSql(typeNames.get(1))).append(')');

        if (behavior != null) {
            result.append(behavior);
        }

        return result.toString();
    }

    private String dropAggregateToString() {
        StringBuilder result = new StringBuilder();

        result.append("drop aggregate ");

        if (missing_ok) {
            result.append("if exists ");
        }

        String separator = "";
        for (Node agg : objects) {
            result.append(separator).append(ParserUtil.nameToSql(agg));
            separator = ", ";
            if (((ObjectWithArgs) agg).objargs == null || ((ObjectWithArgs) agg).objargs.isEmpty()) {
                result.append("(*)");
            }
        }

        if (behavior != null) {
            result.append(behavior);
        }

        return result.toString();
    }
}
