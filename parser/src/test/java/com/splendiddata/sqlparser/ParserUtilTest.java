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

package com.splendiddata.sqlparser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ParserUtilTest {
    @Test
    void testIdentifierToString() {
        String identifier = "identifier";
        String expected = "identifier";
        String actual = ParserUtil.identifierToSql(identifier);
        Assertions.assertEquals(expected, actual);
        
        identifier = "IdEnTiFiEr";
        expected = "\"IdEnTiFiEr\"";
        actual = ParserUtil.identifierToSql(identifier);
        Assertions.assertEquals(expected, actual);

        
        identifier = "Iden\"ti\"fier";
        expected = "\"Iden\"\"ti\"\"fier\"";
        actual = ParserUtil.identifierToSql(identifier);
        Assertions.assertEquals(expected, actual);
        
        identifier = "Iden \"ti\" fier";
        expected = "\"Iden \"\"ti\"\" fier\"";
        actual = ParserUtil.identifierToSql(identifier);
        Assertions.assertEquals(expected, actual);
    }
}
