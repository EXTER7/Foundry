package exter.foundry.api.recipe.manager;

import java.util.Collection;
import java.util.Collections;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import exter.foundry.recipes.MeltingRecipe;
import exter.foundry.util.FoundryMiscUtils;

public interface IMeltingRecipeManager
{
  
  /**
   * Register a Metal Smelter recipe
   * @param solid_name Item to be melted
   * @param fluid_stack Resulting fluid
   */
  public void AddRecipe(String solid_name,FluidStack fluid_stack);
  
}
