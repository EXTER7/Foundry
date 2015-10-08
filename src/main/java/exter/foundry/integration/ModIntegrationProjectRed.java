package exter.foundry.integration;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.orestack.OreStack;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.FoundryRecipes;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

      Fluid liquid_redstone = LiquidMetalRegistry.instance.getFluid("Redstone");
      Fluid liquid_redalloy = LiquidMetalRegistry.instance.getFluid("RedAlloy");

      Fluid destabilized_redstone = FluidRegistry.getFluid("redstone");
      

      AlloyMixerRecipeManager.instance.addRecipe(new FluidStack(liquid_redalloy,1),
          new FluidStack[] {
            new FluidStack(FoundryRecipes.liquid_iron,1),
            new FluidStack(liquid_redstone,4)});

      if(destabilized_redstone != null)
      {
        AlloyMixerRecipeManager.instance.addRecipe(new FluidStack(liquid_redalloy,1),
            new FluidStack[] {
              new FluidStack(FoundryRecipes.liquid_iron,27),
              new FluidStack(destabilized_redstone,100)});
      }

      AlloyFurnaceRecipeManager.instance.addRecipe(redalloy, 
          new Object[] {
              new OreStack("ingotIron",1),
              new OreStack("dustIron",1)},
          new Object[] {
              new OreStack("dustRedstone",4)});

      RegisterCasting(redalloy, liquid_redalloy, 1, ItemMold.MOLD_INGOT, null);
      
      if(Loader.isModLoaded("ProjRed|Transmission"))
      {
        Item redalloy_wire_item = GameRegistry.findItem("ProjRed|Transmission", "projectred.transmission.wire");
        ItemStack redalloy_wire = new ItemStack(redalloy_wire_item,1,0);
        FluidStack wire_fluid = new FluidStack(liquid_redalloy,FoundryAPI.FLUID_AMOUNT_INGOT / 4);
        FluidStack wire_fluid_bundled = new FluidStack(liquid_redalloy,FoundryAPI.FLUID_AMOUNT_INGOT / 4 * 5);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_WIRE_PR_SOFT, redalloy_wire);
        RegisterCasting(redalloy_wire, wire_fluid, ItemMold.MOLD_WIRE_PR, null);
        int i;
        for(i = 0; i < 17; i++)
        {
          MeltingRecipeManager.instance.addRecipe(new ItemStack(redalloy_wire_item,1,i), wire_fluid);
        }
        for(i = 17; i < 34; i++)
        {
          MeltingRecipeManager.instance.addRecipe(new ItemStack(redalloy_wire_item,1,i), wire_fluid_bundled);
        }
      }
    }
  }
}
