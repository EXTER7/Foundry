package exter.foundry.item.ammo;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class ItemRoundHollow extends ItemRoundBase
{
  public IIcon icon;
  
  public ItemRoundHollow()
  {
    super(16,30,25);
    setUnlocalizedName("roundHollow");
  }


  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    icon = register.registerIcon("foundry:round_hollow");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int dmg)
  {
    return icon;
  }

  @Override
  public String GetRoundType()
  {
    return ItemRevolver.AMMO_TYPE;
  }
}