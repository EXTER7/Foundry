#!/bin/bash

MODNAME=foundry
MODVERSION=0.4.0.0
MODVERSION_API=0.4.0.0

rm -rf packed/*
if ./recompile.sh && ./reobfuscate_srg.sh
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
  ZIPFILE=$MODNAME"-"$MODVERSION".zip"
  zip -r $ZIPFILE *
  mv $ZIPFILE "../"
  cd .. 

  rm -rf packed/*

  mkdir -p "packed/exter/$MODNAME"
  cp -r "src/minecraft/exter/"$MODNAME"/api" "packed/exter/$MODNAME"

  cd packed
  APIZIPFILE=$MODNAME"-api-"$MODVERSION_API".zip"
  zip -r $APIZIPFILE *
  mv $APIZIPFILE "../"
  cd .. 

  rm -rf packed/*
  echo "$0: Build complete, '"$ZIPFILE"' and '"$APIZIPFILE"' generated."
else
  echo "$0: Compile failed, aborting build."
  exit 1
fi
