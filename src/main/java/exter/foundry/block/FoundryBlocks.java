package exter.foundry.block;


import exter.foundry.item.ItemBlockMulti;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FoundryBlocks
{  
  static public BlockComponent block_component;
  static public BlockRefractoryGlass block_refractory_glass;
  static public BlockFoundryMachine block_machine;
  static public BlockCastingTable block_casting_table;
  
  static public BlockAlloyFurnace block_alloy_furnace;
  static public BlockMoldStation block_mold_station;
  static public BlockBurnerHeater block_burner_heater;
  
  static public BlockRefractoryHopper block_refractory_hopper;
  static public BlockRefractorySpout block_refractory_spout;
  static public BlockRefractoryTank block_refractory_tank;

  static public void register(Block block)
  {
    GameRegistry.register(block);
    GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));   
  }

  static private <T extends Block & IBlockVariants>void registerMulti(T block)
  {
    GameRegistry.register(block);
    GameRegistry.register(new ItemBlockMulti(block).setRegistryName(block.getRegistryName()));
  }

  static public void registerBlocks(Configuration config)
  {
    block_component = new BlockComponent();
    block_refractory_glass = new BlockRefractoryGlass();
    block_machine = new BlockFoundryMachine();
    block_casting_table = new BlockCastingTable();

    block_alloy_furnace = new BlockAlloyFurnace();
    block_mold_station = new BlockMoldStation();
    block_refractory_hopper = new BlockRefractoryHopper();
    block_burner_heater = new BlockBurnerHeater();
    block_refractory_spout = new BlockRefractorySpout();
    block_refractory_tank = new BlockRefractoryTank();
    
    registerMulti(block_component);
    registerMulti(block_machine);
    registerMulti(block_casting_table);
    register(block_refractory_glass);
    register(block_alloy_furnace);
    register(block_mold_station);
    register(block_refractory_hopper);
    register(block_burner_heater);
    register(block_refractory_spout);
    register(block_refractory_tank);
  }
}
