package exter.foundry.tileentity;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityRefractoryTank extends TileEntityFoundry implements ISidedInventory, IFluidHandler
{
  static public final int INVENTORY_CONTAINER_DRAIN = 0;
  static public final int INVENTORY_CONTAINER_FILL = 1;
  private FluidTank tank;
  private FluidTankInfo[] tank_info;

  public TileEntityRefractoryTank()
  {

    tank = new FluidTank(getTankCapacity());
    tank_info = new FluidTankInfo[1];
    tank_info[0] = new FluidTankInfo(tank);
    addContainerSlot(new ContainerSlot(0, INVENTORY_CONTAINER_DRAIN, false));
    addContainerSlot(new ContainerSlot(0, INVENTORY_CONTAINER_FILL, true));
  }
  
  protected int getTankCapacity()
  {
    return 16000;
  }

  @Override
  public int getSizeInventory()
  {
    return 2;
  }

  @Override
  public void openInventory(EntityPlayer player)
  {

  }

  @Override
  public void closeInventory(EntityPlayer player)
  {

  }


  static private final int[] SLOTS = {};

  @Override
  public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
    return false;
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side)
  {
    return SLOTS;
  }

  @Override
  public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing side)
  {
    return isItemValidForSlot(i, itemstack);
  }

  @Override
  public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing side)
  {
    return false;
  }

  @Override
  public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
  {
    return drainTank(0,resource,doDrain);
  }

  @Override
  public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
  {
    return drainTank(0,maxDrain,doDrain);
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
  public int fill(EnumFacing from, FluidStack resource, boolean doFill)
  {
    return fillTank(0,resource,doFill);
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
  protected void updateServer()
  {
  }

  @Override
  public FluidTank getTank(int slot)
  {
    return tank;
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
