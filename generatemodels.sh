#!/bin/bash

modeldir="src/main/resources/assets/foundry/models"

rm -f $texturedir/liquid*_still.png.mcmeta
rm -f $texturedir/liquid*_flow.png.mcmeta

for block in $(cat "blocks.list")
do
  (
  cat <<- EOF
	{
	  "parent": "minecraft:block/cube_all",
	  "textures":
	  {
	    "all": "foundry:blocks/@@TEXTURE@@"
	  }
	}
	EOF
  ) | sed -e "s/@@TEXTURE@@/"$block"/g" > $modeldir"/block/"$block".json"

  (
  cat <<- EOF
	{
	    "parent": "foundry:block/@@MODEL@@",
	    "display": {
	        "thirdperson": {
	            "rotation": [ 10, -45, 170 ],
	            "translation": [ 0, 1.5, -2.75 ],
	            "scale": [ 0.375, 0.375, 0.375 ]
	        }
	    }
	}
	EOF
  ) | sed -e "s/@@MODEL@@/"$block"/g" > $modeldir"/item/"$block".json"
done

for slab in $(cat "slabs.list")
do
  (
  cat <<- EOF
	{
	   "parent": "minecraft:block/half_slab",
	   "textures":
	   {
	     "bottom": "foundry:blocks/@@TEXTURE@@",
	     "top": "foundry:blocks/@@TEXTURE@@",
	     "side": "foundry:blocks/@@TEXTURE@@"
	  }
	}
	EOF
  ) | sed -e "s/@@TEXTURE@@/block"$slab"/g" \
    | sed -e 's/foundry:blocks\/blockIron/minecraft:blocks\/iron_block/g' \
    | sed -e 's/foundry:blocks\/blockGold/minecraft:blocks\/gold_block/g' > $modeldir"/block/slabBottom"$slab".json"

  (
  cat <<- EOF
	{
	   "parent": "minecraft:block/upper_slab",
	   "textures":
	   {
	     "bottom": "foundry:blocks/@@TEXTURE@@",
	     "top": "foundry:blocks/@@TEXTURE@@",
	     "side": "foundry:blocks/@@TEXTURE@@"
	  }
	}
	EOF
  ) | sed -e "s/@@TEXTURE@@/block"$slab"/g" \
    | sed -e 's/foundry:blocks\/blockIron/minecraft:blocks\/iron_block/g' \
    | sed -e 's/foundry:blocks\/blockGold/minecraft:blocks\/gold_block/g' > $modeldir"/block/slabTop"$slab".json"

  (
  cat <<- EOF
	{
	    "parent": "foundry:block/@@MODEL@@",
	    "display": {
	        "thirdperson": {
	            "rotation": [ 10, -45, 170 ],
	            "translation": [ 0, 1.5, -2.75 ],
	            "scale": [ 0.375, 0.375, 0.375 ]
	        }
	    }
	}
	EOF
  ) | sed -e "s/@@MODEL@@/slabBottom"$slab"/g" > $modeldir"/item/slab"$slab".json"
done


