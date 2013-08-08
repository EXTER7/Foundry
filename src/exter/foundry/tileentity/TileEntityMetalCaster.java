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
import exter.foundry.recipes.SmeltingRecipe;
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

  static public final int CAST_TIME = 4000;
  
  private ItemStack[] inventory;
  private FluidTank tank;
  private FluidTankInfo[] tank_info;

  private int progress;
  
  public final int SLOT_OUTPUT = 0;
  public final int SLOT_MOLD = 1;
  public final int SLOT_EXTRA = 2;
  
  private PowerHandler power_handler;

  private static final String[] NBT_SLOT_NAMES = 
  {
    "Output",
    "Mold",
    "Extra"
  };
 
  public TileEntityMetalCaster()
  {
    tank = new FluidTank(5000);
    
    tank_info = new FluidTankInfo[1];
    tank_info[0] = new FluidTankInfo(tank);
    progress = 0;
    inventory = new ItemStack[3];
    
    power_handler = new PowerHandler(this,PowerHandler.Type.MACHINE);
    power_handler.configure(0, 2, 1, 2);
    power_handler.configurePowerPerdition(0, 0);
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
    
    NBTTagCompound tank_tag = (NBTTagCompound)compund.getTag("Tank");
    
    for(i = 0; i < getSizeInventory(); i++)
    {
      NBTTagCompound item_tag = (NBTTagCompound)compund.getTag(NBT_SLOT_NAMES[i]);
      if(item_tag != null)
      {
        inventory[i] = ItemStack.loadItemStackFromNBT(item_tag);
      }
    }
    if(tank_tag != null)
    {
      tank.readFromNBT(tank_tag);
    }
  }

  private void WriteInventoryItemToNBT(NBTTagCompound compound,int slot)
  {
    NBTTagCompound item_tag = new NBTTagCompound();

    if(inventory[slot] != null)
    {
      inventory[slot].writeToNBT(item_tag);
    }
    compound.setTag(NBT_SLOT_NAMES[slot], item_tag);
  }
  
  private void WriteInventoryToNBT(NBTTagCompound compound)
  {
    int i;
    for(i = 0; i < getSizeInventory(); i++)
    {
      WriteInventoryItemToNBT(compound,i);
    }
  }
  
  private void WriteProgressToNBT(NBTTagCompound compound)
  {
    compound.setInteger("progress", progress);
  }

  private void WriteTankToNBT(NBTTagCompound compound)
  {
    NBTTagCompound tank_tag = new NBTTagCompound();
    tank.writeToNBT(tank_tag);
    compound.setTag("Tank", tank_tag);
  }

  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    this.WriteInventoryToNBT(compound);
    this.WriteTankToNBT(compound);
    this.WriteProgressToNBT(compound);
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

  public FluidTank GetTank()
  {
    return tank;
  }
  
  @Override
  public int getSizeInventory()
  {
    return 3;
  }

  @Override
  public ItemStack getStackInSlot(int slot)
  {
    if(slot < 0 || slot >= 3)
    {
      return null;
    }
    return inventory[slot];
  }

  @Override
  public ItemStack decrStackSize(int slot, int amount)
  {
    if(slot < 0 || slot >= 3)
    {
      return null;
    }
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
    if(slot < 0 || slot >= 3)
    {
      return null;
    }
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
    if(slot < 0 || slot >= 3)
    {
      return;
    }
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
    return slot == SLOT_EXTRA;
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
    return slot == SLOT_OUTPUT;
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
    boolean update_clients = false;
    NBTTagCompound packet = new NBTTagCompound();
    super.writeToNBT(packet);

    power_handler.getPowerReceiver().update();

    int last_progress = progress;
    if(tank.getFluidAmount() > 0)
    {
      CastingRecipe recipe = CastingRecipe.FindRecipe(tank.getFluid(), inventory[1]);
      if(recipe != null)
      {
        ItemStack result = recipe.GetOutput();
        if(result != null)
        {
          ItemStack output = inventory[SLOT_OUTPUT];
          if(output == null || output.isItemEqual(recipe.GetOutput()) && output.stackSize < output.getMaxStackSize())
          {
            ItemStack extra = recipe.GetExtra();
            if(extra == null || (inventory[SLOT_EXTRA] != null && extra.isItemEqual(inventory[SLOT_EXTRA]) && inventory[SLOT_EXTRA].stackSize >= extra.stackSize))
            {
              int energy = (int) (power_handler.useEnergy(0, 2, true) * 100);
              progress += energy;

              if(progress >= CAST_TIME)
              {
                progress -= CAST_TIME;
                tank.drain(recipe.GetFluid().amount, true);
                if(extra != null)
                {
                  decrStackSize(SLOT_EXTRA, extra.stackSize);
                  WriteInventoryItemToNBT(packet, SLOT_EXTRA);
                }
                if(output == null)
                {
                  inventory[SLOT_OUTPUT] = result;
                  inventory[SLOT_OUTPUT].stackSize = 1;
                } else
                {
                  output.stackSize++;
                }
                WriteInventoryItemToNBT(packet, SLOT_OUTPUT);
                WriteTankToNBT(packet);
                update_clients = true;
                onInventoryChanged();
              }
            } else
            {
              progress = 0;
            }
          } else
          {
            progress = 0;
          }
        } else
        {
          progress = 0;
        }
      } else
      {
        progress = 0;
      }
    } else
    {
      progress = 0;
    }
    if(last_progress != progress)
    {
      update_clients = true;
      WriteProgressToNBT(packet);
    }
    
    if(update_clients)
    {
      SendUpdatePacket(packet);
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


}
