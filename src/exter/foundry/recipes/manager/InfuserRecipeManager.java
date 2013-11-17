package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.api.recipe.IInfuserSubstanceRecipe;
import exter.foundry.api.recipe.ISubstanceGuiTexture;
import exter.foundry.api.recipe.manager.IInfuserRecipeManager;
import exter.foundry.recipes.InfuserRecipe;
import exter.foundry.recipes.InfuserSubstance;
import exter.foundry.recipes.InfuserSubstanceRecipe;
import exter.foundry.recipes.SubstanceGuiTexture;
import exter.foundry.util.FoundryMiscUtils;

public class InfuserRecipeManager implements IInfuserRecipeManager
{
  private List<InfuserRecipe> recipes;

  public static final InfuserRecipeManager instance = new InfuserRecipeManager();

  
  @SideOnly(Side.CLIENT)
  private Map<String,SubstanceGuiTexture> substance_textures;

  private List<InfuserSubstanceRecipe> substance_recipes;

  private InfuserRecipeManager()
  {
    recipes = new ArrayList<InfuserRecipe>();
    substance_recipes = new ArrayList<InfuserSubstanceRecipe>();
  }
  
  
  @SideOnly(Side.CLIENT)
  public void InitTextures()
  {
    substance_textures = new HashMap<String,SubstanceGuiTexture>();
  }
  @SideOnly(Side.CLIENT)
  public void RegisterSubstanceTexture(String substance_type,ResourceLocation texture_path,int pos_x,int pos_y)
  {
    substance_textures.put(substance_type, new SubstanceGuiTexture(texture_path,pos_x,pos_y));
  }
  
  /**
   * Get a substance's texture from it's name.
   * @param name Substance name. e.g: "Carbon".
   * @return The substance's texture.
   */
  @SideOnly(Side.CLIENT)
  public SubstanceGuiTexture GetSubstanceTexture(String name)
  {
    return substance_textures.get(name);
  }
  
  @Override
  public void AddRecipe(FluidStack result,FluidStack in_fluid,String substance_type,int substance_amount)
  {
    recipes.add(new InfuserRecipe(result,in_fluid,new InfuserSubstance(substance_type,substance_amount)));
  }
  
  /**
   * Find a infusing recipe given a FluidStack and a substance.
   * @param fluid FluidStack that contains the recipe's required fluid.
   * @param substance Substance that contains the recipe's required substance.
   * @return The infusing recipe, or null if no matching recipe.
   */
  public InfuserRecipe FindRecipe(FluidStack fluid,InfuserSubstance substance)
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
  
  @Override
  public void AddSubstanceRecipe(String substance_type,int substance_amount,Object itm, int time)
  {
    substance_recipes.add(new InfuserSubstanceRecipe(new InfuserSubstance(substance_type,substance_amount),itm,time));
  }

  /**
   * Find a substance recipe given a Item.
   * @param item The item required in the recipe
   * @return The substance recipe, or null if no matching recipe.
   */
  public InfuserSubstanceRecipe FindSubstanceRecipe(ItemStack item)
  {
    if(item == null)
    {
      return null;
    }
    for(InfuserSubstanceRecipe isr:substance_recipes)
    {
      if(FoundryMiscUtils.IsItemMatch(item, isr.item))
      {
        return isr;
      }
    }
    return null;
  }


  @Override
  public List<? extends IInfuserRecipe> GetRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }


  @Override
  public List<? extends IInfuserSubstanceRecipe> GetSubstanceRecipes()
  {
    return Collections.unmodifiableList(substance_recipes);
  }


  @Override
  @SideOnly(Side.CLIENT)
  public Map<String, ? extends ISubstanceGuiTexture> GetSubstanceGuiTextures()
  {
    return Collections.unmodifiableMap(substance_textures);
  }
}
