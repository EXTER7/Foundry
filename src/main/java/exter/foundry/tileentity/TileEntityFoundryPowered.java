package exter.foundry.tileentity;



import cofh.api.energy.IEnergyReceiver;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.capability.TeslaCapabilities;
//import ic2.api.energy.event.EnergyTileLoadEvent;
//import ic2.api.energy.event.EnergyTileUnloadEvent;
//import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

/**
 * Base class for all machines.
 */
//@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2")
public abstract class TileEntityFoundryPowered extends TileEntityFoundry implements IEnergyReceiver/*,IEnergySink*/
{
  @Optional.Interface(iface = "net.darkhax.tesla.api.ITeslaConsumer", modid = "Tesla")
  private class TeslaConsumer implements ITeslaConsumer
  {
    @Optional.Method(modid = "Tesla")
    @Override
    public long givePower(long power, boolean simulated)
    {
      return receiveFoundryEnergy(power * RATIO_TESLA,!simulated,false) / RATIO_TESLA;
    }
  }
  
  private class ForgeEnergyConsumer implements IEnergyStorage
  {

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
      return (int)receiveFoundryEnergy(maxReceive * RATIO_FE,!simulate,false) / RATIO_FE;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
      return 0;
    }

    @Override
    public int getEnergyStored()
    {
      return (int)(energy_stored / RATIO_FE);
    }

    @Override
    public int getMaxEnergyStored()
    {
      return 0;
    }

    @Override
    public boolean canExtract()
    {
      return false;
    }

    @Override
    public boolean canReceive()
    {
      return true;
    }    
  }
  
//  private boolean added_enet;
  protected boolean update_energy;
  protected boolean update_energy_tick;
  
  public abstract long getFoundryEnergyCapacity();

  static public int RATIO_RF = 10;
  static public int RATIO_TESLA = 10;
  static public int RATIO_FE = 10;
  static public int RATIO_EU = 40;
  
  private long energy_stored;
  
  private final TeslaConsumer tesla;
  private final ForgeEnergyConsumer fe;

  public TileEntityFoundryPowered()
  {
    super();
    if(Loader.isModLoaded("Tesla"))
    {
      tesla = new TeslaConsumer();
    } else
    {
      tesla = null;
    }
    fe = new ForgeEnergyConsumer();
    update_energy = false;
    update_energy_tick = true;
//    added_enet = false;
  }

  
  private long receiveFoundryEnergy(long en,boolean do_receive, boolean allow_overflow)
  {
    if(!allow_overflow)
    {
      long needed = getFoundryEnergyCapacity() - energy_stored;
      if(en > needed)
      {
        en = needed;
      }
    }
    if(do_receive)
    {
      energy_stored += en;
      if(en > 0)
      {
        if(update_energy && !world.isRemote)
        {
          update_energy_tick = true;
        }
      }
    }
    return en;
  }
  
//  private double receiveEU(double eu,boolean do_receive)
//  {
//    return (double)receiveFoundryEnergy((int)(eu * RATIO_EU),do_receive,true) / RATIO_EU;
//  }
  
  public long useFoundryEnergy(long amount,boolean do_use)
  {
    if(amount > energy_stored)
    {
      amount = energy_stored;
    }
    if(do_use)
    {
      energy_stored -= amount;
      updateFoundryEnergy();
    }
    return amount;
  }
  
  public long getStoredFoundryEnergy()
  {
    long capacity = getFoundryEnergyCapacity();
    if(energy_stored > capacity)
    {
      return capacity;
    } else
    {
      return energy_stored;
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound)
  {
    super.readFromNBT(compound);
    if(compound.hasKey("energy"))
    {
      energy_stored = compound.getLong("energy");
    }
  }
  
  
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound)
  {
    if(compound == null)
    {
      compound = new NBTTagCompound();
    }
    super.writeToNBT(compound);
    compound.setLong("energy", energy_stored);
    return compound;
  }

  protected void onInitialize()
  {
    update_energy_tick = true;
  }
  
