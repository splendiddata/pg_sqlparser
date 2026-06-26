/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2026
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
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * 
 * Base class for CreatePublicationStmt and AlterPublicationStmt
 * 
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class AbstractPublicationStmt extends Node {
    /** Name of of the publication */
    @XmlAttribute
    public String pubname;

    /**
     * parameters used for ALTER PUBLICATION ... WITH * List of DefElem nodes
     */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /*
     * Parameters used for ALTER PUBLICATION ... ADD/DROP/SET publication objects.
     */
    /**
     * Optional list of publication objects
     * 
     * @since Postgres 15
     */
    @XmlElementWrapper(name = "pubobjects")
    @XmlElement(name = "pubobject")
    public List<PublicationObjSpec> pubobjects;

    /** Special publication for all tables in db */
    @XmlAttribute
    public boolean for_all_tables;

    /**
     * True if ALL SEQUENCES is specified
     * 
     * @since 19beta1
     */
    @XmlAttribute
    public boolean for_all_sequences;

    /**
     * No argument Constructor to satisfy jaxb. 
     * @throws UnsupportedOperationException whenever invoked
     */
    @SuppressWarnings("unused")
    private AbstractPublicationStmt() {
        throw(new UnsupportedOperationException());
    }
    /**
     * Constructor
     */
    AbstractPublicationStmt(NodeTag tag) {
        super(tag);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterPublicationStmt to copy
     */
    AbstractPublicationStmt(AbstractPublicationStmt original) {
        super(original);
        this.pubname = original.pubname;
        if (original.options != null) {
            this.options = original.options.clone();
        }
        if (original.pubobjects != null) {
            this.pubobjects = original.pubobjects.clone();
        }
        this.for_all_tables = original.for_all_tables;
        this.for_all_sequences = original.for_all_sequences;
    }

    @Override
    public AbstractPublicationStmt clone() {
        AbstractPublicationStmt clone = (AbstractPublicationStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        if (pubobjects != null) {
            clone.pubobjects = pubobjects.clone();
        }
        return clone;
    }
}
