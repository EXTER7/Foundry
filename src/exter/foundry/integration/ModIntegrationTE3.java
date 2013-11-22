package exter.foundry.integration;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.util.FoundryMiscUtils;
import exter.foundry.api.recipe.FoundryRecipes;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.CastingRecipe;
import exter.foundry.recipes.MeltingRecipe;
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


  public ModIntegrationTE3(String mod_name)
  {
    super(mod_name);
    items = new ItemStack[9];

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

    if(is_loaded)
    {
      Fluid liquid_invar = LiquidMetalRegistry.instance.GetFluid("Invar");
      Fluid liquid_redstone = FluidRegistry.getFluid("redstone");
      Fluid liquid_ender = FluidRegistry.getFluid("ender");

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

      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Item.redstone), new FluidStack(liquid_redstone,100),1000);
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Item.enderPearl), new FluidStack(liquid_ender,250),1500);
      
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
      
    }
  }
}
