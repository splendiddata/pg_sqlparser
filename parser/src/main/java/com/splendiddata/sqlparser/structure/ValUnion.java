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
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Content of a Value, copied from /postgresql-9.3.4/src/include/nodes/value.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class ValUnion implements Cloneable {

    /** machine integer */
    @XmlAttribute
    public int ival;

    /** string */
    @XmlAttribute
    public String str;

    /**
     * boolean value
     * 
     * @since Postgres 15
     */
    @XmlAttribute
    public boolean boolval;

    /**
     * Constructor
     */
    public ValUnion() {
        // empty
    }

    /**
     * Constructor
     *
     * @param toCopy
     *            The ValUnion to copy
     */
    public ValUnion(ValUnion toCopy) {
        ival = toCopy.ival;
        str = toCopy.str;
        boolval = toCopy.boolval;
    }

    @Override
    public String toString() {
        if (str == null) {
            return Integer.toString(ival);
        }
        return str;
    }

    @Override
    public ValUnion clone() {
        try {
            return (ValUnion) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("com.splendiddata.sqlparser.structure.ValUnion.clone()->failed", e);
        }
    }
}
