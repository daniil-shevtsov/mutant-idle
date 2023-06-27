#!/usr/bin/env sh
while true
do
   inotifywait.exe -r -e modify ./app/src/main/
  ./gradlew app:test && git commit -am "[WIP]" || git checkout HEAD -- ./app/src/main/
done