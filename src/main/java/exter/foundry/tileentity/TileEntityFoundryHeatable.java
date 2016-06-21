package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.heatable.IHeatProvider;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;


public abstract class TileEntityFoundryHeatable extends TileEntityFoundry
{
  static public final int TEMP_MIN = 29000;
  
  private int heat;

  
  
  public TileEntityFoundryHeatable()
  {
    super();
    heat = TEMP_MIN;
  }
  

  @Override
  public void readFromNBT(NBTTagCompound compund)
  {
    super.readFromNBT(compund);
    
    if(compund.hasKey("heat"))
    {
      heat = compund.getInteger("heat");
      if(heat < TEMP_MIN)
      {
        heat = TEMP_MIN;
      }
      int temp_max = getMaxTemperature();
      if(heat > temp_max)
      {
        heat = temp_max;
      }
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
    compound.setInteger("heat", heat);
    return compound;
  }

  @Override
  public int getSizeInventory()
  {
    return 3;
  }

  public int getHeat()
  {
    return heat;
  }

  private IHeatProvider getHeatProvider()
  {
    TileEntity te = worldObj.getTileEntity(getPos().down());
    if(te != null && te.hasCapability(FoundryAPI.capability_heatprovider, EnumFacing.UP))
    {
      return te.getCapability(FoundryAPI.capability_heatprovider, EnumFacing.UP);
    }
    return null;
  }

  @Override
  protected void updateServer()
  {
    int last_heat = heat;
    
    int temp_max = getMaxTemperature();

    if(canReceiveHeat())
    {
      IHeatProvider heater = getHeatProvider();

      if(heater != null)
      {
        heat += heater.provideHeat(getMaxHeatRecieve(temp_max,getTemperatureLossRate()));
      }
    }
    heat -= (heat - TEMP_MIN) / getTemperatureLossRate();
    if(heat > temp_max)
    {
      heat = temp_max;
    }
    if(heat < TEMP_MIN)
    {
      heat = TEMP_MIN;
    }
    if(last_heat / 100 != heat / 100)
    {
      updateValue("heat",heat);
    }
  }
  
  protected abstract boolean canReceiveHeat();
  
  protected abstract int getMaxTemperature();
  
  abstract protected int getTemperatureLossRate();
  
  static public int getMaxHeatRecieve(int max_heat, int temp_loss_rate)
  {
    return (max_heat - TEMP_MIN) / temp_loss_rate;
  }
}
