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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityRefractorySpout extends TileEntityFoundry
{
  protected class FluidHandler implements IFluidHandler
  {
    private IFluidTankProperties[] props;
    
    public FluidHandler()
    {
      props = new IFluidTankProperties[0];
    }

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
      return props;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
      return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
      return null;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
      return null;
    }    
  }

  
  private FluidTank fluid_moved;
  private IFluidHandler fluid_handler;

  private int pour_length;
  private int next_move;

  public TileEntityRefractorySpout()
  {

    next_move = 2;

    fluid_moved = new FluidTank(10);
    pour_length = 0;
    fluid_handler = new FluidHandler();
  }
  
  @Override
  protected IFluidHandler getFluidHandler(EnumFacing facing)
  {
    EnumFacing side = worldObj.getBlockState(getPos()).getValue(BlockRefractorySpout.FACING).facing;
    return (facing == EnumFacing.DOWN || facing == side)?fluid_handler:null;
  }
  
  public int getPourLength()
  {
    return pour_length;
  }
  
  @Override
  public void readFromNBT(NBTTagCompound compund)
  {
    super.readFromNBT(compund);

    if(compund.hasKey("next_move"))
    {
      next_move = compund.getInteger("next_move");
    }
    if(compund.hasKey("pour_length"))
    {
      pour_length = compund.getInteger("pour_length");
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound)
  {
    if(compound == null)
    {
      compound = new NBTTagCompound();
    }
    super.writeToNBT(compound);
    compound.setInteger("next_move", next_move);
    compound.setInteger("pour_length", pour_length);
    return compound;
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

    if(--next_move == 0)
    {
      next_move = 2;
      FluidStack last_moved = fluid_moved.getFluid();
      fluid_moved.setFluid(null);

      // Get fluid from the back.
      if(worldObj.getBlockState(getPos()).getValue(BlockFoundrySidedMachine.STATE) == BlockFoundrySidedMachine.EnumMachineState.ON)
      {

        FluidStack drained = null;
        EnumFacing side = worldObj.getBlockState(getPos()).getValue(BlockRefractorySpout.FACING).facing;
        TileEntity source = worldObj.getTileEntity(getPos().add(side.getDirectionVec()));
        IFluidHandler hsource = null;
        side = side.getOpposite();
        if(source != null && source.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side))
        {
          hsource = source.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
          drained = hsource.drain(10, false);
        }

        // Fill to the bottom.
        if(drained != null)
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
            if(dest != null && dest.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,EnumFacing.UP))
            {
              IFluidHandler hdest = dest.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
              if(drained != null)
              {
                drained.amount = hdest.fill(drained, false);
                if(drained.amount > 0)
                {
                  hsource.drain(drained.amount, true);
                  hdest.fill(drained, true);
                  fluid_moved.setFluid(drained.copy());
                  pour_length = down - 1;
                  updateValue("pour_length", pour_length);
                }
              }
            }
            break;
          }
        }
      }
      if(!areFluidStacksEqual(fluid_moved.getFluid(), last_moved))
      {
        updateTank(0);
      }
    }
  }

  @Override
  public FluidTank getTank(int slot)
  {
    return fluid_moved;
  }

  @Override
  public int getTankCount()
  {
    return 1;
  }

  @Override
  protected void onInitialize()
  {

  }
}
