package exter.foundry.integration.minetweaker;

import java.util.ArrayList;
import java.util.List;

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

  static private List<ItemStack> getItemList(IIngredient input)
  {
    List<ItemStack> list = new ArrayList<ItemStack>();
    for(IItemStack item:input.getItems())
    {
      list.add((ItemStack)item.getInternal());
    }
    return list;
  }

  
  @ZenMethod
  static public void addRecipe(IItemStack output,IIngredient input_a,IIngredient input_b)
  {
    List<ItemStack> in_a = getItemList(input_a);
    List<ItemStack> in_b = getItemList(input_b);
    ItemStack out = (ItemStack)output.getInternal();

    IAlloyFurnaceRecipe recipe = null;
    for(ItemStack a:in_a)
    {
      for(ItemStack b:in_b)
      {
        try
        {
          recipe = new AlloyFurnaceRecipe(out,a,b);
        } catch(IllegalArgumentException e)
        {
          MineTweakerAPI.logError("Invalid alloy furnace recipe: " + e.getMessage());
          return;
        }
        MineTweakerAPI.apply((new AlloyFurnaceAction(recipe).action_add));
      }
    }
  }

  @ZenMethod
  static public void removeRecipe(IItemStack input_a,IIngredient input_b)
  {
    ItemStack in_a = (ItemStack)input_a.getInternal();
    ItemStack in_b = (ItemStack)input_b.getInternal();
    
    IAlloyFurnaceRecipe recipe = AlloyFurnaceRecipeManager.instance.FindRecipe(in_a,in_b);
    if(recipe == null)
    {
      MineTweakerAPI.logWarning("Alloy furnace recipe not found.");
      return;
    }
    MineTweakerAPI.apply((new AlloyFurnaceAction(recipe)).action_remove);
  }
}
