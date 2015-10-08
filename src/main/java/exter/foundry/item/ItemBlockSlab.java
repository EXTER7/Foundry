package exter.foundry.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockSlab extends ItemBlockMulti
{
  public ItemBlockSlab(Block block)
  {
    super(block);
  }

  @Override
  public int getMetadata(int dmg)
  {
    return dmg;
  }
  
  @Override
  protected int getSubIndex(ItemStack stack)
  {
    return stack.getItemDamage() & 7;
  }  
}
