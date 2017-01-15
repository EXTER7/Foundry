package exter.foundry.tileentity;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.ModFoundry;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.network.MessageTileEntitySync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.items.IItemHandler;

public class TileEntityMaterialRouter extends TileEntityFoundry
{
  static public class Route
  {
    public String material;
    public String type;
    public EnumFacing side;

    public Route(String route_material, String route_type, EnumFacing route_side)
    {
      material = route_material;
      type = route_type;
      side = route_side;
    }

    public Route(ByteBuf data)
    {
      material = ByteBufUtils.readUTF8String(data);
      type = ByteBufUtils.readUTF8String(data);
      side = EnumFacing.values()[data.readByte()];
    }

    public Route(NBTTagCompound tag)
    {
      readFromNBT(tag);
    }

    public void readFromNBT(NBTTagCompound tag)
    {
      material = tag.getString("material");
      type = tag.getString("type");
      side = EnumFacing.VALUES[tag.getByte("side")];
    }

    public void writeToNBT(NBTTagCompound tag)
    {
      tag.setString("material", material);
      tag.setString("type", type);
      tag.setByte("side", (byte) side.getIndex());
    }

    public boolean matchesItem(ItemStack stack)
    {
      ModFoundry.log.info("Item: " + stack.getUnlocalizedName());
      ModFoundry.log.info("Material: " + MaterialRegistry.instance.getMaterial(stack));
      ModFoundry.log.info("Type: " + MaterialRegistry.instance.getType(stack));
      if(!material.equals("_Any"))
      {
        String stack_material = MaterialRegistry.instance.getMaterial(stack);
        if(!material.equals(stack_material))
        {
          return false;
        }
      }

      if(!type.equals("_Any"))
      {
        String stack_type = MaterialRegistry.instance.getType(stack);
        if(!type.equals(stack_type))
        {
          return false;
        }
      }

      return true;
    }

    public void writeToPacket(ByteBuf data)
    {
      ByteBufUtils.writeUTF8String(data, material);
      ByteBufUtils.writeUTF8String(data, type);
      data.writeByte(side.getIndex());
    }
  }

  public static final int SLOT_OUTPUT = 3;

  static private final Set<Integer> IH_SLOTS_INPUT = ImmutableSet.of(0, 1, 2);

  private List<Route> routes;
  private int input_index;

  public int gui_material_scroll;
  public int gui_type_scroll;
  public int gui_route_scroll;
  public int gui_material_selected;
  public int gui_type_selected;


  private Map<EnumFacing,ItemHandler> item_handlers;

  public TileEntityMaterialRouter()
  {
    routes = new ArrayList<Route>();
    input_index = 0;
    gui_material_scroll = 0;
    gui_type_scroll = 0;
    gui_route_scroll = 0;
    gui_material_selected = 0;
    gui_type_selected = 0;
    item_handlers = new EnumMap<EnumFacing,ItemHandler>(EnumFacing.class);
    for(EnumFacing facing:EnumFacing.VALUES)
    {
      item_handlers.put(facing, new ItemHandler(getSizeInventory(), IH_SLOTS_INPUT,ImmutableSet.of(SLOT_OUTPUT + facing.getIndex())));
    }
  }

  @Override
  protected IItemHandler getItemHandler(EnumFacing side)
  {
    if(side == null)
    {
      return null;
    }
    return item_handlers.get(side);
  }

  @Override
  public int getSizeInventory()
  {
    return SLOT_OUTPUT + 6;
  }

  @Override
  public int getInventoryStackLimit()
  {
    return 64;
  }

  @Override
  public void openInventory(EntityPlayer player)
  {
    if(FMLCommonHandler.instance().getEffectiveSide().isServer())
    {
      syncRoutes();
    }
  }

