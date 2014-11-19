package exter.foundry.integration;

//import ic2.api.item.Items;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import exter.foundry.material.MaterialRegistry;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;

public class ModIntegrationIC2 extends ModIntegration
{
  private void RegisterPlateMoldRecipe(ItemStack item,String oredict_name)
  {
    if(FoundryUtils.IsItemInOreDictionary(oredict_name, item))
    {
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PLATE_IC2_SOFT, oredict_name);
    } else
    {
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PLATE_IC2_SOFT, item);
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
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void OnClientPostInit()
  {
    MaterialRegistry.instance.RegisterTypeIcon("Casing", ItemStack.copyItemStack(IC2Items.getItem("casingcopper")));  
    MaterialRegistry.instance.RegisterTypeIcon("Cable", ItemStack.copyItemStack(IC2Items.getItem("copperCableItem")));
  }

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("IC2"))
    {
      is_loaded = false;
      return;
    }

    ItemStack bronze_pickaxe = ItemStack.copyItemStack(IC2Items.getItem("bronzePickaxe"));
    ItemStack bronze_axe = ItemStack.copyItemStack(IC2Items.getItem("bronzeAxe"));
    ItemStack bronze_shovel = ItemStack.copyItemStack(IC2Items.getItem("bronzeShovel"));
    ItemStack bronze_hoe = ItemStack.copyItemStack(IC2Items.getItem("bronzeHoe"));
    ItemStack bronze_sword = ItemStack.copyItemStack(IC2Items.getItem("bronzeSword"));
    ItemStack bronze_helmet = ItemStack.copyItemStack(IC2Items.getItem("bronzeHelmet"));
    ItemStack bronze_chestplate = ItemStack.copyItemStack(IC2Items.getItem("bronzeChestplate"));
    ItemStack bronze_leggings = ItemStack.copyItemStack(IC2Items.getItem("bronzeLeggings"));
    ItemStack bronze_boots = ItemStack.copyItemStack(IC2Items.getItem("bronzeBoots"));

    ItemStack copper_cable = ItemStack.copyItemStack(IC2Items.getItem("copperCableItem"));
    ItemStack tin_cable = ItemStack.copyItemStack(IC2Items.getItem("tinCableItem"));
    ItemStack gold_cable = ItemStack.copyItemStack(IC2Items.getItem("goldCableItem"));
    ItemStack iron_cable = ItemStack.copyItemStack(IC2Items.getItem("ironCableItem"));

    ItemStack copper_casing = ItemStack.copyItemStack(IC2Items.getItem("casingcopper"));
    ItemStack tin_casing = ItemStack.copyItemStack(IC2Items.getItem("casingtin"));
    ItemStack bronze_casing = ItemStack.copyItemStack(IC2Items.getItem("casingbronze"));
    ItemStack gold_casing = ItemStack.copyItemStack(IC2Items.getItem("casinggold"));
    ItemStack iron_casing = ItemStack.copyItemStack(IC2Items.getItem("casingiron"));
    ItemStack lead_casing = ItemStack.copyItemStack(IC2Items.getItem("casinglead"));
    ItemStack steel_casing = ItemStack.copyItemStack(IC2Items.getItem("casingadviron"));

    ItemStack copper_plate = ItemStack.copyItemStack(IC2Items.getItem("platecopper"));
    ItemStack tin_plate = ItemStack.copyItemStack(IC2Items.getItem("platetin"));
    ItemStack bronze_plate = ItemStack.copyItemStack(IC2Items.getItem("platebronze"));
    ItemStack gold_plate = ItemStack.copyItemStack(IC2Items.getItem("plategold"));
    ItemStack iron_plate = ItemStack.copyItemStack(IC2Items.getItem("plateiron"));
    ItemStack lead_plate = ItemStack.copyItemStack(IC2Items.getItem("platelead"));
    ItemStack steel_plate = ItemStack.copyItemStack(IC2Items.getItem("plateadviron"));
    
    ItemStack copper_cable_insulated = ItemStack.copyItemStack(IC2Items.getItem("insulatedCopperCableItem"));
    ItemStack tin_cable_insulated = ItemStack.copyItemStack(IC2Items.getItem("insulatedTinCableItem"));
    ItemStack gold_cable_insulated = ItemStack.copyItemStack(IC2Items.getItem("insulatedGoldCableItem"));
    ItemStack iron_cable_insulated = ItemStack.copyItemStack(IC2Items.getItem("insulatedIronCableItem"));
    ItemStack resin = ItemStack.copyItemStack(IC2Items.getItem("resin"));

    if(is_loaded)
    {
      Fluid liquid_bronze = LiquidMetalRegistry.instance.GetFluid("Bronze");
      Fluid liquid_copper = LiquidMetalRegistry.instance.GetFluid("Copper");
      Fluid liquid_tin = LiquidMetalRegistry.instance.GetFluid("Tin");
      Fluid liquid_gold = LiquidMetalRegistry.instance.GetFluid("Gold");
      Fluid liquid_iron = LiquidMetalRegistry.instance.GetFluid("Iron");
      Fluid liquid_lead = LiquidMetalRegistry.instance.GetFluid("Lead");
      Fluid liquid_steel = LiquidMetalRegistry.instance.GetFluid("Steel");
      Fluid liquid_rubber = LiquidMetalRegistry.instance.GetFluid("Rubber");

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
        
        CastingRecipeManager.instance.AddRecipe(bronze_chestplate, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
        CastingRecipeManager.instance.AddRecipe(bronze_pickaxe, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(bronze_axe, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(bronze_shovel, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(bronze_sword, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
        CastingRecipeManager.instance.AddRecipe(bronze_hoe, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
        CastingRecipeManager.instance.AddRecipe(bronze_leggings, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
        CastingRecipeManager.instance.AddRecipe(bronze_helmet, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
        CastingRecipeManager.instance.AddRecipe(bronze_boots, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);

        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, bronze_chestplate);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, bronze_leggings);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, bronze_helmet);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, bronze_boots);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, bronze_pickaxe);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, bronze_axe);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, bronze_shovel);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, bronze_hoe);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, bronze_sword);
      }
      ItemStack mold_cable = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_CABLE_IC2);
      ItemStack mold_casing = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_CASING_IC2);
      ItemStack mold_plate = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_PLATE_IC2);
      ItemStack mold_insulated_cable = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_INSULATED_CABLE_IC2);

      MeltingRecipeManager.instance.AddRecipe(copper_cable, new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT / 3));
      MeltingRecipeManager.instance.AddRecipe(tin_cable, new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT / 3));
      MeltingRecipeManager.instance.AddRecipe(gold_cable, new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT / 4));
      MeltingRecipeManager.instance.AddRecipe(iron_cable, new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT / 4));
      
      MeltingRecipeManager.instance.AddRecipe(copper_cable_insulated, new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT / 3));
      MeltingRecipeManager.instance.AddRecipe(tin_cable_insulated, new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT / 3));
      MeltingRecipeManager.instance.AddRecipe(gold_cable_insulated, new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT / 4));
      MeltingRecipeManager.instance.AddRecipe(iron_cable_insulated, new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT / 4));
      
      MeltingRecipeManager.instance.AddRecipe("itemRubber", new FluidStack(liquid_rubber,FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(resin, new FluidStack(liquid_rubber,FoundryAPI.FLUID_AMOUNT_INGOT * 2),640);
      
      
      MeltingRecipeManager.instance.AddRecipe(copper_casing, new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
      MeltingRecipeManager.instance.AddRecipe(tin_casing, new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
      MeltingRecipeManager.instance.AddRecipe(bronze_casing, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
      MeltingRecipeManager.instance.AddRecipe(gold_casing, new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
      MeltingRecipeManager.instance.AddRecipe(iron_casing, new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
      MeltingRecipeManager.instance.AddRecipe(lead_casing, new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
      MeltingRecipeManager.instance.AddRecipe(steel_casing, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT / 2));

      
      MeltingRecipeManager.instance.AddRecipe(copper_plate, new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(tin_plate, new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(bronze_plate, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(gold_plate, new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(iron_plate, new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(lead_plate, new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe(steel_plate, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT));


      CastingRecipeManager.instance.AddRecipe(copper_cable, new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT / 3), mold_cable, null);
      CastingRecipeManager.instance.AddRecipe(tin_cable, new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT / 3), mold_cable, null);
      CastingRecipeManager.instance.AddRecipe(gold_cable, new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT / 4), mold_cable, null);
      CastingRecipeManager.instance.AddRecipe(iron_cable, new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT / 4), mold_cable, null);

      CastingRecipeManager.instance.AddRecipe(copper_cable_insulated, new FluidStack(liquid_rubber, FoundryAPI.FLUID_AMOUNT_INGOT), mold_insulated_cable, copper_cable);
      CastingRecipeManager.instance.AddRecipe(tin_cable_insulated, new FluidStack(liquid_rubber, FoundryAPI.FLUID_AMOUNT_INGOT), mold_insulated_cable, tin_cable);
      CastingRecipeManager.instance.AddRecipe(gold_cable_insulated, new FluidStack(liquid_rubber, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_insulated_cable, gold_cable);
      CastingRecipeManager.instance.AddRecipe(iron_cable_insulated, new FluidStack(liquid_rubber, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_insulated_cable, iron_cable);

      
      CastingRecipeManager.instance.AddRecipe(copper_casing, new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipeManager.instance.AddRecipe(tin_casing, new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipeManager.instance.AddRecipe(bronze_casing, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipeManager.instance.AddRecipe(gold_casing, new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipeManager.instance.AddRecipe(iron_casing, new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipeManager.instance.AddRecipe(lead_casing, new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_casing, null);
      CastingRecipeManager.instance.AddRecipe(steel_casing, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_casing, null);


      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CABLE_IC2_SOFT, copper_cable);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CABLE_IC2_SOFT, tin_cable);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CABLE_IC2_SOFT, gold_cable);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CABLE_IC2_SOFT, iron_cable);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INSULATED_CABLE_IC2_SOFT, copper_cable_insulated);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INSULATED_CABLE_IC2_SOFT, tin_cable_insulated);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INSULATED_CABLE_IC2_SOFT, gold_cable_insulated);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INSULATED_CABLE_IC2_SOFT, iron_cable_insulated);

      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_SOFT, copper_casing);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_SOFT, tin_casing);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_SOFT, bronze_casing);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_SOFT, gold_casing);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_SOFT, iron_casing);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_SOFT, lead_casing);
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CASING_IC2_SOFT, steel_casing);

      MaterialRegistry.instance.RegisterItem(copper_casing, "Copper", "Casing");
      MaterialRegistry.instance.RegisterItem(tin_casing, "Tin", "Casing");
      MaterialRegistry.instance.RegisterItem(bronze_casing, "Bronze", "Casing");
      MaterialRegistry.instance.RegisterItem(gold_casing, "Gold", "Casing");
      MaterialRegistry.instance.RegisterItem(iron_casing, "Iron", "Casing");
      MaterialRegistry.instance.RegisterItem(lead_casing, "Lead", "Casing");
      MaterialRegistry.instance.RegisterItem(steel_casing, "Steel", "Casing");

      MaterialRegistry.instance.RegisterItem(copper_cable, "Copper", "Cable");
      MaterialRegistry.instance.RegisterItem(tin_cable, "Tin", "Cable");
      MaterialRegistry.instance.RegisterItem(gold_cable, "Gold", "Cable");
      MaterialRegistry.instance.RegisterItem(iron_cable, "Iron", "Cable");

      ModIntegration gti = GetIntegration("gregtech");
      if(gti == null || !gti.is_loaded)
      {
        CastingRecipeManager.instance.AddRecipe(copper_plate, new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT), mold_plate, null);
        CastingRecipeManager.instance.AddRecipe(tin_plate, new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT), mold_plate, null);
        CastingRecipeManager.instance.AddRecipe(bronze_plate, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT), mold_plate, null);
        CastingRecipeManager.instance.AddRecipe(gold_plate, new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT), mold_plate, null);
        CastingRecipeManager.instance.AddRecipe(iron_plate, new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT), mold_plate, null);
        CastingRecipeManager.instance.AddRecipe(lead_plate, new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_INGOT), mold_plate, null);
        CastingRecipeManager.instance.AddRecipe(steel_plate, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT), mold_plate, null);

        RegisterPlateMoldRecipe(copper_plate, "plateCopper");
        RegisterPlateMoldRecipe(tin_plate, "plateTin");
        RegisterPlateMoldRecipe(bronze_plate, "plateBronze");
        RegisterPlateMoldRecipe(gold_plate, "plateGold");
        RegisterPlateMoldRecipe(iron_plate, "plateIron");
        RegisterPlateMoldRecipe(lead_plate, "plateLead");
        RegisterPlateMoldRecipe(steel_plate, "plateSteel");
      }
    }
  }
}
