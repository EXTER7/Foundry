package exter.foundry.tileentity;

import universalelectricity.api.UniversalClass;
import universalelectricity.api.core.grid.INodeProvider;
import universalelectricity.api.core.grid.electric.IElectricNode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Base class for all machines.
 */
@UniversalClass
public abstract class TileEntityFoundryPowered extends TileEntityFoundry implements IElectricNode
{
  protected double energy;
  protected boolean update_energy;
  protected boolean update_energy_tick;
  

  
  public TileEntityFoundryPowered()
  {
    super();
    
    energy = 0;
    
    update_energy = false;
    update_energy_tick = true;
  }
  

  @Override
  public void readFromNBT(NBTTagCompound compound)
  {
    super.readFromNBT(compound);
    if(compound.hasKey("energy"))
    {
      energy = compound.getDouble("energy");
    }
  }
  
  
  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    compound.setDouble("energy", 0);
  }

  protected void OnInitialize()
  {
    update_energy_tick = true;
  }
  
  @Override
  protected void UpdateEntityServer()
  {
    double last_energy = energy;
    
    
    if(update_energy && (update_energy_tick || energy != last_energy))
    {
      UpdateValue("energy",energy);
    }
    update_energy_tick = false;
  }
  
  public void UpdateRedstone()
  {
    redstone_signal = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
  }
  
  /** Adds energy to the node returns energy added */
  @Override
  public final double addEnergy(ForgeDirection from, double wattage, boolean doAdd)
  {
    return 0;//TODO
  }
  /** Removes energy from the node returns energy removed */
  @Override
  public final double removeEnergy(ForgeDirection from, double wattage, boolean doRemove)
  {
    return 0;
  }
  /** Current energy stored in UE joules */
  @Override
  public final double getEnergy(ForgeDirection from)
  {
    return 0;//TODO
  }
  

  protected double UseEnergy(double amount,boolean do_use)
  {
    if(amount > energy)
    {
      amount = energy;
    }
    if(do_use)
    {
      energy -= amount;
    }
    return amount;
  }
  

  @Override
  public void reconstruct()
  {

  }

  @Override
  public void deconstruct()
  {

  }
  
  @Override
  public INodeProvider getParent()
  {
    return null;
  }

}
