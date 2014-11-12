package exter.foundry.tileentity;


import cofh.api.energy.IEnergyHandler;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import exter.foundry.tileentity.energy.EnergyManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.Optional;
/**
 * Base class for all machines.
 */
@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2")
public abstract class TileEntityFoundryPowered extends TileEntityFoundry implements IEnergyHandler,IEnergySink
{
  private boolean added_enet;
  protected EnergyManager energy_manager;
  protected boolean update_energy;
  protected boolean update_energy_tick;
  
  public abstract int GetMaxStoredEnergy();

  public abstract int GetEnergyUse();
  
  public TileEntityFoundryPowered()
  {
    super();
    
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
  public void updateEntity()
  {
    if(!added_enet)
    {
      LoadEnet();
    }
    super.updateEntity();
  }
  
  @Override
  protected void UpdateEntityServer()
  {
    if(update_energy_tick)
    {
      UpdateEnergy(energy_manager);
    }
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
  
  @Optional.Method(modid = "IC2")
  @Override
  public double getDemandedEnergy()
  {
    return (double)(GetMaxStoredEnergy() - energy_manager.GetStoredEnergy()) / EnergyManager.RATIO_EU;
  }

  @Optional.Method(modid = "IC2")
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
