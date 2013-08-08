package exter.foundry.recipes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Metal Smelter recipe manager
 */
public class SmeltingRecipe
{
  static public final int AMOUNT_BLOCK = 972;
  static public final int AMOUNT_INGOT = 108;
  static public final int AMOUNT_NUGGET = 12;
  static public final int AMOUNT_ORE = 216;
  
  static private Map<String,SmeltingRecipe> metals = new HashMap<String,SmeltingRecipe>();
  
  private final FluidStack fluid;
  public final String solid;
  
  /**
   * Helper method for registering basic smelting recipes for a given metal.
   * @param partial_name The partial ore dictionary name e.g. "Copper" for "ingotCopper","oreCopper", etc.
   * @param fluid The liquid created by the smelter.
   */
  static public void RegisterBasicRecipes(String partial_name,Fluid fluid)
  {
    RegisterRecipe("ingot" + partial_name, new FluidStack(fluid, AMOUNT_INGOT));
    RegisterRecipe("block" + partial_name, new FluidStack(fluid, AMOUNT_BLOCK));
    RegisterRecipe("nugget" + partial_name, new FluidStack(fluid, AMOUNT_NUGGET));
    RegisterRecipe("dust" + partial_name, new FluidStack(fluid, AMOUNT_INGOT));
    RegisterRecipe("ore" + partial_name, new FluidStack(fluid, AMOUNT_ORE));
  }
  
  /**
   * Register a Metal Smelter recipe
   * @param solid_name Item to be smelted
   * @param fluid_stack Resulting fluid
   */
  static public void RegisterRecipe(String solid_name,FluidStack fluid_stack)
  {
    metals.put(solid_name,new SmeltingRecipe(solid_name,fluid_stack));
  }
  
  private SmeltingRecipe(String solid_name,FluidStack fluid_stack)
  {
    solid = solid_name;
    fluid = fluid_stack.copy();
  }
  
  public FluidStack GetFluid()
  {
    return fluid.copy();
  }
  
  public static SmeltingRecipe FindByStack(ItemStack it)
  {
    String od_name = null;
    find_odname: for (String name : OreDictionary.getOreNames())
    {
      for (ItemStack ore : OreDictionary.getOres(name))
      {
        if (it.isItemEqual(ore))
        {
          od_name = name;
          break find_odname;
        }
      }
    }
    if(od_name == null)
    {
      return null;
    }
    return metals.get(od_name);
  }

}
