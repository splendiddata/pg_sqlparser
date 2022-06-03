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

package com.splendiddata.sqlparser.structure;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-15beta1/src/backend/parser/gram.c
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class KeyActions {

    @XmlElement
    public KeyAction updateAction;

    @XmlElement
    public KeyAction deleteAction;

    /**
     * Constructor
     */
    public KeyActions() {
        super();
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The KeyActions to copy
     */
    public KeyActions(KeyActions orig) {
        if (orig.updateAction != null) {
            this.updateAction = orig.updateAction.clone();
        }
        if (orig.deleteAction != null) {
            this.deleteAction = orig.deleteAction.clone();
        }
    }

    @Override
    public KeyActions clone() {
        try {
            KeyActions clone = (KeyActions) super.clone();
            if (updateAction != null) {
                clone.updateAction = updateAction.clone();
            }
            if (deleteAction != null) {
                clone.deleteAction = deleteAction.clone();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("com.splendiddata.sqlparser.structure.Node.clone()->failed", e);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("????? please implement ").append(getClass().getName()).append(".toString()");

        return result.toString();
    }
}
