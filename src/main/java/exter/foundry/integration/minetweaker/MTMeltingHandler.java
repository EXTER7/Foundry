package exter.foundry.integration.minetweaker;

import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.recipes.MeltingRecipe;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
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
    }

    @Override
    protected void remove()
    {
      MeltingRecipeManager.instance.recipes.remove(recipe);
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
          MTHelper.getDescription(recipe.GetInput()),
          MTHelper.getDescription(recipe.GetOutput()));
    }
  }

  @ZenMethod
  static public void addRecipe(ILiquidStack output,IIngredient input,@Optional int melting_point,@Optional int speed)
  {
    Object obj = input.getInternal();
    if(!((obj instanceof String) && (input instanceof IOreDictEntry)) && !(obj instanceof ItemStack))
    {
      return;
    }
    
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
      recipe = new MeltingRecipe(obj,(FluidStack)output.getInternal(), melting_point, speed);
    } catch(IllegalArgumentException e)
    {
      MineTweakerAPI.logError("Invalid melting recipe.");
      return;
    }
    MineTweakerAPI.apply((new MeltingAction(recipe).action_add));
  }

  @ZenMethod
  static public void removeRecipe(IIngredient input)
  {
    Object obj = input.getInternal();
    if(!((obj instanceof String) && (input instanceof IOreDictEntry)) && !(obj instanceof ItemStack))
    {
      return;
    }
    
    IMeltingRecipe recipe = null;
    if(obj instanceof String)
    {
      recipe = MeltingRecipeManager.instance.FindRecipe((String)obj);
    } else if(obj instanceof ItemStack)
    {
      recipe = MeltingRecipeManager.instance.FindRecipe((ItemStack)obj);
    }
    if(recipe == null)
    {
      MineTweakerAPI.logWarning("Melting recipe not found.");
      return;
    }
    MineTweakerAPI.apply((new MeltingAction(recipe)).action_remove);
  }
}
