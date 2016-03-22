#!/bin/bash

modeldir="src/main/resources/assets/foundry/models"
blockstatedir="src/main/resources/assets/foundry/blockstates"

for item in $(cat "items.list")
do
  ( # Item model
  cat <<- EOF
	{
	  "parent": "builtin/generated",
	  "textures":
	  {
	      "layer0": "foundry:items/@@TEXTURE@@"
	  }
	}
	EOF
  ) | sed -e "s/@@TEXTURE@@/"$item"/g" > $modeldir"/item/"$item".json"
done

for fluid in $(cat "fluids.list")
do
  ( # Fluid model
    cat <<- EOF
	{
	  "forge_marker": 1,
	  "variants":
	  {
	    "normal":
	    {
	      "model": "forge:fluid",
	      "custom": { "fluid": "@@FLUID@@" }
	    }
	  }
	}
	EOF
  ) | sed -e "s/@@FLUID@@/"$(echo $fluid | tr '[A-Z]' '[a-z]')"/g" > $blockstatedir"/"$fluid".json"
done

