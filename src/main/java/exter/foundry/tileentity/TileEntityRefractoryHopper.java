package exter.foundry.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

  private Random random;

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
  private int top_y;
  private int[] distance;
  private boolean[] isfluid;

  public TileEntityRefractoryHopper()
  {
    distance = new int[41 * 20 * 41];
    isfluid = new boolean[41 * 20 * 41];
    random = new Random();
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

  private void Visit(int x, int y, int z,int dist, List<Integer> queue, Fluid fluid)
  {
    int i = x + (y + z * 20) * 41;
    if(x >= 0 && x < 41 && y >= 0 && y < 20 && z >= 0 && z < 41 && distance[i] == 0)
    {
      FluidStack todrain = FoundryMiscUtils.DrainFluidFromWorld(worldObj, xCoord + x - 20, yCoord + y + 1, zCoord + z - 20, false);
      if(todrain != null && todrain.getFluid() == fluid && tank.fill(todrain, false) == todrain.amount)
      {
        queue.add(i);
        if(y > top_y)
        {
          top_y = y;
        }
        isfluid[i] = true;
      }
      distance[i] = dist;
    }
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
        if(!drainfluid.isGaseous(todrain) && drainfluid.getDensity(todrain) > 0)
        {
          // Find all the valid fluid blocks in range
          int x;
          int z;
          for(x = 0; x < 41 * 20 * 41; x++)
          {
            distance[x] = 0;
            isfluid[x] = false;
          }

          List<Integer> queue = new ArrayList<Integer>();
          int i = 20 + (20 * 20) * 41;
          distance[i] = 1;
          isfluid[i] = true;
          top_y = 0;
          queue.add(i);
          int dist = 2;
          do
          {
            List<Integer> newqueue = new ArrayList<Integer>();
            for(int p : queue)
            {
              int px = p % 41;
              int py = (p / 41) % 20;
              int pz = p / (41 * 20);
              Visit(px - 1, py, pz, dist, newqueue, drainfluid);
              Visit(px + 1, py, pz, dist, newqueue, drainfluid);
              Visit(px, py, pz - 1, dist, newqueue, drainfluid);
              Visit(px, py, pz + 1, dist, newqueue, drainfluid);
              Visit(px, py + 1, pz, dist, newqueue, drainfluid);
            }
            dist++;
            queue = newqueue;
          } while(!queue.isEmpty());

          // Find the top-most fluid blocks.
          List<Integer> top = new ArrayList<Integer>();
          dist = 0;
          for(z = 0; z < 41; z++)
          {
            for(x = 0; x < 41; x++)
            {
              // Pick the furthests blocks on the top.
              i = x + (top_y + z * 20) * 41;
              if(isfluid[i])
              {
                int d = distance[i];
                if(d > dist)
                {
                  top.clear();
                  top.add(x | z << 7);
                  dist = d;
                } else if(d == dist)
                {
                  top.add(x | z << 7);
                }
              }
            }
          }
          int s = top.size();
          int p = top.get(s == 1 ? 0 : random.nextInt(s));
          int px = p & 127;
          int pz = (p >>> 7);
          todrain = FoundryMiscUtils.DrainFluidFromWorld(worldObj, xCoord + px - 20, yCoord + top_y + 1, zCoord + pz - 20, true);
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
        FluidStack fs = tank.getFluid();
        if(hsource.canDrain(ForgeDirection.DOWN, fs == null ? null : fs.getFluid()))
        {
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
