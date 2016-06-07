package exter.foundry.item.ammo;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class FirearmRoundBase implements IFirearmRound,ICapabilityProvider
{
  static public class Storage implements IStorage<IFirearmRound>
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
  
  public final double base_damage;
  public final double base_range;
  public final double falloff_range;
  
  
  public FirearmRoundBase(double base_damage,double base_range,double falloff_range)
  {
    this.base_damage = base_damage;
    this.base_range = base_range;
    this.falloff_range = falloff_range;
  }
  

  @Override
  public boolean breaksGlass()
  {
    return true;
  }

  @Override
  public double getBaseRange()
  {
    return base_range;
  }

  @Override
  public double getFalloffRange()
  {
    return falloff_range;
  }

  @Override
  public double getBaseDamage(EntityLivingBase entity_hit)
  {
    return base_damage;
  }
  
  @Override
  public void onBulletDamagedLivingEntity(EntityLivingBase entity,int count)
  {
    
  }

  @Override
  public void onBulletHitBlock(EntityLivingBase shooter, Vec3d from, World world, BlockPos pos, EnumFacing side)
  {
    
  }
  
  @Override
  public ItemStack getCasing()
  {
    return FoundryItems.component(ItemComponent.SubItem.AMMO_CASING);
  }
  
  @Override
  public boolean ignoresArmor()
  {
    return false;
  }

  @Override
  public String getRoundType()
  {
    return ItemRevolver.AMMO_TYPE;
  }

  @Override
  public final boolean hasCapability(Capability<?> cap,EnumFacing facing)
  {
    return cap == FoundryAPI.capability_firearmround;
  }
  
  @Override
  public final <T> T getCapability(Capability<T> cap, EnumFacing facing)
  {
    if(cap == FoundryAPI.capability_firearmround)
    {
      return FoundryAPI.capability_firearmround.cast(this);
    } else
    {
      return null;
    }
  }
}