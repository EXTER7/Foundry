package exter.foundry.integration;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.event.FMLInterModComms;
import exter.foundry.registry.LiquidMetalRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;

public class ModIntegrationRFTools extends ModIntegration
{
  private List<String> allowed_metals = new ArrayList<String>();
  
  public ModIntegrationRFTools(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    
  }

  private void AddLiquidMetalDimlet(String metal_name, int create, int maintain, int ticks, int rarity)
  {
    Fluid fluid = LiquidMetalRegistry.instance.GetFluid(metal_name);
    if(fluid == null)
    {
      return;
    }

    FMLInterModComms.sendMessage("rftools", "dimlet_configure",
        String.format("Liquid.%s=%d,%d,%d,%d",
            fluid.getName(),
            create, maintain, ticks, rarity));
    
    allowed_metals.add(metal_name);
  }
  
  @Override
  public void OnAfterPreInit()
  {
    AddLiquidMetalDimlet("Glass", 2000, 2000, 250, 3);
    AddLiquidMetalDimlet("Copper", 7500, 5500, 500, 4);
    AddLiquidMetalDimlet("Tin", 9000, 7000, 500, 4);
    AddLiquidMetalDimlet("Iron", 10000, 8500, 1000, 5);
    AddLiquidMetalDimlet("Zinc", 10000, 8000, 1000, 4);
    
    for(String name:LiquidMetalRegistry.instance.GetFluidNames())
    {
      if(!allowed_metals.contains(name))
      {
        Fluid fluid = LiquidMetalRegistry.instance.GetFluid(name);
        FMLInterModComms.sendMessage("rftools", "dimlet_blacklist", "Liquid." + fluid.getName());    
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
