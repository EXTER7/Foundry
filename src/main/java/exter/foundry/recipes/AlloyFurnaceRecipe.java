package exter.foundry.recipes;

import exter.foundry.api.orestack.OreStack;
import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.util.FoundryMiscUtils;
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
  public Object GetInputA()
  {
    if(input_a instanceof ItemStack)
    {
      return ((ItemStack)input_a).copy();
    }    
    return input_a;
  }

  @Override
  public Object GetInputB()
  {
    if(input_b instanceof ItemStack)
    {
      return ((ItemStack)input_b).copy();
    }    
    return input_b;
  }

  @Override
  public ItemStack GetOutput()
  {
    return output.copy();
  }
  
  public AlloyFurnaceRecipe(ItemStack out,Object in_a,Object in_b)
  {
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
  public boolean MatchesRecipe(ItemStack in_a,ItemStack in_b)
  {
    return FoundryMiscUtils.IsItemMatch(in_a, input_a) && in_a.stackSize >= FoundryMiscUtils.GetStackSize(input_a)
        && FoundryMiscUtils.IsItemMatch(in_b, input_b) && in_b.stackSize >= FoundryMiscUtils.GetStackSize(input_b);
  }
}
