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

/**
 * A class defining a pair of positions. Positions, defined by the <code>Position</code> class, denote a point in the
 * input. Locations represent a part of the input through the beginning and ending positions.
 */
public class Location implements Cloneable, Comparable<Location> {
    /** The first, inclusive, position in the range. */
    public Position begin;

    /** The first position beyond the range. */
    public Position end;

    /**
     * Create a <code>Location</code> denoting an empty range located at a given point.
     * 
     * @param loc
     *            The position at which the range is anchored.
     */
    public Location(Position loc) {
        this.begin = this.end = loc;
    }

    /**
     * Create a <code>Location</code> from the endpoints of the range.
     * 
     * @param begin
     *            The first position included in the range.
     * @param end
     *            The first position beyond the range.
     */
    public Location(Position begin, Position end) {
        this.begin = begin;
        this.end = end;
    }

    /**
     * Print a representation of the location. For this to be correct, <code>Position</code> should override the
     * <code>equals</code> method.
     */
    @Override
    public String toString() {
        if (begin == null) {
            return "null";
        }
        if (end == null || begin.equals(end)) {
            return begin.toString();
        }
        return begin.toString() + "-" + end.toString();
    }

    @Override
    public Location clone() {
        try {
            Location clone = (Location) super.clone();
            if (begin != null) {
                clone.begin = begin.clone();
            }
            if (end != null) {
                clone.end = end.clone();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("com.splendiddata.sqlparser.structure.Location.clone()->failed", e);
        }
    }

    /**
     * Compares the begin positions of the locations
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Location o) {
        if (o == null) {
            return 1;
        }
        if (begin == null) {
            if (o.begin == null) {
                return 0;
            }
            return 1;
        }
        return begin.compareTo(o.begin);
    }
}