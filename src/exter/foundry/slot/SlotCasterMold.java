package exter.foundry.slot;

import exter.foundry.recipes.CastingRecipe;
import exter.foundry.recipes.manager.CastingRecipeManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCasterMold extends Slot
{
  public SlotCasterMold(IInventory inventory, int par2, int par3, int par4)
  {
    super(inventory, par2, par3, par4);
  }
  
  @Override
  public boolean isItemValid(ItemStack stack)
  {
    return CastingRecipeManager.instance.IsItemMold(stack);
  }
}
