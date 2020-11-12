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

copy a to '/some/file';
copy (select * from schema.table) to program 'program.exe';
copy (select a, 'b', c from schema.table) to stdout with (format format, oids true, freeze false, delimiter '''', null 'Null');
copy schema.table(a,b,c) from stdin with (header, quote '''', esc '\', force_quote (a,c), force_not_null(b), force_null(d), encoding 'ebcdic');
copy schema.table(a,b,c) from '/file/name' with (force_quote *);
copy my_table from program '/file/name.exe';