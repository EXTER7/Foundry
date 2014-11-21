package exter.foundry.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityRefractoryHopper extends TileEntityFoundry
{
  //TODO
  private FluidTank tank;
  
  public TileEntityRefractoryHopper()
  {
    tank = new FluidTank(2000);
  }

  @Override
  public int getSizeInventory()
  {
    return 0;
  }

  @Override
  public ItemStack getStackInSlot(int p_70301_1_)
  {
    return null;
  }

  @Override
  public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
  {
    return null;
  }

  @Override
  public ItemStack getStackInSlotOnClosing(int p_70304_1_)
  {
    return null;
  }

  @Override
  public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
  {

  }

  @Override
  public String getInventoryName()
  {
    return "foundry.refractoryTank";
  }

  @Override
  public boolean hasCustomInventoryName()
  {
    return true;
  }

  @Override
  public int getInventoryStackLimit()
  {
    return 0;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
  {
    return false;
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
  public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
  {
    return false;
  }

  @Override
  protected void UpdateEntityClient()
  {

  }

  @Override
  protected void UpdateEntityServer()
  {

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
