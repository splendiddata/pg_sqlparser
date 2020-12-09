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
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.ReplicaIdentityType;

/**
 * Copied from /postgresql-9.4.1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 2.1.0
 */
@XmlRootElement(namespace = "parser")
public class ReplicaIdentityStmt extends Node {
    @XmlAttribute
    public ReplicaIdentityType identity_type;

    @XmlAttribute
    public String name;

    @Override
    public ReplicaIdentityStmt clone() {
        return (ReplicaIdentityStmt) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("replica identity ");
        
        switch(identity_type) {
        case REPLICA_IDENTITY_DEFAULT:
            result.append("default");
            break;
        case REPLICA_IDENTITY_FULL:
            result.append("full");
            break;
        case REPLICA_IDENTITY_INDEX:
            result.append("using index");
            break;
        case REPLICA_IDENTITY_NOTHING:
            result.append("nothing");
            break;
        default:
            result.append(ParserUtil.reportUnknownValue("ReplicaIdentityType", identity_type, getClass()));
            break;        
        }
        

        if (name != null) {
            result.append(' ').append(ParserUtil.identifierToSql(name));
        }

        return result.toString();
    }
}