//  @Override
//  public void updateEntity()
//  {
//    if(!added_enet)
//    {
//      try
//      {
//        getClass().getMethod("LoadEnet").invoke(this);
//      } catch(IllegalAccessException e)
//      {
//        throw new RuntimeException(e);
//      } catch(IllegalArgumentException e)
//      {
//        throw new RuntimeException(e);
//      } catch(InvocationTargetException e)
//      {
//        throw new RuntimeException(e);
//      } catch(NoSuchMethodException e)
//      {
//        if(Loader.isModLoaded("IC2"))
//        {
//          throw new RuntimeException(e);
//        }
//      } catch(SecurityException e)
//      {
//        throw new RuntimeException(e);
//      }
//    }
//    super.updateEntity();
//  }
  
  private void updateFoundryEnergy()
  {
    if(update_energy)
    {
      updateValue("energy",energy_stored);
    }
  }
  
  @Override
  protected void updateServer()
  {
    if(update_energy_tick)
    {
      updateFoundryEnergy();
      update_energy_tick = false;
    }
  }
  
  public void updateRedstone()
  {
    redstone_signal = world.isBlockIndirectlyGettingPowered(getPos()) > 0;
  }
  
  @Override
  public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
  {
    return (int)(receiveFoundryEnergy(maxReceive * RATIO_RF,!simulate,false) / RATIO_RF);
  }

  @Override
  public boolean canConnectEnergy(EnumFacing from)
  {
    return true;
  }

  @Override
  public int getEnergyStored(EnumFacing from)
  {
    return (int)getStoredFoundryEnergy() / RATIO_RF;
  }

  @Override
  public int getMaxEnergyStored(EnumFacing from)
  {
    return (int)getFoundryEnergyCapacity() / RATIO_RF;
  }

  @Override
  public boolean hasCapability(Capability<?> cap,EnumFacing facing)
  {
    if(tesla != null)
    {
      if(hasTeslaCapability(cap,facing))
      {
        return true;
      }
    }
    if(cap == CapabilityEnergy.ENERGY)
    {
      return true;
    } else
    {
      return super.hasCapability(cap, facing);
    }
  }
  
  @Override
  public <T> T getCapability(Capability<T> cap, EnumFacing facing)
  {
    if(tesla != null)
    {
      T tcap = getTeslaCapability(cap,facing);
      if(tcap != null)
      {
        return tcap;
      }
    }
    if(cap == CapabilityEnergy.ENERGY)
    {
      return CapabilityEnergy.ENERGY.cast(fe);
    } else
    {
      return super.getCapability(cap, facing);
    }
  }

  @Optional.Method(modid = "Tesla")
  private <T> boolean hasTeslaCapability(Capability<T> cap, EnumFacing facing)
  {
    return cap == TeslaCapabilities.CAPABILITY_CONSUMER;
  }

  @Optional.Method(modid = "Tesla")
  private <T> T getTeslaCapability(Capability<T> cap, EnumFacing facing)
  {
    if(cap == TeslaCapabilities.CAPABILITY_CONSUMER)
    {
      return TeslaCapabilities.CAPABILITY_CONSUMER.cast(tesla);
    } else
    {
      return null;
    }
  }

//  @Override
//  public void onChunkUnload()
//  {
//    try
//    {
//      getClass().getMethod("UnloadEnet").invoke(this);
//    } catch(IllegalAccessException e)
//    {
//      throw new RuntimeException(e);
//    } catch(IllegalArgumentException e)
//    {
//      throw new RuntimeException(e);
//    } catch(InvocationTargetException e)
//    {
//      throw new RuntimeException(e);
//    } catch(NoSuchMethodException e)
//    {
//      if(Loader.isModLoaded("IC2"))
//      {
//        throw new RuntimeException(e);
//      }
//    } catch(SecurityException e)
//    {
//      throw new RuntimeException(e);
//    }
//  }

//  @Optional.Method(modid = "IC2")
//  public void UnloadEnet()
//  {
//    if(added_enet)
//    {
//      MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
//      added_enet = false;
//    }
//  }
//  
//  @Optional.Method(modid = "IC2")
//  public void LoadEnet()
//  {
//    if(!added_enet && !FMLCommonHandler.instance().getEffectiveSide().isClient())
//    {
//      MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
//      added_enet = true;
//    }
//  }
//  
//  @Optional.Method(modid = "IC2")
//  @Override
//  public double getDemandedEnergy()
//  {
//    return (double)(GetEnergyCapacity() - GetStoredEnergy()) / RATIO_EU;
//  }
//
//  @Optional.Method(modid = "IC2")
//  @Override
//  public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage)
//  {
//    double use_amount = Math.max(Math.min(amount, getDemandedEnergy()), 0);
//
//    return amount - ReceiveEU(use_amount, true);
//  }
//
//  @Optional.Method(modid = "IC2")
//  @Override
//  public int getSinkTier()
//  {
//    return 1;
//  }
//  
//  @Optional.Method(modid = "IC2")
//  @Override
//  public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
//  {
//    return true;
//  }
}
