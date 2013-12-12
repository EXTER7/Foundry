package exter.foundry.block;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import exter.foundry.item.ItemBlockMulti;
import exter.foundry.item.ItemMold;
import exter.foundry.registry.ItemRegistry;
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
    "Lead",
    "Aluminum"
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
    "Lead Slab",
    "Aluminum Slab"
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
    "foundry:metalblock_lead",
    "foundry:metalblock_aluminum"
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
    new MetalStair("Lead","Lead Stairs",null,BlockMetal.BLOCK_LEAD),
    new MetalStair("Aluminum","Aluminum Stairs",null,BlockMetal.BLOCK_ALUMINUM)
  };
  
  public static BlockFoundryCrucible block_foundry_crucible;
  public static BlockFoundryMachine block_machine;

  public static BlockMetal block_metal;
  public static BlockFoundryOre block_ore;
  
  public static BlockMetalSlab block_slab1;
  public static BlockMetalSlab block_slab2;

  public static BlockMetalSlab block_slabdouble1;
  public static BlockMetalSlab block_slabdouble2;
  
  public static BlockStairs[] block_metal_stairs;

  //All blocks mapped by the metal name.
  public static Map<String,ItemStack> block_stacks = new HashMap<String,ItemStack>();
  
  //All slabs mapped by the metal name.
  public static Map<String,ItemStack> slab_stacks = new HashMap<String,ItemStack>();
  
  
  static private void RegisterHalfSlabs(Configuration config)
  {
    int i;
    int id_single1 = config.getBlock( "metal_slab1", GetNextID()).getInt();
    int id_single2 = config.getBlock( "metal_slab2", GetNextID()).getInt();
    int id_double1 = config.getBlock( "metal_slabdouble1", GetNextID()).getInt();
    int id_double2 = config.getBlock( "metal_slabdouble2", GetNextID()).getInt();
    
    if(id_single1 < 1 || id_single2 < 1 || id_double1 < 1 || id_double2 < 1)
    {
      return;
    }
    block_slab1 = (BlockMetalSlab)new BlockMetalSlab(id_single1,false,-1, SLAB1_METALS,SLAB1_ICONS).setUnlocalizedName("metalSlab1");
    block_slab2 = (BlockMetalSlab)new BlockMetalSlab(id_single2,false,-1, SLAB2_METALS,SLAB2_ICONS).setUnlocalizedName("metalSlab2");
    block_slabdouble1 = (BlockMetalSlab)new BlockMetalSlab(id_double1,true,block_slab1.blockID, SLAB1_METALS,SLAB1_ICONS).setUnlocalizedName("metalSlabDouble1");
    block_slabdouble2 = (BlockMetalSlab)new BlockMetalSlab(id_double2,true,block_slab2.blockID, SLAB2_METALS,SLAB2_ICONS).setUnlocalizedName("metalSlabDouble2");
    block_slab1.SetOtherBlockID(block_slabdouble1.blockID);
    block_slab2.SetOtherBlockID(block_slabdouble2.blockID);

    GameRegistry.registerBlock(block_slab1, ItemBlockMulti.class, "slab1");
    GameRegistry.registerBlock(block_slab2, ItemBlockMulti.class, "slab2");
    GameRegistry.registerBlock(block_slabdouble1, ItemBlockMulti.class, "slabDouble1");
    GameRegistry.registerBlock(block_slabdouble2, ItemBlockMulti.class, "slabDouble2");

    for(i = 0; i < SLAB1_NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(block_slab1,  1, i);
      LanguageRegistry.addName(stack, SLAB1_NAMES[i]);
      slab_stacks.put(SLAB1_METALS[i], stack);
      ItemRegistry.instance.RegisterItem("blockSlab" + SLAB1_METALS[i], stack);
    }

    for(i = 0; i < SLAB2_NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(block_slab2,  1, i);
      LanguageRegistry.addName(stack, SLAB2_NAMES[i]);
      slab_stacks.put(SLAB2_METALS[i], stack);
      ItemRegistry.instance.RegisterItem("blockSlab" + SLAB2_METALS[i], stack);
    }
  }
  
  static public void RegisterBlocks(Configuration config)
  {
    int i;
    
    block_foundry_crucible = new BlockFoundryCrucible(config.getBlock( "foundry_crucible", GetNextID()).getInt());
    block_machine = new BlockFoundryMachine(config.getBlock( "foundry_machine", GetNextID()).getInt());
    block_metal = new BlockMetal(config.getBlock( "metal_block", GetNextID()).getInt());
    block_ore = new BlockFoundryOre(config.getBlock( "ore", GetNextID()).getInt());

    RegisterHalfSlabs(config);

    int id;
    
    block_metal_stairs = new BlockStairs[STAIRS_BLOCKS.length];
    for(i = 0; i < STAIRS_BLOCKS.length; i++)
    {
      MetalStair ms = STAIRS_BLOCKS[i];
      id = config.getBlock( "stair_" +  ms.metal, GetNextID()).getInt();
      if(id > 0)
      {
        block_metal_stairs[i] = (BlockStairs)new BlockStairsFoundry(id,ms.GetBlock(),ms.block_meta).setUnlocalizedName("stairs" + ms.metal);
        GameRegistry.registerBlock(block_metal_stairs[i], "stairs" + ms.metal);
        LanguageRegistry.addName(block_metal_stairs[i], ms.name);
        ItemRegistry.instance.RegisterItem("blockStairs" + ms.metal, new ItemStack(block_metal_stairs[i]));
      }
    }

    for(i = 0; i < BlockMetal.METAL_NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(block_metal,1,i);
      block_stacks.put(BlockMetal.METAL_NAMES[i], stack);
      ItemRegistry.instance.RegisterItem("blockMetal" + BlockMetal.METAL_NAMES[i], stack);
    }
    block_stacks.put("Iron", new ItemStack(Block.blockIron));
    block_stacks.put("Gold", new ItemStack(Block.blockGold));

    
    MinecraftForge.setBlockHarvestLevel(block_ore, "pickaxe", 1);
    
    GameRegistry.registerBlock(block_foundry_crucible, "foundryCrucible");
    GameRegistry.registerBlock(block_machine, ItemBlockMulti.class, "foundryMachine");
    GameRegistry.registerBlock(block_metal, ItemBlockMulti.class, "blockFoundryMetal");
    GameRegistry.registerBlock(block_ore, ItemBlockMulti.class, "blockFoundryOre");

    
    
    LanguageRegistry.addName(block_foundry_crucible, "Refractory Casing");
    ItemRegistry.instance.RegisterItem("blockRefractoryCasing", new ItemStack(block_foundry_crucible));
    
    LanguageRegistry.addName(new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_ICF), "Induction Crucible Furnace");
    LanguageRegistry.addName(new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_CASTER), "Metal Caster");
    LanguageRegistry.addName(new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_ALLOYMIXER), "Alloy Mixer");
    LanguageRegistry.addName(new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_INFUSER), "Metal Infuser");

    ItemRegistry.instance.RegisterItem("blockMachineICF", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_ICF));
    ItemRegistry.instance.RegisterItem("blockMachineCaster", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_CASTER));
    ItemRegistry.instance.RegisterItem("blockMachineAlloyMixer", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_ALLOYMIXER));
    ItemRegistry.instance.RegisterItem("blockMachineInfuser", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_INFUSER));

    
    for(i = 0; i < BlockMetal.NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(block_metal,  1, i);
      LanguageRegistry.addName(stack, BlockMetal.NAMES[i]);
      OreDictionary.registerOre(BlockMetal.OREDICT_NAMES[i], stack);
    }
    for(i = 0; i < BlockFoundryOre.NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(block_ore,  1, i);
      LanguageRegistry.addName(stack, BlockFoundryOre.NAMES[i]);
      OreDictionary.registerOre(BlockFoundryOre.OREDICT_NAMES[i], stack);
    }

  }
}
