package exter.foundry.tileentity;

import exter.foundry.api.heatable.IHeatProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityInductionHeater extends TileEntityFoundryPowered implements IHeatProvider
{
  private static int MAX_PROVIDE = TileEntityMeltingCrucible.getMaxHeatRecieve(350000);
  
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
  
  @Override
  protected void updateServer()
  {
    super.updateServer();
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
    return 25000;
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack)
  {
    return false;
  }  
  
  @Override
  public int provideHeat(EnumFacing side, int max_heat)
  {
    if(side == EnumFacing.UP)
    {
      if(max_heat > MAX_PROVIDE)
      {
        max_heat = MAX_PROVIDE;
      }
      return useFoundryEnergy(max_heat * 3, true) / 3;
    }
    return 0;
  }

  
//  @Optional.Method(modid = "IC2")
//  @Override
//  public int getSinkTier()
//  {
//    return 2;
//  }
}
