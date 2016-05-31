package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.manager.ICastingTableRecipeManager;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.recipes.CastingTableRecipe;

public class CastingTableRecipeManager implements ICastingTableRecipeManager
{
  public Map<ICastingTableRecipe.TableType,Map<String,ICastingTableRecipe>> recipes;

  public static final CastingTableRecipeManager instance = new CastingTableRecipeManager();

  private CastingTableRecipeManager()
  {
    recipes = new EnumMap<ICastingTableRecipe.TableType,Map<String,ICastingTableRecipe>>(ICastingTableRecipe.TableType.class);
    for(ICastingTableRecipe.TableType type:ICastingTableRecipe.TableType.values())
    {
      recipes.put(type, new HashMap<String,ICastingTableRecipe>());
    }
  }

  @Override
  public void addRecipe(IItemMatcher result,FluidStack fluid,ICastingTableRecipe.TableType type)
  {
    ICastingTableRecipe recipe = new CastingTableRecipe(result,fluid,type);
    recipes.get(recipe.getTableType()).put(recipe.getInput().getFluid().getName(), recipe);
  }


  @Override
  public ICastingTableRecipe findRecipe(FluidStack fluid,ICastingTableRecipe.TableType type)
  {
    if(type == null || fluid == null || fluid.amount == 0)
    {
      return null;
    }
    ICastingTableRecipe recipe = recipes.get(type).get(fluid.getFluid().getName());
    return recipe;
  }

  @Override
  public List<ICastingTableRecipe> getRecipes()
  {
    List<ICastingTableRecipe> result = new ArrayList<ICastingTableRecipe>();
    for(ICastingTableRecipe.TableType type:ICastingTableRecipe.TableType.values())
    {
      result.addAll(recipes.get(type).values());
    }
    return result;
  }

  @Override
  public void removeRecipe(ICastingTableRecipe recipe)
  {
    recipes.get(recipe.getTableType()).remove(recipe.getInput().getFluid().getName());
  }
}
