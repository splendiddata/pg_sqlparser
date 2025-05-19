/*
 * This file has been altered by SplendidData.
 * It is only used for happy flow syntax checking, so erroneous statements are commented out here.
 * The deactivated lines are marked by: -- Deactivated for SplendidDataTest: 
 */


--
-- Tests using psql pipelining
--

CREATE TABLE psql_pipeline(a INTEGER PRIMARY KEY, s TEXT);

-- Single query
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 'val1' \sendpipeline
\endpipeline
\startpipeline
SELECT 'val1';
\endpipeline

-- Multiple queries
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 'val1' \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1, $2 \bind 'val2' 'val3' \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1, $2 \bind 'val2' 'val3' \sendpipeline
SELECT 'val4';
SELECT 'val5', 'val6';
\endpipeline

-- Multiple queries in single line, separated by semicolons
\startpipeline
SELECT 1; SELECT 2; SELECT 3
;
\echo :PIPELINE_COMMAND_COUNT
\endpipeline

-- Test \flush
\startpipeline
\flush
-- Deactivated for SplendidDataTest: SELECT $1 \bind 'val1' \sendpipeline
\flush
-- Deactivated for SplendidDataTest: SELECT $1, $2 \bind 'val2' 'val3' \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1, $2 \bind 'val2' 'val3' \sendpipeline
\flush
SELECT 'val4';
SELECT 'val5', 'val6';
\endpipeline

-- Send multiple syncs
\startpipeline
\echo :PIPELINE_COMMAND_COUNT
\echo :PIPELINE_SYNC_COUNT
\echo :PIPELINE_RESULT_COUNT
-- Deactivated for SplendidDataTest: SELECT $1 \bind 'val1' \sendpipeline
\syncpipeline
\syncpipeline
-- Deactivated for SplendidDataTest: SELECT $1, $2 \bind 'val2' 'val3' \sendpipeline
\syncpipeline
-- Deactivated for SplendidDataTest: SELECT $1, $2 \bind 'val4' 'val5' \sendpipeline
\echo :PIPELINE_COMMAND_COUNT
\echo :PIPELINE_SYNC_COUNT
\echo :PIPELINE_RESULT_COUNT
SELECT 'val7';
\syncpipeline
\syncpipeline
SELECT 'val8';
\syncpipeline
SELECT 'val9';
\echo :PIPELINE_COMMAND_COUNT
\echo :PIPELINE_SYNC_COUNT
\echo :PIPELINE_RESULT_COUNT
\endpipeline

-- Query terminated with a semicolon replaces an unnamed prepared
-- statement.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \parse ''
SELECT 1;
\bind_named ''
\endpipeline

-- Extended query is appended to pipeline by a semicolon after a
-- newline.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1
;
SELECT 2;
\endpipeline

-- \startpipeline should not have any effect if already in a pipeline.
\startpipeline
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 'val1' \sendpipeline
\endpipeline

-- Convert an implicit transaction block to an explicit transaction block.
\startpipeline
-- Deactivated for SplendidDataTest: INSERT INTO psql_pipeline VALUES ($1) \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: BEGIN \bind \sendpipeline
-- Deactivated for SplendidDataTest: INSERT INTO psql_pipeline VALUES ($1) \bind 2 \sendpipeline
-- Deactivated for SplendidDataTest: ROLLBACK \bind \sendpipeline
\endpipeline

-- Multiple explicit transactions
\startpipeline
-- Deactivated for SplendidDataTest: BEGIN \bind \sendpipeline
-- Deactivated for SplendidDataTest: INSERT INTO psql_pipeline VALUES ($1) \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: ROLLBACK \bind \sendpipeline
-- Deactivated for SplendidDataTest: BEGIN \bind \sendpipeline
-- Deactivated for SplendidDataTest: INSERT INTO psql_pipeline VALUES ($1) \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: COMMIT \bind \sendpipeline
\endpipeline

