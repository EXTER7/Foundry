package exter.foundry.block;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.item.ItemBlockMulti;
import exter.foundry.item.ItemBlockSlab;
import exter.foundry.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class FoundryBlocks
{

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
    "Aluminum",
    "Chromium",
    "Platinum"
  };

  private static final String[] SLAB3_METALS = 
  {
    "Manganese",
    "Titanium",
    "Cupronickel"
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
    "foundry:metalblock_aluminum",
    "foundry:metalblock_chromium",
    "foundry:metalblock_platinum"
  };

  private static final String[] SLAB3_ICONS = 
  {
    "foundry:metalblock_manganese",
    "foundry:metalblock_titanium",
    "foundry:metalblock_cupronickel"
  };
  
  public static class MetalStair
  {
    public final String metal;
    public final int block_meta;
    private final Block block;
    private final int block_index;
    
    public MetalStair(String me,Block bl,int bm)
    {
      metal = me;
      block = bl;
      block_meta = bm;
      block_index = -1;
    }

    public MetalStair(String me,int idx,int bm)
    {
      metal = me;
      block = null;
      block_meta = bm;
      block_index = idx;
    }
    
    public Block GetBlock()
    {
      switch(block_index)
      {
        case 0:
          return block_metal1;
        case 1:
          return block_metal2;
        default:
          return block;
      }
    }
  }
  
  //Metal stair block data
  public static final MetalStair[] STAIRS_BLOCKS = 
  {
    new MetalStair("Iron",Blocks.iron_block,0),
    new MetalStair("Gold",Blocks.gold_block,0),
    new MetalStair("Copper",0,BlockMetal1.BLOCK_COPPER),
    new MetalStair("Tin",0,BlockMetal1.BLOCK_TIN),
    new MetalStair("Bronze",0,BlockMetal1.BLOCK_BRONZE),
    new MetalStair("Electrum",0,BlockMetal1.BLOCK_ELECTRUM),
    new MetalStair("Invar",0,BlockMetal1.BLOCK_INVAR),
    new MetalStair("Nickel",0,BlockMetal1.BLOCK_NICKEL),
    new MetalStair("Zinc",0,BlockMetal1.BLOCK_ZINC),
    new MetalStair("Brass",0,BlockMetal1.BLOCK_BRASS),
    new MetalStair("Silver",0,BlockMetal1.BLOCK_SILVER),
    new MetalStair("Steel",0,BlockMetal1.BLOCK_STEEL),
    new MetalStair("Lead",0,BlockMetal1.BLOCK_LEAD),
    new MetalStair("Aluminum",0,BlockMetal1.BLOCK_ALUMINUM),
    new MetalStair("Chromium",0,BlockMetal1.BLOCK_CHROMIUM),
    new MetalStair("Platinum",0,BlockMetal1.BLOCK_PLATINUM),
    new MetalStair("Manganese",0,BlockMetal1.BLOCK_MANGANESE),
    new MetalStair("Titanium",0,BlockMetal1.BLOCK_TITANIUM),
    new MetalStair("Cupronickel",1,BlockMetal2.BLOCK_CUPRONICKEL)
  };
  
  public static BlockRefractoryCasing block_refractory_casing;
  public static BlockFoundryMachine block_machine;

  public static BlockMetal1 block_metal1;
  public static BlockMetal2 block_metal2;
  public static BlockFoundryOre block_ore;
  
  public static BlockMetalSlab block_slab1;
  public static BlockMetalSlab block_slab2;
  public static BlockMetalSlab block_slab3;

  public static BlockMetalSlab block_slabdouble1;
  public static BlockMetalSlab block_slabdouble2;
  public static BlockMetalSlab block_slabdouble3;
  
  public static BlockStairs[] block_metal_stairs;
  
  public static BlockAlloyFurnace block_alloy_furnace;

  //All blocks mapped by the metal name.
  public static Map<String,ItemStack> block_stacks = new HashMap<String,ItemStack>();
  
  //All slabs mapped by the metal name.
  public static Map<String,ItemStack> slab_stacks = new HashMap<String,ItemStack>();
  
  
  static private void RegisterHalfSlabs(Configuration config)
  {
    int i;
    block_slab1 = (BlockMetalSlab)new BlockMetalSlab(false,null, SLAB1_METALS,SLAB1_ICONS);
    block_slab2 = (BlockMetalSlab)new BlockMetalSlab(false,null, SLAB2_METALS,SLAB2_ICONS);
    block_slab3 = (BlockMetalSlab)new BlockMetalSlab(false,null, SLAB3_METALS,SLAB3_ICONS);
    block_slabdouble1 = (BlockMetalSlab)new BlockMetalSlab(true,block_slab1, SLAB1_METALS,SLAB1_ICONS).setBlockName("metalSlabDouble1");
    block_slabdouble2 = (BlockMetalSlab)new BlockMetalSlab(true,block_slab2, SLAB2_METALS,SLAB2_ICONS).setBlockName("metalSlabDouble2");
    block_slabdouble3 = (BlockMetalSlab)new BlockMetalSlab(true,block_slab3, SLAB3_METALS,SLAB3_ICONS).setBlockName("metalSlabDouble3");
    block_slab1.SetOtherBlock(block_slabdouble1);
    block_slab2.SetOtherBlock(block_slabdouble2);
    block_slab3.SetOtherBlock(block_slabdouble3);

    GameRegistry.registerBlock(block_slab1, ItemBlockSlab.class, "slab1");
    GameRegistry.registerBlock(block_slab2, ItemBlockSlab.class, "slab2");
    GameRegistry.registerBlock(block_slab3, ItemBlockSlab.class, "slab3");
    GameRegistry.registerBlock(block_slabdouble1, ItemBlockMulti.class, "slabDouble1");
    GameRegistry.registerBlock(block_slabdouble2, ItemBlockMulti.class, "slabDouble2");
    GameRegistry.registerBlock(block_slabdouble3, ItemBlockMulti.class, "slabDouble3");

    for(i = 0; i < SLAB1_METALS.length; i++)
    {
      ItemStack stack = new ItemStack(block_slab1,  1, i);
      slab_stacks.put(SLAB1_METALS[i], stack);
      ItemRegistry.instance.RegisterItem("blockSlab" + SLAB1_METALS[i], stack);
    }

    for(i = 0; i < SLAB2_METALS.length; i++)
    {
      ItemStack stack = new ItemStack(block_slab2,  1, i);
      slab_stacks.put(SLAB2_METALS[i], stack);
      ItemRegistry.instance.RegisterItem("blockSlab" + SLAB2_METALS[i], stack);
    }

    for(i = 0; i < SLAB3_METALS.length; i++)
    {
      ItemStack stack = new ItemStack(block_slab3,  1, i);
      slab_stacks.put(SLAB3_METALS[i], stack);
      ItemRegistry.instance.RegisterItem("blockSlab" + SLAB3_METALS[i], stack);
    }
  }
  
  static public void RegisterBlocks(Configuration config)
  {
    int i;
    
    block_refractory_casing = new BlockRefractoryCasing();
    block_machine = new BlockFoundryMachine();
    block_metal1 = new BlockMetal1();
    block_metal2 = new BlockMetal2();
    block_ore = new BlockFoundryOre();
    block_alloy_furnace = new BlockAlloyFurnace();
    GameRegistry.registerBlock(block_refractory_casing, "refractoryCasing");
    GameRegistry.registerBlock(block_machine, ItemBlockMulti.class, "foundryMachine");
    GameRegistry.registerBlock(block_metal1, ItemBlockMulti.class, "blockFoundryMetal");
    GameRegistry.registerBlock(block_metal2, ItemBlockMulti.class, "blockFoundryMetal2");
    GameRegistry.registerBlock(block_ore, ItemBlockMulti.class, "blockFoundryOre");
    GameRegistry.registerBlock(block_alloy_furnace, "alloyFurnace");

    RegisterHalfSlabs(config);
    
    block_metal_stairs = new BlockStairs[STAIRS_BLOCKS.length];
    for(i = 0; i < STAIRS_BLOCKS.length; i++)
    {
      MetalStair ms = STAIRS_BLOCKS[i];
      block_metal_stairs[i] = (BlockStairs)new BlockStairsFoundry(ms.GetBlock(),ms.block_meta).setBlockName("metalStairs." + ms.metal);
      GameRegistry.registerBlock(block_metal_stairs[i], "stairs" + ms.metal);
      ItemRegistry.instance.RegisterItem("blockStairs" + ms.metal, new ItemStack(block_metal_stairs[i]));
    }

    for(i = 0; i < BlockMetal1.METAL_NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(block_metal1,1,i);
      block_stacks.put(BlockMetal1.METAL_NAMES[i], stack);
      ItemRegistry.instance.RegisterItem("blockMetal" + BlockMetal1.METAL_NAMES[i], stack);
    }
    for(i = 0; i < BlockMetal2.METAL_NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(block_metal2,1,i);
      block_stacks.put(BlockMetal2.METAL_NAMES[i], stack);
      ItemRegistry.instance.RegisterItem("blockMetal" + BlockMetal2.METAL_NAMES[i], stack);
    }
    block_stacks.put("Iron", new ItemStack(Blocks.iron_block));
    block_stacks.put("Gold", new ItemStack(Blocks.gold_block));
    block_stacks.put("Glass", new ItemStack(Blocks.glass));
    

    
    
    ItemRegistry.instance.RegisterItem("blockRefractoryCasing", new ItemStack(block_refractory_casing));
    ItemRegistry.instance.RegisterItem("blockMachineAlloyFurnace", new ItemStack(block_alloy_furnace));
    ItemRegistry.instance.RegisterItem("blockMachineICF", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_ICF));
    ItemRegistry.instance.RegisterItem("blockMachineCaster", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_CASTER));
    ItemRegistry.instance.RegisterItem("blockMachineAlloyMixer", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_ALLOYMIXER));
    ItemRegistry.instance.RegisterItem("blockMachineInfuser", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_INFUSER));

    
    for(i = 0; i < BlockMetal1.METAL_NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(block_metal1,  1, i);
      OreDictionary.registerOre(BlockMetal1.OREDICT_NAMES[i], stack);
    }
    for(i = 0; i < BlockMetal2.METAL_NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(block_metal2,  1, i);
      OreDictionary.registerOre(BlockMetal2.OREDICT_NAMES[i], stack);
    }
    for(i = 0; i < BlockFoundryOre.OREDICT_NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(block_ore,  1, i);
      OreDictionary.registerOre(BlockFoundryOre.OREDICT_NAMES[i], stack);
    }

  }
}
