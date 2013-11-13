package exter.foundry.recipes;

import ic2.api.recipe.ICannerBottleRecipeManager.Input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import exter.foundry.api.recipe.IInfuserRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Metal Infuser recipe manager
 */
public class InfuserRecipe implements IInfuserRecipe
{
 
  /**
   * Required fluid.
   */
  public final FluidStack fluid;

  /**
   * Substance required for the recipe.
   */
  public final InfuserSubstance substance;

  /**
   * Fluid produced.
   */
  public final FluidStack output;
  
  @Override
  public FluidStack GetInputFluid()
  {
    return fluid.copy();
  }
  
  @Override
  public String GetInputSubstanceType()
  {
    return substance.type;
  }
  
  @Override
  public int GetInputSubstanceAmount()
  {
    return substance.amount;
  }
  
  @Override
  public FluidStack GetOutput()
  {
    return output.copy();
  }

  public InfuserRecipe(FluidStack result,FluidStack in_fluid,InfuserSubstance in_substance)
  {
    fluid = in_fluid.copy();
    substance = new InfuserSubstance(in_substance);
    output = result.copy();
  }
}
