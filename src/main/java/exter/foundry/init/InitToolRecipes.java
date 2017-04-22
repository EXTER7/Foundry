package exter.foundry.init;

import java.util.Map.Entry;

import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.MoldRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class InitToolRecipes
{
  static public void init()
  {
    OreMatcher extra_sticks1 = new OreMatcher("stickWood",1);
    OreMatcher extra_sticks2 = new OreMatcher("stickWood",2);


    ItemStack mold_chestplate = FoundryItems.mold(ItemMold.SubItem.CHESTPLATE);
    ItemStack mold_pickaxe = FoundryItems.mold(ItemMold.SubItem.PICKAXE);
    ItemStack mold_axe = FoundryItems.mold(ItemMold.SubItem.AXE);
    ItemStack mold_shovel = FoundryItems.mold(ItemMold.SubItem.SHOVEL);
    ItemStack mold_hoe = FoundryItems.mold(ItemMold.SubItem.HOE);
    ItemStack mold_sword = FoundryItems.mold(ItemMold.SubItem.SWORD);
    ItemStack mold_leggings = FoundryItems.mold(ItemMold.SubItem.LEGGINGS);
    ItemStack mold_helmet = FoundryItems.mold(ItemMold.SubItem.HELMET);
    ItemStack mold_boots = FoundryItems.mold(ItemMold.SubItem.BOOTS);

    MoldRecipeManager.instance.addRecipe(mold_helmet, 4, 3, new int[]
        {
            3, 3, 3, 3,
            3, 1, 1, 3,
            3, 1, 1, 3
        });

    MoldRecipeManager.instance.addRecipe(mold_chestplate, 6, 6, new int[]
        {
            3, 1, 0, 0, 1, 3,
            3, 1, 0, 0, 1, 3,
            3, 1, 1, 1, 1, 3,
            3, 1, 1, 1, 1, 3,
            3, 1, 1, 1, 1, 3,
            3, 1, 1, 1, 1, 3
        });
      
    MoldRecipeManager.instance.addRecipe(mold_leggings, 6, 6, new int[]
        {
            3, 1, 1, 1, 1, 3,
            3, 1, 1, 1, 1, 3,
            3, 1, 0, 0, 1, 3,
            3, 1, 0, 0, 1, 3,
            3, 1, 0, 0, 1, 3,
            3, 1, 0, 0, 1, 3
        });

    MoldRecipeManager.instance.addRecipe(mold_boots, 6, 6, new int[]
        {
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            2, 3, 0, 0, 3, 2,
            3, 3, 0, 0, 3, 3,
            3, 3, 0, 0, 3, 3
        });

    MoldRecipeManager.instance.addRecipe(mold_pickaxe, 5, 5, new int[]
        {
            0, 2, 2, 2, 0,
            1, 0, 1, 0, 1,
            0, 0, 1, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 1, 0, 0
        });

    MoldRecipeManager.instance.addRecipe(mold_axe, 3, 5, new int[]
        {
            1, 2, 2,
            1, 2, 1,
            1, 0, 1,
            0, 0, 1,
            0, 0, 1
        });

    MoldRecipeManager.instance.addRecipe(mold_shovel, 3, 6, new int[]
        {
            0, 1, 0,
            1, 1, 1,
            1, 1, 1,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0
        });

    MoldRecipeManager.instance.addRecipe(mold_hoe, 3, 5, new int[]
        {
            0, 2, 2,
            1, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
        });

    MoldRecipeManager.instance.addRecipe(mold_sword, 3, 6, new int[]
        {
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            1, 2, 1,
            0, 1, 0,
        });



    FoundryMiscUtils.registerCasting(new ItemStack(Items.IRON_PICKAXE), FoundryFluids.liquid_iron, 3, ItemMold.SubItem.PICKAXE, extra_sticks2);
    FoundryMiscUtils.registerCasting(new ItemStack(Items.GOLDEN_PICKAXE), FoundryFluids.liquid_gold, 3, ItemMold.SubItem.PICKAXE, extra_sticks2);

    FoundryMiscUtils.registerCasting(new ItemStack(Items.IRON_AXE), FoundryFluids.liquid_iron, 3, ItemMold.SubItem.AXE, extra_sticks2);
    FoundryMiscUtils.registerCasting(new ItemStack(Items.GOLDEN_AXE), FoundryFluids.liquid_gold, 3, ItemMold.SubItem.AXE, extra_sticks2);

    FoundryMiscUtils.registerCasting(new ItemStack(Items.IRON_SHOVEL), FoundryFluids.liquid_iron, 1, ItemMold.SubItem.SHOVEL, extra_sticks2);
    FoundryMiscUtils.registerCasting(new ItemStack(Items.GOLDEN_SHOVEL), FoundryFluids.liquid_gold, 1, ItemMold.SubItem.SHOVEL, extra_sticks2);

    FoundryMiscUtils.registerCasting(new ItemStack(Items.IRON_HOE), FoundryFluids.liquid_iron, 2, ItemMold.SubItem.HOE, extra_sticks2);
    FoundryMiscUtils.registerCasting(new ItemStack(Items.GOLDEN_HOE), FoundryFluids.liquid_gold, 2, ItemMold.SubItem.HOE, extra_sticks2);

    FoundryMiscUtils.registerCasting(new ItemStack(Items.IRON_SWORD), FoundryFluids.liquid_iron, 2, ItemMold.SubItem.SWORD, extra_sticks1);
    FoundryMiscUtils.registerCasting(new ItemStack(Items.GOLDEN_SWORD), FoundryFluids.liquid_gold, 2, ItemMold.SubItem.SWORD, extra_sticks1);


    FoundryMiscUtils.registerCasting(new ItemStack(Items.IRON_HELMET), FoundryFluids.liquid_iron, 5, ItemMold.SubItem.HELMET, null);
    FoundryMiscUtils.registerCasting(new ItemStack(Items.GOLDEN_HELMET), FoundryFluids.liquid_gold, 5, ItemMold.SubItem.HELMET, null);

    FoundryMiscUtils.registerCasting(new ItemStack(Items.IRON_CHESTPLATE), FoundryFluids.liquid_iron, 8, ItemMold.SubItem.CHESTPLATE, null);
    FoundryMiscUtils.registerCasting(new ItemStack(Items.GOLDEN_CHESTPLATE), FoundryFluids.liquid_gold, 8, ItemMold.SubItem.CHESTPLATE, null);

    FoundryMiscUtils.registerCasting(new ItemStack(Items.IRON_LEGGINGS), FoundryFluids.liquid_iron, 7, ItemMold.SubItem.LEGGINGS, null);
    FoundryMiscUtils.registerCasting(new ItemStack(Items.GOLDEN_LEGGINGS), FoundryFluids.liquid_gold, 7, ItemMold.SubItem.LEGGINGS, null);

    FoundryMiscUtils.registerCasting(new ItemStack(Items.IRON_BOOTS), FoundryFluids.liquid_iron, 4, ItemMold.SubItem.BOOTS, null);
    FoundryMiscUtils.registerCasting(new ItemStack(Items.GOLDEN_BOOTS), FoundryFluids.liquid_gold, 4, ItemMold.SubItem.BOOTS, null);
    
    for(Entry<String, FluidLiquidMetal> metal:LiquidMetalRegistry.instance.getFluids().entrySet())
    {
      FoundryMiscUtils.registerCasting(FoundryMiscUtils.getModItemFromOreDictionary("substratum", "pickaxe" + metal.getKey()), metal.getValue(), 3, ItemMold.SubItem.PICKAXE, extra_sticks2);
      FoundryMiscUtils.registerCasting(FoundryMiscUtils.getModItemFromOreDictionary("substratum", "axe" + metal.getKey()), metal.getValue(), 3, ItemMold.SubItem.AXE, extra_sticks2);
      FoundryMiscUtils.registerCasting(FoundryMiscUtils.getModItemFromOreDictionary("substratum", "shovel" + metal.getKey()), metal.getValue(), 1, ItemMold.SubItem.SHOVEL, extra_sticks2);
      FoundryMiscUtils.registerCasting(FoundryMiscUtils.getModItemFromOreDictionary("substratum", "hoe" + metal.getKey()), metal.getValue(), 2, ItemMold.SubItem.HOE, extra_sticks2);
      FoundryMiscUtils.registerCasting(FoundryMiscUtils.getModItemFromOreDictionary("substratum", "sword" + metal.getKey()), metal.getValue(), 2, ItemMold.SubItem.SWORD, extra_sticks1);
      FoundryMiscUtils.registerCasting(FoundryMiscUtils.getModItemFromOreDictionary("substratum", "helmet" + metal.getKey()), metal.getValue(), 5, ItemMold.SubItem.HELMET, null);
      FoundryMiscUtils.registerCasting(FoundryMiscUtils.getModItemFromOreDictionary("substratum", "chestplate" + metal.getKey()), metal.getValue(), 8, ItemMold.SubItem.CHESTPLATE, null);
      FoundryMiscUtils.registerCasting(FoundryMiscUtils.getModItemFromOreDictionary("substratum", "leggings" + metal.getKey()), metal.getValue(), 7, ItemMold.SubItem.LEGGINGS, null);
      FoundryMiscUtils.registerCasting(FoundryMiscUtils.getModItemFromOreDictionary("substratum", "boots" + metal.getKey()), metal.getValue(), 4, ItemMold.SubItem.BOOTS, null);
    }
  }
}
