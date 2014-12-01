package exter.foundry.api.recipe.manager;

import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.api.recipe.IInfuserSubstanceRecipe;
import exter.foundry.api.substance.ISubstanceGuiTexture;
import exter.foundry.api.substance.InfuserSubstance;
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
  public void AddRecipe(FluidStack result,FluidStack fluid,InfuserSubstance substance);

  /**
   * Get a list of all the recipes
   * @return List of all the recipes
   */
  public List<IInfuserRecipe> GetRecipes();
  

  /**
   * Register a Metal Infuser substance recipe.
   * @param subs Substance produced.
   * @param item Item required.
   * @param energy Energy required (100 Energy = 1 MJ, 10 Energy = 1 RF, 40 Energy = 1 EU).
   */
  public void AddSubstanceRecipe(InfuserSubstance subs,Object item, int energy);

  /**
   * Get a list of all the substance recipes
   * @return List of all the substance recipes
   */
  public List<IInfuserSubstanceRecipe> GetSubstanceRecipes();

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
   * Register a texture for a substance
   * @param substance_type Substance name. e.g: "Carbon"
   * @param texture_path Location of the texture
   * @param pos_x X coordinate in the texture
   * @param pos_y Y coordinate in the texture
   * @param texture_color color of the texture
   */
  @SideOnly(Side.CLIENT)
  public void RegisterSubstanceTexture(String substance_type,ResourceLocation texture_path,int pos_x,int pos_y,int texture_color);

  /**
   * Get the substance GUI textures mapped by the type.
   * @return Substance type -> texture map.
   */
  @SideOnly(Side.CLIENT)
  public Map<String,ISubstanceGuiTexture> GetSubstanceGuiTextures();

  /**
   * Find a infusing recipe given a FluidStack and a substance.
   * @param fluid FluidStack that contains the recipe's required fluid.
   * @param substance Substance that contains the recipe's required substance.
   * @return The infusing recipe, or null if no matching recipe.
   */
  public IInfuserRecipe FindRecipe(FluidStack fluid,InfuserSubstance substance);

  /**
   * Find a substance recipe given a Item.
   * @param item The item required in the recipe
   * @return The substance recipe, or null if no matching recipe.
   */
  public IInfuserSubstanceRecipe FindSubstanceRecipe(ItemStack item);

  /**
   * Removes a recipe.
   * @param The recipe to remove.
   */
  public void RemoveRecipe(IInfuserRecipe recipe);

  /**
   * Removes a substance recipe.
   * @param The recipe to remove.
   */
  public void RemoveSubstanceRecipe(IInfuserSubstanceRecipe recipe);
}
