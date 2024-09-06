## Agent Smith

Java agent to learn and have fun with Java bytecode modification.

### build

```shell
git clone git@github.com:SylvainJuge/agent-smith.git
cd agent-smith
./mvnw clean package
```

The agent jar in `target/smith-agent-1.0-SNAPSHOT.jar` should be used with the `-javaagent:` JVM argument.

## Features

### Dump bytecode

Set the `-Dsmith.dump_path=/tmp/dump` JVM system property to dump all the classes in `/tmp/dump`.

### Shattered Pixel Dungeon game cheat

[Shattered Pixel](https://github.com/00-Evan/shattered-pixel-dungeon/) is a very nice game written in Java and available under

Set the `-Dsmith.cheat=true` to enable cheating

Within the `agent-smith` folder:

```shell
# download game (only needed once)
./examples/shattered-pixel-dungeon/download.sh

# run game normally
./examples/shattered-pixel-dungeon/run.sh 0

# run game and dump game bytecode
./examples/shattered-pixel-dungeon/run.sh 1

# run game with cheat enabled
./examples/shattered-pixel-dungeon/run.sh 2
```


