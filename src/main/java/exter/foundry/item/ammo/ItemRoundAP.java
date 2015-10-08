package exter.foundry.item.ammo;

import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.item.ItemStack;

public class ItemRoundAP extends ItemRoundBase
{
  public ItemRoundAP()
  {
    super(7,50,25);
    setUnlocalizedName("roundAP");
  }

  @Override
  public String getRoundType(ItemStack round)
  {
    return ItemRevolver.AMMO_TYPE;
  }

  @Override
  public boolean ignoresArmor(ItemStack round)
  {
    return true;
  }
}
