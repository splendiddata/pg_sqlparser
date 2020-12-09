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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class CreateSchemaStmt extends Node {

    /** the name of the schema to create */
    @XmlAttribute
    public String schemaname;

    /** the owner of the created schema */
    @XmlElement
    public Node authrole;

    /** schema components (list of parsenodes) */
    @XmlElementWrapper(name = "schemaElts")
    @XmlElement(name = "schemaElt")
    public List<Node> schemaElts;

    /** just do nothing if schema already exists? */
    @XmlAttribute
    public boolean if_not_exists;

    /**
     * Constructor
     */
    public CreateSchemaStmt() {
        super(NodeTag.T_CreateSchemaStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateSchemaStmt to copy
     */
    public CreateSchemaStmt(CreateSchemaStmt original) {
        super(original);
        this.schemaname = original.schemaname;
        if (original.authrole != null) {
            this.authrole = original.authrole.clone();
        }
        if (original.schemaElts != null) {
            this.schemaElts = original.schemaElts.clone();
        }
        this.if_not_exists = original.if_not_exists;
    }

    @Override
    public CreateSchemaStmt clone() {
        CreateSchemaStmt clone = (CreateSchemaStmt) super.clone();
        if (authrole != null) {
            clone.authrole = authrole.clone();
        }
        if (schemaElts != null) {
            clone.schemaElts = schemaElts.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create schema");

        if (if_not_exists) {
            result.append(" if not exists");
        }

        if (schemaname != null) {
            result.append(' ').append(ParserUtil.identifierToSql(schemaname));
        }

        if (authrole != null) {
            result.append(" authorization ").append(authrole);
        }

        if (schemaElts != null) {
            for (Node el : schemaElts) {
                result.append(' ');
                result.append(el);
            }
        }

        return result.toString();
    }
}
