package exter.foundry.tileentity;


import cofh.api.energy.IEnergyReceiver;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.Optional;
/**
 * Base class for all machines.
 */
@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2")
public abstract class TileEntityFoundryPowered extends TileEntityFoundry implements IEnergyReceiver,IEnergySink
{
  private boolean added_enet;
  protected boolean update_energy;
  protected boolean update_energy_tick;
  
  public abstract int GetEnergyCapacity();
  
  public TileEntityFoundryPowered()
  {
    super();    
    
    update_energy = false;
    update_energy_tick = true;
    added_enet = false;
  }

  static public int RATIO_RF = 10;
  static public int RATIO_EU = 40;
  
  private int energy_stored;
  
  private int ReceiveEnergy(int en,boolean do_receive, boolean allow_overflow)
  {
    if(!allow_overflow)
    {
      int needed = GetEnergyCapacity() - energy_stored;
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
  
  private int ReceiveRF(int rf,boolean do_receive)
  {
    return ReceiveEnergy(rf * RATIO_RF,do_receive,false) / RATIO_RF;
  }
  
  private double ReceiveEU(double eu,boolean do_receive)
  {
    return (double)ReceiveEnergy((int)(eu * RATIO_EU),do_receive,true) / RATIO_EU;
  }
  
  public int UseEnergy(int amount,boolean do_use)
  {
    if(amount > energy_stored)
    {
      amount = energy_stored;
    }
    if(do_use)
    {
      energy_stored -= amount;
      UpdateEnergy();
    }
    return amount;
  }
  
  public int GetStoredEnergy()
  {
    int capacity = GetEnergyCapacity();
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

  protected void OnInitialize()
  {
    update_energy_tick = true;
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
  
  private void UpdateEnergy()
  {
    if(update_energy)
    {
      UpdateValue("energy",energy_stored);
    }
  }
  
  @Override
  protected void UpdateEntityServer()
  {
    if(update_energy_tick)
    {
      UpdateEnergy();
      update_energy_tick = false;
    }
  }
  
  public void UpdateRedstone()
  {
    redstone_signal = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
  }
  
  @Override
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
  {
    return ReceiveRF(maxReceive, !simulate);
  }

  @Override
  public boolean canConnectEnergy(ForgeDirection from)
  {
    return true;
  }

  @Override
  public int getEnergyStored(ForgeDirection from)
  {
    return GetStoredEnergy() / RATIO_RF;
  }

  @Override
  public int getMaxEnergyStored(ForgeDirection from)
  {
    return GetEnergyCapacity() / RATIO_RF;
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
  
  @Optional.Method(modid = "IC2")
  @Override
  public double getDemandedEnergy()
  {
    return (double)(GetEnergyCapacity() - GetStoredEnergy()) / RATIO_EU;
  }

  @Optional.Method(modid = "IC2")
  @Override
  public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage)
  {
    double use_amount = Math.max(Math.min(amount, getDemandedEnergy()), 0);

    return amount - ReceiveEU(use_amount, true);
  }

  @Optional.Method(modid = "IC2")
  @Override
  public int getSinkTier()
  {
    return 1;
  }
  
  @Optional.Method(modid = "IC2")
  @Override
  public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
  {
    return true;
  }
}
