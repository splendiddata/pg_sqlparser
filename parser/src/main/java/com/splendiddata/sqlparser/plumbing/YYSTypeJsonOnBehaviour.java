/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2022
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

package com.splendiddata.sqlparser.plumbing;

import com.splendiddata.sqlparser.structure.JsonBehavior;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * on_behavior struct that may be used as YYSType
 * <p>
 * Copied from postgresql-15beta1/src/backend/parser/gram.y
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class YYSTypeJsonOnBehaviour implements Cloneable {

    @XmlElement
    public JsonBehavior on_empty;

    @XmlElement
    public JsonBehavior on_error;

    /**
     * Constructor
     */
    public YYSTypeJsonOnBehaviour() {
        super();
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonYystypeUnion to copy
     */
    public YYSTypeJsonOnBehaviour(YYSTypeJsonOnBehaviour orig) {
        super();
        if (orig.on_empty != null) {
            this.on_empty = orig.on_empty;
        }
        if (orig.on_error != null) {
            this.on_error = orig.on_error;
        }
    }

    @Override
    public YYSTypeJsonOnBehaviour clone() {
        YYSTypeJsonOnBehaviour clone;
        try {
            clone = (YYSTypeJsonOnBehaviour) super.clone();
        } catch (CloneNotSupportedException e) {
            clone = new YYSTypeJsonOnBehaviour();
        }
        if (on_empty != null) {
            clone.on_empty = on_empty;
        }
        if (on_error != null) {
            clone.on_error = on_error;
        }
        return clone;
    }
}
