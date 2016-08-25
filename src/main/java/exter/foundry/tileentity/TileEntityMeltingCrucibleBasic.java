package exter.foundry.tileentity;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;


public class TileEntityMeltingCrucibleBasic extends TileEntityFoundryHeatable implements ISidedInventory,net.minecraftforge.fluids.IFluidHandler
{
  
  static public final int SMELT_TIME = 5000000;

  
  static public final int INVENTORY_INPUT = 0;
  static public final int INVENTORY_CONTAINER_DRAIN = 1;
  static public final int INVENTORY_CONTAINER_FILL = 2;

  @Deprecated
  static private final int[] INSERT_SLOTS = { INVENTORY_INPUT };

  static private final Set<Integer> IH_SLOTS_INPUT = ImmutableSet.of(INVENTORY_INPUT);
  static private final Set<Integer> IH_SLOTS_OUTPUT = ImmutableSet.of();

  private FluidTank tank;
  private IFluidHandler fluid_handler;
  private ItemHandler item_handler;
  
  private int progress;
  private int melt_point;
  private IMeltingRecipe current_recipe;

  
  
  public TileEntityMeltingCrucibleBasic()
  {
    super();
    tank = new FluidTank(FoundryAPI.CRUCIBLE_TANK_CAPACITY);
    fluid_handler = new FluidHandler(-1,0);
    item_handler = new ItemHandler(getSizeInventory(),IH_SLOTS_INPUT,IH_SLOTS_OUTPUT);

    progress = 0;
    melt_point = 0;
    current_recipe = null;
    
    addContainerSlot(new ContainerSlot(0,INVENTORY_CONTAINER_DRAIN,false));
    addContainerSlot(new ContainerSlot(0,INVENTORY_CONTAINER_FILL,true));
  }
  
  @Override
  protected IItemHandler getItemHandler(EnumFacing side)
  {
    return item_handler;
  }

  @Override
  protected IFluidHandler getFluidHandler(EnumFacing facing)
  {
    return fluid_handler;
  }

  @Override
  public void readFromNBT(NBTTagCompound compund)
  {
    super.readFromNBT(compund);
    
    if(compund.hasKey("progress"))
    {
      progress = compund.getInteger("progress");
    }
    
    if(compund.hasKey("melt_point"))
    {
      melt_point = compund.getInteger("melt_point");
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
    compound.setInteger("melt_point", melt_point);
    compound.setInteger("progress", progress);
    return compound;
  }

  @Override
  public int getSizeInventory()
  {
    return 3;
  }

  
  public int getProgress()
  {
    return progress;
  }
  
  public int getMeltingPoint()
  {
    return melt_point;
  }


  @Override
  public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
    return i == INVENTORY_INPUT;
  }

  @Deprecated
  @Override
  public int[] getSlotsForFace(EnumFacing side)
  {
    return INSERT_SLOTS;
  }

  @Deprecated
  @Override
  public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing side)
  {
    return isItemValidForSlot(i, itemstack);
  }

  @Deprecated
  @Override
  public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing side)
  {
    return false;
  }

  @Override
  protected void updateClient()
  {

  }
  
  private void checkCurrentRecipe()
  {
    if(current_recipe == null)
    {
      progress = 0;
      return;
    }
    
    if(!current_recipe.matchesRecipe(inventory[INVENTORY_INPUT]))
    {
      progress = 0;
      current_recipe = null;
    }
  }
  
  private void doMeltingProgress()
  {
    if(current_recipe == null)
    {
      progress = 0;
      melt_point = 0;
      return;
    }
    
    FluidStack fs = current_recipe.getOutput();
    melt_point = current_recipe.getMeltingPoint() * 100;
    
    int heat = getTemperature();
        
    if(heat <= melt_point || tank.fill(fs, false) < fs.amount)
    {
      progress = 0;
      return;
    }
    int increment = (heat - melt_point) * 5 * current_recipe.getMeltingSpeed() / (fs.amount * 4);
    if(increment < 1)
    {
      increment = 1;
    }
    if(increment > SMELT_TIME / 4)
    {
      increment = SMELT_TIME / 4;
    }
    progress += increment;
    if(progress >= SMELT_TIME)
    {
      progress = 0;
      tank.fill(fs, true);
      decrStackSize(INVENTORY_INPUT,current_recipe.getInput().getAmount());
      updateTank(0);
    }
  }

 
  @Override
  protected void updateServer()
  {
    super.updateServer();
    int last_progress = progress;
    int last_melt_point = melt_point;
    
    checkCurrentRecipe();
    if(current_recipe == null)
    {
      current_recipe = MeltingRecipeManager.instance.findRecipe(inventory[INVENTORY_INPUT]);
    }
    
    doMeltingProgress();
    
    if(last_progress != progress)
    {
      updateValue("progress",progress);
    } 
    
    if(last_melt_point != melt_point)
    {
      updateValue("melt_point",melt_point);
    }
  }

  @Override
  public FluidTank getTank(int slot)
  {
    if(slot != 0)
    {
      return null;
    }
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

  @Override
  public int getMaxTemperature()
  {
    return FoundryAPI.CRUCIBLE_BASIC_MAX_TEMP;
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
