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
import javax.xml.bind.annotation.XmlTransient;

import com.splendiddata.sqlparser.enums.FrameOption;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class WindowDef extends Node {
    /** window's own name */
    @XmlAttribute
    public String name;

    /** referenced window name, if any */
    @XmlAttribute
    public String refname;

    /** PARTITION BY expression list */
    @XmlElementWrapper(name = "partitionClause")
    @XmlElement(name = "partitionBy")
    public List<Node> partitionClause;

    /** ORDER BY (list of SortBy) */
    @XmlElementWrapper(name = "orderClause")
    @XmlElement(name = "sortBy")
    public List<SortBy> orderClause;

    /** frame_clause options, see below */
    @XmlTransient
    public int frameOptions;

    /** expression for starting bound, if any */
    @XmlElement
    public Node startOffset;

    /** expression for ending bound, if any */
    @XmlElement
    public Node endOffset;

    /**
     * Constructor
     */
    public WindowDef() {
        super(NodeTag.T_WindowDef);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The WindowDef to copy
     */
    public WindowDef(WindowDef original) {
        super(original);
        this.name = original.name;
        this.refname = original.refname;
        if (original.partitionClause != null) {
            this.partitionClause = original.partitionClause.clone();
        }
        if (original.orderClause != null) {
            this.orderClause = original.orderClause.clone();
        }
        this.frameOptions = original.frameOptions;
        if (original.startOffset != null) {
            this.startOffset = original.startOffset.clone();
        }
        if (original.endOffset != null) {
            this.endOffset = original.endOffset.clone();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("(");
        String leadingSpace = "";
        if (refname != null) {
            result.append(refname);
            leadingSpace = " ";
        }

        if (partitionClause != null) {
            result.append(leadingSpace).append("partition by");
            leadingSpace = " ";
            String separator = " ";
            for (Node node : partitionClause) {
                result.append(separator).append(node);
                separator = ", ";
            }
        }

        if (orderClause != null) {
            result.append(leadingSpace).append("order by");
            leadingSpace = " ";
            String separator = " ";
            for (SortBy sortBy : orderClause) {
                result.append(separator).append(sortBy);
                separator = ", ";
            }
        }

        if (frameOptions == FrameOption.FRAMEOPTION_DEFAULTS) {
            return result.append(')').toString();
        }

        if ((frameOptions & FrameOption.FRAMEOPTION_RANGE) != 0) {
            result.append(" range");
        } else if ((frameOptions & FrameOption.FRAMEOPTION_ROWS) != 0) {
            result.append(" rows");
        } else if ((frameOptions & FrameOption.FRAMEOPTION_GROUPS) != 0) {
            result.append(" groups");
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_BETWEEN) != 0) {
            result.append(" between");
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_START_UNBOUNDED_PRECEDING) != 0) {
            result.append(" unbounded preceding");
        } else if ((frameOptions & FrameOption.FRAMEOPTION_START_UNBOUNDED_FOLLOWING) != 0) {
            result.append(" unbounded following");
        } else if ((frameOptions & FrameOption.FRAMEOPTION_START_CURRENT_ROW) != 0) {
            result.append(" current row");
        } else if ((frameOptions & FrameOption.FRAMEOPTION_START_OFFSET_PRECEDING) != 0) {
            result.append(' ').append(startOffset).append(" preceding");
        } else if ((frameOptions & FrameOption.FRAMEOPTION_START_OFFSET_FOLLOWING) != 0) {
            result.append(' ').append(startOffset).append(" following");
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_BETWEEN) != 0) {
            result.append(" and");
            if ((frameOptions & FrameOption.FRAMEOPTION_END_UNBOUNDED_PRECEDING) != 0) {
                result.append(" unbounded preceding");
            } else if ((frameOptions & FrameOption.FRAMEOPTION_END_UNBOUNDED_FOLLOWING) != 0) {
                result.append(" unbounded following");
            } else if ((frameOptions & FrameOption.FRAMEOPTION_END_CURRENT_ROW) != 0) {
                result.append(" current row");
            } else if ((frameOptions & FrameOption.FRAMEOPTION_END_OFFSET_PRECEDING) != 0) {
                result.append(' ').append(endOffset).append(" preceding");
            } else if ((frameOptions & FrameOption.FRAMEOPTION_END_OFFSET_FOLLOWING) != 0) {
                result.append(' ').append(endOffset).append(" following");
            }
        }
        result.append(')');
        return result.toString();
    }

    @Override
    public WindowDef clone() {
        WindowDef clone = (WindowDef) super.clone();
        if (partitionClause != null) {
            clone.partitionClause = partitionClause.clone();
        }
        if (orderClause != null) {
            clone.orderClause = orderClause.clone();
        }
        if (startOffset != null) {
            clone.startOffset = startOffset.clone();
        }
        if (endOffset != null) {
            clone.endOffset = endOffset.clone();
        }
        return clone;
    }

    /**
     * Returns the frameOptions decimal and in a readable format for debugging purposes
     *
     * @return String The frame options
     */
    @XmlAttribute(name = "frameOptions")
    private String getFrameOptionsString() {
        StringBuilder result = new StringBuilder();
        char separator = '=';
        result.append(frameOptions);
        if ((frameOptions & FrameOption.FRAMEOPTION_NONDEFAULT) != 0) {
            result.append(separator).append("FRAMEOPTION_NONDEFAULT");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_RANGE) != 0) {
            result.append(separator).append("FRAMEOPTION_RANGE");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_ROWS) != 0) {
            result.append(separator).append("FRAMEOPTION_ROWS");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_GROUPS) != 0) {
            result.append(separator).append("FRAMEOPTION_GROUPS");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_BETWEEN) != 0) {
            result.append(separator).append("FRAMEOPTION_BETWEEN");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_START_UNBOUNDED_PRECEDING) != 0) {
            result.append(separator).append("FRAMEOPTION_START_UNBOUNDED_PRECEDING");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_END_UNBOUNDED_PRECEDING) != 0) {
            result.append(separator).append("FRAMEOPTION_END_UNBOUNDED_PRECEDING");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_START_UNBOUNDED_FOLLOWING) != 0) {
            result.append(separator).append("FRAMEOPTION_START_UNBOUNDED_FOLLOWING");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_END_UNBOUNDED_FOLLOWING) != 0) {
            result.append(separator).append("FRAMEOPTION_END_UNBOUNDED_FOLLOWING");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_START_CURRENT_ROW) != 0) {
            result.append(separator).append("FRAMEOPTION_START_CURRENT_ROW");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_END_CURRENT_ROW) != 0) {
            result.append(separator).append("FRAMEOPTION_END_CURRENT_ROW");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_START_OFFSET_PRECEDING) != 0) {
            result.append(separator).append("FRAMEOPTION_START_OFFSET_PRECEDING");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_END_OFFSET_PRECEDING) != 0) {
            result.append(separator).append("FRAMEOPTION_END_OFFSET_PRECEDING");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_START_OFFSET_FOLLOWING) != 0) {
            result.append(separator).append("FRAMEOPTION_START_OFFSET_FOLLOWING");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_END_OFFSET_FOLLOWING) != 0) {
            result.append(separator).append("FRAMEOPTION_END_OFFSET_FOLLOWING");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_EXCLUDE_CURRENT_ROW) != 0) {
            result.append(separator).append("FRAMEOPTION_EXCLUDE_CURRENT_ROW");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_EXCLUDE_GROUP) != 0) {
            result.append(separator).append("FRAMEOPTION_EXCLUDE_GROUP");
            separator = '|';
        }
        if ((frameOptions & FrameOption.FRAMEOPTION_EXCLUDE_TIES) != 0) {
            result.append(separator).append("FRAMEOPTION_EXCLUDE_TIES");
            separator = '|';
        }
        return result.toString();
    }

}
