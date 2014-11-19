package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;

public class ModIntegrationTF extends ModIntegration
{

  static public final int ITEM_INVAR_GEAR = 0;
  static public final int ITEM_ELECTRUM_GEAR = 1;

  static public final int ITEM_COPPER_GEAR = 2;
  static public final int ITEM_TIN_GEAR = 3;

  static public final int ITEM_ENDERIUM_INGOT = 4;
  static public final int ITEM_ENDERIUM_BLOCK = 5;
  static public final int ITEM_PYROTHEUM = 6;
  static public final int ITEM_BRONZE_GEAR = 7;
  static public final int ITEM_GOLD_GEAR = 8;
  static public final int ITEM_ENDERIUM_GEAR = 9;
  static public final int ITEM_SILVER_GEAR = 10;
  static public final int ITEM_LEAD_GEAR = 11;
  static public final int ITEM_MITHRIL_GEAR = 12;
  static public final int ITEM_SIGNALUM_GEAR = 13;
  static public final int ITEM_LUMIUM_GEAR = 14;
  static public final int ITEM_MITHRIL_INGOT = 15;
  static public final int ITEM_MITHRIL_BLOCK = 16;
  static public final int ITEM_SIGNALUM_INGOT = 17;
  static public final int ITEM_SIGNALUM_BLOCK = 18;
  static public final int ITEM_LUMIUM_INGOT = 19;
  static public final int ITEM_LUMIUM_BLOCK = 20;
  static public final int ITEM_IRON_GEAR = 21;
  static public final int ITEM_NICKEL_GEAR = 22;
  static public final int ITEM_PLATINUM_GEAR = 23;

  public boolean gear_recipes;
  
  public ModIntegrationTF(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    gear_recipes = config.get("integration", Name + ".gears", true).getBoolean(true);

    Fluid liquid_enderium = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Enderium", 1900, 12);
    FoundryUtils.RegisterBasicMeltingRecipes("Enderium", liquid_enderium);
    Fluid liquid_mithril = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Mithril", 1950, 12);
    FoundryUtils.RegisterBasicMeltingRecipes("Mithril", liquid_mithril);
    Fluid liquid_signalum = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Signalum", 1400, 12);
    FoundryUtils.RegisterBasicMeltingRecipes("Signalum", liquid_signalum);
    Fluid liquid_lumium = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Lumium", 2500, 15);
    FoundryUtils.RegisterBasicMeltingRecipes("Lumium", liquid_lumium);
  }

