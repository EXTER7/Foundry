package exter.foundry.tileentity;

import buildcraft.api.power.IPowerReceptor;
import exter.foundry.network.FoundryPacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityFoundry extends TileEntity
{
  protected void SendUpdatePacket(NBTTagCompound nbt)
  {
    FoundryPacketHandler.SendTileEntityPacketToPlayers(new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt), this);
  }

  protected abstract void UpdateEntityClient();

  protected abstract void UpdateEntityServer();

  @Override
  public Packet getDescriptionPacket()
  {
    NBTTagCompound nbt = new NBTTagCompound();
    writeToNBT(nbt);    
    return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
  }

  @Override
  public final void updateEntity()
  {
    if(this instanceof IPowerReceptor)
    {
      ((IPowerReceptor)this).getPowerReceiver(null).update();
    }

    if(!worldObj.isRemote)
    {
      UpdateEntityServer();
    } else
    {
      UpdateEntityClient();
    }
  }

  @Override
  public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
  {
    super.onDataPacket(net, pkt);
    readFromNBT(pkt.customParam1);
    //worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
  }

}
