package exter.foundry.tileentity;

import java.util.ArrayList;
import java.util.List;

import exter.foundry.ModFoundry;
import exter.foundry.network.MessageTileEntitySync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

/**
 * Base class for all machines.
 */
public abstract class TileEntityFoundry extends TileEntity implements IUpdatePlayerListBox,IInventory
{
  public enum RedstoneMode
  {
    RSMODE_IGNORE(0),
    RSMODE_ON(1),
    RSMODE_OFF(2),
    RSMODE_PULSE(3);
    
    public final int id;
    
    private RedstoneMode(int num)
    {
      id = num;
    }
    
    static public RedstoneMode fromID(int num)
    {
      for(RedstoneMode m:RedstoneMode.values())
      {
        if(m.id == num)
        {
          return m;
        }
      }
      return RSMODE_IGNORE;
    }
  }

  private RedstoneMode mode;
  
  /**
   * Links an item slot to a tank for filling/draining containers.
   */
  public class ContainerSlot
  {
    public final boolean fill;
    public final int tank_slot;
    public final int slot;
    
    public final Fluid fluid;
    
    public ContainerSlot(int container_tank,int container_slot,boolean container_fill)
    {
      this(container_tank,container_slot,container_fill,null);
    }

    public ContainerSlot(int container_tank,int container_slot,boolean container_fill,Fluid container_fluid)
    {
      tank_slot = container_tank;
      slot = container_slot;
      fill = container_fill;
      fluid = container_fluid;
    }
    
    public void update()
    {
      ItemStack stack = getStackInSlot(slot);
      if(stack == null || !(stack.getItem() instanceof IFluidContainerItem))
      {
        return;
      }
      IFluidContainerItem fluid_cont = (IFluidContainerItem)stack.getItem();
      
      FluidTank tank = getTank(tank_slot);
      if(fill)
      {
        FluidStack drained = tank.drain(25, false);
        if(drained == null || drained.amount == 0)
        {
          return;
        }
        int filled = fluid_cont.fill(stack, drained, false);
        if(filled == 0)
        {
          return;
        }
        drained = tank.drain(filled, true);
        fluid_cont.fill(stack, drained, true);
        updateTank(tank_slot);
        updateInventoryItem(slot);
      } else
      {
        FluidStack drained = fluid_cont.drain(stack, 25, false);
        if(drained == null || drained.amount == 0 || (fluid != null && drained.getFluid() != fluid))
        {
          return;
        }
        
        int filled = tank.fill(drained, false);
        if(filled == 0)
        {
          return;
        }
        drained = fluid_cont.drain(stack, filled, true);
        tank.fill(drained, true);
        updateTank(tank_slot);
        updateInventoryItem(slot);
      }
    }
  }
  
  private List<ContainerSlot> conatiner_slots;
  private NBTTagCompound packet;
  private boolean do_update;
  private boolean initialized;
  
  protected boolean last_redstone_signal;
  protected boolean redstone_signal;

  protected final void AddContainerSlot(ContainerSlot cs)
  {
    conatiner_slots.add(cs);
  }

  protected abstract void updateClient();

  protected abstract void updateServer();

  public abstract FluidTank getTank(int slot);
  
  public abstract int getTankCount();
  
  protected abstract void onInitialize();

  public TileEntityFoundry()
  {
    conatiner_slots = new ArrayList<ContainerSlot>();
    last_redstone_signal = false;
    redstone_signal = false;
    initialized = false;
    mode = RedstoneMode.RSMODE_IGNORE;
  }
  
  @Override
  public void invalidate()
  {
    super.invalidate();
    initialized = false;
    onChunkUnload();
  }


  @Override
  public final Packet getDescriptionPacket()
  {
    NBTTagCompound nbt = new NBTTagCompound();
    writeToNBT(nbt);    
    return new S35PacketUpdateTileEntity(getPos(), 0, nbt);
  }
  
  
  protected final void updateTank(int slot)
  {
    if(packet == null)
    {
      return;
    }
    writeTankToNBT(packet,slot);
    do_update = true;
  }
  
  protected final void updateInventoryItem(int slot)
  {
    if(packet == null)
    {
      return;
    }
    writeInventoryItemToNBT(packet,slot);
    do_update = true;
  }

  protected final void writeTankToNBT(NBTTagCompound compound,int slot)
  {
    NBTTagCompound tag = new NBTTagCompound();
    getTank(slot).writeToNBT(tag);
    compound.setTag("Tank_" + String.valueOf(slot), tag);
  }

  protected final void writeInventoryItemToNBT(NBTTagCompound compound,int slot)
  {
    ItemStack is = getStackInSlot(slot);
    NBTTagCompound tag = new NBTTagCompound();
    if(is != null)
    {
      tag.setBoolean("empty", false);
      is.writeToNBT(tag);
    } else
    {
      tag.setBoolean("empty", true);
    }
    compound.setTag("Item_" + String.valueOf(slot), tag);
  }


  @Override
  public void readFromNBT(NBTTagCompound compound)
  {
    super.readFromNBT(compound);

    int i;
    for(i = 0; i < getTankCount(); i++)
    {
      NBTTagCompound tag = (NBTTagCompound)compound.getTag("Tank_" + String.valueOf(i));
      if(tag != null)
      {
        FluidTank tank = getTank(i);
        tank.setFluid(null);
        tank.readFromNBT(tag);
      }
    }
    
    for(i = 0; i < getSizeInventory(); i++)
    {
      NBTTagCompound tag = (NBTTagCompound)compound.getTag("Item_" + String.valueOf(i));
      if(tag != null)
      {
        ItemStack stack = null;
        if(!tag.getBoolean("empty"))
        {
          stack = ItemStack.loadItemStackFromNBT(tag);
        }
        setInventorySlotContents(i, stack);
      }
    }
    if(compound.hasKey("rsmode"))
    {
      mode = RedstoneMode.fromID(compound.getInteger("rsmode"));
    }
  }
  
