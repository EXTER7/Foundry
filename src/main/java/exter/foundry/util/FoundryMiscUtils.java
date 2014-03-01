package exter.foundry.util;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.api.orestack.OreStack;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;


/**
 * Miscellaneous utility methods
 */
public class FoundryMiscUtils
{
  static public void RegisterMoldRecipe(int dv,ItemStack pattern)
  {
    GameRegistry.addShapelessRecipe(new ItemStack(FoundryItems.item_mold,1,dv),
      new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_BLANKMOLD), pattern);  
  }

  static public void RegisterMoldRecipe(int dv,String oredict_pattern)
  {
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(FoundryItems.item_mold,1,dv),
      new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_BLANKMOLD), oredict_pattern));  
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
    if(!IsItemInOreDictionary(name,stack))
    {
      OreDictionary.registerOre(name, stack);
    }
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
