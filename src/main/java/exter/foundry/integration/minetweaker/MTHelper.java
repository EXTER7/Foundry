package exter.foundry.integration.minetweaker;

import exter.foundry.api.orestack.OreStack;
import exter.foundry.api.substance.InfuserSubstance;
import exter.foundry.integration.minetweaker.orestack.IOreStack;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.UniqueIdentifier;

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
  
  static public Object getIngredient(IIngredient ingr)
  {
    if(ingr instanceof IItemStack)
    {
      return MineTweakerMC.getItemStack((IItemStack)ingr);
    }
    if(ingr instanceof IOreDictEntry)
    {
      return (String)ingr.getInternal();
    }
    if(ingr instanceof IOreStack)
    {
      return ((IOreStack)ingr).getOreStack();
    }
    return null;
  }
}
