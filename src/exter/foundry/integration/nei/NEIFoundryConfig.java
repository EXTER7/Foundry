package exter.foundry.integration.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import exter.foundry.ModFoundry;
import exter.foundry.block.FoundryBlocks;


public class NEIFoundryConfig implements IConfigureNEI
{

  @Override
  public void loadConfig()
  {
    API.hideItem(FoundryBlocks.block_slabdouble2.blockID);
    API.hideItem(FoundryBlocks.block_slabdouble1.blockID);
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
