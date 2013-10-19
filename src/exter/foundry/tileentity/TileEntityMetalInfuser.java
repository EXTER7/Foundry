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
import exter.foundry.container.ContainerMetalInfuser;
import exter.foundry.network.FoundryPacketHandler;
import exter.foundry.recipes.InfuserRecipe;
import exter.foundry.recipes.InfuserSubstance;
import exter.foundry.recipes.InfuserSubstanceRecipe;
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

public class TileEntityMetalInfuser extends TileEntityFoundry implements ISidedInventory,IFluidHandler,IPowerReceptor
{
  static private final int NETDATAID_INPUT_TANK_FLUID = 1;
  static private final int NETDATAID_INPUT_TANK_AMOUNT = 2;

  static private final int NETDATAID_OUTPUT_TANK_FLUID = 3;
  static private final int NETDATAID_OUTPUT_TANK_AMOUNT = 4;
  
  
  private ItemStack substance_input;
  private FluidTank input_tank;
  private FluidTank output_tank;
  private FluidTankInfo[] tank_info;

  private InfuserSubstance substance;
  
  private int progress;
  private int extract_time;
  
  
  private PowerHandler power_handler;
 
  public TileEntityMetalInfuser()
  {
    input_tank = new FluidTank(5000);
    output_tank = new FluidTank(5000);

    tank_info = new FluidTankInfo[2];
    tank_info[0] = new FluidTankInfo(input_tank);
    tank_info[1] = new FluidTankInfo(output_tank);
    progress = 0;
    extract_time = 1;
    
    power_handler = new PowerHandler(this,PowerHandler.Type.MACHINE);
    power_handler.configure(1, 8, 1, 8);
    power_handler.configurePowerPerdition(1, 100);
    
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
    
    NBTTagCompound input_tank_tag = (NBTTagCompound)compund.getTag("InputTank");
    NBTTagCompound output_tank_tag = (NBTTagCompound)compund.getTag("OuputTank");
    NBTTagCompound substance_tag = (NBTTagCompound)compund.getTag("Substance");
    
    NBTTagCompound item_tag = (NBTTagCompound)compund.getTag("Item");
    if(item_tag != null)
    {
      substance_input = ItemStack.loadItemStackFromNBT(item_tag);
    }
    if(input_tank_tag != null)
    {
      input_tank.readFromNBT(input_tank_tag);
    }
    if(output_tank_tag != null)
    {
      output_tank.readFromNBT(output_tank_tag);
    }
    if(substance_tag != null)
    {
      substance = InfuserSubstance.ReadFromNBT(substance_tag);
    }
  }

  private void WriteSubstanceItemToNBT(NBTTagCompound compound)
  {
    NBTTagCompound item_tag = new NBTTagCompound();

    if(substance_input != null)
    {
      substance_input.writeToNBT(item_tag);
    }
    compound.setTag("Item", item_tag);
  }

  private void WriteSubstanceToNBT(NBTTagCompound compound)
  {
    NBTTagCompound item_tag = new NBTTagCompound();

    if(substance != null)
    {
      substance.WriteToNBT(item_tag);
    }
    compound.setTag("Substance", item_tag);
  }

  private void WriteProgressToNBT(NBTTagCompound compound)
  {
    compound.setInteger("progress", progress);
  }

  private void WriteExtractTimeToNBT(NBTTagCompound compound)
  {
    compound.setInteger("extract_time", extract_time);
  }

  private void WriteInputTankToNBT(NBTTagCompound compound)
  {
    NBTTagCompound tank_tag = new NBTTagCompound();
    input_tank.writeToNBT(tank_tag);
    compound.setTag("InputTank", tank_tag);
  }

