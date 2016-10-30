package exter.foundry.tileentity;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityMetalInfuser extends TileEntityFoundryPowered
{

  
  static public final int INVENTORY_SUBSTANCE_INPUT = 0;
  static public final int INVENTORY_CONTAINER_INPUT_DRAIN = 1;
  static public final int INVENTORY_CONTAINER_INPUT_FILL = 2;
  static public final int INVENTORY_CONTAINER_OUTPUT_DRAIN = 3;
  static public final int INVENTORY_CONTAINER_OUTPUT_FILL = 4;

  static public final int TANK_INPUT = 0;
  static public final int TANK_OUTPUT = 1;



  static private final Set<Integer> IH_SLOTS_INPUT = ImmutableSet.of(INVENTORY_SUBSTANCE_INPUT);
  static private final Set<Integer> IH_SLOTS_OUTPUT = ImmutableSet.of();


  private FluidTank[] tanks;
  private IFluidHandler fluid_handler;
  private ItemHandler item_handler;
  
  private int progress;
  private int extract_energy;
  
  private IInfuserRecipe current_recipe;
 
  public TileEntityMetalInfuser()
  {
    super();

    tanks = new FluidTank[2];
    tanks[0] = new FluidTank(FoundryAPI.INFUSER_TANK_CAPACITY);
    tanks[1] = new FluidTank(FoundryAPI.INFUSER_TANK_CAPACITY);
    fluid_handler = new FluidHandler(TANK_INPUT,TANK_OUTPUT);
    item_handler = new ItemHandler(getSizeInventory(),IH_SLOTS_INPUT,IH_SLOTS_OUTPUT);

    progress = 0;
    extract_energy = 1;
    
    current_recipe = null;
    
    addContainerSlot(new ContainerSlot(TANK_INPUT,INVENTORY_CONTAINER_INPUT_DRAIN,false));
    addContainerSlot(new ContainerSlot(TANK_INPUT,INVENTORY_CONTAINER_INPUT_FILL,true));
    addContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_DRAIN,false));
    addContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_FILL,true));
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

    if(compund.hasKey("extract_time"))
    {
      extract_energy = compund.getInteger("extract_time");
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
    compound.setInteger("extract_time", extract_energy);
    return compound;
  }

  @Override
  public int getSizeInventory()
  {
    return 5;
  }

  @Override
  public void openInventory(EntityPlayer player)
  {

  }  

  @Override
  public void closeInventory(EntityPlayer player)
  {

  }

  public int getProgress()
  {
    return progress;
  }


  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemstack)
  {
    return true;
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
      extract_energy = 1;
      return;
    }

    if(!current_recipe.matchesRecipe(getTank(TANK_INPUT).getFluid(),inventory[INVENTORY_SUBSTANCE_INPUT]))
    {
      progress = 0;
      extract_energy = 1;
      current_recipe = null;
      return;
    }
  }
  
  private void doInfusion()
  {
    FluidStack output = current_recipe.getOutput();
    extract_energy = current_recipe.getEnergyNeeded();
    if(fillTank(TANK_OUTPUT, output, false) < output.amount)
    {
      progress = 0;
      return;
    }
      
    if(getStoredFoundryEnergy() > 0)
    {
      int needed = extract_energy - progress;
      if(needed > 200)
      {
        needed = 200;
      }
      long energy = useFoundryEnergy(needed, true);
      progress += energy;
      if(progress >= extract_energy)
      {
        progress = 0;
        drainTank(TANK_INPUT, current_recipe.getInputFluid(), true);
        fillTank(TANK_OUTPUT, output,true);
        decrStackSize(INVENTORY_SUBSTANCE_INPUT, 1);
      }
    }
  }

  @Override
  protected void updateServer()
  {
    super.updateServer();
    int last_progress = progress;
    int last_extract_time = extract_energy;
    
    checkCurrentRecipe();
    
    if(current_recipe == null)
    {
      current_recipe = InfuserRecipeManager.instance.findRecipe(tanks[TANK_INPUT].getFluid(),inventory[INVENTORY_SUBSTANCE_INPUT]);
      progress = 0;
    }
    
    if(current_recipe != null)
    {
      doInfusion();
    }

    if(last_extract_time != extract_energy)
    {
      updateValue("extract_time",extract_energy);
    }

    if(last_progress != progress)
    {
      updateValue("progress",progress);
    }
  }

  public int getExtractTime()
  {
    return extract_energy;
  }

  @Override
  public FluidTank getTank(int slot)
  {
    return tanks[slot];
  }

  @Override
  public int getTankCount()
  {
    return 2;  
  }

  @Override
  public long getFoundryEnergyCapacity()
  {
    return 3000;
  }
}
