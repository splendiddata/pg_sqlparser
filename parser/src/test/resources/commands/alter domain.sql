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

alter domain schema.domain set default 25;
alter domain domain drop default;
alter domain some_domain set not null;
alter domain a.domain drop not null;
alter domain a_domain add constraint size_check check(char_length(value) between 2 and 7);
alter domain a_domain rename constraint size_check to sizecheck;
alter domain a_domain drop constraint sizecheck;
alter domain a_domain drop constraint if exists sizecheck cascade;
alter domain schema.a_domain validate constraint "some constraint";
alter domain a_domain owner to "owner";
alter domain a_schema."do main" rename to domain;
alter domain a_schema."do main" set schema "a Schema";
alter domain a_domain add constraint size_check check(char_length(value) between 2 and 7) not valid;