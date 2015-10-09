package exter.foundry.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import org.lwjgl.opengl.GL11;

import exter.foundry.container.ContainerAlloyFurnace;
import exter.foundry.tileentity.TileEntityAlloyFurnace;

@SideOnly(Side.CLIENT)
public class GuiAlloyFurnace extends GuiFoundry
{

  public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/alloyfurnace.png");

  public static final int BURN_X = 49;
  public static final int BURN_Y = 37;
  public static final int BURN_WIDTH = 14;
  public static final int BURN_HEIGHT = 14;

  public static final int PROGRESS_X = 79;
  public static final int PROGRESS_Y = 35;
  public static final int PROGRESS_WIDTH = 22;
  public static final int PROGRESS_HEIGHT = 15;

  public static final int PROGRESS_OVERLAY_X = 176;
  public static final int PROGRESS_OVERLAY_Y = 14;

  public static final int BURN_OVERLAY_X = 176;
  public static final int BURN_OVERLAY_Y = 0;


  private TileEntityAlloyFurnace te_af;

  public GuiAlloyFurnace(TileEntityAlloyFurnace af, EntityPlayer player)
  {
    super(new ContainerAlloyFurnace(af, player));
    allowUserInput = false;
    ySize = 166;
    te_af = af;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y)
  {
    super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);

    fontRendererObj.drawString("Alloy Furnace", 5, 6, 0x404040);
    fontRendererObj.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);

  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    mc.renderEngine.bindTexture(GUI_TEXTURE);
    int window_x = (width - xSize) / 2;
    int window_y = (height - ySize) / 2;
    drawTexturedModalRect(window_x, window_y, 0, 0, xSize, ySize);

    
    if(te_af.item_burn_time > 0)
    {
      int burn = te_af.burn_time * PROGRESS_HEIGHT / te_af.item_burn_time;

      if(burn > 0)
      {
        drawTexturedModalRect(window_x + BURN_X, window_y + BURN_Y + BURN_HEIGHT - burn, BURN_OVERLAY_X, BURN_OVERLAY_Y + BURN_HEIGHT - burn, BURN_WIDTH, burn);
      }
    }
    if(te_af.progress > 0)
    {
      int progress = te_af.progress * PROGRESS_WIDTH / 400;
      drawTexturedModalRect(window_x + PROGRESS_X, window_y + PROGRESS_Y, PROGRESS_OVERLAY_X, PROGRESS_OVERLAY_Y, progress, PROGRESS_HEIGHT);
    }
  }

  @Override
  protected ResourceLocation GetGUITexture()
  {
    return GUI_TEXTURE;
  }
}
