package exter.foundry.integration;

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
  }

  @Override
  public void OnPostInit()
  {

  }
}
