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

alter default privileges grant insert, update, delete, truncate, references, trigger on tables to group "group", group1, group group2 with grant option;
alter default privileges grant all on tables to group group1, group group2;
alter default privileges for role role1, role2 grant all privileges on sequences to group a;
alter default privileges in schema a_schema revoke grant option for execute on functions from public cascade;
alter default privileges for role role1 in schema schema revoke usage on types from group1, group2;