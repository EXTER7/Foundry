package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.FoundryRecipes;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;


public class ModIntegrationRedstoneArsenal extends ModIntegration
{
  public ModIntegrationRedstoneArsenal(String mod_name)
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
    ModIntegration mod_tf = GetIntegration("tf");
    if(!Loader.isModLoaded("RedstoneArsenal") || mod_tf == null || !mod_tf.is_loaded)
    {
      is_loaded = false;
      return;
    }


    ItemStack electrumflux_ingot = GameRegistry.findItemStack("RedstoneArsenal", "ingotElectrumFlux", 1);
    ItemStack electrumflux_block = GameRegistry.findItemStack("RedstoneArsenal", "blockElectrumFlux", 1);

    if(is_loaded)
    {
      Fluid destabilized_redstone = FluidRegistry.getFluid("redstone");
      Fluid liquid_redstone = LiquidMetalRegistry.instance.getFluid("Redstone");
      Fluid liquid_electrumflux = LiquidMetalRegistry.instance.getFluid("ElectrumFlux");

      AlloyMixerRecipeManager.instance.addRecipe(
          new FluidStack(liquid_electrumflux, 27),
          new FluidStack[] {
            new FluidStack(FoundryRecipes.liquid_electrum, 27),
            new FluidStack(destabilized_redstone, 50) });

      AlloyMixerRecipeManager.instance.addRecipe(
          new FluidStack(liquid_electrumflux,5),
          new FluidStack[] {
            new FluidStack(FoundryRecipes.liquid_electrum,5),
            new FluidStack(liquid_redstone,4)});

      
      AlloyMixerRecipeManager.instance.addRecipe(
          new FluidStack(liquid_electrumflux, 54),
          new FluidStack[] {
            new FluidStack(FoundryRecipes.liquid_gold, 27),
            new FluidStack(FoundryRecipes.liquid_silver, 27),
            new FluidStack(destabilized_redstone, 100) });

      
      AlloyMixerRecipeManager.instance.addRecipe(
          new FluidStack(liquid_electrumflux, 10),
          new FluidStack[] {
            new FluidStack(FoundryRecipes.liquid_gold, 5),
            new FluidStack(FoundryRecipes.liquid_silver, 5),
            new FluidStack(liquid_redstone, 8) });
    
      ItemStack mold_ingot = FoundryItems.Mold(ItemMold.MOLD_INGOT);
      ItemStack mold_block = FoundryItems.Mold(ItemMold.MOLD_BLOCK);
      
      CastingRecipeManager.instance.addRecipe(electrumflux_ingot, new FluidStack(liquid_electrumflux, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.addRecipe(electrumflux_block, new FluidStack(liquid_electrumflux, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

      if(FoundryConfig.recipe_tools_armor)
      {
        ItemStack extra_sticks1 = GameRegistry.findItemStack("RedstoneArsenal", "rodObsidianFlux", 1);
        ItemStack extra_sticks2 = GameRegistry.findItemStack("RedstoneArsenal", "rodObsidianFlux", 2);
        if(extra_sticks1 != null && extra_sticks2 != null)
        {
          ItemStack pickaxe = GameRegistry.findItemStack("RedstoneArsenal", "toolFluxPickaxe", 1);
          ItemStack axe = GameRegistry.findItemStack("RedstoneArsenal", "toolFluxAxe", 1);
          ItemStack shovel = GameRegistry.findItemStack("RedstoneArsenal", "toolFluxShovel", 1);
          ItemStack hoe = GameRegistry.findItemStack("RedstoneArsenal", "toolFluxHoe", 1);
          ItemStack sword = GameRegistry.findItemStack("RedstoneArsenal", "toolFluxSword", 1);
          ItemStack helmet = GameRegistry.findItemStack("RedstoneArsenal", "armorFluxHelmet", 1);
          ItemStack chestplate = GameRegistry.findItemStack("RedstoneArsenal", "armorFluxPlate", 1);
          ItemStack leggings = GameRegistry.findItemStack("RedstoneArsenal", "armorFluxLegs", 1);
          ItemStack boots = GameRegistry.findItemStack("RedstoneArsenal", "armorFluxBoots", 1);
          ItemStack sickle = GameRegistry.findItemStack("RedstoneArsenal", "toolFluxSickle", 1);
          
          RegisterCasting(pickaxe, liquid_electrumflux, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
          RegisterCasting(axe, liquid_electrumflux, 3, ItemMold.MOLD_AXE, extra_sticks2);
          RegisterCasting(shovel, liquid_electrumflux, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
          RegisterCasting(sword, liquid_electrumflux, 2, ItemMold.MOLD_SWORD, extra_sticks1);
          RegisterCasting(hoe, liquid_electrumflux, 2, ItemMold.MOLD_HOE, extra_sticks2);
          RegisterCasting(sickle, liquid_electrumflux, 3, ItemMold.MOLD_SICKLE, extra_sticks1);
          
          ItemStack fluxplate = GameRegistry.findItemStack("RedstoneArsenal", "plateFlux", 1);
          ItemStack fluxgem2 = GameRegistry.findItemStack("RedstoneArsenal", "gemCrystalFlux", 2);

          if(fluxplate != null && fluxgem2 != null)
          {
            int plate_amount = FoundryAPI.FLUID_AMOUNT_INGOT + FoundryAPI.FLUID_AMOUNT_NUGGET * 6;
            CastingRecipeManager.instance.addRecipe(
                fluxplate,
                new FluidStack(liquid_electrumflux, plate_amount),
                FoundryItems.Mold(ItemMold.MOLD_FLUXPLATE),
                fluxgem2);
            FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_FLUXPLATE_SOFT, fluxplate);
            FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_FLUXPLATE_SOFT, ItemMold.MOLD_FLUXPLATE);

            RegisterCasting(leggings, new FluidStack(liquid_electrumflux, plate_amount * 7), ItemMold.MOLD_LEGGINGS, GameRegistry.findItemStack("RedstoneArsenal", "gemCrystalFlux", 14));
            RegisterCasting(chestplate, new FluidStack(liquid_electrumflux, plate_amount * 8), ItemMold.MOLD_CHESTPLATE, GameRegistry.findItemStack("RedstoneArsenal", "gemCrystalFlux", 16));
            RegisterCasting(helmet, new FluidStack(liquid_electrumflux, plate_amount * 5), ItemMold.MOLD_HELMET, GameRegistry.findItemStack("RedstoneArsenal", "gemCrystalFlux", 10));
            RegisterCasting(boots, new FluidStack(liquid_electrumflux, plate_amount * 4), ItemMold.MOLD_BOOTS, GameRegistry.findItemStack("RedstoneArsenal", "gemCrystalFlux", 8));
          }
        }
      }
    }
  }
}