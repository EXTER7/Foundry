package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.api.recipe.manager.IAlloyFurnaceRecipeManager;
import exter.foundry.recipes.AlloyFurnaceRecipe;

public class AlloyFurnaceRecipeManager implements IAlloyFurnaceRecipeManager
{
  private List<IAlloyFurnaceRecipe> recipes;

  public static final AlloyFurnaceRecipeManager instance = new AlloyFurnaceRecipeManager();
  
  private AlloyFurnaceRecipeManager()
  {
    recipes = new ArrayList<IAlloyFurnaceRecipe>();
  }
  
  @Override
  public void AddRecipe(ItemStack out, Object in_a,Object in_b)
  {
    recipes.add(new AlloyFurnaceRecipe(out,in_a,in_b));
  }

  @Override
  public IAlloyFurnaceRecipe FindRecipe(ItemStack in_a,ItemStack in_b)
  {
    for(IAlloyFurnaceRecipe r:recipes)
    {
      if(r.MatchesRecipe(in_a,in_b))
      {
        return r;
      }
    }
    return null;
  }

  @Override
  public List<IAlloyFurnaceRecipe> GetRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }

  @Override
  public void AddRecipe(ItemStack out, Object[] in_a, Object[] in_b)
  {
    for(Object a:in_a)
    {
      for(Object b:in_b)
      {
        AddRecipe(out, a, b);
      }
    }
  }
}
