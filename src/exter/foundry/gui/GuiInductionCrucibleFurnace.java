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
import exter.foundry.container.ContainerInductionCrucibleFurnace;
import exter.foundry.tileentity.TileEntityInductionCrucibleFurnace;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonMerchant;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.oredict.OreDictionary;

@SideOnly(Side.CLIENT)
public class GuiInductionCrucibleFurnace extends GuiFoundry
{

  private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/metalsmelter.png");

  private static final int TANK_WIDTH = 16;
  private static final int TANK_HEIGHT = 47;
  private static final int TANK_X = 107;
  private static final int TANK_Y = 22;

  private static final int HEAT_BAR_X = 41;
  private static final int HEAT_BAR_Y = 57;
  private static final int HEAT_BAR_WIDTH = 54;
  private static final int HEAT_BAR_HEIGHT = 12;


  private static final int PROGRESS_X = 79;
  private static final int PROGRESS_Y = 23;
  private static final int PROGRESS_WIDTH = 22;
  private static final int PROGRESS_HEIGHT = 15;

  
  private static final int TANK_OVERLAY_X = 176;
  private static final int TANK_OVERLAY_Y = 0;
  
  private static final int HEAT_BAR_OVERLAY_X = 176;
  private static final int HEAT_BAR_OVERLAY_Y = 53;

  private static final int HEAT_BAR_MELT_X = 176;
  private static final int HEAT_BAR_MELT_Y = 65;
  private static final int HEAT_BAR_MELT_WIDTH = 3;

  private static final int PROGRESS_OVERLAY_X = 176;
  private static final int PROGRESS_OVERLAY_Y = 78;

  
  private TileEntityInductionCrucibleFurnace te_icf;
  private IInventory player_inventory;

  public GuiInductionCrucibleFurnace(TileEntityInductionCrucibleFurnace ms, IInventory player_inv)
  {
    super(new ContainerInductionCrucibleFurnace(ms, player_inv));
    player_inventory = player_inv;
    allowUserInput = false;
    ySize = 166;
    te_icf = ms;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y)
  {
    super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);

    fontRenderer.drawString("Induction Crucible Furnace", 5, 6, 0x404040);
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

    int heat = (te_icf.GetHeat() - TileEntityInductionCrucibleFurnace.HEAT_MIN) * HEAT_BAR_WIDTH / (TileEntityInductionCrucibleFurnace.HEAT_MAX - TileEntityInductionCrucibleFurnace.HEAT_MIN);
    int melt_point = (te_icf.GetMeltingPoint() - TileEntityInductionCrucibleFurnace.HEAT_MIN) * HEAT_BAR_WIDTH / (TileEntityInductionCrucibleFurnace.HEAT_MAX - TileEntityInductionCrucibleFurnace.HEAT_MIN);
    int progress = te_icf.GetProgress() * PROGRESS_WIDTH / te_icf.SMELT_TIME;
    
    
    if(heat > 0)
    {
      drawTexturedModalRect(window_x + HEAT_BAR_X, window_y + HEAT_BAR_Y, HEAT_BAR_OVERLAY_X, HEAT_BAR_OVERLAY_Y, heat, HEAT_BAR_HEIGHT);
    }
    
    if(melt_point > 0)
    {
      drawTexturedModalRect(window_x + HEAT_BAR_X + melt_point - HEAT_BAR_MELT_WIDTH / 2, window_y + HEAT_BAR_Y, HEAT_BAR_MELT_X, HEAT_BAR_MELT_Y, HEAT_BAR_MELT_WIDTH, HEAT_BAR_HEIGHT);
    }
    if(progress > 0)
    {
      drawTexturedModalRect(window_x + PROGRESS_X, window_y + PROGRESS_Y, PROGRESS_OVERLAY_X, PROGRESS_OVERLAY_Y, progress, PROGRESS_HEIGHT);
    }
    
    DisplayTank(window_x, window_y, TANK_X, TANK_Y, TANK_HEIGHT,TANK_OVERLAY_X, TANK_OVERLAY_Y, te_icf.GetTank(0));
  }

  @Override
  public void drawScreen(int mouse_x, int mouse_y, float par3)
  {
    super.drawScreen(mouse_x, mouse_y, par3);
    if(isPointInRegion(TANK_X,TANK_Y,16,TANK_HEIGHT,mouse_x,mouse_y))
    {
      DisplayTankTooltip(mouse_x, mouse_y, te_icf.GetTank(0));
    }

    if(isPointInRegion(HEAT_BAR_X,HEAT_BAR_Y,HEAT_BAR_WIDTH,HEAT_BAR_HEIGHT,mouse_x,mouse_y))
    {
      List<String> list = new ArrayList<String>();
      float heat = (float)te_icf.GetHeat() / 10.0f;
      float melt_point = (float)te_icf.GetMeltingPoint() / 10.0f;
      list.add("Heat: " + String.valueOf(heat) + " K");
      if(melt_point > 0)
      {
        list.add("Melt: " + String.valueOf(melt_point) + " K");
      }
      drawHoveringText(list, mouse_x, mouse_y, fontRenderer);    
    }
  }

  @Override
  protected ResourceLocation GetGUITexture()
  {
    return GUI_TEXTURE;
  }
}
