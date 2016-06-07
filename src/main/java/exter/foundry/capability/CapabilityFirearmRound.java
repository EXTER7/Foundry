package exter.foundry.capability;

import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityFirearmRound
{
  static private class ReferenceFirearmRound implements IFirearmRound
  {
    @Override
    public String getRoundType()
    {
      return ItemRevolver.AMMO_TYPE;
    }

    @Override
    public void onBulletHitBlock(EntityLivingBase shooter, Vec3d from, World world, BlockPos pos, EnumFacing side)
    {
      
    }

    @Override
    public void onBulletDamagedLivingEntity(EntityLivingBase entity, int count)
    {
      
    }

    @Override
    public boolean breaksGlass()
    {
      return true;
    }

    @Override
    public double getBaseRange()
    {
      return 60;
    }

    @Override
    public double getFalloffRange()
    {
      return 30;
    }

    @Override
    public double getBaseDamage(EntityLivingBase entity_hit)
    {
      return 10;
    }

    @Override
    public ItemStack getCasing()
    {
      return null;
    }

    @Override
    public boolean ignoresArmor()
    {
      return false;
    }
  }
  
  static private class Storage implements IStorage<IFirearmRound>
  {
    @Override
    public NBTBase writeNBT(Capability<IFirearmRound> capability, IFirearmRound instance, EnumFacing side)
    {
      return null;
    }

    @Override
    public void readNBT(Capability<IFirearmRound> capability, IFirearmRound instance, EnumFacing side, NBTBase nbt)
    {
      
    }
  }
  
  static public void init()
  {
    CapabilityManager.INSTANCE.register(IFirearmRound.class,new Storage(),ReferenceFirearmRound.class);
  }
}
