package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.api.recipe.manager.IAlloyFurnaceRecipeManager;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.recipes.AlloyFurnaceRecipe;

public class AlloyFurnaceRecipeManager implements IAlloyFurnaceRecipeManager
{
  public List<IAlloyFurnaceRecipe> recipes;

  public static final AlloyFurnaceRecipeManager instance = new AlloyFurnaceRecipeManager();
  
  private AlloyFurnaceRecipeManager()
  {
    recipes = new ArrayList<IAlloyFurnaceRecipe>();
  }
  
  @Override
  public void addRecipe(ItemStack out, IItemMatcher in_a,IItemMatcher in_b)
  {
    recipes.add(new AlloyFurnaceRecipe(out,in_a,in_b));
  }

  @Override
  public IAlloyFurnaceRecipe findRecipe(ItemStack in_a,ItemStack in_b)
  {
    for(IAlloyFurnaceRecipe r:recipes)
    {
      if(r.matchesRecipe(in_a,in_b))
      {
        return r;
      }
    }
    return null;
  }

  @Override
  public List<IAlloyFurnaceRecipe> getRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }

  @Override
  public void addRecipe(ItemStack out, IItemMatcher[] in_a, IItemMatcher[] in_b)
  {
    for(IItemMatcher a:in_a)
    {
      for(IItemMatcher b:in_b)
      {
        addRecipe(out, a, b);
      }
    }
  }

  @Override
  public void removeRecipe(IAlloyFurnaceRecipe recipe)
  {
    recipes.remove(recipe);    
  }
}
