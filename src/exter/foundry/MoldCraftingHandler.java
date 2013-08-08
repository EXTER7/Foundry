package exter.foundry;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.ICraftingHandler;
import exter.foundry.item.FoundryItems;

public class MoldCraftingHandler implements ICraftingHandler
{

  @Override
  public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
  {
    if(item.itemID == FoundryItems.item_mold.itemID)
    {
      int clayblocks = 0;
      int blanks = 0;
      ItemStack pattern = null;
      int i;
      int size = craftMatrix.getSizeInventory();
      for(i = 0; i < size; i++)
      {
        ItemStack is = craftMatrix.getStackInSlot(i);
        if(is == null)
        {
          blanks++;
        } else if(is.itemID == Block.blockClay.blockID)
        {
          clayblocks++;
        } else
        {
          pattern = is;
        }
      }
      if(pattern != null && clayblocks == 1 && size - blanks - clayblocks == 1)
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
