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

public class ItemShellLumium extends Item
{
  static private class FirearmRound extends FirearmRoundShell
  {
    public FirearmRound()
    {
      super(3,50,20);
    }

    @Override
    public double getBaseDamage(EntityLivingBase entity_hit)
    {
      double damage = super.getBaseDamage(entity_hit);
      if(entity_hit.isEntityUndead())
      {
        damage += 9;
      }
      return damage;
    }
  }
  
  public ItemShellLumium()
  {
    setCreativeTab(FoundryTabFirearms.tab);
    setUnlocalizedName("shellLumium");
    setRegistryName("shellLumium");
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4)
  {
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(TextFormatting.BLUE + "Base Damage: 3/pellet");
      list.add(TextFormatting.BLUE + "Base Range: 50");
      list.add(TextFormatting.BLUE + "Falloff Range: 20");
      list.add(TextFormatting.YELLOW + "+6 damage to undead creatures.");
    }
  }
  
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
  {
    return new FirearmRound();
  }
}
