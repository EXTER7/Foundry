package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import net.minecraft.item.ItemStack;

public class TileEntityCastingTableRod extends TileEntityCastingTableBase
{
  private final ItemStack mold;
  public TileEntityCastingTableRod()
  {
    super();    
    mold = FoundryItems.mold(ItemMold.SubItem.ROD);
  }
  
  @Override
  public ItemStack getMold()
  {
    return mold;
  }

  @Override
  public int getFluidNeeded()
  {
    return FoundryAPI.FLUID_AMOUNT_ROD;
  }
}
