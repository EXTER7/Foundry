package exter.foundry.item.ammo;


import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.item.ItemStack;

public class ItemRoundHollow extends ItemRoundBase
{

  public ItemRoundHollow()
  {
    super(16,30,25);
    setUnlocalizedName("roundHollow");
  }

  @Override
  public String getRoundType(ItemStack round)
  {
    return ItemRevolver.AMMO_TYPE;
  }
}