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
  public String getUnlocalizedName(ItemStack stack)
  {
    return getUnlocalizedName() + String.valueOf(stack.getItemDamage() & 7);
  }
}
