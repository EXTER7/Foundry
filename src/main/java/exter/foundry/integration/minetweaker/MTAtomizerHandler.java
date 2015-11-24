package exter.foundry.integration.minetweaker;

import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.recipes.AtomizerRecipe;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
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
    }

    @Override
    protected void remove()
    {
      AtomizerRecipeManager.instance.recipes.remove(recipe);
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
          MTHelper.getDescription(recipe.GetInputFluid()),
          MTHelper.getDescription(recipe.GetOutput()));
    }
  }

  @ZenMethod
  static public void addRecipe(IItemStack output,ILiquidStack input)
  {
    Object out = MineTweakerMC.getItemStack(output);
    IAtomizerRecipe recipe = null;
    try
    {
      recipe = new AtomizerRecipe(
        out,
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
    IAtomizerRecipe recipe = AtomizerRecipeManager.instance.FindRecipe(
        MineTweakerMC.getLiquidStack(input));
    if(recipe == null)
    {
      MineTweakerAPI.logWarning("Atomizer recipe not found.");
      return;
    }
    MineTweakerAPI.apply((new AtomizerAction(recipe)).action_remove);
  }
}
