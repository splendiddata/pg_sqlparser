/*
 * This file has been altered by SplendidData.
 * It is only used for happy flow syntax checking, so erroneous statements are commented out here.
 * The deactivated lines are marked by: -- Deactivated for SplendidDataTest: 
 */
 
 
 

--
-- Tests for psql features that aren't closely connected to any
-- specific server features
--

-- \set

-- fail: invalid name
-- Deactivated for SplendidDataTest: \set invalid/name foo
-- fail: invalid value for special variable
-- Deactivated for SplendidDataTest: \set AUTOCOMMIT foo
-- Deactivated for SplendidDataTest: \set FETCH_COUNT foo
-- check handling of built-in boolean variable
-- Deactivated for SplendidDataTest: \echo :ON_ERROR_ROLLBACK
-- Deactivated for SplendidDataTest: \set ON_ERROR_ROLLBACK
-- Deactivated for SplendidDataTest: \echo :ON_ERROR_ROLLBACK
-- Deactivated for SplendidDataTest: \set ON_ERROR_ROLLBACK foo
-- Deactivated for SplendidDataTest: \echo :ON_ERROR_ROLLBACK
-- Deactivated for SplendidDataTest: \set ON_ERROR_ROLLBACK on
-- Deactivated for SplendidDataTest: \echo :ON_ERROR_ROLLBACK
-- Deactivated for SplendidDataTest: \unset ON_ERROR_ROLLBACK
-- Deactivated for SplendidDataTest: \echo :ON_ERROR_ROLLBACK

-- \g and \gx

-- Deactivated for SplendidDataTest: SELECT 1 as one, 2 as two \g
-- Deactivated for SplendidDataTest: \gx
-- Deactivated for SplendidDataTest: SELECT 3 as three, 4 as four \gx
-- Deactivated for SplendidDataTest: \g

-- \gx should work in FETCH_COUNT mode too
-- Deactivated for SplendidDataTest: \set FETCH_COUNT 1

-- Deactivated for SplendidDataTest: SELECT 1 as one, 2 as two \g
-- Deactivated for SplendidDataTest: \gx
-- Deactivated for SplendidDataTest: SELECT 3 as three, 4 as four \gx
-- Deactivated for SplendidDataTest: \g

-- Deactivated for SplendidDataTest: \unset FETCH_COUNT

-- \g/\gx with pset options

-- Deactivated for SplendidDataTest: SELECT 1 as one, 2 as two \g (format=csv csv_fieldsep='\t')
-- Deactivated for SplendidDataTest: \g
-- Deactivated for SplendidDataTest: SELECT 1 as one, 2 as two \gx (title='foo bar')
-- Deactivated for SplendidDataTest: \g

-- \gset

-- Deactivated for SplendidDataTest: select 10 as test01, 20 as test02, 'Hello' as test03 \gset pref01_

-- Deactivated for SplendidDataTest: \echo :pref01_test01 :pref01_test02 :pref01_test03

-- should fail: bad variable name
-- Deactivated for SplendidDataTest: select 10 as "bad name"
-- Deactivated for SplendidDataTest: \gset

-- Deactivated for SplendidDataTest: select 97 as "EOF", 'ok' as _foo \gset IGNORE
-- Deactivated for SplendidDataTest: \echo :IGNORE_foo :IGNOREEOF

-- multiple backslash commands in one line
-- Deactivated for SplendidDataTest: select 1 as x, 2 as y \gset pref01_ \\ \echo :pref01_x
-- Deactivated for SplendidDataTest: select 3 as x, 4 as y \gset pref01_ \echo :pref01_x \echo :pref01_y
-- Deactivated for SplendidDataTest: select 5 as x, 6 as y \gset pref01_ \\ \g \echo :pref01_x :pref01_y
-- Deactivated for SplendidDataTest: select 7 as x, 8 as y \g \gset pref01_ \echo :pref01_x :pref01_y

-- NULL should unset the variable
-- Deactivated for SplendidDataTest: \set var2 xyz
-- Deactivated for SplendidDataTest: select 1 as var1, NULL as var2, 3 as var3 \gset
-- Deactivated for SplendidDataTest: \echo :var1 :var2 :var3

-- \gset requires just one tuple
-- Deactivated for SplendidDataTest: select 10 as test01, 20 as test02 from generate_series(1,3) \gset
-- Deactivated for SplendidDataTest: select 10 as test01, 20 as test02 from generate_series(1,0) \gset

-- \gset should work in FETCH_COUNT mode too
-- Deactivated for SplendidDataTest: \set FETCH_COUNT 1

-- Deactivated for SplendidDataTest: select 1 as x, 2 as y \gset pref01_ \\ \echo :pref01_x
-- Deactivated for SplendidDataTest: select 3 as x, 4 as y \gset pref01_ \echo :pref01_x \echo :pref01_y
-- Deactivated for SplendidDataTest: select 10 as test01, 20 as test02 from generate_series(1,3) \gset
-- Deactivated for SplendidDataTest: select 10 as test01, 20 as test02 from generate_series(1,0) \gset

-- Deactivated for SplendidDataTest: \unset FETCH_COUNT

-- \gdesc

-- Deactivated for SplendidDataTest: SELECT
-- Deactivated for SplendidDataTest:     NULL AS zero,
-- Deactivated for SplendidDataTest:     1 AS one,
-- Deactivated for SplendidDataTest:     2.0 AS two,
-- Deactivated for SplendidDataTest:     'three' AS three,
-- Deactivated for SplendidDataTest:     $1 AS four,
-- Deactivated for SplendidDataTest:     sin($2) as five,
-- Deactivated for SplendidDataTest:     'foo'::varchar(4) as six,
-- Deactivated for SplendidDataTest:     CURRENT_DATE AS now
-- Deactivated for SplendidDataTest: \gdesc

-- should work with tuple-returning utilities, such as EXECUTE
PREPARE test AS SELECT 1 AS first, 2 AS second;
-- Deactivated for SplendidDataTest: EXECUTE test \gdesc
-- Deactivated for SplendidDataTest: EXPLAIN EXECUTE test \gdesc

-- should fail cleanly - syntax error
-- Deactivated for SplendidDataTest: SELECT 1 + \gdesc

-- check behavior with empty results
-- Deactivated for SplendidDataTest: SELECT \gdesc
-- Deactivated for SplendidDataTest: CREATE TABLE bububu(a int) \gdesc

-- subject command should not have executed
TABLE bububu;  -- fail

-- query buffer should remain unchanged
-- Deactivated for SplendidDataTest: SELECT 1 AS x, 'Hello', 2 AS y, true AS "dirty\name"
-- Deactivated for SplendidDataTest: \gdesc
-- Deactivated for SplendidDataTest: \g

-- all on one line
-- Deactivated for SplendidDataTest: SELECT 3 AS x, 'Hello', 4 AS y, true AS "dirty\name" \gdesc \g

-- \gexec

create temporary table gexec_test(a int, b text, c date, d float);
-- Deactivated for SplendidDataTest: select format('create index on gexec_test(%I)', attname)
-- Deactivated for SplendidDataTest: from pg_attribute
-- Deactivated for SplendidDataTest: where attrelid = 'gexec_test'::regclass and attnum > 0
-- Deactivated for SplendidDataTest: order by attnum
-- Deactivated for SplendidDataTest: \gexec

-- \gexec should work in FETCH_COUNT mode too
-- (though the fetch limit applies to the executed queries not the meta query)
-- Deactivated for SplendidDataTest: \set FETCH_COUNT 1

-- Deactivated for SplendidDataTest: select 'select 1 as ones', 'select x.y, x.y*2 as double from generate_series(1,4) as x(y)'
-- Deactivated for SplendidDataTest: union all
-- Deactivated for SplendidDataTest: select 'drop table gexec_test', NULL
-- Deactivated for SplendidDataTest: union all
-- Deactivated for SplendidDataTest: select 'drop table gexec_test', 'select ''2000-01-01''::date as party_over'
-- Deactivated for SplendidDataTest: \gexec

-- Deactivated for SplendidDataTest: \unset FETCH_COUNT

-- \setenv, \getenv

-- ensure MYVAR isn't set
-- Deactivated for SplendidDataTest: \setenv MYVAR
-- in which case, reading it doesn't change the target
-- Deactivated for SplendidDataTest: \getenv res MYVAR
-- Deactivated for SplendidDataTest: \echo :res
-- now set it
-- Deactivated for SplendidDataTest: \setenv MYVAR 'environment value'
-- Deactivated for SplendidDataTest: \getenv res MYVAR
-- Deactivated for SplendidDataTest: \echo :res

-- show all pset options
-- Deactivated for SplendidDataTest: \pset

