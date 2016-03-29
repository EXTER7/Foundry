package exter.foundry.tileentity;

import exter.foundry.api.recipe.IMoldRecipe;
import exter.foundry.block.BlockAlloyFurnace;
import exter.foundry.block.BlockMoldStation;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;

//@Optional.Interface(iface = "vazkii.botania.api.item.IExoflameHeatable", modid = "Botania")
public class TileEntityMoldStation extends TileEntityFoundry/*,IExoflameHeatable*/
{

  public static final int SLOT_BLOCK = 0;
  public static final int SLOT_CLAY = 1;
  public static final int SLOT_OUTPUT = 2;
  public static final int SLOT_FUEL = 3;

  private int burn_time;

  private int item_burn_time;

  private int progress;
  
  private boolean has_block;

  private boolean update_burn_times;
  
  private int[] grid;

  private IMoldRecipe current_recipe;

  public TileEntityMoldStation()
  {
    burn_time = 0;
    item_burn_time = 0;
    progress = 0;
    update_burn_times = false;
    grid = new int[36];
    has_block = false;
    current_recipe = null;
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
    
    for(int i = 0; i < 25; i++)
    {
      if(tag.hasKey("RecipeGrid_" + i))
      {
        grid[i] = tag.getInteger("RecipeGrid_" + i);
      }
    }
    
    if(tag.hasKey("HasBlock"))
    {
      has_block = tag.getBoolean("HasBlock");
    }
    
    if(worldObj != null && !worldObj.isRemote)
    {
      ((BlockMoldStation)getBlockType()).setMachineState(worldObj, getPos(), worldObj.getBlockState(getPos()), burn_time > 0);
    }
  }

  public int getBurnTime()
  {
    return burn_time;
  }

  public int getItemBurnTime()
  {
    return item_burn_time;
  }

  public int getProgress()
  {
    return progress;
  }

  public boolean hasBlock()
  {
    return has_block;
  }

  public int getGridSlot(int slot)
  {
    return grid[slot];
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
  {
    return oldState.getBlock() != newSate.getBlock();
  }

  @Override
  public void writeToNBT(NBTTagCompound tag)
  {
    super.writeToNBT(tag);
    tag.setInteger("BurnTime", burn_time);
    tag.setInteger("CookTime", progress);
    tag.setInteger("ItemBurnTime", item_burn_time);
    for(int i = 0; i < 25; i++)
    {
      tag.setInteger("RecipeGrid_" + i,grid[i]);
    }
    tag.setBoolean("HasBlock", has_block);
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

  /**
   * Returns true if automation can insert the given item in the given slot from
   * the given side. Args: Slot, item, side
   */
  public boolean canInsertItem(int par1, ItemStack par2ItemStack, EnumFacing side)
  {
    return this.isItemValidForSlot(par1, par2ItemStack);
  }

  @Override
  protected void updateClient()
  {

  }
  
  private boolean canOutput(ItemStack output,int slot)
  {
    ItemStack inv_output = inventory[slot];
    return output == null || inv_output == null || (inv_output.isItemEqual(output) && inv_output.stackSize - output.stackSize <= inv_output.getMaxStackSize());
  }

  private ItemStack getCarvedClay()
  {
    int amount = 0;
    for(int g:grid)
    {
      amount += g;
    }
    amount /= 10;
    
    if(amount == 0)
    {
      return null;
    }
    return FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY_SMALL,amount);
  }
  private boolean canRecipeOutput(IMoldRecipe recipe)
  {
    ItemStack output = recipe.getOutput();
    ItemStack clay = getCarvedClay();
    
    return canOutput(output,SLOT_OUTPUT) && canOutput(clay,SLOT_CLAY);
  }

  private void doSmelt(IMoldRecipe recipe)
  {
    ItemStack output = recipe.getOutput();
    ItemStack clay = getCarvedClay();
    
    if(!canOutput(output,SLOT_OUTPUT) || !canOutput(clay,SLOT_CLAY))
    {
      progress = 0;
      return;
    }

    if(++progress == 200)
    {
      progress = 0;
      if(inventory[SLOT_OUTPUT] == null)
      {
        inventory[SLOT_OUTPUT] = output.copy();
      } else
      {
        inventory[SLOT_OUTPUT].stackSize += output.stackSize;
      }
      updateInventoryItem(SLOT_OUTPUT);
      has_block = false;
      updateValue("HasBlock", has_block);
    }
  }
  
  @Override
  protected void updateServer()
  {
    int last_burn_time = burn_time;
    int last_progress = progress;
    int last_item_burn_time = item_burn_time;
    
    if(!has_block)
    {
      ItemStack block = getStackInSlot(SLOT_BLOCK);
      if(block != null)
      {
        
        decrStackSize(SLOT_BLOCK, 1);
        has_block = true;
        updateValue("HasBlock",has_block);
      }
    }
    
    if(burn_time > 0)
    {
      --burn_time;
    }
    if(has_block && progress >= 0)
    {
      if(burn_time == 0 && current_recipe != null && canRecipeOutput(current_recipe))
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
            updateInventoryItem(SLOT_FUEL);
          }
        }
      }

      if(burn_time > 0)
      {
        if(!current_recipe.matches(grid))
        {
          current_recipe = null;
        }
        if(current_recipe != null)
        {
          doSmelt(current_recipe);
        } else
        {
          progress = 0;
        }
      } else
      {
        progress = 0;
      }
    }
    
    if(last_burn_time != burn_time || update_burn_times)
    {
      if(last_burn_time*burn_time == 0)
      {
        ((BlockAlloyFurnace)getBlockType()).setMachineState(worldObj, getPos(), worldObj.getBlockState(getPos()), burn_time > 0);
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
    current_recipe = null;
  }

  public void carve(int x1,int y1, int x2, int y2)
  {
    if(worldObj.isRemote)
    {
      NBTTagCompound tag = new NBTTagCompound();
      for(int j = y1; j <= y2; j++)
      {
        for(int i = x1; i <= x2; i++)
        {
          int slot = j * 6 + i;
          if(grid[slot] < 4)
          {
            grid[slot]++;
            tag.setInteger("RecipeGrid_" + slot,grid[slot]);
          }
        }
      }
      sendToServer(tag);
    }
  }

  public void mend(int x1,int y1, int x2, int y2)
  {
    if(worldObj.isRemote)
    {
      NBTTagCompound tag = new NBTTagCompound();
      for(int j = y1; j <= y2; j++)
      {
        for(int i = x1; i <= x2; i++)
        {
          int slot = j * 6 + i;
          if(grid[slot] > 0)
          {
            grid[slot]--;
            tag.setInteger("RecipeGrid_" + slot, grid[slot]);
          }
        }
      }
      sendToServer(tag);
    }
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
