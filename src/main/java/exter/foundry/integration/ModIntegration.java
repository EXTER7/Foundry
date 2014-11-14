package exter.foundry.integration;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public abstract class ModIntegration
{
  static private Map<String,ModIntegration> integrations = new HashMap<String,ModIntegration>();
  
  public final String Name;
  
  protected boolean is_loaded;
  
  protected ItemStack[] items;
  
  protected void VerifyItems()
  {
    int i;
    for(i = 0; i < items.length; i++)
    {
      if(items[i] == null || items[i].getItem() == null)
      {
        ModFoundry.log.warn("Integration " + Name + ": item " + i + " is null.");
        is_loaded = false;
      }
    }
  }
  
  public ModIntegration(String mod_name)
  {
    Name = mod_name;
    is_loaded = true;
  }
  
  public abstract void OnPreInit(Configuration config);
  public abstract void OnInit();
  public abstract void OnPostInit();

  @SideOnly(Side.CLIENT)
  public void OnClientPreInit()
  {
    
  }
  
  @SideOnly(Side.CLIENT)
  public void OnClientInit()
  {
    
  }

  @SideOnly(Side.CLIENT)
  public void OnClientPostInit()
  {
    
  }

  public final boolean IsLoaded()
  {
    return is_loaded;
  }
  
  public final ItemStack GetItem(int index)
  {
    return items[index];
  }
  
  static final public ModIntegration GetIntegration(String name)
  {
    return integrations.get(name);
  }
  
  static final public void RegisterIntegration(Configuration config,Class<? extends ModIntegration> mod,String name)
  {
    try
    {
      ModIntegration integration = mod.getDeclaredConstructor(String.class).newInstance(name);
      if(config.get("integration", "enable."+integration.Name, true).getBoolean(true))
      {
        integrations.put(integration.Name, integration);
      }
    } catch(NoClassDefFoundError e)
    {
      ModFoundry.log.debug(e.getMessage());
    } catch(InstantiationException e)
    {
      e.printStackTrace();
    } catch(IllegalAccessException e)
    {
      e.printStackTrace();
    } catch(IllegalArgumentException e)
    {
      e.printStackTrace();
    } catch(InvocationTargetException e)
    {
      e.printStackTrace();
    } catch(NoSuchMethodException e)
    {
      e.printStackTrace();
    } catch(SecurityException e)
    {
      e.printStackTrace();
    }
  }
  
  static final public void PreInit(Configuration config)
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        ModFoundry.log.info("PreInit integration: " + m.Name);
        m.OnPreInit(config);
      }
    }
  }

  static final public void Init()
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        ModFoundry.log.info("Init integration: " + m.Name);
        m.OnInit();
      }
    }
  }

  static final public void PostInit()
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        ModFoundry.log.info("PostInit integration: " + m.Name);
        m.OnPostInit();
      }
    }
  }
  

  @SideOnly(Side.CLIENT)
  static final public void ClientPreInit()
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        m.OnClientPreInit();
      }
    }
  }

  

  @SideOnly(Side.CLIENT)
  static final public void ClientPostInit()
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        m.OnClientPostInit();
      }
    }
  }

  

  @SideOnly(Side.CLIENT)
  static final public void ClientInit()
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        m.OnClientInit();
      }
    }
  }

}
