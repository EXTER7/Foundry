package exter.foundry.api.recipe;

import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISubstanceGuiTexture
{
  public ResourceLocation GetLocation();
  
  public int GetX();
  
  public int GetY();
}
