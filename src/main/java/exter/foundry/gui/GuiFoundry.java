package exter.foundry.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import exter.foundry.tileentity.TileEntityFoundry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public abstract class GuiFoundry extends GuiContainer
{
  private static final ResourceLocation BLOCK_TEXTURE = TextureMap.LOCATION_BLOCKS_TEXTURE;

  public GuiFoundry(Container container)
  {
    super(container);
  }

  
  protected abstract ResourceLocation getGUITexture();

  protected void drawItemStack(int x,int y, ItemStack stack)
  {
    //GL11.glTranslatef(0.0F, 0.0F, 32.0F);
    GL11.glPushMatrix();
    RenderHelper.enableGUIStandardItemLighting();
    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    GL11.glEnable(GL11.GL_COLOR_MATERIAL);
    GL11.glEnable(GL11.GL_LIGHTING);
    zLevel = 200.0F;
    itemRender.zLevel = 200.0F;
    FontRenderer font = null;
    if(stack != null)
    {
      font = stack.getItem().getFontRenderer(stack);
    }
    if(font == null)
    {
      font = fontRendererObj;
    }
    itemRender.renderItemAndEffectIntoGUI(stack, x, y);
    itemRender.renderItemOverlayIntoGUI(font, stack, x, y, null);
    zLevel = 0.0F;
    itemRender.zLevel = 0.0F;
    mc.renderEngine.bindTexture(this.getGUITexture());
    GL11.glPopMatrix();
    RenderHelper.enableStandardItemLighting();
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glDisable(GL11.GL_LIGHTING);
    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    //GL11.glTranslatef(0.0F, 0.0F, -32.0F);
  }
  
  /**
   * Draw part of an icon
   * @param x X coordinate to draw to.
   * @param y Y coordinate to draw to.
   * @param icon Icon to draw
   * @param width Width to draw.
   * @param height Height to draw.
   * @param icon_x X coordinate offset in the icon.
   * @param icon_y Y coordinate offset in the icon.
   */
  private void drawTexturedModelRectFromIconPartial(int x, int y, TextureAtlasSprite icon, int width, int height,int icon_x,int icon_y, int color)
  {
    VertexBuffer tessellator = Tessellator.getInstance().getBuffer();
      
      double min_u = icon.getInterpolatedU(icon_x);
      double min_v = icon.getInterpolatedV(icon_y);
      double max_u = icon.getInterpolatedU(icon_x + width);
      double max_v = icon.getInterpolatedV(icon_y + height);
      int red = color >>> 16 & 255;
      int green = color >>> 8 & 255;
      int blue = color & 255;
      
      tessellator.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      tessellator.pos(x, y + height, zLevel).tex(min_u, max_v).color(red, green, blue, 255).endVertex();
      tessellator.pos(x + width, y + height, zLevel).tex( max_u, max_v).color(red, green, blue, 255).endVertex();
      tessellator.pos(x + width, y, zLevel).tex( max_u, min_v).color(red, green, blue, 255).endVertex();
      tessellator.pos(x, y, zLevel).tex( min_u, min_v).color(red, green, blue, 255).endVertex();
      Tessellator.getInstance().draw();
  }

  protected void addTankTooltip(List<String> tooltip, int x, int y, FluidTank tank)
  {
    FluidStack stack = tank.getFluid();
    if(stack != null && stack.amount > 0)
    {
      
      tooltip.add(stack.getLocalizedName());
      tooltip.add(String.valueOf(stack.amount) + " / " + String.valueOf(tank.getCapacity()) + " mB");
    } else
    {
      tooltip.add("0 / " + String.valueOf(tank.getCapacity()) + " mB");
    }
  }
  
  /**
   * Draw a tank in the GUI.
   * @param window_x X coordinate of the GUI window.
   * @param window_y Y coordinate of the GUI window.
   * @param x X coordinate of the tank in the GUI window.
   * @param y Y coordinate of the tank in the GUI window.
   * @param tank_height Height of the tank in the GUI.
   * @param overlay_x X coordinate of overlay of the tank drawn in front of the fluid.
   * @param overlay_y Y coordinate of overlay of the tank drawn in front of the fluid.
   * @param tank Tank to draw.
   */
  protected void displayTank(int window_x,int window_y,int x, int y, int tank_height,int overlay_x,int overlay_y, FluidTank tank)
  {
    FluidStack liquid = tank.getFluid();
    if(liquid == null)
    {
      return;
    }
    int start = 0;
    GL11.glDisable(GL11.GL_DEPTH_TEST);

    TextureAtlasSprite liquid_icon = null;
    Fluid fluid = liquid.getFluid();
    if(fluid != null && fluid.getStill() != null)
    {
      ResourceLocation texture = fluid.getStill(liquid);
      if(texture == null)
      {
        texture = TextureMap.LOCATION_MISSING_TEXTURE;
      }
      liquid_icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
    }

    int h = liquid.amount * tank_height / tank.getCapacity();
    
    if(liquid_icon != null)
    {
      mc.renderEngine.bindTexture(BLOCK_TEXTURE);
      int color = fluid.getColor(liquid);
      //setGLColor(color);
      while(true)
      {
        int i;

        if(h > 16)
        {
          i = 16;
          h -= 16;
        } else
        {
          i = h;
          h = 0;
        }

        if(i > 0)
        {
          drawTexturedModelRectFromIconPartial(
              window_x + x, window_y + y + tank_height - i - start,
              liquid_icon,
              16, i,
              0, 16 - i,
              color);
        }
        start += 16;

        if(i == 0 || h == 0)
        {
          break;
        }
      }
      GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
      mc.renderEngine.bindTexture(getGUITexture());
    }

    drawTexturedModalRect(window_x + x, window_y + y, overlay_x, overlay_y, 16, tank_height);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
  }


  protected void setGLColor(int color)
  {
    float red = (float) (color >> 16 & 255) / 255.0F;
    float green = (float) (color >> 8 & 255) / 255.0F;
    float blue = (float) (color & 255) / 255.0F;
    GL11.glColor4f(red, green, blue, 1.0f);
  }
  
  protected String getRedstoenModeText(TileEntityFoundry.RedstoneMode mode)
  {
    switch(mode)
    {
      case RSMODE_IGNORE:
        return "Mode: Ignore Restone";
      case RSMODE_OFF:
        return "Mode: Redstone signal OFF";
      case RSMODE_ON:
        return "Mode: Redstone signal ON";
      case RSMODE_PULSE:
        return "Mode: Redstone pulse";
      default:
        return null;
    }
  }
}
