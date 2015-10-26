package exter.foundry.block;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import exter.foundry.item.ItemBlockMulti;
import exter.foundry.item.ItemBlockSlab;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
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

  public static BlockMetal[] block_metal;
  public static BlockFoundryOre block_ore;
  
  public static BlockMetalSlab[] block_slab;

  public static BlockMetalSlab[] block_slabdouble;
  
  public static Map<String,BlockStairs> block_metal_stairs;
  
  public static BlockAlloyFurnace block_alloy_furnace;
  
  public static BlockRefractoryHopper block_refractory_hopper;

  //All blocks mapped by the metal name.
  public static Map<String,ItemStack> block_stacks = new HashMap<String,ItemStack>();
  
  //All slabs mapped by the metal name.
  public static Map<String,ItemStack> slab_stacks = new HashMap<String,ItemStack>();
  
  
  static private void registerHalfSlabs(Configuration config)
  {
    int i;
    block_slab = new BlockMetalSlab[3];
    block_slab[0] = new BlockMetalSlab(null) { @Override public Variant[] getVariants() { return SLAB1_METALS; } };
    block_slab[1] = new BlockMetalSlab(null) { @Override public Variant[] getVariants() { return SLAB2_METALS; } };
    block_slab[2] = new BlockMetalSlab(null) { @Override public Variant[] getVariants() { return SLAB3_METALS; } };

    block_slabdouble = new BlockMetalSlab[3];
    block_slabdouble[0] = new BlockMetalSlab(block_slab[0]) {
      @Override public Variant[] getVariants() { return SLAB1_METALS; }
      @Override public IProperty getVariantProperty() { return block_slab[0].getVariantProperty(); } };
    block_slabdouble[1] = new BlockMetalSlab(block_slab[1]) {
      @Override public Variant[] getVariants() { return SLAB2_METALS; }
      @Override public IProperty getVariantProperty() { return block_slab[1].getVariantProperty(); } };
    block_slabdouble[2] = new BlockMetalSlab(block_slab[2]) {
      @Override public Variant[] getVariants() { return SLAB3_METALS; }
      @Override public IProperty getVariantProperty() { return block_slab[2].getVariantProperty(); } };

    for(i = 0; i < block_slab.length; i++)
    {
      BlockSlab slab = block_slab[i];
      ImmutablePair<BlockSlab,Object> slabdouble = new ImmutablePair<BlockSlab,Object>(block_slabdouble[i],null);
      GameRegistry.registerBlock(slab, ItemBlockSlab.class, "slabMetal" + (i + 1), slabdouble);
      GameRegistry.registerBlock(slabdouble.left, ItemBlockSlab.class, "slabMetalDouble" + (i + 1), slabdouble);
    }
  }
  
  static public void registerBlocks(Configuration config)
  {
    int i;
    block_refractory_casing = new BlockRefractoryCasing();
    block_machine = new BlockFoundryMachine();
    block_metal = new BlockMetal[2];
    block_metal[0] = new BlockMetal() { public Variant[] getVariants() { return BLOCK1_METALS; } };
    block_metal[1] = new BlockMetal() { public Variant[] getVariants() { return BLOCK2_METALS; } };
    block_ore = new BlockFoundryOre();
    block_alloy_furnace = new BlockAlloyFurnace();
    block_refractory_hopper = new BlockRefractoryHopper();

    block_metal_stairs = new HashMap<String,BlockStairs>();
    for(i = 0; i < block_metal.length; i++)
    {
      
      GameRegistry.registerBlock(block_metal[i], ItemBlockMulti.class, "blockMetal" + (i + 1));
      for(BlockMetal.Variant v:block_metal[i].getVariants())
      {
        IBlockState state = block_metal[i].getVariantState(v);
        block_metal_stairs.put(v.metal,new BlockMetalStairs(state,v.metal));
        ItemStack item = new ItemStack(block_metal[i],1,block_metal[i].getMetaFromState(state));
        block_stacks.put(v.metal, item);
        OreDictionary.registerOre(v.oredict, item);
      }
    }

    GameRegistry.registerBlock(block_refractory_casing, "casing");
    GameRegistry.registerBlock(block_machine, ItemBlockMulti.class, "machine");
    GameRegistry.registerBlock(block_ore, ItemBlockMulti.class, "ore");
    GameRegistry.registerBlock(block_alloy_furnace, "alloyFurnace");
    GameRegistry.registerBlock(block_refractory_hopper, "refractoryHopper");

    registerHalfSlabs(config);
    
    block_metal_stairs.put("Iron",new BlockMetalStairs(Blocks.iron_block.getDefaultState(),"Iron"));
    block_metal_stairs.put("Gold",new BlockMetalStairs(Blocks.gold_block.getDefaultState(),"Gold"));

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
