package exter.foundry.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import exter.foundry.ModFoundry;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityFoundry;
import exter.foundry.tileentity.TileEntityInductionCrucibleFurnace;
import exter.foundry.tileentity.TileEntityMetalCaster;

public class FoundryPacketHandler implements IPacketHandler
{
  static final int MAX_DISTANCE = 192;
  
  public static void SendTileEntityPacketToPlayers(Packet packet, TileEntity tile)
  {
    World world = tile.worldObj; 
    if(!world.isRemote && packet != null)
    {
      for(int j = 0; j < world.playerEntities.size(); j++)
      {
        EntityPlayerMP player = (EntityPlayerMP) world.playerEntities.get(j);

        if(Math.abs(player.posX - tile.xCoord) <= MAX_DISTANCE && Math.abs(player.posY - tile.yCoord) <= MAX_DISTANCE && Math.abs(player.posZ - tile.zCoord) <= MAX_DISTANCE)
        {
          player.playerNetServerHandler.sendPacketToPlayer(packet);
        }
      }
    }
  }

  static private Packet250CustomPayload MakePacket(ByteArrayOutputStream bytes)
  {
    Packet250CustomPayload packet = new Packet250CustomPayload();
    packet.channel = ModFoundry.CHANNEL;
    packet.data = bytes.toByteArray();
    packet.length = packet.data.length;
    packet.isChunkDataPacket = true;
    return packet;
  }
  
  static private Packet250CustomPayload MakeICFModePacket(TileEntityInductionCrucibleFurnace sender)
  {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    DataOutputStream data = new DataOutputStream(bytes);
    try
    {
      //Position
      data.writeInt(sender.xCoord);
      data.writeInt(sender.yCoord);
      data.writeInt(sender.zCoord);
      
      data.writeByte(sender.GetMode().number);
    } catch(IOException e)
    {
      e.printStackTrace();
    }
    return MakePacket(bytes);
  }

  static private Packet250CustomPayload MakeCasterModePacket(TileEntityMetalCaster sender)
  {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    DataOutputStream data = new DataOutputStream(bytes);
    try
    {
      //Position
      data.writeInt(sender.xCoord);
      data.writeInt(sender.yCoord);
      data.writeInt(sender.zCoord);
      
      data.writeByte(sender.GetMode().number);
    } catch(IOException e)
    {
      e.printStackTrace();
    }
    return MakePacket(bytes);
  }
  
  static private Packet250CustomPayload MakeAlloyMixerModePacket(TileEntityAlloyMixer sender)
  {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    DataOutputStream data = new DataOutputStream(bytes);
    try
    {
      //Position
      data.writeInt(sender.xCoord);
      data.writeInt(sender.yCoord);
      data.writeInt(sender.zCoord);
      
      data.writeByte(sender.GetMode().number);
    } catch(IOException e)
    {
      e.printStackTrace();
    }
    return MakePacket(bytes);
  }

  static public void SendCasterModeToServer(TileEntityMetalCaster sender)
  {
    PacketDispatcher.sendPacketToServer(MakeCasterModePacket(sender));
  }

  public static void SendCasterModeToClients(TileEntityMetalCaster sender)
  {
    SendTileEntityPacketToPlayers(MakeCasterModePacket(sender), sender);
  }

  static public void SendICFModeToServer(TileEntityInductionCrucibleFurnace sender)
  {
    PacketDispatcher.sendPacketToServer(MakeICFModePacket(sender));
  }

  public static void SendICFModeToClients(TileEntityInductionCrucibleFurnace sender)
  {
    SendTileEntityPacketToPlayers(MakeICFModePacket(sender), sender);
  }

  static public void SendAlloyMixerModeToServer(TileEntityAlloyMixer sender)
  {
    PacketDispatcher.sendPacketToServer(MakeAlloyMixerModePacket(sender));
  }

  public static void SendAlloyMixerModeToClients(TileEntityAlloyMixer sender)
  {
    SendTileEntityPacketToPlayers(MakeAlloyMixerModePacket(sender), sender);
  }

  @Override
  public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
  {
    try
    {
      ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
      int x = data.readInt();
      int y = data.readInt();
      int z = data.readInt();
      World world = ((EntityPlayer)player).worldObj;

      if(world != null)
      {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if(tileEntity != null)
        {
          if(tileEntity instanceof TileEntityFoundry)
          {
            ((TileEntityFoundry)tileEntity).ReceivePacketData(manager, packet, ((EntityPlayer)player), data);
          }
        }
      }
    } catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}
