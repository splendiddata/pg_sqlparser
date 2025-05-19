/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2025
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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import org.apache.logging.log4j.LogManager;

import com.splendiddata.sqlparser.enums.LimitOption;

/**
 * Private struct for the result of opt_select_limit production
 * <p>
 * copied from postgresql-13beta1/src/backend/parser/gram.c
 *
 * @author Splendid Data Product Development B.V.
 * @since 8.0 - Postgres version 13
 */
@XmlRootElement(namespace = "parser")
public class SelectLimit {

    @XmlElement
    public Node limitOffset;

    /** indicates presence of WITH TIES */
    @XmlElement
    public Node limitCount;

    @XmlAttribute
    public LimitOption limitOption;

    /**
     * location of OFFSET token, if present
     * 
     * @since Postgres 18
     */
    @XmlTransient
    public Location offsetLoc;

    /** location of LIMIT/FETCH token, if present */
    @XmlTransient
    public Location countLoc;

    /**
     * location of WITH TIES, if present
     * 
     * @since Postgres 18
     */
    @XmlTransient
    public Location optionLoc;

    /**
     * Constructor
     */
    public SelectLimit() {
        // empty
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The SelectLimit to copy
     */
    public SelectLimit(SelectLimit original) {
        if (original.limitOffset != null) {
            this.limitOffset = original.limitOffset.clone();
        }
        if (original.limitCount != null) {
            this.limitCount = original.limitCount.clone();
        }
        this.limitOption = original.limitOption;
    }

    /**
     * @see java.lang.Object#clone()
     *
     * @return SelectLimit the clone
     */
    @Override
    public SelectLimit clone() {
        SelectLimit clone;
        try {
            clone = (SelectLimit) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(SelectLimit.class.getName() + ".clone()->failed", e);
        }
        if (this.limitOffset != null) {
            clone.limitOffset = this.limitOffset.clone();
        }
        if (this.limitCount != null) {
            clone.limitCount = this.limitCount.clone();
        }
        return clone;
    }

    /**
     * @see java.lang.Object#toString()
     *
     * @return String the limit clause
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (limitOption != null) {
            switch (limitOption) {
            case LIMIT_OPTION_COUNT:
                if (limitOffset != null) {
                    result.append("offset ").append(limitOffset).append(' ');
                }
                result.append("fetch first");
                if (limitCount != null) {
                    result.append(' ').append(limitCount);
                }
                result.append(" only");
                break;
            case LIMIT_OPTION_DEFAULT:
                if (limitCount != null) {
                    result.append("limit").append(limitCount);
                }
                if (limitOffset != null) {
                    if (result.length() > 0) {
                        result.append(' ');
                    }
                    result.append("offset ").append(limitOffset);
                }
                break;
            case LIMIT_OPTION_WITH_TIES:
                if (limitOffset != null) {
                    result.append("offset ").append(limitOffset).append(' ');
                }
                result.append("fetch first");
                if (limitCount != null) {
                    result.append(' ').append(limitCount);
                }
                result.append(" with ties");
                break;
            default:
                LogManager.getLogger(getClass())
                        .error(new StringBuilder("Please implement ").append(LimitOption.class.getName())
                                .append(" value ").append(limitOption).append(" in the toString() method of ")
                                .append(getClass().getName()));
                break;
            }
        }
        return result.toString();
    }

    /**
     * @return String returns the offsetLoc as String to be represented in an XML structure for debugging purposes
     */
    @XmlAttribute(name = "offsetLoc")
    private String getOffsetLocString() {
        return offsetLoc == null ? null : offsetLoc.toString();
    }

    /**
     * @return String returns the countLoc as String to be represented in an XML structure for debugging purposes
     */
    @XmlAttribute(name = "countLoc")
    private String getCountLocString() {
        return countLoc == null ? null : countLoc.toString();
    }

    /**
     * @return String returns the optionLoc as String to be represented in an XML structure for debugging purposes
     */
    @XmlAttribute(name = "optionLoc")
    private String getOptionLocString() {
        return optionLoc == null ? null : optionLoc.toString();
    }
}
