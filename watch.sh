#!/usr/bin/env sh
#while true
#do
    #inotifywait -r -e modify .app/src
    ./gradlew app:test && git commit -am "[WIP]" || git reset --hard
#done
"./gradlew app:test && git commit -am [WIP] || git reset --hard"