package exter.foundry.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.item.ItemRefractoryFluidContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

@SideOnly(Side.CLIENT)
public class RendererItemContainer implements IItemRenderer
{
  private static RenderItem renderItem = new RenderItem();
  private static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;
  private static final ResourceLocation ITEM_TEXTURE = TextureMap.locationItemsTexture;

  @Override
  public boolean handleRenderType(ItemStack itemStack, ItemRenderType type)
  {
    return type == ItemRenderType.INVENTORY;
  }

  @Override
  public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
  {
    return false;
  }

  private void renderIconPartial(int x, int y, IIcon icon, int width, int height, int icon_x, int icon_y, int color, int alpha)
  {
    Tessellator tessellator = Tessellator.instance;

    double min_u = icon.getInterpolatedU(icon_x);
    double min_v = icon.getInterpolatedV(icon_y);
    double max_u = icon.getInterpolatedU(icon_x + width);
    double max_v = icon.getInterpolatedV(icon_y + height);

    float red = (float) (color >> 16 & 255) / 255.0F;
    float green = (float) (color >> 8 & 255) / 255.0F;
    float blue = (float) (color & 255) / 255.0F;
    GL11.glColor4f(red, green, blue, (alpha & 255) / 255.0F);
    int src = GL11.glGetInteger(GL11.GL_BLEND_SRC);
    int dst = GL11.glGetInteger(GL11.GL_BLEND_DST);
    boolean enabled = GL11.glIsEnabled(GL11.GL_BLEND);
    boolean enabled_test = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glDisable(GL11.GL_ALPHA_TEST);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    tessellator.startDrawingQuads();
    tessellator.addVertexWithUV(x, y + height, 0, min_u, max_v);
    tessellator.addVertexWithUV(x + width, y + height, 0, max_u, max_v);
    tessellator.addVertexWithUV(x + width, y, 0, max_u, min_v);
    tessellator.addVertexWithUV(x, y, 0, min_u, min_v);
    tessellator.draw();
    if(!enabled)
    {
      GL11.glDisable(GL11.GL_BLEND);
    }
    if(enabled_test)
    {
      GL11.glEnable(GL11.GL_ALPHA_TEST);
    }
    GL11.glBlendFunc(src, dst);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }

  @Override
  public void renderItem(ItemRenderType type, ItemStack stack, Object... data)
  {
    ItemRefractoryFluidContainer item = (ItemRefractoryFluidContainer) stack.getItem();
    FluidStack fluid_stack = item.getFluid(stack);

    renderItem.renderIcon(0, 0, item.icon_bg, 16, 16);
    if(fluid_stack != null)
    {
      IIcon fluid_icon = fluid_stack.getFluid().getStillIcon();
      if(fluid_icon != null)
      {
        int alpha;
        int h;
        if(fluid_stack.getFluid().isGaseous())
        {
          alpha = fluid_stack.amount * 250 / FluidContainerRegistry.BUCKET_VOLUME;
          h = 10;
        } else
        {
          h = fluid_stack.amount * 10 / FluidContainerRegistry.BUCKET_VOLUME;
          alpha = 255;
        }
        if(h > 0)
        {
          if(type == ItemRenderType.ENTITY || type == ItemRenderType.EQUIPPED_FIRST_PERSON)
          {
            GL11.glScalef(1, 1, 0.9f);
          }
          Minecraft mc = Minecraft.getMinecraft();
          mc.renderEngine.bindTexture(BLOCK_TEXTURE);
          renderIconPartial(4, 16 - 3 - h, fluid_icon, 8, h, 4, 16 - 3 - h, fluid_stack.getFluid().getColor(fluid_stack), alpha);
          mc.renderEngine.bindTexture(ITEM_TEXTURE);
        }
      }
    }
    renderItem.renderIcon(0, 0, item.icon_fg, 16, 16);
  }
}