  @Override
  public void OnInit()
  {
  }

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("ThermalFoundation"))
    {
      is_loaded = false;
      return;
    }

    items = new ItemStack[24];

    items[ITEM_INVAR_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearInvar", 1);
    items[ITEM_ELECTRUM_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearElectrum", 1);

    items[ITEM_COPPER_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearCopper", 1);
    items[ITEM_TIN_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearTin", 1);

    items[ITEM_MITHRIL_INGOT] = GameRegistry.findItemStack("ThermalFoundation", "ingotMithril", 1);
    items[ITEM_MITHRIL_BLOCK] = GameRegistry.findItemStack("ThermalFoundation", "blockMithril", 1);

    items[ITEM_ENDERIUM_INGOT] = GameRegistry.findItemStack("ThermalFoundation", "ingotEnderium", 1);
    items[ITEM_ENDERIUM_BLOCK] = GameRegistry.findItemStack("ThermalFoundation", "blockEnderium", 1);

    items[ITEM_SIGNALUM_INGOT] = GameRegistry.findItemStack("ThermalFoundation", "ingotSignalum", 1);
    items[ITEM_SIGNALUM_BLOCK] = GameRegistry.findItemStack("ThermalFoundation", "blockSignalum", 1);

    items[ITEM_LUMIUM_INGOT] = GameRegistry.findItemStack("ThermalFoundation", "ingotLumium", 1);
    items[ITEM_LUMIUM_BLOCK] = GameRegistry.findItemStack("ThermalFoundation", "blockLumium", 1);

    items[ITEM_PYROTHEUM] = GameRegistry.findItemStack("ThermalFoundation", "dustPyrotheum", 1);

    items[ITEM_BRONZE_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearBronze", 1);
    items[ITEM_GOLD_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearGold", 1);
    items[ITEM_ENDERIUM_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearEnderium", 1);
    items[ITEM_SILVER_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearSilver", 1);

    items[ITEM_LEAD_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearLead", 1);
    items[ITEM_MITHRIL_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearMithril", 1);
    items[ITEM_SIGNALUM_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearSignalum", 1);
    items[ITEM_LUMIUM_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearLumium", 1);
    items[ITEM_IRON_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearIron", 1);
    items[ITEM_NICKEL_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearNickel", 1);
    items[ITEM_PLATINUM_GEAR] = GameRegistry.findItemStack("ThermalFoundation", "gearPlatinum", 1);

    VerifyItems();

    if(is_loaded)
    {
      Fluid liquid_copper = LiquidMetalRegistry.instance.GetFluid("Copper");
      Fluid liquid_tin = LiquidMetalRegistry.instance.GetFluid("Tin");
      Fluid liquid_platinum = LiquidMetalRegistry.instance.GetFluid("Platinum");
      Fluid liquid_iron = LiquidMetalRegistry.instance.GetFluid("Iron");
      Fluid liquid_nickel = LiquidMetalRegistry.instance.GetFluid("Nickel");
      Fluid liquid_silver = LiquidMetalRegistry.instance.GetFluid("Silver");
      Fluid liquid_lead = LiquidMetalRegistry.instance.GetFluid("Lead");
      Fluid liquid_invar = LiquidMetalRegistry.instance.GetFluid("Invar");
      Fluid liquid_redstone = FluidRegistry.getFluid("redstone");
      Fluid liquid_ender = FluidRegistry.getFluid("ender");
      Fluid liquid_glowstone = FluidRegistry.getFluid("glowstone");
      Fluid liquid_coal = FluidRegistry.getFluid("coal");
      Fluid liquid_pyrotheum = FluidRegistry.getFluid("pyrotheum");

      Fluid liquid_mithril = LiquidMetalRegistry.instance.GetFluid("Mithril");
      Fluid liquid_enderium = LiquidMetalRegistry.instance.GetFluid("Enderium");
      Fluid liquid_signalum = LiquidMetalRegistry.instance.GetFluid("Signalum");
      Fluid liquid_lumium = LiquidMetalRegistry.instance.GetFluid("Lumium");

      ItemStack mold_ingot = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_INGOT);
      ItemStack mold_block = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_BLOCK);

      MeltingRecipeManager.instance.AddRecipe("dustCoal", new FluidStack(liquid_coal, 100), 1000);

      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Items.redstone), new FluidStack(liquid_redstone, 100), 1000);
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.redstone_block), new FluidStack(liquid_redstone, 900), 1000);

      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Items.ender_pearl), new FluidStack(liquid_ender, 250), 1500);

      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Items.glowstone_dust), new FluidStack(liquid_glowstone, 250), 2500);
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.glowstone), new FluidStack(liquid_glowstone, 1000), 2500);

      MeltingRecipeManager.instance.AddRecipe(items[ITEM_PYROTHEUM], new FluidStack(liquid_pyrotheum, 250), 2500);

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_enderium, 108),
          new FluidStack[] {
            new FluidStack(liquid_tin, 54),
            new FluidStack(liquid_silver, 27),
            new FluidStack(liquid_platinum, 27),
            new FluidStack(liquid_ender, 250)
            });

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_signalum, 108),
          new FluidStack[] {
            new FluidStack(liquid_copper, 81),
            new FluidStack(liquid_silver, 27),
            new FluidStack(liquid_redstone, 250)
            });

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_lumium, 108),
          new FluidStack[] {
            new FluidStack(liquid_tin, 81),
            new FluidStack(liquid_silver, 27),
            new FluidStack(liquid_glowstone, 250)
            });

      CastingRecipeManager.instance.AddRecipe(items[ITEM_MITHRIL_INGOT], new FluidStack(liquid_mithril, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_MITHRIL_BLOCK], new FluidStack(liquid_mithril, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

      CastingRecipeManager.instance.AddRecipe(items[ITEM_ENDERIUM_INGOT], new FluidStack(liquid_enderium, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_ENDERIUM_BLOCK], new FluidStack(liquid_enderium, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

      CastingRecipeManager.instance.AddRecipe(items[ITEM_SIGNALUM_INGOT], new FluidStack(liquid_signalum, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_SIGNALUM_BLOCK], new FluidStack(liquid_signalum, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

      CastingRecipeManager.instance.AddRecipe(items[ITEM_LUMIUM_INGOT], new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_LUMIUM_BLOCK], new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

      if(!FoundryConfig.recipe_gear_useoredict && gear_recipes)
      {
        Fluid liquid_electrum = LiquidMetalRegistry.instance.GetFluid("Electrum");
        Fluid liquid_bronze = LiquidMetalRegistry.instance.GetFluid("Bronze");
        Fluid liquid_gold = LiquidMetalRegistry.instance.GetFluid("Gold");
        ItemStack mold_gear = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_GEAR);
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_COPPER_GEAR], new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_TIN_GEAR], new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_GEAR], new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_ELECTRUM_GEAR], new FluidStack(liquid_electrum, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_GEAR], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_GOLD_GEAR], new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_ENDERIUM_GEAR], new FluidStack(liquid_enderium, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_SILVER_GEAR], new FluidStack(liquid_silver, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_LEAD_GEAR], new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_MITHRIL_GEAR], new FluidStack(liquid_mithril, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_SIGNALUM_GEAR], new FluidStack(liquid_signalum, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_LUMIUM_GEAR], new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_IRON_GEAR], new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_NICKEL_GEAR], new FluidStack(liquid_nickel, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_PLATINUM_GEAR], new FluidStack(liquid_platinum, FoundryAPI.FLUID_AMOUNT_INGOT * 4));

        
        CastingRecipeManager.instance.AddRecipe(items[ITEM_COPPER_GEAR], new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_TIN_GEAR], new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_INVAR_GEAR], new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_ELECTRUM_GEAR], new FluidStack(liquid_electrum, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_BRONZE_GEAR], new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_GOLD_GEAR], new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_ENDERIUM_GEAR], new FluidStack(liquid_enderium, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_SILVER_GEAR], new FluidStack(liquid_silver, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_LEAD_GEAR], new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_MITHRIL_GEAR], new FluidStack(liquid_mithril, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_SIGNALUM_GEAR], new FluidStack(liquid_signalum, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_LUMIUM_GEAR], new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_IRON_GEAR], new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_NICKEL_GEAR], new FluidStack(liquid_nickel, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_PLATINUM_GEAR], new FluidStack(liquid_platinum, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
      }
    }
  }
}
