package exter.foundry.item.ammo;

import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.item.ItemStack;

public class ItemRoundNormal extends ItemRoundBase
{

  public ItemRoundNormal()
  {
    super(10,60,30);
    setRegistryName("roundNormal");
    setUnlocalizedName("roundNormal");
  }
  
  @Override
  public String getRoundType(ItemStack round)
  {
    return ItemRevolver.AMMO_TYPE;
  }
}
