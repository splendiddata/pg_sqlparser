/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ObjectType;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class SecLabelStmt extends Node {

    /** Object's type */
    @XmlAttribute
    public ObjectType objtype;

    /**
     * Qualified name of the object
     * 
     * @since 5.0
     */
    public Node object;

    /**
     * Qualified name of the object
     * 
     * @deprecated The objname is not used any more since version 5.0 (Postgres version 10). {@link #object} is used
     *             instead.
     */
    @Deprecated
    @XmlElementWrapper(name = "objname")
    @XmlElement(name = "nameNode")
    public List<? extends Node> objname;

    /**
     * Arguments if needed (eg, for functions)
     * 
     * @deprecated The objargs is not used any more since version 5.0 (Postgres version 10). {@link #object} is used
     *             instead.
     */
    @Deprecated
    @XmlElementWrapper(name = "objargs")
    @XmlElement(name = "objarg")
    public List<Node> objargs;

    /** Label provider (or NULL) */
    @XmlAttribute
    public String provider;

    /** New security label to be assigned */
    @XmlAttribute
    public String label;

    /**
     * Constructor
     */
    public SecLabelStmt() {
        super(NodeTag.T_SecLabelStmt);
    }

    /**
     * Constructor
     *
     * @param original
     *            The SecLabelStmt to copy
     */
    public SecLabelStmt(SecLabelStmt original) {
        super(original);
        this.objtype = original.objtype;
        if (original.object != null) {
            this.object = original.object.clone();
        }
        if (original.objname != null) {
            throw new IllegalArgumentException(CommentStmt.class.getName()
                    + ".objname is not supported any more since version 5.0 (Postgres version 10)), contains: "
                    + original.objname);
        }
        if (original.objargs != null) {
            throw new IllegalArgumentException(CommentStmt.class.getName()
                    + ".objargs is not supported any more since version 5.0 (Postgres version 10)), contains: "
                    + original.objargs);
        }
        this.provider = original.provider;
        this.label = original.label;
    }

    @Override
    public SecLabelStmt clone() {
        SecLabelStmt clone = (SecLabelStmt) super.clone();
        if (objname != null) {
            clone.objname = objname.clone();
        }
        if (objargs != null) {
            clone.objargs = objargs.clone();
        }
        if (object != null) {
            clone.object = object.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("security label");

        if (provider != null) {
            result.append(" for ").append(ParserUtil.identifierToSql(provider));
        }

        result.append(" on ").append(objtype).append(' ').append(ParserUtil.nameToSql(object)).append(" is ")
                .append(ParserUtil.toSqlTextString(label));

        return result.toString();
    }
}
