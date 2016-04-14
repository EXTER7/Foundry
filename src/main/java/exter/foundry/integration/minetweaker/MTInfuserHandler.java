package exter.foundry.integration.minetweaker;

import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.recipes.InfuserRecipe;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.Infuser")
public class MTInfuserHandler
{
  public static class InfuserAction extends AddRemoveAction
  {
    
    IInfuserRecipe recipe;
    
    public InfuserAction(IInfuserRecipe recipe)
    {
      this.recipe = recipe;
    }
    
    @Override
    protected void add()
    {
      InfuserRecipeManager.instance.recipes.add(recipe);
    }

    @Override
    protected void remove()
    {
      InfuserRecipeManager.instance.recipes.remove(recipe);
    }

    @Override
    public String getRecipeType()
    {
      return "infuser";
    }

    @Override
    public String getDescription()
    {
      return String.format("( %s, %s ) -> %s",
          MTHelper.getFluidDescription(recipe.getInputFluid()),
          MTHelper.getItemDescription(recipe.getInput()),
          MTHelper.getFluidDescription(recipe.getOutput()));
    }
  }

  @ZenMethod
  static public void addRecipe(ILiquidStack output,ILiquidStack input, IIngredient substance,int energy)
  {
    IInfuserRecipe recipe = null;
    try
    {
      recipe = new InfuserRecipe(
          MineTweakerMC.getLiquidStack(output),
          MineTweakerMC.getLiquidStack(input),
          MTHelper.getIngredient(substance),
          energy);
    } catch(IllegalArgumentException e)
    {
      MineTweakerAPI.logError("Invalid infuser recipe: " + e.getMessage());
      return;
    }
    MineTweakerAPI.apply((new InfuserAction(recipe).action_add));
  }

  @ZenMethod
  static public void removeRecipe(ILiquidStack input, IItemStack substance)
  {
    IInfuserRecipe recipe = InfuserRecipeManager.instance.findRecipe(
        MineTweakerMC.getLiquidStack(input),
        MineTweakerMC.getItemStack(substance));
    if(recipe == null)
    {
      MineTweakerAPI.logWarning("Infuser recipe not found.");
      return;
    }
    MineTweakerAPI.apply((new InfuserAction(recipe)).action_remove);
  }
}
