package exter.foundry.item.ammo;

import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.firearm.ItemShotgun;
import net.minecraft.item.ItemStack;

public class ItemShellAP extends ItemRoundBase
{
  public ItemShellAP()
  {
    super(4,40,15);
    setUnlocalizedName("shellAP");
  }
  
  @Override
  public String getRoundType(ItemStack round)
  {
    return ItemShotgun.AMMO_TYPE;
  }

  @Override
  public ItemStack getCasing(ItemStack round)
  {
    return FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING_SHELL);
  }
  
  @Override
  public boolean ignoresArmor(ItemStack round)
  {
    return true;
  }
}
