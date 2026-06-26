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
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-19beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 19beta1
 */
@XmlRootElement(namespace = "parser")
public class PropGraphLabelAndProperties extends Node {

    @XmlAttribute
    public String label;

    @XmlElement
    public PropGraphProperties properties;

    /**
     * Constructor
     */
    public PropGraphLabelAndProperties() {
        super(NodeTag.T_PropGraphLabelAndProperties);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The PropGraphLabelAndProperties to copy
     */
    public PropGraphLabelAndProperties(PropGraphLabelAndProperties original) {
        super(original);
        this.label = original.label;
        if (original.properties != null) {
            this.properties = original.properties.clone();
        }
    }

    @Override
    public PropGraphLabelAndProperties clone() {
        PropGraphLabelAndProperties clone = (PropGraphLabelAndProperties) super.clone();
        if (properties != null) {
            clone.properties = properties.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (label == null) {
            result.append("default label");
        } else {
            result.append("label ").append(label);
        }
        if (properties != null) {
            result.append(' ').append(properties);
        }
        return result.toString();
    }
}