-- COPY FROM STDIN
-- with \sendpipeline and \bind
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 'val1' \sendpipeline
-- Deactivated for SplendidDataTest: COPY psql_pipeline FROM STDIN \bind \sendpipeline
\endpipeline
-- Deactivated for SplendidDataTest: 2	test2
\.
-- with semicolon
\startpipeline
SELECT 'val1';
COPY psql_pipeline FROM STDIN;
\endpipeline
-- Deactivated for SplendidDataTest: 20	test2
\.

-- COPY FROM STDIN with \flushrequest + \getresults
-- with \sendpipeline and \bind
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 'val1' \sendpipeline
-- Deactivated for SplendidDataTest: COPY psql_pipeline FROM STDIN \bind \sendpipeline
\flushrequest
\getresults
-- Deactivated for SplendidDataTest: 3	test3
\.
\endpipeline
-- with semicolon
\startpipeline
SELECT 'val1';
COPY psql_pipeline FROM STDIN;
\flushrequest
\getresults
-- Deactivated for SplendidDataTest: 30	test3
\.
\endpipeline

-- COPY FROM STDIN with \syncpipeline + \getresults
-- with \bind and \sendpipeline
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 'val1' \sendpipeline
-- Deactivated for SplendidDataTest: COPY psql_pipeline FROM STDIN \bind \sendpipeline
\syncpipeline
\getresults
-- Deactivated for SplendidDataTest: 4	test4
\.
\endpipeline
-- with semicolon
\startpipeline
SELECT 'val1';
COPY psql_pipeline FROM STDIN;
\syncpipeline
\getresults
-- Deactivated for SplendidDataTest: 40	test4
\.
\endpipeline

-- COPY TO STDOUT
-- with \bind and \sendpipeline
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 'val1' \sendpipeline
-- Deactivated for SplendidDataTest: copy psql_pipeline TO STDOUT \bind \sendpipeline
\endpipeline
-- with semicolon
\startpipeline
SELECT 'val1';
copy psql_pipeline TO STDOUT;
\endpipeline

-- COPY TO STDOUT with \flushrequest + \getresults
-- with \bind and \sendpipeline
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 'val1' \sendpipeline
-- Deactivated for SplendidDataTest: copy psql_pipeline TO STDOUT \bind \sendpipeline
\flushrequest
\getresults
\endpipeline
-- with semicolon
\startpipeline
SELECT 'val1';
copy psql_pipeline TO STDOUT;
\flushrequest
\getresults
\endpipeline

-- COPY TO STDOUT with \syncpipeline + \getresults
-- with \bind and \sendpipeline
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 'val1' \sendpipeline
-- Deactivated for SplendidDataTest: copy psql_pipeline TO STDOUT \bind \sendpipeline
\syncpipeline
\getresults
\endpipeline
-- with semicolon
\startpipeline
SELECT 'val1';
copy psql_pipeline TO STDOUT;
\syncpipeline
\getresults
\endpipeline

-- Use \parse and \bind_named
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \parse ''
-- Deactivated for SplendidDataTest: SELECT $1, $2 \parse ''
-- Deactivated for SplendidDataTest: SELECT $2 \parse pipeline_1
-- Deactivated for SplendidDataTest: \bind_named '' 1 2 \sendpipeline
-- Deactivated for SplendidDataTest: \bind_named pipeline_1 2 \sendpipeline
\endpipeline

-- \getresults displays all results preceding a \flushrequest.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\flushrequest
\getresults
\endpipeline

-- \getresults displays all results preceding a \syncpipeline.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\syncpipeline
\getresults
\endpipeline

-- \getresults immediately returns if there is no result to fetch.
\startpipeline
\getresults
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\getresults
\flushrequest
\endpipeline
\getresults

-- \getresults only fetches results preceding a \flushrequest.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\flushrequest
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\getresults
\endpipeline

-- \getresults only fetches results preceding a \syncpipeline.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\syncpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\getresults
\endpipeline

