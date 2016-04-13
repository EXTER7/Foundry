package exter.foundry.util;

import java.util.List;

import exter.foundry.api.FoundryUtils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;


/**
 * Miscellaneous utility methods
 */
public class FoundryMiscUtils
{
  static public int divCeil(int a,int b)
  {
    return a / b + ((a % b == 0) ? 0 : 1);
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
    return getModItemFromOreDictionary(modid, orename, 1);
  }

  static public ItemStack getModItemFromOreDictionary(String modid,String orename, int amount)
  {
    modid = modid.toLowerCase();
    for(ItemStack is:OreDictionary.getOres(orename))
    {
      if(is.getItem().getRegistryName().getResourceDomain().equals(modid))
      {
        is = is.copy();
        is.stackSize = amount;
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
    
  @SuppressWarnings("deprecation")
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

    if(state.getMaterial() == Material.WATER && Integer.valueOf(0).equals(state.getValue(BlockLiquid.LEVEL)))
    {
      if(do_drain)
      {
        world.setBlockToAir(pos);
      }
      return new FluidStack(FluidRegistry.WATER,FluidContainerRegistry.BUCKET_VOLUME);
    }

    if(state.getMaterial() == Material.LAVA && Integer.valueOf(0).equals(state.getValue(BlockLiquid.LEVEL)))
    {
      if(do_drain)
      {
        world.setBlockToAir(pos);
      }
      return new FluidStack(FluidRegistry.LAVA,FluidContainerRegistry.BUCKET_VOLUME);
    }
    return null;
  }
  
  @SideOnly(Side.CLIENT)
  static public void localizeTooltip(String key, List<String> tooltip)
  {
    for(String str:I18n.translateToLocal(key).split("//"))
    {
      tooltip.add(TextFormatting.GRAY + str);
    }
  }
}
