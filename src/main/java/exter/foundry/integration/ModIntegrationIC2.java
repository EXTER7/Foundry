package exter.foundry.integration;

//import ic2.api.item.Items;
import ic2.api.item.IC2Items;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.util.FoundryMiscUtils;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;

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
  static public final int ITEM_IRON_CABLE = 12;

  static public final int ITEM_COPPER_CASING = 13;
  static public final int ITEM_TIN_CASING = 14;
  static public final int ITEM_BRONZE_CASING = 15;
  static public final int ITEM_GOLD_CASING = 16;
  static public final int ITEM_IRON_CASING = 17;
  static public final int ITEM_LEAD_CASING = 18;

  static public final int ITEM_COPPER_PLATE = 19;
  static public final int ITEM_TIN_PLATE = 20;
  static public final int ITEM_BRONZE_PLATE = 21;
  static public final int ITEM_GOLD_PLATE = 22;
  static public final int ITEM_IRON_PLATE = 23;
  static public final int ITEM_LEAD_PLATE = 24;

  private void RegisterPlateMoldRecipe(ItemStack item,String oredict_name)
  {
    if(FoundryUtils.IsItemInOreDictionary(oredict_name, item))
    {
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PLATE_IC2_CLAY, oredict_name);
    } else
    {
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PLATE_IC2_CLAY, item);
    }
  }
  
  public ModIntegrationIC2(String mod_name)
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
    items = new ItemStack[25];

    items[ITEM_BRONZE_PICKAXE] = ItemStack.copyItemStack(IC2Items.getItem("bronzePickaxe"));
    items[ITEM_BRONZE_AXE] = ItemStack.copyItemStack(IC2Items.getItem("bronzeAxe"));
    items[ITEM_BRONZE_SHOVEL] = ItemStack.copyItemStack(IC2Items.getItem("bronzeShovel"));
    items[ITEM_BRONZE_HOE] = ItemStack.copyItemStack(IC2Items.getItem("bronzeHoe"));
    items[ITEM_BRONZE_SWORD] = ItemStack.copyItemStack(IC2Items.getItem("bronzeSword"));
    items[ITEM_BRONZE_HELMET] = ItemStack.copyItemStack(IC2Items.getItem("bronzeHelmet"));
    items[ITEM_BRONZE_CHESTPLATE] = ItemStack.copyItemStack(IC2Items.getItem("bronzeChestplate"));
    items[ITEM_BRONZE_LEGGINGS] = ItemStack.copyItemStack(IC2Items.getItem("bronzeLeggings"));
    items[ITEM_BRONZE_BOOTS] = ItemStack.copyItemStack(IC2Items.getItem("bronzeBoots"));

    items[ITEM_COPPER_CABLE] = ItemStack.copyItemStack(IC2Items.getItem("copperCableItem"));
    items[ITEM_TIN_CABLE] = ItemStack.copyItemStack(IC2Items.getItem("tinCableItem"));
    items[ITEM_GOLD_CABLE] = ItemStack.copyItemStack(IC2Items.getItem("goldCableItem"));
    items[ITEM_IRON_CABLE] = ItemStack.copyItemStack(IC2Items.getItem("ironCableItem"));

    items[ITEM_COPPER_CASING] = ItemStack.copyItemStack(IC2Items.getItem("casingcopper"));
    items[ITEM_TIN_CASING] = ItemStack.copyItemStack(IC2Items.getItem("casingtin"));
    items[ITEM_BRONZE_CASING] = ItemStack.copyItemStack(IC2Items.getItem("casingbronze"));
    items[ITEM_GOLD_CASING] = ItemStack.copyItemStack(IC2Items.getItem("casinggold"));
    items[ITEM_IRON_CASING] = ItemStack.copyItemStack(IC2Items.getItem("casingiron"));
    items[ITEM_LEAD_CASING] = ItemStack.copyItemStack(IC2Items.getItem("casinglead"));

    items[ITEM_COPPER_PLATE] = ItemStack.copyItemStack(IC2Items.getItem("platecopper"));
    items[ITEM_TIN_PLATE] = ItemStack.copyItemStack(IC2Items.getItem("platetin"));
    items[ITEM_BRONZE_PLATE] = ItemStack.copyItemStack(IC2Items.getItem("platebronze"));
    items[ITEM_GOLD_PLATE] = ItemStack.copyItemStack(IC2Items.getItem("plategold"));
    items[ITEM_IRON_PLATE] = ItemStack.copyItemStack(IC2Items.getItem("plateiron"));
    items[ITEM_LEAD_PLATE] = ItemStack.copyItemStack(IC2Items.getItem("platelead"));

    VerifyItems();

    if(is_loaded)
    {
      Fluid liquid_bronze = LiquidMetalRegistry.instance.GetFluid("Bronze");
      Fluid liquid_copper = LiquidMetalRegistry.instance.GetFluid("Copper");
      Fluid liquid_tin = LiquidMetalRegistry.instance.GetFluid("Tin");
      Fluid liquid_gold = LiquidMetalRegistry.instance.GetFluid("Gold");
      Fluid liquid_iron = LiquidMetalRegistry.instance.GetFluid("Iron");
      Fluid liquid_lead = LiquidMetalRegistry.instance.GetFluid("Lead");

      if(FoundryConfig.recipe_tools_armor)
      {
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
        
        CastingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_CHESTPLATE], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_PICKAXE], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_AXE], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_SHOVEL], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_SWORD], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_HOE], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_LEGGINGS], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_HELMET], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_BOOTS], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);

        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_CLAY, items[ITEM_BRONZE_CHESTPLATE]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_CLAY, items[ITEM_BRONZE_LEGGINGS]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_CLAY, items[ITEM_BRONZE_HELMET]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_CLAY, items[ITEM_BRONZE_BOOTS]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_CLAY, items[ITEM_BRONZE_PICKAXE]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_CLAY, items[ITEM_BRONZE_AXE]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_CLAY, items[ITEM_BRONZE_SHOVEL]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_CLAY, items[ITEM_BRONZE_HOE]);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_CLAY, items[ITEM_BRONZE_SWORD]);
      }
      ItemStack mold_cable = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_CABLE_IC2);
      ItemStack mold_casing = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_CASING_IC2);
      ItemStack mold_plate = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_PLATE_IC2);

      MeltingRecipeManager.instance.AddRecipe(items[ITEM_COPPER_CABLE], new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT / 3));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_TIN_CABLE], new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT / 3));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_GOLD_CABLE], new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT / 4));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_IRON_CABLE], new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT / 4));

      MeltingRecipeManager.instance.AddRecipe(items[ITEM_COPPER_CASING], new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_TIN_CASING], new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_CASING], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_GOLD_CASING], new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_IRON_CASING], new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_LEAD_CASING], new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_INGOT / 2));

      MeltingRecipeManager.instance.AddRecipe(items[ITEM_COPPER_PLATE], new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_TIN_PLATE], new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_PLATE], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_GOLD_PLATE], new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_IRON_PLATE], new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(items[ITEM_LEAD_PLATE], new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_INGOT));


      CastingRecipeManager.instance.AddRecipe(items[ITEM_COPPER_CABLE], new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT / 3), mold_cable, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_TIN_CABLE], new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT / 3), mold_cable, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_GOLD_CABLE], new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT / 4), mold_cable, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_IRON_CABLE], new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT / 4), mold_cable, null);

      CastingRecipeManager.instance.AddRecipe(items[ITEM_COPPER_CASING], new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_TIN_CASING], new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_CASING], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_GOLD_CASING], new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_IRON_CASING], new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_LEAD_CASING], new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_casing, null);

      CastingRecipeManager.instance.AddRecipe(items[ITEM_COPPER_PLATE], new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT), mold_plate, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_TIN_PLATE], new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT), mold_plate, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_PLATE], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT), mold_plate, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_GOLD_PLATE], new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT), mold_plate, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_IRON_PLATE], new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT), mold_plate, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_LEAD_PLATE], new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_INGOT), mold_plate, null);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CABLE_IC2_CLAY, items[ITEM_COPPER_CABLE]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CABLE_IC2_CLAY, items[ITEM_TIN_CABLE]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CABLE_IC2_CLAY, items[ITEM_GOLD_CABLE]);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_CLAY, items[ITEM_COPPER_CASING]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_CLAY, items[ITEM_TIN_CASING]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_CLAY, items[ITEM_BRONZE_CASING]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_CLAY, items[ITEM_GOLD_CASING]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_CLAY, items[ITEM_IRON_CASING]);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_CLAY, items[ITEM_LEAD_CASING]);

      ModIntegration gti = GetIntegration("gregtech");
      if(gti == null || !gti.is_loaded)
      {
        RegisterPlateMoldRecipe(items[ITEM_COPPER_PLATE], "plateCopper");
        RegisterPlateMoldRecipe(items[ITEM_TIN_PLATE], "plateTin");
        RegisterPlateMoldRecipe(items[ITEM_BRONZE_PLATE], "plateBronze");
        RegisterPlateMoldRecipe(items[ITEM_GOLD_PLATE], "plateGold");
        RegisterPlateMoldRecipe(items[ITEM_IRON_PLATE], "plateIron");
        RegisterPlateMoldRecipe(items[ITEM_LEAD_PLATE], "plateLead");
      }
    }
  }

  @Override
  public void OnPostInit()
  {

  }
}
