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
import exter.foundry.container.ContainerAlloyMixer;
import exter.foundry.network.FoundryPacketHandler;
import exter.foundry.recipes.AlloyRecipe;
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

public class TileEntityAlloyMixer extends TileEntityFoundry implements ISidedInventory,IFluidHandler,IPowerReceptor
{
  static private final int NETDATAID_TANK_INPUT_A_FLUID = 0;
  static private final int NETDATAID_TANK_INPUT_A_AMOUNT = 1;
  static private final int NETDATAID_TANK_INPUT_B_FLUID = 2;
  static private final int NETDATAID_TANK_INPUT_B_AMOUNT = 3;
  static private final int NETDATAID_TANK_OUTPUT_FLUID = 4;
  static private final int NETDATAID_TANK_OUTPUT_AMOUNT = 5;
  
  static private final int PROGRESS_MAX = 400;
  
  private FluidTank tank_input_a;
  private FluidTank tank_input_b;
  private FluidTank tank_output;
  private FluidTankInfo[] tank_info;

  private PowerHandler power_handler;

  
  private int progress;
  
 
  public TileEntityAlloyMixer()
  {
    tank_input_a = new FluidTank(2000);
    tank_input_b = new FluidTank(2000);
    tank_output = new FluidTank(2000);
    
    tank_info = new FluidTankInfo[3];
    tank_info[0] = new FluidTankInfo(tank_input_a);
    tank_info[1] = new FluidTankInfo(tank_input_b);
    tank_info[2] = new FluidTankInfo(tank_output);
    progress = 0;

    power_handler = new PowerHandler(this,PowerHandler.Type.MACHINE);
    power_handler.configure(0, 4, 1, 4);
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

    NBTTagCompound tank_input_a_tag = (NBTTagCompound)compund.getTag("TankInputA");
    NBTTagCompound tank_input_b_tag = (NBTTagCompound)compund.getTag("TankInputB");
    NBTTagCompound tank_output_tag = (NBTTagCompound)compund.getTag("TankOutput");
    tank_input_a.readFromNBT(tank_input_a_tag);
    tank_input_b.readFromNBT(tank_input_b_tag);
    tank_output.readFromNBT(tank_output_tag);
  }
  
  private void WriteTanksToNBT(NBTTagCompound compound)
  {
    NBTTagCompound tank_input_a_tag = new NBTTagCompound();
    NBTTagCompound tank_input_b_tag = new NBTTagCompound();
    NBTTagCompound tank_output_tag = new NBTTagCompound();

    tank_input_a.writeToNBT(tank_input_a_tag);
    tank_input_b.writeToNBT(tank_input_b_tag);
    tank_output.writeToNBT(tank_output_tag);
    compound.setTag("TankInputA", tank_input_a_tag);
    compound.setTag("TankInputB", tank_input_b_tag);
    compound.setTag("TankOutput", tank_output_tag);
  }

