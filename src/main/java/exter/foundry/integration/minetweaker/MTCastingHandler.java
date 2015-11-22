package exter.foundry.integration.minetweaker;

import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.recipes.CastingRecipe;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.Casting")
public class MTCastingHandler
{
  public static class CastingAction extends AddRemoveAction
  {
    
    ICastingRecipe recipe;
    
    public CastingAction(ICastingRecipe recipe)
    {
      this.recipe = recipe;
    }
    
    @Override
    protected void add()
    {
      if(recipe.RequiresExtra())
      {
        CastingRecipeManager.instance.recipes.add(0,recipe);
      } else
      {
        CastingRecipeManager.instance.recipes.add(recipe);
      }
    }

    @Override
    protected void remove()
    {
      MeltingRecipeManager.instance.recipes.remove(recipe);
    }

    @Override
    public String getRecipeType()
    {
      return "casting";
    }

    @Override
    public String getDescription()
    {
      return String.format("( %s, %s, %s ) -> %s",
          MTHelper.getDescription(recipe.GetInputFluid()),
          MTHelper.getDescription(recipe.GetInputMold()),
          MTHelper.getDescription(recipe.GetInputExtra()),
          MTHelper.getDescription(recipe.GetOutput()));
    }
  }

  @ZenMethod
  static public void addRecipe(IItemStack output,ILiquidStack input, IItemStack mold,@Optional IItemStack extra,@Optional int speed)
  {
    if(speed == 0)
    {
      speed = 100;
    }
    ICastingRecipe recipe = null;
    try
    {
      recipe = new CastingRecipe(
        (ItemStack)output.getInternal(),
        (FluidStack)input.getInternal(),
        (ItemStack)mold.getInternal(),
        extra == null?null:((ItemStack)extra.getInternal()),
        speed);
    } catch(IllegalArgumentException e)
    {
      MineTweakerAPI.logError("Invalid casting recipe: " + e.getMessage());
      return;
    }
    MineTweakerAPI.apply((new CastingAction(recipe).action_add));
  }

  @ZenMethod
  static public void removeRecipe(ILiquidStack input, IItemStack mold,@Optional IIngredient extra)
  {
    ICastingRecipe recipe = CastingRecipeManager.instance.FindRecipe((
        FluidStack)input.getInternal(),
        (ItemStack)mold.getInternal(),
        extra == null?null:((ItemStack)extra.getItems().get(0).getInternal()));
    if(recipe == null)
    {
      MineTweakerAPI.logWarning("Casting recipe not found.");
      return;
    }
    MineTweakerAPI.apply((new CastingAction(recipe)).action_remove);
  }
}
