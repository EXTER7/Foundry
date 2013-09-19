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
import exter.foundry.container.ContainerInductionCrucibleFurnace;
import exter.foundry.network.FoundryPacketHandler;
import exter.foundry.recipes.MeltingRecipe;
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

public class TileEntityInductionCrucibleFurnace extends TileEntityFoundry implements ISidedInventory,IFluidHandler,IPowerReceptor
{
  static private final int NETDATAID_TANK_FLUID = 1;
  static private final int NETDATAID_TANK_AMOUNT = 2;

  
  static public final int HEAT_MAX = 50000;
  static public final int HEAT_MIN = 2900;
  static public final int SMELT_TIME = 4000;
  
  static public final int MAX_ENERGY_USE = 40;
  
  private ItemStack input;
  private FluidTank tank;
  private FluidTankInfo[] tank_info;

  private int progress;
  private int heat;
  private int melt_point;
  
  private PowerHandler power_handler;
 
  
  public TileEntityInductionCrucibleFurnace()
  {
    input = null;
    tank = new FluidTank(5000);
    
    tank_info = new FluidTankInfo[1];
    tank_info[0] = new FluidTankInfo(tank);
    progress = 0;
    heat = HEAT_MIN;
    
    melt_point = 0;
    
    power_handler = new PowerHandler(this,PowerHandler.Type.MACHINE);
    
    power_handler.configure(0, 32, 1, 64);
    power_handler.configurePowerPerdition(0, 0);
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
    
    NBTTagCompound inv_tag = (NBTTagCompound)compund.getTag("Input");
    NBTTagCompound tank_tag = (NBTTagCompound)compund.getTag("Tank");
    if(inv_tag != null)
    {
      input = ItemStack.loadItemStackFromNBT(inv_tag);
    }
    if(tank_tag != null)
    {
      tank.readFromNBT(tank_tag);
    }
  }
  

  private void WriteHeatToNBT(NBTTagCompound compound)
  {
    compound.setInteger("heat", heat);
  }

  private void WriteMeltPointToNBT(NBTTagCompound compound)
  {
    compound.setInteger("melt_point", melt_point);
  }

  private void WriteProgressToNBT(NBTTagCompound compound)
  {
    compound.setInteger("progress", progress);
  }
  
  private void WriteInputToNBT(NBTTagCompound compound)
  {
    if(input != null)
    {
      NBTTagCompound inv_tag = new NBTTagCompound();
      input.writeToNBT(inv_tag);
      compound.setTag("Input", inv_tag);
    }
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
    WriteHeatToNBT(compound);
    WriteProgressToNBT(compound);
    WriteInputToNBT(compound);
    WriteTankToNBT(compound);    
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

  public void SendGUINetworkData(ContainerInductionCrucibleFurnace container, ICrafting crafting)
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
    return 1;
  }

  @Override
  public ItemStack getStackInSlot(int slot)
  {
    return slot == 0?input:null;
  }

  @Override
  public ItemStack decrStackSize(int slot, int amount)
  {
    if(slot != 0)
    {
      return null;
    }
    if(input != null)
    {
      ItemStack is;

      if(input.stackSize <= amount)
      {
        is = input;
        input = null;
        onInventoryChanged();
        return is;
      } else
      {
        is = input.splitStack(amount);

        if(input.stackSize == 0)
        {
          input = null;
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
    if(slot != 0)
    {
      return null;
    }
    if(input != null)
    {
      ItemStack is = input;
      input = null;
      return is;
    } else
    {
      return null;
    }
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack stack)
  {
    if(slot != 0)
    {
      return;
    }
    input = stack;

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
    return "Metal Smelter";
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
  
  public int GetHeat()
  {
    return heat;
  }

  public int GetSmeltingSpeed()
  {
    return (heat - melt_point) * 400 / HEAT_MAX;
  }
  
  public int GetProgress()
  {
    return progress;
  }
  
  public int GetMeltingPoint()
  {
    return melt_point;
  }
  
  @Override
  public boolean isInvNameLocalized()
  {
    return false;
  }

  static private final int[] INSERT_SLOTS = { 0 };

  @Override
  public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
    return i == 0;
  }

  @Override
  public int[] getAccessibleSlotsFromSide(int side)
  {
    return INSERT_SLOTS;
  }

  @Override
  public boolean canInsertItem(int i, ItemStack itemstack, int j)
  {
    return isItemValidForSlot(i, itemstack);
  }

  @Override
  public boolean canExtractItem(int i, ItemStack itemstack, int j)
  {
    return false;
  }

  @Override
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
  {
    return 0;
  }

  @Override
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    if(resource.isFluidEqual(tank.getFluid()))
    {
      return tank.drain(resource.amount, doDrain);
    }
    return null;
  }

  @Override
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    return tank.drain(maxDrain, doDrain);
  }

  @Override
  public boolean canFill(ForgeDirection from, Fluid fluid)
  {
    return false;
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
  protected void UpdateEntityClient()
  {

  }

  @Override
  protected void UpdateEntityServer()
  {
    boolean update_clients = false;
    NBTTagCompound packet = new NBTTagCompound();
    super.writeToNBT(packet);
    
    int last_progress = progress;
    int last_melt_point = melt_point;
    if(input != null)
    {      
      MeltingRecipe metal = MeltingRecipe.FindByStack(input);
      if(metal != null)
      {
        FluidStack fs = metal.GetFluid();
        
        melt_point = fs.getFluid().getTemperature() * 10;
        
        if(heat > melt_point && tank.fill(fs, false) == fs.amount)
        {
          progress += GetSmeltingSpeed();
          if(progress >= SMELT_TIME)
          {
            update_clients = true;
            progress -= SMELT_TIME;
            tank.fill(fs, true);
            decrStackSize(0,1);
            WriteInputToNBT(packet);
            WriteTankToNBT(packet);
          }
        } else
        {
          progress = 0;
        }
      } else
      {
        progress = 0;
        melt_point = 0;
      }
    } else
    {
      progress = 0;
      melt_point = 0;
    }
    
    if(last_progress != progress)
    {
      update_clients = true;
      WriteProgressToNBT(packet);
    }

    if(last_melt_point != melt_point)
    {
      update_clients = true;
      WriteMeltPointToNBT(packet);
    }

    int last_heat = heat;
    if(heat > 0)
    {
      heat -= heat / (HEAT_MAX / 10 ) + 1;
      if(heat < 0)
      {
        heat = 0;
      }
    }

    int energy_need = HEAT_MAX - heat;
    if(energy_need > MAX_ENERGY_USE)
    {
      energy_need = MAX_ENERGY_USE;
    }
    
    if(power_handler.getMaxEnergyStored() > 0)
    {
      int energy = (int)power_handler.useEnergy(1, energy_need, true);
      heat += energy;
      if(heat > HEAT_MAX)
      {
        heat = HEAT_MAX;
      }
    }
    if(last_heat != heat)
    {
      update_clients = true;
      WriteHeatToNBT(packet);
    }
    if(update_clients)
    {
      SendUpdatePacket(packet);
    }
  }

}
