package exter.foundry.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraftforge.fml.common.registry.GameRegistry;
import exter.foundry.api.registry.IFluidRegistry;
import exter.foundry.block.BlockLiquidMetal;
import exter.foundry.block.FoundryBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Utility class for registering a metal's corresponding block, items, fluid, and recipes.
 */
public class LiquidMetalRegistry implements IFluidRegistry
{
  private Map<String,Fluid> registry;
  
  static public LiquidMetalRegistry instance = new LiquidMetalRegistry();
  
  private LiquidMetalRegistry()
  {
    registry = new HashMap<String,Fluid>();
    MinecraftForge.EVENT_BUS.register(this);
  }

  /**
   * Helper method to register a metal's fluid, and block.
   * @param metal_name Name of the metal e.g: "Copper" for "oreCopper" in the Ore Dictionary.
   */
  public Fluid registerLiquidMetal(String metal_name,int temperature,int luminosity)
  {

    return registerLiquidMetal(metal_name,temperature,luminosity,"liquid" + metal_name,0xFFFFFF);
  }

  /**
   * Helper method to register a metal's fluid, and block.
   * @param metal_name Name of the metal e.g: "Copper" for "oreCopper" in the Ore Dictionary.
   */
  public Fluid registerLiquidMetal(String metal_name,int temperature,int luminosity,String texture,int color)
  {
    Fluid fluid = new ColoredFluid("liquid" + metal_name,
        new ResourceLocation("foundry","blocks/" + texture + "_still"),
        new ResourceLocation("foundry","blocks/" + texture + "_flow")).setColor(color).setTemperature(temperature).setLuminosity(luminosity).setDensity(2000);
    FluidRegistry.registerFluid(fluid);

    String block_name = "block" + metal_name;
    Object solid = FoundryBlocks.block_stacks.get(metal_name);
    if(solid == null)
    {
      solid = block_name;
    }
    Block liquid_block = new BlockLiquidMetal(fluid, "liquid" + metal_name, Material.lava,solid);
    GameRegistry.registerBlock(liquid_block, "liquid" + metal_name);

    fluid.setBlock(liquid_block);
    
    registry.put(metal_name, fluid);
    return fluid;
  }
 
  @Override
  public Fluid getFluid(String name)
  {
    return registry.get(name);
  }

  public Map<String,Fluid> getFluids()
  {
    return Collections.unmodifiableMap(registry);
  }

  public Set<String> getFluidNames()
  {
    return Collections.unmodifiableSet(registry.keySet());
  }
}
