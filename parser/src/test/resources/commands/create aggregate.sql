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

create aggregate aggregate.name (in a text, in b schema.type) (
    sfunc = schema.sfunc(internal_tate, next_data_values ),
    stype = schem.state_data_type,
    sspace = 50000,
    finalfunc = schema.ffunc(internal_state),
    finalfunc_extra,
    initcond = 'initial_condition',
    msfunc = msfunc(mstate_data_type),
    minvfunc = minvfunc(mstate_data_type),
    mstype = mstate_data_type,
    psspace = 30,
    mfinalfunc = mffunc(mstate_data_type),
    mfinalfunc_extra,
    minitcond = initial_condition,
    sortop = my_schema."<="
);

create aggregate aggregate.name (in a text, in b schema.type) (
    sfunc = schema.sfunc(internal_tate, next_data_values ),
    stype = schem.state_data_type,
    sspace = 50000,
    finalfunc = schema.ffunc(internal_state),
    finalfunc_extra,
    initcond = 'initial_condition',
    msfunc = msfunc(mstate_data_type),
    minvfunc = minvfunc(mstate_data_type),
    mstype = mstate_data_type,
    psspace = 30,
    mfinalfunc = mffunc(mstate_data_type),
    mfinalfunc_extra,
    minitcond = initial_condition,
    sortop = 'my_schema.<<>>'
);
create aggregate aggregate.name (in a text, in b schema.type) (
    sfunc = schema.sfunc(internal_tate, next_data_values ),
    stype = schem.state_data_type
);

create aggregate agg (text, int order by float, varchar, a.b, c) (
    sfunc = schema.sfunc(internal_tate, next_data_values ),
    stype = schem.state_data_type,
    sspace = 50000,
    finalfunc = schema.ffunc(internal_state),
    finalfunc_extra,
    initcond = 'initial_condition',
    sfunc = msfunc(mstate_data_type),
    stype = state_data_type,
	hypothetical
);

create aggregate agg (text, int order by float, varchar, a.b, c) (
    sfunc = schema.sfunc(internal_tate, next_data_values ),
    stype = schem.state_data_type
);

create aggregate aggregate.name (
    basetype = schema.base_type,
    sfunc = schema.sfunc(internal_tate, next_data_values ),
    stype = schem.state_data_type,
    sspace = 50000,
    finalfunc = schema.ffunc(internal_state),
--    finalfunc_extra,
    initcond = 'initial_condition',
    msfunc = msfunc(mstate_data_type),
    minvfunc = minvfunc(mstate_data_type),
    mstype = mstate_data_type,
    psspace = 30,
    mfinalfunc = mffunc(mstate_data_type),
--    mfinalfunc_extra,
    minitcond = initial_condition,
    sortop = <=
);

create aggregate aggregate.name (
    basetype = schema.base_type,
    sfunc = schema.sfunc(internal_tate, next_data_values ),
    stype = schem.state_data_type
);