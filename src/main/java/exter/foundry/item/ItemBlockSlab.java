package exter.foundry.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockSlab extends ItemBlockMulti
{
  public ItemBlockSlab(Block block)
  {
    super(block);
    setHasSubtypes(true);
  }

  @Override
  public int GetSubIndex(ItemStack stack)
  {
    return stack.getItemDamage() & 7;
  }
}
