package exter.foundry.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import exter.foundry.container.ContainerMoldStation;
import exter.foundry.gui.button.GuiButtonFoundry;
import exter.foundry.tileentity.TileEntityMoldStation;


@SideOnly(Side.CLIENT)
public class GuiMoldStation extends GuiFoundry
{

  public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/moldstation.png");

  public static final int BURN_X = 119;
  public static final int BURN_Y = 59;
  public static final int BURN_WIDTH = 14;
  public static final int BURN_HEIGHT = 14;

  public static final int PROGRESS_X = 116;
  public static final int PROGRESS_Y = 39;
  public static final int PROGRESS_WIDTH = 22;
  public static final int PROGRESS_HEIGHT = 15;

  public static final int PROGRESS_OVERLAY_X = 176;
  public static final int PROGRESS_OVERLAY_Y = 14;

  public static final int BURN_OVERLAY_X = 176;
  public static final int BURN_OVERLAY_Y = 0;

  public static final int BLOCK_X = 38;
  public static final int BLOCK_Y = 16;
  
  public static final int BLOCK_SIZE = 76;
  
  public static final int BLOCK_OVERLAY_X = 176;
  public static final int BLOCK_OVERLAY_Y = 31;

  public static final int GRID_X = BLOCK_X + 5;
  public static final int GRID_Y = BLOCK_Y + 5;

  public static final int GRID_SLOT_SIZE = 11;
  public static final int GRID_SIZE = GRID_SLOT_SIZE * 6;

  public static final int GRID_OVERLAY_X = 176;
  public static final int GRID_OVERLAY_Y = 107;

  private TileEntityMoldStation te_ms;
  private GuiButtonFoundry button_fire;

  public GuiMoldStation(TileEntityMoldStation af, EntityPlayer player)
  {
    super(new ContainerMoldStation(af, player));
    allowUserInput = false;
    ySize = 190;
    te_ms = af;
  }

  public void initGui()
  {
    super.initGui();
    int window_x = (width - xSize) / 2;
    int window_y = (height - ySize) / 2;
    button_fire = new GuiButtonFoundry(
        1,
        117 + window_x, 15 + window_y,
        17, 17,
        GUI_TEXTURE,187,107,
        204,107);
    buttonList.add(button_fire);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y)
  {
    super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);

    fontRendererObj.drawString("Mold Station", 5, 6, 0x404040);
    fontRendererObj.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
  }
  
  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
  {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    if(isPointInRegion(GRID_X, GRID_Y, GRID_SIZE, GRID_SIZE, mouseX, mouseY))
    {
      int x = (mouseX - GRID_X - guiLeft) / GRID_SLOT_SIZE;
      int y = (mouseY - GRID_Y - guiTop) / GRID_SLOT_SIZE;
      if(mouseButton == 0)
      {
        te_ms.carve(x,y,x,y);
      } else
      {
        te_ms.mend(x,y,x,y);
      }
    }
  }


  @Override
  protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    mc.renderEngine.bindTexture(GUI_TEXTURE);
    int window_x = (width - xSize) / 2;
    int window_y = (height - ySize) / 2;
    drawTexturedModalRect(window_x, window_y, 0, 0, xSize, ySize);

    
    if(te_ms.getItemBurnTime() > 0)
    {
      int burn = te_ms.getBurnTime() * PROGRESS_HEIGHT / te_ms.getItemBurnTime();

      if(burn > 0)
      {
        drawTexturedModalRect(window_x + BURN_X, window_y + BURN_Y + BURN_HEIGHT - burn, BURN_OVERLAY_X, BURN_OVERLAY_Y + BURN_HEIGHT - burn, BURN_WIDTH, burn);
      }
    }
    if(te_ms.getProgress() > 0)
    {
      int progress = te_ms.getProgress() * PROGRESS_WIDTH / 200;
      drawTexturedModalRect(window_x + PROGRESS_X, window_y + PROGRESS_Y, PROGRESS_OVERLAY_X, PROGRESS_OVERLAY_Y, progress, PROGRESS_HEIGHT);
    }
    
    if(te_ms.hasBlock())
    {
      drawTexturedModalRect(window_x + BLOCK_X, window_y + BLOCK_Y, BLOCK_OVERLAY_X, BLOCK_OVERLAY_Y, BLOCK_SIZE, BLOCK_SIZE);
      for(int i = 0; i < 36; i++)
      {
        int gx = i % 6;
        int gy = i / 6;
        int sv = te_ms.getGridSlot(i);
        if(sv > 0)
        {
          drawTexturedModalRect(
              window_x + GRID_X + gx * GRID_SLOT_SIZE,
              window_y + GRID_Y + gy * GRID_SLOT_SIZE,
              GRID_OVERLAY_X,
              GRID_OVERLAY_Y + ((sv - 1) * GRID_SLOT_SIZE),
              GRID_SLOT_SIZE, GRID_SLOT_SIZE);
        }
      }
    }
  }

  @Override
  protected ResourceLocation getGUITexture()
  {
    return GUI_TEXTURE;
  }
  
  @Override
  protected void actionPerformed(GuiButton button)
  {
    if(button.id == button_fire.id)
    {
      te_ms.fire();
    }
  }
}
