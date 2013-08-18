#!/bin/bash

MODNAME=foundry

rm -rf packed/*
if ./recompile.sh && ./reobfuscate.sh
then
  mkdir -p "packed/exter"
  mkdir -p "packed/assets/"$MODNAME

  mkdir -p "packed/buildcraft"

  cp -r "reobf/minecraft/exter/"$MODNAME "packed/exter/"
  cp -r "src/minecraft/assets/"$MODNAME"/"* "packed/assets/$MODNAME/"

  cp -r "reobf/minecraft/buildcraft/api" "packed/buildcraft/"
  cp -r "reobf/minecraft/forestry" "packed/forestry/"
  cp -r "reobf/minecraft/ic2" "packed/ic2/"

  cd packed
  zip -r $MODNAME".zip" *
  mv $MODNAME".zip" "../"
  cd .. 
  echo "$0: Build complete, '"$MODNAME".zip' generated."
else
  echo "$0: Compile failed, aborting build."
  exit 1
fi
