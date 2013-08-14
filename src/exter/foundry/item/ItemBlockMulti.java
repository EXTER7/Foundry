package exter.foundry.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMulti extends ItemBlock
{
  public ItemBlockMulti(int id)
  {
    super(id);
    setHasSubtypes(true);
  }

  @Override
  public int getMetadata(int dmg)
  {
    return dmg;
  }
  
  @Override
  public String getUnlocalizedName(ItemStack stack)
  {
    return getUnlocalizedName() + String.valueOf(stack.getItemDamage());
  }

}
