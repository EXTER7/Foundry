package exter.foundry.util;

import java.util.List;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;


/**
 * Miscellaneous utility methods
 */
public class FoundryMiscUtils
{
  static public int divCeil(int a,int b)
  {
    return a / b + ((a % b == 0) ? 0 : 1);
  }

  @Deprecated
  static public void registerMoldRecipe(ItemMold.SubItem sub,ItemStack pattern)
  {
    if(pattern != null)
    {
      GameRegistry.addShapelessRecipe(FoundryItems.mold(sub),
          FoundryItems.component(ItemComponent.SubItem.BLANKMOLD), pattern);  
    }
  }

  @Deprecated
  static public void registerMoldRecipe(ItemMold.SubItem sub,String oredict_pattern)
  {
    GameRegistry.addRecipe(new ShapelessOreRecipe(FoundryItems.mold(sub),
      FoundryItems.component(ItemComponent.SubItem.BLANKMOLD), oredict_pattern));  
  }

  @Deprecated
  static public void registerMoldSmelting(ItemMold.SubItem clay,ItemMold.SubItem mold)
  {
    GameRegistry.addSmelting(
        FoundryItems.mold(clay),
        FoundryItems.mold(mold), 0.0f);
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

  @SuppressWarnings("deprecation")
  static public ItemStack getModItemFromOreDictionary(String modid,String orename, int amount)
  {
    modid = modid.toLowerCase();
    for(ItemStack is:OreDictionary.getOres(orename))
    {
      if(GameRegistry.findUniqueIdentifierFor(is.getItem()).modId.equals(modid))
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

    if(state.getMaterial() == Material.water && Integer.valueOf(0).equals(state.getValue(BlockLiquid.LEVEL)))
    {
      if(do_drain)
      {
        world.setBlockToAir(pos);
      }
      return new FluidStack(FluidRegistry.WATER,FluidContainerRegistry.BUCKET_VOLUME);
    }

    if(state.getMaterial() == Material.lava && Integer.valueOf(0).equals(state.getValue(BlockLiquid.LEVEL)))
    {
      if(do_drain)
      {
        world.setBlockToAir(pos);
      }
      return new FluidStack(FluidRegistry.LAVA,FluidContainerRegistry.BUCKET_VOLUME);
    }
    return null;
  }

  static public void registerCasting(ItemStack item,Fluid liquid_metal,int ingots,int mold_meta,ItemStack extra)
  {
    registerCasting(item,new FluidStack(liquid_metal, FoundryAPI.FLUID_AMOUNT_INGOT * ingots),mold_meta,extra);
  }

  static public void registerCasting(ItemStack item,FluidStack fluid,int mold_meta,ItemStack extra)
  {
    if(item != null)
    {
      ItemStack mold = new ItemStack(FoundryItems.item_mold, 1, mold_meta);
      if(CastingRecipeManager.instance.findRecipe(new FluidStack(fluid.getFluid(),FoundryAPI.CASTER_TANK_CAPACITY), mold, extra) == null)
      {
        CastingRecipeManager.instance.addRecipe(item, fluid, mold, extra);
      }
    }
  }
  
  @Deprecated
  static public void registerPlateMoldRecipe(ItemStack item,String oredict_name)
  {
    if(FoundryUtils.isItemInOreDictionary(oredict_name, item))
    {
      FoundryMiscUtils.registerMoldRecipe(ItemMold.SubItem.PLATE_SOFT, oredict_name);
    } else
    {
      FoundryMiscUtils.registerMoldRecipe(ItemMold.SubItem.PLATE_SOFT, item);
    }
  }
}
