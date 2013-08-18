package exter.foundry;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.item.FoundryItems;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;


/**
 * Miscellaneous utility methods
 */
public class FoundryUtils
{
  static public void RegisterMoldRecipe(int dv,ItemStack pattern)
  {
    GameRegistry.addShapelessRecipe(new ItemStack(FoundryItems.item_mold,1,dv),
      new ItemStack(Block.blockClay), pattern);  
  }

  static public void RegisterMoldRecipe(int dv,String oredict_pattern)
  {
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(FoundryItems.item_mold,1,dv),
      new ItemStack(Block.blockClay), oredict_pattern));  
  }

  static public void RegisterMoldSmelting(int clay,int mold)
  {
    FurnaceRecipes.smelting().addSmelting(FoundryItems.item_mold.itemID, clay, new ItemStack(FoundryItems.item_mold, 1, mold), 0.0f);
  }

  static public boolean IsItemInOreDictionary(String name,ItemStack stack)
  {
    List<ItemStack> ores = OreDictionary.getOres(name);
    for(ItemStack i:ores)
    {
      if(i.isItemEqual(stack))
      {
        return true;
      }
    }
    return false;
  }

  //Register item in the ore dictionary only if its not already registered
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

}
