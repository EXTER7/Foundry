package exter.foundry.integration;

import exter.foundry.integration.minetweaker.MTAlloyFurnaceHandler;
import exter.foundry.integration.minetweaker.MTAlloyMixerHandler;
import exter.foundry.integration.minetweaker.MTAtomizerHandler;
import exter.foundry.integration.minetweaker.MTCastingHandler;
import exter.foundry.integration.minetweaker.MTInfuserHandler;
import exter.foundry.integration.minetweaker.MTMeltingHandler;
import exter.foundry.integration.minetweaker.substance.InfuserSubstanceBracketHandler;
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
    MineTweakerAPI.registerBracketHandler(new InfuserSubstanceBracketHandler());
    MineTweakerAPI.registerClass(MTMeltingHandler.class);
    MineTweakerAPI.registerClass(MTCastingHandler.class);
    MineTweakerAPI.registerClass(MTAlloyMixerHandler.class);
    MineTweakerAPI.registerClass(MTAlloyFurnaceHandler.class);
    MineTweakerAPI.registerClass(MTAtomizerHandler.class);
    MineTweakerAPI.registerClass(MTInfuserHandler.class);
  }

  @Override
  public void OnPostInit()
  {

  }
}
