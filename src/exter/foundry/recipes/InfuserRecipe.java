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

public class InfuserRecipe
{
  static private List<InfuserRecipe> recipes = new ArrayList<InfuserRecipe>();
  
  @SideOnly(Side.CLIENT)
  static private Map<String,SubstanceGuiTexture> substance_textures = new HashMap<String,SubstanceGuiTexture>();
  
  private final FluidStack fluid;
  private final InfuserSubstance substance;
  private final FluidStack output;
  
  public FluidStack GetFluid()
  {
    return fluid.copy();
  }

  public InfuserSubstance GetSubstance()
  {
    return new InfuserSubstance(substance);
  }

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

  @SideOnly(Side.CLIENT)
  static public void RegisterSubstanceTexture(String name,SubstanceGuiTexture tex)
  {
    ModFoundry.log.info("Registering texture for " + name);
    substance_textures.put(name, tex);
  }
  
  @SideOnly(Side.CLIENT)
  static public SubstanceGuiTexture GetSubstanceTexture(String name)
  {
    return substance_textures.get(name);
  }
  
  static public void RegisterRecipe(FluidStack result,FluidStack in_fluid,InfuserSubstance in_substance)
  {
    recipes.add(new InfuserRecipe(result,in_fluid,in_substance));
  }
  
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
