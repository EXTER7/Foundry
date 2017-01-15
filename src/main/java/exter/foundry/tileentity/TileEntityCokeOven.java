package exter.foundry.tileentity;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.api.FoundryAPI;
import exter.foundry.block.BlockCokeOven;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandler;


public class TileEntityCokeOven extends TileEntityFoundryHeatable
{
  
  static public final int BAKE_TIME = 60000000;
  
  static public final int BAKE_TEMP = 160000;
  
  static public final int INVENTORY_INPUT = 0;
  static public final int INVENTORY_OUTPUT = 1;

  static private final Set<Integer> IH_SLOTS_INPUT = ImmutableSet.of(INVENTORY_INPUT);
  static private final Set<Integer> IH_SLOTS_OUTPUT = ImmutableSet.of(INVENTORY_OUTPUT);

  private ItemHandler item_handler;

  private int progress;
  
  
  public TileEntityCokeOven()
  {
    super();
    progress = 0;
    item_handler = new ItemHandler(getSizeInventory(),IH_SLOTS_INPUT,IH_SLOTS_OUTPUT);
  }
  
  @Override
  protected IItemHandler getItemHandler(EnumFacing side)
  {
    return item_handler;
  }

  @Override
  public void readFromNBT(NBTTagCompound compund)
  {
    super.readFromNBT(compund);
    
    if(compund.hasKey("progress"))
    {
      progress = compund.getInteger("progress");
    }
    if(world != null && !world.isRemote)
    {
      ((BlockCokeOven)getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()), progress > 0);
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
  

  @Override
  public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
    return i == INVENTORY_INPUT;
  }

  @Override
  protected void updateClient()
  {

  }
  
  private boolean canBake()
  {
    if(getTemperature() <= BAKE_TEMP)
    {
      return false;
    }
    ItemStack input = inventory[INVENTORY_INPUT];
    ItemStack output = inventory[INVENTORY_OUTPUT];
    if(input.isEmpty() || input.getItem() != Items.COAL || input.getMetadata() != 0)
    {
      return false;
    }
    if(!output.isEmpty() && output.getItem() != FoundryItems.item_component
       && output.getMetadata() != ItemComponent.SubItem.COAL_COKE.id
       && output.getCount() == output.getMaxStackSize())
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
        
    int increment = heat - BAKE_TEMP;
    progress += increment;
    if(progress >= BAKE_TIME)
    {
      if(inventory[INVENTORY_OUTPUT].isEmpty())
      {
        inventory[INVENTORY_OUTPUT] = FoundryItems.component(ItemComponent.SubItem.COAL_COKE);
      } else
      {
        inventory[INVENTORY_OUTPUT].grow(1);        
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
        ((BlockCokeOven)getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()), progress > 0);
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
    return 220000;
  }

  @Override
  protected int getTemperatureLossRate()
  {
    return FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE;
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
