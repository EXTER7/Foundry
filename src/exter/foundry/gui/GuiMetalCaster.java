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
import exter.foundry.container.ContainerMetalCaster;
import exter.foundry.tileentity.TileEntityMetalCaster;
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
public class GuiMetalCaster extends GuiContainer
{

  private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/caster.png");
  private static final ResourceLocation BLOCK_TEXTURE = TextureMap.field_110575_b;

  private static final int TANK_HEIGHT = 47;
  private static final int TANK_X = 25;
  private static final int TANK_Y = 21;
  
  private static final int PROGRESS_X = 49;
  private static final int PROGRESS_Y = 51;
  private static final int PROGRESS_WIDTH = 22;
  private static final int PROGRESS_HEIGHT = 15;
  
  private static final int TANK_OVERLAY_X = 176;
  private static final int TANK_OVERLAY_Y = 0;

  private static final int PROGRESS_OVERLAY_X = 176;
  private static final int PROGRESS_OVERLAY_Y = 53;

  private TileEntityMetalCaster te_caster;
  private IInventory player_inventory;

  public GuiMetalCaster(TileEntityMetalCaster cs, IInventory player_inv)
  {
    super(new ContainerMetalCaster(cs, player_inv));
    player_inventory = player_inv;
    allowUserInput = false;
    ySize = 166;
    te_caster = cs;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int par1, int par2)
  {
    super.drawGuiContainerForegroundLayer(par1, par2);
    fontRenderer.drawString("Metal Caster", 5, 6, 0x404040);
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
    int progress = te_caster.GetProgress() * PROGRESS_WIDTH / te_caster.CAST_TIME;

    if(progress > 0)
    {
      drawTexturedModalRect(window_x + PROGRESS_X, window_y + PROGRESS_Y, PROGRESS_OVERLAY_X, PROGRESS_OVERLAY_Y, progress, PROGRESS_HEIGHT);
    }
    DisplayTank(window_x, window_y, TANK_X, TANK_Y, TANK_HEIGHT, te_caster.GetTank());

  }

  private void DisplayTank(int window_x,int window_y,int x, int y, int tank_height, FluidTank tank)
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

        drawTexturedModelRectFromIcon(window_x + x, window_y + y + tank_height - i - start, liquid_icon, 16, i);
        start += 16;

        if(i == 0 || h == 0)
        {
          break;
        }
      }
    }

    mc.renderEngine.func_110577_a(GUI_TEXTURE);
    drawTexturedModalRect(window_x + x, window_y + y, TANK_OVERLAY_X, TANK_OVERLAY_Y, 16, TANK_HEIGHT);
  }
}
