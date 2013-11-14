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
  
  public static final String[] SLAB1_METALS = 
  {
    "Iron",
    "Gold",
    "Copper",
    "Tin",
    "Bronze",
    "Electrum",
    "Invar",
    "Nickel",
  };
  
  public static final String[] SLAB2_METALS = 
  {
    "Zinc",
    "Brass",
    "Silver",
    "Steel",
    "Lead"
  };

  public static final String[] SLAB1_NAMES = 
  {
    "Iron Slab",
    "Gold Slab",
    "Copper Slab",
    "Tin Slab",
    "Bronze Slab",
    "Electrum Slab",
    "Invar Slab",
    "Nickel Slab"
  };

  public static final String[] SLAB2_NAMES = 
  {
    "Zinc Slab",
    "Brass Slab",
    "Silver Slab",
    "Steel Slab",
    "Lead Slab"
  };

  public static final String[] SLAB1_ICONS = 
  {
    "iron_block",
    "gold_block",
    "foundry:metalblock_copper",
    "foundry:metalblock_tin",
    "foundry:metalblock_bronze",
    "foundry:metalblock_electrum",
    "foundry:metalblock_invar",
    "foundry:metalblock_nickel"
  };

  public static final String[] SLAB2_ICONS = 
  {
    "foundry:metalblock_zinc",
    "foundry:metalblock_brass",
    "foundry:metalblock_silver",
    "foundry:metalblock_steel",
    "foundry:metalblock_lead"
  };

  public static BlockFoundryCrucible block_foundry_crucible;
  public static BlockInductionCrucibleFurnace block_induction_crucible_furnace;
  public static BlockMetalCaster block_metal_caster;
  public static BlockAlloyMixer block_alloy_mixer;
  public static BlockMetal block_metal;
  public static BlockFoundryOre block_ore;
  public static BlockMetalInfuser block_metal_infuser;
  
  public static BlockMetalSlab block_slab1;
  public static BlockMetalSlab block_slab2;

  public static BlockMetalSlab block_slabdouble1;
  public static BlockMetalSlab block_slabdouble2;

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
    block_slab1 = (BlockMetalSlab)new BlockMetalSlab(config.getBlock( "metal_slab1", GetNextID()).getInt(),false,-1, SLAB1_METALS,SLAB1_ICONS).setUnlocalizedName("metalSlab1");
    block_slab2 = (BlockMetalSlab)new BlockMetalSlab(config.getBlock( "metal_slab2", GetNextID()).getInt(),false,-1, SLAB2_METALS,SLAB2_ICONS).setUnlocalizedName("metalSlab2");

    block_slabdouble1 = (BlockMetalSlab)new BlockMetalSlab(config.getBlock( "metal_slabdouble1", GetNextID()).getInt(),true,block_slab1.blockID, SLAB1_METALS,SLAB1_ICONS).setUnlocalizedName("metalSlabDouble1");
    block_slabdouble2 = (BlockMetalSlab)new BlockMetalSlab(config.getBlock( "metal_slabdouble2", GetNextID()).getInt(),true,block_slab2.blockID, SLAB2_METALS,SLAB2_ICONS).setUnlocalizedName("metalSlabDouble2");

    block_slab1.SetOtherBlockID(block_slabdouble1.blockID);
    block_slab2.SetOtherBlockID(block_slabdouble2.blockID);
    
    MinecraftForge.setBlockHarvestLevel(block_ore, "pickaxe", 1);
    
    GameRegistry.registerBlock(block_foundry_crucible, "foundryCrucible");
    GameRegistry.registerBlock(block_induction_crucible_furnace, "inductionCrucibleFurnace");
    GameRegistry.registerBlock(block_metal_caster, "caster");
    GameRegistry.registerBlock(block_alloy_mixer, "alloyMixer");
    GameRegistry.registerBlock(block_metal, ItemBlockMulti.class, "blockFoundryMetal");
    GameRegistry.registerBlock(block_ore, ItemBlockMulti.class, "blockFoundryOre");
    GameRegistry.registerBlock(block_metal_infuser, "infuser");

    GameRegistry.registerBlock(block_slab1, ItemBlockMulti.class, "slab1");
    GameRegistry.registerBlock(block_slab2, ItemBlockMulti.class, "slab2");
    GameRegistry.registerBlock(block_slabdouble1, ItemBlockMulti.class, "slabDouble1");
    GameRegistry.registerBlock(block_slabdouble2, ItemBlockMulti.class, "slabDouble2");
    
    
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

    for(i = 0; i < SLAB1_NAMES.length; i++)
    {
      ItemStack is = new ItemStack(block_slab1,  1, i);
      LanguageRegistry.addName(is, SLAB1_NAMES[i]);
    }

    for(i = 0; i < SLAB2_NAMES.length; i++)
    {
      ItemStack is = new ItemStack(block_slab2,  1, i);
      LanguageRegistry.addName(is, SLAB2_NAMES[i]);
    }
  }
}
