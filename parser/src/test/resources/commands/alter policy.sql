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

alter policy polname on my_schema.my_table rename to another_policy_name;
alter policy polname on my_schema.my_table to public;
alter policy polname on my_schema.my_table to some_role, another_role using (a > b);
alter policy polname on my_schema.my_tabl;
alter policy polname on my_schema.my_table to current_user, session_user, some_role using (b = c) with check (do_check(a,b,c));
alter policy polname on my_schema.my_table with check (do_check(a,b,c));