package exter.foundry.integration.minetweaker;

import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.api.recipe.IInfuserSubstanceRecipe;
import exter.foundry.integration.minetweaker.substance.IInfuserSubstance;
import exter.foundry.recipes.InfuserRecipe;
import exter.foundry.recipes.InfuserSubstanceRecipe;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
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
          MTHelper.getDescription(recipe.GetInputFluid()),
          MTHelper.getDescription(recipe.GetInputSubstance()),
          MTHelper.getDescription(recipe.GetOutput()));
    }
  }

  public static class InfuserSubstanceAction extends AddRemoveAction
  {
    
    IInfuserSubstanceRecipe recipe;
    
    public InfuserSubstanceAction(IInfuserSubstanceRecipe recipe)
    {
      this.recipe = recipe;
    }
    
    @Override
    protected void add()
    {
      InfuserRecipeManager.instance.substance_recipes.add(recipe);
    }

    @Override
    protected void remove()
    {
      InfuserRecipeManager.instance.substance_recipes.remove(recipe);
    }

    @Override
    public String getRecipeType()
    {
      return "infuser";
    }

    @Override
    public String getDescription()
    {
      return String.format("%s -> %s",
          MTHelper.getDescription(recipe.GetInputItem()),
          MTHelper.getDescription(recipe.GetOutputSubstance()));
    }
  }

  @ZenMethod
  static public void addRecipe(ILiquidStack output,ILiquidStack input, IInfuserSubstance substance)
  {
    IInfuserRecipe recipe = null;
    try
    {
      recipe = new InfuserRecipe(
          MineTweakerMC.getLiquidStack(output),
          MineTweakerMC.getLiquidStack(input),
          substance.getSubstance());
    } catch(IllegalArgumentException e)
    {
      MineTweakerAPI.logError("Invalid infuser recipe: " + e.getMessage());
      return;
    }
    MineTweakerAPI.apply((new InfuserAction(recipe).action_add));
  }

  @ZenMethod
  static public void removeRecipe(ILiquidStack input, IInfuserSubstance substance)
  {
    IInfuserRecipe recipe = InfuserRecipeManager.instance.FindRecipe(
        MineTweakerMC.getLiquidStack(input),
        substance.getSubstance());
    if(recipe == null)
    {
      MineTweakerAPI.logWarning("Infuser recipe not found.");
      return;
    }
    MineTweakerAPI.apply((new InfuserAction(recipe)).action_remove);
  }
  
  @ZenMethod
  static public void addSubstanceRecipe(IInfuserSubstance output,IIngredient input,int energy)
  {
    IInfuserSubstanceRecipe recipe = null;

    try
    {
      recipe = new InfuserSubstanceRecipe(
        output.getSubstance(),
        MTHelper.getIngredient(input),
        energy);
    } catch(IllegalArgumentException e)
    {
      MineTweakerAPI.logError("Invalid infuser substance recipe: " + e.getMessage());
      return;
    }
    MineTweakerAPI.apply((new InfuserSubstanceAction(recipe).action_add));
  }

  @ZenMethod
  static public void removeSubstanceRecipe(IItemStack input)
  {
    IInfuserSubstanceRecipe recipe = InfuserRecipeManager.instance.FindSubstanceRecipe(
        MineTweakerMC.getItemStack(input));
    if(recipe == null)
    {
      MineTweakerAPI.logWarning("Infuser substance recipe not found.");
      return;
    }
    MineTweakerAPI.apply((new InfuserSubstanceAction(recipe)).action_remove);
  }

}
