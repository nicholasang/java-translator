function invalid_arg_exit {
    echo "usage: ./cmp.sh <test_number> optional:[t] or [bt]"
    exit
}
function pre_clean {
    if [ -f "$1" ]
    then
        rm $1
    fi
}
# run sbt translation and C++ compilation commands
function translate_and_compile {
    if [ ! -f "src/test/java/inputs/test$1/Test$1.java" ]
    then
        echo "TEST FILE DOES NOT EXIT"
        exit
    fi
    
    echo -e "runxtc -translateJava src/test/java/inputs/test$1/Test$1.java\ncpp\nexit" | sbt
}
function translate_and_compile_batch {
    if [ ! -f "src/test/java/inputs/test$1/Test$1.java" ]
    then
        echo "TEST FILE DOES NOT EXIT"
        exit
    fi
    
    echo -e "runxtc -translateJava src/test/java/inputs/test$1/Test$1.java\ncpp\n" > ./pipe
}
# compile run Java program, output standard out to file jOut.txt
function output_java {
    cd ./src/test/java/inputs/test$1    
    javac *.java
    cd ../..
    
    java inputs/test$1/Test$1 > ../jOut.txt
    
    cd inputs/test$1
    cd ../../../../..
}
# run the C++ program, output standard out to file cppOut.txt
function output_cpp {
    cd output
        
    local tries_left=2
    
    while [ ! -f "a.out" ]; do
        echo "...waiting"
        sleep 1
        
        if [ $tries_left -eq 0 ]
        then
            return 1
        fi
        
        let tries_left=tries_left-1
    done
    
    ./a.out > "../src/test/cppOut.txt"
    rm a.out
    rm main.cpp
    rm output.cpp
    rm output.h
    
    return 0
}
# compare the outputs of Java and C++ program,
# if no difference, translation successful
function comp_java_cpp {
    cd ../src/test
    
    local COMP=$(diff jOut.txt cppOut.txt)
    if [ "$COMP" ]
    then
        echo "DIFF: $COMP"
        echo "INCORRECT Translation of $1"
        RECORD="$RECORD$1 -INCONSISTENCY\n"

    else
        echo "SUCCESSFUL Translation of $1"
    fi
    
    rm jOut.txt cppOut.txt
    cd ../../    
}

#run all functions in order
function run_test {
    pre_clean "./src/test/record.txt"
    touch "./src/test/record.txt"
    
    output_java $1
    output_cpp
    if [ "$?" -eq 0 ]
    then
        comp_java_cpp $1
    else
        cd ../src/test
        rm jOut.txt
        cd ../../
        echo "FAILED TO COMPILE Translation of $1"
        RECORD="$RECORD$1 -COMPILE ERROR\n"
    fi
}

##################################################

# start at the 4-Tran project directory, run cpInit.sh first and wait for sbt to initialize
# enter at least two arguments 
# (the test number xyz and 't') or 
# (which test to run last and 'bt')(e.g. 5 bt to test tests 000,001,002,003,004,005 in a row)
# for the 'bt' option, enter ./cmpInit.sh in one terminal window and wait for sbt to start
# (in that window you can see what sbt is doing)
# then open another terminal window and run ./cmpRun.sh <args>
# errors recorded in ./src/test/record.txt
# ~ Karl

RECORD=""

if [ "$#" -eq 2 ]
then
    if [ "$2" = "t" ]
    then
        pre_clean "./src/test/jOut.txt"
        pre_clean "./src/test/cppOut.txt"
        pre_clean "./output/a.out"
        pre_clean "./output/main.cpp"
        pre_clean "./output/output.cpp"
        pre_clean "./output/output.h"
        
        translate_and_compile $1
        run_test $1
    elif [ "$2" = "bt" ]
    then
        pre_clean "./src/test/jOut.txt"
        pre_clean "./src/test/cppOut.txt"
        pre_clean "./output/a.out"
        pre_clean "./output/main.cpp"
        pre_clean "./output/output.cpp"
        pre_clean "./output/output.h"
        
        for i in $(seq -f "%03g" 0 "$1")
        do
            translate_and_compile_batch $i
            run_test $i
            echo "---------------------------------------------"
        done
    else
        invalid_arg_exit
    fi
else
    invalid_arg_exit
fi

echo -e $RECORD >> ./src/test/record.txt
