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

    items = new ItemStack[20];

    items[ITEM_INVAR_PICKAXE] = GameRegistry.findItemStack("ThermalExpansion", "toolInvarPickaxe", 1);
    items[ITEM_INVAR_AXE] = GameRegistry.findItemStack("ThermalExpansion", "toolInvarAxe", 1);
    items[ITEM_INVAR_SHOVEL] = GameRegistry.findItemStack("ThermalExpansion", "toolInvarShovel", 1);
    items[ITEM_INVAR_HOE] = GameRegistry.findItemStack("ThermalExpansion", "toolInvarHoe", 1);
    items[ITEM_INVAR_SWORD] = GameRegistry.findItemStack("ThermalExpansion", "toolInvarSword", 1);

    items[ITEM_INVAR_HELMET] = GameRegistry.findItemStack("ThermalExpansion", "armorInvarHelmet", 1);
    items[ITEM_INVAR_CHESTPLATE] = GameRegistry.findItemStack("ThermalExpansion", "armorInvarPlate", 1);
    items[ITEM_INVAR_LEGGINGS] = GameRegistry.findItemStack("ThermalExpansion", "armorInvarLegs", 1);
    items[ITEM_INVAR_BOOTS] = GameRegistry.findItemStack("ThermalExpansion", "armorInvarBoots", 1);

    VerifyItems();

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

      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_CHESTPLATE], new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_PICKAXE], new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_AXE], new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_SHOVEL], new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_SWORD], new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_HOE], new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_LEGGINGS], new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_HELMET], new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_BOOTS], new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, items[ITEM_INVAR_CHESTPLATE]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, items[ITEM_INVAR_LEGGINGS]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, items[ITEM_INVAR_HELMET]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, items[ITEM_INVAR_BOOTS]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, items[ITEM_INVAR_PICKAXE]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, items[ITEM_INVAR_AXE]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, items[ITEM_INVAR_SHOVEL]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, items[ITEM_INVAR_HOE]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, items[ITEM_INVAR_SWORD]);
    }
  }
}
