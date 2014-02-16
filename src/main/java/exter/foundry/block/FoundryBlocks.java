package exter.foundry.block;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import exter.foundry.item.ItemBlockMulti;
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
    "Titanium"
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
    "Aluminum Slab",
    "Chromium Slab",
    "Platinum Slab"
  };

  private static final String[] SLAB3_NAMES = 
  {
    "Manganese Slab",
    "Titanium Slab",
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
    "foundry:metalblock_titanium"
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
    new MetalStair("Iron","Iron Stairs",Blocks.iron_block,0),
    new MetalStair("Gold","Gold Stairs",Blocks.gold_block,0),
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
    new MetalStair("Aluminum","Aluminum Stairs",null,BlockMetal.BLOCK_ALUMINUM),
    new MetalStair("Chromium","Chromium Stairs",null,BlockMetal.BLOCK_CHROMIUM),
    new MetalStair("Platinum","Platinum Stairs",null,BlockMetal.BLOCK_PLATINUM),
    new MetalStair("Manganese","Manganese Stairs",null,BlockMetal.BLOCK_MANGANESE),
    new MetalStair("Titanium","Titanium Stairs",null,BlockMetal.BLOCK_TITANIUM)
  };
  
  public static BlockRefractoryCasing block_foundry_crucible;
  public static BlockFoundryMachine block_machine;

  public static BlockMetal block_metal;
  public static BlockFoundryOre block_ore;
  
  public static BlockMetalSlab block_slab1;
  public static BlockMetalSlab block_slab2;
  public static BlockMetalSlab block_slab3;

  public static BlockMetalSlab block_slabdouble1;
  public static BlockMetalSlab block_slabdouble2;
  public static BlockMetalSlab block_slabdouble3;
  
  public static BlockStairs[] block_metal_stairs;

  //All blocks mapped by the metal name.
  public static Map<String,ItemStack> block_stacks = new HashMap<String,ItemStack>();
  
  //All slabs mapped by the metal name.
  public static Map<String,ItemStack> slab_stacks = new HashMap<String,ItemStack>();
  
  
  static private void RegisterHalfSlabs(Configuration config)
  {
    int i;
    block_slab1 = (BlockMetalSlab)new BlockMetalSlab(false,null, SLAB1_METALS,SLAB1_ICONS).setBlockName("metalSlab1");
    block_slab2 = (BlockMetalSlab)new BlockMetalSlab(false,null, SLAB2_METALS,SLAB2_ICONS).setBlockName("metalSlab2");
    block_slab3 = (BlockMetalSlab)new BlockMetalSlab(false,null, SLAB3_METALS,SLAB3_ICONS).setBlockName("metalSlab3");
    block_slabdouble1 = (BlockMetalSlab)new BlockMetalSlab(true,block_slab1, SLAB1_METALS,SLAB1_ICONS).setBlockName("metalSlabDouble1");
    block_slabdouble2 = (BlockMetalSlab)new BlockMetalSlab(true,block_slab2, SLAB2_METALS,SLAB2_ICONS).setBlockName("metalSlabDouble2");
    block_slabdouble3 = (BlockMetalSlab)new BlockMetalSlab(true,block_slab3, SLAB3_METALS,SLAB3_ICONS).setBlockName("metalSlabDouble3");
    block_slab1.SetOtherBlock(block_slabdouble1);
    block_slab2.SetOtherBlock(block_slabdouble2);
    block_slab3.SetOtherBlock(block_slabdouble3);

    GameRegistry.registerBlock(block_slab1, ItemBlockMulti.class, "slab1");
    GameRegistry.registerBlock(block_slab2, ItemBlockMulti.class, "slab2");
    GameRegistry.registerBlock(block_slab3, ItemBlockMulti.class, "slab3");
    GameRegistry.registerBlock(block_slabdouble1, ItemBlockMulti.class, "slabDouble1");
    GameRegistry.registerBlock(block_slabdouble2, ItemBlockMulti.class, "slabDouble2");
    GameRegistry.registerBlock(block_slabdouble3, ItemBlockMulti.class, "slabDouble3");

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

    for(i = 0; i < SLAB3_NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(block_slab3,  1, i);
      LanguageRegistry.addName(stack, SLAB3_NAMES[i]);
      slab_stacks.put(SLAB3_METALS[i], stack);
      ItemRegistry.instance.RegisterItem("blockSlab" + SLAB3_METALS[i], stack);
    }
  }
  
  static public void RegisterBlocks(Configuration config)
  {
    int i;
    
    block_foundry_crucible = new BlockRefractoryCasing();
    block_machine = new BlockFoundryMachine();
    block_metal = new BlockMetal();
    block_ore = new BlockFoundryOre();

    RegisterHalfSlabs(config);

    int id;
    
    block_metal_stairs = new BlockStairs[STAIRS_BLOCKS.length];
    for(i = 0; i < STAIRS_BLOCKS.length; i++)
    {
      MetalStair ms = STAIRS_BLOCKS[i];
      block_metal_stairs[i] = (BlockStairs)new BlockStairsFoundry(ms.GetBlock(),ms.block_meta).setBlockName("stairs" + ms.metal);
      GameRegistry.registerBlock(block_metal_stairs[i], "stairs" + ms.metal);
      LanguageRegistry.addName(block_metal_stairs[i], ms.name);
      ItemRegistry.instance.RegisterItem("blockStairs" + ms.metal, new ItemStack(block_metal_stairs[i]));
    }

    for(i = 0; i < BlockMetal.METAL_NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(block_metal,1,i);
      block_stacks.put(BlockMetal.METAL_NAMES[i], stack);
      ItemRegistry.instance.RegisterItem("blockMetal" + BlockMetal.METAL_NAMES[i], stack);
    }
    block_stacks.put("Iron", new ItemStack(Blocks.iron_block));
    block_stacks.put("Gold", new ItemStack(Blocks.gold_block));
    block_stacks.put("Glass", new ItemStack(Blocks.glass));
    
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
