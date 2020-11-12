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

create policy polname on my_schema.my_table with check (a > b);
create policy polname on my_schema.my_table for all with check (a > b);
create policy polname on my_schema.my_table for select to some_role with check (a > b);
create policy polname on my_schema.my_table for update to public using (b = c);
create policy polname on my_schema.my_table for update to current_user, session_user, some_role using (b = c) with check (do_check(a,b,c));
create policy polname on my_schema.my_table;