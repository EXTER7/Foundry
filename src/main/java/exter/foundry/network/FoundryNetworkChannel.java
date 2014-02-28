package exter.foundry.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityFoundryPowered;
import exter.foundry.tileentity.TileEntityInductionCrucibleFurnace;
import exter.foundry.tileentity.TileEntityMetalCaster;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class FoundryNetworkChannel
{
  public static final String CHANNEL_NAME = "EXTER.FOUNDRY";
  
  private FMLEventChannel network_channel;
  
  public FoundryNetworkChannel()
  {
    network_channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(CHANNEL_NAME);
    network_channel.register(this);
  }

  static private void WriteTileEntityCoords(ByteBufOutputStream data,TileEntity sender) throws IOException
  {
    data.writeInt(sender.xCoord);
    data.writeInt(sender.yCoord);
    data.writeInt(sender.zCoord);
    data.writeInt(sender.getWorldObj().provider.dimensionId);
  }

  static private FMLProxyPacket MakeCasterModePacket(TileEntityMetalCaster sender)
  {
    ByteBuf bytes = Unpooled.buffer();
    ByteBufOutputStream data = new ByteBufOutputStream(bytes);
    try
    {
      WriteTileEntityCoords(data,sender);

      data.writeByte(sender.GetMode().number);
    } catch(IOException e)
    {
      e.printStackTrace();
    }

    return new FMLProxyPacket(bytes, CHANNEL_NAME);
  }

  static private FMLProxyPacket MakeICFModePacket(TileEntityInductionCrucibleFurnace sender)
  {
    ByteBuf bytes = Unpooled.buffer();
    ByteBufOutputStream data = new ByteBufOutputStream(bytes);
    try
    {
      WriteTileEntityCoords(data,sender);
      
      data.writeByte(sender.GetMode().number);
    } catch(IOException e)
    {
      e.printStackTrace();
    }
    return new FMLProxyPacket(bytes, CHANNEL_NAME);
  }
  

  static private FMLProxyPacket MakeAlloyMixerModePacket(TileEntityAlloyMixer sender)
  {
    ByteBuf bytes = Unpooled.buffer();
    ByteBufOutputStream data = new ByteBufOutputStream(bytes);
    try
    {
      WriteTileEntityCoords(data,sender);
      
      data.writeByte(sender.GetMode().number);
    } catch(IOException e)
    {
      e.printStackTrace();
    }
    return new FMLProxyPacket(bytes, CHANNEL_NAME);
  }

  public void SendCasterModeToServer(TileEntityMetalCaster sender)
  {
    network_channel.sendToServer(MakeCasterModePacket(sender));
  }

  public void SendCasterModeToClients(TileEntityMetalCaster sender)
  {
    network_channel.sendToAllAround(
        MakeCasterModePacket(sender),
        new TargetPoint(sender.getWorldObj().provider.dimensionId, sender.xCoord, sender.yCoord, sender.zCoord, 192));
  }

  public void SendICFModeToServer(TileEntityInductionCrucibleFurnace sender)
  {
    network_channel.sendToServer(MakeICFModePacket(sender));
  }

  public void SendICFModeToClients(TileEntityInductionCrucibleFurnace sender)
  {
    network_channel.sendToAllAround(
        MakeICFModePacket(sender),
        new TargetPoint(sender.getWorldObj().provider.dimensionId, sender.xCoord, sender.yCoord, sender.zCoord, 192));
  }

  public void SendAlloyMixerModeToServer(TileEntityAlloyMixer sender)
  {
    network_channel.sendToServer(MakeAlloyMixerModePacket(sender));
  }

  public void SendAlloyMixerModeToClients(TileEntityAlloyMixer sender)
  {
    network_channel.sendToAllAround(
        MakeAlloyMixerModePacket(sender),
        new TargetPoint(sender.getWorldObj().provider.dimensionId, sender.xCoord, sender.yCoord, sender.zCoord, 192));
  }

  private void OnTEPacketData(ByteBufInputStream data, World world, int x, int y, int z) throws IOException
  {
    if(world != null)
    {
      TileEntity tileEntity = world.getTileEntity(x, y, z);

      if(tileEntity != null)
      {
        if(tileEntity instanceof TileEntityFoundryPowered)
        {
          ((TileEntityFoundryPowered) tileEntity).ReceivePacketData(data);
        }
      }
    }
  }

  @SubscribeEvent
  public void onClientPacketData(ClientCustomPacketEvent event)
  {
    try
    {
      ByteBufInputStream data = new ByteBufInputStream(event.packet.payload());
      int x = data.readInt();
      int y = data.readInt();
      int z = data.readInt();
      int d = data.readInt();
      World world = Minecraft.getMinecraft().theWorld;
      if(d == world.provider.dimensionId)
      {
        OnTEPacketData(data, world, x, y, z);
      }
    } catch(Exception e)
    {
      new RuntimeException(e);
    }
  }

  @SubscribeEvent
  public void onServerPacketData(ServerCustomPacketEvent event)
  {
    try
    {
      ByteBufInputStream data = new ByteBufInputStream(event.packet.payload());
      int x = data.readInt();
      int y = data.readInt();
      int z = data.readInt();
      int d = data.readInt();
      World world = DimensionManager.getWorld(d);
      OnTEPacketData(data, world, x, y, z);
    } catch(Exception e)
    {
      new RuntimeException(e);
    }
  }
}
