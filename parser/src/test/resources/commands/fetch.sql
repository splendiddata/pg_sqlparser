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

fetch cursor_name;
fetch next "cursor name";
fetch prior from my_cursor;
fetch first in my_cursor;
fetch last cursor;
fetch absolute 7 from my_cursor;
fetch relative -10 from my_cursor;
fetch 5 from my_cursor;
fetch all my_cursor;
fetch forward cursor;
fetch forward 20 in my_cursor;
fetch forward all from my_cursor;
fetch backward from my_cursor;
fetch backward 4 in my_cursor;
fetch backward all in my_cursor;
