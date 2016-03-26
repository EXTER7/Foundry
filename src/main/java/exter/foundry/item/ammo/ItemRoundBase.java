package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.creativetab.FoundryTabFirearms;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
      list.add(TextFormatting.BLUE + "Base Damage: " + base_damage);
      list.add(TextFormatting.BLUE + "Base Range: " + base_range);
      list.add(TextFormatting.BLUE + "Fallof Range: " + falloff_range);
    }
  }

  @Override
  public boolean breaksGlass(ItemStack round)
  {
    return true;
  }

  @Override
  public double getBaseRange(ItemStack round)
  {
    return base_range;
  }

  @Override
  public double getFalloffRange(ItemStack round)
  {
    return falloff_range;
  }

  @Override
  public double getBaseDamage(ItemStack round)
  {
    return base_damage;
  }
  
  @Override
  public void onBulletDamagedLivingEntity(ItemStack round, EntityLivingBase entity,int count)
  {
    
  }

  @Override
  public void onBulletHitBlock(ItemStack round, EntityLivingBase shooter, Vec3d from, World world, BlockPos pos, EnumFacing side)
  {
    
  }
  
  @Override
  public ItemStack getCasing(ItemStack round)
  {
    return FoundryItems.component(ItemComponent.SubItem.AMMO_CASING);
  }
  
  @Override
  public boolean ignoresArmor(ItemStack round)
  {
    return false;
  }
}