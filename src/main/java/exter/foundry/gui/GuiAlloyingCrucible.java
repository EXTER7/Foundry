package exter.foundry.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import exter.foundry.container.ContainerAlloyingCrucible;
import exter.foundry.tileentity.TileEntityAlloyingCrucible;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class GuiAlloyingCrucible extends GuiFoundry
{
  private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/alloyingcrucible.png");

  public static final int TANK_HEIGHT = 35;
  
  private static final int TANK_INPUT_A_X = 35;
  private static final int TANK_INPUT_B_X = 125;
  private static final int TANK_OUTPUT_X = 80;
  private static final int TANK_Y = 45;

  private static final int TANK_OVERLAY_X = 176;
  private static final int TANK_OVERLAY_Y = 0;


  private TileEntityAlloyingCrucible te_alloymixer;

  public GuiAlloyingCrucible(TileEntityAlloyingCrucible am, EntityPlayer player)
  {
    super(new ContainerAlloyingCrucible(am, player));
    allowUserInput = false;
    ySize = 209;
    te_alloymixer = am;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y)
  {
    super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);

    
    fontRendererObj.drawString("Alloying Crucible", 5, 6, 0x404040);
    fontRendererObj.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    
  }
  
  @Override
  public void drawScreen(int mousex, int mousey, float par3)
  {
    super.drawScreen(mousex, mousey, par3);

    //Draw tool tips.
    if(isPointInRegion(TANK_INPUT_A_X, TANK_Y, 16, TANK_HEIGHT, mousex, mousey))
    {
      List<String> currenttip = new ArrayList<String>();
      addTankTooltip(currenttip, mousex, mousey, te_alloymixer.getTank(TileEntityAlloyingCrucible.TANK_INPUT_A));
      drawHoveringText(currenttip, mousex, mousey, fontRendererObj);
    }
    if(isPointInRegion(TANK_INPUT_B_X, TANK_Y, 16, TANK_HEIGHT, mousex, mousey))
    {
      List<String> currenttip = new ArrayList<String>();
      addTankTooltip(currenttip, mousex, mousey, te_alloymixer.getTank(TileEntityAlloyingCrucible.TANK_INPUT_B));
      drawHoveringText(currenttip, mousex, mousey, fontRendererObj);
    }
    if(isPointInRegion(TANK_OUTPUT_X, TANK_Y, 16, TANK_HEIGHT, mousex, mousey))
    {
      List<String> currenttip = new ArrayList<String>();
      addTankTooltip(currenttip, mousex, mousey, te_alloymixer.getTank(TileEntityAlloyingCrucible.TANK_OUTPUT));
      drawHoveringText(currenttip, mousex, mousey, fontRendererObj);
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

    displayTank(window_x, window_y, TANK_INPUT_A_X, TANK_Y, TANK_HEIGHT, TANK_OVERLAY_X, TANK_OVERLAY_Y, te_alloymixer.getTank(TileEntityAlloyingCrucible.TANK_INPUT_A));
    displayTank(window_x, window_y, TANK_INPUT_B_X, TANK_Y, TANK_HEIGHT, TANK_OVERLAY_X, TANK_OVERLAY_Y, te_alloymixer.getTank(TileEntityAlloyingCrucible.TANK_INPUT_B));
    displayTank(window_x, window_y, TANK_OUTPUT_X, TANK_Y, TANK_HEIGHT, TANK_OVERLAY_X, TANK_OVERLAY_Y, te_alloymixer.getTank(TileEntityAlloyingCrucible.TANK_OUTPUT));
  }

  @Override
  protected ResourceLocation getGUITexture()
  {
    return GUI_TEXTURE;
  }
}
