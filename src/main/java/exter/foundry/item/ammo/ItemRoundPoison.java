package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRoundPoison extends ItemRoundBase
{
  public ItemRoundPoison()
  {
    super(8, 50, 25);
    setUnlocalizedName("roundPoison");
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean par4)
  {
    super.addInformation(stack, player, list, par4);
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(TextFormatting.YELLOW + "Inflicts poison on target.");
    }
  }


  @Override
  public void onBulletDamagedLivingEntity(ItemStack round, EntityLivingBase entity,int count)
  {
    entity.addPotionEffect(new PotionEffect(Potion.potionRegistry.getObject(new ResourceLocation("poison")), 400));
  }
  
  @Override
  public String getRoundType(ItemStack round)
  {
    return ItemRevolver.AMMO_TYPE;
  }
}
