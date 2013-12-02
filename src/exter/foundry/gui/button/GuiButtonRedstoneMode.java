package exter.foundry.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiButtonRedstoneMode extends GuiButton
{
  private ResourceLocation gui_texture;
  static public final int TEXTURE_X = 176;
  static public final int TEXTURE_Y = 90;
  static public final int TEXTURE_WIDTH = 16;
  static public final int TEXTURE_HEIGHT = 15;

  public GuiButtonRedstoneMode(int id, int x, int y,ResourceLocation texture)
  {
    super(id, x, y, TEXTURE_WIDTH, TEXTURE_HEIGHT, "");
    gui_texture = texture;
  }

  public void drawButton(Minecraft mc, int x, int y)
  {
    if(drawButton)
    {
      mc.getTextureManager().bindTexture(gui_texture);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      boolean mouse_over = x >= xPosition && y >= yPosition && x < xPosition + width && y < yPosition + height;
      int tex_x = TEXTURE_X;

      if(mouse_over)
      {
        tex_x += width;
      }

      drawTexturedModalRect(xPosition, yPosition, tex_x, TEXTURE_Y, width, height);
    }
  }
}
