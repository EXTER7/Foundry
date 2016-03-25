package exter.foundry.tileentity;

import exter.foundry.api.heatable.IHeatable;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityInductionHeater extends TileEntityFoundryPowered
{

  static public final int ENERGY_USE = 6000;
  
  
  public TileEntityInductionHeater()
  {
    super();    
  }

  @Override
  public int getSizeInventory()
  {
    return 0;
  }


  @Override
  protected void updateClient()
  {

  }

  private IHeatable getHeatable()
  {
    TileEntity te = worldObj.getTileEntity(pos.up());
    if(te instanceof IHeatable)
    {
      return (IHeatable)te;
    }
    return null;
  }
  
  @Override
  protected void updateServer()
  {
    super.updateServer();
    

    boolean use_energy = false;
    switch(getRedstoneMode())
    {
      case RSMODE_IGNORE:
        use_energy = true;
        break;
      case RSMODE_OFF:
        use_energy = !redstone_signal;
        break;
      case RSMODE_ON:
        use_energy = redstone_signal;
        break;
      default:
        break;
    }

    if(use_energy && getStoredFoundryEnergy() > 0)
    {
      IHeatable heatable = getHeatable();
      
      int heat = heatable.getMaxHeatReceive(EnumFacing.DOWN);
      if(heat > 0)
      {
        int needed = heat / 6;
        int energy = useFoundryEnergy(needed, true);
        heatable.receiveHeat(EnumFacing.DOWN, energy * 10);
      }
    }
  }

  @Override
  public FluidTank getTank(int slot)
  {
    return null;
  }

  @Override
  public int getTankCount()
  {
    return 0;
  }

  @Override
  public int getFoundryEnergyCapacity()
  {
    return 18000;
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack)
  {
    // TODO Auto-generated method stub
    return false;
  }  
  
//  @Optional.Method(modid = "IC2")
//  @Override
//  public int getSinkTier()
//  {
//    return 2;
//  }
}
