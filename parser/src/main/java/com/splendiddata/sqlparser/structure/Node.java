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
import jakarta.xml.bind.annotation.XmlTransient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Base class for all sql nodes
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class Node implements Cloneable {
    @XmlAttribute(name = "nodeType")
    public NodeTag type;

    /**
     * The offset of this Node relative to the start of the sql stream
     */
    @XmlTransient
    public Location location;

    /**
     * just the class name to be represented in an XML structure for debugging purposes
     */
    @XmlAttribute(name = "class")
    private final String className = getClass().getSimpleName();
    
    /**
     * Constructor
     */
    public Node() {
        try {
            type = NodeTag.valueOf("T_" + getClass().getSimpleName());
        } catch (IllegalArgumentException e) {
            Logger log = LogManager.getLogger(getClass());
            if (log.isDebugEnabled()) {
                log.debug("NodeTag.T_" + getClass().getSimpleName() + " does not exist");
            }
        }
    }

    /**
     * Copy constructor
     *
     * @param other
     *            The node to copy
     */
    public Node(Node other) {
        this.type = other.type;
        this.location = other.location;
    }

    /**
     * Constructor
     *
     * @param type
     *            The type of the node to construct
     */
    protected Node(NodeTag type) {
        this.type = type;
    }

    /**
     * Returns the start offset of the node in the sql statement
     *
     * @return long the start offset of this node or -1 if unknown
     */
    @XmlTransient
    public long getStartOffset() {
        if (location == null || location.begin == null) {
            return -1;
        }
        return location.begin.getOffset();
    }

    @Override
    public Node clone() {
        try {
            Node clone = (Node) super.clone();
            if (location != null) {
                clone.location = location.clone();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("com.splendiddata.sqlparser.structure.Node.clone()->failed", e);
        }
    }
    
    /**
     * @return NodeTag the type of node
     */
    public final NodeTag getNodeType() {
        return type;
    }

    /**
     * @return String returns the location as String to be represented in an XML structure for debugging purposes
     */
    @XmlAttribute(name = "location")
    private String getLocationString() {
        return location == null ? null : location.toString();
    }
}
