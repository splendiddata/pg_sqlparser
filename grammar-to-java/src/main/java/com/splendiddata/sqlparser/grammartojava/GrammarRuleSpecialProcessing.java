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

package com.splendiddata.sqlparser.grammartojava;


/**
 * Closure for GrammarRuleSpecial to perform some special processing where applicable for some grammar rules 
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
public interface GrammarRuleSpecialProcessing {
    /**
     * Perform special processing on a grammar line.
     * The default implementation returns the line unchanged.
     *
     * @param line  read from the input grammar
     * @return String line to be written to the resulting grammar
     */
    String processLine(String line);
}