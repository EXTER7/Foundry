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
  static private int foundry_crucible_id;
  static private int metal_smelter_id;
  static private int metal_caster_id;
  static private int alloy_mixer_id;
  static private int metal_block_id;
  static private int ore_id;
  static private int metal_infuser_id;


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
    foundry_crucible_id = config.getBlock( "foundry_crucible", 3843).getInt();
    metal_smelter_id = config.getBlock("induction_crucible_furnace", 3829).getInt();
    metal_caster_id = config.getBlock( "metal_caster", 3832).getInt();
    alloy_mixer_id = config.getBlock( "alloy_mixer", 3836).getInt();
    metal_block_id = config.getBlock( "metal_block", 3846).getInt();
    ore_id = config.getBlock( "ore", 3844).getInt();
    metal_infuser_id = config.getBlock( "metal_infuser", 3847).getInt();
    
    block_foundry_crucible = new BlockFoundryCrucible(foundry_crucible_id);
    block_induction_crucible_furnace = new BlockInductionCrucibleFurnace(metal_smelter_id);
    block_metal_caster = new BlockMetalCaster(metal_caster_id);
    block_alloy_mixer = new BlockAlloyMixer(alloy_mixer_id);
    block_metal = new BlockMetal(metal_block_id);
    block_ore = new BlockFoundryOre(ore_id);
    block_metal_infuser = new BlockMetalInfuser(metal_infuser_id);

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
