package exter.foundry.integration.nei;

import java.util.List;

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
    API.hideItem(FoundryBlocks.block_slabdouble1.blockID);
    API.hideItem(FoundryBlocks.block_slabdouble2.blockID);
    API.hideItem(FoundryBlocks.block_slabdouble3.blockID);

    List<TemplateRecipeHandler> handlers = ImmutableList.<TemplateRecipeHandler>of(
        new InductionCrucibleFurnaceRecipeHandler(),
        new AlloyRecipeHandler(),
        new MetalCasterRecipeHandler(),
        new InfuserRecipeHandler());
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