  private void WriteProgressToNBT(NBTTagCompound compound)
  {
    compound.setInteger("progress", progress);
  }

  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    WriteTanksToNBT(compound);
    WriteProgressToNBT(compound);
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
      case NETDATAID_TANK_INPUT_A_FLUID:
        SetTankFluid(tank_input_a,value);
        break;
      case NETDATAID_TANK_INPUT_A_AMOUNT:
        SetTankAmount(tank_input_a,value);
        break;
      case NETDATAID_TANK_INPUT_B_FLUID:
        SetTankFluid(tank_input_b,value);
        break;
      case NETDATAID_TANK_INPUT_B_AMOUNT:
        SetTankAmount(tank_input_b,value);
        break;
      case NETDATAID_TANK_OUTPUT_FLUID:
        SetTankFluid(tank_output,value);
        break;
      case NETDATAID_TANK_OUTPUT_AMOUNT:
        SetTankAmount(tank_output,value);
        break;
    }
  }

  private int GetTankFluid(FluidTank tank)
  {
    return tank.getFluid() != null ? tank.getFluid().fluidID : 0;
  }

  private int GetTankAmount(FluidTank tank)
  {
    return tank.getFluid() != null ? tank.getFluid().amount : 0;
  }

  public void SendGUINetworkData(ContainerAlloyMixer container, ICrafting crafting)
  {
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_A_FLUID, GetTankFluid(tank_input_a));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_A_AMOUNT, GetTankAmount(tank_input_a));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_B_FLUID, GetTankFluid(tank_input_b));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_INPUT_B_AMOUNT, GetTankAmount(tank_input_b));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_OUTPUT_FLUID, GetTankFluid(tank_output));
    crafting.sendProgressBarUpdate(container, NETDATAID_TANK_OUTPUT_AMOUNT, GetTankAmount(tank_output));
  }
  
  public FluidTank GetInputATank()
  {
    return tank_input_a;
  }

  public FluidTank GetInputBTank()
  {
    return tank_input_b;
  }
  
  public FluidTank GetOutputTank()
  {
    return tank_output;
  }


  @Override
  public int getSizeInventory()
  {
    return 0;
  }

  @Override
  public ItemStack getStackInSlot(int slot)
  {
    return null;
  }

  @Override
  public ItemStack decrStackSize(int slot, int amount)
  {
    return null;
  }

  @Override
  public ItemStack getStackInSlotOnClosing(int slot)
  {
    return null;
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack stack)
  {
  }

  
  @Override
  public String getInvName()
  {
    return "Alloy Mixer";
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

  private boolean MixAlloy(NBTTagCompound update_packet,AlloyRecipe recipe,FluidTank tank_a,FluidTank tank_b)
  {
    int in_a = recipe.GetInputA().amount;
    int in_b = recipe.GetInputB().amount;
    FluidStack out = recipe.GetOutput();

    boolean update_client = false;

    FluidStack drain_a = tank_a.drain(in_a, false);
    FluidStack drain_b = tank_b.drain(in_b, false);
    if(power_handler.getEnergyStored() > 0 && drain_a != null && drain_a.amount == in_a && drain_b != null && drain_b.amount == in_b && tank_output.fill(out, false) == out.amount)
    {
      int energy = (int) (power_handler.useEnergy(0, 4, true) * 100);
      progress += energy;

      if(progress >= PROGRESS_MAX)
      {
        progress -= PROGRESS_MAX;

        tank_a.drain(in_a, true);
        tank_b.drain(in_b, true);
        tank_output.fill(out, true);
        WriteTanksToNBT(update_packet);
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isInvNameLocalized()
  {
    return false;
  }

  static private final int[] SLOTS = { };

  @Override
  public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
    return false;
  }

  @Override
  public int[] getAccessibleSlotsFromSide(int side)
  {
    return SLOTS;
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
  
  private boolean TankContainsFluid(FluidTank tank,FluidStack fluid)
  {
    FluidStack tf = tank.getFluid();
    return (tf != null && tf.amount > 0 && tf.isFluidEqual(fluid));
  }

  @Override
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
  {
    if(tank_input_a.fill(resource, false) > 0 && !TankContainsFluid(tank_input_b,resource))
    {
      return tank_input_a.fill(resource, doFill);
    } else if(tank_input_b.fill(resource, false) > 0 && !TankContainsFluid(tank_input_a,resource))
    {
      return tank_input_b.fill(resource, doFill);
    } else
    {
      return 0;
    }
  }

  @Override
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    if(resource.isFluidEqual(tank_output.getFluid()))
    {
      return tank_output.drain(resource.amount, doDrain);
    }
    return null;
  }

  @Override
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    return tank_output.drain(maxDrain, doDrain);
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
    boolean update_clients = false;
    NBTTagCompound packet = new NBTTagCompound();
    super.writeToNBT(packet);

    int last_progress = progress;

    if(tank_input_a.getFluidAmount() * tank_input_b.getFluidAmount() > 0)
    {
      AlloyRecipe r = AlloyRecipe.FindRecipe(tank_input_a.getFluid(), tank_input_b.getFluid());
      if(r != null)
      {
        if(MixAlloy(packet, r, tank_input_a, tank_input_b))
        {
          update_clients = true;
        }
      } else
      {
        r = AlloyRecipe.FindRecipe(tank_input_b.getFluid(), tank_input_a.getFluid());
        if(r != null)
        {
          if(MixAlloy(packet, r, tank_input_b, tank_input_a))
          {
            update_clients = true;
          }
        } else
        {
          progress = 0;
        }
      }
    } else
    {
      progress = 0;
    }

    if(progress != last_progress)
    {      
      WriteProgressToNBT(packet);
    }

    if(update_clients)
    {
      update_clients = true;
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
