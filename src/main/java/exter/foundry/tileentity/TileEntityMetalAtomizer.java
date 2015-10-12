package exter.foundry.tileentity;


import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.container.ContainerMetalAtomizer;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityMetalAtomizer extends TileEntityFoundryPowered implements ISidedInventory,IFluidHandler
{
  
  static private final int NETDATAID_TANK_FLUID = 1;
  static private final int NETDATAID_TANK_AMOUNT = 2;
  static private final int NETDATAID_WATER_TANK_FLUID = 3;
  static private final int NETDATAID_WATER_TANK_AMOUNT = 4;

  static public final int ATOMIZE_TIME = 500000;
  
  static public final int ENERGY_REQUIRED = 15000;
  
  static public final int INVENTORY_OUTPUT = 0;
  static public final int INVENTORY_CONTAINER_DRAIN = 1;
  static public final int INVENTORY_CONTAINER_FILL = 2;
  static public final int INVENTORY_CONTAINER_WATER_DRAIN = 3;
  static public final int INVENTORY_CONTAINER_WATER_FILL = 4;

  static public final int TANK_INPUT = 0;
  static public final int TANK_WATER = 1;
  
  private ItemStack[] inventory;
  private FluidTank[] tanks;
  private FluidTankInfo[] tank_info;
  IAtomizerRecipe current_recipe;
  
  private int progress;

  private final FluidStack water_required = new FluidStack(FluidRegistry.WATER,50);
  
  public TileEntityMetalAtomizer()
  {
    super();

    tanks = new FluidTank[2];
    tanks[TANK_INPUT] = new FluidTank(FoundryAPI.ATOMIZER_TANK_CAPACITY);
    tanks[TANK_WATER] = new FluidTank(FoundryAPI.ATOMIZER_WATER_TANK_CAPACITY);
    
    tank_info = new FluidTankInfo[2];
    tank_info[TANK_INPUT] = new FluidTankInfo(tanks[TANK_INPUT]);
    tank_info[TANK_WATER] = new FluidTankInfo(tanks[TANK_WATER]);
    progress = -1;
    inventory = new ItemStack[5];
    
    current_recipe = null;
    
    AddContainerSlot(new ContainerSlot(TANK_INPUT,INVENTORY_CONTAINER_DRAIN,false));
    AddContainerSlot(new ContainerSlot(TANK_INPUT,INVENTORY_CONTAINER_FILL,true));
    AddContainerSlot(new ContainerSlot(TANK_WATER,INVENTORY_CONTAINER_WATER_DRAIN,false,FluidRegistry.WATER));
    AddContainerSlot(new ContainerSlot(TANK_WATER,INVENTORY_CONTAINER_WATER_FILL,true,FluidRegistry.WATER));
   
    update_energy = true;
  }
  
  
  @Override
  public void readFromNBT(NBTTagCompound compund)
  {
    super.readFromNBT(compund);
    
    if(compund.hasKey("progress"))
    {
      progress = compund.getInteger("progress");
    }
  }


  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    compound.setInteger("progress", progress);
  }

  public void GetGUINetworkData(int id, int value)
  {
    switch(id)
    {
      case NETDATAID_TANK_FLUID:
        setTankFluid(tanks[TANK_INPUT],value);
        break;
      case NETDATAID_TANK_AMOUNT:
        setTankAmount(tanks[TANK_INPUT],value);
        break;
      case NETDATAID_WATER_TANK_FLUID:
        setTankFluid(tanks[TANK_WATER],value);
        break;
      case NETDATAID_WATER_TANK_AMOUNT:
        setTankAmount(tanks[TANK_WATER],value);
        break;
    }
  }

  public void SendGUINetworkData(ContainerMetalAtomizer container, ICrafting crafting)
  {
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_FLUID, getTankFluid(tanks[TANK_INPUT]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_AMOUNT, getTankAmount(tanks[TANK_INPUT]));
    crafting.sendProgressBarUpdate(container, NETDATAID_WATER_TANK_FLUID, getTankFluid(tanks[TANK_WATER]));
    crafting.sendProgressBarUpdate(container, NETDATAID_WATER_TANK_AMOUNT, getTankAmount(tanks[TANK_WATER]));
  }
  
  @Override
  public int getSizeInventory()
  {
    return 5;
  }

  @Override
  public ItemStack getStackInSlot(int slot)
  {
    return inventory[slot];
  }

  @Override
  public ItemStack decrStackSize(int slot, int amount)
  {
    if(inventory[slot] != null)
    {
      ItemStack is;

      if(inventory[slot].stackSize <= amount)
      {
        is = inventory[slot];
        inventory[slot] = null;
        markDirty();
        return is;
      } else
      {
        is = inventory[slot].splitStack(amount);

        if(inventory[slot].stackSize == 0)
        {
          inventory[slot] = null;
        }

        markDirty();
        return is;
      }
    } else
    {
      return null;
    }
  }

  @Override
  public ItemStack getStackInSlotOnClosing(int slot)
  {
    if(inventory[slot] != null)
    {
      ItemStack is = inventory[slot];
      inventory[slot] = null;
      return is;
    } else
    {
      return null;
    }
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack stack)
  {
    inventory[slot] = stack;

    if(stack != null && stack.stackSize > this.getInventoryStackLimit())
    {
      stack.stackSize = this.getInventoryStackLimit();
    }

    markDirty();
  }

  @Override
  public int getInventoryStackLimit()
  {
    return 64;
  }

  public int GetProgress()
  {
    return progress;
  }

  static private final int[] EXTRACT_SLOTS = { INVENTORY_OUTPUT };

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemstack)
  {
    return false;
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side)
  {
    return EXTRACT_SLOTS;
  }

  @Override
  public boolean canInsertItem(int slot, ItemStack itemstack, EnumFacing side)
  {
    return isItemValidForSlot(slot, itemstack);
  }

  @Override
  public boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing side)
  {
    return slot == INVENTORY_OUTPUT;
  }

  @Override
  public int fill(EnumFacing from, FluidStack resource, boolean doFill)
  {
    if(resource != null && resource.getFluid() == FluidRegistry.WATER)
    {
      return tanks[TANK_WATER].fill(resource, doFill);
    }
    return tanks[TANK_INPUT].fill(resource, doFill);
  }

  @Override
  public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
  {
    if(resource.isFluidEqual(tanks[TANK_INPUT].getFluid()))
    {
      return tanks[TANK_INPUT].drain(resource.amount, doDrain);
    }
    return null;
  }

  @Override
  public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
  {
    return tanks[TANK_INPUT].drain(maxDrain, doDrain);
  }

  @Override
  public boolean canFill(EnumFacing from, Fluid fluid)
  {
    return true;
  }

  @Override
  public boolean canDrain(EnumFacing from, Fluid fluid)
  {
    return true;
  }

  @Override
  public FluidTankInfo[] getTankInfo(EnumFacing from)
  {
    return tank_info;
  }

  @Override
  protected void updateClient()
  {
    
  }
  
  private void CheckCurrentRecipe()
  {
    if(current_recipe == null)
    {
      progress = -1;
      return;
    }
    
    if(!current_recipe.matchesRecipe(tanks[TANK_INPUT].getFluid()))
    {
      progress = -1;
      current_recipe = null;
      return;
    }
  }
  
  private void BeginAtomizing()
  {
    if(current_recipe != null && CanAtomizeCurrentRecipe() && getStoredFoundryEnergy() >= ENERGY_REQUIRED)
    {
      useFoundryEnergy(ENERGY_REQUIRED, true);
      progress = 0;
    }
  }
  
  private boolean CanAtomizeCurrentRecipe()
  {
    if(tanks[TANK_WATER].getFluid() == null || !tanks[TANK_WATER].getFluid().containsFluid(water_required))
    {
      return false;
    }
    
    ItemStack recipe_output = current_recipe.getOutputItem();

    ItemStack inv_output = inventory[INVENTORY_OUTPUT];
    if(inv_output != null && (!inv_output.isItemEqual(recipe_output) || inv_output.stackSize >= inv_output.getMaxStackSize()))
    {
      return false;
    }
    return true;
  }

  @Override
  protected void updateServer()
  {
    super.updateServer();
    int last_progress = progress;
    
    CheckCurrentRecipe();
    
    if(current_recipe == null)
    {
      current_recipe = AtomizerRecipeManager.instance.findRecipe(tanks[TANK_INPUT].getFluid());
      progress = -1;
    }
    
    
    if(progress < 0)
    {
      switch(getRedstoneMode())
      {
        case RSMODE_IGNORE:
          BeginAtomizing();
          break;
        case RSMODE_OFF:
          if(!redstone_signal)
          {
            BeginAtomizing();
          }
          break;
        case RSMODE_ON:
          if(redstone_signal)
          {
            BeginAtomizing();
          }
          break;
        case RSMODE_PULSE:
          if(redstone_signal && !last_redstone_signal)
          {
            BeginAtomizing();
          }
          break;
      }
    } else
    {
      if(CanAtomizeCurrentRecipe())
      {
        FluidStack input_fluid = current_recipe.getInput();
        int increment = 1800000 / input_fluid.amount;
        if(increment > ATOMIZE_TIME / 4)
        {
          increment = ATOMIZE_TIME / 4;
        }
        if(increment < 1)
        {
          increment = 1;
        }
        progress += increment;
        
        if(progress >= ATOMIZE_TIME)
        {
          progress = -1;
          tanks[TANK_INPUT].drain(input_fluid.amount, true);
          tanks[TANK_WATER].drain(water_required.amount, true);
          if(inventory[INVENTORY_OUTPUT] == null)
          {
            inventory[INVENTORY_OUTPUT] = current_recipe.getOutputItem();
            inventory[INVENTORY_OUTPUT].stackSize = 1;
          } else
          {
            inventory[INVENTORY_OUTPUT].stackSize++;
          }
          updateInventoryItem(INVENTORY_OUTPUT);
          updateTank(0);
          updateTank(1);
          markDirty();
        }
      } else
      {
        progress = -1;
      }
    }
    
    if(last_progress != progress)
    {
      updateValue("progress",progress);
    }
  }

  @Override
  public FluidTank getTank(int slot)
  {
    if(slot < 0 || slot > 1)
    {
      return null;
    }
    return tanks[slot];
  }

  @Override
  public int getTankCount()
  {
    return 2;
  }

  @Override
  public int getFoundryEnergyCapacity()
  {
    return 60000;
  }
}
