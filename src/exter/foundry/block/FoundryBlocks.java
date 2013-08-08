package exter.foundry.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;

public class FoundryBlocks
{
  static private int foundry_crucible_id;
  static private int metal_smelter_id;
  static private int metal_caster_id;
  static private int alloy_mixer_id;

  public static BlockFoundryCrucible block_foundry_crucible;
  public static BlockInductionCrucibleFurnace block_induction_crucible_furnace;
  public static BlockMetalCaster block_metal_caster;
  public static BlockAlloyMixer block_alloy_mixer;

  static public void RegisterBlocks(Configuration config)
  {
    foundry_crucible_id = config.getBlock( "foundry_crucible", 3843).getInt();
    metal_smelter_id = config.getBlock("induction_crucible_furnace", 3829).getInt();
    metal_caster_id = config.getBlock( "metal_caster", 3832).getInt();
    alloy_mixer_id = config.getBlock( "alloy_mixer", 3836).getInt();
    
    block_foundry_crucible = (BlockFoundryCrucible) (new BlockFoundryCrucible(foundry_crucible_id)).setHardness(2.5F).setStepSound(Block.soundWoodFootstep);
    block_induction_crucible_furnace = (BlockInductionCrucibleFurnace) (new BlockInductionCrucibleFurnace(metal_smelter_id)).setHardness(2.5F).setStepSound(Block.soundWoodFootstep);
    block_metal_caster = (BlockMetalCaster) (new BlockMetalCaster(metal_caster_id)).setHardness(2.5F).setStepSound(Block.soundWoodFootstep);
    block_alloy_mixer = (BlockAlloyMixer) (new BlockAlloyMixer(alloy_mixer_id)).setHardness(2.5F).setStepSound(Block.soundWoodFootstep);

    
    GameRegistry.registerBlock(block_foundry_crucible, "foundryCrucible");
    GameRegistry.registerBlock(block_induction_crucible_furnace, "inductionCrucibleFurnace");
    GameRegistry.registerBlock(block_metal_caster, "caster");
    GameRegistry.registerBlock(block_alloy_mixer, "alloyMixer");
    
    LanguageRegistry.addName(block_foundry_crucible, "Foundry Crucible");
    LanguageRegistry.addName(block_induction_crucible_furnace, "Induction Crucible Furnace");
    LanguageRegistry.addName(block_metal_caster, "Metal Caster");
    LanguageRegistry.addName(block_alloy_mixer, "Alloy Mixer");

  }
}
