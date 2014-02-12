package exter.foundry.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.Loader;
import exter.foundry.api.recipe.FoundryRecipes;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import tconstruct.TConstruct;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.AlloyMix;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.Smeltery;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationTiCon extends ModIntegration
{
  static public final int ITEM_INGOT_CAST = 0;
  
  private Map<String,String> liquid_map;
  private static final int GCD(int a, int b)
  {
    while(b != 0)
    {
      int t = b;
      b = a % b;
      a = t;
    }
    return a;
  }

  static private final int INGOT_GCD = GCD(TConstruct.ingotLiquidValue,FoundryRecipes.FLUID_AMOUNT_INGOT);
  
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
    if(!Loader.isModLoaded("TConstruct"))
    {
      is_loaded = false;
      return;
    }
    items = new ItemStack[1];

    items[ITEM_INGOT_CAST] = ItemStack.copyItemStack(TConstructRegistry.getItemStack("ingotCast"));
    
    liquid_map = new HashMap<String,String>();
    liquid_map.put("iron.molten","Iron");
    liquid_map.put("gold.molten","Gold");
    liquid_map.put("copper.molten", "Copper");
    liquid_map.put("tin.molten", "Tin");
    liquid_map.put("platinum.molten","Platinum");
    liquid_map.put("aluminum.molten","Aluminum");
    liquid_map.put("bronze.molten","Bronze");
    liquid_map.put("steel.molten","Steel");
    liquid_map.put("nickel.molten","Nickel");
    liquid_map.put("lead.molten","Lead");
    liquid_map.put("silver.molten","Silver");
    liquid_map.put("invar.molten","Invar");
    liquid_map.put("electrum.molten","Electrum");
  }
  
  private void CreateAlloyRecipe(AlloyMix mix,int index,List<FluidStack> inputs)
  {
    if(index == mix.mixers.size())
    {
      FluidStack[] in = new FluidStack[mix.mixers.size()];
      in = inputs.toArray(in);
      FluidStack result = new FluidStack(mix.result.getFluid(),mix.result.amount / INGOT_GCD);
      AlloyRecipeManager.instance.AddRecipe(result, in);
      return;
    }

    FluidStack ing = mix.mixers.get(index);
    String mapped = liquid_map.get(ing.getFluid().getName());
    if(mapped != null)
    {
      List<FluidStack> in = new ArrayList<FluidStack>(inputs);
      in.add(new FluidStack( // Convert TiCon Fluid Stack to Foundry Fluid Stack
          LiquidMetalRegistry.instance.GetFluid(mapped),
          ing.amount * FoundryRecipes.FLUID_AMOUNT_INGOT / (TConstruct.ingotLiquidValue * INGOT_GCD)));
      CreateAlloyRecipe(mix,index + 1,in);
    }
    List<FluidStack> in = new ArrayList<FluidStack>(inputs);
    FluidStack fl = ing;
    in.add(new FluidStack(fl.getFluid(),fl.amount / INGOT_GCD));
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
    //Convert TiCon Smeltery recipes to Foundry ICF melting recipes (except those that have an existing recipe).
    for(List<Integer> item : Smeltery.getSmeltingList().keySet())
    {
      ItemStack stack = new ItemStack(item.get(0), 1, item.get(1));
      if(MeltingRecipeManager.instance.FindRecipe(stack) == null)
      {
        FluidStack result = Smeltery.getSmelteryResult(stack);
        if(result.amount <= 6000)
        {
          MeltingRecipeManager.instance.AddRecipe(stack, result, Smeltery.getLiquifyTemperature(stack) + 274);
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
    ItemStack block_mold = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_BLOCK);
    for(tconstruct.library.crafting.CastingRecipe casting:table_casting.getCastingRecipes())
    {
      if(casting.cast != null && !casting.consumeCast)
      {
        if(!CastingRecipeManager.instance.IsItemMold(casting.cast))
        {
          //Register the cast as a mold
          CastingRecipeManager.instance.AddMold(casting.cast);
        }
        
        String mapped = liquid_map.get(casting.castingMetal.getFluid().getName());
        FluidStack mapped_liquid = null;
        if(mapped != null)
        {
          mapped_liquid = new FluidStack(
              LiquidMetalRegistry.instance.GetFluid(mapped),
              casting.castingMetal.amount * FoundryRecipes.FLUID_AMOUNT_INGOT / TConstruct.ingotLiquidValue);
        }
        if(casting.cast.isItemEqual(items[ITEM_INGOT_CAST]))
        {
          ItemStack ingot_mold = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_INGOT);
          if(casting.castingMetal.amount <= 6000)
          {
            CastingRecipeManager.instance.AddRecipe(casting.output, casting.castingMetal, ingot_mold, null);
          }
        } else if(mapped_liquid != null)
        {
          if(mapped_liquid.amount <= 6000)
          {
            CastingRecipeManager.instance.AddRecipe(casting.output, mapped_liquid, casting.cast, null);
          }
        }
        if(casting.castingMetal.amount <= 6000)
        {
          CastingRecipeManager.instance.AddRecipe(casting.output, casting.castingMetal, casting.cast, null);
        }
      }
    }
    for(tconstruct.library.crafting.CastingRecipe casting:basin_casting.getCastingRecipes())
    {
      if(casting.castingMetal.amount <= 6000)
      {
        CastingRecipeManager.instance.AddRecipe(casting.output, casting.castingMetal, block_mold, null);
      }
    }
  }
}
