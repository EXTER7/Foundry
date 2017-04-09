package exter.foundry.integration.minetweaker;

import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.integration.jei.AtomizerJEI;
import exter.foundry.recipes.AtomizerRecipe;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.Atomizer")
public class MTAtomizerHandler
{
  public static class AtomizerAction extends AddRemoveAction
  {    
    IAtomizerRecipe recipe;
    
    public AtomizerAction(IAtomizerRecipe recipe)
    {
      this.recipe = recipe;
    }
    
    @Override
    protected void add()
    {
      AtomizerRecipeManager.instance.recipes.add(recipe);
      MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new AtomizerJEI.Wrapper(recipe));
    }

    @Override
    protected void remove()
    {
      AtomizerRecipeManager.instance.recipes.remove(recipe);
      MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new AtomizerJEI.Wrapper(recipe));
    }

    @Override
    public String getRecipeType()
    {
      return "atomizer";
    }

    @Override
    public String getDescription()
    {
      return String.format("%s -> %s",
          MTHelper.getFluidDescription(recipe.getInput()),
          MTHelper.getItemDescription(recipe.getOutput()));
    }
  }

  @ZenMethod
  static public void addRecipe(IItemStack output,ILiquidStack input)
  {
    IAtomizerRecipe recipe = null;
    try
    {
      recipe = new AtomizerRecipe(
        new ItemStackMatcher(MineTweakerMC.getItemStack(output)),
        MineTweakerMC.getLiquidStack(input));
    } catch(IllegalArgumentException e)
    {
      MineTweakerAPI.logError("Invalid atomizer recipe: " + e.getMessage());
      return;
    }
    MineTweakerAPI.apply((new AtomizerAction(recipe).action_add));
  }

  @ZenMethod
  static public void removeRecipe(ILiquidStack input)
  {
    IAtomizerRecipe recipe = AtomizerRecipeManager.instance.findRecipe(
        MineTweakerMC.getLiquidStack(input));
    if(recipe == null)
    {
      MineTweakerAPI.logWarning("Atomizer recipe not found.");
      return;
    }
    MineTweakerAPI.apply((new AtomizerAction(recipe)).action_remove);
  }
}
