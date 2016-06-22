package exter.foundry.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.container.ContainerCokeOven;
import exter.foundry.gui.button.GuiButtonFoundry;
import exter.foundry.tileentity.TileEntityCokeOven;
import exter.foundry.tileentity.TileEntityFoundry.RedstoneMode;
import exter.foundry.tileentity.TileEntityFoundryHeatable;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiCokeOven extends GuiFoundry
{

  public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/cokeoven.png");

  public static final int HEAT_BAR_X = 61;
  public static final int HEAT_BAR_Y = 57;
  public static final int HEAT_BAR_WIDTH = 61;
  public static final int HEAT_BAR_HEIGHT = 12;


  public static final int PROGRESS_X = 76;
  public static final int PROGRESS_Y = 23;
  public static final int PROGRESS_WIDTH = 23;
  public static final int PROGRESS_HEIGHT = 15;

  
  
  public static final int HEAT_BAR_OVERLAY_X = 176;
  public static final int HEAT_BAR_OVERLAY_Y = 53;

  public static final int HEAT_BAR_BAKE_X = 176;
  public static final int HEAT_BAR_BAKE_Y = 65;
  public static final int HEAT_BAR_BAKE_WIDTH = 3;
  

  public static final int PROGRESS_OVERLAY_X = 176;
  public static final int PROGRESS_OVERLAY_Y = 78;

  private static final int RSMODE_X = 176 - 16 - 4;
  private static final int RSMODE_Y = 4;
  private static final int RSMODE_TEXTURE_X = 176;
  private static final int RSMODE_TEXTURE_Y = 100;

  private TileEntityCokeOven te_oven;
  private GuiButtonFoundry button_mode;

  public GuiCokeOven(TileEntityCokeOven te, EntityPlayer player)
  {
    super(new ContainerCokeOven(te, player));
    allowUserInput = false;
    ySize = 166;
    te_oven = te;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y)
  {
    super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);

    fontRendererObj.drawString("Coke Oven", 5, 6, 0x404040);
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

    
    //Draw heat bar.
    int heat = (te_oven.getTemperature() - TileEntityFoundryHeatable.TEMP_MIN) * HEAT_BAR_WIDTH / (te_oven.getMaxTemperature() - TileEntityFoundryHeatable.TEMP_MIN);
    int bake = (TileEntityCokeOven.BAKE_TEMP - TileEntityFoundryHeatable.TEMP_MIN) * HEAT_BAR_WIDTH / (te_oven.getMaxTemperature() - TileEntityFoundryHeatable.TEMP_MIN);
    if(heat > 0)
    {
      drawTexturedModalRect(window_x + HEAT_BAR_X, window_y + HEAT_BAR_Y, HEAT_BAR_OVERLAY_X, HEAT_BAR_OVERLAY_Y, heat, HEAT_BAR_HEIGHT);
    }
    drawTexturedModalRect(window_x + HEAT_BAR_X + bake - HEAT_BAR_BAKE_WIDTH / 2, window_y + HEAT_BAR_Y, HEAT_BAR_BAKE_X, HEAT_BAR_BAKE_Y, HEAT_BAR_BAKE_WIDTH, HEAT_BAR_HEIGHT);

    //Draw progress bar.
    int progress = (te_oven.getProgress() / 100) * PROGRESS_WIDTH / (TileEntityCokeOven.BAKE_TIME / 100);
    if(progress > 0)
    {
      drawTexturedModalRect(window_x + PROGRESS_X, window_y + PROGRESS_Y, PROGRESS_OVERLAY_X, PROGRESS_OVERLAY_Y, progress, PROGRESS_HEIGHT);
    }
  }

  @Override
  public void drawScreen(int mousex, int mousey, float par3)
  {
    super.drawScreen(mousex, mousey, par3);

    //Draw tool tips.
    if(isPointInRegion(HEAT_BAR_X,HEAT_BAR_Y,HEAT_BAR_WIDTH,HEAT_BAR_HEIGHT,mousex,mousey))
    {
      List<String> currenttip = new ArrayList<String>();
      int heat = te_oven.getTemperature() / 100;
      int bake = TileEntityCokeOven.BAKE_TEMP / 100;
      currenttip.add("Temperature: " + String.valueOf(heat) + " °K");
      if(bake > 0)
      {
        currenttip.add("Bake: " + String.valueOf(bake) + " °K");
      }
      drawHoveringText(currenttip, mousex, mousey, fontRendererObj);
    }
    if(isPointInRegion(RSMODE_X,RSMODE_Y,button_mode.getWidth(),button_mode.getHeight(),mousex,mousey))
    {
      List<String> currenttip = new ArrayList<String>();
      currenttip.add(getRedstoenModeText(te_oven.getRedstoneMode()));
      drawHoveringText(currenttip, mousex, mousey, fontRendererObj);
    }
  }

  @Override
  protected ResourceLocation getGUITexture()
  {
    return GUI_TEXTURE;
  }
  
  @Override 
  public void initGui()
  {
    super.initGui();
    int window_x = (width - xSize) / 2;
    int window_y = (height - ySize) / 2;
    button_mode = new GuiButtonFoundry(
        1,
        RSMODE_X + window_x, RSMODE_Y + window_y,
        16, 15,
        GUI_TEXTURE,RSMODE_TEXTURE_X,RSMODE_TEXTURE_Y,
        RSMODE_TEXTURE_X + 16, RSMODE_TEXTURE_Y);
    buttonList.add(button_mode);
  }

  @Override
  protected void actionPerformed(GuiButton button)
  {
    if(button.id == button_mode.id)
    {
      switch(te_oven.getRedstoneMode())
      {
        case RSMODE_IGNORE:
          te_oven.setRedstoneMode(RedstoneMode.RSMODE_OFF);
          break;
        case RSMODE_OFF:
          te_oven.setRedstoneMode(RedstoneMode.RSMODE_ON);
          break;
        case RSMODE_ON:
          te_oven.setRedstoneMode(RedstoneMode.RSMODE_IGNORE);
          break;
        case RSMODE_PULSE:
          te_oven.setRedstoneMode(RedstoneMode.RSMODE_IGNORE);
          break;
      }
    }
  }
}
