package exter.foundry.util.hashstack;

import net.minecraft.item.ItemStack;

/**
 * ItemStack wrapper for use in HashMaps (stack size insensitive)
 */
public class HashableItem
{
  protected ItemStack stack;
  
  //Used to get value from a HashMap without creating a new object every time.
  private static final HashableItem cache = new HashableItem();

  private HashableItem()
  {
    stack = null;
  }
  
  private void SetStack(ItemStack is)
  {
    stack = is;
  }
  
  //This should only be used as a key argument in HashMap.get or similar methods.
  public static HashableItem Cache(ItemStack is)
  {
    cache.SetStack(is);
    return cache;
  }

  public HashableItem(ItemStack item_stack)
  {
    if(item_stack == null)
    {
      stack = null;
      return;
    }
    stack = item_stack.copy();
  }
  
  public final ItemStack GetItemStack()
  {
    if(stack == null)
    {
      return null;
    }
    return stack.copy();
  }

  @Override
  public int hashCode()
  {
    if(stack == null)
    {
      return 0;
    }
    final int prime = 1289;
    int result = 1;
    result = prime * result + stack.getUnlocalizedName().hashCode();
    result = prime * result + ((stack.getTagCompound() == null) ? 0 : stack.getTagCompound().hashCode());
    result = prime * result + stack.getItemDamage();
    return result;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if(this == obj)
    {
      return true;
    }
    if(obj == null)
    {
      return false;
    }
    if(!(obj instanceof HashableItem))
    {
      return false;
    }
    HashableItem other = (HashableItem)obj;
    return stack.isItemEqual(other.stack) && ItemStack.areItemStackTagsEqual(stack, other.stack);
  }
}
