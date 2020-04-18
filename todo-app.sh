#!/bin/sh

set -a
. .env
set +a

CMD=$1

. .env

start() {
    rm -rf ./todo-app/build/libs/*
    cd todo-app
    ./gradlew bootJar
    docker-compose -f ../docker-compose-todo-app.yml up -d
}

stop() {
    docker-compose -f docker-compose-todo-app.yml stop
}

case "${CMD}" in    
    start)
    start
    ;;
    stop)
    stop
    ;;
    *)
        echo "사용법 : ./todo-app.sh {start|stop}"
        exit 1
    ;;
esac

exit $RETVAL
