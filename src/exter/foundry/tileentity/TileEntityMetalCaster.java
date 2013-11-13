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
import exter.foundry.container.ContainerMetalCaster;
import exter.foundry.network.FoundryPacketHandler;
import exter.foundry.recipes.CastingRecipe;
import exter.foundry.recipes.MeltingRecipe;
import exter.foundry.recipes.manager.CastingRecipeManager;
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

public class TileEntityMetalCaster extends TileEntityFoundry implements ISidedInventory,IFluidHandler,IPowerReceptor
{
  static private final int NETDATAID_TANK_FLUID = 1;
  static private final int NETDATAID_TANK_AMOUNT = 2;

  static public final int CAST_TIME = 100;
  
  static public final int POWER_REQUIRED = 100;
  
  static public final int INVENTORY_OUTPUT = 0;
  static public final int INVENTORY_MOLD = 1;
  static public final int INVENTORY_EXTRA = 2;
  static public final int INVENTORY_CONTAINER_DRAIN = 3;
  static public final int INVENTORY_CONTAINER_FILL = 4;
  private ItemStack[] inventory;
  private FluidTank tank;
  private FluidTankInfo[] tank_info;

  private int progress;
  
  
  private PowerHandler power_handler;

  private static final String[] NBT_SLOT_NAMES = 
  {
    "Output",
    "Mold",
    "Extra"
  };
 
  public TileEntityMetalCaster()
  {
    super();

    tank = new FluidTank(5000);
    
    tank_info = new FluidTankInfo[1];
    tank_info[0] = new FluidTankInfo(tank);
    progress = -1;
    inventory = new ItemStack[5];
    
    power_handler = new PowerHandler(this,PowerHandler.Type.MACHINE);
    power_handler.configure(1, 5, 1, 400);
    power_handler.configurePowerPerdition(1, 100);
    
    AddContainerSlot(new ContainerSlot(0,INVENTORY_CONTAINER_DRAIN,false));
    AddContainerSlot(new ContainerSlot(0,INVENTORY_CONTAINER_FILL,true));
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
    if(compund.hasKey("Power"))
    {
      power_handler.readFromNBT(compund.getCompoundTag("Power"));
    }
    
  }


  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    compound.setInteger("progress", progress);
    NBTTagCompound power = new NBTTagCompound();
    power_handler.writeToNBT(power);
  }

  public void GetGUINetworkData(int id, int value)
  {
    switch(id)
    {
      case NETDATAID_TANK_FLUID:
        if(tank.getFluid() == null)
        {
          tank.setFluid(new FluidStack(value, 0));
        } else
        {
          tank.getFluid().fluidID = value;
        }
        break;
      case NETDATAID_TANK_AMOUNT:
        if(tank.getFluid() == null)
        {
          tank.setFluid(new FluidStack(0, value));
        } else
        {
          tank.getFluid().amount = value;
        }
        break;
    }
  }

  public void SendGUINetworkData(ContainerMetalCaster container, ICrafting crafting)
  {
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_FLUID, tank.getFluid() != null ? tank.getFluid().fluidID : 0);
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_AMOUNT, tank.getFluid() != null ? tank.getFluid().amount : 0);
  }

  
  public float GetStoredPower()
  {
    return power_handler.getEnergyStored();
  }

  public float GetMaxStoredPower()
  {
    return power_handler.getMaxEnergyStored();
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
    return "Caster";
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

  static private final int[] INSERT_SLOTS = { 2 };
  static private final int[] EXTRACT_SLOTS = { 0 };

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemstack)
  {
    return slot == INVENTORY_EXTRA;
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
    return slot == INVENTORY_OUTPUT;
  }

  @Override
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
  {
    return tank.fill(resource, doFill);
  }

  @Override
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    return null;
  }

  @Override
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    return null;
  }

  @Override
  public boolean canFill(ForgeDirection from, Fluid fluid)
  {
    return true;
  }

  @Override
  public boolean canDrain(ForgeDirection from, Fluid fluid)
  {
    return false;
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

    float last_power = power_handler.getEnergyStored();
    
    int last_progress = progress;
    if(tank.getFluidAmount() > 0)
    {
      CastingRecipe recipe = CastingRecipeManager.instance.FindRecipe(tank.getFluid(), inventory[1]);
      if(recipe != null)
      {
        ItemStack result = recipe.GetOutput();
        if(result != null)
        {
          ItemStack output = inventory[INVENTORY_OUTPUT];
          if(output == null || output.isItemEqual(recipe.GetOutput()) && output.stackSize < output.getMaxStackSize())
          {
            ItemStack extra = recipe.GetExtra();
            if(extra == null || (inventory[INVENTORY_EXTRA] != null && extra.isItemEqual(inventory[INVENTORY_EXTRA]) && inventory[INVENTORY_EXTRA].stackSize >= extra.stackSize))
            {
             
              if(progress < 0)
              {
                if(last_power >= POWER_REQUIRED)
                {
                  power_handler.useEnergy(POWER_REQUIRED, POWER_REQUIRED, true);
                  progress = 0;
                }
              }
                
              if(progress >= 0)
              {
                if(++progress >= CAST_TIME)
                {
                  progress = -1;
                  tank.drain(recipe.GetFluid().amount, true);
                  if(extra != null)
                  {
                    decrStackSize(INVENTORY_EXTRA, extra.stackSize);
                    UpdateInventoryItem(INVENTORY_EXTRA);
                  }
                  if(output == null)
                  {
                    inventory[INVENTORY_OUTPUT] = result;
                    inventory[INVENTORY_OUTPUT].stackSize = 1;
                  } else
                  {
                    output.stackSize++;
                  }
                  UpdateInventoryItem(INVENTORY_OUTPUT);
                  UpdateTank(0);
                  onInventoryChanged();
                }
              }
            } else
            {
              progress = -1;
            }
          } else
          {
            progress = -1;
          }
        } else
        {
          progress = -1;
        }
      } else
      {
        progress = -1;
      }
    } else
    {
      progress = -1;
    }
    
    if(Math.abs(last_power - power_handler.getEnergyStored()) < 0.01)
    {
      NBTTagCompound tag = new NBTTagCompound();
      power_handler.writeToNBT(tag);
      UpdateNBTTag("Power",tag);
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

  @Override
  public FluidTank GetTank(int slot)
  {
    if(slot != 0)
    {
      return null;
    }
    return tank;
  }

  @Override
  public int GetTankCount()
  {
    return 1;
  }
}
