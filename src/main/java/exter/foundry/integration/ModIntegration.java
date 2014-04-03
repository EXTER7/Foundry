package exter.foundry.integration;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public abstract class ModIntegration
{
  static private Map<String,ModIntegration> integrations = new HashMap<String,ModIntegration>();
  
  public final String Name;
  
  protected boolean is_loaded;
  
  protected ItemStack[] items;
  
  protected ItemStack GetItemFromField(Class<?> clazz,String field_name)
  {
    try
    {
      Object obj = clazz.getField(field_name).get(null);
      if(obj == null)
      {
        return null;
      }
      if(obj instanceof Item)
      {
        return new ItemStack((Item)obj);
      } 
      if(obj instanceof ItemStack)
      {
        return ((ItemStack)obj).copy();
      }

      ModFoundry.log.info("[ModIntegration ("+ Name +")] field" + field_name + "is not Item or ItemStack");
    } catch(IllegalAccessException e)
    {
      ModFoundry.log.info("[ModIntegration ("+ Name +")] Cannot find item " + field_name);
    } catch(NoSuchFieldException e)
    {
      ModFoundry.log.info("[ModIntegration ("+ Name +")] Cannot find item " + field_name);
    }
    return null;
  }

  protected void VerifyItems()
  {
    int i;
    for(i = 0; i < items.length; i++)
    {
      if(items[i] == null)
      {
        ModFoundry.log.warning("Integration " + Name + ": item " + i + " is null.");
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
  
  static final public void RegisterIntegration(Configuration config,ModIntegration mod)
  {
    if(config.get("integration", "enable."+mod.Name, true).getBoolean(true))
    {
      integrations.put(mod.Name, mod);
    }
  }
  
  static final public void PreInit(Configuration config)
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
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
