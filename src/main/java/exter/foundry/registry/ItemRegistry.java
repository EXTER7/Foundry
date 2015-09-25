package exter.foundry.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import exter.foundry.api.registry.IItemRegistry;

@Deprecated
public class ItemRegistry implements IItemRegistry
{
  private Map<String,ItemStack> items;
  static public ItemRegistry instance = new ItemRegistry();
  
  @Deprecated
  private ItemRegistry()
  {
    items = new HashMap<String,ItemStack>();
  }

  @Override
  @Deprecated
  public ItemStack GetItem(String name)
  {
    return items.get(name).copy();
  }
  
  @Deprecated
  public void RegisterItem(String name,ItemStack item)
  {
    items.put(name, item);
  }
}
