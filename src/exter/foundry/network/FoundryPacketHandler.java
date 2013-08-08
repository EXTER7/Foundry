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
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import exter.foundry.ModFoundry;
import exter.foundry.tileentity.TileEntityInductionCrucibleFurnace;

public class FoundryPacketHandler implements IPacketHandler
{
  public static void SendPacketToPlayer(EntityPlayer player, Packet packet)
  {
    if(packet != null)
    {
      EntityPlayerMP pmp = (EntityPlayerMP) player;
      pmp.playerNetServerHandler.sendPacketToPlayer(packet);
    }
  }
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

  @Override
  public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
  {

  }
}
