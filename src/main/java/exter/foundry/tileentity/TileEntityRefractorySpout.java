package exter.foundry.tileentity;

import exter.foundry.block.BlockFoundrySidedMachine;
import exter.foundry.block.BlockRefractorySpout;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityRefractorySpout extends TileEntityFoundry implements IFluidHandler
{
  private FluidTank tank;
  private FluidTank fluid_moved;
  private int pour_length;
  private FluidTankInfo[] tank_info;
  private int next_drain;
  private int next_fill;

  public TileEntityRefractorySpout()
  {

    next_drain = 12;
    next_fill = 3;

    tank = new FluidTank(250);
    fluid_moved = new FluidTank(10);
    pour_length = 0;
    tank_info = new FluidTankInfo[2];
    tank_info[0] = new FluidTankInfo(tank);
    tank_info[1] = new FluidTankInfo(fluid_moved);
  }

  public int getPourLength()
  {
    return pour_length;
  }
  
  @Override
  public void readFromNBT(NBTTagCompound compund)
  {
    super.readFromNBT(compund);

    if(compund.hasKey("next_drain"))
    {
      next_drain = compund.getInteger("next_drain");
    }
    if(compund.hasKey("next_fill"))
    {
      next_fill = compund.getInteger("next_fill");
    }
    if(compund.hasKey("pour_length"))
    {
      pour_length = compund.getInteger("pour_length");
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    compound.setInteger("next_drain", next_drain);
    compound.setInteger("next_fill", next_fill);
    compound.setInteger("pour_length", pour_length);
  }

  @Override
  public int getSizeInventory()
  {
    return 0;
  }

  @Override
  public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
    return false;
  }

  @Override
  public int fill(EnumFacing from, FluidStack resource, boolean doFill)
  {
    if(from != worldObj.getBlockState(getPos()).getValue(BlockRefractorySpout.FACING).facing)
    {
      return 0;
    }
    if(doFill && resource != null && doFill)
    {
      next_drain = 12;
    }
    return fillTank(0, resource, doFill);
  }

  @Override
  public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
  {
    return null;
  }

  @Override
  public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
  {
    return null;
  }

  @Override
  public boolean canFill(EnumFacing from, Fluid fluid)
  {
    return from == worldObj.getBlockState(getPos()).getValue(BlockRefractorySpout.FACING).facing;
  }

  @Override
  public boolean canDrain(EnumFacing from, Fluid fluid)
  {
    return false;
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

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
  {
    return oldState.getBlock() != newSate.getBlock();
  }

  static private boolean areFluidStacksEqual(FluidStack a,FluidStack b)
  {
    if(a == null)
    {
      return b == null;
    }
    if(b == null)
    {
      return false;
    }
    
    return a.isFluidStackIdentical(b);
  }
  
  @Override
  protected void updateServer()
  {
    if(--next_drain == 0)
    {
      next_drain = 12;

      // Drain from the top TileEntity
      EnumFacing side = worldObj.getBlockState(getPos()).getValue(BlockRefractorySpout.FACING).facing;
      TileEntity source = worldObj.getTileEntity(getPos().add(side.getDirectionVec()));
      side = side.getOpposite();
      if(source instanceof IFluidHandler)
      {
        IFluidHandler hsource = (IFluidHandler) source;
        FluidStack drained = hsource.drain(side, 40, false);
        if(drained != null && !drained.getFluid().isGaseous(drained) && drained.getFluid().getDensity(drained) > 0)
        {
          drained.amount = tank.fill(drained, false);
          if(drained.amount > 0)
          {
            hsource.drain(side, drained, true);
            tank.fill(drained, true);
            updateTank(0);
          }
        }
      }
    }

    if(--next_fill == 0)
    {
      next_fill = 3;
      FluidStack last_moved = fluid_moved.getFluid();
      fluid_moved.setFluid(null);
      if(worldObj.getBlockState(getPos()).getValue(BlockFoundrySidedMachine.STATE) == BlockFoundrySidedMachine.EnumMachineState.ON)
      {
        // Fill to the bottom
        if(tank.getFluid() != null && tank.getFluid().amount > 0)
        {
          int down = 0;
          while(true)
          {
            BlockPos pos = getPos().down(++down);
            if(pos.getY() < 0)
            {
              break;
            }
            IBlockState state = worldObj.getBlockState(pos);
            if(state.getBlock().isAir(state, worldObj, pos))
            {
              continue;
            }
            TileEntity dest = worldObj.getTileEntity(pos);
            if(dest instanceof IFluidHandler)
            {
              IFluidHandler hdest = (IFluidHandler) dest;
              if(hdest.canFill(EnumFacing.UP, tank.getFluid().getFluid()))
              {
                FluidStack drained = tank.drain(10, false);
                if(drained != null)
                {
                  drained.amount = hdest.fill(EnumFacing.UP, drained, false);
                  if(drained.amount > 0)
                  {
                    tank.drain(drained.amount, true);
                    hdest.fill(EnumFacing.UP, drained, true);
                    updateTank(0);
                    fluid_moved.setFluid(drained.copy());
                    pour_length = down - 1;
                    updateValue("pour_length",pour_length);
                  }
                }
              }
            }
            break;
          }
        }
      }
      if(!areFluidStacksEqual(fluid_moved.getFluid(), last_moved))
      {
        updateTank(1);
      }
    }
  }

  @Override
  public FluidTank getTank(int slot)
  {
    switch(slot)
    {
      case 0:
        return tank;
      case 1:
        return fluid_moved;
    }
    return null;
  }

  @Override
  public int getTankCount()
  {
    return 2;
  }

  @Override
  protected void onInitialize()
  {

  }
}
