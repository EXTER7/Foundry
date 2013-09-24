package exter.foundry.integration;

import ic2.api.item.Items;
import net.minecraft.block.Block;
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

public class ModIntegrationIC2 extends ModIntegration
{
  static public final int ITEM_BRONZE_PICKAXE = 0;
  static public final int ITEM_BRONZE_AXE = 1;
  static public final int ITEM_BRONZE_SHOVEL = 2;
  static public final int ITEM_BRONZE_HOE = 3;
  static public final int ITEM_BRONZE_SWORD = 4;

  static public final int ITEM_BRONZE_HELMET = 5;
  static public final int ITEM_BRONZE_CHESTPLATE = 6;
  static public final int ITEM_BRONZE_LEGGINGS = 7;
  static public final int ITEM_BRONZE_BOOTS = 8;

  static public final int ITEM_COPPER_CABLE = 9;
  static public final int ITEM_TIN_CABLE = 10;
  static public final int ITEM_GOLD_CABLE = 11;

  static public final int ITEM_COPPER_CASING = 12;
  static public final int ITEM_TIN_CASING = 13;
  static public final int ITEM_BRONZE_CASING = 14;
  static public final int ITEM_GOLD_CASING = 15;
  static public final int ITEM_IRON_CASING = 16;
  static public final int ITEM_LEAD_CASING = 17;

  
  public ModIntegrationIC2(String mod_name)
  {
    super(mod_name);
    items = new ItemStack[18];

    items[ITEM_BRONZE_PICKAXE] = ItemStack.copyItemStack(Items.getItem("bronzePickaxe"));
    items[ITEM_BRONZE_AXE] = ItemStack.copyItemStack(Items.getItem("bronzeAxe"));
    items[ITEM_BRONZE_SHOVEL] = ItemStack.copyItemStack(Items.getItem("bronzeShovel"));
    items[ITEM_BRONZE_HOE] = ItemStack.copyItemStack(Items.getItem("bronzeHoe"));
    items[ITEM_BRONZE_SWORD] = ItemStack.copyItemStack(Items.getItem("bronzeSword"));
    items[ITEM_BRONZE_HELMET] = ItemStack.copyItemStack(Items.getItem("bronzeHelmet"));
    items[ITEM_BRONZE_CHESTPLATE] = ItemStack.copyItemStack(Items.getItem("bronzeChestplate"));
    items[ITEM_BRONZE_LEGGINGS] = ItemStack.copyItemStack(Items.getItem("bronzeLeggings"));
    items[ITEM_BRONZE_BOOTS] = ItemStack.copyItemStack(Items.getItem("bronzeBoots"));

    items[ITEM_COPPER_CABLE] = ItemStack.copyItemStack(Items.getItem("copperCableItem"));
    items[ITEM_TIN_CABLE] = ItemStack.copyItemStack(Items.getItem("tinCableItem"));
    items[ITEM_GOLD_CABLE] = ItemStack.copyItemStack(Items.getItem("goldCableItem"));

    items[ITEM_COPPER_CASING] = ItemStack.copyItemStack(Items.getItem("casingcopper"));
    items[ITEM_TIN_CASING] = ItemStack.copyItemStack(Items.getItem("casingtin"));
    items[ITEM_BRONZE_CASING] = ItemStack.copyItemStack(Items.getItem("casingbronze"));
    items[ITEM_GOLD_CASING] = ItemStack.copyItemStack(Items.getItem("casinggold"));
    items[ITEM_IRON_CASING] = ItemStack.copyItemStack(Items.getItem("casingiron"));
    items[ITEM_LEAD_CASING] = ItemStack.copyItemStack(Items.getItem("casinglead"));

    VerifyItems();

    if(is_loaded)
    {
      Fluid liquid_bronze = LiquidMetalRegistry.GetMetal("Bronze").fluid;
      Fluid liquid_copper = LiquidMetalRegistry.GetMetal("Copper").fluid;
      Fluid liquid_tin = LiquidMetalRegistry.GetMetal("Tin").fluid;
      Fluid liquid_gold = LiquidMetalRegistry.GetMetal("Gold").fluid;
      Fluid liquid_iron = LiquidMetalRegistry.GetMetal("Iron").fluid;
      Fluid liquid_lead = LiquidMetalRegistry.GetMetal("Lead").fluid;

      ItemStack extra_sticks1 = new ItemStack(Item.stick,1);
      ItemStack extra_sticks2 = new ItemStack(Item.stick,2);
      ItemStack mold_chestplate = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CHESTPLATE);
      ItemStack mold_pickaxe = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_PICKAXE);
      ItemStack mold_axe = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_AXE);
      ItemStack mold_shovel = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_SHOVEL);
      ItemStack mold_hoe = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_HOE);
      ItemStack mold_sword = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_SWORD);
      ItemStack mold_leggings = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_LEGGINGS);
      ItemStack mold_helmet = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_HELMET);
      ItemStack mold_boots = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_BOOTS);
      ItemStack mold_cable = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CABLE_IC2);
      ItemStack mold_casing = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CASING_IC2);

      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_CHESTPLATE], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 8), mold_chestplate, null);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_PICKAXE], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_AXE], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_SHOVEL], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_SWORD], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_HOE], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_LEGGINGS], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 7), mold_leggings, null);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_HELMET], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 5), mold_helmet, null);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_BOOTS], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 4), mold_boots, null);
      CastingRecipe.RegisterRecipe(items[ITEM_COPPER_CABLE], new FluidStack(liquid_copper,MeltingRecipe.AMOUNT_INGOT / 4), mold_cable, null);
      CastingRecipe.RegisterRecipe(items[ITEM_TIN_CABLE], new FluidStack(liquid_tin,MeltingRecipe.AMOUNT_INGOT / 3), mold_cable, null);
      CastingRecipe.RegisterRecipe(items[ITEM_GOLD_CABLE], new FluidStack(liquid_gold,MeltingRecipe.AMOUNT_INGOT / 4), mold_cable, null);

      CastingRecipe.RegisterRecipe(items[ITEM_COPPER_CASING], new FluidStack(liquid_copper,MeltingRecipe.AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipe.RegisterRecipe(items[ITEM_TIN_CASING], new FluidStack(liquid_tin,MeltingRecipe.AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_CASING], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipe.RegisterRecipe(items[ITEM_GOLD_CASING], new FluidStack(liquid_gold,MeltingRecipe.AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipe.RegisterRecipe(items[ITEM_IRON_CASING], new FluidStack(liquid_iron,MeltingRecipe.AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipe.RegisterRecipe(items[ITEM_LEAD_CASING], new FluidStack(liquid_lead,MeltingRecipe.AMOUNT_INGOT / 2), mold_casing, null);

      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, items[ITEM_BRONZE_CHESTPLATE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, items[ITEM_BRONZE_LEGGINGS]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, items[ITEM_BRONZE_HELMET]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, items[ITEM_BRONZE_BOOTS]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, items[ITEM_BRONZE_PICKAXE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, items[ITEM_BRONZE_AXE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, items[ITEM_BRONZE_SHOVEL]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, items[ITEM_BRONZE_HOE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, items[ITEM_BRONZE_SWORD]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CABLE_IC2_CLAY, items[ITEM_COPPER_CABLE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CABLE_IC2_CLAY, items[ITEM_TIN_CABLE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CABLE_IC2_CLAY, items[ITEM_GOLD_CABLE]);

      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_CLAY, items[ITEM_COPPER_CASING]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_CLAY, items[ITEM_TIN_CASING]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_CLAY, items[ITEM_BRONZE_CASING]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_CLAY, items[ITEM_GOLD_CASING]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_CLAY, items[ITEM_IRON_CASING]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_CLAY, items[ITEM_LEAD_CASING]);
    }
  }
}
