package exter.foundry.item.ammo;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.creativetab.FoundryTabFirearms;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class ItemRoundBase extends Item implements IFirearmRound
{
  public final double base_damage;
  public final double base_range;
  public final double falloff_range;
  
  
  public ItemRoundBase(double base_damage,double base_range,double falloff_range)
  {
    super();
    this.base_damage = base_damage;
    this.base_range = base_range;
    this.falloff_range = falloff_range;
    setCreativeTab(FoundryTabFirearms.tab);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean par4)
  {
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(EnumChatFormatting.BLUE + "Base Damage: " + base_damage);
      list.add(EnumChatFormatting.BLUE + "Base Range: " + base_range);
      list.add(EnumChatFormatting.BLUE + "Fallof Range: " + falloff_range);
    }
  }

  @Override
  public boolean BreakGlass(ItemStack round)
  {
    return true;
  }

  @Override
  public double GetBaseRange(ItemStack round)
  {
    return base_range;
  }

  @Override
  public double GetFalloffRange(ItemStack round)
  {
    return falloff_range;
  }

  @Override
  public double GetBaseDamage(ItemStack round)
  {
    return base_damage;
  }
  
  @Override
  public void OnBulletDamagedLivingEntity(ItemStack round, EntityLivingBase entity,int count)
  {
    
  }

  @Override
  public void OnBulletHitBlock(ItemStack round, EntityLivingBase shooter, Vec3 from, World world, int x, int y, int z, ForgeDirection side)
  {
    
  }
}