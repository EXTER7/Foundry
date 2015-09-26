package exter.foundry.tileentity;


import io.netty.buffer.ByteBuf;

import exter.foundry.ModFoundry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.container.ContainerMetalAtomizer;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityMetalAtomizer extends TileEntityFoundryPowered implements ISidedInventory,IFluidHandler
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
  
  
  private RedstoneMode mode;
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
    
    mode = RedstoneMode.RSMODE_IGNORE;
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
        SetTankFluid(tanks[TANK_INPUT],value);
        break;
      case NETDATAID_TANK_AMOUNT:
        SetTankAmount(tanks[TANK_INPUT],value);
        break;
      case NETDATAID_WATER_TANK_FLUID:
        SetTankFluid(tanks[TANK_WATER],value);
        break;
      case NETDATAID_WATER_TANK_AMOUNT:
        SetTankAmount(tanks[TANK_WATER],value);
        break;
    }
  }

  public void SendGUINetworkData(ContainerMetalAtomizer container, ICrafting crafting)
  {
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_FLUID, GetTankFluid(tanks[TANK_INPUT]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_AMOUNT, GetTankAmount(tanks[TANK_INPUT]));
    crafting.sendProgressBarUpdate(container, NETDATAID_WATER_TANK_FLUID, GetTankFluid(tanks[TANK_WATER]));
    crafting.sendProgressBarUpdate(container, NETDATAID_WATER_TANK_AMOUNT, GetTankAmount(tanks[TANK_WATER]));
  }

  @Override
  public void ReceivePacketData(ByteBuf data)
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
        ModFoundry.network_channel.SendAtomizerModeToServer(this);
      }
    }
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
  public String getInventoryName()
  {
    return "Atomizer";
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
      ModFoundry.network_channel.SendAtomizerModeToClients(this);
    }
  }

  @Override
  public void closeInventory()
  {
    if(!worldObj.isRemote)
    {
      ModFoundry.network_channel.SendAtomizerModeToClients(this);
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

  static private final int[] EXTRACT_SLOTS = { INVENTORY_OUTPUT };

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemstack)
  {
    return false;
  }

  @Override
  public int[] getAccessibleSlotsFromSide(int side)
  {
    return EXTRACT_SLOTS;
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
    if(resource != null && resource.getFluid() == FluidRegistry.WATER)
    {
      return tanks[TANK_WATER].fill(resource, doFill);
    }
    return tanks[TANK_INPUT].fill(resource, doFill);
  }

  @Override
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    if(resource.isFluidEqual(tanks[TANK_INPUT].getFluid()))
    {
      return tanks[TANK_INPUT].drain(resource.amount, doDrain);
    }
    return null;
  }

  @Override
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    return tanks[TANK_INPUT].drain(maxDrain, doDrain);
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
    
    if(!current_recipe.MatchesRecipe(tanks[TANK_INPUT].getFluid()))
    {
      progress = -1;
      current_recipe = null;
      return;
    }
  }
  
  private void BeginAtomizing()
  {
    if(current_recipe != null && CanAtomizeCurrentRecipe() && GetStoredEnergy() >= ENERGY_REQUIRED)
    {
      UseEnergy(ENERGY_REQUIRED, true);
      progress = 0;
    }
  }
  
  private boolean CanAtomizeCurrentRecipe()
  {
    if(tanks[TANK_WATER].getFluid() == null || !tanks[TANK_WATER].getFluid().containsFluid(water_required))
    {
      return false;
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
      current_recipe = AtomizerRecipeManager.instance.FindRecipe(tanks[TANK_INPUT].getFluid());
      progress = -1;
    }
    
    
    if(progress < 0)
    {
      switch(mode)
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
        FluidStack input_fluid = current_recipe.GetInputFluid();
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
            inventory[INVENTORY_OUTPUT] = current_recipe.GetOutputItem();
            inventory[INVENTORY_OUTPUT].stackSize = 1;
          } else
          {
            inventory[INVENTORY_OUTPUT].stackSize++;
          }
          UpdateInventoryItem(INVENTORY_OUTPUT);
          UpdateTank(0);
          UpdateTank(1);
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
    if(slot < 0 || slot > 1)
    {
      return null;
    }
    return tanks[slot];
  }

  @Override
  public int GetTankCount()
  {
    return 2;
  }

  @Override
  public int GetEnergyCapacity()
  {
    return 60000;
  }
}
