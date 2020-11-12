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

import java.io.Serializable;

/**
 * ListCell as defined in /postgresql-9.3.4/src/include/nodes/pg_list.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public class ListCell<T> implements Serializable {
    private static final long serialVersionUID = 400L;

    public T data;
    public ListCell<T> next;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("<ListCell");
        if (next == null) {
            result.append(" next=\"null\"");
        } else {
            result.append(" next=\"filled\"");
        }
        if (data == null) {
            result.append(" data=\"null\"/>");
        } else {
            result.append(" dataClass=\"").append(data.getClass().getName()).append("\">").append(data)
                    .append("</ListCell>");
        }
        return result.toString();
    }
}
