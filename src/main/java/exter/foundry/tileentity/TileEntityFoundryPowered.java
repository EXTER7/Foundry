package exter.foundry.tileentity;


import cofh.api.energy.IEnergyHandler;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import buildcraft.api.mj.IBatteryObject;
import buildcraft.api.mj.IBatteryProvider;
import buildcraft.api.mj.MjAPI;
import exter.foundry.tileentity.energy.EnergyManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Base class for all machines.
 */
public abstract class TileEntityFoundryPowered extends TileEntityFoundry implements IEnergyHandler,IEnergySink,IBatteryProvider
{
  
  private class MjBattery implements IBatteryObject
  {
    private double energy;
    private double capacity;
    private double max_received;
    private double minimum;

    @Override
    public double getEnergyRequested()
    {
      return minimum;
    }

    @Override
    public double addEnergy(double mj)
    {
      return addEnergy(mj, false);
    }

    @Override
    public double addEnergy(double mj, boolean ignoreCycleLimit)
    {
      if(mj > max_received && !ignoreCycleLimit)
      {
        mj = max_received;
      }
      double needed = capacity - energy;
      if(mj > needed)
      {
        mj = needed;
      }
      
      energy += mj;
      return mj;
    }

    @Override
    public double getEnergyStored()
    {
      return energy;
    }

    @Override
    public void setEnergyStored(double mj)
    {
      if(mj < 0)
      {
        mj = 0;
      }
      if(mj > capacity)
      {
        mj = capacity;
      }
    }

    @Override
    public double maxCapacity()
    {
      return capacity;
    }

    @Override
    public double minimumConsumption()
    {
      return minimum;
    }

    @Override
    public double maxReceivedPerCycle()
    {
      return max_received;
    }

    @Override
    public IBatteryObject reconfigure(double maxCapacity, double maxReceivedPerCycle, double minimumConsumption)
    {
      capacity = maxCapacity;
      max_received = maxReceivedPerCycle;
      minimum = minimumConsumption;
      return this;
    }

    @Override
    public String kind()
    {
      return MjAPI.DEFAULT_POWER_FRAMEWORK;
    }
    
    public MjBattery(double maxCapacity, double maxReceivedPerCycle, double minimumConsumption)
    {
      energy = 0;
      reconfigure(maxCapacity, maxReceivedPerCycle, minimumConsumption);
    }
    
    public double UseEnergy(double amount,boolean do_use)
    {
      if(energy < amount)
      {
        amount = energy;
        if(do_use)
        {
          energy = 0;
        }
        return amount;
      }
      if(do_use)
      {
        energy -= amount;
      }
      return amount;
    }
  }
  
  private boolean added_enet;
  protected EnergyManager energy_manager;
  protected boolean update_energy;
  protected boolean update_energy_tick;
  
  private MjBattery battery;

  public abstract int GetMaxStoredEnergy();

  public abstract int GetEnergyUse();
  
  public TileEntityFoundryPowered()
  {
    super();
    
    energy_manager = new EnergyManager(GetMaxStoredEnergy());
    
    update_energy = false;
    update_energy_tick = true;
    added_enet = false;
    double mj_tick = (double)GetEnergyUse() / EnergyManager.RATIO_MJ + 1;
    battery = new MjBattery(mj_tick * 50, mj_tick * 5, 1);

  }
  

  @Override
  public void readFromNBT(NBTTagCompound compound)
  {
    super.readFromNBT(compound);
    energy_manager.ReadFromNBT(compound);
  }
  
  
  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    energy_manager.WriteToNBT(compound);
  }

  protected void OnInitialize()
  {
    update_energy_tick = true;
  }
  
  @Override
  protected void UpdateEntityServer()
  {
    int last_energy = energy_manager.GetStoredEnergy();
    
    double mj = battery.UseEnergy(battery.getEnergyStored(),false);
    double used = energy_manager.ReceiveMJ(mj, false);

    energy_manager.ReceiveMJ(battery.UseEnergy(used, true), true);
    
    if(update_energy && (update_energy_tick || energy_manager.GetStoredEnergy() != last_energy))
    {
      UpdateEnergy(energy_manager);
    }
    update_energy_tick = false;
  }
  
  @Override
  public void updateEntity()
  {
    if(!added_enet)
    {
      LoadEnet();
    }
    super.updateEntity();
  }
  
  public void UpdateRedstone()
  {
    redstone_signal = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
  }
  
  @Override
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
  {
    if(!simulate && update_energy && !worldObj.isRemote)
    {
      update_energy_tick = true;
    }
    return energy_manager.ReceiveRF(maxReceive, !simulate);
  }

  @Override
  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
  {
    return 0;
  }

  @Override
  public boolean canConnectEnergy(ForgeDirection from)
  {
    return true;
  }

  @Override
  public int getEnergyStored(ForgeDirection from)
  {
    return energy_manager.GetStoredEnergy() / EnergyManager.RATIO_RF;
  }

  @Override
  public int getMaxEnergyStored(ForgeDirection from)
  {
    return GetMaxStoredEnergy() / EnergyManager.RATIO_RF;
  }
  
  @Override
  public void onChunkUnload()
  {
    if(added_enet && Loader.isModLoaded("IC2"))
    {
      MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
      added_enet = false;
    }
  }

  public void LoadEnet()
  {
    if(!added_enet && !FMLCommonHandler.instance().getEffectiveSide().isClient() && Loader.isModLoaded("IC2"))
    {
      MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
      added_enet = true;
    }
  }
  
  @Override
  public double getDemandedEnergy()
  {
    return (double)(GetMaxStoredEnergy() - energy_manager.GetStoredEnergy()) / EnergyManager.RATIO_EU;
  }

  @Override
  public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage)
  {
    double use_amount = Math.max(Math.min(amount, getDemandedEnergy()), 0);
    if(update_energy && !worldObj.isRemote)
    {
      update_energy_tick = true;
    }

    return amount - energy_manager.ReceiveEU(use_amount, true);
  }

  @Override
  public int getSinkTier()
  {
    return 1;
  }
  
  @Override
  public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
  {
    return true;
  }

  @Override
  public IBatteryObject getMjBattery(String kind)
  {
    if(kind.equals(battery.kind()))
    {
      return battery;
    }
    return null;
  }
}
