package exter.foundry.tileentity;


import io.netty.buffer.ByteBufInputStream;

import java.io.IOException;

import exter.foundry.ModFoundry;
import exter.foundry.container.ContainerAlloyMixer;
import exter.foundry.recipes.AlloyRecipe;
import exter.foundry.recipes.manager.AlloyRecipeManager;
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

public class TileEntityAlloyMixer extends TileEntityFoundryPowered implements ISidedInventory,IFluidHandler
{
  public enum RedstoneMode
  {
    RSMODE_IGNORE(0),
    RSMODE_ON(1),
    RSMODE_OFF(2);
    
    public final int number;
    
    private RedstoneMode(int num)
    {
      number = num;
    }
    
    public RedstoneMode Next()
    {
      return FromNumber((number + 1) % 3);
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

  static private final int REQUIRED_ENERGY = 400;
  
  static private final int NETDATAID_TANK_INPUT_0_FLUID = 0;
  static private final int NETDATAID_TANK_INPUT_0_AMOUNT = 1;
  static private final int NETDATAID_TANK_INPUT_1_FLUID = 2;
  static private final int NETDATAID_TANK_INPUT_1_AMOUNT = 3;
  static private final int NETDATAID_TANK_INPUT_2_FLUID = 4;
  static private final int NETDATAID_TANK_INPUT_2_AMOUNT = 5;
  static private final int NETDATAID_TANK_INPUT_3_FLUID = 6;
  static private final int NETDATAID_TANK_INPUT_3_AMOUNT = 7;
  static private final int NETDATAID_TANK_OUTPUT_FLUID = 8;
  static private final int NETDATAID_TANK_OUTPUT_AMOUNT = 9;

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
  private ItemStack[] inventory;

  
  static public final int TANK_INPUT_0 = 0;
  static public final int TANK_INPUT_1 = 1;
  static public final int TANK_INPUT_2 = 2;
  static public final int TANK_INPUT_3 = 3;
  static public final int TANK_OUTPUT = 4;
  private FluidTank[] tanks;
  private FluidTankInfo[] tank_info;

  private RedstoneMode mode;

  
  
 
  public TileEntityAlloyMixer()
  {
    super();
    int i;
    inventory = new ItemStack[10];
    
    tanks = new FluidTank[5];
    tank_info = new FluidTankInfo[5];
    for(i = 0; i < 5; i++)
    {
      tanks[i] = new FluidTank(2000);
      tank_info[i] = new FluidTankInfo(tanks[i]);
    }
    mode = RedstoneMode.RSMODE_IGNORE;

    AddContainerSlot(new ContainerSlot(TANK_INPUT_0,INVENTORY_CONTAINER_INPUT_0_DRAIN,false));
    AddContainerSlot(new ContainerSlot(TANK_INPUT_0,INVENTORY_CONTAINER_INPUT_0_FILL,true));
    AddContainerSlot(new ContainerSlot(TANK_INPUT_1,INVENTORY_CONTAINER_INPUT_1_DRAIN,false));
    AddContainerSlot(new ContainerSlot(TANK_INPUT_1,INVENTORY_CONTAINER_INPUT_1_FILL,true));
    AddContainerSlot(new ContainerSlot(TANK_INPUT_2,INVENTORY_CONTAINER_INPUT_2_DRAIN,false));
    AddContainerSlot(new ContainerSlot(TANK_INPUT_2,INVENTORY_CONTAINER_INPUT_2_FILL,true));
    AddContainerSlot(new ContainerSlot(TANK_INPUT_3,INVENTORY_CONTAINER_INPUT_3_DRAIN,false));
    AddContainerSlot(new ContainerSlot(TANK_INPUT_3,INVENTORY_CONTAINER_INPUT_3_FILL,true));
    AddContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_DRAIN,false));
    AddContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_FILL,true));
  }

  @Override
  public void readFromNBT(NBTTagCompound compund)
  {
    super.readFromNBT(compund);
    
    if(compund.hasKey("mode"))
    {
      mode = RedstoneMode.FromNumber(compund.getInteger("mode"));
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    compound.setInteger("mode", mode.number);
  }
  
  private void SetTankFluid(FluidTank tank,int value)
  {
    if(tank.getFluid() == null)
    {
      tank.setFluid(new FluidStack(value, 0));
    } else
    {
      tank.getFluid().fluidID = value;
    }
  }

  private void SetTankAmount(FluidTank tank,int value)
  {
    if(tank.getFluid() == null)
    {
      tank.setFluid(new FluidStack(0, value));
    } else
    {
      tank.getFluid().amount = value;
    }
  }

  
  public void GetGUINetworkData(int id, int value)
  {
    switch(id)
    {
      case NETDATAID_TANK_INPUT_0_FLUID:
        SetTankFluid(tanks[TANK_INPUT_0],value);
        break;
      case NETDATAID_TANK_INPUT_0_AMOUNT:
        SetTankAmount(tanks[TANK_INPUT_0],value);
        break;
      case NETDATAID_TANK_INPUT_1_FLUID:
        SetTankFluid(tanks[TANK_INPUT_1],value);
        break;
      case NETDATAID_TANK_INPUT_1_AMOUNT:
        SetTankAmount(tanks[TANK_INPUT_1],value);
        break;
      case NETDATAID_TANK_INPUT_2_FLUID:
        SetTankFluid(tanks[TANK_INPUT_2],value);
        break;
      case NETDATAID_TANK_INPUT_2_AMOUNT:
        SetTankAmount(tanks[TANK_INPUT_2],value);
        break;
      case NETDATAID_TANK_INPUT_3_FLUID:
        SetTankFluid(tanks[TANK_INPUT_3],value);
        break;
      case NETDATAID_TANK_INPUT_3_AMOUNT:
        SetTankAmount(tanks[TANK_INPUT_3],value);
        break;
      case NETDATAID_TANK_OUTPUT_FLUID:
        SetTankFluid(tanks[TANK_OUTPUT],value);
        break;
      case NETDATAID_TANK_OUTPUT_AMOUNT:
        SetTankAmount(tanks[TANK_OUTPUT],value);
        break;
    }
  }

  private int GetTankFluid(FluidTank tank)
  {
    return tank.getFluid() != null ? tank.getFluid().fluidID : 0;
  }

  private int GetTankAmount(FluidTank tank)
  {
    return tank.getFluid() != null ? tank.getFluid().amount : 0;
  }

  public void SendGUINetworkData(ContainerAlloyMixer container, ICrafting crafting)
  {
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_0_FLUID, GetTankFluid(tanks[TANK_INPUT_0]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_0_AMOUNT, GetTankAmount(tanks[TANK_INPUT_0]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_1_FLUID, GetTankFluid(tanks[TANK_INPUT_1]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_1_AMOUNT, GetTankAmount(tanks[TANK_INPUT_1]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_2_FLUID, GetTankFluid(tanks[TANK_INPUT_2]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_2_AMOUNT, GetTankAmount(tanks[TANK_INPUT_2]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_3_FLUID, GetTankFluid(tanks[TANK_INPUT_3]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_3_AMOUNT, GetTankAmount(tanks[TANK_INPUT_3]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_OUTPUT_FLUID, GetTankFluid(tanks[TANK_OUTPUT]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_OUTPUT_AMOUNT, GetTankAmount(tanks[TANK_OUTPUT]));
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
        ModFoundry.network_channel.SendAlloyMixerModeToServer(this);
      }
    }
  }

  @Override
  public int getSizeInventory()
  {
    return 10;
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
    return "Alloy Mixer";
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
      ModFoundry.network_channel.SendAlloyMixerModeToClients(this);
    }
  }

  @Override
  public void closeInventory()
  {
    if(!worldObj.isRemote)
    {
      ModFoundry.network_channel.SendAlloyMixerModeToClients(this);
    }
  }

  @Override
  public boolean hasCustomInventoryName()
  {
    return false;
  }

  static private final int[] SLOTS = { };

  @Override
  public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
    return false;
  }

  @Override
  public int[] getAccessibleSlotsFromSide(int side)
  {
    return SLOTS;
  }

  @Override
  public boolean canInsertItem(int i, ItemStack itemstack, int j)
  {
    return isItemValidForSlot(i, itemstack);
  }

  @Override
  public boolean canExtractItem(int i, ItemStack itemstack, int j)
  {
    return false;
  }
  
  @Override
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
  {
    int i;
    for(i = 0; i < 4; i++)
    {
      if(tanks[i].fill(resource, false) > 0)
      {
        return tanks[i].fill(resource, doFill);
      }
    }
    return 0;
  }

  @Override
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    if(resource.isFluidEqual(tanks[TANK_OUTPUT].getFluid()))
    {
      return tanks[TANK_OUTPUT].drain(resource.amount, doDrain);
    }
    return null;
  }

  @Override
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    return tanks[TANK_OUTPUT].drain(maxDrain, doDrain);
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
  
  private int[] recipe_order = new int[4];
  private FluidStack[] input_tank_fluids = new FluidStack[4];

  private void MixAlloy()
  {
    if(energy_manager.GetStoredEnergy() < REQUIRED_ENERGY)
    {
      return;
    }

    boolean do_mix = false;
    switch(mode)
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

    AlloyRecipe recipe = AlloyRecipeManager.instance.FindRecipe(input_tank_fluids, recipe_order);
    if(recipe == null)
    {
      return;
    }
    FluidStack output = recipe.output;

    if(tanks[TANK_OUTPUT].fill(output, false) < output.amount)
    {
      return;
    }

    energy_manager.UseEnergy(REQUIRED_ENERGY, true);
    tanks[TANK_OUTPUT].fill(output, true);
    UpdateTank(TANK_OUTPUT);
    for(i = 0; i < recipe.GetInputCount(); i++)
    {
      tanks[recipe_order[i]].drain(recipe.inputs[i].amount, true);
      UpdateTank(recipe_order[i]);
    }
  }

  @Override
  protected void UpdateEntityServer()
  {
    super.UpdateEntityServer();

    if(tanks[TANK_OUTPUT].getFluidAmount() < tanks[TANK_OUTPUT].getCapacity()
        && (tanks[TANK_INPUT_0].getFluidAmount() > 0
        ||  tanks[TANK_INPUT_1].getFluidAmount() > 0
        ||  tanks[TANK_INPUT_2].getFluidAmount() > 0
        ||  tanks[TANK_INPUT_3].getFluidAmount() > 0))
    {
      MixAlloy();
    }
  }

  @Override
  public FluidTank GetTank(int slot)
  {
    return tanks[slot];
  }

  @Override
  public int GetTankCount()
  {
    return 5;
  }

  @Override
  public int GetMaxStoredEnergy()
  {
    return 1500;
  }

  @Override
  public int GetEnergyUse()
  {
    return REQUIRED_ENERGY;
  }
}
