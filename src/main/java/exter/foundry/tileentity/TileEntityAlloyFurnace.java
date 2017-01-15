package exter.foundry.tileentity;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.block.BlockAlloyFurnace;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.tileentity.itemhandler.ItemHandlerFuel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.item.IExoflameHeatable;

@Optional.Interface(iface = "vazkii.botania.api.item.IExoflameHeatable", modid = "Botania")
public class TileEntityAlloyFurnace extends TileEntityFoundry implements IExoflameHeatable
{
  public static final int SLOT_INPUT_A = 0;
  public static final int SLOT_INPUT_B = 1;
  public static final int SLOT_OUTPUT = 2;
  public static final int SLOT_FUEL = 3;

  public int burn_time;

  public int item_burn_time;

  public int progress;

  private boolean update_burn_times;

  static private final Set<Integer> IH_SLOTS_INPUT = ImmutableSet.of(SLOT_INPUT_A, SLOT_INPUT_B);
  static private final Set<Integer> IH_SLOTS_INPUT_FUEL = ImmutableSet.of(SLOT_INPUT_A, SLOT_INPUT_B, SLOT_FUEL);
  static private final Set<Integer> IH_SLOTS_OUTPUT = ImmutableSet.of(SLOT_OUTPUT);
  static private final Set<Integer> IH_SLOTS_OUTPUT_FUEL = ImmutableSet.of(SLOT_OUTPUT,SLOT_FUEL);
  static private final Set<Integer> IH_SLOTS_FUEL = ImmutableSet.of(SLOT_FUEL);
  
  
  private ItemHandler item_handler;
  private ItemHandlerFuel item_handler_fuel;
  
  public TileEntityAlloyFurnace()
  {
    burn_time = 0;
    item_burn_time = 0;
    progress = 0;
    update_burn_times = false;
    item_handler = new ItemHandler(getSizeInventory(),IH_SLOTS_INPUT,IH_SLOTS_OUTPUT);
    item_handler_fuel = new ItemHandlerFuel(this,getSizeInventory(),IH_SLOTS_INPUT_FUEL,IH_SLOTS_OUTPUT_FUEL,IH_SLOTS_FUEL);
  }
  
  @Override
  protected IItemHandler getItemHandler(EnumFacing side)
  {
    switch(side)
    {
      case UP:
        return item_handler;
      default:
        return item_handler_fuel;
    }
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
    if(tag.hasKey("CookTime"))
    {
      progress = tag.getInteger("CookTime");
    }
    if(tag.hasKey("ItemBurnTime"))
    {
      item_burn_time = tag.getInteger("ItemBurnTime");
    }
    if(world != null && !world.isRemote)
    {
      ((BlockAlloyFurnace)getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()), burn_time > 0);
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
    compound.setInteger("CookTime", progress);
    compound.setInteger("ItemBurnTime", item_burn_time);
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
  public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer)
  {
    return this.world.getTileEntity(getPos()) != this ? false : par1EntityPlayer.getDistanceSq(getPos()) <= 64.0D;
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
    switch(slot)
    {
      case SLOT_OUTPUT:
        return false;
      case SLOT_FUEL:
        return TileEntityFurnace.isItemFuel(stack);
    }
    return true;
  }

  @Override
  protected void updateClient()
  {

  }
  
  private boolean canOutput(IAlloyFurnaceRecipe recipe)
  {
    ItemStack output = recipe.getOutput();
    ItemStack inv_output = inventory[SLOT_OUTPUT];
    return inv_output.isEmpty() || (inv_output.isItemEqual(output) && inv_output.getCount() - output.getCount() <= inv_output.getMaxStackSize());
  }

