package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.heatable.IHeatProvider;
import exter.foundry.block.BlockBurnerHeater;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.api.item.IExoflameHeatable;

@Optional.Interface(iface = "vazkii.botania.api.item.IExoflameHeatable", modid = "Botania")
public class TileEntityBurnerHeater extends TileEntityFoundry implements ISidedInventory,IExoflameHeatable
{
  private class HeatProvider implements IHeatProvider
  {
    @Override
    public int provideHeat(int max_heat)
    {
      if(burn_time > 0 && max_heat > 0)
      {
        if(max_heat > 0)
        {
          if(max_heat > MAX_PROVIDE)
          {
            max_heat = MAX_PROVIDE;
          }
          int last_burn_time = burn_time;
          burn_time -= FoundryMiscUtils.divCeil(max_heat * 10,MAX_PROVIDE);
          if(burn_time < 0)
          {
            burn_time = 0;
          }
          
          if(last_burn_time != burn_time || update_burn_times)
          {
            if(last_burn_time*burn_time == 0)
            {
              ((BlockBurnerHeater)getBlockType()).setMachineState(worldObj, getPos(), worldObj.getBlockState(getPos()), burn_time > 0);
            }
            updateValue("BurnTime",burn_time);
          }
        }
        return max_heat;
      }

      return 0;
    }
  }
  

  private int burn_time;
  private int item_burn_time;
  private boolean update_burn_times;

  private static int MAX_PROVIDE = TileEntityFoundryHeatable.getMaxHeatRecieve(TileEntityMeltingCrucible.TEMP_LOSS_RATE,170000);

  private HeatProvider heat_provider;

  private static final int[] SLOTS = new int[] { 0, 1, 2, 3 };

  public TileEntityBurnerHeater()
  {
    burn_time = 0;
    item_burn_time = 0;
    update_burn_times = false;
    heat_provider = new HeatProvider();
  }

  @Override
  public int getSizeInventory()
  {
    return 4;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag)
  {
    super.readFromNBT(tag);
    if(tag.hasKey("BurnTime"))
    {
      burn_time = tag.getInteger("BurnTime");
    }
    if(tag.hasKey("ItemBurnTime"))
    {
      item_burn_time = tag.getInteger("ItemBurnTime");
    }
    if(worldObj != null && !worldObj.isRemote)
    {
      ((BlockBurnerHeater)getBlockType()).setMachineState(worldObj, getPos(), worldObj.getBlockState(getPos()), burn_time > 0);
    }
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
  {
    return oldState.getBlock() != newSate.getBlock();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound)
  {
    if(compound == null)
    {
      compound = new NBTTagCompound();
    }
    super.writeToNBT(compound);
    compound.setInteger("BurnTime", burn_time);
    compound.setInteger("ItemBurnTime", item_burn_time);
    return compound;
  }

  @Override
  public int getInventoryStackLimit()
  {
    return 64;
  }

  public boolean isBurning()
  {
    return burn_time > 0;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
  {
    return this.worldObj.getTileEntity(getPos()) != this ? false : par1EntityPlayer.getDistanceSq(getPos()) <= 64.0D;
  }

  @Override
  public void openInventory(EntityPlayer player)
  {

  }

  @Override
  public void closeInventory(EntityPlayer player)
  {

  }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack stack)
  {
    return TileEntityFurnace.isItemFuel(stack);
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side)
  {
    return SLOTS;
  }

  /**
   * Returns true if automation can insert the given item in the given slot from
   * the given side. Args: Slot, item, side
   */
  public boolean canInsertItem(int par1, ItemStack par2ItemStack, EnumFacing side)
  {
    return this.isItemValidForSlot(par1, par2ItemStack);
  }

  @Override
  public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side)
  {
    return true;
  }

  @Override
  protected void updateClient()
  {

  }

  @Override
  protected void updateServer()
  {
    int last_burn_time = burn_time;
    int last_item_burn_time = item_burn_time;


    if(burn_time < 10)
    {
      for(int i = 0; i < 4; i++)
      {
        if(inventory[i] != null)
        {
          int burn = TileEntityFurnace.getItemBurnTime(inventory[i]) * 10;
          if(burn > 0)
          {
            burn_time += burn;
            item_burn_time = burn_time;
            if(--inventory[i].stackSize == 0)
            {
              inventory[i] = inventory[i].getItem().getContainerItem(inventory[i]);
            }
            updateInventoryItem(i);
            break;
          }
        }
      }
    }

    if(last_burn_time != burn_time || update_burn_times)
    {
      if(last_burn_time*burn_time == 0)
      {
        ((BlockBurnerHeater)getBlockType()).setMachineState(worldObj, getPos(), worldObj.getBlockState(getPos()), burn_time > 0);
      }
      updateValue("BurnTime",burn_time);
    }

    if(last_item_burn_time != item_burn_time || update_burn_times)
    {
      updateValue("ItemBurnTime",item_burn_time);
    }
    update_burn_times = false;
  }

  @Override
  public FluidTank getTank(int slot)
  {
    return null;
  }

  @Override
  public int getTankCount()
  {
    return 0;
  }

  @Override
  protected void onInitialize()
  {

  }

  public int getBurningTime()
  {
    return burn_time;
  }

  public int getItemBurnTime()
  {
    return item_burn_time;
  }

  @Override
  public boolean hasCapability(Capability<?> cap,EnumFacing facing)
  {
    return super.hasCapability(cap, facing) || (cap == FoundryAPI.capability_heatprovider && facing == EnumFacing.UP);
  }
  
  @Override
  public <T> T getCapability(Capability<T> cap, EnumFacing facing)
  {
    if(cap == FoundryAPI.capability_heatprovider && facing == EnumFacing.UP)
    {
      return FoundryAPI.capability_heatprovider.cast(heat_provider);
    }
    return super.getCapability(cap, facing);
  }

  
  @Optional.Method(modid = "Botania")
  @Override
  public boolean canSmelt()
  {
    return true;
  }

  @Optional.Method(modid = "Botania")
  @Override
  public int getBurnTime()
  {
    return burn_time <= 1 ? 0 : burn_time - 1;
  }

  @Optional.Method(modid = "Botania")
  @Override
  public void boostBurnTime()
  {
    if(!worldObj.isRemote)
    {
      burn_time = 2000;
      item_burn_time = 1999;
      update_burn_times = true;
      markDirty();
    }
  }

  @Optional.Method(modid = "Botania")
  @Override
  public void boostCookTime()
  {

  }
}
