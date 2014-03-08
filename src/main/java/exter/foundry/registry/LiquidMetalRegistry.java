package exter.foundry.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.registry.IFluidRegistry;
import exter.foundry.block.BlockLiquidMetal;
import exter.foundry.block.FoundryBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
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
   * Helper method to register a metal's fluid, block, melting, and casting.
   * @param config Forge Configuration file.
   * @param metal_name Name of the metal e.g: "Copper" for "oreCopper" in the Ore Dictionary.
   * @param default_container_id Default item id of the fluid container.
   */
  public Fluid RegisterLiquidMetal(Configuration config,String metal_name,int temperature,int luminosity)
  {

    Fluid fluid = new Fluid("liquid" + metal_name).setTemperature(temperature).setLuminosity(luminosity).setDensity(2000);
    FluidRegistry.registerFluid(fluid);

    String block_name = "block" + metal_name;
    Object solid = FoundryBlocks.block_stacks.get(metal_name);
    if(solid == null)
    {
      solid = block_name;
    }
    Block liquid_block = new BlockLiquidMetal(fluid, Material.lava,"liquid" + metal_name,solid);
    liquid_block.setBlockName("liquid" + metal_name);
    GameRegistry.registerBlock(liquid_block, "liquid" + metal_name);

    fluid.setBlock(liquid_block);

    
    registry.put(metal_name, fluid);
    return fluid;
  }
  
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void textureHook(TextureStitchEvent.Post event)
  {
    if(event.map.getTextureType() == 0)
    {
      for(Fluid fluid:registry.values())
      {
        fluid.setIcons(fluid.getBlock().getBlockTextureFromSide(1));
      }
    }
  }
 
  @Override
  public Fluid GetFluid(String name)
  {
    return registry.get(name);
  }
  
  public Set<String> GetFluidNames()
  {
    return Collections.unmodifiableSet(registry.keySet());
  }
}
