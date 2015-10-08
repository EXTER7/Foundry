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
  
  public abstract void OnPreInit(Configuration config);
  public abstract void OnInit();
  public abstract void OnPostInit();

  public void OnAfterPostInit()
  {
    
  }

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

  static final public void AfterPostInit()
  {
    for(ModIntegration m:integrations.values())
    {
      if(m.is_loaded)
      {
        ModFoundry.log.info("AfterPostInit integration: " + m.Name);
        m.OnAfterPostInit();
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
  
  static protected void RegisterCasting(ItemStack item,Fluid liquid_metal,int ingots,int mold_meta,ItemStack extra)
  {
    RegisterCasting(item,new FluidStack(liquid_metal, FoundryAPI.FLUID_AMOUNT_INGOT * ingots),mold_meta,extra);
  }

  static protected void RegisterCasting(ItemStack item,FluidStack fluid,int mold_meta,ItemStack extra)
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
  
  static protected void RegisterPlateMoldRecipe(ItemStack item,String oredict_name)
  {
    if(FoundryUtils.isItemInOreDictionary(oredict_name, item))
    {
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PLATE_SOFT, oredict_name);
    } else
    {
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PLATE_SOFT, item);
    }
  }
  
}
