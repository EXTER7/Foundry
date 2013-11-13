package exter.foundry.recipes.manager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import exter.foundry.api.recipe.manager.IMeltingRecipeManager;
import exter.foundry.recipes.MeltingRecipe;
import exter.foundry.util.FoundryMiscUtils;

public class MeltingRecipeManager implements IMeltingRecipeManager
{
  private Map<String,MeltingRecipe> recipes;
  
  public static final MeltingRecipeManager instance = new MeltingRecipeManager();

  private MeltingRecipeManager()
  {
    recipes = new HashMap<String,MeltingRecipe>();
  }
  /**
   * Find a valid recipe that contains the given item
   * @param item The item required in the recipe
   * @return
   */
  public MeltingRecipe FindRecipe(ItemStack item)
  {
    String od_name = null;
    for (String name : OreDictionary.getOreNames())
    {
      if (FoundryMiscUtils.IsItemInOreDictionary(name, item))
      {
        od_name = name;
        break;
      }
    }
    if(od_name == null)
    {
      return null;
    }
    return recipes.get(od_name);
  }
  
  @Override
  public void AddRecipe(String solid_name,FluidStack fluid_stack)
  {
    recipes.put(solid_name,new MeltingRecipe(solid_name,fluid_stack));
  }

}
