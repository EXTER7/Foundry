package exter.foundry.tileentity.itemhandler;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.tileentity.TileEntityFoundry;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class ItemHandlerFuel extends TileEntityFoundry.ItemHandler
{
  protected final ImmutableSet<Integer> fuel_slots;
  
  public ItemHandlerFuel(TileEntityFoundry te,int slots, Set<Integer> insert_slots, Set<Integer> extract_slots,Set<Integer> fuel_slots)
  {
    te.super(slots, insert_slots, extract_slots);
    this.fuel_slots = ImmutableSet.copyOf(fuel_slots);
  }
  
  @Override
  protected boolean canInsert(int slot,ItemStack stack)
  {
    if(fuel_slots.contains(slot))
    {
      return TileEntityFurnace.isItemFuel(stack);
    } else
    {
      return true;
    }
  }

  @Override
  protected boolean canExtract(int slot)
  {
    if(fuel_slots.contains(slot))
    {
      return !TileEntityFurnace.isItemFuel(getStackInSlot(slot));
    } else
    {
      return true;
    }
  }
}
