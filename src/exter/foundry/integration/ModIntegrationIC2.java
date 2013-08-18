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

  
  public ModIntegrationIC2(String mod_name)
  {
    super(mod_name);
    items = new ItemStack[9];

    items[ITEM_BRONZE_PICKAXE] = Items.getItem("bronzePickaxe").copy();
    items[ITEM_BRONZE_AXE] = Items.getItem("bronzeAxe").copy();
    items[ITEM_BRONZE_SHOVEL] = Items.getItem("bronzeShovel").copy();
    items[ITEM_BRONZE_HOE] = Items.getItem("bronzeHoe").copy();
    items[ITEM_BRONZE_SWORD] = Items.getItem("bronzeSword").copy();
    items[ITEM_BRONZE_HELMET] = Items.getItem("bronzeHelmet").copy();
    items[ITEM_BRONZE_CHESTPLATE] = Items.getItem("bronzeChestplate").copy();
    items[ITEM_BRONZE_LEGGINGS] = Items.getItem("bronzeLeggings").copy();
    items[ITEM_BRONZE_BOOTS] = Items.getItem("bronzeBoots").copy();
    VerifyItems();

    if(is_loaded)
    {
      Fluid liquid_bronze = LiquidMetalRegistry.GetMetal("Bronze").fluid;
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

      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_CHESTPLATE], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 8), mold_chestplate, null);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_PICKAXE], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_AXE], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_SHOVEL], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_SWORD], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_HOE], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_LEGGINGS], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 7), mold_leggings, null);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_HELMET], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 5), mold_helmet, null);
      CastingRecipe.RegisterRecipe(items[ITEM_BRONZE_BOOTS], new FluidStack(liquid_bronze,MeltingRecipe.AMOUNT_INGOT * 4), mold_boots, null);

      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, items[ITEM_BRONZE_CHESTPLATE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, items[ITEM_BRONZE_LEGGINGS]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, items[ITEM_BRONZE_HELMET]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, items[ITEM_BRONZE_BOOTS]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, items[ITEM_BRONZE_PICKAXE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, items[ITEM_BRONZE_AXE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, items[ITEM_BRONZE_SHOVEL]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, items[ITEM_BRONZE_HOE]);
      FoundryUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, items[ITEM_BRONZE_SWORD]);
    }
  }
}
