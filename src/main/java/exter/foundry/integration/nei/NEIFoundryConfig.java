package exter.foundry.integration.nei;

import java.util.List;

import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.google.common.collect.ImmutableList;

import exter.foundry.ModFoundry;
import exter.foundry.block.FoundryBlocks;

public class NEIFoundryConfig implements IConfigureNEI
{

  @Override
  public void loadConfig()
  {
    API.hideItem(new ItemStack(FoundryBlocks.block_slabdouble1));
    API.hideItem(new ItemStack(FoundryBlocks.block_slabdouble2));
    API.hideItem(new ItemStack(FoundryBlocks.block_slabdouble3));

    List<TemplateRecipeHandler> handlers = ImmutableList.<TemplateRecipeHandler>of(
        new InductionCrucibleFurnaceRecipeHandler(),
        new AlloyRecipeHandler(),
        new MetalCasterRecipeHandler(),
        new InfuserRecipeHandler(),
        new InfuserSubstanceRecipeHandler());
    
    for(TemplateRecipeHandler handler : handlers)
    {
      API.registerRecipeHandler(handler);
      API.registerUsageHandler(handler);
    }
  }

  @Override
  public String getName()
  {
    return ModFoundry.MODNAME;
  }

  @Override
  public String getVersion()
  {
    return ModFoundry.MODVERSION;
  }
}