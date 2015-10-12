package exter.foundry.tileentity;



import cofh.api.energy.IEnergyReceiver;
//import ic2.api.energy.event.EnergyTileLoadEvent;
//import ic2.api.energy.event.EnergyTileUnloadEvent;
//import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
/**
 * Base class for all machines.
 */
//@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2")
public abstract class TileEntityFoundryPowered extends TileEntityFoundry implements IEnergyReceiver/*,IEnergySink*/
{
//  private boolean added_enet;
  protected boolean update_energy;
  protected boolean update_energy_tick;
  
  public abstract int getFoundryEnergyCapacity();
  
  public TileEntityFoundryPowered()
  {
    super();    
    
    update_energy = false;
    update_energy_tick = true;
//    added_enet = false;
  }

  static public int RATIO_RF = 10;
  static public int RATIO_EU = 40;
  
  private int energy_stored;
  
  private int receiveFoundryEnergy(int en,boolean do_receive, boolean allow_overflow)
  {
    if(!allow_overflow)
    {
      int needed = getFoundryEnergyCapacity() - energy_stored;
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
        if(update_energy && !worldObj.isRemote)
        {
          update_energy_tick = true;
        }
      }
    }
    return en;
  }
  
  private int receiveRF(int rf,boolean do_receive)
  {
    return receiveFoundryEnergy(rf * RATIO_RF,do_receive,false) / RATIO_RF;
  }
  
//  private double receiveEU(double eu,boolean do_receive)
//  {
//    return (double)receiveFoundryEnergy((int)(eu * RATIO_EU),do_receive,true) / RATIO_EU;
//  }
  
  public int useFoundryEnergy(int amount,boolean do_use)
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
  
  public int getStoredFoundryEnergy()
  {
    int capacity = getFoundryEnergyCapacity();
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
      energy_stored = compound.getInteger("energy");
    }
  }
  
  
  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    compound.setInteger("energy", energy_stored);
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
    redstone_signal = worldObj.isBlockIndirectlyGettingPowered(getPos()) > 0;
  }
  
  @Override
  public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
  {
    return receiveRF(maxReceive, !simulate);
  }

  @Override
  public boolean canConnectEnergy(EnumFacing from)
  {
    return true;
  }

  @Override
  public int getEnergyStored(EnumFacing from)
  {
    return getStoredFoundryEnergy() / RATIO_RF;
  }

  @Override
  public int getMaxEnergyStored(EnumFacing from)
  {
    return getFoundryEnergyCapacity() / RATIO_RF;
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
