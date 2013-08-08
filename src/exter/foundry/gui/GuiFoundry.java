package exter.foundry.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import org.lwjgl.opengl.GL11;

import exter.foundry.container.ContainerMetalCaster;
import exter.foundry.tileentity.TileEntityMetalCaster;

public abstract class GuiFoundry extends GuiContainer
{
  private static final ResourceLocation BLOCK_TEXTURE = TextureMap.field_110575_b;

  public GuiFoundry(Container container)
  {
    super(container);
  }

  
  protected abstract ResourceLocation GetGUITexture();

  private void drawTexturedModelRectFromIconPartial(int x, int y, Icon icon, int width, int height,int icon_x,int icon_y)
  {
      Tessellator tessellator = Tessellator.instance;
      
      double min_u = icon.getInterpolatedU(icon_x);
      double min_v = icon.getInterpolatedV(icon_y);
      double max_u = icon.getInterpolatedU(icon_x + width);
      double max_v = icon.getInterpolatedV(icon_y + height);
      
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV(x, y + height, zLevel, min_u, max_v);
      tessellator.addVertexWithUV(x + width, y + height, zLevel, max_u, max_v);
      tessellator.addVertexWithUV(x + width, y, zLevel, max_u, min_v);
      tessellator.addVertexWithUV(x, y, zLevel, min_u, min_v);
      tessellator.draw();
  }

  
  protected void DisplayTank(int window_x,int window_y,int x, int y, int tank_height,int overlay_x,int overlay_y, FluidTank tank)
  {
    FluidStack liquid = tank.getFluid();
    if(liquid == null)
    {
      return;
    }
    int start = 0;

    Icon liquid_icon = null;
    Fluid fluid = liquid.getFluid();
    if(fluid != null && fluid.getStillIcon() != null)
    {
      liquid_icon = fluid.getStillIcon();
    }
    mc.renderEngine.func_110577_a(BLOCK_TEXTURE);

    int h = liquid.amount * tank_height / tank.getCapacity();
    
    
    if(liquid_icon != null)
    {
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
          drawTexturedModelRectFromIconPartial(window_x + x, window_y + y + tank_height - i - start, liquid_icon, 16, i,0,16 - i);
        }
        start += 16;

        if(i == 0 || h == 0)
        {
          break;
        }
      }
    }

    mc.renderEngine.func_110577_a(GetGUITexture());
    drawTexturedModalRect(window_x + x, window_y + y, overlay_x, overlay_y, 16, tank_height);
  }
}
