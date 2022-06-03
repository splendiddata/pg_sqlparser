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

import com.splendiddata.sqlparser.enums.ImportForeignSchemaType;

/**
 * Private struct for the result of import_qualification production
 * <p>
 * Copied from /postgresql-9.5alpha2/src/backend/parser/gram.c
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class ImportQual {
    @XmlAttribute
    public ImportForeignSchemaType type;

    @XmlElementWrapper(name = "table_names")
    @XmlElement(name = "table_name")
    public List<RangeVar> table_names;

    @Override
    public String toString() {
        return "TODO: implement com.splendiddata.sqlparser.structure.ImportQual.toString()";
    }

    @Override
    public ImportQual clone() {
        ImportQual clone;
        try {
            clone = (ImportQual) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("com.splendiddata.sqlparser.structure.ImportQual.clone()->failed", e);
        }
        if (table_names != null) {
            clone.table_names = table_names.clone();
        }
        return clone;
    }
}
