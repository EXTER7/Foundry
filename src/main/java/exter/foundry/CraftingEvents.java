package exter.foundry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraft.item.ItemStack;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;

public class CraftingEvents
{
  @SubscribeEvent
  public void onCrafting(PlayerEvent.ItemCraftedEvent event)
  {
    if(event.crafting.getItem() == FoundryItems.item_mold)
    {
      int blanks = 0;
      int empty = 0;
      ItemStack pattern = null;
      int i;
      int size = event.craftMatrix.getSizeInventory();
      for(i = 0; i < size; i++)
      {
        ItemStack is = event.craftMatrix.getStackInSlot(i);
        if(is == null)
        {
          empty++;
        } else if(is.getItem() == FoundryItems.item_component && is.getItemDamage() == ItemComponent.SubItem.BLANKMOLD.id)
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
  
  public CraftingEvents()
  {
    MinecraftForge.EVENT_BUS.register(this); 
  }
}
