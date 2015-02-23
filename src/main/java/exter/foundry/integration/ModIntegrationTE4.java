package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import net.minecraftforge.common.config.Configuration;


public class ModIntegrationTE4 extends ModIntegration
{

  public ModIntegrationTE4(String mod_name)
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
    if(!Loader.isModLoaded("ThermalExpansion") || !Loader.isModLoaded("ThermalFoundation"))
    {
      is_loaded = false;
      return;
    }
  }
}
