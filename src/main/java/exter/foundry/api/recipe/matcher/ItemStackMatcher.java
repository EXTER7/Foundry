package exter.foundry.api.recipe.matcher;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemStackMatcher implements IItemMatcher
{
  private ItemStack match;

  public ItemStackMatcher(Block match)
  {
    this.match = new ItemStack(match);
  }

  public ItemStackMatcher(Item match)
  {
    this.match = new ItemStack(match);
  }

  public ItemStackMatcher(ItemStack match)
  {
    this.match = match.copy();
  }

  @Override
  public boolean apply(ItemStack input)
  {
    return ItemStack.areItemsEqual(match, input) && ItemStack.areItemStackTagsEqual(input, match) && input.stackSize >= match.stackSize;
  }

  @Override
  public int getAmount()
  {
    return match.stackSize;
  }

  @Override
  public List<ItemStack> getItems()
  {
    return Lists.newArrayList(match.copy());
  }

  @Override
  public ItemStack getItem()
  {
    return match;
  }
}
