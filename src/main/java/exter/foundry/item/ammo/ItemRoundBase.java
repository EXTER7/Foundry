package exter.foundry.item.ammo;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.creativetab.FoundryTabFirearms;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSourceIndirect;
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
  
  protected abstract void OnBulletDamagedLivingEntity(EntityLiving entity);

  private double GetDamageFromDistance(double distance)
  {
    if(distance < base_range)
    {
      return base_damage;
    } else if(distance > base_range + falloff_range)
    {
      return 0;
    } else
    {
      return base_damage * (1.0f - (distance - base_range) / falloff_range);
    }    
  }
  
  
  @Override
  public void OnBulletHitBlock(ItemStack ammo, EntityPlayer player, Vec3 from, World world, int x, int y, int z, ForgeDirection side)
  {
    Block b = world.getBlock(x, y, z);
    int m = world.getBlockMetadata(x, y, z);
    if(b.getMaterial() == Material.glass && b.getBlockHardness(world, x, y, z) < 0.4)
    {
      world.playAuxSFXAtEntity(null, 2001, x, y, z, Block.getIdFromBlock(b)+(m<<12));
      world.setBlockToAir(x, y, z);
    }    
  }

  @Override
  public final void OnBulletHitEntity(ItemStack ammo, EntityPlayer player, Vec3 from, Entity entity)
  {
    if(entity instanceof EntityLiving)
    {
      Vec3 end = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
      double damage = GetDamageFromDistance(end.distanceTo(from));
      if(damage >= 1)
      {
        if(entity.attackEntityFrom((new EntityDamageSourceIndirect("bullet", entity, player)).setProjectile(), (float)damage))
        {
          OnBulletDamagedLivingEntity((EntityLiving)entity);
        }
      }
    }
  }
  
  @Override
  public final int getItemStackLimit(ItemStack stack)
  {
    return 16;
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

}