/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class DoStmt extends Node {
    
    /** List of DefElem nodes */
    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    public List<DefElem> args;
    
    /**
     * Constructor
     */
    public DoStmt() {
        super(NodeTag.T_DoStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The DoStmt to copy
     */
    public DoStmt(DoStmt original) {
        super(original);
        if (original.args != null) {
            this.args = original.args.clone();
        }
    }

    @Override
    public DoStmt clone() {
        DoStmt clone = (DoStmt) super.clone();
        if (args != null) {
            clone.args = args.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("do");
        for (DefElem arg : args) {
            switch (arg.defname) {
            case "language":
                result.append(" language ").append(arg.arg);
                break;
            case "as":
                String argString = arg.arg.toString().replace("$$", "$");
                String quote = "$$";
                for (int i = 0; argString.contains(quote); i++) {
                    quote = "$_" + i + "_$";
                }
                result.append(' ').append(quote).append(argString).append(quote);
                break;
            default:
                result.append(' ').append(ParserUtil.reportUnknownValue("arg", arg.defname, getClass()));
                break;
            }
        }
        return result.toString();
    }
}
