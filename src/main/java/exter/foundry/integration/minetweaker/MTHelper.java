package exter.foundry.integration.minetweaker;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import exter.foundry.api.orestack.OreStack;
import exter.foundry.api.substance.InfuserSubstance;
import minetweaker.api.item.IIngredient;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class MTHelper
{
  static public String getDescription(Object obj)
  {
    if(obj == null)
    {
      return "NULL";
    }
    if(obj instanceof String)
    {
      return String.format("O(%s)", (String) obj);
    } else if(obj instanceof OreStack)
    {
      OreStack stack = (OreStack) obj;
      return String.format("O(%s,%d)", stack.name, stack.amount);
    } else if(obj instanceof ItemStack)
    {
      ItemStack stack = (ItemStack) obj;
      UniqueIdentifier item = GameRegistry.findUniqueIdentifierFor(stack.getItem());
      return String.format("I(%s:%s:%d,%d)", item.modId, item.name, stack.getItemDamage(), stack.stackSize);
    } else if(obj instanceof Item)
    {
      UniqueIdentifier item = GameRegistry.findUniqueIdentifierFor((Item) obj);
      return String.format("I(%s:%s)", item.modId, item.name);
    } else if(obj instanceof Block)
    {
      UniqueIdentifier item = GameRegistry.findUniqueIdentifierFor((Block) obj);
      return String.format("I(%s:%s)", item.modId, item.name);
    } else if(obj instanceof FluidStack)
    {
      FluidStack stack = (FluidStack) obj;
      return String.format("F(%s,%s)", stack.getFluid().getName(), stack.amount);
    } else if(obj instanceof InfuserSubstance)
    {
      InfuserSubstance stack = (InfuserSubstance) obj;
      return String.format("S(%s,%s)", stack.type, stack.amount);
    } else
    {
      throw new IllegalArgumentException("Invalid object class.");
    }
  }
  
  static public Object getIngredient(IIngredient input)
  {
    Object obj = input.getInternal();
    if((obj instanceof String) && (input instanceof IOreDictEntry))
    {
      if(input.getAmount() > 1)
      {
        return new OreStack((String)obj,input.getAmount());
      }
      return obj;
    }
    if(obj instanceof ItemStack)
    {
      return obj;
    }
    return null;
  }
}
