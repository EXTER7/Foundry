package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.creativetab.FoundryTabFirearms;
import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRoundLumium extends ItemRoundBase
{
  
  public ItemRoundLumium()
  {
    super(6,60,30);
    setCreativeTab(FoundryTabFirearms.tab);
    setUnlocalizedName("roundLumium");
    setRegistryName("roundLumium");
  }
  
  @Override
  public double getBaseDamage(ItemStack round,EntityLivingBase entity_hit)
  {
    double damage = super.getBaseDamage(round, entity_hit);
    if(entity_hit.isEntityUndead())
    {
      damage += 12;
    }
    return damage;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4)
  {
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(TextFormatting.BLUE + "Base Damage: " + base_damage + " (+12.0 to undead creatures)");
      list.add(TextFormatting.BLUE + "Base Range: " + base_range);
      list.add(TextFormatting.BLUE + "Fallof Range: " + falloff_range);
    }
  }



  @Override
  public String getRoundType(ItemStack round)
  {
    return ItemRevolver.AMMO_TYPE;
  }
  
  
}
