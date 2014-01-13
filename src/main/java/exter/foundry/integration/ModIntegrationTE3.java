package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.util.FoundryMiscUtils;
import exter.foundry.api.recipe.FoundryRecipes;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;

public class ModIntegrationTE3 extends ModIntegration
{
  static public final int ITEM_INVAR_PICKAXE = 0;
  static public final int ITEM_INVAR_AXE = 1;
  static public final int ITEM_INVAR_SHOVEL = 2;
  static public final int ITEM_INVAR_HOE = 3;
  static public final int ITEM_INVAR_SWORD = 4;

  static public final int ITEM_INVAR_HELMET = 5;
  static public final int ITEM_INVAR_CHESTPLATE = 6;
  static public final int ITEM_INVAR_LEGGINGS = 7;
  static public final int ITEM_INVAR_BOOTS = 8;

  static public final int ITEM_INVAR_GEAR = 9;
  static public final int ITEM_ELECTRUM_GEAR = 10;

  static public final int ITEM_COPPER_GEAR = 11;
  static public final int ITEM_TIN_GEAR = 12;

  static public final int ITEM_ENDERIUM_INGOT = 13;
  static public final int ITEM_ENDERIUM_BLOCK = 14;
  static public final int ITEM_PYROTHEUM_BLOCK = 15;
  static public final int ITEM_BRONZE_GEAR = 16;
  

  public ModIntegrationTE3(String mod_name)
  {
    super(mod_name);
  }


  @Override
  public void OnPreInit(Configuration config)
  {
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Enderium", 1900, 12);
  }


