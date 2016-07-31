package exter.foundry.tileentity;

import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public abstract class TileEntityCastingTableBase extends TileEntityFoundry implements ISidedInventory,net.minecraftforge.fluids.IFluidHandler
{
  protected class FluidHandler implements IFluidHandler
  {
    private IFluidTankProperties[] props;
    
    public FluidHandler()
    {
      props = new IFluidTankProperties[getTankCount()];
      for(int i = 0; i < props.length; i++)
      {
        props[i] = new FluidTankPropertiesWrapper(getTank(i));
      }
    }

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
      return props;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
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
    public FluidStack drain(FluidStack resource, boolean doDrain)
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
    public FluidStack drain(int maxDrain, boolean doDrain)
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
  }
  
  static public final int CAST_TIME = 300;
  
  private FluidTank tank;
  private IFluidHandler fluid_handler;
  
  private ICastingTableRecipe recipe;
  
  private int progress;

  public TileEntityCastingTableBase()
  {
    super();

    tank = new FluidTank(getDefaultCapacity());    
    fluid_handler = new FluidHandler();
    
    progress = 0;
    recipe = null;
  }
  
  @Override
  protected IFluidHandler getFluidHandler(EnumFacing facing)
  {
    return fluid_handler;
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
    if(worldObj.isRemote && compound.hasKey("tank_capacity"))
    {
      tank.setCapacity(compound.getInteger("tank_capacity"));
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
  protected void updateClient()
  {
    
  }
  
  private void setRecipe(FluidStack fluid)
  {
    if(fluid == null || fluid.amount == 0)
    {
      recipe = null;
      tank.setCapacity(getDefaultCapacity());
      updateValue("tank_capacity",tank.getCapacity());
      return;
    }
    
    recipe = CastingTableRecipeManager.instance.findRecipe(fluid, getTableType());
    if(recipe != null)
    {
      if(recipe.getOutput() == null)
      {
        recipe = null;
        tank.setCapacity(getDefaultCapacity());
        updateValue("tank_capacity",tank.getCapacity());
        return;
      }
      tank.setCapacity(recipe.getInput().amount);
      updateValue("tank_capacity",tank.getCapacity());
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
    } else if(inventory[0] == null && recipe != null && tank.getFluid().amount == recipe.getInput().amount)
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