-- test multi-line headers, wrapping, and newline indicators
-- in aligned, unaligned, and wrapped formats
prepare q as select array_to_string(array_agg(repeat('x',2*n)),E'\n') as "ab

c", array_to_string(array_agg(repeat('y',20-2*n)),E'\n') as "a
bc" from generate_series(1,10) as n(n) group by n>1 order by n>1;

-- Deactivated for SplendidDataTest: \pset linestyle ascii

-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \pset columns 40

-- Deactivated for SplendidDataTest: \pset border 0
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \pset columns 20

-- Deactivated for SplendidDataTest: \pset border 0
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset linestyle old-ascii

-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \pset columns 40

-- Deactivated for SplendidDataTest: \pset border 0
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \pset columns 20

-- Deactivated for SplendidDataTest: \pset border 0
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

deallocate q;

-- test single-line header and data
prepare q as select repeat('x',2*n) as "0123456789abcdef", repeat('y',20-2*n) as "0123456789" from generate_series(1,10) as n;

-- Deactivated for SplendidDataTest: \pset linestyle ascii

-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \pset columns 40

-- Deactivated for SplendidDataTest: \pset border 0
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \pset columns 30

-- Deactivated for SplendidDataTest: \pset border 0
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \pset columns 20

-- Deactivated for SplendidDataTest: \pset border 0
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset linestyle old-ascii

-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \pset columns 40

-- Deactivated for SplendidDataTest: \pset border 0
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset expanded on

-- Deactivated for SplendidDataTest: \pset border 0
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
-- Deactivated for SplendidDataTest: \pset format unaligned
execute q;
-- Deactivated for SplendidDataTest: \pset format aligned
execute q;
-- Deactivated for SplendidDataTest: \pset format wrapped
execute q;

deallocate q;

-- Deactivated for SplendidDataTest: \pset linestyle ascii
-- Deactivated for SplendidDataTest: \pset border 1

-- support table for output-format tests (useful to create a footer)

create table psql_serial_tab (id serial);

-- test header/footer/tuples_only behavior in aligned/unaligned/wrapped cases

-- Deactivated for SplendidDataTest: \pset format aligned

-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false
-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false
-- empty table is a special case for this format
select 1 where false;

-- Deactivated for SplendidDataTest: \pset format unaligned

-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false
-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false

-- Deactivated for SplendidDataTest: \pset format wrapped

-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false
-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false

-- check conditional am display
-- Deactivated for SplendidDataTest: \pset expanded off

CREATE SCHEMA tableam_display;
CREATE ROLE regress_display_role;
ALTER SCHEMA tableam_display OWNER TO regress_display_role;
SET search_path TO tableam_display;
CREATE ACCESS METHOD heap_psql TYPE TABLE HANDLER heap_tableam_handler;
SET ROLE TO regress_display_role;
-- Use only relations with a physical size of zero.
CREATE TABLE tbl_heap_psql(f1 int, f2 char(100)) using heap_psql;
CREATE TABLE tbl_heap(f1 int, f2 char(100)) using heap;
CREATE VIEW view_heap_psql AS SELECT f1 from tbl_heap_psql;
CREATE MATERIALIZED VIEW mat_view_heap_psql USING heap_psql AS SELECT f1 from tbl_heap_psql;
-- Deactivated for SplendidDataTest: \d+ tbl_heap_psql
-- Deactivated for SplendidDataTest: \d+ tbl_heap
-- Deactivated for SplendidDataTest: \set HIDE_TABLEAM off
-- Deactivated for SplendidDataTest: \d+ tbl_heap_psql
-- Deactivated for SplendidDataTest: \d+ tbl_heap
-- AM is displayed for tables, indexes and materialized views.
-- Deactivated for SplendidDataTest: \d+
-- Deactivated for SplendidDataTest: \dt+
-- Deactivated for SplendidDataTest: \dm+
-- But not for views and sequences.
-- Deactivated for SplendidDataTest: \dv+
-- Deactivated for SplendidDataTest: \set HIDE_TABLEAM on
-- Deactivated for SplendidDataTest: \d+
RESET ROLE;
RESET search_path;
DROP SCHEMA tableam_display CASCADE;
DROP ACCESS METHOD heap_psql;
DROP ROLE regress_display_role;

