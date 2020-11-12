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

create domain domain as a.type;
create domain us_postal_code as text check("value" ~ '^\d{5}$' or "value" ~ '^\d{5}-\d{4}$');
create domain dom as text collate coll default 'default' not null;
create domain dom 
    as integer default 
    nextval('some_sequence') 
    constraint not_null_constraint not null;
create domain my.domain 
    integer 
    default nextval('some_sequence') 
    constraint not_null_constraint not null 
    constraint "must be > 10" check(value > 10);