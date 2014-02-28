package exter.foundry.api.recipe.manager;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.recipe.IMeltingRecipe;

public interface IMeltingRecipeManager
{
  
  /**
   * Register a Metal Smelter recipe
   * @param solid Can be an {@link ItemStack}, {@link Item}, {@link Block} of the item, or a {@link String} of the Ore Dictionary name of the item to be melted
   * @param fluid_stack Resulting fluid
   * @param melting_point Temperature required for the item to melt. Must be >295 and <5000
   */
  public void AddRecipe(Object solid,FluidStack fluid_stack,int melting_point);

  /**
   * Register a Metal Smelter recipe.
   * Uses the fluid's temperature as it's melting point.
   * @param solid Can be an {@link ItemStack}, {@link Item}, {@link Block} of the item, or a {@link String} of the Ore Dictionary name of the item to be melted
   * @param fluid_stack Resulting fluid
   */
  public void AddRecipe(Object solid,FluidStack fluid_stack);

  /**
   * Get a list of all the recipes
   * @return List of all the recipes
   */
  public List<IMeltingRecipe> GetRecipes();
  
  /**
   * Find a valid recipe that contains the given item
   * @param item The item required in the recipe
   * @return
   */
  public IMeltingRecipe FindRecipe(ItemStack item);
  
}
