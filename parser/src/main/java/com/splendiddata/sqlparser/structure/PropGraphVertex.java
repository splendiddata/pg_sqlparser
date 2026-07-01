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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-Postgres 19beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 19beta1
 */
@XmlRootElement(namespace = "parser")
public class PropGraphVertex extends Node {
    @XmlElement
    public RangeVar vtable;

    @XmlElementWrapper(name = "vkey")
    @XmlElement(name = "vkeyElement")
    public List<Value> vkey;

    @XmlElementWrapper(name = "labels")
    @XmlElement(name = "label")
    public List<PropGraphLabelAndProperties> labels;

    /**
     * Constructor
     */
    public PropGraphVertex() {
        super(NodeTag.T_PropGraphVertex);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The PropGraphVertex to copy
     */
    public PropGraphVertex(PropGraphVertex original) {
        super(original);
        if (original.vtable != null) {
            this.vtable = original.vtable.clone();
        }
        if (original.vkey != null) {
            this.vkey = original.vkey.clone();
        }
        if (original.labels != null) {
            this.labels = original.labels.clone();
        }
    }

    @Override
    public PropGraphVertex clone() {
        PropGraphVertex clone = (PropGraphVertex) super.clone();
        if (vtable != null) {
            clone.vtable = vtable.clone();
        }
        if (vkey != null) {
            clone.vkey = vkey.clone();
        }
        if (labels != null) {
            clone.labels = labels.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (vtable != null) {
            result.append(vtable);
        }
        if (vkey != null && !vkey.isEmpty()) {
            result.append(" key ").append(vkey);
        }
        if (labels != null && !labels.isEmpty()) {
            for (PropGraphLabelAndProperties label : labels) {
                result.append(' ').append(label);
            }
        }
        return result.toString();
    }
}
