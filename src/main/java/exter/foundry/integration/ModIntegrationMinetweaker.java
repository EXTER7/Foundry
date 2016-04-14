package exter.foundry.integration;

import exter.foundry.integration.minetweaker.MTAlloyFurnaceHandler;
import exter.foundry.integration.minetweaker.MTAlloyMixerHandler;
import exter.foundry.integration.minetweaker.MTAtomizerHandler;
import exter.foundry.integration.minetweaker.MTCastingHandler;
import exter.foundry.integration.minetweaker.MTInfuserHandler;
import exter.foundry.integration.minetweaker.MTMeltingHandler;
import exter.foundry.integration.minetweaker.orestack.OreStackBracketHandler;
import minetweaker.MineTweakerAPI;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(iface = "exter.foundry.integration.IModIntegration", modid = "MineTweaker3")
public class ModIntegrationMinetweaker implements IModIntegration
{
  
  @Optional.Method(modid = "MineTweaker3")
  @Override
  public void onPreInit(Configuration config)
  {
    
  }

  @Optional.Method(modid = "MineTweaker3")
  @Override
  public void onInit()
  {
    MineTweakerAPI.registerBracketHandler(new OreStackBracketHandler());
    MineTweakerAPI.registerClass(MTMeltingHandler.class);
    MineTweakerAPI.registerClass(MTCastingHandler.class);
    MineTweakerAPI.registerClass(MTAlloyMixerHandler.class);
    MineTweakerAPI.registerClass(MTAlloyFurnaceHandler.class);
    MineTweakerAPI.registerClass(MTAtomizerHandler.class);
    MineTweakerAPI.registerClass(MTInfuserHandler.class);
  }

  @Optional.Method(modid = "MineTweaker3")
  @Override
  public void onPostInit()
  {

  }

  @Optional.Method(modid = "MineTweaker3")
  @Override
  public String getName()
  {
    return "minetweaker";
  }

  @Optional.Method(modid = "MineTweaker3")
  @Override
  public void onAfterPostInit()
  {

  }

  @Optional.Method(modid = "MineTweaker3")
  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPreInit()
  {
    
  }

  @Optional.Method(modid = "MineTweaker3")
  @SideOnly(Side.CLIENT)
  @Override
  public void onClientInit()
  {
    
  }

  @Optional.Method(modid = "MineTweaker3")
  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPostInit()
  {
    
  }
}
