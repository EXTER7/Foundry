package exter.foundry.block;

import java.util.HashMap;
import java.util.Map;

import exter.foundry.item.ItemBlockMulti;
import exter.foundry.item.ItemBlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class FoundryBlocks
{
  private static final BlockMetal.Variant[] BLOCK1_METALS = 
  {
      new BlockMetal.Variant("copper", "Copper", "blockCopper"),
      new BlockMetal.Variant("tin", "Tin", "blockTin"),
      new BlockMetal.Variant("bronze", "Bronze", "blockBronze"),
      new BlockMetal.Variant("electrum", "Electrum", "blockElectrum"),
      new BlockMetal.Variant("invar", "Invar", "blockInvar"),
      new BlockMetal.Variant("nickel", "Nickel", "blockNickel"),
      new BlockMetal.Variant("zinc", "Zinc", "blockZinc"),
      new BlockMetal.Variant("brass", "Brass", "blockBrass"),
      new BlockMetal.Variant("silver", "Silver", "blockSilver"),
      new BlockMetal.Variant("steel", "Steel", "blockSteel"),
      new BlockMetal.Variant("lead", "Lead", "blockLead"),
      new BlockMetal.Variant("aluminum", "Aluminum", "blockAluminum"),
      new BlockMetal.Variant("chromium", "Chromium", "blockChromium"),
      new BlockMetal.Variant("platinum", "Platinum", "blockPlatinum"),
      new BlockMetal.Variant("manganese", "Manganese", "blockManganese"),
      new BlockMetal.Variant("titanium", "Titanium", "blockTitanium")
  };

  private static final BlockMetal.Variant[] BLOCK2_METALS = 
  {
      new BlockMetal.Variant("cupronickel", "Cupronickel", "blockCupronickel")
  };

  private static final BlockMetalSlab.Variant[] SLAB1_METALS = 
  {
    new BlockMetalSlab.Variant("iron", "Iron"),
    new BlockMetalSlab.Variant("gold", "Gold"),
    new BlockMetalSlab.Variant("copper", "Copper"),
    new BlockMetalSlab.Variant("tin", "Tin"),
    new BlockMetalSlab.Variant("bronze", "Bronze"),
    new BlockMetalSlab.Variant("electrum", "Electrum"),
    new BlockMetalSlab.Variant("invar", "Invar"),
    new BlockMetalSlab.Variant("nickel", "Nickel")
  };
  
  private static final BlockMetalSlab.Variant[] SLAB2_METALS = 
  {
    new BlockMetalSlab.Variant("zinc", "Zinc"),
    new BlockMetalSlab.Variant("brass", "Brass"),
    new BlockMetalSlab.Variant("silver", "Silver"),
    new BlockMetalSlab.Variant("steel", "Steel"),
    new BlockMetalSlab.Variant("lead", "Lead"),
    new BlockMetalSlab.Variant("aluminum", "Aluminum"),
    new BlockMetalSlab.Variant("chromium", "Chromium"),
    new BlockMetalSlab.Variant("platinum", "Platinum")
  };

  private static final BlockMetalSlab.Variant[] SLAB3_METALS = 
  {
    new BlockMetalSlab.Variant("manganese", "Manganese"),
    new BlockMetalSlab.Variant("titanium", "Titanium"),
    new BlockMetalSlab.Variant("cupronickel", "Cupronickel")
  };
  
  public static BlockRefractoryCasing block_refractory_casing;
  public static BlockFoundryMachine block_machine;

  public static BlockMetal block_metal1;
  public static BlockMetal block_metal2;
  public static BlockFoundryOre block_ore;
  
  public static BlockMetalSlab block_slab1;
  public static BlockMetalSlab block_slab2;
  public static BlockMetalSlab block_slab3;

//  public static BlockMetalSlab block_slabdouble1;
//  public static BlockMetalSlab block_slabdouble2;
//  public static BlockMetalSlab block_slabdouble3;
  
  public static Map<String,BlockStairs> block_metal_stairs;
  
  public static BlockAlloyFurnace block_alloy_furnace;
  
  public static BlockRefractoryHopper block_refractory_hopper;

  //All blocks mapped by the metal name.
  public static Map<String,ItemStack> block_stacks = new HashMap<String,ItemStack>();
  
  //All slabs mapped by the metal name.
  public static Map<String,ItemStack> slab_stacks = new HashMap<String,ItemStack>();
  
  
  static private void RegisterHalfSlabs(Configuration config)
  {
    block_slab1 = new BlockMetalSlab() { public Variant[] getVariants() { return SLAB1_METALS; } };
    block_slab2 = new BlockMetalSlab() { public Variant[] getVariants() { return SLAB2_METALS; } };
    block_slab3 = new BlockMetalSlab() { public Variant[] getVariants() { return SLAB3_METALS; } };
//    block_slabdouble1 = (BlockMetalSlab)new BlockMetalSlab(true,block_slab1, SLAB1_METALS,SLAB1_ICONS).setBlockName("metalSlabDouble1");
//    block_slabdouble2 = (BlockMetalSlab)new BlockMetalSlab(true,block_slab2, SLAB2_METALS,SLAB2_ICONS).setBlockName("metalSlabDouble2");
//    block_slabdouble3 = (BlockMetalSlab)new BlockMetalSlab(true,block_slab3, SLAB3_METALS,SLAB3_ICONS).setBlockName("metalSlabDouble3");
//    block_slab1.SetOtherBlock(block_slabdouble1);
//    block_slab2.SetOtherBlock(block_slabdouble2);
//    block_slab3.SetOtherBlock(block_slabdouble3);

    GameRegistry.registerBlock(block_slab1, ItemBlockSlab.class, "slabMetal1");
    GameRegistry.registerBlock(block_slab2, ItemBlockSlab.class, "slabMetal2");
    GameRegistry.registerBlock(block_slab3, ItemBlockSlab.class, "slabMetal3");
//    GameRegistry.registerBlock(block_slabdouble1, ItemBlockMulti.class, "slabDouble1");
//    GameRegistry.registerBlock(block_slabdouble2, ItemBlockMulti.class, "slabDouble2");
//    GameRegistry.registerBlock(block_slabdouble3, ItemBlockMulti.class, "slabDouble3");

//    for(i = 0; i < SLAB1_METALS.length; i++)
//    {
//      ItemStack stack = new ItemStack(block_slab1,  1, i);
//      GameRegistry.registerCustomItemStack("blockSlab" + SLAB1_METALS[i], stack);
//    }
//
//    for(i = 0; i < SLAB2_METALS.length; i++)
//    {
//      ItemStack stack = new ItemStack(block_slab2,  1, i);
//      GameRegistry.registerCustomItemStack("blockSlab" + SLAB2_METALS[i], stack);
//    }

//    for(i = 0; i < SLAB3_METALS.length; i++)
//    {
//      ItemStack stack = new ItemStack(block_slab3,  1, i);
//      slab_stacks.put(SLAB3_METALS[i], stack);
//      GameRegistry.registerCustomItemStack("blockSlab" + SLAB3_METALS[i], stack);
//    }
  }
  
  static public void RegisterBlocks(Configuration config)
  {
   
    block_refractory_casing = new BlockRefractoryCasing();
    block_machine = new BlockFoundryMachine();
    block_metal1 = new BlockMetal() { public Variant[] getVariants() { return BLOCK1_METALS; } };
    block_metal2 = new BlockMetal() { public Variant[] getVariants() { return BLOCK2_METALS; } };
    block_ore = new BlockFoundryOre();
    block_alloy_furnace = new BlockAlloyFurnace();
    block_refractory_hopper = new BlockRefractoryHopper();

    GameRegistry.registerBlock(block_refractory_casing, "casing");
    GameRegistry.registerBlock(block_machine, ItemBlockMulti.class, "machine");
    GameRegistry.registerBlock(block_metal1, ItemBlockMulti.class, "blockMetal1");
    GameRegistry.registerBlock(block_metal2, ItemBlockMulti.class, "blockMetal2");
    GameRegistry.registerBlock(block_ore, ItemBlockMulti.class, "ore");
    GameRegistry.registerBlock(block_alloy_furnace, "alloyFurnace");
    GameRegistry.registerBlock(block_refractory_hopper, "refractoryHopper");

    RegisterHalfSlabs(config);
    
    block_metal_stairs = new HashMap<String,BlockStairs>();
    block_metal_stairs.put("Iron",new BlockMetalStairs(Blocks.iron_block.getDefaultState(),"Iron"));
    block_metal_stairs.put("Gold",new BlockMetalStairs(Blocks.gold_block.getDefaultState(),"Gold"));
    for(BlockMetal.Variant v:BLOCK1_METALS)
    {
      IBlockState state = block_metal1.getVariantState(v);
      block_metal_stairs.put(v.metal,new BlockMetalStairs(state,v.metal));
      ItemStack item = new ItemStack(block_metal1,1,block_metal1.getMetaFromState(state));
      block_stacks.put(v.metal, item);
      OreDictionary.registerOre(v.oredict, item);
    }
    for(BlockMetal.Variant v:BLOCK2_METALS)
    {
      IBlockState state = block_metal2.getVariantState(v);
      block_metal_stairs.put(v.metal,new BlockMetalStairs(state,v.metal));
      ItemStack item = new ItemStack(block_metal2,1,block_metal2.getMetaFromState(state));
      block_stacks.put(v.metal, item);
      OreDictionary.registerOre(v.oredict, item);
    }
    for(Map.Entry<String,BlockStairs> e:block_metal_stairs.entrySet())
    {
      GameRegistry.registerBlock(e.getValue(), "stairs" + e.getKey());
    }

    block_stacks.put("Iron", new ItemStack(Blocks.iron_block));
    block_stacks.put("Gold", new ItemStack(Blocks.gold_block));
    block_stacks.put("Glass", new ItemStack(Blocks.glass));
    block_stacks.put("Redstone", new ItemStack(Blocks.redstone_block));

    for(EnumDyeColor color:EnumDyeColor.values())
    {
      block_stacks.put(
          "Glass." + color.name(),
          new ItemStack(Blocks.stained_glass,1,color.getDyeDamage()));
    }
    
//    GameRegistry.registerCustomItemStack("refractoryCasing", new ItemStack(block_refractory_casing));
//    GameRegistry.registerCustomItemStack("machineAlloyFurnace", new ItemStack(block_alloy_furnace));
//    GameRegistry.registerCustomItemStack("machineICF", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_ICF));
//    GameRegistry.registerCustomItemStack("machineCaster", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_CASTER));
//    GameRegistry.registerCustomItemStack("machineAlloyMixer", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_ALLOYMIXER));
//    GameRegistry.registerCustomItemStack("machineInfuser", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_INFUSER));
//    GameRegistry.registerCustomItemStack("machineMaterialRouter", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_MATERIALROUTER));
//    GameRegistry.registerCustomItemStack("refractoryHopper", new ItemStack(block_refractory_hopper));
//    GameRegistry.registerCustomItemStack("machineAtomizer", new ItemStack(block_machine,1,BlockFoundryMachine.MACHINE_ATOMIZER));

    
    for(BlockFoundryOre.EnumOre v:BlockFoundryOre.EnumOre.values())
    {
      IBlockState state = block_ore.getDefaultState().withProperty(BlockFoundryOre.VARIANT, v);
      ItemStack stack = new ItemStack(block_ore,  1, block_ore.getMetaFromState(state));
      OreDictionary.registerOre(v.oredict_name,stack);
    }

  }
}