  private void WriteOutputTankToNBT(NBTTagCompound compound)
  {
    NBTTagCompound tank_tag = new NBTTagCompound();
    output_tank.writeToNBT(tank_tag);
    compound.setTag("OutputTank", tank_tag);
  }

  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    this.WriteSubstanceItemToNBT(compound);
    this.WriteSubstanceToNBT(compound);
    this.WriteInputTankToNBT(compound);
    this.WriteOutputTankToNBT(compound);
    this.WriteProgressToNBT(compound);
    this.WriteExtractTimeToNBT(compound);
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
        SetTankFluid(input_tank,value);
        break;
      case NETDATAID_INPUT_TANK_AMOUNT:
        SetTankAmount(input_tank,value);
        break;
      case NETDATAID_OUTPUT_TANK_FLUID:
        SetTankFluid(output_tank,value);
        break;
      case NETDATAID_OUTPUT_TANK_AMOUNT:
        SetTankAmount(output_tank,value);
        break;
    }
  }

  public void SendGUINetworkData(ContainerMetalInfuser container, ICrafting crafting)
  {
    crafting.sendProgressBarUpdate(container, NETDATAID_INPUT_TANK_FLUID, input_tank.getFluid() != null ? input_tank.getFluid().fluidID : 0);
    crafting.sendProgressBarUpdate(container, NETDATAID_INPUT_TANK_AMOUNT, input_tank.getFluid() != null ? input_tank.getFluid().amount : 0);
    crafting.sendProgressBarUpdate(container, NETDATAID_OUTPUT_TANK_FLUID, output_tank.getFluid() != null ? output_tank.getFluid().fluidID : 0);
    crafting.sendProgressBarUpdate(container, NETDATAID_OUTPUT_TANK_AMOUNT, output_tank.getFluid() != null ? output_tank.getFluid().amount : 0);
  }

  public FluidTank GetInputTank()
  {
    return input_tank;
  }

  public FluidTank GetOutputTank()
  {
    return output_tank;
  }
  
  @Override
  public int getSizeInventory()
  {
    return 3;
  }

  @Override
  public ItemStack getStackInSlot(int slot)
  {
    if(slot != 0)
    {
      return null;
    }
    return substance_input;
  }

  @Override
  public ItemStack decrStackSize(int slot, int amount)
  {
    if(slot != 0)
    {
      return null;
    }
    if(substance_input != null)
    {
      ItemStack is;

      if(substance_input.stackSize <= amount)
      {
        is = substance_input;
        substance_input = null;
        onInventoryChanged();
        return is;
      } else
      {
        is = substance_input.splitStack(amount);

        if(substance_input.stackSize == 0)
        {
          substance_input = null;
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
    if(substance_input != null)
    {
      ItemStack is = substance_input;
      substance_input = null;
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
    substance_input = stack;

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
    return input_tank.fill(resource, doFill);
  }

  @Override
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    if(resource.isFluidEqual(output_tank.getFluid()))
    {
      return output_tank.drain(resource.amount, doDrain);
    }
    return null;
  }

  @Override
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    return output_tank.drain(maxDrain, doDrain);
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
    int last_extract_time = extract_time;
    InfuserSubstanceRecipe sub_recipe = InfuserSubstanceRecipe.FindRecipe(substance_input);
    if(sub_recipe != null)
    {
      if(substance == null || sub_recipe.substance.IsSubstanceEqual(substance) && InfuserSubstance.MAX_AMOUNT - sub_recipe.substance.amount >= substance.amount)
      {
        extract_time = sub_recipe.extract_time * 100;
        if(power_handler.getEnergyStored() > 0)
        {
          int energy = (int) (power_handler.useEnergy(0, 8, true) * 100 / 8);
          progress += energy;
          if(progress >= extract_time)
          {
            update_clients = true;
            progress -= extract_time;
            if(substance == null)
            {
              substance = new InfuserSubstance(sub_recipe.substance);
            } else
            {
              substance.amount += sub_recipe.substance.amount;
            }
            decrStackSize(0, 1);
            WriteSubstanceItemToNBT(packet);
            WriteSubstanceToNBT(packet);
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
    
    if(last_progress != progress)
    {
      update_clients = true;
      WriteProgressToNBT(packet);
    }

    if(last_extract_time != extract_time)
    {
      update_clients = true;
      WriteExtractTimeToNBT(packet);
    }

    if(input_tank.getFluidAmount() > 0)
    {
      InfuserRecipe recipe = InfuserRecipe.FindRecipe(input_tank.getFluid(), substance);
      if(recipe != null)
      {
        FluidStack result = recipe.GetOutput();
        if(output_tank.fill(result, false) == result.amount)
        {
          input_tank.drain(recipe.GetFluid().amount, true);
          output_tank.fill(result,true);
          substance.amount -= recipe.GetSubstance().amount;
          if(substance.amount <= 0)
          {
            substance = null;
          }
          WriteSubstanceToNBT(packet);
          WriteInputTankToNBT(packet);
          WriteOutputTankToNBT(packet);
          update_clients = true;
        }
      }
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

  public int GetExtractTime()
  {
    return extract_time;
  }
  
  public InfuserSubstance GetSubstance()
  {
    return substance;
  }
}
