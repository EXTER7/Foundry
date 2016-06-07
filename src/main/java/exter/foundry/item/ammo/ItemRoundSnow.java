package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.creativetab.FoundryTabFirearms;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRoundSnow extends Item
{
  static private class FirearmRound extends FirearmRoundBase
  {
    public FirearmRound()
    {
      super(4, 40, 20);
    }
    
    @Override
    public double getBaseDamage(EntityLivingBase entity_hit)
    {
      double damage = super.getBaseDamage(entity_hit);
      if(entity_hit instanceof EntityEnderman || entity_hit instanceof EntityBlaze)
      {
        damage += 16;
      }
      return damage;
    }
  }
  
  public ItemRoundSnow()
  {
    setCreativeTab(FoundryTabFirearms.tab);
    setUnlocalizedName("roundSnow");
    setRegistryName("roundSnow");
  }
  

  
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4)
  {
    super.addInformation(stack, player, list, par4);
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(TextFormatting.BLUE + "Base Damage: 4");
      list.add(TextFormatting.BLUE + "Base Range: 40");
      list.add(TextFormatting.BLUE + "Falloff Range: 20");
      list.add(TextFormatting.YELLOW + "+16 damage to Enderman and Blaze.");
    }
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
  {
    return new FirearmRound();
  }
}
