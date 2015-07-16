package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import exter.foundry.api.orestack.OreStack;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationProjectRed extends ModIntegration
{
  public ModIntegrationProjectRed(String mod_name)
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
    if(Loader.isModLoaded("ProjRed|Core"))
    {
      ItemStack redalloy = FoundryMiscUtils.GetModItemFromOreDictionary("ProjRed|Core", "ingotRedAlloy");

      Fluid liquid_iron = LiquidMetalRegistry.instance.GetFluid("Iron");
      Fluid liquid_redstone = LiquidMetalRegistry.instance.GetFluid("Redstone");
      Fluid liquid_redalloy = LiquidMetalRegistry.instance.GetFluid("RedAlloy");

      Fluid destabilized_redstone = FluidRegistry.getFluid("redstone");

      AlloyMixerRecipeManager.instance.AddRecipe(new FluidStack(liquid_redalloy,1),
          new FluidStack[] {
            new FluidStack(liquid_iron,1),
            new FluidStack(liquid_redstone,4)});

      if(destabilized_redstone != null)
      {
        AlloyMixerRecipeManager.instance.AddRecipe(new FluidStack(liquid_redalloy,1),
            new FluidStack[] {
              new FluidStack(liquid_iron,27),
              new FluidStack(destabilized_redstone,100)});
      }

      AlloyFurnaceRecipeManager.instance.AddRecipe(redalloy, 
          new Object[] {
              new OreStack("ingotIron",1),
              new OreStack("dustIron",1)},
          new Object[] {
              new OreStack("dustRedstone",4)});

      RegisterCasting(redalloy, liquid_redalloy, 1, ItemMold.MOLD_INGOT, null);
    }
  }
}
