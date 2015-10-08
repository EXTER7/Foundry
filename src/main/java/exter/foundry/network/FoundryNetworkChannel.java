package exter.foundry.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityFoundry;
import exter.foundry.tileentity.TileEntityInductionCrucibleFurnace;
import exter.foundry.tileentity.TileEntityMaterialRouter;
import exter.foundry.tileentity.TileEntityMetalAtomizer;
import exter.foundry.tileentity.TileEntityMetalCaster;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FoundryNetworkChannel
{
  public static final String CHANNEL_NAME = "EXTER.FOUNDRY";
  
  private FMLEventChannel network_channel;
  
  public FoundryNetworkChannel()
  {
    network_channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(CHANNEL_NAME);
    network_channel.register(this);
  }

  static private void WriteTileEntityCoords(PacketBuffer data,TileEntity sender)
  {
    data.writeInt(sender.getPos().getX());
    data.writeInt(sender.getPos().getY());
    data.writeInt(sender.getPos().getZ());
    data.writeInt(sender.getWorld().provider.getDimensionId());
  }

  static private FMLProxyPacket MakeCasterModePacket(TileEntityMetalCaster sender)
  {
    PacketBuffer data = new PacketBuffer(Unpooled.buffer());
    WriteTileEntityCoords(data,sender);
    data.writeByte(sender.GetMode().number);

    return new FMLProxyPacket(data, CHANNEL_NAME);
  }

  static private FMLProxyPacket MakeAtomizerModePacket(TileEntityMetalAtomizer sender)
  {
    PacketBuffer data = new PacketBuffer(Unpooled.buffer());
    WriteTileEntityCoords(data,sender);
    data.writeByte(sender.GetMode().number);

    return new FMLProxyPacket(data, CHANNEL_NAME);
  }

  static private FMLProxyPacket MakeICFModePacket(TileEntityInductionCrucibleFurnace sender)
  {
    PacketBuffer data = new PacketBuffer(Unpooled.buffer());
    WriteTileEntityCoords(data,sender);
    data.writeByte(sender.GetMode().number);
    return new FMLProxyPacket(data, CHANNEL_NAME);
  }
  

  static private FMLProxyPacket MakeAlloyMixerModePacket(TileEntityAlloyMixer sender)
  {
    PacketBuffer data = new PacketBuffer(Unpooled.buffer());
    WriteTileEntityCoords(data,sender);
    data.writeByte(sender.GetMode().number);
    return new FMLProxyPacket(data, CHANNEL_NAME);
  }

  private FMLProxyPacket MakeMateralRouterPacket(TileEntityMaterialRouter sender)
  {
    PacketBuffer data = new PacketBuffer(Unpooled.buffer());
    WriteTileEntityCoords(data,sender);
    data.writeInt(sender.gui_material_scroll);
    data.writeInt(sender.gui_type_scroll);
    data.writeInt(sender.gui_route_scroll);
    data.writeInt(sender.gui_material_selected);
    data.writeInt(sender.gui_type_selected);
    List<TileEntityMaterialRouter.Route> routes = sender.GetRoutes();
    data.writeInt(routes.size());
    for(TileEntityMaterialRouter.Route r:routes)
    {
      r.WriteToPacket(data);
    }
    return new FMLProxyPacket(data, CHANNEL_NAME);
  }
  
  public void SendCasterModeToServer(TileEntityMetalCaster sender)
  {
    network_channel.sendToServer(MakeCasterModePacket(sender));
  }

  public void SendAtomizerModeToServer(TileEntityMetalAtomizer sender)
  {
    network_channel.sendToServer(MakeAtomizerModePacket(sender));
  }

  public void SendCasterModeToClients(TileEntityMetalCaster sender)
  {
    network_channel.sendToAllAround(
        MakeCasterModePacket(sender),
        new TargetPoint(sender.getWorld().provider.getDimensionId(), sender.getPos().getX(), sender.getPos().getY(), sender.getPos().getZ(), 192));
  }

  public void SendAtomizerModeToClients(TileEntityMetalAtomizer sender)
  {
    network_channel.sendToAllAround(
        MakeAtomizerModePacket(sender),
        new TargetPoint(sender.getWorld().provider.getDimensionId(), sender.getPos().getX(), sender.getPos().getY(), sender.getPos().getZ(), 192));
  }

  public void SendICFModeToServer(TileEntityInductionCrucibleFurnace sender)
  {
    network_channel.sendToServer(MakeICFModePacket(sender));
  }

  public void SendICFModeToClients(TileEntityInductionCrucibleFurnace sender)
  {
    network_channel.sendToAllAround(
        MakeICFModePacket(sender),
        new TargetPoint(sender.getWorld().provider.getDimensionId(), sender.getPos().getX(), sender.getPos().getY(), sender.getPos().getZ(), 192));
  }

  public void SendAlloyMixerModeToServer(TileEntityAlloyMixer sender)
  {
    network_channel.sendToServer(MakeAlloyMixerModePacket(sender));
  }

  public void SendAlloyMixerModeToClients(TileEntityAlloyMixer sender)
  {
    network_channel.sendToAllAround(
        MakeAlloyMixerModePacket(sender),
        new TargetPoint(sender.getWorld().provider.getDimensionId(), sender.getPos().getX(), sender.getPos().getY(), sender.getPos().getZ(), 192));
  }

  
  public void SendMaterialRouterPacketToServer(TileEntityMaterialRouter sender)
  {
    network_channel.sendToServer(MakeMateralRouterPacket(sender));
  }

  public void SendMaterialRouterPacketToClients(TileEntityMaterialRouter sender)
  {
    network_channel.sendToAllAround(
        MakeMateralRouterPacket(sender),
        new TargetPoint(sender.getWorld().provider.getDimensionId(), sender.getPos().getX(), sender.getPos().getY(), sender.getPos().getZ(), 192));
  }

  private void OnTEPacketData(ByteBuf data, World world, BlockPos pos)
  {
    if(world != null)
    {
      TileEntity tileEntity = world.getTileEntity(pos);

      if(tileEntity != null)
      {
        if(tileEntity instanceof TileEntityFoundry)
        {
          ((TileEntityFoundry) tileEntity).ReceivePacketData(data);
        }
      }
    }
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void onClientPacketData(ClientCustomPacketEvent event)
  {
    try
    {
      ByteBuf data = event.packet.payload();
      int x = data.readInt();
      int y = data.readInt();
      int z = data.readInt();
      int d = data.readInt();
      World world = Minecraft.getMinecraft().theWorld;
      if(d == world.provider.getDimensionId())
      {
        OnTEPacketData(data, world, new BlockPos(x,y,z));
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
      ByteBuf data = event.packet.payload();
      int x = data.readInt();
      int y = data.readInt();
      int z = data.readInt();
      int d = data.readInt();
      World world = DimensionManager.getWorld(d);
      OnTEPacketData(data, world, new BlockPos(x,y,z));
    } catch(Exception e)
    {
      new RuntimeException(e);
    }
  }
}
