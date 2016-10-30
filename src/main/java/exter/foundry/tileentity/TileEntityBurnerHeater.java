package exter.foundry.tileentity;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.heatable.IHeatProvider;
import exter.foundry.api.recipe.IBurnerHeaterFuel;
import exter.foundry.block.BlockBurnerHeater;
import exter.foundry.recipes.manager.BurnerHeaterFuelManager;
import exter.foundry.tileentity.itemhandler.ItemHandlerFuel;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.item.IExoflameHeatable;

@Optional.Interface(iface = "vazkii.botania.api.item.IExoflameHeatable", modid = "Botania")
public class TileEntityBurnerHeater extends TileEntityFoundry implements IExoflameHeatable
{
  private class HeatProvider implements IHeatProvider
  {
    @Override
    public int provideHeat(int max_heat)
    {
      if(burn_time > 0 && max_heat > 0)
      {
        if(max_heat > 0)
        {
          if(max_heat > heat_provide)
          {
            max_heat = heat_provide;
          }
          int last_burn_time = burn_time;
          burn_time -= FoundryMiscUtils.divCeil(max_heat * 10,heat_provide);
          if(burn_time < 0)
          {
            burn_time = 0;
          }
          
          if(last_burn_time != burn_time || update_burn_times)
          {
            if(last_burn_time*burn_time == 0)
            {
              ((BlockBurnerHeater)getBlockType()).setMachineState(worldObj, getPos(), worldObj.getBlockState(getPos()), burn_time > 0);
            }
            updateValue("BurnTime",burn_time);
          }
        }
        return max_heat;
      }

      return 0;
    }
  }
  

  private int burn_time;
  private int item_burn_time;
  private int heat_provide;
  private boolean update_burn_times;

  private HeatProvider heat_provider;
  private ItemHandlerFuel item_handler;

  private static int DEFAULT_HEAT_PROVIDE = TileEntityFoundryHeatable.getMaxHeatRecieve(170000,FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE);
 
  static private final Set<Integer> IH_SLOTS_INPUT = ImmutableSet.of(0, 1, 2, 3);
  static private final Set<Integer> IH_SLOTS_OUTPUT = ImmutableSet.of();
  static private final Set<Integer> IH_SLOTS_FUEL = ImmutableSet.of(0, 1, 2, 3);
  
  

  public TileEntityBurnerHeater()
  {
    burn_time = 0;
    item_burn_time = 0;
    update_burn_times = false;
    heat_provide = DEFAULT_HEAT_PROVIDE;
    heat_provider = new HeatProvider();
    item_handler = new ItemHandlerFuel(this,getSizeInventory(),IH_SLOTS_INPUT,IH_SLOTS_OUTPUT,IH_SLOTS_FUEL);
  }
  
  @Override
  protected IItemHandler getItemHandler(EnumFacing side)
  {
    return item_handler;
  }

  @Override
  public int getSizeInventory()
  {
    return 4;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag)
  {
    super.readFromNBT(tag);
    if(tag.hasKey("BurnTime"))
    {
      burn_time = tag.getInteger("BurnTime");
    }
    if(tag.hasKey("ItemBurnTime"))
    {
      item_burn_time = tag.getInteger("ItemBurnTime");
    }
    if(tag.hasKey("HeatProvide"))
    {
      heat_provide = tag.getInteger("HeatProvide");
    }
    if(worldObj != null && !worldObj.isRemote)
    {
      ((BlockBurnerHeater)getBlockType()).setMachineState(worldObj, getPos(), worldObj.getBlockState(getPos()), burn_time > 0);
    }
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
  {
    return oldState.getBlock() != newSate.getBlock();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound)
  {
    if(compound == null)
    {
      compound = new NBTTagCompound();
    }
    super.writeToNBT(compound);
    compound.setInteger("BurnTime", burn_time);
    compound.setInteger("ItemBurnTime", item_burn_time);
    compound.setInteger("HeatProvide", heat_provide);
    return compound;
  }

  @Override
  public int getInventoryStackLimit()
  {
    return 64;
  }

  public boolean isBurning()
  {
    return burn_time > 0;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
  {
    return this.worldObj.getTileEntity(getPos()) != this ? false : par1EntityPlayer.getDistanceSq(getPos()) <= 64.0D;
  }

  @Override
  public void openInventory(EntityPlayer player)
  {

  }

  @Override
  public void closeInventory(EntityPlayer player)
  {

  }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack stack)
  {
    return TileEntityFurnace.isItemFuel(stack);
  }

  @Override
  protected void updateClient()
  {

  }

  @Override
  protected void updateServer()
  {
    int last_burn_time = burn_time;
    int last_item_burn_time = item_burn_time;


    if(burn_time < 10)
    {
      for(int i = 0; i < 4; i++)
      {
        ItemStack item = inventory[i];
        if(item != null)
        {
          int burn = 0;
          IBurnerHeaterFuel fuel = BurnerHeaterFuelManager.instance.getFuel(item);
          if(fuel != null)
          {
            burn = fuel.getBurnTime() * 10;
            heat_provide = fuel.getHeat();
          } else
          {
            burn = TileEntityFurnace.getItemBurnTime(item) * 10;
            heat_provide = DEFAULT_HEAT_PROVIDE;
          }
          if(burn > 0)
          {
            burn_time += burn;
            item_burn_time = burn_time;
            if(--item.stackSize == 0)
            {
              inventory[i] = item.getItem().getContainerItem(item);
            }
            updateInventoryItem(i);
            break;
          }
        }
      }
    }

    if(last_burn_time != burn_time || update_burn_times)
    {
      if(last_burn_time*burn_time == 0)
      {
        ((BlockBurnerHeater)getBlockType()).setMachineState(worldObj, getPos(), worldObj.getBlockState(getPos()), burn_time > 0);
      }
      updateValue("BurnTime",burn_time);
    }

    if(last_item_burn_time != item_burn_time || update_burn_times)
    {
      updateValue("ItemBurnTime",item_burn_time);
    }
    update_burn_times = false;
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

  public int getBurningTime()
  {
    return burn_time;
  }

  public int getItemBurnTime()
  {
    return item_burn_time;
  }

  @Override
  public boolean hasCapability(Capability<?> cap,EnumFacing facing)
  {
    return super.hasCapability(cap, facing) || (cap == FoundryAPI.capability_heatprovider && facing == EnumFacing.UP);
  }
  
  @Override
  public <T> T getCapability(Capability<T> cap, EnumFacing facing)
  {
    if(cap == FoundryAPI.capability_heatprovider && facing == EnumFacing.UP)
    {
      return FoundryAPI.capability_heatprovider.cast(heat_provider);
    }
    return super.getCapability(cap, facing);
  }

  
  @Optional.Method(modid = "Botania")
  @Override
  public boolean canSmelt()
  {
    return true;
  }

  @Optional.Method(modid = "Botania")
  @Override
  public int getBurnTime()
  {
    return burn_time <= 1 ? 0 : burn_time - 1;
  }

  @Optional.Method(modid = "Botania")
  @Override
  public void boostBurnTime()
  {
    if(!worldObj.isRemote)
    {
      heat_provide = DEFAULT_HEAT_PROVIDE;
      burn_time = 2000;
      item_burn_time = 1999;
      update_burn_times = true;
      markDirty();
    }
  }

  @Optional.Method(modid = "Botania")
  @Override
  public void boostCookTime()
  {

  }
}
