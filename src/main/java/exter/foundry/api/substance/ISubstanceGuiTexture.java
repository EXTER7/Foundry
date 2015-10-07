package exter.foundry.api.substance;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public interface ISubstanceGuiTexture
{
  /**
   * Get the texture's location/
   * @return The texture's location.
   */
  public ResourceLocation getLocation();
  
  /**
   * Get the X coordinate offset of the substance in the texture.
   * @return The X coordinate offset of the substance in the texture.
   */
  public int getX();
  
  /**
   * Get the Y coordinate offset of the substance in the texture.
   * @return The Y coordinate offset of the substance in the texture.
   */
  public int getY();
  
  /**
   * Get the color of the substance.
   * @return The color of the substance.
   */
  public int getColor();
}
