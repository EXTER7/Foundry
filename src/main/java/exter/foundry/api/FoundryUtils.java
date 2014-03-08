package exter.foundry.api;

import java.util.List;

import exter.foundry.api.orestack.OreStack;
import exter.foundry.api.recipe.FoundryRecipes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class FoundryUtils
{

  /**
   * Maximum amount of substance a metal infuser can store.
   */
  static public final int INFUSER_SUBSTANCE_AMOUNT_MAX = 1000;

  /**
   * Tank capacity for machines.
   */
  static public final int ICF_TANK_CAPACITY = 6000;
  static public final int CASTER_TANK_CAPACITY = 6000;
  static public final int INFUSER_TANK_CAPACITY = 5000;
  static public final int ALLOYMIXER_TANK_CAPACITY = 2000;

  /**
   * Helper method for registering basic melting recipes for a given metal.
   * @param partial_name The partial ore dictionary name e.g. "Copper" for "ingotCopper","oreCopper", etc.
   * @param fluid The liquid created by the smelter.
   */
  static public void RegisterBasicMeltingRecipes(String partial_name,Fluid fluid)
  {
    if(FoundryRecipes.melting != null)
    {
      FoundryRecipes.melting.AddRecipe("ingot" + partial_name, new FluidStack(fluid, FoundryRecipes.FLUID_AMOUNT_INGOT));
      FoundryRecipes.melting.AddRecipe("block" + partial_name, new FluidStack(fluid, FoundryRecipes.FLUID_AMOUNT_BLOCK));
      FoundryRecipes.melting.AddRecipe("nugget" + partial_name, new FluidStack(fluid, FoundryRecipes.FLUID_AMOUNT_NUGGET));
      FoundryRecipes.melting.AddRecipe("dust" + partial_name, new FluidStack(fluid, FoundryRecipes.FLUID_AMOUNT_INGOT));
      FoundryRecipes.melting.AddRecipe("ore" + partial_name, new FluidStack(fluid, FoundryRecipes.FLUID_AMOUNT_ORE));
    }
  }


  /**
   * Check if an item is registered in the Ore Dictionary.
   * @param name Ore name to check.
   * @param stack Item to check.
   * @return true if the item is registered, false otherwise.
   */
  static public boolean IsItemInOreDictionary(String name,ItemStack stack)
  {
    List<ItemStack> ores = OreDictionary.getOres(name);
    for(ItemStack i:ores)
    {
      if(i.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(i, stack))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Compares ItemStack to various types of objects.
   * @param item Stack of item to compare
   * @param match object to compare. Can be of the following types: {@link String} (Ore Dictionary name), {@link ItemStack}, {@link Item}, {@link Block}.
   * @return true if the item matches, false otherwise.
   */
  static public boolean IsItemMatch(ItemStack item,Object match)
  {
    if(item == null)
    {
      return match == null;
    }
    if(match == null)
    {
      return false;
    }
    if(match instanceof String )
    {
      return IsItemInOreDictionary((String)match, item);
    }
    if(match instanceof OreStack )
    {
      return IsItemInOreDictionary(((OreStack)match).name, item);
    }
    if(match instanceof ItemStack)
    {
      ItemStack match_stack = (ItemStack)match;
      return item.isItemEqual(match_stack) && ItemStack.areItemStackTagsEqual(item, match_stack);
    }
    if(match instanceof Item)
    {
      return item.getItem() == (Item)match;
    }
    if(match instanceof Block)
    {
      return item.getItem() instanceof ItemBlock && ((ItemBlock)item.getItem()).field_150939_a == (Block)match;
    }
    return false;
  }
  
  /**
   * Get the stack size of an item.
   * @param stack to check. Can be of the following types: {@link String} (Ore Dictionary name), {@link ItemStack}, {@link Item}, {@link Block}.
   * @return the stack size
   */
  static public int GetStackSize(Object stack)
  {
    if(stack == null)
    {
      return 0;
    }
    if(stack instanceof String || stack instanceof Item || stack instanceof Block)
    {
      return 1;
    }
    if(stack instanceof OreStack )
    {
      return ((OreStack)stack).amount;
    }
    if(stack instanceof ItemStack)
    {
      return ((ItemStack)stack).stackSize;
    }
    return 0;
  }
}
