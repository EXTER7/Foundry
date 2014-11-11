package exter.foundry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;

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
        } else if(is.getItem() == FoundryItems.item_component && is.getItemDamage() == ItemFoundryComponent.COMPONENT_BLANKMOLD)
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
    FMLCommonHandler.instance().bus().register(this); 
  }
}
