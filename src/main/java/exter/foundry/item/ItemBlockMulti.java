package exter.foundry.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMulti extends ItemBlock
{
  private String[] sub_names;
  
  public ItemBlockMulti(Block block)
  {
    super(block);
    setHasSubtypes(true);
    sub_names = null;
  }

  @Override
  public int getMetadata(int dmg)
  {
    return dmg;
  }
  
  public final void SetSubNames(String[] names)
  {
    sub_names = names.clone();
  }
  
  protected int GetSubIndex(ItemStack stack)
  {
    return stack.getItemDamage();
  }
  
  @Override
  public final String getUnlocalizedName(ItemStack stack)
  {
    if(sub_names != null)
    {
      return getUnlocalizedName() + "." + sub_names[GetSubIndex(stack)];
    } else
    {
      return getUnlocalizedName() + String.valueOf(GetSubIndex(stack));
    }
  }
}
