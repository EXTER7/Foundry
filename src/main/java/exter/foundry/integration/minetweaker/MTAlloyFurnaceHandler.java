package exter.foundry.integration.minetweaker;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.recipes.AlloyFurnaceRecipe;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
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
    }

    @Override
    protected void remove()
    {
      AlloyFurnaceRecipeManager.instance.recipes.remove(recipe);
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
          MTHelper.getDescription(recipe.GetInputA()),
          MTHelper.getDescription(recipe.GetInputB()),
          MTHelper.getDescription(recipe.GetOutput()));
    }
  }
  
  @ZenMethod
  static public void addRecipe(IItemStack output,IIngredient input_a,IIngredient input_b)
  {
    Object in_a = MTHelper.getIngredient(input_a);
    Object in_b = MTHelper.getIngredient(input_b);
    ItemStack out = (ItemStack)output.getInternal();

    IAlloyFurnaceRecipe recipe = null;
    try
    {
      recipe = new AlloyFurnaceRecipe(out, in_a, in_b);
    } catch(IllegalArgumentException e)
    {
      MineTweakerAPI.logError("Invalid alloy furnace recipe: " + e.getMessage());
      return;
    }
    MineTweakerAPI.apply((new AlloyFurnaceAction(recipe).action_add));
  }

  @ZenMethod
  static public void removeRecipe(IIngredient input_a,IIngredient input_b)
  {
    ItemStack in_a = (ItemStack)input_a.getItems().get(0).getInternal();
    ItemStack in_b = (ItemStack)input_b.getItems().get(0).getInternal();
    
    IAlloyFurnaceRecipe recipe = AlloyFurnaceRecipeManager.instance.FindRecipe(in_a,in_b);
    if(recipe == null)
    {
      MineTweakerAPI.logWarning("Alloy furnace recipe not found.");
      return;
    }
    MineTweakerAPI.apply((new AlloyFurnaceAction(recipe)).action_remove);
  }
}
