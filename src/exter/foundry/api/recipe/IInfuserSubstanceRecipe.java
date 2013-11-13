package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;

public interface IInfuserSubstanceRecipe
{
  /**
   * Get the required item.
   * @return If the recipe used the Ore Dictionary, a {@link String} of it's name, an {@link ItemStack} of the required item otherwise.
   */
  public Object GetInputItem();
  
  public String GetOutputSubstanceType();
  
  public int GetOutputSubstanceAmount();
  
  public int GetEneryNeeded();
}
