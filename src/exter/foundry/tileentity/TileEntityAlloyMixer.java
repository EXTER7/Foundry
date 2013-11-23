package exter.foundry.tileentity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLLog;
import exter.foundry.ModFoundry;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.container.ContainerAlloyMixer;
import exter.foundry.network.FoundryPacketHandler;
import exter.foundry.recipes.AlloyRecipe;
import exter.foundry.recipes.manager.AlloyRecipeManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityAlloyMixer extends TileEntityFoundry implements ISidedInventory,IFluidHandler,IPowerReceptor
{
  static private final int NETDATAID_TANK_INPUT_A_FLUID = 0;
  static private final int NETDATAID_TANK_INPUT_A_AMOUNT = 1;
  static private final int NETDATAID_TANK_INPUT_B_FLUID = 2;
  static private final int NETDATAID_TANK_INPUT_B_AMOUNT = 3;
  static private final int NETDATAID_TANK_OUTPUT_FLUID = 4;
  static private final int NETDATAID_TANK_OUTPUT_AMOUNT = 5;
  
  static private final int PROGRESS_MAX = 400;

  static public final int INVENTORY_CONTAINER_INPUT_A_DRAIN = 0;
  static public final int INVENTORY_CONTAINER_INPUT_A_FILL = 1;
  static public final int INVENTORY_CONTAINER_INPUT_B_DRAIN = 2;
  static public final int INVENTORY_CONTAINER_INPUT_B_FILL = 3;
  static public final int INVENTORY_CONTAINER_OUTPUT_DRAIN = 4;
  static public final int INVENTORY_CONTAINER_OUTPUT_FILL = 5;
  private ItemStack[] inventory;

  
  static public final int TANK_INPUT_A = 0;
  static public final int TANK_INPUT_B = 1;
  static public final int TANK_OUTPUT = 2;
  private FluidTank[] tanks;
  private FluidTankInfo[] tank_info;

  private PowerHandler power_handler;

  
  private int progress;
  
 
  public TileEntityAlloyMixer()
  {
    super();
    int i;
    inventory = new ItemStack[6];
    
    tanks = new FluidTank[3];
    tank_info = new FluidTankInfo[3];
    for(i = 0; i < 3; i++)
    {
      tanks[i] = new FluidTank(2000);
      tank_info[i] = new FluidTankInfo(tanks[i]);
    }
    progress = 0;

    power_handler = new PowerHandler(this,PowerHandler.Type.MACHINE);
    power_handler.configure(0, 4, 1, 4);
    power_handler.configurePowerPerdition(0, 0);

    AddContainerSlot(new ContainerSlot(TANK_INPUT_A,INVENTORY_CONTAINER_INPUT_A_DRAIN,false));
    AddContainerSlot(new ContainerSlot(TANK_INPUT_A,INVENTORY_CONTAINER_INPUT_A_FILL,true));
    AddContainerSlot(new ContainerSlot(TANK_INPUT_B,INVENTORY_CONTAINER_INPUT_B_DRAIN,false));
    AddContainerSlot(new ContainerSlot(TANK_INPUT_B,INVENTORY_CONTAINER_INPUT_B_FILL,true));
    AddContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_DRAIN,false));
    AddContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_FILL,true));
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
      case NETDATAID_TANK_INPUT_A_FLUID:
        SetTankFluid(tanks[TANK_INPUT_A],value);
        break;
      case NETDATAID_TANK_INPUT_A_AMOUNT:
        SetTankAmount(tanks[TANK_INPUT_A],value);
        break;
      case NETDATAID_TANK_INPUT_B_FLUID:
        SetTankFluid(tanks[TANK_INPUT_B],value);
        break;
      case NETDATAID_TANK_INPUT_B_AMOUNT:
        SetTankAmount(tanks[TANK_INPUT_B],value);
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
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_A_FLUID, GetTankFluid(tanks[TANK_INPUT_A]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_A_AMOUNT, GetTankAmount(tanks[TANK_INPUT_A]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_B_FLUID, GetTankFluid(tanks[TANK_INPUT_B]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_B_AMOUNT, GetTankAmount(tanks[TANK_INPUT_B]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_OUTPUT_FLUID, GetTankFluid(tanks[TANK_OUTPUT]));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_OUTPUT_AMOUNT, GetTankAmount(tanks[TANK_OUTPUT]));
  }

  @Override
  public int getSizeInventory()
  {
    return 6;
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
        onInventoryChanged();
        return is;
      } else
      {
        is = inventory[slot].splitStack(amount);

        if(inventory[slot].stackSize == 0)
        {
          inventory[slot] = null;
        }

        onInventoryChanged();
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
    
    onInventoryChanged();
  }

  
  @Override
  public String getInvName()
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
    return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
  }


  @Override
  public void openChest()
  {

  }  

  @Override
  public void closeChest()
  {

  }

  private boolean MixAlloy(AlloyRecipe recipe,int tank_a,int tank_b)
  {
    int in_a = recipe.GetInputA().amount;
    int in_b = recipe.GetInputB().amount;
    FluidStack out = recipe.GetOutput();


    FluidStack drain_a = tanks[tank_a].drain(in_a, false);
    FluidStack drain_b = tanks[tank_b].drain(in_b, false);
    if(power_handler.getEnergyStored() > 0 && drain_a != null && drain_a.amount == in_a && drain_b != null && drain_b.amount == in_b && tanks[TANK_OUTPUT].fill(out, false) == out.amount)
    {
      int energy = (int) (power_handler.useEnergy(0, 4, true) * 100);
      progress += energy;

      if(progress >= PROGRESS_MAX)
      {
        progress -= PROGRESS_MAX;

        tanks[tank_a].drain(in_a, true);
        tanks[tank_b].drain(in_b, true);
        tanks[TANK_OUTPUT].fill(out, true);
        UpdateTank(tank_a);
        UpdateTank(tank_b);
        UpdateTank(TANK_OUTPUT);
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isInvNameLocalized()
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
  
  private boolean TankContainsFluid(FluidTank tank,FluidStack fluid)
  {
    FluidStack tf = tank.getFluid();
    return (tf != null && tf.amount > 0 && tf.isFluidEqual(fluid));
  }

  @Override
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
  {
    if(tanks[TANK_INPUT_A].fill(resource, false) > 0 && !TankContainsFluid(tanks[TANK_INPUT_B],resource))
    {
      return tanks[TANK_INPUT_A].fill(resource, doFill);
    } else if(tanks[TANK_INPUT_B].fill(resource, false) > 0 && !TankContainsFluid(tanks[TANK_INPUT_A],resource))
    {
      return tanks[TANK_INPUT_B].fill(resource, doFill);
    } else
    {
      return 0;
    }
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

  @Override
  protected void UpdateEntityServer()
  {
    boolean update_clients = false;
    NBTTagCompound packet = new NBTTagCompound();
    super.writeToNBT(packet);

    int last_progress = progress;

    if(tanks[TANK_INPUT_A].getFluidAmount() * tanks[TANK_INPUT_B].getFluidAmount() > 0)
    {
      AlloyRecipe r = AlloyRecipeManager.instance.FindRecipe(tanks[TANK_INPUT_A].getFluid(), tanks[TANK_INPUT_B].getFluid());
      if(r != null)
      {
        if(MixAlloy(r, TANK_INPUT_A, TANK_INPUT_B))
        {
          update_clients = true;
        }
      } else
      {
        r = AlloyRecipeManager.instance.FindRecipe(tanks[TANK_INPUT_B].getFluid(), tanks[TANK_INPUT_A].getFluid());
        if(r != null)
        {
          if(MixAlloy( r, TANK_INPUT_B, TANK_INPUT_A))
          {
            update_clients = true;
          }
        } else
        {
          progress = 0;
        }
      }
    } else
    {
      progress = 0;
    }

    if(progress != last_progress)
    {      
      UpdateValue("progress",progress);
    }
  }

  @Override
  public PowerReceiver getPowerReceiver(ForgeDirection side)
  {
    return power_handler.getPowerReceiver();
  }

  @Override
  public void doWork(PowerHandler workProvider)
  {
  }

  @Override
  public World getWorld()
  {
    return worldObj;
  }

  @Override
  public FluidTank GetTank(int slot)
  {
    return tanks[slot];
  }

  @Override
  public int GetTankCount()
  {
    return 3;
  }
}
