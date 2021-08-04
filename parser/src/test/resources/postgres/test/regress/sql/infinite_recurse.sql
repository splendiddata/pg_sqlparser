/*
 * This file has been altered by SplendidData.
 * It is only used for happy flow syntax checking, so erroneous statements are commented out here.
 * The deactivated lines are marked by: -- Deactivated for SplendidDataTest: 
 */



-- Check that stack depth detection mechanism works and
-- max_stack_depth is not set too high.

create function infinite_recurse() returns int as
'select infinite_recurse()' language sql;

-- Unfortunately, up till mid 2020 the Linux kernel had a bug in PPC64
-- signal handling that would cause this test to crash if it happened
-- to receive an sinval catchup interrupt while the stack is deep:
-- https://bugzilla.kernel.org/show_bug.cgi?id=205183
-- It is likely to be many years before that bug disappears from all
-- production kernels, so disable this test on such platforms.
-- (We still create the function, so as not to have a cross-platform
-- difference in the end state of the regression database.)

-- Deactivated for SplendidDataTest: SELECT version() ~ 'powerpc64[^,]*-linux-gnu'
-- Deactivated for SplendidDataTest:        AS skip_test \gset
-- Deactivated for SplendidDataTest: \if :skip_test
-- Deactivated for SplendidDataTest: \quit
-- Deactivated for SplendidDataTest: \endif

-- The full error report is not very stable, so we show only SQLSTATE
-- and primary error message.

-- Deactivated for SplendidDataTest: \set VERBOSITY sqlstate

select infinite_recurse();

-- Deactivated for SplendidDataTest: \echo :LAST_ERROR_MESSAGE
