package exter.foundry.util;

import java.util.List;

import exter.foundry.api.FoundryUtils;
import exter.foundry.block.BlockFoundryOre;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;


/**
 * Miscellaneous utility methods
 */
public class FoundryMiscUtils
{
  static public void registerMoldRecipe(int dv,ItemStack pattern)
  {
    if(pattern != null)
    {
      GameRegistry.addShapelessRecipe(new ItemStack(FoundryItems.item_mold,1,dv),
          new ItemStack(FoundryItems.item_component,1,ItemComponent.COMPONENT_BLANKMOLD), pattern);  
    }
  }

  static public void registerMoldRecipe(int dv,String oredict_pattern)
  {
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(FoundryItems.item_mold,1,dv),
      new ItemStack(FoundryItems.item_component,1,ItemComponent.COMPONENT_BLANKMOLD), oredict_pattern));  
  }

  static public void registerMoldSmelting(int clay,int mold)
  {
    GameRegistry.addSmelting(
        new ItemStack(FoundryItems.item_mold, 1, clay),
        new ItemStack(FoundryItems.item_mold, 1, mold), 0.0f);
  }

  static public void registerOreSmelting(BlockFoundryOre.EnumOre ore,int ingot)
  {
    GameRegistry.addSmelting(
        FoundryBlocks.block_ore.asItemStack(ore),
        new ItemStack(FoundryItems.item_ingot, 1, ingot), 0.0f);
  }

  static public String getItemOreDictionaryName(ItemStack stack)
  {
    for(String name:OreDictionary.getOreNames())
    {
      List<ItemStack> ores = OreDictionary.getOres(name);
      for(ItemStack i : ores)
      {
        if(i.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(i, stack))
        {
          return name;
        }
      }
    }
    return null;
  }

  static public ItemStack getModItemFromOreDictionary(String modid,String orename)
  {
    for(ItemStack is:OreDictionary.getOres(orename))
    {
      if(GameRegistry.findUniqueIdentifierFor(is.getItem()).modId.equals(modid))
      {
        is = is.copy();
        is.stackSize = 1;
        return is;
      }
    }
    return null;
  }
  
  /**
   * Register item in the ore dictionary only if it's not already registered.
   * @param name Ore Dictionary name.
   * @param stack Item to register.
   */
  static public void registerInOreDictionary(String name,ItemStack stack)
  {
    if(stack == null)
    {
      return;
    }
    if(!FoundryUtils.isItemInOreDictionary(name,stack))
    {
      OreDictionary.registerOre(name, stack);
    }
  }
    
  static public FluidStack drainFluidFromWorld(World world,BlockPos pos,boolean do_drain)
  {
    IBlockState state = world.getBlockState(pos);
    if(state.getBlock() instanceof IFluidBlock)
    {
      IFluidBlock fluid_block = (IFluidBlock)state;
      if(!fluid_block.canDrain(world, pos))
      {
        return null;
      }
      return fluid_block.drain(world, pos, do_drain);
    }

    if(state.getBlock().getMaterial() == Material.water && Integer.valueOf(0).equals(state.getValue(BlockLiquid.LEVEL)))
    {
      if(do_drain)
      {
        world.setBlockToAir(pos);
      }
      return new FluidStack(FluidRegistry.WATER,FluidContainerRegistry.BUCKET_VOLUME);
    }

    if(state.getBlock().getMaterial() == Material.lava && Integer.valueOf(0).equals(state.getValue(BlockLiquid.LEVEL)))
    {
      if(do_drain)
      {
        world.setBlockToAir(pos);
      }
      return new FluidStack(FluidRegistry.LAVA,FluidContainerRegistry.BUCKET_VOLUME);
    }
    return null;
  }
  
}
