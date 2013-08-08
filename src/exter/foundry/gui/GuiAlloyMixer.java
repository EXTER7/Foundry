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
import exter.foundry.tileentity.TileEntityAlloyMixer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonMerchant;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

@SideOnly(Side.CLIENT)
public class GuiAlloyMixer extends GuiFoundry
{

  private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/alloymixer.png");
  private static final ResourceLocation BLOCK_TEXTURE = TextureMap.field_110575_b;

  private static final int TANK_HEIGHT = 47;
  
  private static final int TANK_INPUT_A_X = 36;
  private static final int TANK_INPUT_A_Y = 21;

  private static final int TANK_INPUT_B_X = 124;
  private static final int TANK_INPUT_B_Y = 21;

  private static final int TANK_OUTPUT_X = 80;
  private static final int TANK_OUTPUT_Y = 21;

  private static final int TANK_OVERLAY_X = 176;
  private static final int TANK_OVERLAY_Y = 0;

  private TileEntityAlloyMixer te_alloymixer;
  private IInventory player_inventory;

  public GuiAlloyMixer(TileEntityAlloyMixer am, IInventory player_inv)
  {
    super(new ContainerAlloyMixer(am, player_inv));
    player_inventory = player_inv;
    allowUserInput = false;
    ySize = 166;
    te_alloymixer = am;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int par1, int par2)
  {
    super.drawGuiContainerForegroundLayer(par1, par2);
    //String title = StringUtils.localize("tile.engineIron");
    fontRenderer.drawString("Alloy Mixer", 5, 6, 0x404040);
    fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    mc.renderEngine.func_110577_a(GUI_TEXTURE);
    int window_x = (width - xSize) / 2;
    int window_y = (height - ySize) / 2;
    drawTexturedModalRect(window_x, window_y, 0, 0, xSize, ySize);

    DisplayTank(window_x, window_y, TANK_INPUT_A_X, TANK_INPUT_A_Y, TANK_HEIGHT, TANK_OVERLAY_X, TANK_OVERLAY_Y, te_alloymixer.GetInputATank());
    DisplayTank(window_x, window_y, TANK_INPUT_B_X, TANK_INPUT_B_Y, TANK_HEIGHT, TANK_OVERLAY_X, TANK_OVERLAY_Y, te_alloymixer.GetInputBTank());
    DisplayTank(window_x, window_y, TANK_OUTPUT_X, TANK_OUTPUT_Y, TANK_HEIGHT, TANK_OVERLAY_X, TANK_OVERLAY_Y, te_alloymixer.GetOutputTank());
  }

  @Override
  protected ResourceLocation GetGUITexture()
  {
    return GUI_TEXTURE;
  }
}
