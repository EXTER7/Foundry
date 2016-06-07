package exter.foundry.item.ammo;

import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.firearm.ItemShotgun;
import net.minecraft.item.ItemStack;

public class FirearmRoundShell extends FirearmRoundBase
{
  public FirearmRoundShell(double base_damage,double base_range,double falloff_range)
  {
    super(base_damage,base_range,falloff_range);
  }
  
  @Override
  public ItemStack getCasing()
  {
    return FoundryItems.component(ItemComponent.SubItem.AMMO_CASING_SHELL);
  }

  @Override
  public String getRoundType()
  {
    return ItemShotgun.AMMO_TYPE;
  }
}