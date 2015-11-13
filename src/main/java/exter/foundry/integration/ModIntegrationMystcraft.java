package exter.foundry.integration;

import java.util.List;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.event.FMLInterModComms;
import exter.foundry.registry.LiquidMetalRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;

public class ModIntegrationMystcraft extends ModIntegration
{
  
  static private List<String> ALLOWED_METALS = ImmutableList.of(
        "Copper",
        "Tin",
        "Iron",
        "Zinc",
        "Glass"
    );

  public ModIntegrationMystcraft(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    
  }

  @Override
  public void OnAfterPreInit()
  {
    for(String name:LiquidMetalRegistry.instance.GetFluidNames())
    {
      if(!ALLOWED_METALS.contains(name))
      {
        Fluid fluid = LiquidMetalRegistry.instance.GetFluid(name);
        FMLInterModComms.sendMessage("Mystcraft", "blacklistfluid", fluid.getName());    
      }
    }
  }

  @Override
  public void OnInit()
  {
    
  }

  @Override
  public void OnPostInit()
  {

  }
}
