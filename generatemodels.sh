#!/bin/bash

modeldir="src/main/resources/assets/foundry/models"
blockstatedir="src/main/resources/assets/foundry/blockstates"

rm -f $texturedir/liquid*_still.png.mcmeta
rm -f $texturedir/liquid*_flow.png.mcmeta

for block in $(cat "blocks.list")
do
  ( # Block model
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

  ( # Block item model
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
  ( # Bottom slab
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

  ( # Top slab
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

  ( # Slab item model
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

  ( # Stairs blockstate
  cat <<- EOF
	{
	  "variants": {
	    "facing=east,half=bottom,shape=straight":  { "model": "foundry:@@MODELSTRAIGHT@@" },
	    "facing=west,half=bottom,shape=straight":  { "model": "foundry:@@MODELSTRAIGHT@@", "y": 180, "uvlock": true },
	    "facing=south,half=bottom,shape=straight": { "model": "foundry:@@MODELSTRAIGHT@@", "y": 90, "uvlock": true },
	    "facing=north,half=bottom,shape=straight": { "model": "foundry:@@MODELSTRAIGHT@@", "y": 270, "uvlock": true },
	    "facing=east,half=bottom,shape=outer_right":  { "model": "foundry:@@MODELOUTER@@" },
	    "facing=west,half=bottom,shape=outer_right":  { "model": "foundry:@@MODELOUTER@@", "y": 180, "uvlock": true },
	    "facing=south,half=bottom,shape=outer_right": { "model": "foundry:@@MODELOUTER@@", "y": 90, "uvlock": true },
	    "facing=north,half=bottom,shape=outer_right": { "model": "foundry:@@MODELOUTER@@", "y": 270, "uvlock": true },
	    "facing=east,half=bottom,shape=outer_left":  { "model": "foundry:@@MODELOUTER@@", "y": 270, "uvlock": true },
	    "facing=west,half=bottom,shape=outer_left":  { "model": "foundry:@@MODELOUTER@@", "y": 90, "uvlock": true },
	    "facing=south,half=bottom,shape=outer_left": { "model": "foundry:@@MODELOUTER@@" },
	    "facing=north,half=bottom,shape=outer_left": { "model": "foundry:@@MODELOUTER@@", "y": 180, "uvlock": true },
	    "facing=east,half=bottom,shape=inner_right":  { "model": "foundry:@@MODELINNER@@" },
	    "facing=west,half=bottom,shape=inner_right":  { "model": "foundry:@@MODELINNER@@", "y": 180, "uvlock": true },
	    "facing=south,half=bottom,shape=inner_right": { "model": "foundry:@@MODELINNER@@", "y": 90, "uvlock": true },
	    "facing=north,half=bottom,shape=inner_right": { "model": "foundry:@@MODELINNER@@", "y": 270, "uvlock": true },
	    "facing=east,half=bottom,shape=inner_left":  { "model": "foundry:@@MODELINNER@@", "y": 270, "uvlock": true },
	    "facing=west,half=bottom,shape=inner_left":  { "model": "foundry:@@MODELINNER@@", "y": 90, "uvlock": true },
	    "facing=south,half=bottom,shape=inner_left": { "model": "foundry:@@MODELINNER@@" },
	    "facing=north,half=bottom,shape=inner_left": { "model": "foundry:@@MODELINNER@@", "y": 180, "uvlock": true },
	    "facing=east,half=top,shape=straight":  { "model": "foundry:@@MODELSTRAIGHT@@", "x": 180, "uvlock": true },
	    "facing=west,half=top,shape=straight":  { "model": "foundry:@@MODELSTRAIGHT@@", "x": 180, "y": 180, "uvlock": true },
	    "facing=south,half=top,shape=straight": { "model": "foundry:@@MODELSTRAIGHT@@", "x": 180, "y": 90, "uvlock": true },
	    "facing=north,half=top,shape=straight": { "model": "foundry:@@MODELSTRAIGHT@@", "x": 180, "y": 270, "uvlock": true },
	    "facing=east,half=top,shape=outer_right":  { "model": "foundry:@@MODELOUTER@@", "x": 180, "uvlock": true },
	    "facing=west,half=top,shape=outer_right":  { "model": "foundry:@@MODELOUTER@@", "x": 180, "y": 180, "uvlock": true },
	    "facing=south,half=top,shape=outer_right": { "model": "foundry:@@MODELOUTER@@", "x": 180, "y": 90, "uvlock": true },
	    "facing=north,half=top,shape=outer_right": { "model": "foundry:@@MODELOUTER@@", "x": 180, "y": 270, "uvlock": true },
	    "facing=east,half=top,shape=outer_left":  { "model": "foundry:@@MODELOUTER@@", "x": 180, "y": 90, "uvlock": true },
	    "facing=west,half=top,shape=outer_left":  { "model": "foundry:@@MODELOUTER@@", "x": 180, "y": 270, "uvlock": true },
	    "facing=south,half=top,shape=outer_left": { "model": "foundry:@@MODELOUTER@@", "x": 180, "y": 180, "uvlock": true },
	    "facing=north,half=top,shape=outer_left": { "model": "foundry:@@MODELOUTER@@", "x": 180, "uvlock": true },
	    "facing=east,half=top,shape=inner_right":  { "model": "foundry:@@MODELINNER@@", "x": 180, "uvlock": true },
	    "facing=west,half=top,shape=inner_right":  { "model": "foundry:@@MODELINNER@@", "x": 180, "y": 180, "uvlock": true },
	    "facing=south,half=top,shape=inner_right": { "model": "foundry:@@MODELINNER@@", "x": 180, "y": 90, "uvlock": true },
	    "facing=north,half=top,shape=inner_right": { "model": "foundry:@@MODELINNER@@", "x": 180, "y": 270, "uvlock": true },
	    "facing=east,half=top,shape=inner_left":  { "model": "foundry:@@MODELINNER@@", "x": 180, "y": 90, "uvlock": true },
	    "facing=west,half=top,shape=inner_left":  { "model": "foundry:@@MODELINNER@@", "x": 180, "y": 270, "uvlock": true },
	    "facing=south,half=top,shape=inner_left": { "model": "foundry:@@MODELINNER@@", "x": 180, "y": 180, "uvlock": true },
	    "facing=north,half=top,shape=inner_left": { "model": "foundry:@@MODELINNER@@", "x": 180, "uvlock": true }
	  }
	}
	EOF
  ) | sed -e "s/@@MODELSTRAIGHT@@/stairsStraight"$slab"/g" \
    | sed -e "s/@@MODELINNER@@/stairsInner"$slab"/g" \
    | sed -e "s/@@MODELOUTER@@/stairsOuter"$slab"/g" > $blockstatedir"/stairs"$slab".json"

  ( # Straight stairs
  cat <<- EOF
	{
	  "parent": "minecraft:block/stairs",
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
    | sed -e 's/foundry:blocks\/blockGold/minecraft:blocks\/gold_block/g' > $modeldir"/block/stairsStraight"$slab".json"

  ( # Inner stairs
  cat <<- EOF
	{
	  "parent": "minecraft:block/inner_stairs",
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
    | sed -e 's/foundry:blocks\/blockGold/minecraft:blocks\/gold_block/g' > $modeldir"/block/stairsInner"$slab".json"

  ( # Outer stairs
  cat <<- EOF
	{
	  "parent": "minecraft:block/outer_stairs",
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
    | sed -e 's/foundry:blocks\/blockGold/minecraft:blocks\/gold_block/g' > $modeldir"/block/stairsOuter"$slab".json"

  ( # Stairs item model
  cat <<- EOF
	{
	    "parent": "foundry:block/@@MODEL@@",
	    "display": {
	        "inventory": {
	            "rotation": [ 0, 180, 0 ]
            },
	        "thirdperson": {
	            "rotation": [ 10, -45, 170 ],
	            "translation": [ 0, 1.5, -2.75 ],
	            "scale": [ 0.375, 0.375, 0.375 ]
	        }
	    }
	}
	EOF
  ) | sed -e "s/@@MODEL@@/stairsStraight"$slab"/g" > $modeldir"/item/stairs"$slab".json"
done


