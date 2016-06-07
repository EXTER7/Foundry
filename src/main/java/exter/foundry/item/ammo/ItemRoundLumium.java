package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.creativetab.FoundryTabFirearms;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRoundLumium extends Item
{
  static private class FirearmRound extends FirearmRoundBase
  {
    public FirearmRound()
    {
      super(6,60,30);
    }
    
    @Override
    public double getBaseDamage(EntityLivingBase entity_hit)
    {
      double damage = super.getBaseDamage(entity_hit);
      if(entity_hit.isEntityUndead())
      {
        damage += 12;
      }
      return damage;
    }
  }
  
  public ItemRoundLumium()
  {
    setCreativeTab(FoundryTabFirearms.tab);
    setUnlocalizedName("roundLumium");
    setRegistryName("roundLumium");
  }
  
  
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4)
  {
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(TextFormatting.BLUE + "Base Damage: 6");
      list.add(TextFormatting.BLUE + "Base Range: 60");
      list.add(TextFormatting.BLUE + "Falloff Range: 30");
      list.add(TextFormatting.YELLOW + "+12 damage to undead creatures.");
    }
  }
  
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
  {
    return new FirearmRound();
  }
}
