package exter.foundry.item.ammo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.IIcon;

public class ItemRoundNormal extends ItemRoundBase
{
  public IIcon icon;
  
  public ItemRoundNormal()
  {
    super(10,60,30);
    setUnlocalizedName("roundNormal");
  }


  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    icon = register.registerIcon("foundry:round_normal");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int dmg)
  {
    return icon;
  }


  @Override
  protected void OnBulletDamagedLivingEntity(EntityLiving entity)
  {

  }
}
