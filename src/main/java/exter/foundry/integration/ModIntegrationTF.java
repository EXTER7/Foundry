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
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;

public class ModIntegrationTF extends ModIntegration
{
  public boolean gear_recipes;
  
  public ModIntegrationTF(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    gear_recipes = config.get("integration", Name + ".gears", true).getBoolean(true);
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

    ItemStack invar_gear = GameRegistry.findItemStack("ThermalFoundation", "gearInvar", 1);
    ItemStack electrum_gear = GameRegistry.findItemStack("ThermalFoundation", "gearElectrum", 1);

    ItemStack copper_gear = GameRegistry.findItemStack("ThermalFoundation", "gearCopper", 1);
    ItemStack tin_gear = GameRegistry.findItemStack("ThermalFoundation", "gearTin", 1);

    ItemStack mithril_ingot = GameRegistry.findItemStack("ThermalFoundation", "ingotMithril", 1);
    ItemStack mithril_block = GameRegistry.findItemStack("ThermalFoundation", "blockMithril", 1);

    ItemStack enderium_ingot = GameRegistry.findItemStack("ThermalFoundation", "ingotEnderium", 1);
    ItemStack enderium_block = GameRegistry.findItemStack("ThermalFoundation", "blockEnderium", 1);

    ItemStack signalum_ingot = GameRegistry.findItemStack("ThermalFoundation", "ingotSignalum", 1);
    ItemStack signalum_block = GameRegistry.findItemStack("ThermalFoundation", "blockSignalum", 1);

    ItemStack lumium_ingot = GameRegistry.findItemStack("ThermalFoundation", "ingotLumium", 1);
    ItemStack lumium_block = GameRegistry.findItemStack("ThermalFoundation", "blockLumium", 1);

    ItemStack pyrotheum = GameRegistry.findItemStack("ThermalFoundation", "dustPyrotheum", 1);

    ItemStack bronze_gear = GameRegistry.findItemStack("ThermalFoundation", "gearBronze", 1);
    ItemStack gold_gear = GameRegistry.findItemStack("ThermalFoundation", "gearGold", 1);
    ItemStack enderium_gear = GameRegistry.findItemStack("ThermalFoundation", "gearEnderium", 1);
    ItemStack silver_gear = GameRegistry.findItemStack("ThermalFoundation", "gearSilver", 1);

    ItemStack lead_gear = GameRegistry.findItemStack("ThermalFoundation", "gearLead", 1);
    ItemStack mithril_gear = GameRegistry.findItemStack("ThermalFoundation", "gearMithril", 1);
    ItemStack signalum_gear = GameRegistry.findItemStack("ThermalFoundation", "gearSignalum", 1);
    ItemStack lumium_gear = GameRegistry.findItemStack("ThermalFoundation", "gearLumium", 1);
    ItemStack iron_gear = GameRegistry.findItemStack("ThermalFoundation", "gearIron", 1);
    ItemStack nickel_gear = GameRegistry.findItemStack("ThermalFoundation", "gearNickel", 1);
    ItemStack platinum_gear = GameRegistry.findItemStack("ThermalFoundation", "gearPlatinum", 1);

  
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
      Fluid liquid_redstone = LiquidMetalRegistry.instance.GetFluid("Redstone");
      Fluid destabilized_redstone = FluidRegistry.getFluid("redstone");
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

      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Items.ender_pearl), new FluidStack(liquid_ender, 250), 1500);

      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Items.glowstone_dust), new FluidStack(liquid_glowstone, 250), 2500);
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.glowstone), new FluidStack(liquid_glowstone, 1000), 2500);

      MeltingRecipeManager.instance.AddRecipe(pyrotheum, new FluidStack(liquid_pyrotheum, 250), 2500);

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
            new FluidStack(destabilized_redstone, 250)
            });

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_signalum, 4),
          new FluidStack[] {
            new FluidStack(liquid_copper, 3),
            new FluidStack(liquid_silver, 4),
            new FluidStack(liquid_redstone, 4)
            });

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_lumium, 108),
          new FluidStack[] {
            new FluidStack(liquid_tin, 81),
            new FluidStack(liquid_silver, 27),
            new FluidStack(liquid_glowstone, 250)
            });

      CastingRecipeManager.instance.AddRecipe(mithril_ingot, new FluidStack(liquid_mithril, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(mithril_block, new FluidStack(liquid_mithril, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

      CastingRecipeManager.instance.AddRecipe(enderium_ingot, new FluidStack(liquid_enderium, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(enderium_block, new FluidStack(liquid_enderium, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

      CastingRecipeManager.instance.AddRecipe(signalum_ingot, new FluidStack(liquid_signalum, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(signalum_block, new FluidStack(liquid_signalum, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

      CastingRecipeManager.instance.AddRecipe(lumium_ingot, new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(lumium_block, new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

      if(!FoundryConfig.recipe_gear_useoredict && gear_recipes)
      {
        Fluid liquid_electrum = LiquidMetalRegistry.instance.GetFluid("Electrum");
        Fluid liquid_bronze = LiquidMetalRegistry.instance.GetFluid("Bronze");
        Fluid liquid_gold = LiquidMetalRegistry.instance.GetFluid("Gold");
        ItemStack mold_gear = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_GEAR);
        MeltingRecipeManager.instance.AddRecipe(copper_gear, new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(tin_gear, new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(invar_gear, new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(electrum_gear, new FluidStack(liquid_electrum, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(bronze_gear, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(gold_gear, new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(enderium_gear, new FluidStack(liquid_enderium, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(silver_gear, new FluidStack(liquid_silver, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(lead_gear, new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(mithril_gear, new FluidStack(liquid_mithril, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(signalum_gear, new FluidStack(liquid_signalum, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(lumium_gear, new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(iron_gear, new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(nickel_gear, new FluidStack(liquid_nickel, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(platinum_gear, new FluidStack(liquid_platinum, FoundryAPI.FLUID_AMOUNT_INGOT * 4));

        
        CastingRecipeManager.instance.AddRecipe(copper_gear, new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(tin_gear, new FluidStack(liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(invar_gear, new FluidStack(liquid_invar, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(electrum_gear, new FluidStack(liquid_electrum, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(bronze_gear, new FluidStack(liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(gold_gear, new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(enderium_gear, new FluidStack(liquid_enderium, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(silver_gear, new FluidStack(liquid_silver, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(lead_gear, new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(mithril_gear, new FluidStack(liquid_mithril, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(signalum_gear, new FluidStack(liquid_signalum, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(lumium_gear, new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(iron_gear, new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(nickel_gear, new FluidStack(liquid_nickel, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
        CastingRecipeManager.instance.AddRecipe(platinum_gear, new FluidStack(liquid_platinum, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
      }
    }
  }
}
