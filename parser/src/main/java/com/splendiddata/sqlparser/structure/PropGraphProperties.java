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
 * Copied from /postgresql-19beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 19beta1
 */
@XmlRootElement(namespace = "parser")
public class PropGraphProperties extends Node {

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    public List<ResTarget> properties;

    @XmlAttribute
    public boolean all;

    /**
     * Constructor
     */
    public PropGraphProperties() {
        super(NodeTag.T_PropGraphProperties);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The vPropGraphLabelAndProperties to copy
     */
    public PropGraphProperties(PropGraphProperties original) {
        super(original);
        if (original.properties != null) {
            this.properties = original.properties.clone();
        }
        this.all = original.all;
    }

    @Override
    public PropGraphProperties clone() {
        PropGraphProperties clone = (PropGraphProperties) super.clone();
        if (properties != null) {
            clone.properties = properties.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (properties == null || properties.isEmpty()) {
            if (all) {
                result.append("properties all columns");
            } else {
                result.append("no properties");
            }
        } else {
            result.append("properties ").append(properties);
        }
        return result.toString();
    }
}
