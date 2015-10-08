package exter.foundry.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMulti extends ItemBlock
{
  
  public ItemBlockMulti(Block block)
  {
    super(block);
    setHasSubtypes(true);
  }
  
  protected int getSubIndex(ItemStack stack)
  {
    return stack.getItemDamage();
  }

  @Override
  public int getMetadata(int dmg)
  {
    return dmg;
  }
  
  @Override
  public final String getUnlocalizedName(ItemStack stack)
  {
    int index = getSubIndex(stack);
    return getUnlocalizedName() + "." + index;
  }
}
