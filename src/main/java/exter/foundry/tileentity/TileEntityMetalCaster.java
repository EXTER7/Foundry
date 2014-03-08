package exter.foundry.tileentity;


import io.netty.buffer.ByteBufInputStream;

import java.io.IOException;

import exter.foundry.ModFoundry;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.container.ContainerMetalCaster;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityMetalCaster extends TileEntityFoundryPowered implements ISidedInventory,IFluidHandler
{
  public enum RedstoneMode
  {
    RSMODE_IGNORE(0),
    RSMODE_ON(1),
    RSMODE_OFF(2),
    RSMODE_PULSE(3);
    
    public final int number;
    
    private RedstoneMode(int num)
    {
      number = num;
    }
    
    public RedstoneMode Next()
    {
      return FromNumber((number + 1) % 4);
    }
    
    static public RedstoneMode FromNumber(int num)
    {
      for(RedstoneMode m:RedstoneMode.values())
      {
        if(m.number == num)
        {
          return m;
        }
      }
      return RSMODE_IGNORE;
    }
  }
  
  static private final int NETDATAID_TANK_FLUID = 1;
  static private final int NETDATAID_TANK_AMOUNT = 2;

  static public final int CAST_TIME = 4000;
  
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
  
  
  private RedstoneMode mode;
  private int progress;

  public TileEntityMetalCaster()
  {
    super();

    tank = new FluidTank(FoundryUtils.CASTER_TANK_CAPACITY);
    
    tank_info = new FluidTankInfo[1];
    tank_info[0] = new FluidTankInfo(tank);
    progress = -1;
    inventory = new ItemStack[14];
    
    mode = RedstoneMode.RSMODE_IGNORE;
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
    if(compund.hasKey("mode"))
    {
      mode = RedstoneMode.FromNumber(compund.getInteger("mode"));
    }
  }


  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    compound.setInteger("progress", progress);
    compound.setInteger("mode", mode.number);
  }

  public void GetGUINetworkData(int id, int value)
  {
    switch(id)
    {
      case NETDATAID_TANK_FLUID:
        if(tank.getFluid() == null)
        {
          tank.setFluid(new FluidStack(value, 0));
        } else
        {
          tank.getFluid().fluidID = value;
        }
        break;
      case NETDATAID_TANK_AMOUNT:
        if(tank.getFluid() == null)
        {
          tank.setFluid(new FluidStack(0, value));
        } else
        {
          tank.getFluid().amount = value;
        }
        break;
    }
  }

  public void SendGUINetworkData(ContainerMetalCaster container, ICrafting crafting)
  {
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_FLUID, tank.getFluid() != null ? tank.getFluid().fluidID : 0);
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_AMOUNT, tank.getFluid() != null ? tank.getFluid().amount : 0);
  }

  @Override
  public void ReceivePacketData(ByteBufInputStream data) throws IOException
  {
    SetMode(RedstoneMode.FromNumber(data.readByte()));
  }
  
  public RedstoneMode GetMode()
  {
    return mode;
  }

  public void SetMode(RedstoneMode new_mode)
  {
    if(mode != new_mode)
    {
      mode = new_mode;
      if(worldObj.isRemote)
      {
        ModFoundry.network_channel.SendCasterModeToServer(this);
      }
    }
  }

  public int GetStoredPower()
  {
    return energy_manager.GetStoredEnergy();
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
  public String getInventoryName()
  {
    return "Caster";
  }

  @Override
  public int getInventoryStackLimit()
  {
    return 64;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer player)
  {
    return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
  }


  @Override
  public void openInventory()
  {
    if(!worldObj.isRemote)
    {
      ModFoundry.network_channel.SendCasterModeToClients(this);
    }
  }

  @Override
  public void closeInventory()
  {
    if(!worldObj.isRemote)
    {
      ModFoundry.network_channel.SendCasterModeToClients(this);
    }
  }

  public int GetProgress()
  {
    return progress;
  }
  
  @Override
  public boolean hasCustomInventoryName()
  {
    return false;
  }

  static private final int[] INSERT_SLOTS = { INVENTORY_EXTRA };
  static private final int[] EXTRACT_SLOTS = { INVENTORY_OUTPUT };

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemstack)
  {
    return slot == INVENTORY_EXTRA;
  }

  @Override
  public int[] getAccessibleSlotsFromSide(int side)
  {
    return side == 1?INSERT_SLOTS:EXTRACT_SLOTS;
  }

  @Override
  public boolean canInsertItem(int slot, ItemStack itemstack, int side)
  {
    return isItemValidForSlot(slot, itemstack);
  }

  @Override
  public boolean canExtractItem(int slot, ItemStack itemstack, int side)
  {
    return slot == INVENTORY_OUTPUT;
  }

  @Override
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
  {
    return tank.fill(resource, doFill);
  }

  @Override
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    if(resource.isFluidEqual(tank.getFluid()))
    {
      return tank.drain(resource.amount, doDrain);
    }
    return null;
  }

  @Override
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    return tank.drain(maxDrain, doDrain);
  }

  @Override
  public boolean canFill(ForgeDirection from, Fluid fluid)
  {
    return true;
  }

  @Override
  public boolean canDrain(ForgeDirection from, Fluid fluid)
  {
    return true;
  }

  @Override
  public FluidTankInfo[] getTankInfo(ForgeDirection from)
  {
    return tank_info;
  }

  @Override
  protected void UpdateEntityClient()
  {
    
  }
  
  private void CheckCurrentRecipe()
  {
    if(current_recipe == null)
    {
      progress = -1;
      return;
    }

    if(!current_recipe.MatchesRecipe(inventory[INVENTORY_MOLD], tank.getFluid()))
    {
      progress = -1;
      current_recipe = null;
      return;
    }
  }
  
  private void BeginCasting()
  {
    if(current_recipe != null && CanCastCurrentRecipe() && energy_manager.GetStoredEnergy() >= ENERGY_REQUIRED)
    {
      energy_manager.UseEnergy(ENERGY_REQUIRED, true);
      progress = 0;
    }
  }
  
  private boolean CanCastCurrentRecipe()
  {
    if(current_recipe.RequiresExtra())
    {
      if(!current_recipe.ContainsExtra(inventory[INVENTORY_EXTRA]))
      {
        return false;
      }
    }
    
    
    ItemStack recipe_output = current_recipe.GetOutputItem();

    ItemStack inv_output = inventory[INVENTORY_OUTPUT];
    if(inv_output != null && (!inv_output.isItemEqual(recipe_output) || inv_output.stackSize >= inv_output.getMaxStackSize()))
    {
      return false;
    }
    return true;
  }

  @Override
  protected void UpdateEntityServer()
  {
    super.UpdateEntityServer();

    int last_progress = progress;
    
    CheckCurrentRecipe();
    
    if(current_recipe == null)
    {
      current_recipe = CastingRecipeManager.instance.FindRecipe(tank.getFluid(), inventory[INVENTORY_MOLD]);
      progress = -1;
    }
    
    
    if(progress < 0)
    {
      switch(mode)
      {
        case RSMODE_IGNORE:
          BeginCasting();
          break;
        case RSMODE_OFF:
          if(!redstone_signal)
          {
            BeginCasting();
          }
          break;
        case RSMODE_ON:
          if(redstone_signal)
          {
            BeginCasting();
          }
          break;
        case RSMODE_PULSE:
          if(redstone_signal && !last_redstone_signal)
          {
            BeginCasting();
          }
          break;
      }
    } else
    {
      if(CanCastCurrentRecipe())
      {
        FluidStack input_fluid = current_recipe.GetInputFluid();
        int increment = 18000 / input_fluid.amount;
        if(increment > CAST_TIME / 15)
        {
          increment = CAST_TIME / 15;
        }
        if(increment < 30)
        {
          increment = 30;
        }
        progress += increment;
        
        if(progress >= CAST_TIME)
        {
          progress = -1;
          tank.drain(input_fluid.amount, true);
          if(current_recipe.RequiresExtra())
          {
            decrStackSize(INVENTORY_EXTRA, FoundryMiscUtils.GetStackSize(current_recipe.GetInputExtra()));
            UpdateInventoryItem(INVENTORY_EXTRA);
          }
          if(inventory[INVENTORY_OUTPUT] == null)
          {
            inventory[INVENTORY_OUTPUT] = current_recipe.GetOutputItem();
            inventory[INVENTORY_OUTPUT].stackSize = 1;
          } else
          {
            inventory[INVENTORY_OUTPUT].stackSize++;
          }
          UpdateInventoryItem(INVENTORY_OUTPUT);
          UpdateTank(0);
          markDirty();
        }
      } else
      {
        progress = -1;
      }
    }
    
    if(last_progress != progress)
    {
      UpdateValue("progress",progress);
    }
  }

  @Override
  public FluidTank GetTank(int slot)
  {
    if(slot != 0)
    {
      return null;
    }
    return tank;
  }

  @Override
  public int GetTankCount()
  {
    return 1;
  }

  @Override
  public int GetMaxStoredEnergy()
  {
    return 40000;
  }


  @Override
  public int GetEnergyUse()
  {
    return ENERGY_REQUIRED;
  }
}
