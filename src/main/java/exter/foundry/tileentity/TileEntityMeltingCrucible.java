package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.heatable.IHeatable;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;


public class TileEntityMeltingCrucible extends TileEntityFoundry implements ISidedInventory,IFluidHandler,IHeatable
{
  static public final int HEAT_MAX = 500000;
  static public final int HEAT_MIN = 29000;
  static public final int HEAT_LOSS_RATE = 100;
  
  static public final int SMELT_TIME = 5000000;

  
  static public final int INVENTORY_INPUT = 0;
  static public final int INVENTORY_CONTAINER_DRAIN = 1;
  static public final int INVENTORY_CONTAINER_FILL = 2;
  
  private FluidTank tank;
  private FluidTankInfo[] tank_info;

  private int progress;
  private int heat;
  private int melt_point;
  private IMeltingRecipe current_recipe;
  
  private int tick_heat;
  
  public TileEntityMeltingCrucible()
  {
    super();
    tank = new FluidTank(FoundryAPI.ICF_TANK_CAPACITY);
    
    tank_info = new FluidTankInfo[1];
    tank_info[0] = new FluidTankInfo(tank);
    progress = 0;
    heat = HEAT_MIN;
    
    melt_point = 0;
    
    current_recipe = null;
    
    tick_heat = 0;
    
    addContainerSlot(new ContainerSlot(0,INVENTORY_CONTAINER_DRAIN,false));
    addContainerSlot(new ContainerSlot(0,INVENTORY_CONTAINER_FILL,true));
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


    if(compund.hasKey("heat"))
    {
      heat = compund.getInteger("heat");
      if(heat < HEAT_MIN)
      {
        heat = HEAT_MIN;
      }
      if(heat > HEAT_MAX)
      {
        heat = HEAT_MAX;
      }
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    compound.setInteger("heat", heat);
    compound.setInteger("melt_point", melt_point);
    compound.setInteger("progress", progress);

  }

  @Override
  public int getSizeInventory()
  {
    return 3;
  }

  public int getHeat()
  {
    return heat;
  }
  
  public int getProgress()
  {
    return progress;
  }
  
  public int getMeltingPoint()
  {
    return melt_point;
  }

  static private final int[] INSERT_SLOTS = { INVENTORY_INPUT };

  @Override
  public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
    return i == INVENTORY_INPUT;
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side)
  {
    return INSERT_SLOTS;
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
  public int fill(EnumFacing from, FluidStack resource, boolean doFill)
  {
    return 0;
  }

  @Override
  public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
  {
    return drainTank(0, resource, doDrain);
  }

  @Override
  public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
  {
    return drainTank(0, maxDrain, doDrain);
  }

  @Override
  public boolean canFill(EnumFacing from, Fluid fluid)
  {
    return false;
  }

  @Override
  public boolean canDrain(EnumFacing from, Fluid fluid)
  {
    return true;
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
      decrStackSize(INVENTORY_INPUT,1);
      updateTank(0);
      updateInventoryItem(INVENTORY_INPUT);
    }
  }

  static public int getEnergyPerTickNeeded(int heat)
  {
    return heat * 3000 / HEAT_MAX + 25;
  }

  @Override
  protected void updateServer()
  {
    int last_progress = progress;
    int last_melt_point = melt_point;
    checkCurrentRecipe();
    if(current_recipe == null)
    {
      current_recipe = MeltingRecipeManager.instance.findRecipe(inventory[INVENTORY_INPUT]);
    }
    
    if(last_progress != progress)
    {
      updateValue("progress",progress);
    }

    if(last_melt_point != melt_point)
    {
      updateValue("melt_point",melt_point);
    }

    int last_heat = heat;

    
    heat += tick_heat;
    if(heat > HEAT_MAX)
    {
      heat = HEAT_MAX;
    }
    
    tick_heat = 0;
    
    heat -= (heat - HEAT_MIN) / HEAT_LOSS_RATE;
 
    doMeltingProgress();
    
    if(last_progress != progress)
    {
      updateValue("progress",progress);
    } 
    
    if(last_melt_point != melt_point)
    {
      updateValue("melt_point",melt_point);
    }

    if(last_heat / 100 != heat / 100)
    {
      updateValue("heat",heat);
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
    tick_heat = 0;
  }
  
  @Override
  public void receiveHeat(EnumFacing side, int heat)
  {
    if(side == EnumFacing.DOWN)
    {
      if(heat > 100)
      {
        heat = 100;
      }
      tick_heat += heat;
    }
  }
  
  static public int getMaxHeatRecieve(int max_heat)
  {
    return (max_heat - HEAT_MIN) / HEAT_LOSS_RATE;
  }

  @Override
  public int getMaxHeatReceive(EnumFacing side)
  {
    if(side == EnumFacing.DOWN)
    {
      return getMaxHeatRecieve(HEAT_MAX);
    }
    return 0;
  }

  
//  @Optional.Method(modid = "IC2")
//  @Override
//  public int getSinkTier()
//  {
//    return 2;
//  }
}
