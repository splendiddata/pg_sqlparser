/*
 * This file has been altered by SplendidData.
 * It is only used for syntax checking, not for the testing of a commandline paser.
 * So some statements that are expected to fail are removed.
 * The deactivated lines are marked by: -- Deactivated for SplendidDataTest: 
 */


--
-- COPY
--

-- directory paths are passed to us in environment variables
-- Deactivated for SplendidDataTest:\getenv abs_srcdir PG_ABS_SRCDIR
-- Deactivated for SplendidDataTest:\getenv abs_builddir PG_ABS_BUILDDIR

--- test copying in CSV mode with various styles
--- of embedded line ending characters

create temp table copytest (
	style	text,
	test 	text,
	filler	int);

insert into copytest values('DOS',E'abc\r\ndef',1);
insert into copytest values('Unix',E'abc\ndef',2);
insert into copytest values('Mac',E'abc\rdef',3);
insert into copytest values(E'esc\\ape',E'a\\r\\\r\\\n\\nb',4);

-- Deactivated for SplendidDataTest:\set filename :abs_builddir '/results/copytest.csv'
-- Deactivated for SplendidDataTest:copy copytest to :'filename' csv;

create temp table copytest2 (like copytest);

-- Deactivated for SplendidDataTest:copy copytest2 from :'filename' csv;

select * from copytest except select * from copytest2;

truncate copytest2;

--- same test but with an escape char different from quote char

-- Deactivated for SplendidDataTest:copy copytest to :'filename' csv quote '''' escape E'\\';

-- Deactivated for SplendidDataTest:copy copytest2 from :'filename' csv quote '''' escape E'\\';

select * from copytest except select * from copytest2;

--- test unquoted \. as data inside CSV
-- do not use copy out to export the data, as it would quote \.
\o :filename
\qecho line1
\qecho '\\.'
\qecho line2
\o
-- get the data back in with copy
truncate copytest2;
copy copytest2(test) from :'filename' csv;
select test from copytest2 order by test collate "C";

-- in text mode, \. must be alone on its line
truncate copytest2;
copy copytest2(test) from stdin;
-- Deactivated for SplendidDataTest: line1
-- Deactivated for SplendidDataTest: line2
-- Deactivated for SplendidDataTest: foo\.
-- Deactivated for SplendidDataTest: line3
-- Deactivated for SplendidDataTest: \.
copy copytest2(test) from stdin;
-- Deactivated for SplendidDataTest: line4
-- Deactivated for SplendidDataTest: line5
-- Deactivated for SplendidDataTest: \.foo
-- Deactivated for SplendidDataTest: line6
-- Deactivated for SplendidDataTest: \.
select test from copytest2;


-- test header line feature

create temp table copytest3 (
	c1 int,
	"col with , comma" text,
	"col with "" quote"  int);

copy copytest3 from stdin csv header;
-- Deactivated for SplendidDataTest: this is just a line full of junk that would error out if parsed
-- Deactivated for SplendidDataTest: 1,a,1
-- Deactivated for SplendidDataTest: 2,b,2
-- Deactivated for SplendidDataTest: \.

copy copytest3 to stdout csv header;

create temp table copytest4 (
	c1 int,
	"colname with tab: 	" text);

copy copytest4 from stdin (header);
-- Deactivated for SplendidDataTest: this is just a line full of junk that would error out if parsed
-- Deactivated for SplendidDataTest: 1	a
-- Deactivated for SplendidDataTest: 2	b
-- Deactivated for SplendidDataTest: \.

copy copytest4 to stdout (header);

-- test copy from with a partitioned table
create table parted_copytest (
	a int,
	b int,
	c text
) partition by list (b);

create table parted_copytest_a1 (c text, b int, a int);
create table parted_copytest_a2 (a int, c text, b int);

alter table parted_copytest attach partition parted_copytest_a1 for values in(1);
alter table parted_copytest attach partition parted_copytest_a2 for values in(2);

-- We must insert enough rows to trigger multi-inserts.  These are only
-- enabled adaptively when there are few enough partition changes.
insert into parted_copytest select x,1,'One' from generate_series(1,1000) x;
insert into parted_copytest select x,2,'Two' from generate_series(1001,1010) x;
insert into parted_copytest select x,1,'One' from generate_series(1011,1020) x;

-- Deactivated for SplendidDataTest: \set filename :abs_builddir '/results/parted_copytest.csv'
-- Deactivated for SplendidDataTest: copy (select * from parted_copytest order by a) to :'filename';

truncate parted_copytest;

-- Deactivated for SplendidDataTest: copy parted_copytest from :'filename';

-- Ensure COPY FREEZE errors for partitioned tables.
begin;
truncate parted_copytest;
-- Deactivated for SplendidDataTest: copy parted_copytest from :'filename' (freeze);
rollback;

select tableoid::regclass,count(*),sum(a) from parted_copytest
group by tableoid order by tableoid::regclass::name;

truncate parted_copytest;

