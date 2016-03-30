package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;

public interface IMoldRecipe
{
  public ItemStack getOutput();
  
  public int[] getRecipeGrid();

  public int getWidth();

  public int getHeight();
  
  public boolean matchesRecipe(int[] grid);
}
