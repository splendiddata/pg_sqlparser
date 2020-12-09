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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.SubLinkType;

/**
 * Copied from /postgresql-9.5alpha2/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class SubLink extends Expr {
    @XmlAttribute
    public SubLinkType subLinkType;

    /** ID (1..n); 0 if not MULTIEXPR */
    @XmlAttribute
    public int subLinkId;

    /** outer-query test for ALL/ANY/ROWCOMPARE */
    @XmlElement
    public Node testexpr;

    /** originally specified operator name */
    @XmlElementWrapper(name = "operName")
    @XmlElement(name = "nameNode")
    public List<Value> operName;

    /** subselect as Query* or parsetree */
    @XmlElement
    public Node subselect;

    /**
     * Constructor
     */
    public SubLink() {
        super(NodeTag.T_SubLink);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The (subclass of) SubLink to copy
     */
    public SubLink(SubLink toCopy) {
        super(toCopy);

        this.subLinkType = toCopy.subLinkType;
        this.subLinkId = toCopy.subLinkId;
        if (toCopy.testexpr == null) {
            this.testexpr = null;
        } else {
            this.testexpr = toCopy.testexpr.clone();
        }
        if (toCopy.operName == null) {
            this.operName = null;
        } else {
            this.operName = toCopy.operName.clone();
        }
        if (toCopy.subselect == null) {
            this.subselect = null;
        } else {
            this.subselect = toCopy.subselect.clone();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        switch (subLinkType) {
        case EXPR_SUBLINK:
            break;
        case EXISTS_SUBLINK:
            result.append("exists ");
            break;
        case ANY_SUBLINK:
            if (operName == null) {
                result.append(testexpr).append(" in ");
            } else {
                result.append(testexpr).append(' ').append(ParserUtil.operatorNameToSql(operName)).append(" any ");
            }
            break;
        case ALL_SUBLINK:
            result.append(testexpr).append(' ').append(ParserUtil.operatorNameToSql(operName)).append(" all ");
            break;
        case ARRAY_SUBLINK:
            result.append("array");
            break;
        case CTE_SUBLINK:
            result.append("??????? CTE_SUBLINK ?????? ");
            break;
        case ROWCOMPARE_SUBLINK:
            result.append("??????? ROWCOMPARE_SUBLINK ?????? ");
            break;
        case MULTIEXPR_SUBLINK:
            break;
        default:
            throw new AssertionError("Unknown SubLinkType: " + subLinkType);
        }
        result.append('(').append(subselect).append(')');
        return result.toString();
    }

    @Override
    public SubLink clone() {
        SubLink clone = (SubLink) super.clone();
        if (testexpr != null) {
            clone.testexpr = testexpr.clone();
        }
        if (operName != null) {
            clone.operName = operName.clone();
        }
        if (subselect != null) {
            clone.subselect = subselect.clone();
        }
        return clone;
    }
}
