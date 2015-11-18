package exter.foundry.integration;

import exter.foundry.integration.minetweaker.MTAlloyFurnaceHandler;
import exter.foundry.integration.minetweaker.MTAlloyMixerHandler;
import exter.foundry.integration.minetweaker.MTAtomizerHandler;
import exter.foundry.integration.minetweaker.MTCastingHandler;
import exter.foundry.integration.minetweaker.MTMeltingHandler;
import minetweaker.MineTweakerAPI;
import net.minecraftforge.common.config.Configuration;

public class ModIntegrationMinetweaker extends ModIntegration
{
  public ModIntegrationMinetweaker(String mod_name)
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
    MineTweakerAPI.registerClass(MTMeltingHandler.class);
    MineTweakerAPI.registerClass(MTCastingHandler.class);
    MineTweakerAPI.registerClass(MTAlloyMixerHandler.class);
    MineTweakerAPI.registerClass(MTAlloyFurnaceHandler.class);
    MineTweakerAPI.registerClass(MTAtomizerHandler.class);
  }

  @Override
  public void OnPostInit()
  {

  }
}
