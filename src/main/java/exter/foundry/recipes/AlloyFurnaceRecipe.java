package exter.foundry.recipes;

import exter.foundry.api.FoundryUtils;
import exter.foundry.api.orestack.OreStack;
import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import net.minecraft.item.ItemStack;

/*
 * Alloy Mixer recipe manager
 */
public class AlloyFurnaceRecipe implements IAlloyFurnaceRecipe
{
  
  public Object input_a;
  public Object input_b;
  public ItemStack output;

  @Override
  public Object getInputA()
  {
    if(input_a instanceof ItemStack)
    {
      return ((ItemStack)input_a).copy();
    }    
    return input_a;
  }

  @Override
  public Object getInputB()
  {
    if(input_b instanceof ItemStack)
    {
      return ((ItemStack)input_b).copy();
    }    
    return input_b;
  }

  @Override
  public ItemStack getOutput()
  {
    return output.copy();
  }
  
  public AlloyFurnaceRecipe(ItemStack out,Object in_a,Object in_b)
  {
    if(out == null)
    {
      throw new IllegalArgumentException("Alloy recipe output cannot be null");
    }
    output = out.copy();

    if(in_a instanceof ItemStack)
    {
      input_a = ((ItemStack)in_a).copy();
    } else if(in_a instanceof OreStack)
    {
      input_a = new OreStack((OreStack)in_a);
    } else
    {
      throw new IllegalArgumentException("Alloy recipe input must be an ItemStack, or an OreStack");
    }

    if(in_b instanceof ItemStack)
    {
      input_b = ((ItemStack)in_b).copy();
    } else if(in_b instanceof OreStack)
    {
      input_b = new OreStack((OreStack)in_b);
    } else
    {
      throw new IllegalArgumentException("Alloy recipe input must be an ItemStack, or an OreStack");
    }
  }

  @Override
  public boolean matchesRecipe(ItemStack in_a,ItemStack in_b)
  {
    return FoundryUtils.isItemMatch(in_a, input_a) && in_a.stackSize >= FoundryUtils.getStackSize(input_a)
        && FoundryUtils.isItemMatch(in_b, input_b) && in_b.stackSize >= FoundryUtils.getStackSize(input_b);
  }
}
