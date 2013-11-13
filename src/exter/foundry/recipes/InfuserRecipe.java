package exter.foundry.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Metal Infuser recipe manager
 */
public class InfuserRecipe
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
  

  public InfuserRecipe(FluidStack result,FluidStack in_fluid,InfuserSubstance in_substance)
  {
    fluid = in_fluid.copy();
    substance = new InfuserSubstance(in_substance);
    output = result.copy();
  }
}
