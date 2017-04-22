package exter.foundry.api;

import java.util.List;

import exter.foundry.api.recipe.matcher.OreMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class FoundryUtils
{


  /**
   * Helper method for registering basic melting recipes for a given metal.
   * @param partial_name The partial ore dictionary name e.g. "Copper" for "ingotCopper","oreCopper", etc.
   * @param fluid The liquid created by the smelter.
   */
  static public void registerBasicMeltingRecipes(String partial_name,Fluid fluid)
  {
    if(FoundryAPI.recipes_melting != null)
    {
      FoundryAPI.recipes_melting.addRecipe(new OreMatcher("ingot" + partial_name), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT));
      FoundryAPI.recipes_melting.addRecipe(new OreMatcher("block" + partial_name), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_BLOCK));
      FoundryAPI.recipes_melting.addRecipe(new OreMatcher("nugget" + partial_name), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_NUGGET));
      FoundryAPI.recipes_melting.addRecipe(new OreMatcher("dust" + partial_name), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT));
      FoundryAPI.recipes_melting.addRecipe(new OreMatcher("ore" + partial_name), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_ORE));
      FoundryAPI.recipes_melting.addRecipe(new OreMatcher("orePoor" + partial_name), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_NUGGET * 2));
      FoundryAPI.recipes_melting.addRecipe(new OreMatcher("dustSmall" + partial_name), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT / 4));
      FoundryAPI.recipes_melting.addRecipe(new OreMatcher("dustTiny" + partial_name), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT / 4));
    }
  }


  /**
   * Check if an item is registered in the Ore Dictionary.
   * @param name Ore name to check.
   * @param stack Item to check.
   * @return true if the item is registered, false otherwise.
   */
  static public boolean isItemInOreDictionary(String name,ItemStack stack)
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
}
