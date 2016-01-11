package exter.foundry.integration.minetweaker;

import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.recipes.CastingRecipe;
import exter.foundry.recipes.manager.CastingRecipeManager;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
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
      if(recipe.requiresExtra())
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
      CastingRecipeManager.instance.recipes.remove(recipe);
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
          MTHelper.getDescription(recipe.getInput()),
          MTHelper.getDescription(recipe.getMold()),
          MTHelper.getDescription(recipe.getInputExtra()),
          MTHelper.getDescription(recipe.getOutput()));
    }
  }

  public static class MoldAction extends AddRemoveAction
  {
    
    ItemStack mold;
    
    public MoldAction(ItemStack mold)
    {
      this.mold = mold;
    }
    
    @Override
    protected void add()
    {
      CastingRecipeManager.instance.molds.add(mold);
    }

    @Override
    protected void remove()
    {
      CastingRecipeManager.instance.molds.remove(mold);
    }

    @Override
    public String getRecipeType()
    {
      return "casting mold";
    }

    @Override
    public String getDescription()
    {
      return String.format("%s",
          MTHelper.getDescription(mold));
    }
  }

  @ZenMethod
  static public void addRecipe(IItemStack output,ILiquidStack input, IItemStack mold,@Optional IIngredient extra,@Optional int speed)
  {
    if(speed == 0)
    {
      speed = 100;
    }
    ICastingRecipe recipe = null;
    try
    {
      recipe = new CastingRecipe(
        MineTweakerMC.getItemStack(output),
        MineTweakerMC.getLiquidStack(input),
        MineTweakerMC.getItemStack(mold),
        MTHelper.getIngredient(extra),
        speed);
    } catch(IllegalArgumentException e)
    {
      MineTweakerAPI.logError("Invalid casting recipe: " + e.getMessage());
      return;
    }
    MineTweakerAPI.apply((new CastingAction(recipe).action_add));
  }

  
  @ZenMethod
  static public void removeRecipe(ILiquidStack input, IItemStack mold,@Optional IItemStack extra)
  {
    ICastingRecipe recipe = CastingRecipeManager.instance.findRecipe(
        MineTweakerMC.getLiquidStack(input),
        MineTweakerMC.getItemStack(mold),
        MineTweakerMC.getItemStack(extra));
    if(recipe == null)
    {
      MineTweakerAPI.logWarning("Casting recipe not found.");
      return;
    }
    MineTweakerAPI.apply((new CastingAction(recipe)).action_remove);
  }
  
  @ZenMethod
  static public void addMold(IItemStack mold)
  {
    ItemStack molditem = MineTweakerMC.getItemStack(mold);
    if(molditem == null)
    {
      MineTweakerAPI.logError("Invalid mold item");
      return;
    }
    MineTweakerAPI.apply((new MoldAction(molditem).action_add));
  }

  @ZenMethod
  static public void removeMold(IItemStack mold)
  {
    ItemStack molditem = MineTweakerMC.getItemStack(mold);
    if(molditem == null)
    {
      MineTweakerAPI.logWarning("Invalid mold item");
      return;
    }
    for(ItemStack m : CastingRecipeManager.instance.molds)
    {
      if(m.isItemEqual(molditem) && ItemStack.areItemStacksEqual(m, molditem))
      {
        MineTweakerAPI.apply((new MoldAction(m)).action_remove);
        return;
      }
    }
    MineTweakerAPI.logWarning("Mold not found.");
  }
}
