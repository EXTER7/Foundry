package exter.foundry.tileentity;

import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.recipes.manager.CastingRecipeManager;
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
  
  
  private int progress;

  public TileEntityCastingTableBase()
  {
    super();

    tank = new FluidTank(getFluidNeeded());
    
    tank_info = new FluidTankInfo[1];
    tank_info[0] = new FluidTankInfo(tank);
    progress = 0;
  }
  
  @Override
  protected final void onInitialize()
  {

  }
  
  @Override
  public final void readFromNBT(NBTTagCompound compund)
  {
    super.readFromNBT(compund);
    
    if(compund.hasKey("progress"))
    {
      progress = compund.getInteger("progress");
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
    return fillTank(0, resource, doFill);
  }

  @Override
  public final FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
  {
    if(progress > 0)
    {
      return null;
    }
    return drainTank(0, resource, doDrain);
  }

  @Override
  public final FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
  {
    if(progress > 0)
    {
      return null;
    }
    return drainTank(0, maxDrain, doDrain);
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
  
  @Override
  protected final void updateServer()
  {
    int last_progress = progress;

    if(progress > 0)
    {
      if(--progress == 0)
      {
        tank.drain(tank.getFluidAmount(), true);
        updateTank(0);
      }
    } else if(inventory[0] == null && tank.getFluid() != null && tank.getFluid().amount == getFluidNeeded())
    {
      ICastingRecipe recipe = CastingRecipeManager.instance.findRecipe(tank.getFluid(), getMold(),null);
      if(recipe != null && !recipe.requiresExtra() && recipe.getOutput().stackSize == 1 && recipe.getInput().amount == getFluidNeeded())
      {
        setInventorySlotContents(0, recipe.getOutput());
        progress = CAST_TIME;
      }
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
  
  abstract public ItemStack getMold();
  
  abstract public int getFluidNeeded();
}
