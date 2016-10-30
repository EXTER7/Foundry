package exter.foundry.tileentity;

import java.util.List;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IAlloyMixerRecipe;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityAlloyMixer extends TileEntityFoundryPowered
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
      for(i = 0; i < 4; i++)
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

  
  static public final int INVENTORY_CONTAINER_INPUT_0_DRAIN = 0;
  static public final int INVENTORY_CONTAINER_INPUT_0_FILL = 1;
  static public final int INVENTORY_CONTAINER_INPUT_1_DRAIN = 2;
  static public final int INVENTORY_CONTAINER_INPUT_1_FILL = 3;
  static public final int INVENTORY_CONTAINER_INPUT_2_DRAIN = 4;
  static public final int INVENTORY_CONTAINER_INPUT_2_FILL = 5;
  static public final int INVENTORY_CONTAINER_INPUT_3_DRAIN = 6;
  static public final int INVENTORY_CONTAINER_INPUT_3_FILL = 7;
  static public final int INVENTORY_CONTAINER_OUTPUT_DRAIN = 8;
  static public final int INVENTORY_CONTAINER_OUTPUT_FILL = 9;

  
  static public final int TANK_INPUT_0 = 0;
  static public final int TANK_INPUT_1 = 1;
  static public final int TANK_INPUT_2 = 2;
  static public final int TANK_INPUT_3 = 3;
  static public final int TANK_OUTPUT = 4;

  private FluidTank[] tanks;
  private IFluidHandler fluid_handler;

   
 
  public TileEntityAlloyMixer()
  {
    super();
    
    tanks = new FluidTank[5];
    for(int i = 0; i < tanks.length; i++)
    {
      tanks[i] = new FluidTank(FoundryAPI.ALLOYMIXER_TANK_CAPACITY);
    }
    fluid_handler = new FluidHandler();
    
    addContainerSlot(new ContainerSlot(TANK_INPUT_0,INVENTORY_CONTAINER_INPUT_0_DRAIN,false));
    addContainerSlot(new ContainerSlot(TANK_INPUT_0,INVENTORY_CONTAINER_INPUT_0_FILL,true));
    addContainerSlot(new ContainerSlot(TANK_INPUT_1,INVENTORY_CONTAINER_INPUT_1_DRAIN,false));
    addContainerSlot(new ContainerSlot(TANK_INPUT_1,INVENTORY_CONTAINER_INPUT_1_FILL,true));
    addContainerSlot(new ContainerSlot(TANK_INPUT_2,INVENTORY_CONTAINER_INPUT_2_DRAIN,false));
    addContainerSlot(new ContainerSlot(TANK_INPUT_2,INVENTORY_CONTAINER_INPUT_2_FILL,true));
    addContainerSlot(new ContainerSlot(TANK_INPUT_3,INVENTORY_CONTAINER_INPUT_3_DRAIN,false));
    addContainerSlot(new ContainerSlot(TANK_INPUT_3,INVENTORY_CONTAINER_INPUT_3_FILL,true));
    addContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_DRAIN,false));
    addContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_FILL,true));
  }
  
  @Override
  protected IFluidHandler getFluidHandler(EnumFacing facing)
  {
    return fluid_handler;
  }

  @Override
  public int getSizeInventory()
  {
    return 10;
  }

  @Override
  public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
    return false;
  }
  
  @Override
  protected void updateClient()
  {

  }
  
  private int[] recipe_order = new int[4];
  private FluidStack[] input_tank_fluids = new FluidStack[4];

  private void mixAlloy()
  {
    if(getStoredFoundryEnergy() < 10)
    {
      return;
    }
    boolean do_mix = false;
    switch(getRedstoneMode())
    {
      case RSMODE_IGNORE:
        do_mix = true;
        break;
      case RSMODE_OFF:
        if(!redstone_signal && !last_redstone_signal)
        {
          do_mix = true;
        }
        break;
      case RSMODE_ON:
        if(redstone_signal && last_redstone_signal)
        {
          do_mix = true;
        }
        break;
      default:
        break;
    }
    if(!do_mix)
    {
      return;
    }

    int i;
    for(i = 0; i < 4; i++)
    {
      input_tank_fluids[i] = tanks[i].getFluid();
    }    

    IAlloyMixerRecipe recipe = AlloyMixerRecipeManager.instance.findRecipe(input_tank_fluids, recipe_order);
    if(recipe == null)
    {
      return;
    }
    int energy_used = 0;
    while(true)
    {
      if(energy_used >= 2500)
      {
        return;
      }
      if(!recipe.matchesRecipe(input_tank_fluids, recipe_order))
      {
        return;
      }
      FluidStack output = recipe.getOutput();

      if(tanks[TANK_OUTPUT].fill(output, false) < output.amount)
      {
        return;
      }
      int required_energy = 10 * output.amount;
      if(useFoundryEnergy(required_energy, false) < required_energy)
      {
        return;
      }
      useFoundryEnergy(required_energy, true);
      energy_used += required_energy;
      tanks[TANK_OUTPUT].fill(output, true);
      updateTank(TANK_OUTPUT);
      List<FluidStack> inputs = recipe.getInputs();
      for(i = 0; i < inputs.size(); i++)
      {
        tanks[recipe_order[i]].drain(inputs.get(i).amount, true);
        updateTank(recipe_order[i]);
      }
    }
  }

  @Override
  protected void updateServer()
  {
    super.updateServer();
    if(tanks[TANK_OUTPUT].getFluidAmount() < tanks[TANK_OUTPUT].getCapacity()
        && (tanks[TANK_INPUT_0].getFluidAmount() > 0
        ||  tanks[TANK_INPUT_1].getFluidAmount() > 0
        ||  tanks[TANK_INPUT_2].getFluidAmount() > 0
        ||  tanks[TANK_INPUT_3].getFluidAmount() > 0))
    {
      mixAlloy();
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
    return 5;
  }

  @Override
  public long getFoundryEnergyCapacity()
  {
    return 3000;
  }
}
