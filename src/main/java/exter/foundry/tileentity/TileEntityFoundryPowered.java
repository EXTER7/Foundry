package exter.foundry.tileentity;


import cofh.api.energy.IEnergyHandler;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import exter.foundry.tileentity.energy.EnergyManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Base class for all machines.
 */
public abstract class TileEntityFoundryPowered extends TileEntityFoundry implements IPowerReceptor,IEnergyHandler,IEnergySink
{
  private boolean added_enet;
  private PowerHandler power_handler;
  protected EnergyManager energy_manager;
  protected boolean update_energy;
  protected boolean update_energy_tick;

  public abstract int GetMaxStoredEnergy();

  public abstract int GetEnergyUse();
  
  public TileEntityFoundryPowered()
  {
    super();
    power_handler = new PowerHandler(this,PowerHandler.Type.MACHINE);
    
    float mj_tick = (float)GetEnergyUse() / EnergyManager.RATIO_MJ + 1;
    
    power_handler.configure(2, mj_tick * 5, mj_tick / 2, mj_tick * 30);
    power_handler.configurePowerPerdition(0, 0);

    energy_manager = new EnergyManager(GetMaxStoredEnergy());
    
    update_energy = false;
    update_energy_tick = true;
    added_enet = false;
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
    double mj_tick = (double)GetEnergyUse() / EnergyManager.RATIO_MJ + 1;
    
    double received = energy_manager.ReceiveMJ(power_handler.useEnergy(0, mj_tick, false),false);
    
    received = power_handler.useEnergy(0, received, true);
    energy_manager.ReceiveMJ(received, true);

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
    power_handler.update();
  }
  
  @Override
  public PowerReceiver getPowerReceiver(ForgeDirection side)
  {
    return power_handler.getPowerReceiver();
  }

  @Override
  public void doWork(PowerHandler workProvider)
  {
  }

  @Override
  public World getWorld()
  {
    return worldObj;
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
  public double demandedEnergyUnits()
  {
    return (double)(GetMaxStoredEnergy() - energy_manager.GetStoredEnergy()) / EnergyManager.RATIO_EU;
  }

  @Override
  public double injectEnergyUnits(ForgeDirection directionFrom, double amount)
  {
    double use_amount = Math.max(Math.min(amount, getMaxSafeInput()), 0);
    if(update_energy && !worldObj.isRemote)
    {
      update_energy_tick = true;
    }

    return amount - energy_manager.ReceiveEU(use_amount, true);
  }

  @Override
  public int getMaxSafeInput()
  {
    return 32;
  }
  @Override
  public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
  {
    return true;
  }
}
