#!/bin/bash

folder="$(dirname $0)"

scenario="${1:-0}"

case ${scenario} in
  0)
    echo "run game"
    java \
      -jar ${folder}/shattered-pixel-dungeon.jar
    ;;
  1)
    echo "run game and dump classes in ${folder}/class_dump"
    java \
      -javaagent:${folder}/../../../target/smith-agent-1.0-SNAPSHOT.jar \
      -Dsmith.dump_path=${folder}/class_dump \
      -jar ${folder}/shattered-pixel-dungeon.jar
    ;;
  2)
    echo "run game with cheat"
    java \
      -javaagent:${folder}/../../../target/smith-agent-1.0-SNAPSHOT.jar \
      -Dsmith.cheat=true \
      -jar ${folder}/shattered-pixel-dungeon.jar
    ;;
  *)
    echo "unknown scenario: ${scenario}"
    ;;
esac

