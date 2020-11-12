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

alter aggregate aggregate_name (in a text, in b int ) rename to another_name;
alter aggregate schema_name.aggregate_name (in a int, in b text order by in c text) rename to another_name;
alter aggregate aggregate_name (aggregate_signature) owner to another_owner;
alter aggregate schema_name.aggregate_name (aggregate_signature) owner to another_owner;
alter aggregate aggregate_name (aggregate_signature) set schema another_owner;
alter aggregate schema_name.aggregate_name (aggregate_signature) set schema another_owner;
