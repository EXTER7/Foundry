package exter.foundry.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class InventoryShotgun extends InventoryFirearm
{
  public InventoryShotgun(ItemStack firearm_item, InventoryPlayer player, int size)
  {
    super(firearm_item, player, size);
  }
  
  @Override
  public void closeInventory()
  {
    int i;
    List<ItemStack> ammo = new ArrayList<ItemStack>();
    for(i = 0; i < items.length; i++)
    {
      ItemStack a = items[i];
      if(a != null)
      {
        ammo.add(a);
      }
    }
    for(i = 0; i < ammo.size(); i++)
    {
      items[i] = ammo.get(i);
    }
    for(i = ammo.size(); i < items.length; i++)
    {
      items[i] = null;
    }
  }
}
