package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.api.FoundryAPI;
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
  public ModIntegrationTwilightForest(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {

  }

  @Override
  public void OnInit()
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
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("TwilightForest"))
    {
      is_loaded = false;
      return;
    }
    Fluid liquid_ironwood = LiquidMetalRegistry.instance.GetFluid("Ironwood");
    Fluid liquid_steeleaf = LiquidMetalRegistry.instance.GetFluid("Steeleaf");
    Fluid liquid_knightmetal = LiquidMetalRegistry.instance.GetFluid("Knightmetal");

    ItemStack ironwood_raw = new ItemStack(GameRegistry.findItem("TwilightForest", "item.ironwoodRaw"));
    ItemStack ironwood_ingot = new ItemStack(GameRegistry.findItem("TwilightForest", "item.ironwoodIngot"));
    ItemStack steeleaf_ingot = new ItemStack(GameRegistry.findItem("TwilightForest", "item.steeleafIngot"));
    ItemStack knightmetal_ingot = new ItemStack(GameRegistry.findItem("TwilightForest", "item.knightMetal"));
    ItemStack knightmetal_block = new ItemStack(GameRegistry.findItem("TwilightForest", "tile.KnightmetalBlock"));
    FoundryMiscUtils.RegisterInOreDictionary("ingotIronwood", ironwood_ingot);
    FoundryMiscUtils.RegisterInOreDictionary("ingotSteeleaf", steeleaf_ingot);
    FoundryMiscUtils.RegisterInOreDictionary("ingotKnightmetal", knightmetal_ingot);
    FoundryMiscUtils.RegisterInOreDictionary("blockKnightmetal", knightmetal_block);
    
    ItemStack kightmetal_cluster = new ItemStack(GameRegistry.findItem("TwilightForest", "item.shardCluster"));
    ItemStack kightmetal_shard = new ItemStack(GameRegistry.findItem("TwilightForest", "item.armorShards"));

    MeltingRecipeManager.instance.AddRecipe(ironwood_raw, new FluidStack(liquid_ironwood,FoundryAPI.FLUID_AMOUNT_INGOT * 2));

    MeltingRecipeManager.instance.AddRecipe(kightmetal_cluster, new FluidStack(liquid_knightmetal,FoundryAPI.FLUID_AMOUNT_INGOT));
    MeltingRecipeManager.instance.AddRecipe(kightmetal_shard, new FluidStack(liquid_knightmetal,FoundryAPI.FLUID_AMOUNT_NUGGET));

    RegisterCasting(ironwood_ingot, liquid_ironwood, 1, ItemMold.MOLD_INGOT, null);
    RegisterCasting(steeleaf_ingot, liquid_steeleaf, 1, ItemMold.MOLD_INGOT, null);
    RegisterCasting(knightmetal_ingot, liquid_knightmetal, 1, ItemMold.MOLD_INGOT, null);
    RegisterCasting(knightmetal_block, liquid_knightmetal, 9, ItemMold.MOLD_BLOCK, null);

    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_SOFT, ironwood_ingot);
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_SOFT, steeleaf_ingot);
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_SOFT, knightmetal_ingot);
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_SOFT, knightmetal_block);

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

      
      RegisterCasting(ironwood_axe, liquid_ironwood, 3, ItemMold.MOLD_AXE, extra_sticks2);
      RegisterCasting(ironwood_sword, liquid_ironwood, 2, ItemMold.MOLD_SWORD, extra_sticks1);
      RegisterCasting(ironwood_pickaxe, liquid_ironwood, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      RegisterCasting(ironwood_shovel, liquid_ironwood, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      RegisterCasting(ironwood_hoe, liquid_ironwood, 2, ItemMold.MOLD_HOE, extra_sticks2);

      RegisterCasting(ironwood_helmet, liquid_ironwood, 5, ItemMold.MOLD_HELMET, null);
      RegisterCasting(ironwood_chestplate, liquid_ironwood, 8, ItemMold.MOLD_CHESTPLATE, null);
      RegisterCasting(ironwood_leggings, liquid_ironwood, 7, ItemMold.MOLD_LEGGINGS, null);
      RegisterCasting(ironwood_boots, liquid_ironwood, 4, ItemMold.MOLD_BOOTS, null);

      RegisterCasting(steeleaf_axe, liquid_steeleaf, 3, ItemMold.MOLD_AXE, extra_sticks2);
      RegisterCasting(steeleaf_sword, liquid_steeleaf, 2, ItemMold.MOLD_SWORD, extra_sticks1);
      RegisterCasting(steeleaf_pickaxe, liquid_steeleaf, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      RegisterCasting(steeleaf_shovel, liquid_steeleaf, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      RegisterCasting(steeleaf_hoe, liquid_steeleaf, 2, ItemMold.MOLD_HOE, extra_sticks2);

      RegisterCasting(steeleaf_helmet, liquid_steeleaf, 5, ItemMold.MOLD_HELMET, null);
      RegisterCasting(steeleaf_chestplate, liquid_steeleaf, 8, ItemMold.MOLD_CHESTPLATE, null);
      RegisterCasting(steeleaf_leggings, liquid_steeleaf, 7, ItemMold.MOLD_LEGGINGS, null);
      RegisterCasting(steeleaf_boots, liquid_steeleaf, 4, ItemMold.MOLD_BOOTS, null);
   
      RegisterCasting(knightmetal_axe, liquid_knightmetal, 3, ItemMold.MOLD_AXE, extra_sticks2);
      RegisterCasting(knightmetal_sword, liquid_knightmetal, 2, ItemMold.MOLD_SWORD, extra_sticks1);
      RegisterCasting(knightmetal_pickaxe, liquid_knightmetal, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
      RegisterCasting(knightmetal_shovel, liquid_knightmetal, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
      RegisterCasting(knightmetal_hoe, liquid_knightmetal, 2, ItemMold.MOLD_HOE, extra_sticks2);

      RegisterCasting(knightmetal_helmet, liquid_knightmetal, 5, ItemMold.MOLD_HELMET, null);
      RegisterCasting(knightmetal_chestplate, liquid_knightmetal, 8, ItemMold.MOLD_CHESTPLATE, null);
      RegisterCasting(knightmetal_leggings, liquid_knightmetal, 7, ItemMold.MOLD_LEGGINGS, null);
      RegisterCasting(knightmetal_boots, liquid_knightmetal, 4, ItemMold.MOLD_BOOTS, null);
      
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, ironwood_axe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, ironwood_sword);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, ironwood_pickaxe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, ironwood_shovel);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, ironwood_hoe);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, ironwood_helmet);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, ironwood_chestplate);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, ironwood_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, ironwood_boots);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, steeleaf_axe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, steeleaf_sword);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, steeleaf_pickaxe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, steeleaf_shovel);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, steeleaf_hoe);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, steeleaf_helmet);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, steeleaf_chestplate);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, steeleaf_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, steeleaf_boots);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, knightmetal_axe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, knightmetal_sword);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, knightmetal_pickaxe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, knightmetal_shovel);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, knightmetal_hoe);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, knightmetal_helmet);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, knightmetal_chestplate);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, knightmetal_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, knightmetal_boots);
    }
  }
}
