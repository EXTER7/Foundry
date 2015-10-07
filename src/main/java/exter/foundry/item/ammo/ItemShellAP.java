package exter.foundry.item.ammo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.firearm.ItemShotgun;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemShellAP extends ItemRoundBase
{
  public IIcon icon;
  public ItemShellAP()
  {
    super(4,40,15);
    setUnlocalizedName("shellAP");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    icon = register.registerIcon("foundry:shell_ap");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int dmg)
  {
    return icon;
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
  
  @Override
  public boolean ignoresArmor(ItemStack round)
  {
    return true;
  }
}
