## CMP Script
- In terminal, run cmpInit.sh and cmpRun.sh to batch-translate and compare Java output with translated C++ output
- errors and inconsistencies printed to the "record.txt" in the "test" directory as <test_number><error_type>
## More-detailed instructions:
- start at the 4-Tran project directory, run cmpInit.sh first and wait for sbt to initialize
- enter at least two arguments
  - (the test number xyz and 't') or
  - (which test to run last and 'bt')(e.g. 5 bt to test tests 000,001,002,003,004,005 in a row)
- NOTE: the bt option is the main incentive to use this utility
### for the 'bt' option, enter ./cmpInit.sh in one terminal window and wait for sbt to start
(in that window you can see what sbt is doing)
- then open another terminal window and run ./cmpRun.sh <args>
- errors are sent to the console and recorded in ./src/test/record.txt