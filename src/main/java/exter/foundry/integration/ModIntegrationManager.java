package exter.foundry.integration;

import java.util.HashMap;
import java.util.Map;

import exter.foundry.ModFoundry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

public final class ModIntegrationManager
{
  static private Map<String,IModIntegration> integrations = new HashMap<String,IModIntegration>();
  
  static public IModIntegration getIntegration(String name)
  {
    return integrations.get(name);
  }
  
  static public void registerIntegration(Configuration config,Object mod)
  {
    if(mod instanceof IModIntegration)
    {
      IModIntegration imod = (IModIntegration)mod;
      String name = imod.getName();
      if(config.get("integration", "enable." + name, true).getBoolean(true))
      {
        integrations.put(name, imod);
      }
    }
  }
  
  static public void preInit(Configuration config)
  {
    for(IModIntegration m:integrations.values())
    {
      ModFoundry.log.info("PreInit integration: " + m.getName());
      m.onPreInit(config);
    }
  }

  static public void init()
  {
    for(IModIntegration m:integrations.values())
    {
        ModFoundry.log.info("Init integration: " + m.getName());
        m.onInit();
    }
  }

  static public void postInit()
  {
    for(IModIntegration m:integrations.values())
    {
      ModFoundry.log.info("PostInit integration: " + m.getName());
      m.onPostInit();
    }
  }

  static public void afterPostInit()
  {
    for(IModIntegration m:integrations.values())
    {
      ModFoundry.log.info("AfterPostInit integration: " + m.getName());
      m.onAfterPostInit();
    }
  }


  @SideOnly(Side.CLIENT)
  static public void clientPreInit()
  {
    for(IModIntegration m:integrations.values())
    {
      m.onClientPreInit();
    }
  }

  @SideOnly(Side.CLIENT)
  static public void clientInit()
  {
    for(IModIntegration m:integrations.values())
    {
      m.onClientInit();
    }
  }

  @SideOnly(Side.CLIENT)
  static public void clientPostInit()
  {
    for(IModIntegration m:integrations.values())
    {
      m.onClientPostInit();
    }
  }

  
}
