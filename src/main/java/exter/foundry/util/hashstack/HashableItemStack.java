package exter.foundry.util.hashstack;

import net.minecraft.item.ItemStack;

/**
 * ItemStack wrapper for use in HashMaps (stack size insensitive)
 */
public class HashableItemStack
{
  private ItemStack stack;
  
  //Used to get value from a HashMap without creating a new object every time.
  private static final HashableItemStack cache = new HashableItemStack();

  private HashableItemStack()
  {
    stack = null;
  }
  
  private void SetStack(ItemStack is)
  {
    stack = is;
  }
  
  //This should only be used as a key argument in HashMap.get or similar methods.
  public static HashableItemStack Cache(ItemStack is)
  {
    cache.SetStack(is);
    return cache;
  }

  public HashableItemStack(ItemStack item_stack)
  {
    if(item_stack == null)
    {
      stack = null;
      return;
    }
    stack = item_stack.copy();
  }

  public HashableItemStack(ItemStack item_stack,int stack_size)
  {
    if(item_stack == null)
    {
      stack = null;
      return;
    }
    stack = item_stack.copy();
    stack.stackSize = stack_size;
  }
  
  public ItemStack GetItemStack()
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
    result = prime * result + ((stack.stackTagCompound == null) ? 0 : stack.stackTagCompound.hashCode());
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
    if(!(obj instanceof HashableItemStack))
    {
      return false;
    }
    HashableItemStack other = (HashableItemStack)obj;
    return stack.isItemEqual(other.stack) && ItemStack.areItemStackTagsEqual(stack, other.stack);
  }
}
