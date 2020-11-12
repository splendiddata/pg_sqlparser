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

alter type schema.name add attribute name schema.type collate coll cascade, add attribute x varchar(15), drop attribute z cascade, drop attribute if exists atribute;
alter type type alter attribute att_a set data type numeric(5,2) , alter attribute att_b type text collate coll cascade;
alter type schema.type owner to another_owner;
alter type type rename attribute a to b;
alter type schema.type rename attribute a to b cascade;
alter type schema.type rename to anothername;
alter type schema.type set schema another_schema;
alter type type add value 'x';
alter type schem.type add value if not exists 'x' before 'y';
alter type schem.type add value 'x' after 'w';