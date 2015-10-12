package exter.foundry.integration;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationTwilightForest extends ModIntegration
{
  private Fluid liquid_ironwood;
  private Fluid liquid_steeleaf;
  private Fluid liquid_knightmetal;
  
  public ModIntegrationTwilightForest(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void onPreInit(Configuration config)
  {
    liquid_ironwood = LiquidMetalRegistry.instance.registerLiquidMetal( "Ironwood", 1850, 15);
    liquid_steeleaf = LiquidMetalRegistry.instance.registerLiquidMetal( "Steeleaf", 1850, 15);
    liquid_knightmetal = LiquidMetalRegistry.instance.registerLiquidMetal( "Knightmetal", 1900, 15);
    
    FoundryUtils.registerBasicMeltingRecipes("Ironwood", liquid_ironwood);
    FoundryUtils.registerBasicMeltingRecipes("Steeleaf", liquid_steeleaf);
    FoundryUtils.registerBasicMeltingRecipes("Knightmetal", liquid_knightmetal);
  }

  @Override
  public void onInit()
  {

  }

  //Find the enchanted versions of a tool/armor that their crafting recipes make.
  private ItemStack FindEnchanted(Item item)
  {
    if(item == null)
    {
      return null;
    }
    for(Object obj:CraftingManager.getInstance().getRecipeList())
    {
      ItemStack output = ((IRecipe)obj).getRecipeOutput();
      if(output != null && output.getItem() == item)
      {
        return output;
      }
    }
    return null;
  }
  
  @Override
  public void onPostInit()
  {
    if(!Loader.isModLoaded("TwilightForest"))
    {
      is_loaded = false;
      return;
    }

    ItemStack ironwood_raw = new ItemStack(GameRegistry.findItem("TwilightForest", "item.ironwoodRaw"));
    ItemStack ironwood_ingot = new ItemStack(GameRegistry.findItem("TwilightForest", "item.ironwoodIngot"));
    ItemStack steeleaf_ingot = new ItemStack(GameRegistry.findItem("TwilightForest", "item.steeleafIngot"));
    ItemStack knightmetal_ingot = new ItemStack(GameRegistry.findItem("TwilightForest", "item.knightMetal"));
    ItemStack knightmetal_block = new ItemStack(GameRegistry.findItem("TwilightForest", "tile.KnightmetalBlock"));
    FoundryMiscUtils.registerInOreDictionary("ingotIronwood", ironwood_ingot);
    FoundryMiscUtils.registerInOreDictionary("ingotSteeleaf", steeleaf_ingot);
    FoundryMiscUtils.registerInOreDictionary("ingotKnightmetal", knightmetal_ingot);
    FoundryMiscUtils.registerInOreDictionary("blockKnightmetal", knightmetal_block);
    
    ItemStack kightmetal_cluster = new ItemStack(GameRegistry.findItem("TwilightForest", "item.shardCluster"));
    ItemStack kightmetal_shard = new ItemStack(GameRegistry.findItem("TwilightForest", "item.armorShards"));

    MeltingRecipeManager.instance.addRecipe(ironwood_raw, new FluidStack(liquid_ironwood,FoundryAPI.FLUID_AMOUNT_INGOT * 2));

    MeltingRecipeManager.instance.addRecipe(kightmetal_cluster, new FluidStack(liquid_knightmetal,FoundryAPI.FLUID_AMOUNT_INGOT));
    MeltingRecipeManager.instance.addRecipe(kightmetal_shard, new FluidStack(liquid_knightmetal,FoundryAPI.FLUID_AMOUNT_NUGGET));

    registerCasting(ironwood_ingot, liquid_ironwood, 1, ItemMold.MOLD_INGOT, null);
    registerCasting(steeleaf_ingot, liquid_steeleaf, 1, ItemMold.MOLD_INGOT, null);
    registerCasting(knightmetal_ingot, liquid_knightmetal, 1, ItemMold.MOLD_INGOT, null);
    registerCasting(knightmetal_block, liquid_knightmetal, 9, ItemMold.MOLD_BLOCK, null);

    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_INGOT_SOFT, ironwood_ingot);
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_INGOT_SOFT, steeleaf_ingot);
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_INGOT_SOFT, knightmetal_ingot);
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BLOCK_SOFT, knightmetal_block);

    if(FoundryConfig.recipe_tools_armor)
    {
      ItemStack extra_sticks1 = new ItemStack(Items.stick, 1);
      ItemStack extra_sticks2 = new ItemStack(Items.stick, 2);
      
      ItemStack ironwood_axe = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.ironwoodAxe"));
      ItemStack ironwood_sword = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.ironwoodSword"));
      ItemStack ironwood_pickaxe = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.ironwoodPick"));
      ItemStack ironwood_shovel = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.ironwoodShovel"));
      ItemStack ironwood_hoe = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.ironwoodHoe"));

      ItemStack ironwood_helmet = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.ironwoodHelm"));
      ItemStack ironwood_chestplate = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.ironwoodPlate"));
      ItemStack ironwood_leggings = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.ironwoodLegs"));
      ItemStack ironwood_boots = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.ironwoodBoots"));

      ItemStack steeleaf_axe = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.steeleafAxe"));
      ItemStack steeleaf_sword = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.steeleafSword"));
      ItemStack steeleaf_pickaxe = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.steeleafPick"));
      ItemStack steeleaf_shovel = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.steeleafShovel"));
      ItemStack steeleaf_hoe = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.steeleafHoe"));

      ItemStack steeleaf_helmet = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.steeleafHelm"));
      ItemStack steeleaf_chestplate = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.steeleafPlate"));
      ItemStack steeleaf_leggings = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.steeleafLegs"));
      ItemStack steeleaf_boots = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.steeleafBoots"));

      ItemStack knightmetal_axe = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.knightlyAxe"));
      ItemStack knightmetal_sword = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.knightlySword"));
      ItemStack knightmetal_pickaxe = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.knightlyPick"));
      ItemStack knightmetal_shovel = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.knightlyShovel"));
      ItemStack knightmetal_hoe = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.knightlyHoe"));

      ItemStack knightmetal_helmet = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.knightlyHelm"));
      ItemStack knightmetal_chestplate = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.knightlyPlate"));
      ItemStack knightmetal_leggings = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.knightlyLegs"));
      ItemStack knightmetal_boots = FindEnchanted(GameRegistry.findItem("TwilightForest", "item.knightlyBoots"));

      
      registerCasting(ironwood_axe, liquid_ironwood, 3, ItemMold.MOLD_AXE, extra_sticks2);
      registerCasting(ironwood_sword, liquid_ironwood, 2, ItemMold.MOLD_SWORD, extra_sticks1);
      registerCasting(ironwood_pickaxe, liquid_ironwood, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      registerCasting(ironwood_shovel, liquid_ironwood, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      registerCasting(ironwood_hoe, liquid_ironwood, 2, ItemMold.MOLD_HOE, extra_sticks2);

      registerCasting(ironwood_helmet, liquid_ironwood, 5, ItemMold.MOLD_HELMET, null);
      registerCasting(ironwood_chestplate, liquid_ironwood, 8, ItemMold.MOLD_CHESTPLATE, null);
      registerCasting(ironwood_leggings, liquid_ironwood, 7, ItemMold.MOLD_LEGGINGS, null);
      registerCasting(ironwood_boots, liquid_ironwood, 4, ItemMold.MOLD_BOOTS, null);

      registerCasting(steeleaf_axe, liquid_steeleaf, 3, ItemMold.MOLD_AXE, extra_sticks2);
      registerCasting(steeleaf_sword, liquid_steeleaf, 2, ItemMold.MOLD_SWORD, extra_sticks1);
      registerCasting(steeleaf_pickaxe, liquid_steeleaf, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      registerCasting(steeleaf_shovel, liquid_steeleaf, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      registerCasting(steeleaf_hoe, liquid_steeleaf, 2, ItemMold.MOLD_HOE, extra_sticks2);

      registerCasting(steeleaf_helmet, liquid_steeleaf, 5, ItemMold.MOLD_HELMET, null);
      registerCasting(steeleaf_chestplate, liquid_steeleaf, 8, ItemMold.MOLD_CHESTPLATE, null);
      registerCasting(steeleaf_leggings, liquid_steeleaf, 7, ItemMold.MOLD_LEGGINGS, null);
      registerCasting(steeleaf_boots, liquid_steeleaf, 4, ItemMold.MOLD_BOOTS, null);
   
      registerCasting(knightmetal_axe, liquid_knightmetal, 3, ItemMold.MOLD_AXE, extra_sticks2);
      registerCasting(knightmetal_sword, liquid_knightmetal, 2, ItemMold.MOLD_SWORD, extra_sticks1);
      registerCasting(knightmetal_pickaxe, liquid_knightmetal, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      registerCasting(knightmetal_shovel, liquid_knightmetal, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      registerCasting(knightmetal_hoe, liquid_knightmetal, 2, ItemMold.MOLD_HOE, extra_sticks2);

      registerCasting(knightmetal_helmet, liquid_knightmetal, 5, ItemMold.MOLD_HELMET, null);
      registerCasting(knightmetal_chestplate, liquid_knightmetal, 8, ItemMold.MOLD_CHESTPLATE, null);
      registerCasting(knightmetal_leggings, liquid_knightmetal, 7, ItemMold.MOLD_LEGGINGS, null);
      registerCasting(knightmetal_boots, liquid_knightmetal, 4, ItemMold.MOLD_BOOTS, null);
      
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_AXE_SOFT, ironwood_axe);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SWORD_SOFT, ironwood_sword);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, ironwood_pickaxe);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, ironwood_shovel);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HOE_SOFT, ironwood_hoe);

      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HELMET_SOFT, ironwood_helmet);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, ironwood_chestplate);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, ironwood_leggings);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, ironwood_boots);

      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_AXE_SOFT, steeleaf_axe);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SWORD_SOFT, steeleaf_sword);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, steeleaf_pickaxe);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, steeleaf_shovel);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HOE_SOFT, steeleaf_hoe);

      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HELMET_SOFT, steeleaf_helmet);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, steeleaf_chestplate);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, steeleaf_leggings);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, steeleaf_boots);

      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_AXE_SOFT, knightmetal_axe);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SWORD_SOFT, knightmetal_sword);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, knightmetal_pickaxe);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, knightmetal_shovel);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HOE_SOFT, knightmetal_hoe);

      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HELMET_SOFT, knightmetal_helmet);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, knightmetal_chestplate);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, knightmetal_leggings);
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, knightmetal_boots);
    }
  }
}
