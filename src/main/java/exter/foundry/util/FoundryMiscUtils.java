package exter.foundry.util;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.api.FoundryUtils;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;


/**
 * Miscellaneous utility methods
 */
public class FoundryMiscUtils
{
  static public void RegisterMoldRecipe(int dv,ItemStack pattern)
  {
    if(pattern != null)
    {
      GameRegistry.addShapelessRecipe(new ItemStack(FoundryItems.item_mold,1,dv),
          new ItemStack(FoundryItems.item_component,1,ItemComponent.COMPONENT_BLANKMOLD), pattern);  
    }
  }

  static public void RegisterMoldRecipe(int dv,String oredict_pattern)
  {
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(FoundryItems.item_mold,1,dv),
      new ItemStack(FoundryItems.item_component,1,ItemComponent.COMPONENT_BLANKMOLD), oredict_pattern));  
  }

  static public void RegisterMoldSmelting(int clay,int mold)
  {
    FurnaceRecipes.smelting().func_151394_a/*addSmelting*/(
        new ItemStack(FoundryItems.item_mold, 1, clay),
        new ItemStack(FoundryItems.item_mold, 1, mold), 0.0f);
  }

  static public void RegisterOreSmelting(int ore,int ingot)
  {
    FurnaceRecipes.smelting().func_151394_a/*addSmelting*/(
        new ItemStack(FoundryBlocks.block_ore, 1, ore),
        new ItemStack(FoundryItems.item_ingot, 1, ingot), 0.0f);
  }

  static public String GetItemOreDictionaryName(ItemStack stack)
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

  static public ItemStack GetModItemFromOreDictionary(String modid,String orename)
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
  static public void RegisterInOreDictionary(String name,ItemStack stack)
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
    
  static public FluidStack DrainFluidFromWorld(World world,int x, int y, int z,boolean do_drain)
  {
    Block block = world.getBlock(x, y, z);
    if(block instanceof IFluidBlock)
    {
      IFluidBlock fluid_block = (IFluidBlock)block;
      if(!fluid_block.canDrain(world, x, y, z))
      {
        return null;
      }
      return fluid_block.drain(world, x, y, z, do_drain);
    }

    if(block.getMaterial() == Material.water && world.getBlockMetadata(x, y, z) == 0)
    {
      if(do_drain)
      {
        world.setBlockToAir(x, y, z);
      }
      return new FluidStack(FluidRegistry.WATER,FluidContainerRegistry.BUCKET_VOLUME);
    }

    if(block.getMaterial() == Material.lava && world.getBlockMetadata(x, y, z) == 0)
    {
      if(do_drain)
      {
        world.setBlockToAir(x, y, z);
      }
      return new FluidStack(FluidRegistry.LAVA,FluidContainerRegistry.BUCKET_VOLUME);
    }
    return null;
  }
  
}
