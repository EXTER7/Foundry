package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.block.BlockCokeOven;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;


public class TileEntityCokeOven extends TileEntityFoundryHeatable implements ISidedInventory
{
  
  static public final int BAKE_TIME = 100000000;
  
  static public final int BAKE_TEMP = 140000;
  
  static public final int INVENTORY_INPUT = 0;
  static public final int INVENTORY_OUTPUT = 1;


  private int progress;

  
  
  public TileEntityCokeOven()
  {
    super();
    progress = 0;
  }

  @Override
  public void readFromNBT(NBTTagCompound compund)
  {
    super.readFromNBT(compund);
    
    if(compund.hasKey("progress"))
    {
      progress = compund.getInteger("progress");
    }
    if(worldObj != null && !worldObj.isRemote)
    {
      ((BlockCokeOven)getBlockType()).setMachineState(worldObj, getPos(), worldObj.getBlockState(getPos()), progress > 0);
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
    compound.setInteger("progress", progress);
    return compound;
  }
  
  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
  {
    return oldState.getBlock() != newSate.getBlock();
  }
  
  @Override
  public int getSizeInventory()
  {
    return 2;
  }

  
  public int getProgress()
  {
    return progress;
  }
  
  static private final int[] SLOTS = { INVENTORY_INPUT, INVENTORY_OUTPUT };

  @Override
  public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
    return i == INVENTORY_INPUT;
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
    return i == INVENTORY_OUTPUT;
  }

  @Override
  protected void updateClient()
  {

  }
  
  private boolean canBake()
  {
    ItemStack input = inventory[INVENTORY_INPUT];
    ItemStack output = inventory[INVENTORY_OUTPUT];
    if(input == null || input.getItem() != Items.COAL || input.getMetadata() != 0)
    {
      return false;
    }
    if(getTemperature() <= BAKE_TEMP
        || (output != null && output.getItem() != FoundryItems.item_component
        && output.getMetadata() != ItemComponent.SubItem.COAL_COKE.id
        && output.stackSize == output.getMaxStackSize()))
    {
      return false;
    }
    return true;
  }
  
  private void doBakingProgress()
  {
    if(!canBake())
    {
      progress = 0;
      return;
    }
    int heat = getTemperature();
        
    int increment = (heat - BAKE_TEMP) * 4;
    if(increment < 1)
    {
      increment = 1;
    }
    progress += increment;
    if(progress >= BAKE_TIME)
    {
      if(inventory[INVENTORY_OUTPUT] == null)
      {
        inventory[INVENTORY_OUTPUT] = FoundryItems.component(ItemComponent.SubItem.COAL_COKE);
      } else
      {
        inventory[INVENTORY_OUTPUT].stackSize++;        
      }
      progress = 0;
      decrStackSize(INVENTORY_INPUT,1);
      updateInventoryItem(INVENTORY_OUTPUT);
      markDirty();
    }
  }

 
  @Override
  protected void updateServer()
  {
    super.updateServer();
    int last_progress = progress;

    doBakingProgress();
    
    if(last_progress != progress)
    {
      if(last_progress*progress == 0)
      {
        ((BlockCokeOven)getBlockType()).setMachineState(worldObj, getPos(), worldObj.getBlockState(getPos()), progress > 0);
      }
      updateValue("progress",progress);
    }
    
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

  @Override
  public int getMaxTemperature()
  {
    return 200000;
  }

  @Override
  protected int getTemperatureLossRate()
  {
    return FoundryAPI.CRUCIBLE_TEMP_LOSS_RATE;
  }

  @Override
  protected boolean canReceiveHeat()
  {
    boolean active = true;
    switch(getRedstoneMode())
    {
      case RSMODE_OFF:
        if(redstone_signal)
        {
          active = false;
        }
        break;
      case RSMODE_ON:
        if(!redstone_signal)
        {
          active = false;
        }
        break;
      default:
    }
    return active;
  }
}
