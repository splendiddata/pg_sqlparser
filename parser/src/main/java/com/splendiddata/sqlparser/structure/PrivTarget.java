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

import com.splendiddata.sqlparser.enums.GrantTargetType;
import com.splendiddata.sqlparser.enums.ObjectType;

/**
 * TODO Please insert some explanation here
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class PrivTarget implements Cloneable {

    @XmlAttribute
    public GrantTargetType targtype;

    @XmlAttribute
    public ObjectType objtype;

    @XmlElementWrapper(name = "objs")
    @XmlElement(name = "obj")
    public List<Node> objs;

    /**
     * Constructor
     */
    public PrivTarget() {
        // empty
    }

    /**
     * Copy constructor
     * @param toCopy The PrivTarget to copy
     */
    public PrivTarget(PrivTarget toCopy) {
        this.targtype=toCopy.targtype;
        this.objtype=toCopy.objtype;
        if (toCopy.objs != null) {
            this.objs = toCopy.objs.clone();
        }
    }
    
    @Override
    public PrivTarget clone() {
        PrivTarget clone;
        try {
            clone = (PrivTarget) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("com.splendiddata.sqlparser.structure.PrivTarget.clone()->failed", e);
        }
        if (objs != null) {
            clone.objs = objs.clone();
        }
        return clone;
    }
}
