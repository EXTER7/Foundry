package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.ICastingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ICastingRecipeManager
{

  /**
   * Register a Metal Caster recipe.
   * Note: the mold must be registered with {@link RegisterMold}.
   * @param result Item produced.
   * @param in_fluid Fluid required (fluid type and amount).
   * @param in_mold Mold required.
   * @param in_extra Extra item required (null, if no extra item is required).
   */
  public void AddRecipe(Object result,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra);

  /**
   * Register a Metal Caster recipe.
   * Note: the mold must be registered with {@link RegisterMold}.
   * @param result Item produced.
   * @param fluid Fluid required (fluid type and amount).
   * @param mold Mold required.
   * @param extra Ore Dictionary name of extra item required (null, if no extra item is required).
   * @param extra_amount Amount of extra item required.
   */
  public void AddRecipe(Object result,FluidStack fluid,ItemStack mold,String extra,int extra_amount);

  /**
   * Register an item as a mold. Only registered items are accepted in the Metal Caster's mold slot.
   * @param mold Item to be registered.
   */
  public void AddMold(ItemStack mold);
  
  /**
   * Get a list of all the recipes
   * @return List of all the recipes
   */
  public List<? extends ICastingRecipe> GetRecipes();
}
