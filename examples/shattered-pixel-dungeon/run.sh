#!/bin/bash

folder="$(dirname "${0}")"

scenario="${1:-0}"

# extra JVM option needed for macos only
macos_option=''
java -X 2>&1|grep -q 'startOnFirstThread'
if [ $? == 0 ]; then
  macos_option='-XstartOnFirstThread'
fi

case ${scenario} in
  0)
    echo "run game"
    java \
      ${macos_option} \
      -jar ${folder}/shattered-pixel-dungeon.jar
    ;;
  1)
    echo "run game and dump classes in ${folder}/class_dump"
    java \
      ${macos_option} \
      -javaagent:${folder}/../../target/smith-agent-1.0-SNAPSHOT.jar \
      -Dsmith.dump_path=${folder}/class_dump \
      -jar ${folder}/shattered-pixel-dungeon.jar
    ;;
  2)
    echo "run game with cheat"
    java \
      ${macos_option} \
      -javaagent:${folder}/../../target/smith-agent-1.0-SNAPSHOT.jar \
      -Dsmith.cheat=true \
      -jar ${folder}/shattered-pixel-dungeon.jar
    ;;
  *)
    echo "unknown scenario: ${scenario}"
    ;;
esac