  protected void writeTileToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
  }

  
  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    int i;
    super.writeToNBT(compound);
    for(i = 0; i < getTankCount(); i++)
    {
      writeTankToNBT(compound,i);
    }
    for(i = 0; i < getSizeInventory(); i++)
    {
      writeInventoryItemToNBT(compound,i);
    }
    compound.setInteger("rsmode", mode.id);
  }

  protected final void updateValue(String name,int value)
  {
    if(packet == null)
    {
      return;
    }
    packet.setInteger(name, value);

    do_update = true;
  }

  protected final void updateNBTTag(String name,NBTTagCompound compound)
  {
    if(packet == null)
    {
      return;
    }
    packet.setTag(name, compound);
    do_update = true;
  }

  protected void sendPacketToPlayers(NBTTagCompound data)
  {
    data.setInteger("dim", worldObj.provider.getDimensionId());
    ModFoundry.network_channel.sendToAllAround(new MessageTileEntitySync(data),
        new TargetPoint(worldObj.provider.getDimensionId(),pos.getX(),pos.getY(),pos.getZ(),192));
  }
   

  protected final int getTankFluid(FluidTank tank)
  {
    FluidStack f = tank.getFluid();
    return f != null ? f.getFluidID() : 0;
  }

  protected final int getTankAmount(FluidTank tank)
  {
    FluidStack f = tank.getFluid();
    return f != null ? f.amount : 0;
  }
  
  protected final void setTankFluid(FluidTank tank,int value)
  {
    Fluid f = FluidRegistry.getFluid(value);
    if(f == null)
    {
      tank.setFluid(null);
      return;
    }
    if(tank.getFluid() == null)
    {
      tank.setFluid(new FluidStack(f, 0));
    } else
    {
      tank.setFluid(new FluidStack(f, tank.getFluidAmount()));
    }
  }

  protected final void setTankAmount(FluidTank tank,int value)
  {
    if(value == 0)
    {
      tank.setFluid(null);
      return;
    }
    if(tank.getFluid() == null)
    {
      tank.setFluid(new FluidStack(FluidRegistry.getFluid(value), value));
    } else
    {
      tank.getFluid().amount = value;
    }
  }


  @Override
  public void update()
  {
    if(!(initialized || isInvalid()))
    {
      updateRedstone();
      onInitialize();
      initialized = true;
    }
    
    if(!worldObj.isRemote)
    {

      packet = new NBTTagCompound();
      super.writeToNBT(packet);
      do_update = false;
      for(ContainerSlot cs:conatiner_slots)
      {
        cs.update();
      }
      updateServer();
      
      if(do_update)
      {
        sendPacketToPlayers(packet);
      }
      packet = null;
    } else
    {
      updateClient();
    }
    last_redstone_signal = redstone_signal;
  }

  @Override
  public final void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
  {
    super.onDataPacket(net, pkt);
    if(worldObj.isRemote)
    {
      readFromNBT(pkt.getNbtCompound());
    }
    //worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
  }
    
  public void updateRedstone()
  {
    redstone_signal = worldObj.isBlockIndirectlyGettingPowered(getPos()) > 0;
  }
    
  @Override
  public boolean isUseableByPlayer(EntityPlayer player)
  {
    return worldObj.getTileEntity(getPos()) != this ? false : player.getDistanceSq(getPos()) <= 64.0D;
  }

  @Override
  public int getField(int id)
  {
    return 0;
  }

  @Override
  public void setField(int id, int value)
  {

  }

  @Override
  public int getFieldCount()
  {
    return 0;
  }

  @Override
  public void clear()
  {

  }

  @Override
  public String getCommandSenderName()
  {
    return null;
  }

  @Override
  public boolean hasCustomName()
  {
    return false;
  }

  @Override
  public IChatComponent getDisplayName()
  {
    return null;
  }

  public RedstoneMode getRedstoneMode()
  {
    return mode;
  }
  
  public void setRedstoneMode(RedstoneMode new_mode)
  {
    if(mode != new_mode)
    {
      mode = new_mode;
      if(worldObj.isRemote)
      {
        NBTTagCompound tag = new NBTTagCompound();
        super.writeToNBT(tag);
        tag.setInteger("rsmode", mode.id);
        tag.setInteger("dim", worldObj.provider.getDimensionId());
        ModFoundry.network_channel.sendToServer(new MessageTileEntitySync(tag));
      }
    }
  }

  @Override
  public void openInventory(EntityPlayer player)
  {
    if(!worldObj.isRemote)
    {
      NBTTagCompound tag = new NBTTagCompound();
      super.writeToNBT(tag);
      tag.setInteger("rsmode", mode.id);
      sendPacketToPlayers(tag);
    }
  }

  @Override
  public void closeInventory(EntityPlayer player)
  {
    if(!worldObj.isRemote)
    {
      NBTTagCompound tag = new NBTTagCompound();
      super.writeToNBT(tag);
      tag.setInteger("rsmode", mode.id);
      sendPacketToPlayers(tag);
    }
  }
}
