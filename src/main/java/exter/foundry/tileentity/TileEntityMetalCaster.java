package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.container.ContainerMetalCaster;
import exter.foundry.recipes.manager.CastingRecipeManager;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityMetalCaster extends TileEntityFoundryPowered implements ISidedInventory,IFluidHandler
{
  static private final int NETDATAID_TANK_FLUID = 1;
  static private final int NETDATAID_TANK_AMOUNT = 2;

  static public final int CAST_TIME = 400000;
  
  static public final int ENERGY_REQUIRED = 10000;
  
  static public final int INVENTORY_OUTPUT = 0;
  static public final int INVENTORY_MOLD = 1;
  static public final int INVENTORY_EXTRA = 2;
  static public final int INVENTORY_CONTAINER_DRAIN = 3;
  static public final int INVENTORY_CONTAINER_FILL = 4;
  static public final int INVENTORY_MOLD_STORAGE = 5;
  static public final int INVENTORY_MOLD_STORAGE_SIZE = 9;
  private ItemStack[] inventory;
  private FluidTank tank;
  private FluidTankInfo[] tank_info;
  ICastingRecipe current_recipe;
  
  
  private int progress;

  public TileEntityMetalCaster()
  {
    super();

    tank = new FluidTank(FoundryAPI.CASTER_TANK_CAPACITY);
    
    tank_info = new FluidTankInfo[1];
    tank_info[0] = new FluidTankInfo(tank);
    progress = -1;
    inventory = new ItemStack[14];
    
    current_recipe = null;
    
    AddContainerSlot(new ContainerSlot(0,INVENTORY_CONTAINER_DRAIN,false));
    AddContainerSlot(new ContainerSlot(0,INVENTORY_CONTAINER_FILL,true));
   
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

  public void getGUINetworkData(int id, int value)
  {
    switch(id)
    {
      case NETDATAID_TANK_FLUID:
        setTankFluid(tank,value);
        break;
      case NETDATAID_TANK_AMOUNT:
        setTankAmount(tank,value);
        break;
    }
  }

  public void sendGUINetworkData(ContainerMetalCaster container, ICrafting crafting)
  {
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_FLUID, getTankFluid(tank));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_AMOUNT, getTankAmount(tank));
  }

  @Override
  public int getSizeInventory()
  {
    return 14;
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

  static private final int[] INSERT_SLOTS = { INVENTORY_EXTRA };
  static private final int[] EXTRACT_SLOTS = { INVENTORY_OUTPUT };

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemstack)
  {
    return slot == INVENTORY_EXTRA;
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side)
  {
    return side == EnumFacing.UP?INSERT_SLOTS:EXTRACT_SLOTS;
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
    return tank.fill(resource, doFill);
  }

  @Override
  public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
  {
    if(resource.isFluidEqual(tank.getFluid()))
    {
      return tank.drain(resource.amount, doDrain);
    }
    return null;
  }

  @Override
  public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
  {
    return tank.drain(maxDrain, doDrain);
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
  
  private void checkCurrentRecipe()
  {
    if(current_recipe == null)
    {
      progress = -1;
      return;
    }

    if(!current_recipe.matchesRecipe(inventory[INVENTORY_MOLD], tank.getFluid(),inventory[INVENTORY_EXTRA]))
    {
      progress = -1;
      current_recipe = null;
      return;
    }
  }
  
  private void beginCasting()
  {
    if(current_recipe != null && canCastCurrentRecipe() && getStoredFoundryEnergy() >= ENERGY_REQUIRED)
    {
      useFoundryEnergy(ENERGY_REQUIRED, true);
      progress = 0;
    }
  }
  
  private boolean canCastCurrentRecipe()
  {
    if(current_recipe.requiresExtra())
    {
      if(!current_recipe.containsExtra(inventory[INVENTORY_EXTRA]))
      {
        return false;
      }
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
    
    checkCurrentRecipe();
    
    if(current_recipe == null)
    {
      current_recipe = CastingRecipeManager.instance.findRecipe(tank.getFluid(), inventory[INVENTORY_MOLD],inventory[INVENTORY_EXTRA]);
      progress = -1;
    }
    
    
    if(progress < 0)
    {
      switch(getRedstoneMode())
      {
        case RSMODE_IGNORE:
          beginCasting();
          break;
        case RSMODE_OFF:
          if(!redstone_signal)
          {
            beginCasting();
          }
          break;
        case RSMODE_ON:
          if(redstone_signal)
          {
            beginCasting();
          }
          break;
        case RSMODE_PULSE:
          if(redstone_signal && !last_redstone_signal)
          {
            beginCasting();
          }
          break;
      }
    } else
    {
      if(canCastCurrentRecipe())
      {
        FluidStack input_fluid = current_recipe.getInput();
        int increment = 18000 * current_recipe.getCastingSpeed() / input_fluid.amount;
        if(increment > CAST_TIME / 4)
        {
          increment = CAST_TIME / 4;
        }
        if(increment < 1)
        {
          increment = 1;
        }
        progress += increment;
        
        if(progress >= CAST_TIME)
        {
          progress = -1;
          tank.drain(input_fluid.amount, true);
          if(current_recipe.requiresExtra())
          {
            decrStackSize(INVENTORY_EXTRA, FoundryUtils.getStackSize(current_recipe.getInputExtra()));
            updateInventoryItem(INVENTORY_EXTRA);
          }
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
    if(slot != 0)
    {
      return null;
    }
    return tank;
  }

  @Override
  public int getTankCount()
  {
    return 1;
  }

  @Override
  public int getFoundryEnergyCapacity()
  {
    return 40000;
  }
}
