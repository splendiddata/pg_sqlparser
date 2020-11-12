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

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class AccessPriv extends Node {

    /** string name of privilege */
    @XmlAttribute
    public String priv_name;

    /** list of Value strings */
    @XmlElementWrapper(name = "cols")
    @XmlElement(name = "col")
    public List<Node> cols;

    @Override
    public AccessPriv clone() {
        AccessPriv clone = (AccessPriv) super.clone();
        if (cols != null) {
            clone.cols = cols.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (priv_name == null) {
            result.append("all privileges");
        } else {
            result.append(priv_name);
        }
        if (cols != null) {
            result.append(' ').append(ParserUtil.argumentsToSql(cols));
        }
        return result.toString();
    }
}
