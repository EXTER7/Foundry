package exter.foundry.item.ammo;


import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.item.ItemStack;

public class ItemRoundJacketed extends ItemRoundBase
{
  public ItemRoundJacketed()
  {
    super(7, 100, 40);
    setUnlocalizedName("roundJacketed");
    setRegistryName("roundJacketed");
  }
  
  @Override
  public String getRoundType(ItemStack round)
  {
    return ItemRevolver.AMMO_TYPE;
  }
}
