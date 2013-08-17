package exter.foundry.integration;

import java.util.HashMap;
import java.util.Map;

import exter.foundry.ModFoundry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModIntegration
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
  
  public final boolean IsLoaded()
  {
    return is_loaded;
  }
  
  public final ItemStack GetItem(int index)
  {
    return items[index];
  }
  
  static public ModIntegration GetIntegration(String name)
  {
    return integrations.get(name);
  }
  
  static public void RegisterIntegration(ModIntegration mod)
  {
    if(mod.IsLoaded())
    {
      ModFoundry.log.info("Mod Integration ("+ mod.Name +") loaded successfully");
      integrations.put(mod.Name, mod);
    } else
    {
      ModFoundry.log.info("Mod Integration ("+ mod.Name +") failed");
    }
  }
  
}
