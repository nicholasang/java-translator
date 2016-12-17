
function create_pipe {
    if [ ! -p "$1" ]
    then
        mkfifo $1
    fi
}

# run this first (start at the 4-Tran Project directory)
# when sbt is initialized, run cmpRun.sh with desired arguments

pipe_path="./pipe"

create_pipe $pipe_path
tail -f $pipe_path | sbt
