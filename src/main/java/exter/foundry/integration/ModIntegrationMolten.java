package exter.foundry.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IAlloyMixerRecipe;
import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AlloyingCrucibleRecipeManager;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// Compatibility with 144mB/ingot fluids.
public class ModIntegrationMolten implements IModIntegration
{
  private Map<String,String> liquid_map;
  
  static private final int gcd(int a, int b)
  {
    while(b != 0)
    {
      int t = b;
      b = a % b;
      a = t;
    }
    return a;
  }

  static private final int MOLTEN_INGOT_AMOUNT = 144;

  @Override
  public void onPreInit(Configuration config)
  {

  }

  @Override
  public void onInit()
  {
    
  }

  private FluidStack toMolten(FluidStack stack)
  {
    String mapped = liquid_map.get(stack.getFluid().getName());
    if(mapped != null)
    {
      Fluid mapped_fluid = FluidRegistry.getFluid(mapped);

      if(mapped.equals("glass"))
      {
        return new FluidStack(mapped_fluid, stack.amount);
      } else
      {
        return new FluidStack(
            mapped_fluid,
            FoundryMiscUtils.divCeil(stack.amount * MOLTEN_INGOT_AMOUNT, FoundryAPI.FLUID_AMOUNT_INGOT));
      }
    } else
    {
      return null;
    }
  }

  private void convertAlloyMixerRecipe(IAlloyMixerRecipe mix,int index,List<FluidStack> inputs,boolean has_mapped)
  {
    if(index == mix.getInputs().size())
    {
      if(!has_mapped)
      {
        return;
      }
      FluidStack[] in = new FluidStack[mix.getInputs().size()];
      for(int i = 0; i < in.length; i++)
      {
        in[i] = inputs.get(i).copy();
      }
      FluidStack result = mix.getOutput();
      result.amount *= FoundryAPI.FLUID_AMOUNT_INGOT;
      int div = result.amount;
      for(FluidStack f:in)
      {
        div = gcd(div,f.amount);
      }
      for(FluidStack f:in)
      {
        f.amount /= div;
      }
      result.amount /= div;
      
      AlloyMixerRecipeManager.instance.addRecipe(result, in);
      return;
    }

    FluidStack input = mix.getInputs().get(index).copy();
    input.amount *= FoundryAPI.FLUID_AMOUNT_INGOT;
    
    FluidStack molten = toMolten(input);
    if(molten != null)
    {
      List<FluidStack> in = new ArrayList<FluidStack>(inputs);
      in.add(molten);
      convertAlloyMixerRecipe(mix,index + 1,in,true);
    }
    List<FluidStack> in = new ArrayList<FluidStack>(inputs);
    in.add(input.copy());
    convertAlloyMixerRecipe(mix,index + 1,in,has_mapped);
  }
  
  private void convertAlloyMixerRecipe(IAlloyMixerRecipe mix)
  {
    convertAlloyMixerRecipe(mix,0,new ArrayList<FluidStack>(),false);
  }

  private void convertAlloyingCrucibleRecipe(IAlloyingCrucibleRecipe mix)
  {
    FluidStack out = mix.getOutput();
    
    FluidStack in_a = mix.getInputA();
    FluidStack in_b = mix.getInputB();
    FluidStack mapped_a = toMolten(in_a);
    FluidStack mapped_b = toMolten(in_b);
    if(mapped_a != null)
    {
      AlloyingCrucibleRecipeManager.instance.addRecipe(out, mapped_a,in_b);
    }
    if(mapped_b != null)
    {
      AlloyingCrucibleRecipeManager.instance.addRecipe(out, in_a,mapped_b);
    }
    if(mapped_a != null && mapped_b != null)
    {
      AlloyingCrucibleRecipeManager.instance.addRecipe(out, mapped_a,mapped_b);
    }
  }

  @Override
  public void onPostInit()
  {
    
  }
  
 
  @Override
  public void onAfterPostInit()
  {    
    liquid_map = new HashMap<String,String>();
    for(String name:LiquidMetalRegistry.instance.getFluidNames())
    {
      FluidLiquidMetal fluid = LiquidMetalRegistry.instance.getFluid(name);
      if(name.equals("Glass"))
      {
        if(FoundryConfig.recipe_glass)
        {
          if(FluidRegistry.getFluid("glass") != null)
          {
            liquid_map.put(fluid.getName(),"glass");
          }
        }
      } else if(!name.startsWith("Glass") && !fluid.special)
      {
        String molten_name = name.toLowerCase();
        if(FluidRegistry.getFluid(molten_name) != null)
        {
          liquid_map.put(fluid.getName(),molten_name);
        }
      }
    }

    //Add support for "molten" fluids to the Metal Caster.
    for(ICastingRecipe casting:new ArrayList<ICastingRecipe>(CastingRecipeManager.instance.getRecipes()))
    {
      FluidStack input = toMolten(casting.getInput());
      if(input != null)
      {
        CastingRecipeManager.instance.addRecipe(
            casting.getOutputMatcher(),
            input,
            casting.getMold(),
            casting.getInputExtra(),
            casting.getCastingSpeed());
      }
    }

    //Add support for "molten" fluids to the Casting Tables.
    for(ICastingTableRecipe casting:CastingTableRecipeManager.instance.getRecipes())
    {
      FluidStack input = toMolten(casting.getInput());
      if(input != null)
      {
        CastingTableRecipeManager.instance.addRecipe(
            casting.getOutputMatcher(),
            input,
            casting.getTableType());
      }
    }

    //Add support for "molten" fluids to the Atomizer.
    for(IAtomizerRecipe atomize:new ArrayList<IAtomizerRecipe>(AtomizerRecipeManager.instance.getRecipes()))
    {
      FluidStack input = toMolten(atomize.getInput());
      if(input != null)
      {
        AtomizerRecipeManager.instance.addRecipe(
            atomize.getOutputMatcher(),
            input);
      }
    }

    //Add support for "molten" fluid inputs to Alloying Crucible recipes.
    for(IAlloyingCrucibleRecipe mix:new ArrayList<IAlloyingCrucibleRecipe>(AlloyingCrucibleRecipeManager.instance.getRecipes()))
    {
      convertAlloyingCrucibleRecipe(mix);
    }

    //Add support for "molten" fluid inputs to Alloy Mixer recipes.
    for(IAlloyMixerRecipe mix:new ArrayList<IAlloyMixerRecipe>(AlloyMixerRecipeManager.instance.getRecipes()))
    {
      convertAlloyMixerRecipe(mix);
    }
  }

  @Override
  public String getName()
  {
    return "MoltenMetalFluids";
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPreInit()
  {
    
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientInit()
  {
    
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPostInit()
  {
    
  }
}