  @Override
  public void closeInventory(EntityPlayer player)
  {
    if(FMLCommonHandler.instance().getEffectiveSide().isServer())
    {
      syncRoutes();
    }
  }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack item)
  {
    return slot < SLOT_OUTPUT;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound)
  {
    super.readFromNBT(compound);

    NBTTagCompound routes_tag = (NBTTagCompound)compound.getTag("Routes");
    if(routes_tag != null)
    {
      routes.clear();
      int size = routes_tag.getInteger("size");
      int i;
      for(i = 0; i < size; i++)
      {
        NBTTagCompound route_entry_tag = routes_tag.getCompoundTag("Route_" + String.valueOf(i));
        routes.add(new Route(route_entry_tag));
      }
    }
    
    if(compound.hasKey("gui_material_scroll"))
    {
      gui_material_scroll = compound.getInteger("gui_material_scroll");
    }
    if(compound.hasKey("gui_type_scroll"))
    {
      gui_type_scroll = compound.getInteger("gui_type_scroll");
    }
    if(compound.hasKey("gui_route_scroll"))
    {
      gui_route_scroll = compound.getInteger("gui_route_scroll");
    }
    if(compound.hasKey("gui_material_selected"))
    {
      gui_material_selected = compound.getInteger("gui_material_selected");
    }
    if(compound.hasKey("gui_type_selected"))
    {
      gui_type_selected = compound.getInteger("gui_type_selected");
    }

  }

  private void writeRoutesToNBT(NBTTagCompound compound)
  {
    NBTTagCompound routes_tag = new NBTTagCompound();
    routes_tag.setInteger("size", routes.size());
    int i;
    for(i = 0; i < routes.size(); i++)
    {
      NBTTagCompound route_entry_tag = new NBTTagCompound();
      routes.get(i).writeToNBT(route_entry_tag);
      routes_tag.setTag("Route_" + String.valueOf(i), route_entry_tag);
    }
    compound.setTag("Routes", routes_tag);
  }
  
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound)
  {
    if(compound == null)
    {
      compound = new NBTTagCompound();
    }
    super.writeToNBT(compound);
    writeRoutesToNBT(compound);
    
    compound.setInteger("gui_material_scroll",gui_material_scroll);
    compound.setInteger("gui_type_scroll",gui_type_scroll);
    compound.setInteger("gui_route_scroll",gui_route_scroll);
    compound.setInteger("gui_material_selected",gui_material_selected);
    compound.setInteger("gui_type_selected",gui_type_selected);
    return compound;
  }

  private void routeItem(int in_slot, int out_slot)
  {
    ItemStack input = inventory[in_slot];
    ItemStack output = inventory[out_slot];
    if(output.isEmpty())
    {
      inventory[out_slot] = input;
      inventory[in_slot] = ItemStack.EMPTY;
      updateInventoryItem(in_slot);
      updateInventoryItem(out_slot);
    } else
    {
      if(!output.isItemEqual(input) || !ItemStack.areItemStackTagsEqual(output, input))
      {
        return;
      }
      int transfer = output.getMaxStackSize() - output.getCount();
      if(transfer > input.getCount())
      {
        transfer = input.getCount();
      }
      decrStackSize(in_slot, transfer);
      inventory[out_slot].grow(transfer);
      updateInventoryItem(in_slot);
      updateInventoryItem(out_slot);
    }
  }

  @Override
  protected void updateClient()
  {

  }

  @Override
  protected void updateServer()
  {
    if(input_index % 4 == 0)
    {
      int i = input_index / 4;
      ItemStack input = inventory[i];
      if(!input.isEmpty())
      {
        for(Route r : routes)
        {
          if(r.matchesItem(input))
          {
            routeItem(i, SLOT_OUTPUT + r.side.getIndex());
            break;
          }
        }
      }
    }
    input_index = (input_index + 1) % (SLOT_OUTPUT * 4);
  }

  @Override
  public FluidTank getTank(int slot)
  {
    return null;
  }

  @Override
  public int getTankCount()
  {
    return 0;
  }

  @Override
  protected void onInitialize()
  {

  }

  public List<Route> getRoutes()
  {
    return routes;
  }

  public void syncRoutes()
  {
    NBTTagCompound tag = new NBTTagCompound();
    writeTileToNBT(tag);   
    writeRoutesToNBT(tag);
    if(world.isRemote)
    {
      tag.setInteger("dim", world.provider.getDimension());
      ModFoundry.network_channel.sendToServer(new MessageTileEntitySync(tag));
    } else
    {
      sendPacketToNearbyPlayers(tag);
    }
  }
}
