package exter.foundry.container.slot;

import cpw.mods.fml.common.FMLCommonHandler;
import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.inventory.InventoryFirearm;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFirearmAmmo extends Slot
{
  private String type;

  public SlotFirearmAmmo(InventoryFirearm inventory, int index, int x, int y,String ammo_type)
  {
    super(inventory, index, x, y);
    type = ammo_type;
  }

  @Override
  public boolean isItemValid(ItemStack stack)
  {
    return (stack.getItem() instanceof IFirearmRound) && ((IFirearmRound)stack.getItem()).getRoundType(stack).equals(type);
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
      ((InventoryFirearm)inventory).Save();
    }
  }
}
