package exter.foundry.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;

public class FoundryBlocks
{
  static private int foundry_crucible_id;
  static private int metal_smelter_id;
  static private int caster_id;
  static private int alloymixer_id;

  public static BlockFoundryCrucible block_foundry_crucible;
  public static BlockMetalSmelter block_metalsmelter;
  public static BlockMetalCaster block_caster;
  public static BlockAlloyMixer block_alloymixer;

  static public void RegisterBlocks(Configuration config)
  {
    foundry_crucible_id = config.getBlock( "foundrycrucible", 3843).getInt();
    metal_smelter_id = config.getBlock("metalsmelter", 3829).getInt();
    caster_id = config.getBlock( "metalcaster", 3832).getInt();
    alloymixer_id = config.getBlock( "alloymixer", 3836).getInt();
    
    block_foundry_crucible = (BlockFoundryCrucible) (new BlockFoundryCrucible(foundry_crucible_id)).setHardness(2.5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("foundryCrucible");
    block_metalsmelter = (BlockMetalSmelter) (new BlockMetalSmelter(metal_smelter_id)).setHardness(2.5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("metalSmelter");
    block_caster = (BlockMetalCaster) (new BlockMetalCaster(caster_id)).setHardness(2.5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("caster");
    block_alloymixer = (BlockAlloyMixer) (new BlockAlloyMixer(alloymixer_id)).setHardness(2.5F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("alloyMixer");

    
    GameRegistry.registerBlock(block_foundry_crucible, "foundryCrucible");
    GameRegistry.registerBlock(block_metalsmelter, "metalSmelter");
    GameRegistry.registerBlock(block_caster, "caster");
    GameRegistry.registerBlock(block_alloymixer, "alloyMixer");
    
    LanguageRegistry.addName(block_foundry_crucible, "Foundry Crucible");
    LanguageRegistry.addName(block_metalsmelter, "Metal Smelter");
    LanguageRegistry.addName(block_caster, "Metal Caster");
    LanguageRegistry.addName(block_alloymixer, "Alloy Mixer");

  }
}
