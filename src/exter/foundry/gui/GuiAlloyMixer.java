package exter.foundry.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.container.ContainerAlloyMixer;
import exter.foundry.gui.button.GuiButtonRedstoneMode;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

@SideOnly(Side.CLIENT)
public class GuiAlloyMixer extends GuiFoundry
{
  private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/alloymixer.png");
  private static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;

  private static final int TANK_HEIGHT = 35;
  
  private static final int[] TANK_X = new int[] {26, 47, 68, 89, 133};
  private static final int TANK_Y = 45;

  private static final int TANK_OVERLAY_X = 176;
  private static final int TANK_OVERLAY_Y = 0;

  private static final int RSMODE_X = 176 - GuiButtonRedstoneMode.TEXTURE_WIDTH - 4;
  private static final int RSMODE_Y = 4;
  private static final int RSMODE_TEXTURE_X = 176;
  private static final int RSMODE_TEXTURE_Y = 50;

  private TileEntityAlloyMixer te_alloymixer;
  private IInventory player_inventory;
  private GuiButtonRedstoneMode button_mode;

  public GuiAlloyMixer(TileEntityAlloyMixer am, IInventory player_inv)
  {
    super(new ContainerAlloyMixer(am, player_inv));
    player_inventory = player_inv;
    allowUserInput = false;
    ySize = 209;
    te_alloymixer = am;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y)
  {
    super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);

    fontRenderer.drawString("Alloy Mixer", 5, 6, 0x404040);
    fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    
  }
  
  @Override
  public void drawScreen(int mouse_x, int mouse_y, float par3)
  {
    super.drawScreen(mouse_x, mouse_y, par3);
    
    //Draw tool tips.

    int i;
    for(i = 0; i < 5; i++)
    {
      if(isPointInRegion(TANK_X[i],TANK_Y,16,TANK_HEIGHT,mouse_x,mouse_y))
      {
        DisplayTankTooltip(mouse_x, mouse_y, te_alloymixer.GetTank(i));
      }
    }
    
    if(isPointInRegion(RSMODE_X,RSMODE_Y,GuiButtonRedstoneMode.TEXTURE_WIDTH,GuiButtonRedstoneMode.TEXTURE_HEIGHT,mouse_x,mouse_y))
    {
      List<String> list = new ArrayList<String>();
      switch(te_alloymixer.GetMode())
      {
        case RSMODE_IGNORE:
          list.add("Mode: Ignore Restone");
          break;
        case RSMODE_OFF:
          list.add("Mode: Redstone signal OFF");
          break;
        case RSMODE_ON:
          list.add("Mode: Redstone signal ON");
          break;
      }
      drawHoveringText(list, mouse_x, mouse_y, fontRenderer);
    }

  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
  {
    int i;
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    mc.renderEngine.bindTexture(GUI_TEXTURE);
    int window_x = (width - xSize) / 2;
    int window_y = (height - ySize) / 2;
    drawTexturedModalRect(window_x, window_y, 0, 0, xSize, ySize);

    for(i = 0; i < 5; i++)
    {
      DisplayTank(window_x, window_y, TANK_X[i], TANK_Y, TANK_HEIGHT, TANK_OVERLAY_X, TANK_OVERLAY_Y, te_alloymixer.GetTank(i));
    }
  }

  @Override 
  public void initGui()
  {
    super.initGui();
    int window_x = (width - xSize) / 2;
    int window_y = (height - ySize) / 2;
    button_mode = new GuiButtonRedstoneMode(1, RSMODE_X + window_x, RSMODE_Y + window_y,GUI_TEXTURE,RSMODE_TEXTURE_X,RSMODE_TEXTURE_Y);
    buttonList.add(button_mode);
  }

  @Override
  protected ResourceLocation GetGUITexture()
  {
    return GUI_TEXTURE;
  }
  
  @Override
  protected void actionPerformed(GuiButton button)
  {
    if(button.id == button_mode.id)
    {
      te_alloymixer.SetMode(te_alloymixer.GetMode().Next());
    }
  }
}
