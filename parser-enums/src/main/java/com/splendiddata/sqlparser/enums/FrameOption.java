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
 * obtained from /postgresql-11.0/src/include/nodes/parsenodes.h
 *
 * frameOptions is an OR of these bits. The NONDEFAULT and BETWEEN bits are used so that ruleutils.c can tell which
 * properties were specified and which were defaulted; the correct behavioral bits must be set either way. The START_foo
 * and END_foo options must come in pairs of adjacent bits for the convenience of gram.y, even though some of them are
 * useless/invalid.
 * 
 * Note that this is mere a bitmap than an enum.
 */
public class FrameOption {

    /** any specified? */
    public static final int FRAMEOPTION_NONDEFAULT = 0x00001;
    /** RANGE behavior */
    public static final int FRAMEOPTION_RANGE = 0x00002;
    /** ROWS behavior */
    public static final int FRAMEOPTION_ROWS = 0x00004;
    /** GROUPS behavior */
    public static final int FRAMEOPTION_GROUPS = 0x00008;
    /** BETWEEN given? */
    public static final int FRAMEOPTION_BETWEEN = 0x00010;
    /** start is U. P. */
    public static final int FRAMEOPTION_START_UNBOUNDED_PRECEDING = 0x00020;
    /** (disallowed) */
    public static final int FRAMEOPTION_END_UNBOUNDED_PRECEDING = 0x00040;
    /** (disallowed) */
    public static final int FRAMEOPTION_START_UNBOUNDED_FOLLOWING = 0x00080;
    /** end is U. F. */
    public static final int FRAMEOPTION_END_UNBOUNDED_FOLLOWING = 0x00100;
    /** start is C. R. */
    public static final int FRAMEOPTION_START_CURRENT_ROW = 0x00200;
    /** end is C. R. */
    public static final int FRAMEOPTION_END_CURRENT_ROW = 0x00400;
    /** start is O. P. */
    public static final int FRAMEOPTION_START_OFFSET_PRECEDING = 0x00800;
    /** end is O. P. */
    public static final int FRAMEOPTION_END_OFFSET_PRECEDING = 0x01000;
    /** start is O. F. */
    public static final int FRAMEOPTION_START_OFFSET_FOLLOWING = 0x02000;
    /** end is O. F. */
    public static final int FRAMEOPTION_END_OFFSET_FOLLOWING = 0x04000;
    /** omit C.R. */
    public static final int FRAMEOPTION_EXCLUDE_CURRENT_ROW = 0x08000;
    /** omit C.R. &amp; peers */
    public static final int FRAMEOPTION_EXCLUDE_GROUP = 0x10000;
    /** omit C.R.'s peers */
    public static final int FRAMEOPTION_EXCLUDE_TIES = 0x20000;

    public static final int FRAMEOPTION_START_OFFSET = FRAMEOPTION_START_OFFSET_PRECEDING
            | FRAMEOPTION_START_OFFSET_FOLLOWING;
    public static final int FRAMEOPTION_END_OFFSET = FRAMEOPTION_END_OFFSET_PRECEDING
            | FRAMEOPTION_END_OFFSET_FOLLOWING;
    public static final int FRAMEOPTION_EXCLUSION = FRAMEOPTION_EXCLUDE_CURRENT_ROW | FRAMEOPTION_EXCLUDE_GROUP
            | FRAMEOPTION_EXCLUDE_TIES;

    public static final int FRAMEOPTION_DEFAULTS = FRAMEOPTION_RANGE | FRAMEOPTION_START_UNBOUNDED_PRECEDING
            | FRAMEOPTION_END_CURRENT_ROW;

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART = "FRAMEOPTION_NONDEFAULT"
            + "|FRAMEOPTION_RANGE|FRAMEOPTION_ROWS|FRAMEOPTION_GROUPS|FRAMEOPTION_BETWEEN"
            + "|FRAMEOPTION_START_UNBOUNDED_PRECEDING|FRAMEOPTION_END_UNBOUNDED_PRECEDING"
            + "|FRAMEOPTION_START_UNBOUNDED_FOLLOWING|FRAMEOPTION_END_UNBOUNDED_FOLLOWING"
            + "|FRAMEOPTION_START_CURRENT_ROW|FRAMEOPTION_END_CURRENT_ROW"
            + "|FRAMEOPTION_START_OFFSET_PRECEDING|FRAMEOPTION_END_OFFSET_PRECEDING"
            + "|FRAMEOPTION_START_OFFSET_FOLLOWING|FRAMEOPTION_END_OFFSET_FOLLOWING"
            + "|FRAMEOPTION_EXCLUDE_CURRENT_ROW|FRAMEOPTION_EXCLUDE_GROUP"
            + "|FRAMEOPTION_EXCLUDE_TIES|FRAMEOPTION_END_OFFSET|FRAMEOPTION_EXCLUSION|FRAMEOPTION_DEFAULTS";

}
