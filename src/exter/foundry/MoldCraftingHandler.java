package exter.foundry;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.ICraftingHandler;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;

public class MoldCraftingHandler implements ICraftingHandler
{

  @Override
  public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
  {
    if(item.itemID == FoundryItems.item_mold.itemID)
    {
      int blanks = 0;
      int empty = 0;
      ItemStack pattern = null;
      int i;
      int size = craftMatrix.getSizeInventory();
      for(i = 0; i < size; i++)
      {
        ItemStack is = craftMatrix.getStackInSlot(i);
        if(is == null)
        {
          empty++;
        } else if(is.itemID == FoundryItems.item_component.itemID && is.getItemDamage() == ItemFoundryComponent.COMPONENT_BLANKMOLD)
        {
          blanks++;
        } else
        {
          pattern = is;
        }
      }
      
      // Don't consume the item used as a shape.
      if(pattern != null && blanks == 1 && size - empty - blanks == 1)
      {
        pattern.stackSize++;
      }
    }
  }

  @Override
  public void onSmelting(EntityPlayer player, ItemStack item)
  {

  }

}
