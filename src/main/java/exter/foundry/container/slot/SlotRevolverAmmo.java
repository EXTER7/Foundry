package exter.foundry.container.slot;

import cpw.mods.fml.common.FMLCommonHandler;
import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.inventory.InventoryRevolver;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRevolverAmmo extends Slot
{

  public SlotRevolverAmmo(InventoryRevolver inventory, int index, int x, int y)
  {
    super(inventory, index, x, y);
  }

  @Override
  public boolean isItemValid(ItemStack stack)
  {
    return stack.getItem() instanceof IFirearmRound;
  }

  @Override
  public int getSlotStackLimit()
  {
    return 1;
  }

  @Override
  public void onSlotChanged()
  {
    super.onSlotChanged();
    if(FMLCommonHandler.instance().getEffectiveSide().isServer())
    {
      ((InventoryRevolver)inventory).Save();
    }
  }
}
