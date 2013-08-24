package exter.foundry.recipes;

import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SubstanceGuiTexture
{
  static public final int TEXTURE_WIDTH = 8;
  
  
  public final ResourceLocation texture;
  public final int x;
  public final int y;
  
  
  public SubstanceGuiTexture(ResourceLocation texture_path,int pos_x,int pos_y)
  {
    texture = texture_path;
    x = pos_x;
    y = pos_y;
  }
}