-- test numericlocale (as best we can without control of psql's locale)

-- Deactivated for SplendidDataTest: \pset format aligned
-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \pset numericlocale true

select n, -n as m, n * 111 as x, '1e90'::float8 as f
from generate_series(0,3) n;

-- Deactivated for SplendidDataTest: \pset numericlocale false

-- test asciidoc output format

-- Deactivated for SplendidDataTest: \pset format asciidoc

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false
-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false

prepare q as
  select 'some|text' as "a|title", '        ' as "empty ", n as int
  from generate_series(1,2) as n;

-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \pset border 0
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
execute q;

-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \pset border 0
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
execute q;

deallocate q;

-- test csv output format

-- Deactivated for SplendidDataTest: \pset format csv

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false
-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false

prepare q as
  select 'some"text' as "a""title", E'  <foo>\n<bar>' as "junk",
         '   ' as "empty", n as int
  from generate_series(1,2) as n;

-- Deactivated for SplendidDataTest: \pset expanded off
execute q;

-- Deactivated for SplendidDataTest: \pset expanded on
execute q;

deallocate q;

-- special cases
-- Deactivated for SplendidDataTest: \pset expanded off
select 'comma,comma' as comma, 'semi;semi' as semi;
-- Deactivated for SplendidDataTest: \pset csv_fieldsep ';'
select 'comma,comma' as comma, 'semi;semi' as semi;
select '\.' as data;
-- Deactivated for SplendidDataTest: \pset csv_fieldsep '.'
select '\' as d1, '' as d2;

-- illegal csv separators
-- Deactivated for SplendidDataTest: \pset csv_fieldsep ''
-- Deactivated for SplendidDataTest: \pset csv_fieldsep '\0'
-- Deactivated for SplendidDataTest: \pset csv_fieldsep '\n'
-- Deactivated for SplendidDataTest: \pset csv_fieldsep '\r'
-- Deactivated for SplendidDataTest: \pset csv_fieldsep '"'
-- Deactivated for SplendidDataTest: \pset csv_fieldsep ',,'

-- Deactivated for SplendidDataTest: \pset csv_fieldsep ','

-- test html output format

-- Deactivated for SplendidDataTest: \pset format html

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false
-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false

prepare q as
  select 'some"text' as "a&title", E'  <foo>\n<bar>' as "junk",
         '   ' as "empty", n as int
  from generate_series(1,2) as n;

-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \pset border 0
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
execute q;

-- Deactivated for SplendidDataTest: \pset tableattr foobar
execute q;
-- Deactivated for SplendidDataTest: \pset tableattr

-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \pset border 0
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
execute q;

-- Deactivated for SplendidDataTest: \pset tableattr foobar
execute q;
-- Deactivated for SplendidDataTest: \pset tableattr

deallocate q;

-- test latex output format

-- Deactivated for SplendidDataTest: \pset format latex

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false
-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false

prepare q as
  select 'some\more_text' as "a$title", E'  #<foo>%&^~|\n{bar}' as "junk",
         '   ' as "empty", n as int
  from generate_series(1,2) as n;

-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \pset border 0
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
execute q;

-- Deactivated for SplendidDataTest: \pset border 3
execute q;

-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \pset border 0
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
execute q;

-- Deactivated for SplendidDataTest: \pset border 3
execute q;

deallocate q;

-- test latex-longtable output format

-- Deactivated for SplendidDataTest: \pset format latex-longtable

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false
-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false

prepare q as
  select 'some\more_text' as "a$title", E'  #<foo>%&^~|\n{bar}' as "junk",
         '   ' as "empty", n as int
  from generate_series(1,2) as n;

-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \pset border 0
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
execute q;

-- Deactivated for SplendidDataTest: \pset border 3
execute q;

-- Deactivated for SplendidDataTest: \pset tableattr lr
execute q;
-- Deactivated for SplendidDataTest: \pset tableattr

-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \pset border 0
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
execute q;

-- Deactivated for SplendidDataTest: \pset border 3
execute q;

-- Deactivated for SplendidDataTest: \pset tableattr lr
execute q;
-- Deactivated for SplendidDataTest: \pset tableattr

deallocate q;

-- test troff-ms output format

-- Deactivated for SplendidDataTest: \pset format troff-ms

-- Deactivated for SplendidDataTest: \pset border 1
-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false
-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \d psql_serial_tab_id_seq
-- Deactivated for SplendidDataTest: \pset tuples_only true
-- Deactivated for SplendidDataTest: \df exp
-- Deactivated for SplendidDataTest: \pset tuples_only false

prepare q as
  select 'some\text' as "a\title", E'  <foo>\n<bar>' as "junk",
         '   ' as "empty", n as int
  from generate_series(1,2) as n;

-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \pset border 0
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
execute q;

-- Deactivated for SplendidDataTest: \pset expanded on
-- Deactivated for SplendidDataTest: \pset border 0
execute q;

-- Deactivated for SplendidDataTest: \pset border 1
execute q;

-- Deactivated for SplendidDataTest: \pset border 2
execute q;

deallocate q;

-- check ambiguous format requests

-- Deactivated for SplendidDataTest: \pset format a
-- Deactivated for SplendidDataTest: \pset format l

-- clean up after output format tests

drop table psql_serial_tab;

-- Deactivated for SplendidDataTest: \pset format aligned
-- Deactivated for SplendidDataTest: \pset expanded off
-- Deactivated for SplendidDataTest: \pset border 1

-- \echo and allied features

-- Deactivated for SplendidDataTest: \echo this is a test
-- Deactivated for SplendidDataTest: \echo -n without newline
-- Deactivated for SplendidDataTest: \echo with -n newline
-- Deactivated for SplendidDataTest: \echo '-n' with newline

-- Deactivated for SplendidDataTest: \set foo bar
-- Deactivated for SplendidDataTest: \echo foo = :foo

-- Deactivated for SplendidDataTest: \qecho this is a test
-- Deactivated for SplendidDataTest: \qecho foo = :foo

-- Deactivated for SplendidDataTest: \warn this is a test
-- Deactivated for SplendidDataTest: \warn foo = :foo

-- tests for \if ... \endif

-- Deactivated for SplendidDataTest: \if true
  select 'okay';
  select 'still okay';
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest:   not okay;
-- Deactivated for SplendidDataTest:   still not okay
-- Deactivated for SplendidDataTest: \endif

-- at this point query buffer should still have last valid line
-- Deactivated for SplendidDataTest: \g

-- \if should work okay on part of a query
select
-- Deactivated for SplendidDataTest:   \if true
    42
-- Deactivated for SplendidDataTest:   \else
-- Deactivated for SplendidDataTest:     (bogus
-- Deactivated for SplendidDataTest:   \endif
  forty_two;

-- Deactivated for SplendidDataTest: select \if false \\ (bogus \else \\ 42 \endif \\ forty_two;

-- test a large nested if using a variety of true-equivalents
-- Deactivated for SplendidDataTest: \if true
-- Deactivated for SplendidDataTest: 	\if 1
-- Deactivated for SplendidDataTest: 		\if yes
-- Deactivated for SplendidDataTest: 			\if on
-- Deactivated for SplendidDataTest: 				\echo 'all true'
-- Deactivated for SplendidDataTest: 			\else
-- Deactivated for SplendidDataTest: 				\echo 'should not print #1-1'
-- Deactivated for SplendidDataTest: 			\endif
-- Deactivated for SplendidDataTest: 		\else
-- Deactivated for SplendidDataTest: 			\echo 'should not print #1-2'
-- Deactivated for SplendidDataTest: 		\endif
-- Deactivated for SplendidDataTest: 	\else
-- Deactivated for SplendidDataTest: 		\echo 'should not print #1-3'
-- Deactivated for SplendidDataTest: 	\endif
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest: 	\echo 'should not print #1-4'
-- Deactivated for SplendidDataTest: \endif

-- test a variety of false-equivalents in an if/elif/else structure
-- Deactivated for SplendidDataTest: \if false
-- Deactivated for SplendidDataTest: 	\echo 'should not print #2-1'
-- Deactivated for SplendidDataTest: \elif 0
-- Deactivated for SplendidDataTest: 	\echo 'should not print #2-2'
-- Deactivated for SplendidDataTest: \elif no
-- Deactivated for SplendidDataTest: 	\echo 'should not print #2-3'
-- Deactivated for SplendidDataTest: \elif off
-- Deactivated for SplendidDataTest: 	\echo 'should not print #2-4'
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest: 	\echo 'all false'
-- Deactivated for SplendidDataTest: \endif

-- test true-false elif after initial true branch
-- Deactivated for SplendidDataTest: \if true
-- Deactivated for SplendidDataTest: 	\echo 'should print #2-5'
-- Deactivated for SplendidDataTest: \elif true
-- Deactivated for SplendidDataTest: 	\echo 'should not print #2-6'
-- Deactivated for SplendidDataTest: \elif false
-- Deactivated for SplendidDataTest: 	\echo 'should not print #2-7'
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest: 	\echo 'should not print #2-8'
-- Deactivated for SplendidDataTest: \endif

-- test simple true-then-else
-- Deactivated for SplendidDataTest: \if true
-- Deactivated for SplendidDataTest: 	\echo 'first thing true'
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest: 	\echo 'should not print #3-1'
-- Deactivated for SplendidDataTest: \endif

-- test simple false-true-else
-- Deactivated for SplendidDataTest: \if false
-- Deactivated for SplendidDataTest: 	\echo 'should not print #4-1'
-- Deactivated for SplendidDataTest: \elif true
-- Deactivated for SplendidDataTest: 	\echo 'second thing true'
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest: 	\echo 'should not print #5-1'
-- Deactivated for SplendidDataTest: \endif

-- invalid boolean expressions are false
-- Deactivated for SplendidDataTest: \if invalid boolean expression
-- Deactivated for SplendidDataTest: 	\echo 'will not print #6-1'
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest: 	\echo 'will print anyway #6-2'
-- Deactivated for SplendidDataTest: \endif

-- test un-matched endif
-- Deactivated for SplendidDataTest: \endif

-- test un-matched else
-- Deactivated for SplendidDataTest: \else

-- test un-matched elif
-- Deactivated for SplendidDataTest: \elif

-- test double-else error
-- Deactivated for SplendidDataTest: \if true
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest: \endif

-- test elif out-of-order
-- Deactivated for SplendidDataTest: \if false
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest: \elif
-- Deactivated for SplendidDataTest: \endif

-- test if-endif matching in a false branch
-- Deactivated for SplendidDataTest: \if false
-- Deactivated for SplendidDataTest:     \if false
-- Deactivated for SplendidDataTest:         \echo 'should not print #7-1'
-- Deactivated for SplendidDataTest:     \else
-- Deactivated for SplendidDataTest:         \echo 'should not print #7-2'
-- Deactivated for SplendidDataTest:     \endif
-- Deactivated for SplendidDataTest:     \echo 'should not print #7-3'
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest:     \echo 'should print #7-4'
-- Deactivated for SplendidDataTest: \endif

-- show that vars and backticks are not expanded when ignoring extra args
-- Deactivated for SplendidDataTest: \set foo bar
-- Deactivated for SplendidDataTest: \echo :foo :'foo' :"foo"
-- Deactivated for SplendidDataTest: \pset fieldsep | `nosuchcommand` :foo :'foo' :"foo"

-- show that vars and backticks are not expanded and commands are ignored
-- when in a false if-branch
-- Deactivated for SplendidDataTest: \set try_to_quit '\\q'
-- Deactivated for SplendidDataTest: \if false
-- Deactivated for SplendidDataTest: 	:try_to_quit
-- Deactivated for SplendidDataTest: 	\echo `nosuchcommand` :foo :'foo' :"foo"
-- Deactivated for SplendidDataTest: 	\pset fieldsep | `nosuchcommand` :foo :'foo' :"foo"
-- Deactivated for SplendidDataTest: 	\a
-- Deactivated for SplendidDataTest: 	\C arg1
-- Deactivated for SplendidDataTest: 	\c arg1 arg2 arg3 arg4
-- Deactivated for SplendidDataTest: 	\cd arg1
-- Deactivated for SplendidDataTest: 	\conninfo
-- Deactivated for SplendidDataTest: 	\copy arg1 arg2 arg3 arg4 arg5 arg6
-- Deactivated for SplendidDataTest: 	\copyright
-- Deactivated for SplendidDataTest: 	SELECT 1 as one, 2, 3 \crosstabview
-- Deactivated for SplendidDataTest: 	\dt arg1
-- Deactivated for SplendidDataTest: 	\e arg1 arg2
-- Deactivated for SplendidDataTest: 	\ef whole_line
-- Deactivated for SplendidDataTest: 	\ev whole_line
-- Deactivated for SplendidDataTest: 	\echo arg1 arg2 arg3 arg4 arg5
-- Deactivated for SplendidDataTest: 	\echo arg1
-- Deactivated for SplendidDataTest: 	\encoding arg1
-- Deactivated for SplendidDataTest: 	\errverbose
-- Deactivated for SplendidDataTest: 	\f arg1
-- Deactivated for SplendidDataTest: 	\g arg1
-- Deactivated for SplendidDataTest: 	\gx arg1
-- Deactivated for SplendidDataTest: 	\gexec
-- Deactivated for SplendidDataTest: 	SELECT 1 AS one \gset
-- Deactivated for SplendidDataTest: 	\h
-- Deactivated for SplendidDataTest: 	\?
-- Deactivated for SplendidDataTest: 	\html
-- Deactivated for SplendidDataTest: 	\i arg1
-- Deactivated for SplendidDataTest: 	\ir arg1
-- Deactivated for SplendidDataTest: 	\l arg1
-- Deactivated for SplendidDataTest: 	\lo arg1 arg2
-- Deactivated for SplendidDataTest: 	\lo_list
-- Deactivated for SplendidDataTest: 	\o arg1
-- Deactivated for SplendidDataTest: 	\p
-- Deactivated for SplendidDataTest: 	\password arg1
-- Deactivated for SplendidDataTest: 	\prompt arg1 arg2
-- Deactivated for SplendidDataTest: 	\pset arg1 arg2
-- Deactivated for SplendidDataTest: 	\q
-- Deactivated for SplendidDataTest: 	\reset
-- Deactivated for SplendidDataTest: 	\s arg1
-- Deactivated for SplendidDataTest: 	\set arg1 arg2 arg3 arg4 arg5 arg6 arg7
-- Deactivated for SplendidDataTest: 	\setenv arg1 arg2
-- Deactivated for SplendidDataTest: 	\sf whole_line
-- Deactivated for SplendidDataTest: 	\sv whole_line
-- Deactivated for SplendidDataTest: 	\t arg1
-- Deactivated for SplendidDataTest: 	\T arg1
-- Deactivated for SplendidDataTest: 	\timing arg1
-- Deactivated for SplendidDataTest: 	\unset arg1
-- Deactivated for SplendidDataTest: 	\w arg1
-- Deactivated for SplendidDataTest: 	\watch arg1
-- Deactivated for SplendidDataTest: 	\x arg1
	-- \else here is eaten as part of OT_FILEPIPE argument
-- Deactivated for SplendidDataTest: 	\w |/no/such/file \else
	-- \endif here is eaten as part of whole-line argument
-- Deactivated for SplendidDataTest: 	\! whole_line \endif
-- Deactivated for SplendidDataTest: 	\z
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest: 	\echo 'should print #8-1'
-- Deactivated for SplendidDataTest: \endif

-- :{?...} defined variable test
-- Deactivated for SplendidDataTest: \set i 1
-- Deactivated for SplendidDataTest: \if :{?i}
-- Deactivated for SplendidDataTest:   \echo '#9-1 ok, variable i is defined'
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest:   \echo 'should not print #9-2'
-- Deactivated for SplendidDataTest: \endif

-- Deactivated for SplendidDataTest: \if :{?no_such_variable}
-- Deactivated for SplendidDataTest:   \echo 'should not print #10-1'
-- Deactivated for SplendidDataTest: \else
-- Deactivated for SplendidDataTest:   \echo '#10-2 ok, variable no_such_variable is not defined'
-- Deactivated for SplendidDataTest: \endif

-- Deactivated for SplendidDataTest: SELECT :{?i} AS i_is_defined;

-- Deactivated for SplendidDataTest: SELECT NOT :{?no_such_var} AS no_such_var_is_not_defined;

-- SHOW_CONTEXT

-- Deactivated for SplendidDataTest: \set SHOW_CONTEXT never
do $$
begin
  raise notice 'foo';
  raise exception 'bar';
end $$;

-- Deactivated for SplendidDataTest: \set SHOW_CONTEXT errors
do $$
begin
  raise notice 'foo';
  raise exception 'bar';
end $$;

-- Deactivated for SplendidDataTest: \set SHOW_CONTEXT always
do $$
begin
  raise notice 'foo';
  raise exception 'bar';
end $$;

-- test printing and clearing the query buffer
SELECT 1;
-- Deactivated for SplendidDataTest: \p
-- Deactivated for SplendidDataTest: SELECT 2 \r
-- Deactivated for SplendidDataTest: \p
-- Deactivated for SplendidDataTest: SELECT 3 \p
-- Deactivated for SplendidDataTest: UNION SELECT 4 \p
-- Deactivated for SplendidDataTest: UNION SELECT 5
-- Deactivated for SplendidDataTest: ORDER BY 1;
-- Deactivated for SplendidDataTest: \r
-- Deactivated for SplendidDataTest: \p

-- tests for special result variables

-- working query, 2 rows selected
SELECT 1 AS stuff UNION SELECT 2;
-- Deactivated for SplendidDataTest: \echo 'error:' :ERROR
-- Deactivated for SplendidDataTest: \echo 'error code:' :SQLSTATE
-- Deactivated for SplendidDataTest: \echo 'number of rows:' :ROW_COUNT

-- syntax error
-- Deactivated for SplendidDataTest: SELECT 1 UNION;
-- Deactivated for SplendidDataTest: \echo 'error:' :ERROR
-- Deactivated for SplendidDataTest: \echo 'error code:' :SQLSTATE
-- Deactivated for SplendidDataTest: \echo 'number of rows:' :ROW_COUNT
-- Deactivated for SplendidDataTest: \echo 'last error message:' :LAST_ERROR_MESSAGE
-- Deactivated for SplendidDataTest: \echo 'last error code:' :LAST_ERROR_SQLSTATE

-- empty query
;
-- Deactivated for SplendidDataTest: \echo 'error:' :ERROR
-- Deactivated for SplendidDataTest: \echo 'error code:' :SQLSTATE
-- Deactivated for SplendidDataTest: \echo 'number of rows:' :ROW_COUNT
-- must have kept previous values
-- Deactivated for SplendidDataTest: \echo 'last error message:' :LAST_ERROR_MESSAGE
-- Deactivated for SplendidDataTest: \echo 'last error code:' :LAST_ERROR_SQLSTATE

-- other query error
DROP TABLE this_table_does_not_exist;
-- Deactivated for SplendidDataTest: \echo 'error:' :ERROR
-- Deactivated for SplendidDataTest: \echo 'error code:' :SQLSTATE
-- Deactivated for SplendidDataTest: \echo 'number of rows:' :ROW_COUNT
-- Deactivated for SplendidDataTest: \echo 'last error message:' :LAST_ERROR_MESSAGE
-- Deactivated for SplendidDataTest: \echo 'last error code:' :LAST_ERROR_SQLSTATE

-- nondefault verbosity error settings (except verbose, which is too unstable)
-- Deactivated for SplendidDataTest: \set VERBOSITY terse
-- Deactivated for SplendidDataTest: SELECT 1 UNION;
-- Deactivated for SplendidDataTest: \echo 'error:' :ERROR
-- Deactivated for SplendidDataTest: \echo 'error code:' :SQLSTATE
-- Deactivated for SplendidDataTest: \echo 'last error message:' :LAST_ERROR_MESSAGE

-- Deactivated for SplendidDataTest: \set VERBOSITY sqlstate
SELECT 1/0;
-- Deactivated for SplendidDataTest: \echo 'error:' :ERROR
-- Deactivated for SplendidDataTest: \echo 'error code:' :SQLSTATE
-- Deactivated for SplendidDataTest: \echo 'last error message:' :LAST_ERROR_MESSAGE

-- Deactivated for SplendidDataTest: \set VERBOSITY default

-- working \gdesc
-- Deactivated for SplendidDataTest: SELECT 3 AS three, 4 AS four \gdesc
-- Deactivated for SplendidDataTest: \echo 'error:' :ERROR
-- Deactivated for SplendidDataTest: \echo 'error code:' :SQLSTATE
-- Deactivated for SplendidDataTest: \echo 'number of rows:' :ROW_COUNT

-- \gdesc with an error
-- Deactivated for SplendidDataTest: SELECT 4 AS \gdesc
-- Deactivated for SplendidDataTest: \echo 'error:' :ERROR
-- Deactivated for SplendidDataTest: \echo 'error code:' :SQLSTATE
-- Deactivated for SplendidDataTest: \echo 'number of rows:' :ROW_COUNT
-- Deactivated for SplendidDataTest: \echo 'last error message:' :LAST_ERROR_MESSAGE
-- Deactivated for SplendidDataTest: \echo 'last error code:' :LAST_ERROR_SQLSTATE

-- check row count for a cursor-fetched query
-- Deactivated for SplendidDataTest: \set FETCH_COUNT 10
select unique2 from tenk1 order by unique2 limit 19;
-- Deactivated for SplendidDataTest: \echo 'error:' :ERROR
-- Deactivated for SplendidDataTest: \echo 'error code:' :SQLSTATE
-- Deactivated for SplendidDataTest: \echo 'number of rows:' :ROW_COUNT

-- cursor-fetched query with an error after the first group
select 1/(15-unique2) from tenk1 order by unique2 limit 19;
-- Deactivated for SplendidDataTest: \echo 'error:' :ERROR
-- Deactivated for SplendidDataTest: \echo 'error code:' :SQLSTATE
-- Deactivated for SplendidDataTest: \echo 'number of rows:' :ROW_COUNT
-- Deactivated for SplendidDataTest: \echo 'last error message:' :LAST_ERROR_MESSAGE
-- Deactivated for SplendidDataTest: \echo 'last error code:' :LAST_ERROR_SQLSTATE

-- Deactivated for SplendidDataTest: \unset FETCH_COUNT

create schema testpart;
create role regress_partitioning_role;

alter schema testpart owner to regress_partitioning_role;

set role to regress_partitioning_role;

-- run test inside own schema and hide other partitions
set search_path to testpart;

create table testtable_apple(logdate date);
create table testtable_orange(logdate date);
create index testtable_apple_index on testtable_apple(logdate);
create index testtable_orange_index on testtable_orange(logdate);

create table testpart_apple(logdate date) partition by range(logdate);
create table testpart_orange(logdate date) partition by range(logdate);

create index testpart_apple_index on testpart_apple(logdate);
create index testpart_orange_index on testpart_orange(logdate);

-- only partition related object should be displayed
-- Deactivated for SplendidDataTest: \dP test*apple*
-- Deactivated for SplendidDataTest: \dPt test*apple*
-- Deactivated for SplendidDataTest: \dPi test*apple*

drop table testtable_apple;
drop table testtable_orange;
drop table testpart_apple;
drop table testpart_orange;

create table parent_tab (id int) partition by range (id);
create index parent_index on parent_tab (id);
create table child_0_10 partition of parent_tab
  for values from (0) to (10);
create table child_10_20 partition of parent_tab
  for values from (10) to (20);
create table child_20_30 partition of parent_tab
  for values from (20) to (30);
insert into parent_tab values (generate_series(0,29));
create table child_30_40 partition of parent_tab
for values from (30) to (40)
  partition by range(id);
create table child_30_35 partition of child_30_40
  for values from (30) to (35);
create table child_35_40 partition of child_30_40
   for values from (35) to (40);
insert into parent_tab values (generate_series(30,39));

-- Deactivated for SplendidDataTest: \dPt
-- Deactivated for SplendidDataTest: \dPi

-- Deactivated for SplendidDataTest: \dP testpart.*
-- Deactivated for SplendidDataTest: \dP

-- Deactivated for SplendidDataTest: \dPtn
-- Deactivated for SplendidDataTest: \dPin
-- Deactivated for SplendidDataTest: \dPn
-- Deactivated for SplendidDataTest: \dPn testpart.*

drop table parent_tab cascade;

drop schema testpart;

set search_path to default;

set role to default;
drop role regress_partitioning_role;

-- \d on toast table (use pg_statistic's toast table, which has a known name)
-- Deactivated for SplendidDataTest: \d pg_toast.pg_toast_2619

-- check printing info about access methods
-- Deactivated for SplendidDataTest: \dA
-- Deactivated for SplendidDataTest: \dA *
-- Deactivated for SplendidDataTest: \dA h*
-- Deactivated for SplendidDataTest: \dA foo
-- Deactivated for SplendidDataTest: \dA foo bar
-- Deactivated for SplendidDataTest: \dA+
-- Deactivated for SplendidDataTest: \dA+ *
-- Deactivated for SplendidDataTest: \dA+ h*
-- Deactivated for SplendidDataTest: \dA+ foo
-- Deactivated for SplendidDataTest: \dAc brin pg*.oid*
-- Deactivated for SplendidDataTest: \dAf spgist
-- Deactivated for SplendidDataTest: \dAf btree int4
-- Deactivated for SplendidDataTest: \dAo+ btree float_ops
-- Deactivated for SplendidDataTest: \dAo * pg_catalog.jsonb_path_ops
-- Deactivated for SplendidDataTest: \dAp+ btree float_ops
-- Deactivated for SplendidDataTest: \dAp * pg_catalog.uuid_ops

-- check \dconfig
set work_mem = 10240;
-- Deactivated for SplendidDataTest: \dconfig work_mem
-- Deactivated for SplendidDataTest: \dconfig+ work*
reset work_mem;

-- check \df, \do with argument specifications
-- Deactivated for SplendidDataTest: \df *sqrt
-- Deactivated for SplendidDataTest: \df *sqrt num*
-- Deactivated for SplendidDataTest: \df int*pl
-- Deactivated for SplendidDataTest: \df int*pl int4
-- Deactivated for SplendidDataTest: \df int*pl * pg_catalog.int8
-- Deactivated for SplendidDataTest: \df acl* aclitem[]
-- Deactivated for SplendidDataTest: \df has_database_privilege oid text
-- Deactivated for SplendidDataTest: \df has_database_privilege oid text -
-- Deactivated for SplendidDataTest: \dfa bit* small*
-- Deactivated for SplendidDataTest: \do - pg_catalog.int4
-- Deactivated for SplendidDataTest: \do && anyarray *

-- AUTOCOMMIT

CREATE TABLE ac_test (a int);
-- Deactivated for SplendidDataTest: \set AUTOCOMMIT off

INSERT INTO ac_test VALUES (1);
COMMIT;
SELECT * FROM ac_test;
COMMIT;

INSERT INTO ac_test VALUES (2);
ROLLBACK;
SELECT * FROM ac_test;
COMMIT;

BEGIN;
INSERT INTO ac_test VALUES (3);
COMMIT;
SELECT * FROM ac_test;
COMMIT;

BEGIN;
INSERT INTO ac_test VALUES (4);
ROLLBACK;
SELECT * FROM ac_test;
COMMIT;

-- Deactivated for SplendidDataTest: \set AUTOCOMMIT on
DROP TABLE ac_test;
SELECT * FROM ac_test;  -- should be gone now

-- ON_ERROR_ROLLBACK

-- Deactivated for SplendidDataTest: \set ON_ERROR_ROLLBACK on
CREATE TABLE oer_test (a int);

BEGIN;
INSERT INTO oer_test VALUES (1);
INSERT INTO oer_test VALUES ('foo');
INSERT INTO oer_test VALUES (3);
COMMIT;
SELECT * FROM oer_test;

BEGIN;
INSERT INTO oer_test VALUES (4);
ROLLBACK;
SELECT * FROM oer_test;

BEGIN;
INSERT INTO oer_test VALUES (5);
COMMIT AND CHAIN;
INSERT INTO oer_test VALUES (6);
COMMIT;
SELECT * FROM oer_test;

DROP TABLE oer_test;
-- Deactivated for SplendidDataTest: \set ON_ERROR_ROLLBACK off

-- ECHO errors
-- Deactivated for SplendidDataTest: \set ECHO errors
SELECT * FROM notexists;
-- Deactivated for SplendidDataTest: \set ECHO all

--
-- combined queries
--
CREATE FUNCTION warn(msg TEXT) RETURNS BOOLEAN LANGUAGE plpgsql
AS $$
  BEGIN RAISE NOTICE 'warn %', msg ; RETURN TRUE ; END
$$;

-- show both
-- Deactivated for SplendidDataTest: SELECT 1 AS one \; SELECT warn('1.5') \; SELECT 2 AS two ;
-- \gset applies to last query only
-- Deactivated for SplendidDataTest: SELECT 3 AS three \; SELECT warn('3.5') \; SELECT 4 AS four \gset
-- Deactivated for SplendidDataTest: \echo :three :four
-- syntax error stops all processing
-- Deactivated for SplendidDataTest: SELECT 5 \; SELECT 6 + \; SELECT warn('6.5') \; SELECT 7 ;
-- with aborted transaction, stop on first error
-- Deactivated for SplendidDataTest: BEGIN \; SELECT 8 AS eight \; SELECT 9/0 AS nine \; ROLLBACK \; SELECT 10 AS ten ;
-- close previously aborted transaction
ROLLBACK;

-- miscellaneous SQL commands
-- (non SELECT output is sent to stderr, thus is not shown in expected results)
-- Deactivated for SplendidDataTest: SELECT 'ok' AS "begin" \;
-- Deactivated for SplendidDataTest: CREATE TABLE psql_comics(s TEXT) \;
-- Deactivated for SplendidDataTest: INSERT INTO psql_comics VALUES ('Calvin'), ('hobbes') \;
-- Deactivated for SplendidDataTest: COPY psql_comics FROM STDIN \;
-- Deactivated for SplendidDataTest: UPDATE psql_comics SET s = 'Hobbes' WHERE s = 'hobbes' \;
-- Deactivated for SplendidDataTest: DELETE FROM psql_comics WHERE s = 'Moe' \;
-- Deactivated for SplendidDataTest: COPY psql_comics TO STDOUT \;
-- Deactivated for SplendidDataTest: TRUNCATE psql_comics \;
-- Deactivated for SplendidDataTest: DROP TABLE psql_comics \;
SELECT 'ok' AS "done" ;
-- Deactivated for SplendidDataTest: Moe
-- Deactivated for SplendidDataTest: Susie
-- Deactivated for SplendidDataTest: \.

-- Deactivated for SplendidDataTest: \set SHOW_ALL_RESULTS off
-- Deactivated for SplendidDataTest: SELECT 1 AS one \; SELECT warn('1.5') \; SELECT 2 AS two ;

-- Deactivated for SplendidDataTest: \set SHOW_ALL_RESULTS on
DROP FUNCTION warn(TEXT);

--
-- AUTOCOMMIT and combined queries
--
-- Deactivated for SplendidDataTest: \set AUTOCOMMIT off
-- Deactivated for SplendidDataTest: \echo '# AUTOCOMMIT:' :AUTOCOMMIT
-- BEGIN is now implicit

-- Deactivated for SplendidDataTest: CREATE TABLE foo(s TEXT) \;
ROLLBACK;

-- Deactivated for SplendidDataTest: CREATE TABLE foo(s TEXT) \;
-- Deactivated for SplendidDataTest: INSERT INTO foo(s) VALUES ('hello'), ('world') \;
COMMIT;

-- Deactivated for SplendidDataTest: DROP TABLE foo \;
ROLLBACK;

-- table foo is still there
-- Deactivated for SplendidDataTest: SELECT * FROM foo ORDER BY 1 \;
-- Deactivated for SplendidDataTest: DROP TABLE foo \;
COMMIT;

-- Deactivated for SplendidDataTest: \set AUTOCOMMIT on
-- Deactivated for SplendidDataTest: \echo '# AUTOCOMMIT:' :AUTOCOMMIT
-- BEGIN now explicit for multi-statement transactions

-- Deactivated for SplendidDataTest: BEGIN \;
-- Deactivated for SplendidDataTest: CREATE TABLE foo(s TEXT) \;
-- Deactivated for SplendidDataTest: INSERT INTO foo(s) VALUES ('hello'), ('world') \;
COMMIT;

-- Deactivated for SplendidDataTest: BEGIN \;
-- Deactivated for SplendidDataTest: DROP TABLE foo \;
-- Deactivated for SplendidDataTest: ROLLBACK \;

-- implicit transactions
-- Deactivated for SplendidDataTest: SELECT * FROM foo ORDER BY 1 \;
DROP TABLE foo;

--
-- test ON_ERROR_ROLLBACK and combined queries
--
CREATE FUNCTION psql_error(msg TEXT) RETURNS BOOLEAN AS $$
  BEGIN
    RAISE EXCEPTION 'error %', msg;
  END;
$$ LANGUAGE plpgsql;

-- Deactivated for SplendidDataTest: \set ON_ERROR_ROLLBACK on
-- Deactivated for SplendidDataTest: \echo '# ON_ERROR_ROLLBACK:' :ON_ERROR_ROLLBACK
-- Deactivated for SplendidDataTest: \echo '# AUTOCOMMIT:' :AUTOCOMMIT

BEGIN;
CREATE TABLE bla(s NO_SUCH_TYPE);               -- fails
CREATE TABLE bla(s TEXT);                       -- succeeds
SELECT psql_error('oops!');                     -- fails
INSERT INTO bla VALUES ('Calvin'), ('Hobbes');
COMMIT;

SELECT * FROM bla ORDER BY 1;

BEGIN;
INSERT INTO bla VALUES ('Susie');         -- succeeds
-- now with combined queries
-- Deactivated for SplendidDataTest: INSERT INTO bla VALUES ('Rosalyn') \;     -- will rollback
-- Deactivated for SplendidDataTest: SELECT 'before error' AS show \;          -- will show nevertheless!
-- Deactivated for SplendidDataTest:   SELECT psql_error('boum!') \;           -- failure
  SELECT 'after error' AS noshow;         -- hidden by preceding error
-- Deactivated for SplendidDataTest: INSERT INTO bla(s) VALUES ('Moe') \;      -- will rollback
  SELECT psql_error('bam!');
INSERT INTO bla VALUES ('Miss Wormwood'); -- succeeds
COMMIT;
SELECT * FROM bla ORDER BY 1;

-- some with autocommit off
-- Deactivated for SplendidDataTest: \set AUTOCOMMIT off
-- Deactivated for SplendidDataTest: \echo '# AUTOCOMMIT:' :AUTOCOMMIT

-- implicit BEGIN
INSERT INTO bla VALUES ('Dad');           -- succeeds
SELECT psql_error('bad!');                -- implicit partial rollback

-- Deactivated for SplendidDataTest: INSERT INTO bla VALUES ('Mum') \;         -- will rollback
-- Deactivated for SplendidDataTest: SELECT COUNT(*) AS "#mum"
-- Deactivated for SplendidDataTest: FROM bla WHERE s = 'Mum' \;               -- but be counted here
-- Deactivated for SplendidDataTest: SELECT psql_error('bad!');                -- implicit partial rollback
COMMIT;

-- Deactivated for SplendidDataTest: SELECT COUNT(*) AS "#mum"
-- Deactivated for SplendidDataTest: FROM bla WHERE s = 'Mum' \;               -- no mum here
SELECT * FROM bla ORDER BY 1;

-- reset all
-- Deactivated for SplendidDataTest: \set AUTOCOMMIT on
-- Deactivated for SplendidDataTest: \set ON_ERROR_ROLLBACK off
-- Deactivated for SplendidDataTest: \echo '# final ON_ERROR_ROLLBACK:' :ON_ERROR_ROLLBACK
DROP TABLE bla;
DROP FUNCTION psql_error;

-- check describing invalid multipart names
-- Deactivated for SplendidDataTest: \dA regression.heap
-- Deactivated for SplendidDataTest: \dA nonesuch.heap
-- Deactivated for SplendidDataTest: \dt host.regression.pg_catalog.pg_class
-- Deactivated for SplendidDataTest: \dt |.pg_catalog.pg_class
-- Deactivated for SplendidDataTest: \dt nonesuch.pg_catalog.pg_class
-- Deactivated for SplendidDataTest: \da host.regression.pg_catalog.sum
-- Deactivated for SplendidDataTest: \da +.pg_catalog.sum
-- Deactivated for SplendidDataTest: \da nonesuch.pg_catalog.sum
-- Deactivated for SplendidDataTest: \dAc nonesuch.brin
-- Deactivated for SplendidDataTest: \dAc regression.brin
-- Deactivated for SplendidDataTest: \dAf nonesuch.brin
-- Deactivated for SplendidDataTest: \dAf regression.brin
-- Deactivated for SplendidDataTest: \dAo nonesuch.brin
-- Deactivated for SplendidDataTest: \dAo regression.brin
-- Deactivated for SplendidDataTest: \dAp nonesuch.brin
-- Deactivated for SplendidDataTest: \dAp regression.brin
-- Deactivated for SplendidDataTest: \db nonesuch.pg_default
-- Deactivated for SplendidDataTest: \db regression.pg_default
-- Deactivated for SplendidDataTest: \dc host.regression.public.conversion
-- Deactivated for SplendidDataTest: \dc (.public.conversion
-- Deactivated for SplendidDataTest: \dc nonesuch.public.conversion
-- Deactivated for SplendidDataTest: \dC host.regression.pg_catalog.int8
-- Deactivated for SplendidDataTest: \dC ).pg_catalog.int8
-- Deactivated for SplendidDataTest: \dC nonesuch.pg_catalog.int8
-- Deactivated for SplendidDataTest: \dd host.regression.pg_catalog.pg_class
-- Deactivated for SplendidDataTest: \dd [.pg_catalog.pg_class
-- Deactivated for SplendidDataTest: \dd nonesuch.pg_catalog.pg_class
-- Deactivated for SplendidDataTest: \dD host.regression.public.gtestdomain1
-- Deactivated for SplendidDataTest: \dD ].public.gtestdomain1
-- Deactivated for SplendidDataTest: \dD nonesuch.public.gtestdomain1
-- Deactivated for SplendidDataTest: \ddp host.regression.pg_catalog.pg_class
-- Deactivated for SplendidDataTest: \ddp {.pg_catalog.pg_class
-- Deactivated for SplendidDataTest: \ddp nonesuch.pg_catalog.pg_class
-- Deactivated for SplendidDataTest: \dE host.regression.public.ft
-- Deactivated for SplendidDataTest: \dE }.public.ft
-- Deactivated for SplendidDataTest: \dE nonesuch.public.ft
-- Deactivated for SplendidDataTest: \di host.regression.public.tenk1_hundred
-- Deactivated for SplendidDataTest: \di ..public.tenk1_hundred
-- Deactivated for SplendidDataTest: \di nonesuch.public.tenk1_hundred
-- Deactivated for SplendidDataTest: \dm host.regression.public.mvtest_bb
-- Deactivated for SplendidDataTest: \dm ^.public.mvtest_bb
-- Deactivated for SplendidDataTest: \dm nonesuch.public.mvtest_bb
-- Deactivated for SplendidDataTest: \ds host.regression.public.check_seq
-- Deactivated for SplendidDataTest: \ds regression|mydb.public.check_seq
-- Deactivated for SplendidDataTest: \ds nonesuch.public.check_seq
-- Deactivated for SplendidDataTest: \dt host.regression.public.b_star
-- Deactivated for SplendidDataTest: \dt regres+ion.public.b_star
-- Deactivated for SplendidDataTest: \dt nonesuch.public.b_star
-- Deactivated for SplendidDataTest: \dv host.regression.public.shoe
-- Deactivated for SplendidDataTest: \dv regress(ion).public.shoe
-- Deactivated for SplendidDataTest: \dv nonesuch.public.shoe
-- Deactivated for SplendidDataTest: \des nonesuch.server
-- Deactivated for SplendidDataTest: \des regression.server
-- Deactivated for SplendidDataTest: \des nonesuch.server
-- Deactivated for SplendidDataTest: \des regression.server
-- Deactivated for SplendidDataTest: \des nonesuch.username
-- Deactivated for SplendidDataTest: \des regression.username
-- Deactivated for SplendidDataTest: \dew nonesuch.fdw
-- Deactivated for SplendidDataTest: \dew regression.fdw
-- Deactivated for SplendidDataTest: \df host.regression.public.namelen
-- Deactivated for SplendidDataTest: \df regres[qrstuv]ion.public.namelen
-- Deactivated for SplendidDataTest: \df nonesuch.public.namelen
-- Deactivated for SplendidDataTest: \dF host.regression.pg_catalog.arabic
-- Deactivated for SplendidDataTest: \dF regres{1,2}ion.pg_catalog.arabic
-- Deactivated for SplendidDataTest: \dF nonesuch.pg_catalog.arabic
-- Deactivated for SplendidDataTest: \dFd host.regression.pg_catalog.arabic_stem
-- Deactivated for SplendidDataTest: \dFd regres?ion.pg_catalog.arabic_stem
-- Deactivated for SplendidDataTest: \dFd nonesuch.pg_catalog.arabic_stem
-- Deactivated for SplendidDataTest: \dFp host.regression.pg_catalog.default
-- Deactivated for SplendidDataTest: \dFp ^regression.pg_catalog.default
-- Deactivated for SplendidDataTest: \dFp nonesuch.pg_catalog.default
-- Deactivated for SplendidDataTest: \dFt host.regression.pg_catalog.ispell
-- Deactivated for SplendidDataTest: \dFt regression$.pg_catalog.ispell
-- Deactivated for SplendidDataTest: \dFt nonesuch.pg_catalog.ispell
-- Deactivated for SplendidDataTest: \dg nonesuch.pg_database_owner
-- Deactivated for SplendidDataTest: \dg regression.pg_database_owner
-- Deactivated for SplendidDataTest: \dL host.regression.plpgsql
-- Deactivated for SplendidDataTest: \dL *.plpgsql
-- Deactivated for SplendidDataTest: \dL nonesuch.plpgsql
-- Deactivated for SplendidDataTest: \dn host.regression.public
-- Deactivated for SplendidDataTest: \dn """".public
-- Deactivated for SplendidDataTest: \dn nonesuch.public
-- Deactivated for SplendidDataTest: \do host.regression.public.!=-
-- Deactivated for SplendidDataTest: \do "regression|mydb".public.!=-
-- Deactivated for SplendidDataTest: \do nonesuch.public.!=-
-- Deactivated for SplendidDataTest: \dO host.regression.pg_catalog.POSIX
-- Deactivated for SplendidDataTest: \dO .pg_catalog.POSIX
-- Deactivated for SplendidDataTest: \dO nonesuch.pg_catalog.POSIX
-- Deactivated for SplendidDataTest: \dp host.regression.public.a_star
-- Deactivated for SplendidDataTest: \dp "regres+ion".public.a_star
-- Deactivated for SplendidDataTest: \dp nonesuch.public.a_star
-- Deactivated for SplendidDataTest: \dP host.regression.public.mlparted
-- Deactivated for SplendidDataTest: \dP "regres(sion)".public.mlparted
-- Deactivated for SplendidDataTest: \dP nonesuch.public.mlparted
-- Deactivated for SplendidDataTest: \drds nonesuch.lc_messages
-- Deactivated for SplendidDataTest: \drds regression.lc_messages
-- Deactivated for SplendidDataTest: \dRp public.mypub
-- Deactivated for SplendidDataTest: \dRp regression.mypub
-- Deactivated for SplendidDataTest: \dRs public.mysub
-- Deactivated for SplendidDataTest: \dRs regression.mysub
-- Deactivated for SplendidDataTest: \dT host.regression.public.widget
-- Deactivated for SplendidDataTest: \dT "regression{1,2}".public.widget
-- Deactivated for SplendidDataTest: \dT nonesuch.public.widget
-- Deactivated for SplendidDataTest: \dx regression.plpgsql
-- Deactivated for SplendidDataTest: \dx nonesuch.plpgsql
-- Deactivated for SplendidDataTest: \dX host.regression.public.func_deps_stat
-- Deactivated for SplendidDataTest: \dX "^regression$".public.func_deps_stat
-- Deactivated for SplendidDataTest: \dX nonesuch.public.func_deps_stat
-- Deactivated for SplendidDataTest: \dy regression.myevt
-- Deactivated for SplendidDataTest: \dy nonesuch.myevt

-- check that dots within quoted name segments are not counted
-- Deactivated for SplendidDataTest: \dA "no.such.access.method"
-- Deactivated for SplendidDataTest: \dt "no.such.table.relation"
-- Deactivated for SplendidDataTest: \da "no.such.aggregate.function"
-- Deactivated for SplendidDataTest: \dAc "no.such.operator.class"
-- Deactivated for SplendidDataTest: \dAf "no.such.operator.family"
-- Deactivated for SplendidDataTest: \dAo "no.such.operator.of.operator.family"
-- Deactivated for SplendidDataTest: \dAp "no.such.operator.support.function.of.operator.family"
-- Deactivated for SplendidDataTest: \db "no.such.tablespace"
-- Deactivated for SplendidDataTest: \dc "no.such.conversion"
-- Deactivated for SplendidDataTest: \dC "no.such.cast"
-- Deactivated for SplendidDataTest: \dd "no.such.object.description"
-- Deactivated for SplendidDataTest: \dD "no.such.domain"
-- Deactivated for SplendidDataTest: \ddp "no.such.default.access.privilege"
-- Deactivated for SplendidDataTest: \di "no.such.index.relation"
-- Deactivated for SplendidDataTest: \dm "no.such.materialized.view"
-- Deactivated for SplendidDataTest: \ds "no.such.relation"
-- Deactivated for SplendidDataTest: \dt "no.such.relation"
-- Deactivated for SplendidDataTest: \dv "no.such.relation"
-- Deactivated for SplendidDataTest: \des "no.such.foreign.server"
-- Deactivated for SplendidDataTest: \dew "no.such.foreign.data.wrapper"
-- Deactivated for SplendidDataTest: \df "no.such.function"
-- Deactivated for SplendidDataTest: \dF "no.such.text.search.configuration"
-- Deactivated for SplendidDataTest: \dFd "no.such.text.search.dictionary"
-- Deactivated for SplendidDataTest: \dFp "no.such.text.search.parser"
-- Deactivated for SplendidDataTest: \dFt "no.such.text.search.template"
-- Deactivated for SplendidDataTest: \dg "no.such.role"
-- Deactivated for SplendidDataTest: \dL "no.such.language"
-- Deactivated for SplendidDataTest: \dn "no.such.schema"
-- Deactivated for SplendidDataTest: \do "no.such.operator"
-- Deactivated for SplendidDataTest: \dO "no.such.collation"
-- Deactivated for SplendidDataTest: \dp "no.such.access.privilege"
-- Deactivated for SplendidDataTest: \dP "no.such.partitioned.relation"
-- Deactivated for SplendidDataTest: \drds "no.such.setting"
-- Deactivated for SplendidDataTest: \dRp "no.such.publication"
-- Deactivated for SplendidDataTest: \dRs "no.such.subscription"
-- Deactivated for SplendidDataTest: \dT "no.such.data.type"
-- Deactivated for SplendidDataTest: \dx "no.such.installed.extension"
-- Deactivated for SplendidDataTest: \dX "no.such.extended.statistics"
-- Deactivated for SplendidDataTest: \dy "no.such.event.trigger"

-- again, but with dotted schema qualifications.
-- Deactivated for SplendidDataTest: \dA "no.such.schema"."no.such.access.method"
-- Deactivated for SplendidDataTest: \dt "no.such.schema"."no.such.table.relation"
-- Deactivated for SplendidDataTest: \da "no.such.schema"."no.such.aggregate.function"
-- Deactivated for SplendidDataTest: \dAc "no.such.schema"."no.such.operator.class"
-- Deactivated for SplendidDataTest: \dAf "no.such.schema"."no.such.operator.family"
-- Deactivated for SplendidDataTest: \dAo "no.such.schema"."no.such.operator.of.operator.family"
-- Deactivated for SplendidDataTest: \dAp "no.such.schema"."no.such.operator.support.function.of.operator.family"
-- Deactivated for SplendidDataTest: \db "no.such.schema"."no.such.tablespace"
-- Deactivated for SplendidDataTest: \dc "no.such.schema"."no.such.conversion"
-- Deactivated for SplendidDataTest: \dC "no.such.schema"."no.such.cast"
-- Deactivated for SplendidDataTest: \dd "no.such.schema"."no.such.object.description"
-- Deactivated for SplendidDataTest: \dD "no.such.schema"."no.such.domain"
-- Deactivated for SplendidDataTest: \ddp "no.such.schema"."no.such.default.access.privilege"
-- Deactivated for SplendidDataTest: \di "no.such.schema"."no.such.index.relation"
-- Deactivated for SplendidDataTest: \dm "no.such.schema"."no.such.materialized.view"
-- Deactivated for SplendidDataTest: \ds "no.such.schema"."no.such.relation"
-- Deactivated for SplendidDataTest: \dt "no.such.schema"."no.such.relation"
-- Deactivated for SplendidDataTest: \dv "no.such.schema"."no.such.relation"
-- Deactivated for SplendidDataTest: \des "no.such.schema"."no.such.foreign.server"
-- Deactivated for SplendidDataTest: \dew "no.such.schema"."no.such.foreign.data.wrapper"
-- Deactivated for SplendidDataTest: \df "no.such.schema"."no.such.function"
-- Deactivated for SplendidDataTest: \dF "no.such.schema"."no.such.text.search.configuration"
-- Deactivated for SplendidDataTest: \dFd "no.such.schema"."no.such.text.search.dictionary"
-- Deactivated for SplendidDataTest: \dFp "no.such.schema"."no.such.text.search.parser"
-- Deactivated for SplendidDataTest: \dFt "no.such.schema"."no.such.text.search.template"
-- Deactivated for SplendidDataTest: \dg "no.such.schema"."no.such.role"
-- Deactivated for SplendidDataTest: \dL "no.such.schema"."no.such.language"
-- Deactivated for SplendidDataTest: \do "no.such.schema"."no.such.operator"
-- Deactivated for SplendidDataTest: \dO "no.such.schema"."no.such.collation"
-- Deactivated for SplendidDataTest: \dp "no.such.schema"."no.such.access.privilege"
-- Deactivated for SplendidDataTest: \dP "no.such.schema"."no.such.partitioned.relation"
-- Deactivated for SplendidDataTest: \drds "no.such.schema"."no.such.setting"
-- Deactivated for SplendidDataTest: \dRp "no.such.schema"."no.such.publication"
-- Deactivated for SplendidDataTest: \dRs "no.such.schema"."no.such.subscription"
-- Deactivated for SplendidDataTest: \dT "no.such.schema"."no.such.data.type"
-- Deactivated for SplendidDataTest: \dx "no.such.schema"."no.such.installed.extension"
-- Deactivated for SplendidDataTest: \dX "no.such.schema"."no.such.extended.statistics"
-- Deactivated for SplendidDataTest: \dy "no.such.schema"."no.such.event.trigger"

-- again, but with current database and dotted schema qualifications.
-- Deactivated for SplendidDataTest: \dt regression."no.such.schema"."no.such.table.relation"
-- Deactivated for SplendidDataTest: \da regression."no.such.schema"."no.such.aggregate.function"
-- Deactivated for SplendidDataTest: \dc regression."no.such.schema"."no.such.conversion"
-- Deactivated for SplendidDataTest: \dC regression."no.such.schema"."no.such.cast"
-- Deactivated for SplendidDataTest: \dd regression."no.such.schema"."no.such.object.description"
-- Deactivated for SplendidDataTest: \dD regression."no.such.schema"."no.such.domain"
-- Deactivated for SplendidDataTest: \di regression."no.such.schema"."no.such.index.relation"
-- Deactivated for SplendidDataTest: \dm regression."no.such.schema"."no.such.materialized.view"
-- Deactivated for SplendidDataTest: \ds regression."no.such.schema"."no.such.relation"
-- Deactivated for SplendidDataTest: \dt regression."no.such.schema"."no.such.relation"
-- Deactivated for SplendidDataTest: \dv regression."no.such.schema"."no.such.relation"
-- Deactivated for SplendidDataTest: \df regression."no.such.schema"."no.such.function"
-- Deactivated for SplendidDataTest: \dF regression."no.such.schema"."no.such.text.search.configuration"
-- Deactivated for SplendidDataTest: \dFd regression."no.such.schema"."no.such.text.search.dictionary"
-- Deactivated for SplendidDataTest: \dFp regression."no.such.schema"."no.such.text.search.parser"
-- Deactivated for SplendidDataTest: \dFt regression."no.such.schema"."no.such.text.search.template"
-- Deactivated for SplendidDataTest: \do regression."no.such.schema"."no.such.operator"
-- Deactivated for SplendidDataTest: \dO regression."no.such.schema"."no.such.collation"
-- Deactivated for SplendidDataTest: \dp regression."no.such.schema"."no.such.access.privilege"
-- Deactivated for SplendidDataTest: \dP regression."no.such.schema"."no.such.partitioned.relation"
-- Deactivated for SplendidDataTest: \dT regression."no.such.schema"."no.such.data.type"
-- Deactivated for SplendidDataTest: \dX regression."no.such.schema"."no.such.extended.statistics"

-- again, but with dotted database and dotted schema qualifications.
-- Deactivated for SplendidDataTest: \dt "no.such.database"."no.such.schema"."no.such.table.relation"
-- Deactivated for SplendidDataTest: \da "no.such.database"."no.such.schema"."no.such.aggregate.function"
-- Deactivated for SplendidDataTest: \dc "no.such.database"."no.such.schema"."no.such.conversion"
-- Deactivated for SplendidDataTest: \dC "no.such.database"."no.such.schema"."no.such.cast"
-- Deactivated for SplendidDataTest: \dd "no.such.database"."no.such.schema"."no.such.object.description"
-- Deactivated for SplendidDataTest: \dD "no.such.database"."no.such.schema"."no.such.domain"
-- Deactivated for SplendidDataTest: \ddp "no.such.database"."no.such.schema"."no.such.default.access.privilege"
-- Deactivated for SplendidDataTest: \di "no.such.database"."no.such.schema"."no.such.index.relation"
-- Deactivated for SplendidDataTest: \dm "no.such.database"."no.such.schema"."no.such.materialized.view"
-- Deactivated for SplendidDataTest: \ds "no.such.database"."no.such.schema"."no.such.relation"
-- Deactivated for SplendidDataTest: \dt "no.such.database"."no.such.schema"."no.such.relation"
-- Deactivated for SplendidDataTest: \dv "no.such.database"."no.such.schema"."no.such.relation"
-- Deactivated for SplendidDataTest: \df "no.such.database"."no.such.schema"."no.such.function"
-- Deactivated for SplendidDataTest: \dF "no.such.database"."no.such.schema"."no.such.text.search.configuration"
-- Deactivated for SplendidDataTest: \dFd "no.such.database"."no.such.schema"."no.such.text.search.dictionary"
-- Deactivated for SplendidDataTest: \dFp "no.such.database"."no.such.schema"."no.such.text.search.parser"
-- Deactivated for SplendidDataTest: \dFt "no.such.database"."no.such.schema"."no.such.text.search.template"
-- Deactivated for SplendidDataTest: \do "no.such.database"."no.such.schema"."no.such.operator"
-- Deactivated for SplendidDataTest: \dO "no.such.database"."no.such.schema"."no.such.collation"
-- Deactivated for SplendidDataTest: \dp "no.such.database"."no.such.schema"."no.such.access.privilege"
-- Deactivated for SplendidDataTest: \dP "no.such.database"."no.such.schema"."no.such.partitioned.relation"
-- Deactivated for SplendidDataTest: \dT "no.such.database"."no.such.schema"."no.such.data.type"
-- Deactivated for SplendidDataTest: \dX "no.such.database"."no.such.schema"."no.such.extended.statistics"
