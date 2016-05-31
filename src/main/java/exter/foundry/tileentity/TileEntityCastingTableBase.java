package exter.foundry.tileentity;

import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class TileEntityCastingTableBase extends TileEntityFoundry implements ISidedInventory,IFluidHandler
{
  static public final int CAST_TIME = 300;
  
  private FluidTank tank;
  private FluidTankInfo[] tank_info;
  private ICastingTableRecipe recipe;
  
  private int progress;

  public TileEntityCastingTableBase()
  {
    super();

    tank = new FluidTank(getDefaultCapacity());
    
    tank_info = new FluidTankInfo[1];
    tank_info[0] = new FluidTankInfo(tank);
    progress = 0;
    recipe = null;
  }
  
  @Override
  protected final void onInitialize()
  {

  }
  
  @Override
  public final void readFromNBT(NBTTagCompound compound)
  {
    super.readFromNBT(compound);
    
    if(compound.hasKey("Tank_0"))
    {
      setRecipe(tank.getFluid());
    }
    if(compound.hasKey("progress"))
    {
      progress = compound.getInteger("progress");
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
    compound.setInteger("progress", progress);
    return compound;
  }


  @Override
  public final int getSizeInventory()
  {
    return 1;
  }

  public final int getProgress()
  {
    return progress;
  }

  static private final int[] EXTRACT_SLOTS = { 0 };

  @Override
  public final boolean isItemValidForSlot(int slot, ItemStack itemstack)
  {
    return false;
  }

  @Override
  public final int[] getSlotsForFace(EnumFacing side)
  {
    return EXTRACT_SLOTS;
  }

  @Override
  public final boolean canInsertItem(int slot, ItemStack itemstack, EnumFacing side)
  {
    return false;
  }

  @Override
  public final boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing side)
  {
    return progress == 0;
  }

  @Override
  public final ItemStack removeStackFromSlot(int slot)
  {
    if(progress > 0)
    {
      return null;
    }
    return super.removeStackFromSlot(slot);
  }
  
  @Override
  public final int fill(EnumFacing from, FluidStack resource, boolean doFill)
  {
    if(inventory[0] != null)
    {
      return 0;
    }
    if(doFill && ( tank.getFluid() == null || tank.getFluid().amount == 0))
    {
      setRecipe(resource);
    }
    return fillTank(0, resource, doFill);
  }

  @Override
  public final FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
  {
    if(progress > 0)
    {
      return null;
    }
    FluidStack result = drainTank(0, resource, doDrain);
    if(doDrain)
    {
      setRecipe(tank.getFluid());      
    }
    return result;
  }

  @Override
  public final FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
  {
    if(progress > 0)
    {
      return null;
    }
    FluidStack result = drainTank(0, maxDrain, doDrain);
    if(doDrain)
    {
      setRecipe(tank.getFluid());      
    }
    return result;
  }

  @Override
  public final boolean canFill(EnumFacing from, Fluid fluid)
  {
    return inventory[0] == null;
  }

  @Override
  public final boolean canDrain(EnumFacing from, Fluid fluid)
  {
    return progress == 0;
  }

  @Override
  public final FluidTankInfo[] getTankInfo(EnumFacing from)
  {
    return tank_info;
  }

  @Override
  protected void updateClient()
  {
    
  }
  
  private void setRecipe(FluidStack fluid)
  {
    if(fluid == null || fluid.amount == 0)
    {
      recipe = null;
      tank.setCapacity(getDefaultCapacity());
      return;
    }
    
    recipe = CastingTableRecipeManager.instance.findRecipe(fluid, getTableType());
    if(recipe != null)
    {
      tank.setCapacity(recipe.getInput().amount);
    }
  }
  
  @Override
  protected final void updateServer()
  {
    int last_progress = progress;

    if(progress > 0)
    {
      if(--progress == 0)
      {
        tank.setFluid(null);
        updateTank(0);
        setRecipe(null);
      }
    } else if(inventory[0] == null && recipe != null && tank.getFluid().amount == tank.getCapacity())
    {
      setInventorySlotContents(0, recipe.getOutput());
      progress = CAST_TIME;
    }
    
    if(last_progress != progress)
    {
      updateValue("progress",progress);
    }
  }

  @Override
  public final FluidTank getTank(int slot)
  {
    return tank;
  }

  @Override
  public final int getTankCount()
  {
    return 1;
  }
  
  
  abstract public int getDefaultCapacity();

  abstract public ICastingTableRecipe.TableType getTableType();
}
