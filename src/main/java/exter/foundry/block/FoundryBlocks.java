package exter.foundry.block;

import exter.foundry.item.ItemBlockMulti;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FoundryBlocks
{
 
  
  public static BlockRefractoryCasing block_refractory_casing;
  public static BlockFoundryMachine block_machine;
  
  public static BlockAlloyFurnace block_alloy_furnace;
  
  public static BlockRefractoryHopper block_refractory_hopper;

  
  static public void registerBlocks(Configuration config)
  {
    block_refractory_casing = new BlockRefractoryCasing();
    block_machine = new BlockFoundryMachine();

    block_alloy_furnace = new BlockAlloyFurnace();
    block_refractory_hopper = new BlockRefractoryHopper();


    GameRegistry.registerBlock(block_refractory_casing, "casing");
    GameRegistry.registerBlock(block_machine, ItemBlockMulti.class, "machine");
    GameRegistry.registerBlock(block_alloy_furnace, "alloyFurnace");
    GameRegistry.registerBlock(block_refractory_hopper, "refractoryHopper");
  }
}
