package exter.foundry.item.ammo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.item.firearm.ItemShotgun;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemShellNormal extends ItemRoundBase
{
  public IIcon icon;
  public ItemShellNormal()
  {
    super(5,50,20);
    setUnlocalizedName("shellNormal");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    icon = register.registerIcon("foundry:shell_normal");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int dmg)
  {
    return icon;
  }
  
  @Override
  public String GetRoundType(ItemStack round)
  {
    return ItemShotgun.AMMO_TYPE;
  }
}
