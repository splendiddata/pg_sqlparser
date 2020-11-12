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

create text search parser schema.parser
( start = schema.start_function
, gettoken = schema.gettoken_function
, end = schema.end_function
, lextypes = schema.lextypes_function
);

create text search parser parser
( start = start_function
, gettoken = gettoken_function
, end = end_function
, lextypes = lextypes_function
, headline = headline_function
);