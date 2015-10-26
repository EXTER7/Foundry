package exter.foundry.item.ammo;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

public class ItemRoundAP extends ItemRoundBase
{
  public IIcon icon;
  
  public ItemRoundAP()
  {
    super(7,50,25);
    setUnlocalizedName("roundAP");
  }


  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    icon = register.registerIcon("foundry:round_ap");
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
    return ItemRevolver.AMMO_TYPE;
  }

  @Override
  public boolean IgnoresArmor(ItemStack round)
  {
    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean par4)
  {
    super.addInformation(stack, player, list, par4);
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(EnumChatFormatting.YELLOW + "Bypasses armor.");
    }
  }
}
