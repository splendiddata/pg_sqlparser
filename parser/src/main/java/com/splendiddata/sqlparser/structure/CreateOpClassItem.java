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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CreateOpClassItem extends Node {
    @XmlAttribute
    public int itemtype;

    /**
     * operator or function name
     * <p>
     * Since 5.0 an ObjectWithArgs, before that a List&lt;Value&gt;
     * </p>
     * TODO: When versions older that 5.0 (Postgres version 10) are no longer supported, please alter to ObjectWithArgs.
     */
    @XmlElement
    public Node name;

    /**
     * argument types
     * 
     * @deprecated since 5.0 - arguments are now in {@link #name}
     */
    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    @Deprecated
    public List<TypeName> args;

    /** strategy num or support proc num */
    @XmlAttribute
    public int number;

    /** only used for ordering operators */
    @XmlElementWrapper(name = "order_family")
    @XmlElement(name = "orderFamilyNode")
    public List<Value> order_family;

    /** only used for functions */
    @XmlElementWrapper(name = "class_args")
    @XmlElement(name = "class_arg")
    public List<TypeName> class_args;

    /** datatype stored in index */
    @XmlElement
    public TypeName storedtype;

    public CreateOpClassItem() {
        super(NodeTag.T_CreateOpClassItem);
    }

    public CreateOpClassItem(CreateOpClassItem original) {
        super(original);
        this.itemtype = original.itemtype;
        if (original.name != null) {
            this.name = original.name.clone();
        }
        if (original.args != null) {
            throw new IllegalArgumentException(AlterExtensionContentsStmt.class.getName()
                    + ".args is not supported any more since version 5.0 (Postgres version 10)), contains: "
                    + original.args);
        }
        this.number = original.number;
        if (original.order_family != null) {
            this.order_family = original.order_family.clone();
        }
        if (original.class_args != null) {
            this.class_args = original.class_args.clone();
        }
        if (original.storedtype != null) {
            this.storedtype = original.storedtype.clone();
        }
    }

    @Override
    public CreateOpClassItem clone() {
        CreateOpClassItem clone = (CreateOpClassItem) super.clone();
        if (name != null) {
            clone.name = name.clone();
        }
        if (args != null) {
            clone.args = args.clone();
        }
        if (order_family != null) {
            clone.order_family = order_family.clone();
        }
        if (class_args != null) {
            clone.class_args = class_args.clone();
        }
        if (storedtype != null) {
            clone.storedtype = storedtype.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        switch (itemtype) {
        case 1:
            result.append("operator ").append(number);
            if (name != null) {
                result.append(' ').append(name);
            }

            if (class_args != null) {
                result.append(' ').append(class_args);
            }

            if (order_family != null) {
                result.append(" for order by ").append(ParserUtil.nameToSql(order_family));
            }
            break;
        case 2:
            result.append("function ").append(number);
            if (class_args != null) {
                result.append(' ').append(class_args);
            }
            if (name != null) {
                result.append(' ').append(name);
            }
            break;
        case 3:
            result.append("storage ").append(storedtype);
            break;
        default:
            result.append(ParserUtil.reportUnknownValue("itemtype", Integer.valueOf(itemtype), getClass()));
        }

        return result.toString();
    }
}