-- create before insert row trigger on parted_copytest_a2
create function part_ins_func() returns trigger language plpgsql as $$
begin
  return new;
end;
$$;

create trigger part_ins_trig
	before insert on parted_copytest_a2
	for each row
	execute procedure part_ins_func();

-- Deactivated for SplendidDataTest: copy parted_copytest from :'filename';

select tableoid::regclass,count(*),sum(a) from parted_copytest
group by tableoid order by tableoid::regclass::name;

truncate table parted_copytest;
create index on parted_copytest (b);
drop trigger part_ins_trig on parted_copytest_a2;

copy parted_copytest from stdin;
-- Deactivated for SplendidDataTest: 1	1	str1
-- Deactivated for SplendidDataTest: 2	2	str2
-- Deactivated for SplendidDataTest: \.

-- Ensure index entries were properly added during the copy.
select * from parted_copytest where b = 1;
select * from parted_copytest where b = 2;

drop table parted_copytest;

--
-- Progress reporting for COPY
--
create table tab_progress_reporting (
	name text,
	age int4,
	location point,
	salary int4,
	manager name
);

-- Add a trigger to catch and print the contents of the catalog view
-- pg_stat_progress_copy during data insertion.  This allows to test
-- the validation of some progress reports for COPY FROM where the trigger
-- would fire.
create function notice_after_tab_progress_reporting() returns trigger AS
$$
declare report record;
begin
  -- The fields ignored here are the ones that may not remain
  -- consistent across multiple runs.  The sizes reported may differ
  -- across platforms, so just check if these are strictly positive.
  with progress_data as (
    select
       relid::regclass::text as relname,
       command,
       type,
       bytes_processed > 0 as has_bytes_processed,
       bytes_total > 0 as has_bytes_total,
       tuples_processed,
       tuples_excluded,
       tuples_skipped
      from pg_stat_progress_copy
      where pid = pg_backend_pid())
  select into report (to_jsonb(r)) as value
    from progress_data r;

  raise info 'progress: %', report.value::text;
  return new;
end;
$$ language plpgsql;

create trigger check_after_tab_progress_reporting
	after insert on tab_progress_reporting
	for each statement
	execute function notice_after_tab_progress_reporting();

-- Generate COPY FROM report with PIPE.
copy tab_progress_reporting from stdin;
-- Deactivated for SplendidDataTest: sharon	25	(15,12)	1000	sam
-- Deactivated for SplendidDataTest: sam	30	(10,5)	2000	bill
-- Deactivated for SplendidDataTest: bill	20	(11,10)	1000	sharon
-- Deactivated for SplendidDataTest: \.

-- Generate COPY FROM report with FILE, with some excluded tuples.
truncate tab_progress_reporting;
-- Deactivated for SplendidDataTest: \set filename :abs_srcdir '/data/emp.data'
-- Deactivated for SplendidDataTest: copy tab_progress_reporting from :'filename'
-- Deactivated for SplendidDataTest: 	where (salary < 2000);

-- Generate COPY FROM report with PIPE, with some skipped tuples.
copy tab_progress_reporting from stdin(on_error ignore);
-- Deactivated for SplendidDataTest: sharon	x	(15,12)	x	sam
-- Deactivated for SplendidDataTest: sharon	25	(15,12)	1000	sam
-- Deactivated for SplendidDataTest: sharon	y	(15,12)	x	sam
-- Deactivated for SplendidDataTest: \.

drop trigger check_after_tab_progress_reporting on tab_progress_reporting;
drop function notice_after_tab_progress_reporting();
drop table tab_progress_reporting;

-- Test header matching feature
create table header_copytest (
	a int,
	b int,
	c text
);
-- Make sure it works with dropped columns
alter table header_copytest drop column c;
alter table header_copytest add column c text;
copy header_copytest to stdout with (header match);
copy header_copytest from stdin with (header wrong_choice);
-- works
copy header_copytest from stdin with (header match);
-- Deactivated for SplendidDataTest: a	b	c
-- Deactivated for SplendidDataTest: 1	2	foo
-- Deactivated for SplendidDataTest: \.
copy header_copytest (c, a, b) from stdin with (header match);
-- Deactivated for SplendidDataTest: c	a	b
-- Deactivated for SplendidDataTest: bar	3	4
-- Deactivated for SplendidDataTest: \.
copy header_copytest from stdin with (header match, format csv);
-- Deactivated for SplendidDataTest: a,b,c
-- Deactivated for SplendidDataTest: 5,6,baz
-- Deactivated for SplendidDataTest: \.
-- errors
copy header_copytest (c, b, a) from stdin with (header match);
-- Deactivated for SplendidDataTest: a	b	c
-- Deactivated for SplendidDataTest: 1	2	foo
-- Deactivated for SplendidDataTest: \.
copy header_copytest from stdin with (header match);
-- Deactivated for SplendidDataTest: a	b	\N
-- Deactivated for SplendidDataTest: 1	2	foo
-- Deactivated for SplendidDataTest: \.
copy header_copytest from stdin with (header match);
-- Deactivated for SplendidDataTest: a	b
-- Deactivated for SplendidDataTest: 1	2
-- Deactivated for SplendidDataTest: \.
copy header_copytest from stdin with (header match);
-- Deactivated for SplendidDataTest: a	b	c	d
-- Deactivated for SplendidDataTest: 1	2	foo	bar
-- Deactivated for SplendidDataTest: \.
copy header_copytest from stdin with (header match);
-- Deactivated for SplendidDataTest: a	b	d
-- Deactivated for SplendidDataTest: 1	2	foo
-- Deactivated for SplendidDataTest: \.
SELECT * FROM header_copytest ORDER BY a;

