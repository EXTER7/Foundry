package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import net.minecraft.item.ItemStack;

public class TileEntityCastingTableIngot extends TileEntityCastingTableBase
{
  private final ItemStack mold;
  public TileEntityCastingTableIngot()
  {
    super();    
    mold = FoundryItems.mold(ItemMold.SubItem.INGOT);
  }
  
  @Override
  public ItemStack getMold()
  {
    return mold;
  }

  @Override
  public int getFluidNeeded()
  {
    return FoundryAPI.FLUID_AMOUNT_INGOT;
  }
}
