package exter.foundry.recipes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Metal Smelter recipe manager
 */
public class MeltingRecipe
{
  
  
  /**
   * Produced fluid and amount.
   */
  public final FluidStack fluid;

  /**
   * Ore dictionary name of the require item.
   */
  public final String solid;
  
  
  public MeltingRecipe(String solid_name,FluidStack fluid_stack)
  {
    solid = solid_name;
    fluid = fluid_stack.copy();
  }
  
}
