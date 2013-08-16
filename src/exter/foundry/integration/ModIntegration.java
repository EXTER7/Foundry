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
  
  protected Map<String,ItemStack> items;
  
  protected void RegisterItem(Class clazz,String field_name)
  {
    try
    {
      Object obj = clazz.getField(field_name).get(null);
      if(obj instanceof Item)
      {
        items.put(field_name,new ItemStack((Item)obj));
      } else
      {
        ModFoundry.log.info("[ModIntegration ("+ Name +")] field" + field_name + "is not Item");
        is_loaded = false;
      }
    } catch(IllegalAccessException e)
    {
      is_loaded = false;
      ModFoundry.log.info("[ModIntegration ("+ Name +")] Cannot find item " + field_name);
      return;
    } catch(NoSuchFieldException e)
    {
      ModFoundry.log.info("[ModIntegration ("+ Name +")] Cannot find item " + field_name);
      is_loaded = false;
      return;
    }
  }

  
  public ModIntegration(String mod_name)
  {
    items = new HashMap<String,ItemStack>();
    Name = mod_name;
    is_loaded = true;
  }
  
  public final boolean IsLoaded()
  {
    return is_loaded;
  }
  
  public final ItemStack GetItem(String name)
  {
    return items.get(name);
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
