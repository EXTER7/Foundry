package exter.foundry.item;

import exter.foundry.block.IBlockVariants;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockSlab extends ItemBlockMulti
{
  public <T extends Block & IBlockVariants> ItemBlockSlab(T block)
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
