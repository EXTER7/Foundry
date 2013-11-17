package exter.foundry.api.recipe.manager;

import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.api.recipe.IInfuserSubstanceRecipe;
import exter.foundry.api.recipe.ISubstanceGuiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public interface IInfuserRecipeManager
{
  /**
   * Register a Metal Infuser recipe.
   * @param result Fluid produced.
   * @param in_fluid Fluid required (fluid type and amount).
   * @param in_substance Substance required (substance type and amount).
   */
  public void AddRecipe(FluidStack result,FluidStack fluid,String substance_type,int substance_amount);

  /**
   * Get a list of all the recipes
   * @return List of all the recipes
   */
  public List<? extends IInfuserRecipe> GetRecipes();
  

  /**
   * Register a Metal Infuser substance recipe.
   * @param subs Substance produced.
   * @param subs Substance produced.
   * @param item Item required.
   * @param energy Energy required in 1/100th MJ.
   */
  public void AddSubstanceRecipe(String substance_type,int substance_amount,Object item, int energy);

  /**
   * Get a list of all the substance recipes
   * @return List of all the substance recipes
   */
  public List<? extends IInfuserSubstanceRecipe> GetSubstanceRecipes();

  /**
   * Register a texture for a substance
   * @param substance_type Substance name. e.g: "Carbon"
   * @param texture_path Location of the texture
   * @param pos_x X coordinate in the texture
   * @param pos_y Y coordinate in the texture
   */
  @SideOnly(Side.CLIENT)
  public void RegisterSubstanceTexture(String substance_type,ResourceLocation texture_path,int pos_x,int pos_y);

  /**
   * Get the substance GUI textures mapped by the type.
   * @return Substance type -> texture map.
   */
  @SideOnly(Side.CLIENT)
  public Map<String,? extends ISubstanceGuiTexture> GetSubstanceGuiTextures();
}
