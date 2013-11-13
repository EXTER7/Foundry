package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.api.recipe.manager.IMeltingRecipeManager;
import exter.foundry.recipes.MeltingRecipe;
import exter.foundry.util.FoundryMiscUtils;

public class MeltingRecipeManager implements IMeltingRecipeManager
{
  private List<MeltingRecipe> recipes;
  
  public static final MeltingRecipeManager instance = new MeltingRecipeManager();

  private MeltingRecipeManager()
  {
    recipes = new ArrayList<MeltingRecipe>();
  }
  /**
   * Find a valid recipe that contains the given item
   * @param item The item required in the recipe
   * @return
   */
  public MeltingRecipe FindRecipe(ItemStack item)
  {
    String od_name = null;
    for (MeltingRecipe r : recipes)
    {
      if(r.IsItemMatch(item))
      {
        return (MeltingRecipe)r;
      }
    }
    return null;
  }
  
  @Override
  public void AddRecipe(String solid_name,FluidStack fluid_stack)
  {
    recipes.add(new MeltingRecipe(solid_name,fluid_stack));
  }
  
  @Override
  public List<? extends IMeltingRecipe> GetRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }

}