  @Override
  public void OnInit()
  {
    if(!Loader.isModLoaded("ThermalExpansion"))
    {
      is_loaded = false;
      return;
    }

    items = new ItemStack[17];

    items[ITEM_INVAR_PICKAXE] = GameRegistry.findItemStack("ThermalExpansion", "toolInvarPickaxe", 1);
    items[ITEM_INVAR_AXE] = GameRegistry.findItemStack("ThermalExpansion", "toolInvarAxe", 1);
    items[ITEM_INVAR_SHOVEL] = GameRegistry.findItemStack("ThermalExpansion", "toolInvarShovel", 1);
    items[ITEM_INVAR_HOE] = GameRegistry.findItemStack("ThermalExpansion", "toolInvarHoe", 1);
    items[ITEM_INVAR_SWORD] = GameRegistry.findItemStack("ThermalExpansion", "toolInvarSword", 1);

    items[ITEM_INVAR_HELMET] = GameRegistry.findItemStack("ThermalExpansion", "armorInvarHelmet", 1);
    items[ITEM_INVAR_CHESTPLATE] = GameRegistry.findItemStack("ThermalExpansion", "armorInvarPlate", 1);
    items[ITEM_INVAR_LEGGINGS] = GameRegistry.findItemStack("ThermalExpansion", "armorInvarLegs", 1);
    items[ITEM_INVAR_BOOTS] = GameRegistry.findItemStack("ThermalExpansion", "armorInvarBoots", 1);

    items[ITEM_INVAR_GEAR] = GameRegistry.findItemStack("ThermalExpansion", "gearInvar", 1);
    items[ITEM_ELECTRUM_GEAR] = GameRegistry.findItemStack("ThermalExpansion", "gearElectrum", 1);

    items[ITEM_COPPER_GEAR] = GameRegistry.findItemStack("ThermalExpansion", "gearCopper", 1);
    items[ITEM_TIN_GEAR] = GameRegistry.findItemStack("ThermalExpansion", "gearTin", 1);

    items[ITEM_ENDERIUM_INGOT] = GameRegistry.findItemStack("ThermalExpansion", "ingotEnderium", 1);
    items[ITEM_ENDERIUM_BLOCK] = GameRegistry.findItemStack("ThermalExpansion", "blockEnderium", 1);

    items[ITEM_PYROTHEUM_BLOCK] = GameRegistry.findItemStack("ThermalExpansion", "dustPyrotheum", 1);

    items[ITEM_BRONZE_GEAR] = GameRegistry.findItemStack("ThermalExpansion", "gearBronze", 1);

    VerifyItems();

    if(is_loaded)
    {
      Fluid liquid_tin = LiquidMetalRegistry.instance.GetFluid("Tin");
      Fluid liquid_platinum = LiquidMetalRegistry.instance.GetFluid("Platinum");
      Fluid liquid_invar = LiquidMetalRegistry.instance.GetFluid("Invar");
      Fluid liquid_enderium = LiquidMetalRegistry.instance.GetFluid("Enderium");
      Fluid liquid_redstone = FluidRegistry.getFluid("redstone");
      Fluid liquid_ender = FluidRegistry.getFluid("ender");
      Fluid liquid_glowstone = FluidRegistry.getFluid("glowstone");
      Fluid liquid_coal = FluidRegistry.getFluid("coal");
      Fluid liquid_pyrotheum = FluidRegistry.getFluid("pyrotheum");
      
      ItemStack extra_sticks1 = new ItemStack(Item.stick, 1);
      ItemStack extra_sticks2 = new ItemStack(Item.stick, 2);
      ItemStack mold_chestplate = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_CHESTPLATE);
      ItemStack mold_pickaxe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_PICKAXE);
      ItemStack mold_axe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_AXE);
      ItemStack mold_shovel = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SHOVEL);
      ItemStack mold_hoe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_HOE);
      ItemStack mold_sword = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SWORD);
      ItemStack mold_leggings = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_LEGGINGS);
      ItemStack mold_helmet = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_HELMET);
      ItemStack mold_boots = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_BOOTS);
      ItemStack mold_ingot = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_INGOT);
      ItemStack mold_block = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_BLOCK);

      MeltingRecipeManager.instance.AddRecipe("dustCoal", new FluidStack(liquid_coal,100),1000);

      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Item.redstone), new FluidStack(liquid_redstone,100),1000);
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Block.blockRedstone), new FluidStack(liquid_redstone,900),1000);

      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Item.enderPearl), new FluidStack(liquid_ender,250),1500);

      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Item.glowstone), new FluidStack(liquid_glowstone,250),2500);
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Block.glowStone), new FluidStack(liquid_glowstone,1000),2500);

      MeltingRecipeManager.instance.AddRecipe(items[ITEM_PYROTHEUM_BLOCK], new FluidStack(liquid_pyrotheum,250),2500);

      AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_enderium,108),
          new FluidStack[] {
            new FluidStack(liquid_tin,81),
            new FluidStack(liquid_platinum,27),
            new FluidStack(liquid_ender,250)
      });
      
      CastingRecipeManager.instance.AddRecipe(items[ITEM_ENDERIUM_INGOT], new FluidStack(liquid_enderium, FoundryRecipes.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_ENDERIUM_BLOCK], new FluidStack(liquid_enderium, FoundryRecipes.FLUID_AMOUNT_BLOCK), mold_block, null);

      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_CHESTPLATE], new FluidStack(liquid_invar, FoundryRecipes.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_PICKAXE], new FluidStack(liquid_invar, FoundryRecipes.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_AXE], new FluidStack(liquid_invar, FoundryRecipes.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_SHOVEL], new FluidStack(liquid_invar, FoundryRecipes.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_SWORD], new FluidStack(liquid_invar, FoundryRecipes.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_HOE], new FluidStack(liquid_invar, FoundryRecipes.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_LEGGINGS], new FluidStack(liquid_invar, FoundryRecipes.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_HELMET], new FluidStack(liquid_invar, FoundryRecipes.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_BOOTS], new FluidStack(liquid_invar, FoundryRecipes.FLUID_AMOUNT_INGOT * 4), mold_boots, null);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, items[ITEM_INVAR_CHESTPLATE]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, items[ITEM_INVAR_LEGGINGS]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, items[ITEM_INVAR_HELMET]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, items[ITEM_INVAR_BOOTS]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, items[ITEM_INVAR_PICKAXE]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, items[ITEM_INVAR_AXE]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, items[ITEM_INVAR_SHOVEL]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, items[ITEM_INVAR_HOE]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, items[ITEM_INVAR_SWORD]);
      
      if(!FoundryConfig.recipe_gear_useoredict)
      {
        Fluid liquid_copper = LiquidMetalRegistry.instance.GetFluid("Copper");
        Fluid liquid_electrum = LiquidMetalRegistry.instance.GetFluid("Electrum");
        Fluid liquid_bronze = LiquidMetalRegistry.instance.GetFluid("Bronze");
        ItemStack mold_gear = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_GEAR);
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_COPPER_GEAR], new FluidStack(liquid_copper,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_TIN_GEAR], new FluidStack(liquid_tin,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_GEAR], new FluidStack(liquid_invar,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_ELECTRUM_GEAR], new FluidStack(liquid_electrum,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_GEAR], new FluidStack(liquid_bronze,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));

        CastingRecipeManager.instance.AddRecipe(items[ITEM_COPPER_GEAR], new FluidStack(liquid_copper,FoundryRecipes.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_TIN_GEAR], new FluidStack(liquid_tin,FoundryRecipes.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_GEAR], new FluidStack(liquid_invar,FoundryRecipes.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_ELECTRUM_GEAR], new FluidStack(liquid_electrum,FoundryRecipes.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_GEAR], new FluidStack(liquid_bronze,FoundryRecipes.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
      }
    }
  }


  @Override
  public void OnPostInit()
  {

  }
}
