#!/bin/bash

texturedir="src/main/resources/assets/foundry/textures/blocks"

rm -f $texturedir/liquid*_still.png.mcmeta
rm -f $texturedir/liquid*_flow.png.mcmeta

for file in $(ls -1 $texturedir/liquid*_flow.png)
do
  meta="$file".mcmeta
  cp flow.png.mcmeta "$meta" && echo "Created "$meta
done

for file in $(ls -1 $texturedir/liquid*_still.png)
do
  meta="$file".mcmeta
  cp still.png.mcmeta "$meta" && echo "Created "$meta
done

