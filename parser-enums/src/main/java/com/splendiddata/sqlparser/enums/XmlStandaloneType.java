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

package com.splendiddata.sqlparser.enums;

/**
 * Copied from /postgresql-9.3.4/src/include/utils/xml.h
 * <p>This class is not implemented as enum because the actual values from the parser are in A_Const integer objects</p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public final class XmlStandaloneType {
    public static final int XML_STANDALONE_YES = 0;
    public static final int XML_STANDALONE_NO = XML_STANDALONE_YES + 1;
    public static final int XML_STANDALONE_NO_VALUE = XML_STANDALONE_NO + 1;
    public static final int XML_STANDALONE_OMITTED = XML_STANDALONE_NO_VALUE + 1;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART = "XML_STANDALONE_YES|XML_STANDALONE_NO|XML_STANDALONE_NO_VALUE|XML_STANDALONE_OMITTED";
}
