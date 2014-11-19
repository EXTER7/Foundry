package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.util.FoundryMiscUtils;
import exter.foundry.api.FoundryAPI;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;

public class ModIntegrationTE4 extends ModIntegration
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

  public ModIntegrationTE4(String mod_name)
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

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("ThermalExpansion") || !Loader.isModLoaded("ThermalFoundation"))
    {
      is_loaded = false;
      return;
    }
    
    ItemStack invar_pickaxe = GameRegistry.findItemStack("ThermalExpansion", "toolInvarPickaxe", 1);
    ItemStack invar_axe = GameRegistry.findItemStack("ThermalExpansion", "toolInvarAxe", 1);
    ItemStack invar_shovel = GameRegistry.findItemStack("ThermalExpansion", "toolInvarShovel", 1);
    ItemStack invar_hoe = GameRegistry.findItemStack("ThermalExpansion", "toolInvarHoe", 1);
    ItemStack invar_sword = GameRegistry.findItemStack("ThermalExpansion", "toolInvarSword", 1);

    ItemStack invar_helmet = GameRegistry.findItemStack("ThermalExpansion", "armorInvarHelmet", 1);
    ItemStack invar_chestplate = GameRegistry.findItemStack("ThermalExpansion", "armorInvarPlate", 1);
    ItemStack invar_leggings = GameRegistry.findItemStack("ThermalExpansion", "armorInvarLegs", 1);
    ItemStack invar_boots = GameRegistry.findItemStack("ThermalExpansion", "armorInvarBoots", 1);

    if(is_loaded && FoundryConfig.recipe_tools_armor)
    {
      Fluid liquid_invar = LiquidMetalRegistry.instance.GetFluid("Invar");

      ItemStack extra_sticks1 = new ItemStack(Items.stick, 1);
      ItemStack extra_sticks2 = new ItemStack(Items.stick, 2);
      ItemStack mold_chestplate = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_CHESTPLATE);
      ItemStack mold_pickaxe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_PICKAXE);
      ItemStack mold_axe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_AXE);
      ItemStack mold_shovel = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SHOVEL);
      ItemStack mold_hoe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_HOE);
      ItemStack mold_sword = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SWORD);
      ItemStack mold_leggings = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_LEGGINGS);
      ItemStack mold_helmet = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_HELMET);
      ItemStack mold_boots = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_BOOTS);

      CastingRecipeManager.instance.AddRecipe(invar_chestplate, new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
      CastingRecipeManager.instance.AddRecipe(invar_pickaxe, new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(invar_axe, new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(invar_shovel, new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(invar_sword, new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
      CastingRecipeManager.instance.AddRecipe(invar_hoe, new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(invar_leggings, new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
      CastingRecipeManager.instance.AddRecipe(invar_helmet, new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
      CastingRecipeManager.instance.AddRecipe(invar_boots, new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, invar_chestplate);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, invar_leggings);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, invar_helmet);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, invar_boots);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, invar_pickaxe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, invar_axe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, invar_shovel);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, invar_hoe);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, invar_sword);
    }
  }
}
