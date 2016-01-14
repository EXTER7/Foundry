package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.api.recipe.IInfuserSubstanceRecipe;
import exter.foundry.api.substance.InfuserSubstance;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityMetalInfuser extends TileEntityFoundryPowered implements ISidedInventory,IFluidHandler
{

  
  static public final int INVENTORY_SUBSTANCE_INPUT = 0;
  static public final int INVENTORY_CONTAINER_INPUT_DRAIN = 1;
  static public final int INVENTORY_CONTAINER_INPUT_FILL = 2;
  static public final int INVENTORY_CONTAINER_OUTPUT_DRAIN = 3;
  static public final int INVENTORY_CONTAINER_OUTPUT_FILL = 4;

  static public final int TANK_INPUT = 0;
  static public final int TANK_OUTPUT = 1;
  private FluidTank[] tanks;
  private FluidTankInfo[] tank_info;

  private InfuserSubstance substance;
  
  static private final int INFUSE_ENERGY_NEEDED = 100;
  
  private int progress;
  private int extract_energy;
  
  private IInfuserSubstanceRecipe current_substance_recipe;
 
  public TileEntityMetalInfuser()
  {
    super();

    int i;
    tanks = new FluidTank[2];
    tank_info = new FluidTankInfo[2];
    for(i = 0; i < 2; i++)
    {
      tanks[i] = new FluidTank(FoundryAPI.INFUSER_TANK_CAPACITY);
      tank_info[i] = new FluidTankInfo(tanks[i]);
    }
    progress = 0;
    extract_energy = 1;
    
    current_substance_recipe = null;
    
    addContainerSlot(new ContainerSlot(TANK_INPUT,INVENTORY_CONTAINER_INPUT_DRAIN,false));
    addContainerSlot(new ContainerSlot(TANK_INPUT,INVENTORY_CONTAINER_INPUT_FILL,true));
    addContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_DRAIN,false));
    addContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_FILL,true));

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
    
    NBTTagCompound substance_tag = (NBTTagCompound)compund.getTag("Substance");
    if(substance_tag != null)
    {
      if(substance_tag.getBoolean("empty"))
      {
        substance = null;
      } else
      {
        substance = InfuserSubstance.readFromNBT(substance_tag);
      }
    }
  }

  private void WriteSubstanceToNBT(NBTTagCompound compound)
  {
    if(substance != null)
    {
      compound.setBoolean("empty", false);
      substance.writeToNBT(compound);
    } else
    {
      compound.setBoolean("empty", true);
    }
  }


  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    NBTTagCompound substance_tag = new NBTTagCompound();
    WriteSubstanceToNBT(substance_tag);
    compound.setTag("Substance", substance_tag);
    compound.setInteger("progress", progress);
    compound.setInteger("extract_time", extract_energy);
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

  static private final int[] INSERT_SLOTS = { 0 };
  static private final int[] EXTRACT_SLOTS = { 0 };

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemstack)
  {
    return slot == 0;
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side)
  {
    return side == EnumFacing.UP?INSERT_SLOTS:EXTRACT_SLOTS;
  }

  @Override
  public boolean canInsertItem(int slot, ItemStack itemstack, EnumFacing side)
  {
    return isItemValidForSlot(slot, itemstack);
  }

  @Override
  public boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing side)
  {
    return slot == 0;
  }

  @Override
  public int fill(EnumFacing from, FluidStack resource, boolean doFill)
  {
    return fillTank(TANK_INPUT, resource, doFill);
  }

  @Override
  public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
  {
    return drainTank(TANK_OUTPUT, resource, doDrain);
  }

  @Override
  public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
  {
    return drainTank(TANK_OUTPUT, maxDrain, doDrain);
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
  public FluidTankInfo[] getTankInfo(EnumFacing from)
  {
    return tank_info;
  }

  @Override
  protected void updateClient()
  {
    
  }

  
  private void checkCurrentSubstanceRecipe()
  {
    if(current_substance_recipe == null)
    {
      progress = 0;
      extract_energy = 1;
      return;
    }

    if(!current_substance_recipe.matchesRecipe(inventory[INVENTORY_SUBSTANCE_INPUT]))
    {
      progress = 0;
      extract_energy = 1;
      current_substance_recipe = null;
      return;
    }
  }
  
  private void doSubstanceExtraction()
  {
    if(current_substance_recipe == null)
    {
      progress = 0;
      extract_energy = 1;
      return;
    }
      
    InfuserSubstance recipe_sub = current_substance_recipe.getOutput();
    if(substance != null
        && (!recipe_sub.isSubstanceEqual(substance)
        || FoundryAPI.INFUSER_SUBSTANCE_AMOUNT_MAX - substance.amount < recipe_sub.amount))
    {
      progress = 0;
      extract_energy = 1;
      return;
    }
    extract_energy = current_substance_recipe.getEnergyNeeded();
    if(getStoredFoundryEnergy() > 0)
    {
      int energy = useFoundryEnergy(600, true);
      progress += energy;
      if(progress >= extract_energy)
      {
        progress -= extract_energy;
        if(substance == null)
        {
          substance = new InfuserSubstance(recipe_sub);
        } else
        {
          substance.amount += recipe_sub.amount;
        }
        decrStackSize(0, 1);
        NBTTagCompound tag = new NBTTagCompound();
        WriteSubstanceToNBT(tag);
        updateNBTTag("Substance",tag);
        updateInventoryItem(0);
      }
    }
  }

  @Override
  protected void updateServer()
  {
    super.updateServer();
    int last_progress = progress;
    int last_extract_time = extract_energy;
    
    if(tanks[TANK_INPUT].getFluidAmount() > 0 && getStoredFoundryEnergy() >= INFUSE_ENERGY_NEEDED)
    {
      IInfuserRecipe recipe = InfuserRecipeManager.instance.findRecipe(tanks[TANK_INPUT].getFluid(), substance);
      if(recipe != null)
      {
        FluidStack result = recipe.getOutput();
        if(tanks[TANK_OUTPUT].fill(result, false) == result.amount)
        {
          tanks[TANK_INPUT].drain(recipe.getInputFluid().amount, true);
          tanks[TANK_OUTPUT].fill(result,true);
          useFoundryEnergy(INFUSE_ENERGY_NEEDED, true);
          substance.amount -= recipe.getInputSubstance().amount;
          if(substance.amount <= 0)
          {
            substance = null;
          }
          NBTTagCompound tag = new NBTTagCompound();
          WriteSubstanceToNBT(tag);
          updateNBTTag("Substance",tag);

          updateTank(TANK_INPUT);
          updateTank(TANK_OUTPUT);
        }
      }
    }

    
    checkCurrentSubstanceRecipe();
    
    if(current_substance_recipe == null)
    {
      current_substance_recipe = InfuserRecipeManager.instance.findSubstanceRecipe(inventory[INVENTORY_SUBSTANCE_INPUT]);
      progress = 0;
    }
    
    doSubstanceExtraction();

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
  
  public InfuserSubstance getSubstance()
  {
    return substance;
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
  public int getFoundryEnergyCapacity()
  {
    return 3000;
  }
}
