package exter.foundry.tileentity;

import java.util.ArrayList;
import java.util.List;

import exter.foundry.ModFoundry;
import exter.foundry.network.MessageTileEntitySync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

/**
 * Base class for all machines.
 */
public abstract class TileEntityFoundry extends TileEntity implements ITickable,IInventory
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
  
  protected class FluidHandler implements IFluidHandler
  {
    private int fill_tank;
    private int drain_tank;
    private IFluidTankProperties[] props;
    
    public FluidHandler(int fill_tank,int drain_tank)
    {
      this.fill_tank = fill_tank;
      this.drain_tank = drain_tank;
      props = new IFluidTankProperties[getTankCount()];
      for(int i = 0; i < props.length; i++)
      {
        props[i] = new FluidTankPropertiesWrapper(getTank(i));
      }
    }

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
      return props;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
      if(fill_tank < 0)
      {
        return 0;
      }
      return fillTank(fill_tank,resource,doFill);
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
      if(drain_tank < 0)
      {
        return null;
      }
      return drainTank(drain_tank,resource,doDrain);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
      if(drain_tank < 0)
      {
        return null;
      }
      return drainTank(drain_tank,maxDrain,doDrain);
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
      if(stack == null || stack.stackSize > 1)
      {
        return;
      }
      
      FluidTank tank = getTank(tank_slot);
      if(fill)
      {
        if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null))
        {
          IFluidHandler handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
          FluidStack drained = handler.drain(25, false);
          if(drained == null || drained.amount == 0 || (fluid != null && drained.getFluid() != fluid))
          {
            return;
          }

          int filled = tank.fill(drained, false);
          if(filled == 0)
          {
            return;
          }
          drained = handler.drain(filled, true);
          tank.fill(drained, true);
          updateTank(tank_slot);
          updateInventoryItem(slot);
        }
      } else
      {
        if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null))
        {
          IFluidHandler handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
          FluidStack drained = handler.drain( 25, false);
          if(drained == null || drained.amount == 0 || (fluid != null && drained.getFluid() != fluid))
          {
            return;
          }

          int filled = tank.fill(drained, false);
          if(filled == 0)
          {
            return;
          }
          drained = handler.drain(filled, true);
          tank.fill(drained, true);
          updateTank(tank_slot);
          updateInventoryItem(slot);
        }
      }
    }
  }
  
  private List<ContainerSlot> conatiner_slots;
  private NBTTagCompound update_packet;
  private boolean initialized;
  
  protected boolean last_redstone_signal;
  protected boolean redstone_signal;
  protected final ItemStack[] inventory;

  protected final void addContainerSlot(ContainerSlot cs)
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
    inventory = new ItemStack[getSizeInventory()];
  }
  

  @Override
  public final ItemStack getStackInSlot(int slot)
  {
    return inventory[slot];
  }

  @Override
  public final ItemStack decrStackSize(int slot, int amount)
  {
    if(inventory[slot] != null)
    {
      ItemStack is;

      if(inventory[slot].stackSize <= amount)
      {
        is = inventory[slot];
        inventory[slot] = null;
        updateInventoryItem(slot);
        markDirty();
        return is;
      } else
      {
        is = inventory[slot].splitStack(amount);

        if(inventory[slot].stackSize == 0)
        {
          inventory[slot] = null;
        }
        updateInventoryItem(slot);
        markDirty();
        return is;
      }
    } else
    {
      return null;
    }
  }

  @Override
  public ItemStack removeStackFromSlot(int slot)
  {
    if(inventory[slot] != null)
    {
      ItemStack is = inventory[slot];
      inventory[slot] = null;
      updateInventoryItem(slot);
      markDirty();
      return is;
    } else
    {
      return null;
    }
  }

  @Override
  public final void setInventorySlotContents(int slot, ItemStack stack)
  {
    inventory[slot] = stack;

    if(stack != null && stack.stackSize > this.getInventoryStackLimit())
    {
      stack.stackSize = this.getInventoryStackLimit();
    }
    updateInventoryItem(slot);
    markDirty();
  }
  
  @Override
  public int getInventoryStackLimit()
  {
    return 64;
  }

  @Override
  public void invalidate()
  {
    super.invalidate();
    initialized = false;
    onChunkUnload();
  }


  @Override
  public final SPacketUpdateTileEntity getUpdatePacket()
  {
    NBTTagCompound nbt = new NBTTagCompound();
    writeToNBT(nbt);    
    return new SPacketUpdateTileEntity(getPos(), 0, nbt);
  }
  
  @Override
  public NBTTagCompound getUpdateTag()
  {
    return writeToNBT(null);
  }
  
  protected final void updateTank(int slot)
  {
    if(worldObj.isRemote)
    {
      return;
    }
    if(update_packet == null)
    {
      update_packet = new NBTTagCompound();
      super.writeToNBT(update_packet);
    }
    writeTankToNBT(update_packet,slot);
  }
  
  protected final void updateInventoryItem(int slot)
  {
    if(worldObj.isRemote)
    {
      return;
    }
    if(update_packet == null)
    {
      update_packet = new NBTTagCompound();
      super.writeToNBT(update_packet);
    }
    writeInventoryItemToNBT(update_packet,slot);
  }

  protected final void writeTankToNBT(NBTTagCompound compound,int slot)
  {
    NBTTagCompound tag = new NBTTagCompound();
    getTank(slot).writeToNBT(tag);
    compound.setTag("Tank_" + String.valueOf(slot), tag);
  }

  protected final void writeInventoryItemToNBT(NBTTagCompound compound,int slot)
  {
    ItemStack is = inventory[slot];
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
        inventory[i] = stack;
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
  public NBTTagCompound writeToNBT(NBTTagCompound compound)
  {
    if(compound == null)
    {
      compound = new NBTTagCompound();
    }
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
    return compound;
  }

  protected final void updateValue(String name,int value)
  {
    if(worldObj.isRemote)
    {
      return;
    }
    if(update_packet == null)
    {
      update_packet = new NBTTagCompound();
      super.writeToNBT(update_packet);
    }
    update_packet.setInteger(name, value);
  }

  protected final void updateValue(String name,boolean value)
  {
    if(worldObj.isRemote)
    {
      return;
    }
    if(update_packet == null)
    {
      update_packet = new NBTTagCompound();
      super.writeToNBT(update_packet);
    }
    update_packet.setBoolean(name, value);
  }

  protected final void updateNBTTag(String name,NBTTagCompound compound)
  {
    if(worldObj.isRemote)
    {
      return;
    }
    if(update_packet == null)
    {
      update_packet = new NBTTagCompound();
      super.writeToNBT(update_packet);
    }
    update_packet.setTag(name, compound);
  }

  protected void sendPacketToNearbyPlayers(NBTTagCompound data)
  {
    data.setInteger("dim", worldObj.provider.getDimension());
    ModFoundry.network_channel.sendToAllAround(new MessageTileEntitySync(data),
        new TargetPoint(worldObj.provider.getDimension(),pos.getX(),pos.getY(),pos.getZ(),192));
  }
   
  protected void sendPacketToPlayer(NBTTagCompound data,EntityPlayerMP player)
  {
    data.setInteger("dim", worldObj.provider.getDimension());
    ModFoundry.network_channel.sendTo(new MessageTileEntitySync(data),player);
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
      if(update_packet == null)
      {
        update_packet = new NBTTagCompound();
        super.writeToNBT(update_packet);
      }
      for(ContainerSlot cs:conatiner_slots)
      {
        cs.update();
      }
      updateServer();
      
      if(update_packet != null)
      {
        sendPacketToNearbyPlayers(update_packet);
      }
      update_packet = null;
    } else
    {
      updateClient();
    }
    last_redstone_signal = redstone_signal;
  }

  @Override
  public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
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
  public String getName()
  {
    return null;
  }

  @Override
  public boolean hasCustomName()
  {
    return false;
  }

  @Override
  public ITextComponent getDisplayName()
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
        tag.setInteger("rsmode", mode.id);
        sendToServer(tag);
      }
    }
  }
  
  protected void sendToServer(NBTTagCompound tag)
  {
    if(worldObj.isRemote)
    {
      super.writeToNBT(tag);
      tag.setInteger("dim", worldObj.provider.getDimension());
      ModFoundry.network_channel.sendToServer(new MessageTileEntitySync(tag));
    }
  }

  @Override
  public void openInventory(EntityPlayer player)
  {
    if(!worldObj.isRemote && player instanceof EntityPlayerMP)
    {
      NBTTagCompound tag = writeToNBT(null);
      sendPacketToPlayer(tag,(EntityPlayerMP)player);
    }
  }

  @Override
  public void closeInventory(EntityPlayer player)
  {
    if(!worldObj.isRemote && player instanceof EntityPlayerMP)
    {
      NBTTagCompound tag = new NBTTagCompound();
      super.writeToNBT(tag);
      tag.setInteger("rsmode", mode.id);
      sendPacketToPlayer(tag,(EntityPlayerMP)player);
    }
  }
  
  protected final FluidStack drainTank(int slot, FluidStack resource, boolean doDrain)
  {
    FluidTank tank = getTank(slot);
    if(resource.isFluidEqual(tank.getFluid()))
    {
      FluidStack drained = tank.drain(resource.amount, doDrain);
      if( doDrain && drained != null && drained.amount > 0)
      {
        updateTank(slot);
        markDirty();
      }
      return drained;
    }
    return null;
  }

  protected final FluidStack drainTank(int slot, int maxDrain, boolean doDrain)
  {
    FluidTank tank = getTank(slot);
    FluidStack drained = tank.drain(maxDrain, doDrain);
    if( doDrain && drained != null && drained.amount > 0)
    {
      updateTank(slot);
      markDirty();
    }
    return drained;
  }
  
  protected final int fillTank(int slot, FluidStack resource, boolean doFill)
  {
    FluidTank tank = getTank(slot);
    int filled = tank.fill(resource, doFill);
    if( doFill && filled > 0)
    {
      updateTank(slot);
      markDirty();
    }
    return filled;
  }
  
  protected IFluidHandler getFluidHandler(EnumFacing facing)
  {
    return null;
  }
  
  @Override
  public boolean hasCapability(Capability<?> cap,EnumFacing facing)
  {
    return super.hasCapability(cap, facing) || (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
  }
  
  @Override
  public <T> T getCapability(Capability<T> cap, EnumFacing facing)
  {
    if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
    {
      IFluidHandler fluid_hnalder = getFluidHandler(facing);
      if(fluid_hnalder != null)
      {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidHandler(facing));
      } else
      {
        return super.getCapability(cap, facing);
      }
    } else
    {
      return super.getCapability(cap, facing);
    }
  }
}
