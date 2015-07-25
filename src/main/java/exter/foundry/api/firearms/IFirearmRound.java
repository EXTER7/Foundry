package exter.foundry.api.firearms;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IFirearmRound
{
  public String GetRoundType();
  
  public void OnBulletHitBlock(ItemStack round, EntityPlayer player, Vec3 from, World world, int x, int y, int z, ForgeDirection side);

  public void OnBulletDamagedLivingEntity(ItemStack round,EntityLiving entity, int count);

  public boolean BreakGlass();
  
  public double GetBaseRange();
  
  public double GetFalloffRange();
  
  public double GetBaseDamage();
}
