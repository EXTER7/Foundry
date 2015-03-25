package exter.foundry.item;

import exter.foundry.block.ISubBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockSlab extends ItemBlockMulti
{
  public <T extends Block & ISubBlocks> ItemBlockSlab(T block)
  {
    super(block);
  }

  @Override
  public int getMetadata(int dmg)
  {
    return dmg;
  }
  
  @Override
  protected int GetSubIndex(ItemStack stack)
  {
    return stack.getItemDamage() & 7;
  }  
}
