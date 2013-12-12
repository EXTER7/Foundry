package exter.foundry.integration;

import java.util.HashMap;
import java.util.Map;

import exter.foundry.ModFoundry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public abstract class ModIntegration
{
  static private Map<String,ModIntegration> integrations = new HashMap<String,ModIntegration>();
  
  public final String Name;
  
  protected boolean is_loaded;
  
  protected ItemStack[] items;
  
  protected ItemStack GetItemFromField(Class clazz,String field_name)
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
    for(ItemStack is:items)
    {
      if(is == null)
      {
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
}
