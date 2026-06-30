/*
 * This file has been altered by SplendidData.
 * It is only used for syntax checking, not for the testing of a commandline paser.
 * So input for the copy statements is removed.
 * The deactivated lines are marked by: -- Deactivated for SplendidDataTest: 
 */



-- This test is about EUC_KR encoding, chosen as perhaps the most prevalent
-- non-UTF8, multibyte encoding as of 2026-01.  Since UTF8 can represent all
-- of EUC_KR, also run the test in UTF8.
-- Deactivated for SplendidDataTest: SELECT getdatabaseencoding() NOT IN ('EUC_KR', 'UTF8') AS skip_test \gset
\if :skip_test
\quit
\endif

-- Exercise is_multibyte_char_in_char (non-UTF8) slow path.
SELECT POSITION(
	convert_from('\xbcf6c7d0', 'EUC_KR') IN
	convert_from('\xb0fac7d02c20bcf6c7d02c20b1e2bcfa2c20bbee', 'EUC_KR'));
