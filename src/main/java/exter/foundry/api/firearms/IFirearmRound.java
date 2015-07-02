package exter.foundry.api.firearms;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IFirearmRound
{
  public void OnBulletHitBlock(ItemStack ammo, EntityPlayer player, Vec3 from, World world,int x,int y,int z,ForgeDirection side);
  public void OnBulletHitEntity(ItemStack ammo, EntityPlayer player, Vec3 from, Entity entity);
}
