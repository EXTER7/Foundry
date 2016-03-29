package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;

public interface IMoldRecipe
{
  public ItemStack getOutput();
  
  public int[] getRecipe();

  public int getWidth();

  public int getHeight();
  
  public boolean matches(int[] grid);
}
