/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2022
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

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from postgresql-15beta1/src/include/nodes/primenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class AlterDatabaseRefreshCollStmt extends Node {

    @XmlAttribute
    public String dbname;

    /**
     * Constructor
     */
    public AlterDatabaseRefreshCollStmt() {
        super(NodeTag.T_AlterDatabaseRefreshCollStmt);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The AlterDatabaseRefreshCollStmt to copy
     */
    public AlterDatabaseRefreshCollStmt(AlterDatabaseRefreshCollStmt orig) {
        super(orig);
        this.dbname = orig.dbname;
    }

    @Override
    public AlterDatabaseRefreshCollStmt clone() {
        return (AlterDatabaseRefreshCollStmt) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter database ").append(dbname).append(" refresh colation version");

        return result.toString();
    }
}
