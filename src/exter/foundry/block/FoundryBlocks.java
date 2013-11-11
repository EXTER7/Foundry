package exter.foundry.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import exter.foundry.item.ItemBlockMulti;
import exter.foundry.item.ItemMold;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

public class FoundryBlocks
{

  //Default block id.
  static private int next_id = 3843;

  static public int GetNextID()
  {
    return next_id++;
  }
  

  public static BlockFoundryCrucible block_foundry_crucible;
  public static BlockInductionCrucibleFurnace block_induction_crucible_furnace;
  public static BlockMetalCaster block_metal_caster;
  public static BlockAlloyMixer block_alloy_mixer;
  public static BlockMetal block_metal;
  public static BlockFoundryOre block_ore;
  public static BlockMetalInfuser block_metal_infuser;

  static public void RegisterBlocks(Configuration config)
  {
    int i;
    
    block_foundry_crucible = new BlockFoundryCrucible(config.getBlock( "foundry_crucible", GetNextID()).getInt());
    block_induction_crucible_furnace = new BlockInductionCrucibleFurnace(config.getBlock("induction_crucible_furnace", GetNextID()).getInt());
    block_metal_caster = new BlockMetalCaster(config.getBlock( "metal_caster", GetNextID()).getInt());
    block_alloy_mixer = new BlockAlloyMixer(config.getBlock( "alloy_mixer", GetNextID()).getInt());
    block_metal = new BlockMetal(config.getBlock( "metal_block", GetNextID()).getInt());
    block_ore = new BlockFoundryOre(config.getBlock( "ore", GetNextID()).getInt());
    block_metal_infuser = new BlockMetalInfuser(config.getBlock( "metal_infuser", GetNextID()).getInt());

    MinecraftForge.setBlockHarvestLevel(block_ore, "pickaxe", 1);
    
    GameRegistry.registerBlock(block_foundry_crucible, "foundryCrucible");
    GameRegistry.registerBlock(block_induction_crucible_furnace, "inductionCrucibleFurnace");
    GameRegistry.registerBlock(block_metal_caster, "caster");
    GameRegistry.registerBlock(block_alloy_mixer, "alloyMixer");
    GameRegistry.registerBlock(block_metal, ItemBlockMulti.class, "blockFoundryMetal");
    GameRegistry.registerBlock(block_ore, ItemBlockMulti.class, "blockFoundryOre");
    GameRegistry.registerBlock(block_metal_infuser, "infuser");
    
    
    LanguageRegistry.addName(block_foundry_crucible, "Foundry Crucible");
    LanguageRegistry.addName(block_induction_crucible_furnace, "Induction Crucible Furnace");
    LanguageRegistry.addName(block_metal_caster, "Metal Caster");
    LanguageRegistry.addName(block_alloy_mixer, "Alloy Mixer");
    LanguageRegistry.addName(block_metal_infuser, "Metal Infuser");
    for(i = 0; i < BlockMetal.NAMES.length; i++)
    {
      ItemStack is = new ItemStack(block_metal,  1, i);
      LanguageRegistry.addName(is, BlockMetal.NAMES[i]);
      OreDictionary.registerOre(BlockMetal.OREDICT_NAMES[i], is);
    }
    for(i = 0; i < BlockFoundryOre.NAMES.length; i++)
    {
      ItemStack is = new ItemStack(block_ore,  1, i);
      LanguageRegistry.addName(is, BlockFoundryOre.NAMES[i]);
      OreDictionary.registerOre(BlockFoundryOre.OREDICT_NAMES[i], is);
    }
  }
}
