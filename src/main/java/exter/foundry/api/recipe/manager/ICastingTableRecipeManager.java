package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ICastingTableRecipeManager
{

  /**
   * Register a Casting Table recipe.
   * @param result Item produced.
   * @param in_fluid Fluid required (fluid type and amount).
   * @param in_type Type of Casting Table required.
   */
  public void addRecipe(IItemMatcher result,FluidStack in_fluid,ICastingTableRecipe.TableType in_type);

  public List<ICastingTableRecipe> getRecipes();

  /**
   * Find a casting recipe given a FluidStack and a mold.
   * @param fluid FluidStack that contains the recipe's required fluid.
   * @param in_type Type of Casting Table used by the recipe.
   * @return The casting recipe, or null if no matching recipe.
   */
  public ICastingTableRecipe findRecipe(FluidStack fluid,ICastingTableRecipe.TableType in_type);

  /**
   * Removes a recipe.
   * @param The recipe to remove.
   */
  public void removeRecipe(ICastingTableRecipe recipe);
}