  private void doSmelt(IAlloyFurnaceRecipe recipe,boolean reversed)
  {
    ItemStack output = recipe.getOutput();
    if(!canOutput(recipe))
    {
      progress = 0;
      return;
    }

    if(++progress == 400)
    {
      progress = 0;
      if(reversed)
      {
        decrStackSize(SLOT_INPUT_B, recipe.getInputA().getAmount());
        decrStackSize(SLOT_INPUT_A, recipe.getInputB().getAmount());
      } else
      {
        decrStackSize(SLOT_INPUT_A, recipe.getInputA().getAmount());
        decrStackSize(SLOT_INPUT_B, recipe.getInputB().getAmount());
      }
      if(inventory[SLOT_OUTPUT].isEmpty())
      {
        inventory[SLOT_OUTPUT] = output.copy();
      } else
      {
        inventory[SLOT_OUTPUT].grow(output.getCount());
      }
      updateInventoryItem(SLOT_OUTPUT);
      markDirty();
    }
  }
  
  @Override
  protected void updateServer()
  {
    int last_burn_time = burn_time;
    int last_progress = progress;
    int last_item_burn_time = item_burn_time;
    
    if(burn_time > 0)
    {
      --burn_time;
    }

    boolean reversed = false;
    IAlloyFurnaceRecipe recipe = null;
    if(!inventory[SLOT_INPUT_A].isEmpty() && !inventory[SLOT_INPUT_B].isEmpty())
    {
      recipe = AlloyFurnaceRecipeManager.instance.findRecipe(inventory[SLOT_INPUT_A], inventory[SLOT_INPUT_B]);
      if(recipe == null)
      {
        recipe = AlloyFurnaceRecipeManager.instance.findRecipe(inventory[SLOT_INPUT_B], inventory[SLOT_INPUT_A]);
        if(recipe != null)
        {
          reversed = true;
        }
      }
    }

    if(burn_time == 0 && recipe != null && canOutput(recipe))
    {
      ItemStack fuel = inventory[SLOT_FUEL];
      if(!fuel.isEmpty())
      {
        item_burn_time = burn_time = TileEntityFurnace.getItemBurnTime(fuel);
        if(burn_time > 0)
        {
          ItemStack container = fuel.getItem().getContainerItem(fuel);
          fuel.shrink(1);
          if(fuel.isEmpty())
          {
            inventory[SLOT_FUEL] = container;
          }
          updateInventoryItem(SLOT_FUEL);
        }
      }
    }

    if(burn_time > 0)
    {
      if(recipe != null)
      {
        doSmelt(recipe,reversed);
      } else
      {
        progress = 0;
      }
    } else
    {
      progress = 0;
    }
    
    if(last_burn_time != burn_time || update_burn_times)
    {
      if(last_burn_time*burn_time == 0)
      {
        ((BlockAlloyFurnace)getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()), burn_time > 0);
      }
      updateValue("BurnTime",burn_time);
    }

    if(last_item_burn_time != item_burn_time || update_burn_times)
    {
      updateValue("ItemBurnTime",item_burn_time);
    }
    update_burn_times = false;

    if(last_progress != progress)
    {
      updateValue("CookTime",progress);
    }
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

  @Optional.Method(modid = "Botania")
  @Override
  public boolean canSmelt()
  {
    if(!inventory[SLOT_INPUT_A].isEmpty() && !inventory[SLOT_INPUT_B].isEmpty())
    {
      IAlloyFurnaceRecipe recipe = AlloyFurnaceRecipeManager.instance.findRecipe(inventory[SLOT_INPUT_A], inventory[SLOT_INPUT_B]);
      if(recipe == null)
      {
        recipe = AlloyFurnaceRecipeManager.instance.findRecipe(inventory[SLOT_INPUT_B], inventory[SLOT_INPUT_A]);
      }
      if(recipe == null)
      {
        return false;
      }
      ItemStack output = recipe.getOutput();
      ItemStack inv_output = inventory[SLOT_OUTPUT];
      if(!inv_output.isEmpty() && (!inv_output.isItemEqual(output) || inv_output.getCount() - output.getCount() > inv_output.getMaxStackSize()))
      {
        return false;
      }
      return true;
    }
    return false;
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
    if(!world.isRemote)
    {
      burn_time = 200;
      item_burn_time = 199;
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
