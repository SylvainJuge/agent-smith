#!/bin/bash

version=2.4.2
folder="$(dirname $0)"

url="https://github.com/00-Evan/shattered-pixel-dungeon/releases/download/v${version}/ShatteredPD-v${version}-Java.jar"
curl --location --output ${folder}/shattered-pixel-dungeon.jar "${url}"
