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

alter user "user" with superuser createdb inherit login noreplication connection limit 0 password '' valid until 'infinity';
alter user "user" with encrypted password 'pwd';
alter user "user" with password null nologin;
alter user "user" with nosuperuser nocreatedb noinherit nologin replication connection limit 9999 password 'some ''good'' password' valid until '9999-12-31';
alter user "user" rename to renamed_user;
alter user someone set config_param = 123456;
alter user the_user set config_param from current;
alter user "all" set config_param from current;
alter user the_user reset config_param;
alter user "all" reset config_param;
alter user the_user reset all;
alter user "all" reset all;