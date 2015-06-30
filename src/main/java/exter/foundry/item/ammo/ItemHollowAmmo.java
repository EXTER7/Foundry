package exter.foundry.item.ammo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.firearms.IFirearmAmmo;
import exter.foundry.creativetab.FoundryTabFirearms;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemHollowAmmo extends Item implements IFirearmAmmo
{
  public IIcon icon;
  
  public ItemHollowAmmo()
  {
    super();
    setCreativeTab(FoundryTabFirearms.tab);
    setUnlocalizedName("ammoHollow");
  }


  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    icon = register.registerIcon("foundry:ammoHollow");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int dmg)
  {
    return icon;
  }


  @Override
  public void OnHitBlock(ItemStack ammo, EntityPlayer player, Vec3 from, World world, int x, int y, int z, ForgeDirection side)
  {

  }

  @Override
  public void OnHitEntity(ItemStack ammo, EntityPlayer player, Vec3 from, Entity entity)
  {
    Vec3 end = Vec3.createVectorHelper( entity.posX, entity.posY, entity.posZ); 
    float distance = (float)end.distanceTo(from);
    float damage = 30 - distance * 1.5f;
    if(damage > 20)
    {
      damage = 20;
    }
    if(damage >= 1)
    {
      entity.attackEntityFrom((new EntityDamageSourceIndirect("bullet", entity, player)).setProjectile(), damage);
    }
  }

  @Override
  public int getItemStackLimit(ItemStack stack)
  {
    return 16;
  }
}