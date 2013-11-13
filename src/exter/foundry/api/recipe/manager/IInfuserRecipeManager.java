package exter.foundry.api.recipe.manager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
   * Register a Metal Infuser substance recipe.
   * @param substance_type Type of substance produced. e.g: "Carbon".
   * @param substance_amount Amount substance produced.
   * @param itm Item required.
   * @param time Energy required.
   */
  public void AddSubstanceRecipe(String substance_type,int substance_amount,ItemStack item, int energy);

  /**
   * Register a Metal Infuser substance recipe.
   * @param subs Substance produced.
   * @param subs Substance produced.
   * @param itm Item required (Ore Dictionary name).
   * @param time Energy required.
   */
  public void AddSubstanceRecipe(String substance_type,int substance_amount,String item, int energy);

  /**
   * Register a texture for a substance
   * @param substance_type Substance name. e.g: "Carbon"
   * @param texture_path Location of the texture
   * @param pos_x X coordinate in the texture
   * @param pos_y Y coordinate in the texture
   */
  @SideOnly(Side.CLIENT)
  public void RegisterSubstanceTexture(String substance_type,ResourceLocation texture_path,int pos_x,int pos_y);

}
