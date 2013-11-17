package exter.foundry.block;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import exter.foundry.item.ItemBlockMulti;
import exter.foundry.item.ItemMold;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

public class FoundryBlocks
{

  //Default block id.
  static private int next_id = 3883;

  static public int GetNextID()
  {
    return next_id++;
  }
  
  private static final String[] SLAB1_METALS = 
  {
    "Iron",
    "Gold",
    "Copper",
    "Tin",
    "Bronze",
    "Electrum",
    "Invar",
    "Nickel"
  };
  
  private static final String[] SLAB2_METALS = 
  {
    "Zinc",
    "Brass",
    "Silver",
    "Steel",
    "Lead"
  };

  private static final String[] SLAB1_NAMES = 
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

  private static final String[] SLAB2_NAMES = 
  {
    "Zinc Slab",
    "Brass Slab",
    "Silver Slab",
    "Steel Slab",
    "Lead Slab"
  };

  private static final String[] SLAB1_ICONS = 
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

  private static final String[] SLAB2_ICONS = 
  {
    "foundry:metalblock_zinc",
    "foundry:metalblock_brass",
    "foundry:metalblock_silver",
    "foundry:metalblock_steel",
    "foundry:metalblock_lead"
  };

  public static class MetalStair
  {
    public final String metal;
    public final String name;
    public final int block_meta;
    private final Block block;
    
    public MetalStair(String me,String na,Block bl,int bm)
    {
      metal = me;
      name = na;
      block = bl;
      block_meta = bm;
    }
    
    public Block GetBlock()
    {
      return block == null?block_metal:block;
    }
  }
  
  //Metal stair block data
  public static final MetalStair[] STAIRS_BLOCKS = 
  {
    new MetalStair("Iron","Iron Stairs",Block.blockIron,0),
    new MetalStair("Gold","Gold Stairs",Block.blockGold,0),
    new MetalStair("Copper","Copper Stairs",null,BlockMetal.BLOCK_COPPER),
    new MetalStair("Tin","Tin Stairs",null,BlockMetal.BLOCK_TIN),
    new MetalStair("Bronze","Bronze Stairs",null,BlockMetal.BLOCK_BRONZE),
    new MetalStair("Electrum","Electrum Stairs",null,BlockMetal.BLOCK_ELECTRUM),
    new MetalStair("Invar","Invar Stairs",null,BlockMetal.BLOCK_INVAR),
    new MetalStair("Nickel","Nickel Stairs",null,BlockMetal.BLOCK_NICKEL),
    new MetalStair("Zinc","Zinc Stairs",null,BlockMetal.BLOCK_ZINC),
    new MetalStair("Brass","Brass Stairs",null,BlockMetal.BLOCK_BRASS),
    new MetalStair("Silver","Silver Stairs",null,BlockMetal.BLOCK_SILVER),
    new MetalStair("Steel","Steel Stairs",null,BlockMetal.BLOCK_STEEL),
    new MetalStair("Lead","Lead Stairs",null,BlockMetal.BLOCK_LEAD)
  };
  
  public static BlockFoundryCrucible block_foundry_crucible;
  public static BlockFoundryMachine block_machine;

  @Deprecated
  public static BlockInductionCrucibleFurnace block_induction_crucible_furnace;
  @Deprecated
  public static BlockMetalCaster block_metal_caster;
  @Deprecated
  public static BlockAlloyMixer block_alloy_mixer;
  public static BlockMetal block_metal;
  public static BlockFoundryOre block_ore;
  @Deprecated
  public static BlockMetalInfuser block_metal_infuser;
  
  public static BlockMetalSlab block_slab1;
  public static BlockMetalSlab block_slab2;

  public static BlockMetalSlab block_slabdouble1;
  public static BlockMetalSlab block_slabdouble2;
  
  public static BlockStairs[] block_metal_stairs;

  //All blocks mapped by the metal name.
  public static Map<String,ItemStack> block_stacks = new HashMap<String,ItemStack>();
  
  //All slabs mapped by the metal name.
  public static Map<String,ItemStack> slab_stacks = new HashMap<String,ItemStack>();
  
  
  static public void RegisterBlocks(Configuration config)
  {
    int i;
    
    block_foundry_crucible = new BlockFoundryCrucible(config.getBlock( "foundry_crucible", GetNextID()).getInt());
    block_machine = new BlockFoundryMachine(config.getBlock( "foundry_machine", GetNextID()).getInt());
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
    
    
    block_metal_stairs = new BlockStairs[STAIRS_BLOCKS.length];
    for(i = 0; i < STAIRS_BLOCKS.length; i++)
    {
      MetalStair ms = STAIRS_BLOCKS[i];
      block_metal_stairs[i] = (BlockStairs)new BlockStairsFoundry(config.getBlock( "stair_" +  ms.metal, GetNextID()).getInt(),ms.GetBlock(),ms.block_meta).setUnlocalizedName("stairs" + ms.metal);
      GameRegistry.registerBlock(block_metal_stairs[i], "stairs" + ms.metal);
      LanguageRegistry.addName(block_metal_stairs[i], ms.name);
    }

    for(i = 0; i < BlockMetal.METAL_NAMES.length; i++)
    {
      block_stacks.put(BlockMetal.METAL_NAMES[i], new ItemStack(block_metal,1,i));
    }
    block_stacks.put("Iron", new ItemStack(Block.blockIron));
    block_stacks.put("Gold", new ItemStack(Block.blockGold));

    
    MinecraftForge.setBlockHarvestLevel(block_ore, "pickaxe", 1);
    
    GameRegistry.registerBlock(block_foundry_crucible, "foundryCrucible");
    GameRegistry.registerBlock(block_machine, ItemBlockMulti.class, "foundryMachine");
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
    
    LanguageRegistry.addName(new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_ICF), "Induction Crucible Furnace");
    LanguageRegistry.addName(new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_CASTER), "Metal Caster");
    LanguageRegistry.addName(new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_ALLOYMIXER), "Alloy Mixer");
    LanguageRegistry.addName(new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_INFUSER), "Metal Infuser");

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
      slab_stacks.put(SLAB1_METALS[i], is);
    }

    for(i = 0; i < SLAB2_NAMES.length; i++)
    {
      ItemStack is = new ItemStack(block_slab2,  1, i);
      LanguageRegistry.addName(is, SLAB2_NAMES[i]);
      slab_stacks.put(SLAB2_METALS[i], is);
    }
  }
}
