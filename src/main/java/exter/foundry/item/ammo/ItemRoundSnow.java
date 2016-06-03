package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.creativetab.FoundryTabFirearms;
import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRoundSnow extends ItemRoundBase
{
  
  public ItemRoundSnow()
  {
    super(3, 50, 25);
    setCreativeTab(FoundryTabFirearms.tab);
    setUnlocalizedName("roundSnow");
    setRegistryName("roundSnow");
  }
  
  @Override
  public double getBaseDamage(ItemStack round,EntityLivingBase entity_hit)
  {
    double damage = super.getBaseDamage(round, entity_hit);
    if(entity_hit instanceof EntityEnderman || entity_hit instanceof EntityBlaze)
    {
      damage += 7;
    }
    return damage;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4)
  {
    super.addInformation(stack, player, list, par4);
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(TextFormatting.YELLOW + "+7.0 damage to Enderman and Blaze.");
    }
  }


  @Override
  public String getRoundType(ItemStack round)
  {
    return ItemRevolver.AMMO_TYPE;
  }
}
