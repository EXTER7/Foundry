package exter.foundry.tileentity;

//import vazkii.botania.api.item.IExoflameHeatable;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.block.BlockAlloyFurnace;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.FMLCommonHandler;

//@Optional.Interface(iface = "vazkii.botania.api.item.IExoflameHeatable", modid = "Botania")
public class TileEntityAlloyFurnace extends TileEntityFoundry implements ISidedInventory/*,IExoflameHeatable*/
{

  public static final int SLOT_INPUT_A = 0;
  public static final int SLOT_INPUT_B = 1;
  public static final int SLOT_OUTPUT = 2;
  public static final int SLOT_FUEL = 3;
  private ItemStack[] inventory;

  public int burn_time;

  public int item_burn_time;

  public int progress;

  private boolean update_burn_times;

  private static final int[] SLOTS_TOP = new int[] { SLOT_INPUT_A, SLOT_INPUT_B };
  private static final int[] SLOTS_BOTTOM = new int[] { SLOT_OUTPUT, SLOT_FUEL };
  private static final int[] SLOTS_SIDES = new int[] { SLOT_FUEL };

  public TileEntityAlloyFurnace()
  {
    inventory = new ItemStack[4];
    burn_time = 0;
    item_burn_time = 0;
    progress = 0;
    update_burn_times = false;
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
      ItemStack stack;

      if(inventory[slot].stackSize <= amount)
      {
        stack = inventory[slot];
        inventory[slot] = null;
        return stack;
      } else
      {
        stack = inventory[slot].splitStack(amount);

        if(inventory[slot].stackSize == 0)
        {
          inventory[slot] = null;
        }

        return stack;
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
      ItemStack itemstack = inventory[slot];
      inventory[slot] = null;
      return itemstack;
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
  }

  @Override
  public void readFromNBT(NBTTagCompound tag)
  {
    super.readFromNBT(tag);
    int last_burn_time = burn_time;

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
    if(FMLCommonHandler.instance().getEffectiveSide().isClient())
    {
      if(last_burn_time*burn_time == 0)
      {
        ((BlockAlloyFurnace)getBlockType()).setFurnaceState(worldObj, getPos(), worldObj.getBlockState(getPos()), burn_time > 0);
      }
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound tag)
  {
    super.writeToNBT(tag);
    tag.setInteger("BurnTime", burn_time);
    tag.setInteger("CookTime", progress);
    tag.setInteger("ItemBurnTime", item_burn_time);
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
  public int[] getSlotsForFace(EnumFacing side)
  {
    switch(side)
    {
      case DOWN:
        return SLOTS_BOTTOM;
      case UP:
        return SLOTS_TOP;
      default:
        return SLOTS_SIDES;
    }
  }

  /**
   * Returns true if automation can insert the given item in the given slot from
   * the given side. Args: Slot, item, side
   */
  public boolean canInsertItem(int par1, ItemStack par2ItemStack, EnumFacing side)
  {
    return this.isItemValidForSlot(par1, par2ItemStack);
  }

  @Override
  public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side)
  {
    return side != EnumFacing.UP || slot != SLOT_INPUT_A || slot != SLOT_INPUT_B || stack.getItem() == Items.bucket;
  }

  @Override
  protected void UpdateEntityClient()
  {

  }

  private void DoSmelt(IAlloyFurnaceRecipe recipe,boolean reversed)
  {
    ItemStack output = recipe.getOutput();
    ItemStack inv_output = inventory[SLOT_OUTPUT];
    if(inv_output != null && (!inv_output.isItemEqual(output) || inv_output.stackSize - output.stackSize > inv_output.getMaxStackSize()))
    {
      progress = 0;
      return;
    }

    if(++progress == 400)
    {
      progress = 0;
      if(reversed)
      {
        decrStackSize(SLOT_INPUT_B, FoundryUtils.getStackSize(recipe.getInputA()));
        decrStackSize(SLOT_INPUT_A, FoundryUtils.getStackSize(recipe.getInputB()));
      } else
      {
        decrStackSize(SLOT_INPUT_A, FoundryUtils.getStackSize(recipe.getInputA()));
        decrStackSize(SLOT_INPUT_B, FoundryUtils.getStackSize(recipe.getInputB()));
      }
      UpdateInventoryItem(SLOT_INPUT_A);
      UpdateInventoryItem(SLOT_INPUT_B);
      if(inventory[SLOT_OUTPUT] == null)
      {
        inventory[SLOT_OUTPUT] = output.copy();
      } else
      {
        inventory[SLOT_OUTPUT].stackSize += output.stackSize;
      }
      UpdateInventoryItem(SLOT_OUTPUT);
      markDirty();
    }
  }
  
  @Override
  protected void UpdateEntityServer()
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
    if(inventory[SLOT_INPUT_A] != null && inventory[SLOT_INPUT_B] != null)
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

    if(burn_time == 0 && recipe != null)
    {
      item_burn_time = burn_time = TileEntityFurnace.getItemBurnTime(inventory[SLOT_FUEL]);
      if(burn_time > 0)
      {
        if(inventory[SLOT_FUEL] != null)
        {
          if(--inventory[SLOT_FUEL].stackSize == 0)
          {
            inventory[SLOT_FUEL] = inventory[SLOT_FUEL].getItem().getContainerItem(inventory[SLOT_FUEL]);
          }
          UpdateInventoryItem(SLOT_FUEL);
        }
      }
    }

    if(burn_time > 0)
    {
      if(recipe != null)
      {
        DoSmelt(recipe,reversed);
      }
    } else
    {
      progress = 0;
    }
    
    if(last_burn_time != burn_time || update_burn_times)
    {
      if(last_burn_time*burn_time == 0)
      {
        ((BlockAlloyFurnace)getBlockType()).setFurnaceState(worldObj, getPos(), worldObj.getBlockState(getPos()), burn_time > 0);
      }
      UpdateValue("BurnTime",burn_time);
    }

    if(last_item_burn_time != item_burn_time || update_burn_times)
    {
      UpdateValue("ItemBurnTime",item_burn_time);
    }
    update_burn_times = false;

    if(last_progress != progress)
    {
      UpdateValue("CookTime",progress);
    }
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


//  @Optional.Method(modid = "Botania")
//  @Override
//  public boolean canSmelt()
//  {
//    if(inventory[SLOT_INPUT_A] != null && inventory[SLOT_INPUT_B] != null)
//    {
//      IAlloyFurnaceRecipe recipe = AlloyFurnaceRecipeManager.instance.findRecipe(inventory[SLOT_INPUT_A], inventory[SLOT_INPUT_B]);
//      if(recipe == null)
//      {
//        recipe = AlloyFurnaceRecipeManager.instance.findRecipe(inventory[SLOT_INPUT_B], inventory[SLOT_INPUT_A]);
//      }
//      if(recipe == null)
//      {
//        return false;
//      }
//      ItemStack output = recipe.getOutput();
//      ItemStack inv_output = inventory[SLOT_OUTPUT];
//      if(inv_output != null && (!inv_output.isItemEqual(output) || inv_output.stackSize - output.stackSize > inv_output.getMaxStackSize()))
//      {
//        return false;
//      }
//      return true;
//    }
//    return false;
//  }
//
//  @Optional.Method(modid = "Botania")
//  @Override
//  public int getBurnTime()
//  {
//    return burn_time <= 1 ? 0 : burn_time - 1;
//  }
//
//  @Optional.Method(modid = "Botania")
//  @Override
//  public void boostBurnTime()
//  {
//    if(!worldObj.isRemote)
//    {
//      burn_time = 200;
//      item_burn_time = 199;
//      update_burn_times = true;
//      markDirty();
//    }
//  }
//
//  @Optional.Method(modid = "Botania")
//  @Override
//  public void boostCookTime()
//  {
//
//  }
}
