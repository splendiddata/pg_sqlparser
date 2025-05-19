/*
 * Copyright (c) Splendid Data Product Development B.V. 2025
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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ReturningOptionKind;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-18beta1/src/include/nodes/parsenodes.h
 * <p>
 * ReturningOption -
 *      An individual option in the RETURNING WITH(...) list
 *
 * @author Splendid Data Product Development B.V.
 * @since 18.0
 */
@XmlRootElement(namespace = "parser")
public class ReturningOption extends Node {

    /** specified option */
    @XmlAttribute
    public ReturningOptionKind option;
    
    /* option's value */
    @XmlAttribute
    public String value;

    /**
     * Constructor
     */
    public ReturningOption() {
        super(NodeTag.T_ReturningOption);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The ReturningOption to copy
     */
    public ReturningOption(ReturningOption original) {
        super(original);
        this.option = original.option;
        this.value=original.value;
    }

    @Override
    public ReturningOption clone() {
        return(ReturningOption) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(option).append(" as ").append(ParserUtil.identifierToSql(value));
        return result.toString();
    }
}
