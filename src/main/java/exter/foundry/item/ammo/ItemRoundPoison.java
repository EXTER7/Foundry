package exter.foundry.item.ammo;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

public class ItemRoundPoison extends ItemRoundBase
{
  public IIcon icon;
  
  public ItemRoundPoison()
  {
    super(8, 50, 25);
    setUnlocalizedName("roundPoison");
  }


  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    icon = register.registerIcon("foundry:round_poison");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int dmg)
  {
    return icon;
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean par4)
  {
    super.addInformation(stack, player, list, par4);
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(EnumChatFormatting.YELLOW + "Inflicts poison on target.");
    }
  }


  @Override
  public void OnBulletDamagedLivingEntity(ItemStack round, EntityLiving entity,int count)
  {
    entity.addPotionEffect(new PotionEffect(Potion.poison.id, 400));    
  }
  
  @Override
  public String GetRoundType()
  {
    return ItemRevolver.AMMO_TYPE;
  }
}