-- Drop an extra column, in the middle of the existing set.
alter table header_copytest drop column b;
-- works
copy header_copytest (c, a) from stdin with (header match);
-- Deactivated for SplendidDataTest: c	a
-- Deactivated for SplendidDataTest: foo	7
-- Deactivated for SplendidDataTest: \.
copy header_copytest (a, c) from stdin with (header match);
-- Deactivated for SplendidDataTest: a	c
-- Deactivated for SplendidDataTest: 8	foo
-- Deactivated for SplendidDataTest: \.
-- errors
copy header_copytest from stdin with (header match);
-- Deactivated for SplendidDataTest: a	........pg.dropped.2........	c
-- Deactivated for SplendidDataTest: 1	2	foo
-- Deactivated for SplendidDataTest: \.
copy header_copytest (a, c) from stdin with (header match);
-- Deactivated for SplendidDataTest: a	c	b
-- Deactivated for SplendidDataTest: 1	foo	2
-- Deactivated for SplendidDataTest: \.

SELECT * FROM header_copytest ORDER BY a;
drop table header_copytest;

-- test COPY with overlong column defaults
create temp table oversized_column_default (
    col1 varchar(5) DEFAULT 'more than 5 chars',
    col2 varchar(5));
-- normal COPY should work
copy oversized_column_default from stdin;
-- Deactivated for SplendidDataTest: \.
-- error if the column is excluded
copy oversized_column_default (col2) from stdin;
-- Deactivated for SplendidDataTest: \.
-- error if the DEFAULT option is given
copy oversized_column_default from stdin (default '');
-- Deactivated for SplendidDataTest: \.
drop table oversized_column_default;


--
-- Create partitioned table that does not allow bulk insertions, to test bugs
-- related to the reuse of BulkInsertState across partitions (only done when
-- not using bulk insert).  Switching between partitions often makes it more
-- likely to encounter these bugs, so we just switch on roughly every insert
-- by having an even/odd number partition and inserting evenly distributed
-- data.
--
CREATE TABLE parted_si (
  id int not null,
  data text not null,
  -- prevent use of bulk insert by having a volatile function
  rand float8 not null default random()
)
PARTITION BY LIST((id % 2));

CREATE TABLE parted_si_p_even PARTITION OF parted_si FOR VALUES IN (0);
CREATE TABLE parted_si_p_odd PARTITION OF parted_si FOR VALUES IN (1);

-- Test that bulk relation extension handles reusing a single BulkInsertState
-- across partitions.  Without the fix applied, this reliably reproduces
-- #18130 unless shared_buffers is extremely small (preventing any use of bulk
-- relation extension). See
-- https://postgr.es/m/18130-7a86a7356a75209d%40postgresql.org
-- https://postgr.es/m/257696.1695670946%40sss.pgh.pa.us
-- Deactivated for SplendidDataTest: \set filename :abs_srcdir '/data/desc.data'
-- Deactivated for SplendidDataTest: COPY parted_si(id, data) FROM :'filename';

-- An earlier bug (see commit b1ecb9b3fcf) could end up using a buffer from
-- the wrong partition. This test is *not* guaranteed to trigger that bug, but
-- does so when shared_buffers is small enough.  To test if we encountered the
-- bug, check that the partition condition isn't violated.
SELECT tableoid::regclass, id % 2 = 0 is_even, count(*) from parted_si GROUP BY 1, 2 ORDER BY 1;

DROP TABLE parted_si;

-- ensure COPY FREEZE errors for foreign tables
begin;
create foreign data wrapper copytest_wrapper;
create server copytest_server foreign data wrapper copytest_wrapper;
create foreign table copytest_foreign_table (a int) server copytest_server;
copy copytest_foreign_table from stdin (freeze);
-- Deactivated for SplendidDataTest: 1
-- Deactivated for SplendidDataTest: \.
rollback;

-- Tests for COPY TO with materialized views.
-- COPY TO should fail for an unpopulated materialized view
-- but succeed for a populated one.
CREATE MATERIALIZED VIEW copytest_mv AS SELECT 1 AS id WITH NO DATA;
COPY copytest_mv(id) TO stdout WITH (header);
REFRESH MATERIALIZED VIEW copytest_mv;
COPY copytest_mv(id) TO stdout WITH (header);
DROP MATERIALIZED VIEW copytest_mv;
