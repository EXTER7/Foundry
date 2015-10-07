package exter.foundry.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tconstruct.TConstruct;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.AlloyMix;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.Smeltery;
import cpw.mods.fml.common.Loader;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import mantle.utils.ItemMetaWrapper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
public class ModIntegrationTiCon extends ModIntegration
{
  private Map<String,String> liquid_map;
  private Map<String,String> reverse_liquid_map;
  static private final int GCD(int a, int b)
  {
    while(b != 0)
    {
      int t = b;
      b = a % b;
      a = t;
    }
    return a;
  }
  
  static private int DivCeil(int a,int b)
  {
    return a / b + ((a % b == 0) ? 0 : 1);
  }


  static private final int INGOT_GCD = GCD(TConstruct.ingotLiquidValue,FoundryAPI.FLUID_AMOUNT_INGOT);

  public ModIntegrationTiCon(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {

  }

  @Override
  public void OnInit()
  {
  }

  private void CreateAlloyRecipe(AlloyMix mix,int index,List<FluidStack> inputs)
  {
    if(index == mix.mixers.size())
    {
      FluidStack[] in = new FluidStack[mix.mixers.size()];
      in = inputs.toArray(in);
      FluidStack result = new FluidStack(mix.result.getFluid(),mix.result.amount * 9 / INGOT_GCD);
      AlloyMixerRecipeManager.instance.addRecipe(result, in);
      return;
    }

    FluidStack ing = mix.mixers.get(index);
    String mapped = liquid_map.get(ing.getFluid().getName());
    if(mapped != null)
    {
      List<FluidStack> in = new ArrayList<FluidStack>(inputs);
      in.add(new FluidStack( // Convert TiCon Fluid Stack to Foundry Fluid Stack
          LiquidMetalRegistry.instance.getFluid(mapped),
          ing.amount * 9 * TConstruct.ingotLiquidValue / (FoundryAPI.FLUID_AMOUNT_INGOT * INGOT_GCD)));
      CreateAlloyRecipe(mix,index + 1,in);
    }
    List<FluidStack> in = new ArrayList<FluidStack>(inputs);
    FluidStack fl = ing;
    in.add(new FluidStack(fl.getFluid(),fl.amount * 9 / INGOT_GCD));
    CreateAlloyRecipe(mix,index + 1,in);
  }
  
  private void CreateAlloyRecipe(AlloyMix mix)
  {
    if(mix.mixers.size() > 4)
    {
      return;
    }
    CreateAlloyRecipe(mix,0,new ArrayList<FluidStack>());
  }

  @Override
  public void OnPostInit()
  {
    
  }
  
 
  @Override
  public void OnAfterPostInit()
  {
    if(!Loader.isModLoaded("TConstruct"))
    {
      is_loaded = false;
      return;
    }

    ItemStack ingot_cast = ItemStack.copyItemStack(TConstructRegistry.getItemStack("ingotCast"));
    
    liquid_map = new HashMap<String,String>();
    for(String name:LiquidMetalRegistry.instance.GetFluidNames())
    {
      if(name.equals("Glass"))
      {
        if(FoundryConfig.recipe_glass)
        {
          liquid_map.put("glass.molten", "Glass");
        }
      } else if(!name.startsWith("Glass") && !name.startsWith("Redstone"))
      {
        String tic_name = name.toLowerCase() + ".molten";
        if(FluidRegistry.getFluid(tic_name) != null)
        {
          liquid_map.put(tic_name, name);
        }
      }
    }

    reverse_liquid_map = new HashMap<String,String>();
    for(Map.Entry<String,String> e:liquid_map.entrySet())
    {
      reverse_liquid_map.put(
          LiquidMetalRegistry.instance.getFluid(e.getValue()).getName(),
          e.getKey());
    }

    //Add support for TiCon's fluids to the Metal Caster recipes.
    for(ICastingRecipe casting:new ArrayList<ICastingRecipe>(CastingRecipeManager.instance.getRecipes()))
    {
      String mapped = reverse_liquid_map.get(casting.getInput().getFluid().getName());
      if(mapped != null)
      {
        Fluid mapped_fluid = FluidRegistry.getFluid(mapped);
        if(mapped_fluid != null)
        {
          CastingRecipeManager.instance.addRecipe(
              casting.getOutput(),
              new FluidStack(
                  mapped_fluid,
                  DivCeil(casting.getInput().amount * TConstruct.ingotLiquidValue, FoundryAPI.FLUID_AMOUNT_INGOT)),
              casting.getMold(),
              casting.getInputExtra());
        }
      }
    }

    
    //Convert TiCon Smeltery recipes to Foundry ICF melting recipes (except those that have an existing recipe).
    for(ItemMetaWrapper item : Smeltery.getSmeltingList().keySet())
    {
      ItemStack stack = new ItemStack(item.item, 1, item.meta);
      if(MeltingRecipeManager.instance.findRecipe(stack) == null)
      {
        FluidStack result = Smeltery.getSmelteryResult(stack);
        String mapped = liquid_map.get(result.getFluid().getName());
        if(mapped != null)
        {
          FluidStack mapped_liquid;
          
          if(mapped.equals("Glass"))
          {
            mapped_liquid = new FluidStack(
                LiquidMetalRegistry.instance.getFluid(mapped),
                result.amount);
          } else
          {
            mapped_liquid = new FluidStack(
                LiquidMetalRegistry.instance.getFluid(mapped),
                DivCeil(result.amount * FoundryAPI.FLUID_AMOUNT_INGOT, TConstruct.ingotLiquidValue));
          }
          if(mapped_liquid.amount <= 6000)
          {
            MeltingRecipeManager.instance.addRecipe(stack, mapped_liquid);
          }
        } else
        {
          if(result.amount <= 6000)
          {
            int temp = Smeltery.getLiquifyTemperature(stack) + 274;
            if(temp < 350)
            {
              temp = 350;
            }
            MeltingRecipeManager.instance.addRecipe(stack, result, temp);
          }
        }
      }
    }
    
    //Convert TiCon Alloy recipes Foundry Alloy Mixer recipes.
    for(AlloyMix mix:Smeltery.getAlloyList())
    {
      String mapped_result = liquid_map.get(mix.result.getFluid().getName());
      if(mapped_result == null)
      {
        CreateAlloyRecipe(mix);
      }
    }
    
    LiquidCasting table_casting = TConstructRegistry.getTableCasting();
    LiquidCasting basin_casting = TConstructRegistry.getBasinCasting();
    
    //Convert TiCon table casting recipes to Foundry Metal Caster recipes.
    ItemStack block_mold = FoundryItems.Mold(ItemMold.MOLD_BLOCK);
    for(tconstruct.library.crafting.CastingRecipe casting:table_casting.getCastingRecipes())
    {
      if(casting.cast != null && !casting.consumeCast)
      {
        if(!CastingRecipeManager.instance.isItemMold(casting.cast))
        {
          //Register the cast as a mold
          CastingRecipeManager.instance.addMold(casting.cast);
        }
        
        String mapped = liquid_map.get(casting.castingMetal.getFluid().getName());
        FluidStack mapped_liquid = null;
        if(mapped != null)
        {
          mapped_liquid = new FluidStack(
              LiquidMetalRegistry.instance.getFluid(mapped),
              DivCeil(casting.castingMetal.amount * FoundryAPI.FLUID_AMOUNT_INGOT, TConstruct.ingotLiquidValue));
        }
        if(casting.cast.isItemEqual(ingot_cast))
        {
          ItemStack ingot_mold = FoundryItems.Mold(ItemMold.MOLD_INGOT);
          if(casting.castingMetal.amount <= 6000)
          {
            CastingRecipeManager.instance.addRecipe(casting.output, casting.castingMetal, ingot_mold, null);
          }
        } else if(mapped_liquid != null)
        {
          if(mapped_liquid.amount <= 6000)
          {
            CastingRecipeManager.instance.addRecipe(casting.output, mapped_liquid, casting.cast, null);
          }
        }
        if(casting.castingMetal.amount <= 6000)
        {
          CastingRecipeManager.instance.addRecipe(casting.output, casting.castingMetal, casting.cast, null);
        }
      }
    }
    for(tconstruct.library.crafting.CastingRecipe casting:basin_casting.getCastingRecipes())
    {
      if(casting.castingMetal.amount <= 6000 && casting.cast == null)
      {
        CastingRecipeManager.instance.addRecipe(casting.output, casting.castingMetal, block_mold, null);
      }
    }
    
    //Add support for Foundry's fluid to the TiCon casting table.
    List<tconstruct.library.crafting.CastingRecipe> recipes = new ArrayList<tconstruct.library.crafting.CastingRecipe>();
    for(tconstruct.library.crafting.CastingRecipe casting : table_casting.getCastingRecipes())
    {
      if(casting.cast != null && casting.cast.getItem() == Items.bucket)
      {
        continue;
      }
      String mapped = liquid_map.get(casting.castingMetal.getFluid().getName());
      if(mapped == null)
      {
        continue;
      }
      FluidStack mapped_liquid = new FluidStack(
          LiquidMetalRegistry.instance.getFluid(mapped),
          mapped.equals("Glass") ?
              casting.castingMetal.amount :
              DivCeil(casting.castingMetal.amount * FoundryAPI.FLUID_AMOUNT_INGOT, TConstruct.ingotLiquidValue));
      tconstruct.library.crafting.CastingRecipe recipe = new tconstruct.library.crafting.CastingRecipe(
          casting.output,
          mapped_liquid,
          casting.cast,
          casting.consumeCast,
          casting.coolTime,
          casting.fluidRenderProperties);
      recipes.add(recipe);
    }
    for(tconstruct.library.crafting.CastingRecipe r : recipes)
    {
      table_casting.addCustomCastingRecipe(r);
    }
    
    
    //Add support for Foundry's fluid to the TiCon casting basin.
    recipes.clear();
    for(tconstruct.library.crafting.CastingRecipe casting : basin_casting.getCastingRecipes())
    {
      if(casting.cast != null)
      {
        continue;
      }
      String mapped = liquid_map.get(casting.castingMetal.getFluid().getName());
      if(mapped == null)
      {
        continue;
      }
      FluidStack mapped_liquid = new FluidStack(
          LiquidMetalRegistry.instance.getFluid(mapped),
          mapped.equals("Glass") ?
              casting.castingMetal.amount :
              DivCeil(casting.castingMetal.amount * FoundryAPI.FLUID_AMOUNT_INGOT, TConstruct.ingotLiquidValue));
      tconstruct.library.crafting.CastingRecipe recipe = new tconstruct.library.crafting.CastingRecipe(
          casting.output,
          mapped_liquid,
          null,
          casting.consumeCast,
          casting.coolTime,
          casting.fluidRenderProperties);
      recipes.add(recipe);
    }
    for(tconstruct.library.crafting.CastingRecipe r : recipes)
    {
      basin_casting.addCustomCastingRecipe(r);
    }
    
    //Add TiCon molten metal support to the Atomizer.
    for(Map.Entry<String, String> entry:liquid_map.entrySet())
    {
      String name = entry.getKey();
      if(!name.equals("glass.molten"))
      {
        AtomizerRecipeManager.instance.addRecipe("dust" + entry.getValue(), new FluidStack(FluidRegistry.getFluid(name),TConstruct.ingotLiquidValue));
      }
    }
  }
}
