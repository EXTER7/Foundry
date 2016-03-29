package exter.foundry.block;

import exter.foundry.item.ItemBlockMulti;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FoundryBlocks
{
 
  
  static public BlockComponent block_component;
  static public BlockFoundryMachine block_machine;
  
  static public BlockAlloyFurnace block_alloy_furnace;
  static public BlockMoldStation block_mold_station;
  
  static public BlockRefractoryHopper block_refractory_hopper;

  
  static public void registerBlocks(Configuration config)
  {
    block_component = new BlockComponent();
    block_machine = new BlockFoundryMachine();

    block_alloy_furnace = new BlockAlloyFurnace();
    block_mold_station = new BlockMoldStation();
    block_refractory_hopper = new BlockRefractoryHopper();
    


    GameRegistry.registerBlock(block_component, ItemBlockMulti.class, "componentBlock");
    GameRegistry.registerBlock(block_machine, ItemBlockMulti.class, "machine");
    GameRegistry.registerBlock(block_alloy_furnace, "alloyFurnace");
    GameRegistry.registerBlock(block_mold_station, "moldStation");
    GameRegistry.registerBlock(block_refractory_hopper, "refractoryHopper");
  }
}
