package exter.foundry.item;

import exter.foundry.block.ISubBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMulti extends ItemBlock
{
  
  public <T extends Block & ISubBlocks> ItemBlockMulti(T block)
  {
    super(block);
    setHasSubtypes(true);
  }
  
  protected int GetSubIndex(ItemStack stack)
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
    int index = GetSubIndex(stack);
    String[] names = ((ISubBlocks)field_150939_a).GetSubNames();
    if(index >= names.length)
    {
      return null;
    }
    return getUnlocalizedName() + "." + names[index];
  }
}
