package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import exter.foundry.recipes.manager.AlloyingCrucibleRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityAlloyingCrucible extends TileEntityFoundry
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
      int i;
      int empty = -1;
      int partial = -1;
      for(i = 0; i < 2; i++)
      {
        FluidTank ft = tanks[i];
        if(ft.getFluidAmount() > 0)
        { 
          if(ft.getFluid().isFluidEqual(resource))
          {
            if(ft.getFluidAmount() < ft.getCapacity())
            {
              partial = i;
            } else
            {
              return 0;
            }
          }
        } else
        {
          empty = i;
        }
      }

      if(partial != -1)
      {
        return fillTank(partial, resource, doFill);
      }
      if(empty != -1)
      {
        return fillTank(empty, resource, doFill);
      }
      return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
      return drainTank(TANK_OUTPUT, resource, doDrain);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
      return drainTank(TANK_OUTPUT, maxDrain, doDrain);
    }    
  }

  static public final int INVENTORY_CONTAINER_INPUT_A_DRAIN = 0;
  static public final int INVENTORY_CONTAINER_INPUT_A_FILL = 1;
  static public final int INVENTORY_CONTAINER_INPUT_B_DRAIN = 2;
  static public final int INVENTORY_CONTAINER_INPUT_B_FILL = 3;
  static public final int INVENTORY_CONTAINER_OUTPUT_DRAIN = 4;
  static public final int INVENTORY_CONTAINER_OUTPUT_FILL = 5;

  
  static public final int TANK_INPUT_A = 0;
  static public final int TANK_INPUT_B = 1;
  static public final int TANK_OUTPUT = 2;

  public int progress;
  
  
  private FluidTank[] tanks;
  private IFluidHandler fluid_handler;


 
  public TileEntityAlloyingCrucible()
  {
    super();
    
    tanks = new FluidTank[3];
    for(int i = 0; i < tanks.length; i++)
    {
      tanks[i] = new FluidTank(FoundryAPI.ALLOYING_CRUCIBLE_TANK_CAPACITY);
    }
    fluid_handler = new FluidHandler();
    progress = 0;
    
    addContainerSlot(new ContainerSlot(TANK_INPUT_A,INVENTORY_CONTAINER_INPUT_A_DRAIN,false));
    addContainerSlot(new ContainerSlot(TANK_INPUT_A,INVENTORY_CONTAINER_INPUT_A_FILL,true));
    addContainerSlot(new ContainerSlot(TANK_INPUT_B,INVENTORY_CONTAINER_INPUT_B_DRAIN,false));
    addContainerSlot(new ContainerSlot(TANK_INPUT_B,INVENTORY_CONTAINER_INPUT_B_FILL,true));
    addContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_DRAIN,false));
    addContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_FILL,true));
  }
  
  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack)
  {
    return false;
  }
  
  @Override
  protected IFluidHandler getFluidHandler(EnumFacing facing)
  {
    return fluid_handler;
  }

  @Override
  public int getSizeInventory()
  {
    return 6;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag)
  {
    super.readFromNBT(tag);
    if(tag.hasKey("Progress"))
    {
      progress = tag.getInteger("Progress");
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
    compound.setInteger("Progress", progress);
    return compound;
  }


  @Override
  protected void updateClient()
  {

  }
  
  private boolean canOutput(IAlloyingCrucibleRecipe recipe)
  {
    FluidStack output = recipe.getOutput();
    return tanks[TANK_OUTPUT].fill(output, false) == output.amount;
  }

  private void doMix(IAlloyingCrucibleRecipe recipe,boolean reversed)
  {
    if(reversed)
    {
      drainTank(TANK_INPUT_B, recipe.getInputA(), true);
      drainTank(TANK_INPUT_A, recipe.getInputB(), true);
    } else
    {
      drainTank(TANK_INPUT_A, recipe.getInputA(), true);
      drainTank(TANK_INPUT_B, recipe.getInputB(), true);
    }
    fillTank(TANK_OUTPUT, recipe.getOutput(), true);
  }
  
  @Override
  protected void updateServer()
  {
    int last_progress = progress;

    boolean reversed = false;
    IAlloyingCrucibleRecipe recipe = null;
    if(tanks[TANK_INPUT_A].getFluid() != null && tanks[TANK_INPUT_B].getFluid() != null)
    {
      recipe = AlloyingCrucibleRecipeManager.instance.findRecipe(tanks[TANK_INPUT_A].getFluid(), tanks[TANK_INPUT_B].getFluid());
      if(recipe == null)
      {
        recipe = AlloyingCrucibleRecipeManager.instance.findRecipe(tanks[TANK_INPUT_B].getFluid(), tanks[TANK_INPUT_A].getFluid());
        if(recipe != null)
        {
          reversed = true;
        }
      }
    }

    if(recipe != null && canOutput(recipe))
    {
      if(++progress >= FoundryMiscUtils.divCeil(recipe.getOutput().amount,2))
      {
        progress = 0;
        doMix(recipe,reversed);
      }
    } else
    {
      progress = 0;
    }

    if(last_progress != progress)
    {
      updateValue("Progress",progress);
    }
  }

  @Override
  public FluidTank getTank(int slot)
  {
    return tanks[slot];
  }

  @Override
  public int getTankCount()
  {
    return 3;
  }

  @Override
  protected void onInitialize()
  {

  }
}
