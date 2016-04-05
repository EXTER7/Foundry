package exter.foundry.api.recipe.manager;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;

public interface IMeltingRecipeManager
{
  /**
   * Register a Melting Crucible recipe.
   * @param solid The item to be melted
   * @param fluid_stack Resulting fluid
   * @param melting_point Temperature required for the item to melt. Must be >295 and <5000
   * @param melting_speed. Speed in which the item melts. Default is 100.
   */
  public void addRecipe(IItemMatcher solid,FluidStack fluid_stack,int melting_point,int melting_speed);
  
  /**
   * Register a Melting Crucible recipe.
   * @param solid The item to be melted
   * @param fluid_stack Resulting fluid
   * @param melting_point Temperature required for the item to melt. Must be >295 and <5000
   */
  public void addRecipe(IItemMatcher solid,FluidStack fluid_stack,int melting_point);

  /**
   * Register a Melting Crucible recipe.
   * Uses the fluid's temperature as it's melting point.
   * @param solid The item to be melted
   * @param fluid_stack Resulting fluid
   */
  public void addRecipe(IItemMatcher solid,FluidStack fluid_stack);

  /**
   * Get a list of all the recipes
   * @return List of all the recipes
   */
  public List<IMeltingRecipe> getRecipes();
  
  /**
   * Find a valid recipe that contains the given item
   * @param item The item required in the recipe
   * @return
   */
  public IMeltingRecipe findRecipe(ItemStack item);
  
  /**
   * Removes a recipe.
   * @param The recipe to remove.
   */
  public void removeRecipe(IMeltingRecipe recipe);
}
