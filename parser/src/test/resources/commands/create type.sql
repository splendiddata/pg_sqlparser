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

create type name;

create type schema.type as ();
create type type as (a text);
create type my.type as (a text collate coll, b integer, "C"  numeric(15, 2));

create type schema.enum as enum ();
create type schema.enum as enum ('a');
create type enum as enum ('a', 'b', 'c');

create type type as range
( subtype = subtype
);
create type schema.type as range
( subtype = schema.subtype
, subtype_opclass = schema.subtype_opclass
, collation = coll
, canonical = schema.canonical_function
, subtype_diff = schema.subtype_diff_function
);

create type name
( input = input_function
, output = output_function
);
create type schema.type 
( input = schema.input_function
, output = schema.output_function
, receive = schema.receive_function
, send = schema.send_function
, typmod_in = schema.type_modifier_input_function
, typmod_out = schema.type_modifier_output_function
, analyze = schema.analyze_function
, internallength = 123
, passedbyvalue
, alignment = 8
, storage = external
, like = schema.like_type
, category = n
, preferred = true
, default = 99999999999999999
, element = double
, delimiter = '|'
, collatable = true
);
create type schema.type 
( input = input_function
, output = schema.output_function
, internallength = variable
, alignment = 2
, storage = main
);