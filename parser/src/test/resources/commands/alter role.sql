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

alter role role with superuser createdb nocreaterole inherit login noreplication connection limit 0 password '' valid until 'infinity';
alter role role with encrypted password 'pwd';
alter role role with password null nologin;
alter role role with nosuperuser nocreatedb createrole noinherit nologin replication connection limit 9999 password 'some ''good'' password' valid until '9999-12-31';
alter role role rename to renamed_role;
alter role someone set config_param = 123456;
alter role someone in database database set config_param to 123456;
alter role the_role set config_param from current;
alter role all set config_param from current;
alter role the_role in database db set config_param from current;
alter role all in database db set config_param from current;
alter role the_role reset config_param;
alter role all reset config_param;
alter role the_role in database db reset config_param;
alter role all in database db reset config_param;
alter role the_role reset all;
alter role all reset all;
alter role the_role in database db reset all;
alter role all in database db reset all;