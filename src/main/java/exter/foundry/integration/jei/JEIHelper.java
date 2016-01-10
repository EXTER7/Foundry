package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.orestack.OreStack;
import exter.foundry.api.substance.ISubstanceGuiTexture;
import exter.foundry.api.substance.InfuserSubstance;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class JEIHelper
{

  public static List<ItemStack> toItemStackList(@Nullable Object input)
  {
    if(input instanceof ItemStack)
    {
      return Collections.singletonList((ItemStack) input);
    }
    if(input instanceof String)
    {
      return OreDictionary.getOres((String) input);
    }
    if(input instanceof OreStack)
    {
      OreStack orestack = (OreStack)input;
      List<ItemStack> result = new ArrayList<ItemStack>();
      for(ItemStack ore:OreDictionary.getOres(orestack.name))
      {
        ore = ore.copy();
        ore.stackSize = orestack.amount;
        result.add(ore);
      }
      return result;
    }
    return Collections.emptyList();
  }
  
  public static IDrawable getSubstanceDrawable(IJeiHelpers helpers,InfuserSubstance substance)
  {
    ISubstanceGuiTexture texture = InfuserRecipeManager.instance.getSubstanceGuiTextures().get(substance.type);
    if(texture == null)
    {
      return null;
    }
    return helpers.getGuiHelper().createDrawable(
        texture.getLocation(),
        texture.getX(), texture.getY(), 8, 47);
  }
  
  public static void setSubstanceGLColor(InfuserSubstance substance)
  {
    int color = InfuserRecipeManager.instance.getSubstanceGuiTextures().get(substance.type).getColor();
    float red = (float) (color >> 16 & 255) / 255.0F;
    float green = (float) (color >> 8 & 255) / 255.0F;
    float blue = (float) (color & 255) / 255.0F;
    GlStateManager.color(red, green, blue,1.0f);
  }
}
