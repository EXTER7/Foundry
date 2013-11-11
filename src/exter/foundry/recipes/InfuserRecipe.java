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
  static private List<InfuserRecipe> recipes = new ArrayList<InfuserRecipe>();
  
  @SideOnly(Side.CLIENT)
  static private Map<String,SubstanceGuiTexture> substance_textures = new HashMap<String,SubstanceGuiTexture>();
  
  private final FluidStack fluid;
  private final InfuserSubstance substance;
  private final FluidStack output;
  
  /**
   * Get the fluid required for the recipe.
   * @return FluidStack containing the required fluid.
   */
  public FluidStack GetFluid()
  {
    return fluid.copy();
  }

  /**
   * Get the substance required for the recipe.
   * @return Substance required for the recipe.
   */
  public InfuserSubstance GetSubstance()
  {
    return new InfuserSubstance(substance);
  }

  /**
   * Get the fluid produced by infusing.
   * @return FluidStack containing the fluid produced.
   */
  public FluidStack GetOutput()
  {
    return output.copy();
  }

  private InfuserRecipe(FluidStack result,FluidStack in_fluid,InfuserSubstance in_substance)
  {
    fluid = in_fluid;
    substance = in_substance;
    output = result;
  }

  /**
   * Register a texture for a substance
   * @param name Substance name. e.g: "Carbon"
   * @param tex Texture of the substance.
   */
  @SideOnly(Side.CLIENT)
  static public void RegisterSubstanceTexture(String name,SubstanceGuiTexture tex)
  {
    ModFoundry.log.info("Registering texture for " + name);
    substance_textures.put(name, tex);
  }
  
  /**
   * Get a substance's texture from it's name.
   * @param name Substance name. e.g: "Carbon".
   * @return The substance's texture.
   */
  @SideOnly(Side.CLIENT)
  static public SubstanceGuiTexture GetSubstanceTexture(String name)
  {
    return substance_textures.get(name);
  }
  
  /**
   * Register a Metal Infuser recipe.
   * @param result Fluid produced.
   * @param in_fluid Fluid required (fluid type and amount).
   * @param in_substance Substance required (substance type and amount).
   */
  static public void RegisterRecipe(FluidStack result,FluidStack in_fluid,InfuserSubstance in_substance)
  {
    recipes.add(new InfuserRecipe(result,in_fluid,in_substance));
  }
  
  /**
   * Find a infusing recipe given a FluidStack and a substance.
   * @param fluid FluidStack that contains the recipe's required fluid.
   * @param substance Substance that contains the recipe's required substance.
   * @return The infusing recipe, or null if no matching recipe.
   */
  static public InfuserRecipe FindRecipe(FluidStack fluid,InfuserSubstance substance)
  {
    if(fluid == null || substance == null)
    {
      return null;
    }
    for(InfuserRecipe ir:recipes)
    {
      if(fluid.containsFluid(ir.fluid) && substance.Contains(ir.substance))
      {
        return ir;
      }
    }
    return null;
  }
}
