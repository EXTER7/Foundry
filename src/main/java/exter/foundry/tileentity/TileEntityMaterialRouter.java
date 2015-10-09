package exter.foundry.tileentity;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import exter.foundry.ModFoundry;
import exter.foundry.material.MaterialRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class TileEntityMaterialRouter extends TileEntityFoundry implements ISidedInventory
{

  public enum RouteSide
  {
    RED(0),
    GREEN(1),
    BLUE(2),
    CYAN(3),
    MAGENTA(4),
    YELLOW(5);

    public final int index;

    private RouteSide(int side_index)
    {
      index = side_index;
    }
  }

  static public class Route
  {
    public String material;
    public String type;
    public RouteSide side;

    public Route(String route_material, String route_type, RouteSide route_side)
    {
      material = route_material;
      type = route_type;
      side = route_side;
    }

    public Route(ByteBuf data)
    {
      material = ByteBufUtils.readUTF8String(data);
      type = ByteBufUtils.readUTF8String(data);
      side = RouteSide.values()[data.readByte()];
    }

    public Route(NBTTagCompound tag)
    {
      ReadFromNBT(tag);
    }

    public void ReadFromNBT(NBTTagCompound tag)
    {
      material = tag.getString("material");
      type = tag.getString("type");
      side = RouteSide.values()[tag.getByte("side")];
    }

    public void WriteToNBT(NBTTagCompound tag)
    {
      tag.setString("material", material);
      tag.setString("type", type);
      tag.setByte("side", (byte) side.index);
    }

    public boolean MatchesItem(ItemStack stack)
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

    public void WriteToPacket(ByteBuf data)
    {
      ByteBufUtils.writeUTF8String(data, material);
      ByteBufUtils.writeUTF8String(data, type);
      data.writeByte(side.index);
    }

  }

  public static final int SLOT_OUTPUT = 3;

  private ItemStack[] inventory;
  private List<Route> routes;
  private int input_index;

  public int gui_material_scroll;
  public int gui_type_scroll;
  public int gui_route_scroll;
  public int gui_material_selected;
  public int gui_type_selected;

  public TileEntityMaterialRouter()
  {
    inventory = new ItemStack[SLOT_OUTPUT + 6];
    routes = new ArrayList<Route>();
    input_index = 0;
    gui_material_scroll = 0;
    gui_type_scroll = 0;
    gui_route_scroll = 0;
    gui_material_selected = 0;
    gui_type_selected = 0;
  }

  @Override
  public int getSizeInventory()
  {
    return inventory.length;
  }

  @Override
  public ItemStack getStackInSlot(int slot)
  {
    return inventory[slot];
  }

  @Override
  public ItemStack decrStackSize(int slot, int amount)
  {
    if(inventory[slot] != null)
    {
      ItemStack is;

      if(inventory[slot].stackSize <= amount)
      {
        is = inventory[slot];
        inventory[slot] = null;
        markDirty();
        return is;
      } else
      {
        is = inventory[slot].splitStack(amount);

        if(inventory[slot].stackSize == 0)
        {
          inventory[slot] = null;
        }

        markDirty();
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
    if(inventory[slot] != null)
    {
      ItemStack is = inventory[slot];
      inventory[slot] = null;
      return is;
    } else
    {
      return null;
    }
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack stack)
  {
    inventory[slot] = stack;

    if(stack != null && stack.stackSize > this.getInventoryStackLimit())
    {
      stack.stackSize = this.getInventoryStackLimit();
    }

    markDirty();
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
      SyncRoutes();
    }
  }

  @Override
  public void closeInventory(EntityPlayer player)
  {
    if(FMLCommonHandler.instance().getEffectiveSide().isServer())
    {
      SyncRoutes();
    }
  }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack item)
  {
    return slot < SLOT_OUTPUT;
  }

  @Override
  public void ReceivePacketData(ByteBuf data)
  {
    routes.clear();

    gui_material_scroll = data.readInt();
    gui_type_scroll = data.readInt();
    gui_route_scroll = data.readInt();
    gui_material_selected = data.readInt();
    gui_type_selected = data.readInt();

    int size = data.readInt();
    int i;
    for(i = 0; i < size; i++)
    {
      routes.add(new Route(data));
    }
    markDirty();
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

  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    NBTTagCompound routes_tag = new NBTTagCompound();
    routes_tag.setInteger("size", routes.size());
    int i;
    for(i = 0; i < routes.size(); i++)
    {
      NBTTagCompound route_entry_tag = new NBTTagCompound();
      routes.get(i).WriteToNBT(route_entry_tag);
      routes_tag.setTag("Route_" + String.valueOf(i), route_entry_tag);
    }
    compound.setTag("Routes", routes_tag);
    
    compound.setInteger("gui_material_scroll",gui_material_scroll);
    compound.setInteger("gui_type_scroll",gui_type_scroll);
    compound.setInteger("gui_route_scroll",gui_route_scroll);
    compound.setInteger("gui_material_selected",gui_material_selected);
    compound.setInteger("gui_type_selected",gui_type_selected);

  }

  private void RouteItem(int in_slot, int out_slot)
  {
    ItemStack input = inventory[in_slot];
    ItemStack output = inventory[out_slot];
    if(output == null)
    {
      inventory[out_slot] = input;
      inventory[in_slot] = null;
      UpdateInventoryItem(in_slot);
      UpdateInventoryItem(out_slot);
    } else
    {
      if(!output.isItemEqual(input) || !ItemStack.areItemStackTagsEqual(output, input))
      {
        return;
      }
      int transfer = output.getMaxStackSize() - output.stackSize;
      if(transfer > input.stackSize)
      {
        transfer = input.stackSize;
      }
      decrStackSize(in_slot, transfer);
      inventory[out_slot].stackSize += transfer;
      UpdateInventoryItem(in_slot);
      UpdateInventoryItem(out_slot);
    }
  }

  @Override
  protected void UpdateEntityClient()
  {

  }

  @Override
  protected void UpdateEntityServer()
  {
    if(input_index % 4 == 0)
    {
      int i = input_index / 4;
      ItemStack input = inventory[i];
      if(input != null)
      {
        for(Route r : routes)
        {
          if(r.MatchesItem(input))
          {
            RouteItem(i, SLOT_OUTPUT + r.side.index);
            break;
          }
        }
      }
    }
    input_index = (input_index + 1) % (SLOT_OUTPUT * 4);
  }

  @Override
  public FluidTank GetTank(int slot)
  {
    return null;
  }

  @Override
  public int GetTankCount()
  {
    return 0;
  }

  @Override
  protected void OnInitialize()
  {

  }

  private static int[][] SIDE_SLOTS = { { 0, 1, 2, SLOT_OUTPUT }, { 0, 1, 2, SLOT_OUTPUT + 1 }, { 0, 1, 2, SLOT_OUTPUT + 2 }, { 0, 1, 2, SLOT_OUTPUT + 3 }, { 0, 1, 2, SLOT_OUTPUT + 4 }, { 0, 1, 2, SLOT_OUTPUT + 5 } };

  @Override
  public int[] getSlotsForFace(EnumFacing side)
  {
    return SIDE_SLOTS[side.getIndex()];
  }

  @Override
  public boolean canInsertItem(int slot, ItemStack item, EnumFacing side)
  {
    return slot < SLOT_OUTPUT;
  }

  @Override
  public boolean canExtractItem(int slot, ItemStack item, EnumFacing side)
  {
    return slot == SLOT_OUTPUT + side.getIndex();
  }

  public List<Route> GetRoutes()
  {
    return routes;
  }

  public void SyncRoutes()
  {
    if(FMLCommonHandler.instance().getEffectiveSide().isClient())
    {
      ModFoundry.network_channel.SendMaterialRouterPacketToServer(this);
    } else
    {
      ModFoundry.network_channel.SendMaterialRouterPacketToClients(this);
    }
  }
}
