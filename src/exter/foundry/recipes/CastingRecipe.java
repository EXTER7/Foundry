package exter.foundry.recipes;

import java.util.ArrayList;
import java.util.List;

import exter.foundry.api.recipe.ICastingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Metal Caster recipe manager
 */
public class CastingRecipe implements ICastingRecipe
{
  public final FluidStack fluid;
  public final ItemStack mold;
  public final ItemStack extra;
  
  private final ItemStack output;
  private final String output_oredict;
  
  @Override
  public FluidStack GetInputFluid()
  {
    return fluid.copy();
  }

  @Override
  public ItemStack GetInputMold()
  {
    return mold.copy();
  }

  @Override
  public ItemStack GetInputExtra()
  {
    return ItemStack.copyItemStack(extra);
  }

  @Override
  public Object GetOutput()
  {
    if(output_oredict != null)
    {
      return output_oredict;
    } else
    {
      return output.copy();
    }
  }

  /**
   * Get item produced by casting.
   * @return ItemStack containing the item produced.
   */
  public ItemStack GetOutputItem()
  {
    if(output_oredict != null)
    {
      List<ItemStack> ores = OreDictionary.getOres(output_oredict);
      if(ores != null && ores.size() > 0)
      {
        ItemStack out = ores.get(0).copy();
        out.stackSize = 1;
        return out;
      } else
      {
        return null;
      }
    } else
    {
      return output.copy();
    }
  }

  public CastingRecipe(ItemStack result,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
  {
    output = result.copy();
    output_oredict = null;
    fluid = in_fluid.copy();
    mold = in_mold.copy();
    extra = ItemStack.copyItemStack(in_extra);
  }

  public CastingRecipe(String result_name,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
  {
    output_oredict = result_name;
    output = null;
    fluid = in_fluid.copy();
    mold = in_mold.copy();
    extra = ItemStack.copyItemStack(in_extra);
  }
}
