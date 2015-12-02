package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.api.recipe.IInfuserSubstanceRecipe;
import exter.foundry.api.recipe.manager.IInfuserRecipeManager;
import exter.foundry.api.substance.ISubstanceGuiTexture;
import exter.foundry.api.substance.InfuserSubstance;
import exter.foundry.recipes.InfuserRecipe;
import exter.foundry.recipes.InfuserSubstanceRecipe;
import exter.foundry.recipes.SubstanceGuiTexture;

public class InfuserRecipeManager implements IInfuserRecipeManager
{
  public List<IInfuserRecipe> recipes;

  public static final InfuserRecipeManager instance = new InfuserRecipeManager();

  
  @SideOnly(Side.CLIENT)
  private Map<String,ISubstanceGuiTexture> substance_textures;

  public List<IInfuserSubstanceRecipe> substance_recipes;

  private InfuserRecipeManager()
  {
    recipes = new ArrayList<IInfuserRecipe>();
    substance_recipes = new ArrayList<IInfuserSubstanceRecipe>();
  }
  
  
  @SideOnly(Side.CLIENT)
  public void InitTextures()
  {
    substance_textures = new HashMap<String,ISubstanceGuiTexture>();
  }
  
  @SideOnly(Side.CLIENT)
  public void registerSubstanceTexture(String substance_type,ResourceLocation texture_path,int pos_x,int pos_y)
  {
    registerSubstanceTexture(substance_type,texture_path,pos_x,pos_y,0xFFFFFF);
  }

  @SideOnly(Side.CLIENT)
  public void registerSubstanceTexture(String substance_type,ResourceLocation texture_path,int pos_x,int pos_y,int texture_color)
  {
    substance_textures.put(substance_type, new SubstanceGuiTexture(texture_path,pos_x,pos_y,texture_color));
  }

  /**
   * Get a substance's texture from it's name.
   * @param name Substance name. e.g: "Carbon".
   * @return The substance's texture.
   */
  @SideOnly(Side.CLIENT)
  public ISubstanceGuiTexture GetSubstanceTexture(String name)
  {
    return substance_textures.get(name);
  }
  
  @Override
  public void addRecipe(FluidStack result,FluidStack in_fluid,InfuserSubstance substance)
  {
    recipes.add(new InfuserRecipe(result,in_fluid,substance));
  }
  
  @Override
  public IInfuserRecipe findRecipe(FluidStack fluid,InfuserSubstance substance)
  {
    if(fluid == null || substance == null)
    {
      return null;
    }
    for(IInfuserRecipe ir:recipes)
    {
      if(ir.matchesRecipe(fluid, substance))
      {
        return ir;
      }
    }
    return null;
  }
  
  @Override
  public void addSubstanceRecipe(InfuserSubstance substance,Object itm, int energy)
  {
    substance_recipes.add(new InfuserSubstanceRecipe(substance,itm,energy));
  }

  @Override
  public IInfuserSubstanceRecipe findSubstanceRecipe(ItemStack item)
  {
    if(item == null)
    {
      return null;
    }
    for(IInfuserSubstanceRecipe isr:substance_recipes)
    {
      if(isr.matchesRecipe(item))
      {
        return isr;
      }
    }
    return null;
  }


  @Override
  public List<IInfuserRecipe> getRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }


  @Override
  public List<IInfuserSubstanceRecipe> getSubstanceRecipes()
  {
    return Collections.unmodifiableList(substance_recipes);
  }


  @Override
  @SideOnly(Side.CLIENT)
  public Map<String, ISubstanceGuiTexture> getSubstanceGuiTextures()
  {
    return Collections.unmodifiableMap(substance_textures);
  }


  @Override
  public void removeRecipe(IInfuserRecipe recipe)
  {
    recipes.remove(recipe);
  }


  @Override
  public void removeSubstanceRecipe(IInfuserSubstanceRecipe recipe)
  {
    substance_recipes.remove(recipe);
  }
}