-- Use pipeline with chunked results for both \getresults and \endpipeline.
\startpipeline
\set FETCH_COUNT 10
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\flushrequest
\getresults
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\endpipeline
\unset FETCH_COUNT

-- \getresults with specific number of requested results.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 3 \sendpipeline
\echo :PIPELINE_SYNC_COUNT
\syncpipeline
\echo :PIPELINE_SYNC_COUNT
\echo :PIPELINE_RESULT_COUNT
\getresults 1
\echo :PIPELINE_RESULT_COUNT
-- Deactivated for SplendidDataTest: SELECT $1 \bind 4 \sendpipeline
\getresults 3
\echo :PIPELINE_RESULT_COUNT
\endpipeline

-- \syncpipeline count as one command to fetch for \getresults.
\startpipeline
\syncpipeline
\syncpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
\flushrequest
\getresults 2
\getresults 1
\endpipeline

-- \getresults 0 should get all the results.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 3 \sendpipeline
\syncpipeline
\getresults 0
\endpipeline

--
-- Pipeline errors
--

-- \endpipeline outside of pipeline should fail
\endpipeline

-- After an aborted pipeline, commands after a \syncpipeline should be
-- displayed.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind \sendpipeline
\syncpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
\endpipeline

-- For an incorrect number of parameters, the pipeline is aborted and
-- the following queries will not be executed.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT \bind 'val1' \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 'val1' \sendpipeline
\endpipeline

-- Using a semicolon with a parameter triggers an error and aborts
-- the pipeline.
\startpipeline
SELECT $1;
SELECT 1;
\endpipeline

-- An explicit transaction with an error needs to be rollbacked after
-- the pipeline.
\startpipeline
-- Deactivated for SplendidDataTest: BEGIN \bind \sendpipeline
-- Deactivated for SplendidDataTest: INSERT INTO psql_pipeline VALUES ($1) \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: ROLLBACK \bind \sendpipeline
\endpipeline
ROLLBACK;

-- \watch is not allowed in a pipeline.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT \bind \sendpipeline
\watch 1
\endpipeline

-- \gdesc should fail as synchronous commands are not allowed in a pipeline,
-- and the pipeline should still be usable.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \gdesc
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
\endpipeline

-- \gset is not allowed in a pipeline, pipeline should still be usable.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 as i, $2 as j \parse ''
-- Deactivated for SplendidDataTest: SELECT $1 as k, $2 as l \parse 'second'
\bind_named '' 1 2 \gset
\bind_named second 1 2 \gset pref02_ \echo :pref02_i :pref02_j
\bind_named '' 1 2 \sendpipeline
\endpipeline

-- \g and \gx are not allowed, pipeline should still be usable.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \g
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \g (format=unaligned tuples_only=on)
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \gx
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \gx (format=unaligned tuples_only=on)
\reset
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
\endpipeline

-- \g and \gx warnings should be emitted in an aborted pipeline, with
-- pipeline still usable.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind \sendpipeline
\flushrequest
\getresults
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \g
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \gx
\endpipeline

-- \sendpipeline is not allowed outside of a pipeline
\sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
\reset

-- \sendpipeline is not allowed if not preceded by \bind or \bind_named
\startpipeline
\sendpipeline
-- Deactivated for SplendidDataTest: SELECT 1 \sendpipeline
\endpipeline

-- \gexec is not allowed, pipeline should still be usable.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT 'INSERT INTO psql_pipeline(a) SELECT generate_series(1, 10)' \parse 'insert_stmt'
\bind_named insert_stmt \gexec
\bind_named insert_stmt \sendpipeline
-- Deactivated for SplendidDataTest: SELECT COUNT(*) FROM psql_pipeline \bind \sendpipeline
\endpipeline

-- After an error, pipeline is aborted and requires \syncpipeline to be
-- reusable.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \parse a
\bind_named a 1 \sendpipeline
\close a
\flushrequest
\getresults
-- Pipeline is aborted.
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \parse a
\bind_named a 1 \sendpipeline
\close a
-- Sync allows pipeline to recover.
\syncpipeline
\getresults
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \parse a
\bind_named a 1 \sendpipeline
\close a
\flushrequest
\getresults
\endpipeline

