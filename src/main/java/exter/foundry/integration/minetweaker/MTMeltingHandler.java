package exter.foundry.integration.minetweaker;

import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.integration.jei.MeltingJEI;
import exter.foundry.recipes.MeltingRecipe;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.Melting")
public class MTMeltingHandler
{
  public static class MeltingAction extends AddRemoveAction
  {
    
    IMeltingRecipe recipe;
    
    public MeltingAction(IMeltingRecipe recipe)
    {
      this.recipe = recipe;
    }
    
    @Override
    protected void add()
    {
      MeltingRecipeManager.instance.recipes.add(recipe);
      MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new MeltingJEI.Wrapper(null,recipe));
    }

    @Override
    protected void remove()
    {
      MeltingRecipeManager.instance.recipes.remove(recipe);
      MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new MeltingJEI.Wrapper(null, recipe));
    }

    @Override
    public String getRecipeType()
    {
      return "melting";
    }

    @Override
    public String getDescription()
    {
      return String.format(" %s -> %s",
          MTHelper.getItemDescription(recipe.getInput()),
          MTHelper.getFluidDescription(recipe.getOutput()));
    }
  }

  @ZenMethod
  static public void addRecipe(ILiquidStack output,IIngredient input,@Optional int melting_point,@Optional int speed)
  {
    
    if(melting_point == 0)
    {
      melting_point = output.getTemperature();
    }
    if(speed == 0)
    {
      speed = 100;
    }
    IMeltingRecipe recipe = null;
    try
    {
      recipe = new MeltingRecipe(
          MTHelper.getIngredient(input),
          MineTweakerMC.getLiquidStack(output),
          melting_point, speed);
    } catch(IllegalArgumentException e)
    {
      MineTweakerAPI.logError("Invalid melting recipe.");
      return;
    }
    MineTweakerAPI.apply((new MeltingAction(recipe).action_add));
  }

  @ZenMethod
  static public void removeRecipe(IItemStack input)
  {
    IMeltingRecipe recipe = MeltingRecipeManager.instance.findRecipe(
        MineTweakerMC.getItemStack(input));
    if(recipe == null)
    {
      MineTweakerAPI.logWarning("Melting recipe not found.");
      return;
    }
    MineTweakerAPI.apply((new MeltingAction(recipe)).action_remove);
  }
}
