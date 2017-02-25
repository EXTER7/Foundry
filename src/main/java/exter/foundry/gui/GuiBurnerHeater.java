package exter.foundry.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import org.lwjgl.opengl.GL11;

import exter.foundry.container.ContainerBurnerHeater;
import exter.foundry.tileentity.TileEntityBurnerHeater;


@SideOnly(Side.CLIENT)
public class GuiBurnerHeater extends GuiFoundry
{

  public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/burnerheater.png");

  public static final int BURN_X = 80;
  public static final int BURN_Y = 17;
  public static final int BURN_WIDTH = 14;
  public static final int BURN_HEIGHT = 14;

  public static final int BURN_OVERLAY_X = 176;
  public static final int BURN_OVERLAY_Y = 0;


  private TileEntityBurnerHeater te_bh;

  public GuiBurnerHeater(TileEntityBurnerHeater bh, EntityPlayer player)
  {
    super(new ContainerBurnerHeater(bh, player));
    allowUserInput = false;
    ySize = 166;
    te_bh = bh;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y)
  {
    super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);

    fontRenderer.drawString("Burner Heater", 5, 6, 0x404040);
    fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);

  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    mc.renderEngine.bindTexture(GUI_TEXTURE);
    int window_x = (width - xSize) / 2;
    int window_y = (height - ySize) / 2;
    drawTexturedModalRect(window_x, window_y, 0, 0, xSize, ySize);

    
    if(te_bh.getItemBurnTime() > 0)
    {
      int burn = te_bh.getBurningTime() * BURN_HEIGHT / te_bh.getItemBurnTime();

      if(burn > 0)
      {
        drawTexturedModalRect(window_x + BURN_X, window_y + BURN_Y + BURN_HEIGHT - burn, BURN_OVERLAY_X, BURN_OVERLAY_Y + BURN_HEIGHT - burn, BURN_WIDTH, burn);
      }
    }
  }

  @Override
  protected ResourceLocation getGUITexture()
  {
    return GUI_TEXTURE;
  }
}
