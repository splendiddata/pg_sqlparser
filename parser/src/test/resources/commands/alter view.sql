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

alter view if exists schema.name alter column "attribute name" set default 15;
alter view view alter coll drop default;
alter view schema.type owner to another_owner;
alter view schema.type rename to anothername;
alter view schema.type set schema another_schema;
alter view if exists view set(minimal_3 = check_option ('value < 3'), another_option, "who knows what" = security_barrier (true));
alter view schema.view reset(minimal_3, another_option, "who knows what");