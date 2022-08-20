#!/usr/bin/env sh
while true
do
   inotifywait.exe -r -e modify .app/src
  ./gradlew app:test && git commit -am "[WIP]" || git reset --hard
done