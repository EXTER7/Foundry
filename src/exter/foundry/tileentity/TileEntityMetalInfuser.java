package exter.foundry.tileentity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLLog;
import exter.foundry.ModFoundry;
import exter.foundry.api.FoundryUtils;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.container.ContainerMetalInfuser;
import exter.foundry.network.FoundryPacketHandler;
import exter.foundry.recipes.InfuserRecipe;
import exter.foundry.recipes.InfuserSubstance;
import exter.foundry.recipes.InfuserSubstanceRecipe;
import exter.foundry.recipes.MeltingRecipe;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.tileentity.TileEntityFoundry.ContainerSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityMetalInfuser extends TileEntityFoundry implements ISidedInventory,IFluidHandler,IPowerReceptor
{
  static private final int NETDATAID_INPUT_TANK_FLUID = 1;
  static private final int NETDATAID_INPUT_TANK_AMOUNT = 2;

  static private final int NETDATAID_OUTPUT_TANK_FLUID = 3;
  static private final int NETDATAID_OUTPUT_TANK_AMOUNT = 4;
  
  
  static public final int INVENTORY_SUBSTANCE_INPUT = 0;
  static public final int INVENTORY_CONTAINER_INPUT_DRAIN = 1;
  static public final int INVENTORY_CONTAINER_INPUT_FILL = 2;
  static public final int INVENTORY_CONTAINER_OUTPUT_DRAIN = 3;
  static public final int INVENTORY_CONTAINER_OUTPUT_FILL = 4;
  private ItemStack[] inventory;

  static public final int TANK_INPUT = 0;
  static public final int TANK_OUTPUT = 1;
  private FluidTank[] tanks;
  private FluidTankInfo[] tank_info;

  private InfuserSubstance substance;
  
  private int progress;
  private int extract_time;
  
  
  private PowerHandler power_handler;
 
  public TileEntityMetalInfuser()
  {
    super();

    int i;
    tanks = new FluidTank[2];
    tank_info = new FluidTankInfo[2];
    for(i = 0; i < 2; i++)
    {
      tanks[i] = new FluidTank(5000);
      tank_info[i] = new FluidTankInfo(tanks[i]);
    }
    progress = 0;
    extract_time = 1;
    
    inventory = new ItemStack[5];
    
    power_handler = new PowerHandler(this,PowerHandler.Type.MACHINE);
    power_handler.configure(1, 8, 1, 8);
    power_handler.configurePowerPerdition(1, 100);
    
    AddContainerSlot(new ContainerSlot(TANK_INPUT,INVENTORY_CONTAINER_INPUT_DRAIN,false));
    AddContainerSlot(new ContainerSlot(TANK_INPUT,INVENTORY_CONTAINER_INPUT_FILL,true));
    AddContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_DRAIN,false));
    AddContainerSlot(new ContainerSlot(TANK_OUTPUT,INVENTORY_CONTAINER_OUTPUT_FILL,true));

  }
  
  @Override
  public void readFromNBT(NBTTagCompound compund)
  {
    super.readFromNBT(compund);
    
    int i;
    if(compund.hasKey("progress"))
    {
      progress = compund.getInteger("progress");
    }

    if(compund.hasKey("extract_time"))
    {
      extract_time = compund.getInteger("extract_time");
    }
    
    NBTTagCompound substance_tag = (NBTTagCompound)compund.getTag("Substance");
    if(substance_tag != null)
    {
      if(substance_tag.getBoolean("empty"))
      {
        substance = null;
      } else
      {
        substance = InfuserSubstance.ReadFromNBT(substance_tag);
      }
    }
  }

  private void WriteSubstanceToNBT(NBTTagCompound compound)
  {
    if(substance != null)
    {
      compound.setBoolean("empty", false);
      substance.WriteToNBT(compound);
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
    compound.setCompoundTag("Substance", substance_tag);
    compound.setInteger("progress", progress);
    compound.setInteger("extract_time", extract_time);
  }

  private void SetTankFluid(FluidTank tank,int value)
  {
    if(tank.getFluid() == null)
    {
      tank.setFluid(new FluidStack(value, 0));
    } else
    {
      tank.getFluid().fluidID = value;
    }
  }

  private void SetTankAmount(FluidTank tank,int value)
  {
    if(tank.getFluid() == null)
    {
      tank.setFluid(new FluidStack(0, value));
    } else
    {
      tank.getFluid().amount = value;
    }
  }

  public void GetGUINetworkData(int id, int value)
  {
    switch(id)
    {
      case NETDATAID_INPUT_TANK_FLUID:
        SetTankFluid(tanks[TANK_INPUT],value);
        break;
      case NETDATAID_INPUT_TANK_AMOUNT:
        SetTankAmount(tanks[TANK_INPUT],value);
        break;
      case NETDATAID_OUTPUT_TANK_FLUID:
        SetTankFluid(tanks[TANK_OUTPUT],value);
        break;
      case NETDATAID_OUTPUT_TANK_AMOUNT:
        SetTankAmount(tanks[TANK_OUTPUT],value);
        break;
    }
  }

  public void SendGUINetworkData(ContainerMetalInfuser container, ICrafting crafting)
  {
    crafting.sendProgressBarUpdate(container, NETDATAID_INPUT_TANK_FLUID, tanks[TANK_INPUT].getFluid() != null ? tanks[TANK_INPUT].getFluid().fluidID : 0);
    crafting.sendProgressBarUpdate(container, NETDATAID_INPUT_TANK_AMOUNT, tanks[TANK_INPUT].getFluid() != null ? tanks[TANK_INPUT].getFluid().amount : 0);
    crafting.sendProgressBarUpdate(container, NETDATAID_OUTPUT_TANK_FLUID, tanks[TANK_OUTPUT].getFluid() != null ? tanks[TANK_OUTPUT].getFluid().fluidID : 0);
    crafting.sendProgressBarUpdate(container, NETDATAID_OUTPUT_TANK_AMOUNT, tanks[TANK_OUTPUT].getFluid() != null ? tanks[TANK_OUTPUT].getFluid().amount : 0);
  }
  
  @Override
  public int getSizeInventory()
  {
    return 5;
  }

  @Override
  public ItemStack getStackInSlot(int slot)
  {
    return inventory[slot];
  }

  @Override
  public ItemStack decrStackSize(int slot, int amount)
  {
    if(inventory[slot] != null)
    {
      ItemStack is;

      if(inventory[slot].stackSize <= amount)
      {
        is = inventory[slot];
        inventory[slot] = null;
        onInventoryChanged();
        return is;
      } else
      {
        is = inventory[slot].splitStack(amount);

        if(inventory[slot].stackSize == 0)
        {
          inventory[slot] = null;
        }

        onInventoryChanged();
        return is;
      }
    } else
    {
      return null;
    }
  }

  @Override
  public ItemStack getStackInSlotOnClosing(int slot)
  {
    if(inventory[slot] != null)
    {
      ItemStack is = inventory[slot];
      inventory[slot] = null;
      return is;
    } else
    {
      return null;
    }
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack stack)
  {
    inventory[slot] = stack;

    if(stack != null && stack.stackSize > this.getInventoryStackLimit())
    {
      stack.stackSize = this.getInventoryStackLimit();
    }

    onInventoryChanged();
  }

  @Override
  public void onInventoryChanged()
  {
    super.onInventoryChanged();
  }
  
  @Override
  public String getInvName()
  {
    return "Infuser";
  }

  @Override
  public int getInventoryStackLimit()
  {
    return 64;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer player)
  {
    return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
  }


  @Override
  public void openChest()
  {

  }  

  @Override
  public void closeChest()
  {

  }

  public int GetProgress()
  {
    return progress;
  }
  
  @Override
  public boolean isInvNameLocalized()
  {
    return false;
  }

  static private final int[] INSERT_SLOTS = { 0 };
  static private final int[] EXTRACT_SLOTS = { 0 };

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemstack)
  {
    return slot == 0;
  }

  @Override
  public int[] getAccessibleSlotsFromSide(int side)
  {
    return side == 1?INSERT_SLOTS:EXTRACT_SLOTS;
  }

  @Override
  public boolean canInsertItem(int slot, ItemStack itemstack, int side)
  {
    return isItemValidForSlot(slot, itemstack);
  }

  @Override
  public boolean canExtractItem(int slot, ItemStack itemstack, int side)
  {
    return slot == 0;
  }

  @Override
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
  {
    return tanks[TANK_INPUT].fill(resource, doFill);
  }

  @Override
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    if(resource.isFluidEqual(tanks[TANK_OUTPUT].getFluid()))
    {
      return tanks[TANK_OUTPUT].drain(resource.amount, doDrain);
    }
    return null;
  }

  @Override
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    return tanks[TANK_OUTPUT].drain(maxDrain, doDrain);
  }

  @Override
  public boolean canFill(ForgeDirection from, Fluid fluid)
  {
    return true;
  }

  @Override
  public boolean canDrain(ForgeDirection from, Fluid fluid)
  {
    return true;
  }

  @Override
  public FluidTankInfo[] getTankInfo(ForgeDirection from)
  {
    return tank_info;
  }

  @Override
  protected void UpdateEntityClient()
  {
    
  }

  @Override
  protected void UpdateEntityServer()
  {

    int last_progress = progress;
    int last_extract_time = extract_time;
    InfuserSubstanceRecipe sub_recipe = InfuserRecipeManager.instance.FindSubstanceRecipe(inventory[INVENTORY_SUBSTANCE_INPUT]);
    if(sub_recipe != null)
    {
      if(substance == null || sub_recipe.substance.IsSubstanceEqual(substance) && FoundryUtils.INFUSER_SUBSTANCE_AMOUNT_MAX - sub_recipe.substance.amount >= substance.amount)
      {
        extract_time = sub_recipe.extract_energy * 100;
        if(power_handler.getEnergyStored() > 0)
        {
          int energy = (int) (power_handler.useEnergy(0, 8, true) * 100);
          progress += energy;
          if(progress >= extract_time)
          {
            progress -= extract_time;
            if(substance == null)
            {
              substance = new InfuserSubstance(sub_recipe.substance);
            } else
            {
              substance.amount += sub_recipe.substance.amount;
            }
            decrStackSize(0, 1);
            NBTTagCompound tag = new NBTTagCompound();
            WriteSubstanceToNBT(tag);
            UpdateNBTTag("Substance",tag);
            UpdateInventoryItem(0);
          }
        }
      } else
      {
        progress = 0;
        extract_time = 1;
      }
    } else
    {
      progress = 0;
      extract_time = 1;
    }

    if(last_extract_time != extract_time)
    {
      UpdateValue("extract_time",extract_time);
    }

    if(tanks[TANK_INPUT].getFluidAmount() > 0)
    {
      InfuserRecipe recipe = InfuserRecipeManager.instance.FindRecipe(tanks[TANK_INPUT].getFluid(), substance);
      if(recipe != null)
      {
        FluidStack result = recipe.output;
        if(tanks[TANK_OUTPUT].fill(result, false) == result.amount)
        {
          tanks[TANK_INPUT].drain(recipe.fluid.amount, true);
          tanks[TANK_OUTPUT].fill(result,true);
          substance.amount -= recipe.substance.amount;
          if(substance.amount <= 0)
          {
            substance = null;
          }
          NBTTagCompound tag = new NBTTagCompound();
          WriteSubstanceToNBT(tag);
          UpdateNBTTag("Substance",tag);

          UpdateTank(TANK_INPUT);
          UpdateTank(TANK_OUTPUT);
        }
      }
    }

    if(last_progress != progress)
    {
      UpdateValue("progress",progress);
    }
  }


  @Override
  public PowerReceiver getPowerReceiver(ForgeDirection side)
  {
    return power_handler.getPowerReceiver();
  }

  @Override
  public void doWork(PowerHandler workProvider)
  {
    
  }

  @Override
  public World getWorld()
  {
    return worldObj;
  }

  public int GetExtractTime()
  {
    return extract_time;
  }
  
  public InfuserSubstance GetSubstance()
  {
    return substance;
  }

  @Override
  public FluidTank GetTank(int slot)
  {
    return tanks[slot];
  }

  @Override
  public int GetTankCount()
  {
    return 2;
  }
}
