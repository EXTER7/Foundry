package exter.foundry.integration;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import exter.foundry.ModFoundry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

public abstract class ModIntegration
{
  static private Map<String,ModIntegration> integrations = new HashMap<String,ModIntegration>();
  
  public final String Name;
  
  protected boolean is_loaded;
  
  public ModIntegration(String mod_name)
  {
    Name = mod_name;
    is_loaded = true;
  }
  
  public abstract void onPreInit(Configuration config);
  public abstract void onInit();
  public abstract void onPostInit();

  public void pnAfterPostInit()
  {
    
  }

  @SideOnly(Side.CLIENT)
  public void onClientPreInit()
  {
    
  }
  
  @SideOnly(Side.CLIENT)
  public void onClientInit()
  {
    
  }

  @SideOnly(Side.CLIENT)
  public void OnClientPostInit()
  {
    
  }

  public final boolean isLoaded()
  {
    return is_loaded;
  }
  
  static final public ModIntegration getIntegration(String name)
  {
    return integrations.get(name);
  }
  
  static final public void registerIntegration(Configuration config,Class<? extends ModIntegration> mod,String name)
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
  
  static final public void preInit(Configuration config)
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        ModFoundry.log.info("PreInit integration: " + m.Name);
        m.onPreInit(config);
      }
    }
  }

  static final public void init()
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        ModFoundry.log.info("Init integration: " + m.Name);
        m.onInit();
      }
    }
  }

  static final public void postInit()
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        ModFoundry.log.info("PostInit integration: " + m.Name);
        m.onPostInit();
      }
    }
  }

  static final public void afterPostInit()
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        ModFoundry.log.info("AfterPostInit integration: " + m.Name);
        m.pnAfterPostInit();
      }
    }
  }


  @SideOnly(Side.CLIENT)
  static final public void clientPreInit()
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        m.onClientPreInit();
      }
    }
  }

  @SideOnly(Side.CLIENT)
  static final public void clientInit()
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        m.onClientInit();
      }
    }
  }

  @SideOnly(Side.CLIENT)
  static final public void clientPostInit()
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        m.OnClientPostInit();
      }
    }
  }

  
  static protected void registerCasting(ItemStack item,Fluid liquid_metal,int ingots,int mold_meta,ItemStack extra)
  {
    registerCasting(item,new FluidStack(liquid_metal, FoundryAPI.FLUID_AMOUNT_INGOT * ingots),mold_meta,extra);
  }

  static protected void registerCasting(ItemStack item,FluidStack fluid,int mold_meta,ItemStack extra)
  {
    if(item != null)
    {
      ItemStack mold = new ItemStack(FoundryItems.item_mold, 1, mold_meta);
      if(CastingRecipeManager.instance.findRecipe(new FluidStack(fluid.getFluid(),FoundryAPI.CASTER_TANK_CAPACITY), mold, extra) == null)
      {
        CastingRecipeManager.instance.addRecipe(item, fluid, mold, extra);
      }
    }
  }
  
  static protected void registerPlateMoldRecipe(ItemStack item,String oredict_name)
  {
    if(FoundryUtils.isItemInOreDictionary(oredict_name, item))
    {
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PLATE_SOFT, oredict_name);
    } else
    {
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PLATE_SOFT, item);
    }
  }
  
}
