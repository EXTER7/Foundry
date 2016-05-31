package exter.foundry.fluid;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import exter.foundry.api.registry.IFluidRegistry;
import exter.foundry.block.BlockLiquidMetal;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Utility class for registering a metal's corresponding block, items, fluid, and recipes.
 */
public class LiquidMetalRegistry implements IFluidRegistry
{
  private Map<String,FluidLiquidMetal> registry;
  
  static public LiquidMetalRegistry instance = new LiquidMetalRegistry();
  
  private LiquidMetalRegistry()
  {
    registry = new HashMap<String,FluidLiquidMetal>();
    MinecraftForge.EVENT_BUS.register(this);
  }

  /**
   * Helper method to register a metal's fluid, and block.
   * @param metal_name Name of the metal e.g: "Copper" for "oreCopper" in the Ore Dictionary.
   */
  public FluidLiquidMetal registerLiquidMetal(String metal_name,int temperature,int luminosity)
  {
    return registerLiquidMetal(metal_name,temperature,luminosity,"liquid" + metal_name,0xFFFFFF);
  }

  /**
   * Helper method to register a metal's fluid, and block.
   * @param metal_name Name of the metal e.g: "Copper" for "oreCopper" in the Ore Dictionary.
   */
  public FluidLiquidMetal registerLiquidMetal(String metal_name,int temperature,int luminosity,String texture,int color)
  {
    FluidLiquidMetal fluid = new FluidLiquidMetal("liquid" + metal_name,
        new ResourceLocation("foundry","blocks/" + texture + "_still"),
        new ResourceLocation("foundry","blocks/" + texture + "_flow"),
        color, false, temperature,luminosity);
    FluidRegistry.registerFluid(fluid);

    String block_name = "block" + metal_name;
    Object solid = FoundryMiscUtils.getModItemFromOreDictionary("substratum", block_name);
    if(solid == null)
    {
      solid = block_name;
    }

    Block liquid_block = new BlockLiquidMetal(fluid, "liquid" + metal_name, solid);
    FoundryBlocks.register(liquid_block);

    fluid.setBlock(liquid_block);
    
    registry.put(metal_name, fluid);
    return fluid;
  }

  public FluidLiquidMetal registerSpecialLiquidMetal(String metal_name,int temperature,int luminosity, ItemStack solid)
  {
    return registerSpecialLiquidMetal(metal_name,temperature,luminosity,"liquid" + metal_name,0xFFFFFF, solid);
  }

  public FluidLiquidMetal registerSpecialLiquidMetal(String metal_name,int temperature,int luminosity,String texture,int color, ItemStack solid)
  {
    FluidLiquidMetal fluid = new FluidLiquidMetal("liquid" + metal_name,
        new ResourceLocation("foundry","blocks/" + texture + "_still"),
        new ResourceLocation("foundry","blocks/" + texture + "_flow"),
        color, true, temperature, luminosity);
    FluidRegistry.registerFluid(fluid);

    Block liquid_block = new BlockLiquidMetal(fluid, "liquid" + metal_name, solid);
    FoundryBlocks.register(liquid_block);

    fluid.setBlock(liquid_block);
    
    registry.put(metal_name, fluid);
    return fluid;
  }

  @Override
  public FluidLiquidMetal getFluid(String name)
  {
    return registry.get(name);
  }

  public Map<String,FluidLiquidMetal> getFluids()
  {
    return Collections.unmodifiableMap(registry);
  }

  public Set<String> getFluidNames()
  {
    return Collections.unmodifiableSet(registry.keySet());
  }
}
