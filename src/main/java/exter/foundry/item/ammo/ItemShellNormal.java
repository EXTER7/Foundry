package exter.foundry.item.ammo;

import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.firearm.ItemShotgun;
import net.minecraft.item.ItemStack;

public class ItemShellNormal extends ItemRoundBase
{
  public ItemShellNormal()
  {
    super(5,50,20);
    setUnlocalizedName("shellNormal");
  }

  @Override
  public String getRoundType(ItemStack round)
  {
    return ItemShotgun.AMMO_TYPE;
  }

  @Override
  public ItemStack getCasing(ItemStack round)
  {
    return FoundryItems.Component(ItemComponent.COMPONENT_AMMO_CASING_SHELL);
  }
}
