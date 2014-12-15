package exter.foundry.tileentity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import exter.foundry.block.BlockRefractoryHopper;
import exter.foundry.container.ContainerRefractoryHopper;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityRefractoryHopper extends TileEntityFoundry implements ISidedInventory, IFluidHandler
{

  static private final int NETDATAID_TANK_FLUID = 0;
  static private final int NETDATAID_TANK_AMOUNT = 1;

  static public final int INVENTORY_CONTAINER_DRAIN = 0;
  static public final int INVENTORY_CONTAINER_FILL = 1;
  private ItemStack[] inventory;
  private FluidTank tank;
  private FluidTankInfo[] tank_info;

  private int next_drain;
  private int next_world_drain;
  private int next_fill;

  // Used by world draining system.
  private boolean[] visited;

  public TileEntityRefractoryHopper()
  {
    visited = new boolean[41 * 20 * 41];
    inventory = new ItemStack[2];

    next_drain = 12;
    next_world_drain = 300;
    next_fill = 3;

    tank = new FluidTank(2000);
    tank_info = new FluidTankInfo[1];
    tank_info[0] = new FluidTankInfo(tank);
    AddContainerSlot(new ContainerSlot(0, INVENTORY_CONTAINER_DRAIN, false));
    AddContainerSlot(new ContainerSlot(0, INVENTORY_CONTAINER_FILL, true));
  }

  @Override
  public void readFromNBT(NBTTagCompound compund)
  {
    super.readFromNBT(compund);

    if(compund.hasKey("next_drain"))
    {
      next_drain = compund.getInteger("next_drain");
    }
    if(compund.hasKey("next_world_drain"))
    {
      next_world_drain = compund.getInteger("next_world_drain");
    }
    if(compund.hasKey("next_fill"))
    {
      next_fill = compund.getInteger("next_fill");
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    compound.setInteger("next_drain", next_drain);
    compound.setInteger("next_world_drain", next_world_drain);
    compound.setInteger("next_fill", next_fill);
  }

  private void SetTankFluid(int value)
  {
    if(tank.getFluid() == null)
    {
      tank.setFluid(new FluidStack(value, 0));
    } else
    {
      tank.getFluid().fluidID = value;
    }
  }

  private void SetTankAmount(int value)
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
      case NETDATAID_TANK_FLUID:
        SetTankFluid(value);
        break;
      case NETDATAID_TANK_AMOUNT:
        SetTankAmount(value);
        break;
    }
  }

  private int GetTankFluid()
  {
    return tank.getFluid() != null ? tank.getFluid().fluidID : 0;
  }

  private int GetTankAmount()
  {
    return tank.getFluid() != null ? tank.getFluid().amount : 0;
  }

  public void SendGUINetworkData(ContainerRefractoryHopper container, ICrafting crafting)
  {
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_FLUID, GetTankFluid());
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_AMOUNT, GetTankAmount());
  }

  @Override
  public int getSizeInventory()
  {
    return 2;
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
    return "Refractory Hopper";
  }

  @Override
  public int getInventoryStackLimit()
  {
    return 64;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer player)
  {
    return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64.0D;
  }

  @Override
  public void openInventory()
  {

  }

  @Override
  public void closeInventory()
  {

  }

  @Override
  public boolean hasCustomInventoryName()
  {
    return false;
  }

  static private final int[] SLOTS = {};

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
    if(from != ForgeDirection.UP)
    {
      return 0;
    }
    if(doFill && resource != null && doFill)
    {
      next_drain = 12;
    }
    return tank.fill(resource, doFill);
  }

  @Override
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    return null;
  }

  @Override
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    return null;
  }

  @Override
  public boolean canFill(ForgeDirection from, Fluid fluid)
  {
    return from == ForgeDirection.UP;
  }

  @Override
  public boolean canDrain(ForgeDirection from, Fluid fluid)
  {
    return false;
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

    // Drain from the the world.
    if(--next_world_drain == 0)
    {
      next_world_drain = 300;


      FluidStack todrain = FoundryMiscUtils.DrainFluidFromWorld(worldObj, xCoord, yCoord + 1, zCoord, false);

      if(todrain != null && tank.fill(todrain, false) == todrain.amount)
      {
        Fluid drainfluid = todrain.getFluid();
        if(!drainfluid.isGaseous(todrain) && drainfluid.getDensity(todrain) >= 0)
        {
          int i;
          for(i = 0; i < 41 * 20 * 41; i++)
          {
            visited[i] = false;
          }

          List<Integer> queue = new ArrayList<Integer>();
          Set<Integer> newqueue = new HashSet<Integer>();
          i = 20 + (20 * 20) * 41; // x = 20, y = 0, z = 20
          visited[i] = true;
          int top_y = 0;

          queue.add(i - 1); // x - 1
          queue.add(i + 1); // x + 1
          queue.add(i - 20 * 41); // z - 1
          queue.add(i + 20 * 41); // z + 1
          queue.add(i + 41); // y + 1
          int drainblock = i;
          do
          {
            newqueue.clear();
            for(int p : queue)
            {
              int x = p % 41;
              int y = (p / 41) % 20;
              int z = p / (41 * 20);
              
              todrain = FoundryMiscUtils.DrainFluidFromWorld(worldObj, xCoord + x - 20, yCoord + y + 1, zCoord + z - 20, false);
              if(todrain != null && todrain.getFluid() == drainfluid && tank.fill(todrain, false) == todrain.amount)
              {
                if(y > top_y)
                {
                  top_y = y;
                }
                if(y == top_y)
                {
                  drainblock = p;
                }
                if(x > 0 && !visited[ p - 1])
                {
                  newqueue.add(p - 1); // x - 1
                }
                if(x < 40 && !visited[ p + 1])
                {
                  newqueue.add(p + 1); // x + 1
                }
                if(z > 0 && !visited[ p - 20 * 41])
                {
                  newqueue.add(p - 20 * 41); // z - 1
                }
                if(z < 40 && !visited[ p + 20 * 41])
                {
                  newqueue.add(p + 20 * 41); // z + 1
                }
                if(y < 19 && !visited[ p + 41])
                {
                  newqueue.add(p + 41); // y + 1
                }
              }
              visited[p] = true;
            }
            queue.clear();
            queue.addAll(newqueue);
          } while(!queue.isEmpty());

          int x = drainblock % 41;
          int z = drainblock / (41 * 20);
          todrain = FoundryMiscUtils.DrainFluidFromWorld(worldObj, xCoord + x - 20, yCoord + top_y + 1, zCoord + z - 20, true);
          tank.fill(todrain, true);
        }
      }
    }

    if(--next_drain == 0)
    {
      next_drain = 12;

      // Drain from the top TileEntity
      TileEntity source = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
      if(source instanceof IFluidHandler)
      {
        IFluidHandler hsource = (IFluidHandler) source;
        FluidStack drained = hsource.drain(ForgeDirection.DOWN, 40, false);
        if(drained != null && !drained.getFluid().isGaseous(drained) && drained.getFluid().getDensity(drained) > 0)
        {
          drained.amount = tank.fill(drained, false);
          if(drained.amount > 0)
          {
            hsource.drain(ForgeDirection.DOWN, drained, true);
            tank.fill(drained, true);
            UpdateTank(0);
             markDirty();
          }
        }
      }
    }

    if(--next_fill == 0)
    {
      next_fill = 3;

      // Fill to the sides/bottom
      if(tank.getFluid() != null && tank.getFluid().amount > 0)
      {
        ForgeDirection side = ForgeDirection.getOrientation(BlockRefractoryHopper.GetDirection(getBlockMetadata()));
        TileEntity dest = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
        side = side.getOpposite();
        if(dest instanceof IFluidHandler)
        {
          IFluidHandler hdest = (IFluidHandler) dest;
          if(hdest.canFill(side, tank.getFluid().getFluid()))
          {
            FluidStack drained = tank.drain(10, false);
            if(drained != null)
            {
              drained.amount = hdest.fill(side, drained, false);
              if(drained.amount > 0)
              {
                tank.drain(drained.amount, true);
                hdest.fill(side, drained, true);
                UpdateTank(0);
                markDirty();
              }
            }
          }
        }
      }
    }
  }

  @Override
  public FluidTank GetTank(int slot)
  {
    return tank;
  }

  @Override
  public int GetTankCount()
  {
    return 1;
  }

  @Override
  protected void OnInitialize()
  {

  }
}
