package exter.foundry.integration.minetweaker;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.integration.jei.AlloyFurnaceJEI;
import exter.foundry.recipes.AlloyFurnaceRecipe;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.AlloyFurnace")
public class MTAlloyFurnaceHandler
{
  public static class AlloyFurnaceAction extends AddRemoveAction
  {
    
    IAlloyFurnaceRecipe recipe;
    
    public AlloyFurnaceAction(IAlloyFurnaceRecipe recipe)
    {
      this.recipe = recipe;
    }
    
    @Override
    protected void add()
    {
      AlloyFurnaceRecipeManager.instance.recipes.add(recipe);
      MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new AlloyFurnaceJEI.Wrapper(recipe));
    }

    @Override
    protected void remove()
    {
      AlloyFurnaceRecipeManager.instance.recipes.remove(recipe);
      MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new AlloyFurnaceJEI.Wrapper(recipe));
    }

    @Override
    public String getRecipeType()
    {
      return "alloy furnace";
    }

    @Override
    public String getDescription()
    {
      return String.format(" ( %s, %s ) -> %s",
          MTHelper.getItemDescription(recipe.getInputA()),
          MTHelper.getItemDescription(recipe.getInputB()),
          MTHelper.getItemDescription(recipe.getOutput()));
    }
  }
  
  @ZenMethod
  static public void addRecipe(IItemStack output,IIngredient input_a,IIngredient input_b)
  {
    IAlloyFurnaceRecipe recipe = null;
    try
    {
      recipe = new AlloyFurnaceRecipe(
          MineTweakerMC.getItemStack(output),
          MTHelper.getIngredient(input_a),
          MTHelper.getIngredient(input_b));
    } catch(IllegalArgumentException e)
    {
      MineTweakerAPI.logError("Invalid alloy furnace recipe: " + e.getMessage());
      return;
    }
    MineTweakerAPI.apply((new AlloyFurnaceAction(recipe).action_add));
  }

  @ZenMethod
  static public void removeRecipe(IItemStack input_a,IItemStack input_b)
  {
    
    IAlloyFurnaceRecipe recipe = AlloyFurnaceRecipeManager.instance.findRecipe(
        MineTweakerMC.getItemStack(input_a),
        MineTweakerMC.getItemStack(input_b));
    if(recipe == null)
    {
      MineTweakerAPI.logWarning("Alloy furnace recipe not found.");
      return;
    }
    MineTweakerAPI.apply((new AlloyFurnaceAction(recipe)).action_remove);
  }
}
