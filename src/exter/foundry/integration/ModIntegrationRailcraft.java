package exter.foundry.integration;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.FoundryUtils;
import exter.foundry.LiquidMetalRegistry;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.CastingRecipe;
import exter.foundry.recipes.MeltingRecipe;

public class ModIntegrationRailcraft extends ModIntegration
{
  static public final int ITEM_STEEL_PICKAXE = 0;
  static public final int ITEM_STEEL_AXE = 1;
  static public final int ITEM_STEEL_SHOVEL = 2;
  static public final int ITEM_STEEL_HOE = 3;
  static public final int ITEM_STEEL_SWORD = 4;

  static public final int ITEM_STEEL_HELMET = 5;
  static public final int ITEM_STEEL_CHESTPLATE = 6;
  static public final int ITEM_STEEL_LEGGINGS = 7;
  static public final int ITEM_STEEL_BOOTS = 8;

  static public final int ITEM_IRON_GEAR = 9;
  static public final int ITEM_STEEL_GEAR = 10;

  public ModIntegrationRailcraft(String mod_name)
  {
    super(mod_name);
    items = new ItemStack[11];

    items[ITEM_STEEL_PICKAXE] = GameRegistry.findItemStack("Railcraft", "tool.steel.pickaxe", 1);
    items[ITEM_STEEL_AXE] = GameRegistry.findItemStack("Railcraft", "tool.steel.axe", 1);
    items[ITEM_STEEL_SHOVEL] = GameRegistry.findItemStack("Railcraft", "tool.steel.shovel", 1);
    items[ITEM_STEEL_HOE] = GameRegistry.findItemStack("Railcraft", "tool.steel.hoe", 1);
    items[ITEM_STEEL_SWORD] = GameRegistry.findItemStack("Railcraft", "tool.steel.sword", 1);

    items[ITEM_STEEL_HELMET] = GameRegistry.findItemStack("Railcraft", "armor.steel.helmet", 1);
    items[ITEM_STEEL_CHESTPLATE] = GameRegistry.findItemStack("Railcraft", "armor.steel.plate", 1);
    items[ITEM_STEEL_LEGGINGS] = GameRegistry.findItemStack("Railcraft", "armor.steel.legs", 1);
    items[ITEM_STEEL_BOOTS] = GameRegistry.findItemStack("Railcraft", "armor.steel.boots", 1);

    items[ITEM_IRON_GEAR] = GameRegistry.findItemStack("Railcraft", "part.gear.iron", 1);
    items[ITEM_STEEL_GEAR] = GameRegistry.findItemStack("Railcraft", "part.gear.steel", 1);

    VerifyItems();

    if(is_loaded)
    {
      Fluid liquid_steel = LiquidMetalRegistry.GetMetal("Steel").fluid;

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

      CastingRecipe.RegisterRecipe(items[ITEM_STEEL_CHESTPLATE], new FluidStack(liquid_steel, MeltingRecipe.AMOUNT_INGOT * 8), mold_chestplate, null);
      CastingRecipe.RegisterRecipe(items[ITEM_STEEL_PICKAXE], new FluidStack(liquid_steel, MeltingRecipe.AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
      CastingRecipe.RegisterRecipe(items[ITEM_STEEL_AXE], new FluidStack(liquid_steel, MeltingRecipe.AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
      CastingRecipe.RegisterRecipe(items[ITEM_STEEL_SHOVEL], new FluidStack(liquid_steel, MeltingRecipe.AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
      CastingRecipe.RegisterRecipe(items[ITEM_STEEL_SWORD], new FluidStack(liquid_steel, MeltingRecipe.AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
      CastingRecipe.RegisterRecipe(items[ITEM_STEEL_HOE], new FluidStack(liquid_steel, MeltingRecipe.AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
      CastingRecipe.RegisterRecipe(items[ITEM_STEEL_LEGGINGS], new FluidStack(liquid_steel, MeltingRecipe.AMOUNT_INGOT * 7), mold_leggings, null);
      CastingRecipe.RegisterRecipe(items[ITEM_STEEL_HELMET], new FluidStack(liquid_steel, MeltingRecipe.AMOUNT_INGOT * 5), mold_helmet, null);
      CastingRecipe.RegisterRecipe(items[ITEM_STEEL_BOOTS], new FluidStack(liquid_steel, MeltingRecipe.AMOUNT_INGOT * 4), mold_boots, null);

      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, items[ITEM_STEEL_CHESTPLATE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, items[ITEM_STEEL_LEGGINGS]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, items[ITEM_STEEL_HELMET]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, items[ITEM_STEEL_BOOTS]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, items[ITEM_STEEL_PICKAXE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, items[ITEM_STEEL_AXE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, items[ITEM_STEEL_SHOVEL]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, items[ITEM_STEEL_HOE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, items[ITEM_STEEL_SWORD]);
      
      FoundryUtils.RegisterInOreDictionary("gearIron",items[ITEM_IRON_GEAR]);
      FoundryUtils.RegisterInOreDictionary("gearSteel",items[ITEM_STEEL_GEAR]);
    }
  }
}