-- In an aborted pipeline, \getresults 1 aborts commands one at a time.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \parse a
\bind_named a 1 \sendpipeline
\syncpipeline
\getresults 1
\getresults 1
\getresults 1
\getresults 1
\getresults 1
\endpipeline

-- Test chunked results with an aborted pipeline.
\startpipeline
\set FETCH_COUNT 10
-- Deactivated for SplendidDataTest: SELECT $1 \bind \sendpipeline
\flushrequest
\getresults
-- Deactivated for SplendidDataTest: SELECT $1 \bind \sendpipeline
\endpipeline
\unset FETCH_COUNT

-- \getresults returns an error when an incorrect number is provided.
\startpipeline
\getresults -1
\endpipeline

-- \getresults when there is no result should not impact the next
-- query executed.
\getresults 1
select 1;

-- Error messages accumulate and are repeated.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT 1 \bind \sendpipeline
\gdesc
\gdesc
\endpipeline

--
-- Pipelines and transaction blocks
--

-- SET LOCAL will issue a warning when modifying a GUC outside of a
-- transaction block.  The change will still be valid as a pipeline
-- runs within an implicit transaction block.  Sending a sync will
-- commit the implicit transaction block. The first command after a
-- sync will not be seen as belonging to a pipeline.
\startpipeline
-- Deactivated for SplendidDataTest: SET LOCAL statement_timeout='1h' \bind \sendpipeline
-- Deactivated for SplendidDataTest: SHOW statement_timeout \bind \sendpipeline
\syncpipeline
-- Deactivated for SplendidDataTest: SHOW statement_timeout \bind \sendpipeline
-- Deactivated for SplendidDataTest: SET LOCAL statement_timeout='2h' \bind \sendpipeline
-- Deactivated for SplendidDataTest: SHOW statement_timeout \bind \sendpipeline
\endpipeline

-- REINDEX CONCURRENTLY fails if not the first command in a pipeline.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: REINDEX TABLE CONCURRENTLY psql_pipeline \bind \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\endpipeline

-- REINDEX CONCURRENTLY works if it is the first command in a pipeline.
\startpipeline
-- Deactivated for SplendidDataTest: REINDEX TABLE CONCURRENTLY psql_pipeline \bind \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\endpipeline

-- Subtransactions are not allowed in a pipeline.
\startpipeline
-- Deactivated for SplendidDataTest: SAVEPOINT a \bind \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: ROLLBACK TO SAVEPOINT a \bind \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\endpipeline

-- LOCK fails as the first command in a pipeline, as not seen in an
-- implicit transaction block.
\startpipeline
-- Deactivated for SplendidDataTest: LOCK psql_pipeline \bind \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\endpipeline

-- LOCK succeeds as it is not the first command in a pipeline,
-- seen in an implicit transaction block.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 1 \sendpipeline
-- Deactivated for SplendidDataTest: LOCK psql_pipeline \bind \sendpipeline
-- Deactivated for SplendidDataTest: SELECT $1 \bind 2 \sendpipeline
\endpipeline

-- VACUUM works as the first command in a pipeline.
\startpipeline
-- Deactivated for SplendidDataTest: VACUUM psql_pipeline \bind \sendpipeline
\endpipeline

-- VACUUM fails when not the first command in a pipeline.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT 1 \bind \sendpipeline
-- Deactivated for SplendidDataTest: VACUUM psql_pipeline \bind \sendpipeline
\endpipeline

-- VACUUM works after a \syncpipeline.
\startpipeline
-- Deactivated for SplendidDataTest: SELECT 1 \bind \sendpipeline
\syncpipeline
-- Deactivated for SplendidDataTest: VACUUM psql_pipeline \bind \sendpipeline
\endpipeline

-- Clean up
DROP TABLE psql_pipeline;
