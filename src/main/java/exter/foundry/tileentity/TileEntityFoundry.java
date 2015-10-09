package exter.foundry.tileentity;


import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Base class for all machines.
 */
public abstract class TileEntityFoundry extends TileEntity implements IUpdatePlayerListBox,IInventory
{
  
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
    
    public void Update()
    {
      ItemStack stack = getStackInSlot(slot);
      if(stack == null || !(stack.getItem() instanceof IFluidContainerItem))
      {
        return;
      }
      IFluidContainerItem fluid_cont = (IFluidContainerItem)stack.getItem();
      
      FluidTank tank = GetTank(tank_slot);
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
        UpdateTank(tank_slot);
        UpdateInventoryItem(slot);
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
        UpdateTank(tank_slot);
        UpdateInventoryItem(slot);
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

  protected abstract void UpdateEntityClient();

  protected abstract void UpdateEntityServer();

  public abstract FluidTank GetTank(int slot);
  
  public abstract int GetTankCount();
  
  protected abstract void OnInitialize();

  public TileEntityFoundry()
  {
    conatiner_slots = new ArrayList<ContainerSlot>();
    last_redstone_signal = false;
    redstone_signal = false;
    initialized = false;
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
  
  
  protected final void UpdateTank(int slot)
  {
    if(packet == null)
    {
      return;
    }
    WriteTankToNBT(packet,slot);
    do_update = true;
  }
  
  protected final void UpdateInventoryItem(int slot)
  {
    if(packet == null)
    {
      return;
    }
    WriteInventoryItemToNBT(packet,slot);
    do_update = true;
  }

  protected final void WriteTankToNBT(NBTTagCompound compound,int slot)
  {
    NBTTagCompound tag = new NBTTagCompound();
    GetTank(slot).writeToNBT(tag);
    compound.setTag("Tank_" + String.valueOf(slot), tag);
  }

  protected final void WriteInventoryItemToNBT(NBTTagCompound compound,int slot)
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
    for(i = 0; i < GetTankCount(); i++)
    {
      NBTTagCompound tag = (NBTTagCompound)compound.getTag("Tank_" + String.valueOf(i));
      if(tag != null)
      {
        FluidTank tank = GetTank(i);
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
    
  }
  
  
  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    int i;
    super.writeToNBT(compound);
    for(i = 0; i < GetTankCount(); i++)
    {
      WriteTankToNBT(compound,i);
    }
    for(i = 0; i < getSizeInventory(); i++)
    {
      WriteInventoryItemToNBT(compound,i);
    }
  }

  protected final void UpdateValue(String name,int value)
  {
    if(packet == null)
    {
      return;
    }
    packet.setInteger(name, value);

    do_update = true;
  }

  protected final void UpdateNBTTag(String name,NBTTagCompound compound)
  {
    if(packet == null)
    {
      return;
    }
    packet.setTag(name, compound);
    do_update = true;
  }

  private void SendPacketToPlayers(Packet packet)
  {
    final int MAX_DISTANCE = 192;
    if(!worldObj.isRemote && packet != null)
    {
      for(int j = 0; j < worldObj.playerEntities.size(); j++)
      {
        EntityPlayerMP player = (EntityPlayerMP) worldObj.playerEntities.get(j);

        BlockPos pos = getPos();
        if(Math.abs(player.posX - pos.getX()) <= MAX_DISTANCE && Math.abs(player.posY - pos.getY()) <= MAX_DISTANCE && Math.abs(player.posZ - pos.getY()) <= MAX_DISTANCE && player.dimension == worldObj.provider.getDimensionId())
        {
          player.playerNetServerHandler.sendPacket(packet);
        }
      }
    }
  }
   

  protected final int GetTankFluid(FluidTank tank)
  {
    FluidStack f = tank.getFluid();
    return f != null ? f.getFluidID() : 0;
  }

  protected final int GetTankAmount(FluidTank tank)
  {
    FluidStack f = tank.getFluid();
    return f != null ? f.amount : 0;
  }
  
  protected final void SetTankFluid(FluidTank tank,int value)
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

  protected final void SetTankAmount(FluidTank tank,int value)
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
      OnInitialize();
      initialized = true;
    }
    
    if(!worldObj.isRemote)
    {

      packet = new NBTTagCompound();
      super.writeToNBT(packet);
      do_update = false;
      for(ContainerSlot cs:conatiner_slots)
      {
        cs.Update();
      }
      UpdateEntityServer();
      
      if(do_update)
      {
        SendPacketToPlayers(new S35PacketUpdateTileEntity(getPos(), 0, packet));
      }
      packet = null;
    } else
    {
      UpdateEntityClient();
    }
    last_redstone_signal = redstone_signal;
  }

  @Override
  public final void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
  {
    super.onDataPacket(net, pkt);
    if(FMLCommonHandler.instance().getEffectiveSide().isClient())
    {
      readFromNBT(pkt.getNbtCompound());
    }
    //worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
  }
    
  public void updateRedstone()
  {
    redstone_signal = worldObj.isBlockIndirectlyGettingPowered(getPos()) > 0;
  }
    
  public void ReceivePacketData(ByteBuf data)
  {
    
  }
  
  @Override
  public boolean isUseableByPlayer(EntityPlayer player)
  {
    return worldObj.getTileEntity(getPos()) != this ? false : player.getDistanceSq(getPos()) <= 64.0D;
  }

  @Override
  public int getField(int id)
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setField(int id, int value)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public int getFieldCount()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void clear()
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getCommandSenderName()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean hasCustomName()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public IChatComponent getDisplayName()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
