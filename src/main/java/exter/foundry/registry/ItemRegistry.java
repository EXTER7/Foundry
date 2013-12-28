package exter.foundry.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import exter.foundry.api.registry.IItemRegistry;

public class ItemRegistry implements IItemRegistry
{
  private Map<String,ItemStack> items;
  static public ItemRegistry instance = new ItemRegistry();
  
  private ItemRegistry()
  {
    items = new HashMap<String,ItemStack>();
  }

  @Override
  public ItemStack GetItem(String name)
  {
    return items.get(name).copy();
  }
  
  public void RegisterItem(String name,ItemStack item)
  {
    items.put(name, item);
  }
}
