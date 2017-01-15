package exter.foundry.util;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.tileentity.TileEntityFoundry;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
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
  static private Random rand = new Random();
  
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

  static public Set<String> getAllItemOreDictionaryNames(ItemStack stack)
  {
    Set<String> result = new HashSet<String>();
    for(String name:OreDictionary.getOreNames())
    {
      List<ItemStack> ores = OreDictionary.getOres(name);
      for(ItemStack i : ores)
      {
        if(i.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(i, stack))
        {
          result.add(name);
        }
      }
    }
    return result;
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
        is.setCount(amount);
        return is;
      }
    }
    return ItemStack.EMPTY;
  }
  
  /**
   * Register item in the ore dictionary only if it's not already registered.
   * @param name Ore Dictionary name.
   * @param stack Item to register.
   */
  static public void registerInOreDictionary(String name,ItemStack stack)
  {
    if(stack.isEmpty())
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

    if(state.getMaterial() == Material.WATER && Integer.valueOf(0).equals(state.getValue(BlockLiquid.LEVEL)))
    {
      if(do_drain)
      {
        world.setBlockToAir(pos);
      }
      return new FluidStack(FluidRegistry.WATER,Fluid.BUCKET_VOLUME);
    }

    if(state.getMaterial() == Material.LAVA && Integer.valueOf(0).equals(state.getValue(BlockLiquid.LEVEL)))
    {
      if(do_drain)
      {
        world.setBlockToAir(pos);
      }
      return new FluidStack(FluidRegistry.LAVA,Fluid.BUCKET_VOLUME);
    }
    return null;
  }
  
  @SideOnly(Side.CLIENT)
  static public void localizeTooltip(String key, List<String> tooltip)
  {
    for(String str:(new TextComponentTranslation(key)).getUnformattedText().split("//"))
    {
      tooltip.add(TextFormatting.GRAY + str);
    }
  }
  
  static public void registerCasting(ItemStack item,Fluid liquid_metal,int ingots,ItemMold.SubItem mold_meta,ItemStack extra)
  {
    registerCasting(item,new FluidStack(liquid_metal, FoundryAPI.FLUID_AMOUNT_INGOT * ingots),mold_meta,extra);
  }

  static public void registerCasting(ItemStack item,FluidStack fluid,ItemMold.SubItem mold_meta,ItemStack extra)
  {
    if(!item.isEmpty())
    {
      ItemStack mold = FoundryItems.mold(mold_meta);
      if(CastingRecipeManager.instance.findRecipe(new FluidStack(fluid.getFluid(),FoundryAPI.CASTER_TANK_CAPACITY), mold, extra) == null)
      {
        IItemMatcher mextra = null;
        if(extra != null)
        {
          mextra = new ItemStackMatcher(extra);
        }
        CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(item), fluid, mold, mextra);
      }
    }
  }

  static public void breakTileEntityBlock(World world, BlockPos pos, IBlockState state)
  {
    TileEntity te = world.getTileEntity(pos);

    if(te != null && (te instanceof TileEntityFoundry) && !world.isRemote)
    {
      TileEntityFoundry tef = (TileEntityFoundry) te;
      int i;
      for(i = 0; i < tef.getSizeInventory(); i++)
      {
        ItemStack is = tef.getStackInSlot(i);

        if(!is.isEmpty())
        {
          double drop_x = (rand.nextFloat() * 0.3) + 0.35;
          double drop_y = (rand.nextFloat() * 0.3) + 0.35;
          double drop_z = (rand.nextFloat() * 0.3) + 0.35;
          EntityItem entityitem = new EntityItem(world, pos.getX() + drop_x, pos.getY() + drop_y, pos.getZ() + drop_z, is);
          entityitem.setPickupDelay(10);

          world.spawnEntity(entityitem);
        }
      }
    }
    world.removeTileEntity(pos);
  }
  
}
