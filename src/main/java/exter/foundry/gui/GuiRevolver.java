package exter.foundry.gui;

import org.lwjgl.opengl.GL11;

import exter.foundry.container.ContainerRevolver;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiRevolver extends GuiContainer
{
  private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/revolver.png");

  public GuiRevolver(ItemStack revolver,InventoryPlayer player)
  {
    super(new ContainerRevolver(revolver,player));
    ySize = 222;
  }

  protected void drawGuiContainerForegroundLayer(int par1, int par2)
  {
    fontRendererObj.drawString("Revolver Ammo", 23, 6, 4210752);
    fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
  }

  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

    mc.getTextureManager().bindTexture(GUI_TEXTURE);
    int center_x = (width - xSize) / 2;
    int center_y = (height - ySize) / 2;
    drawTexturedModalRect(center_x, center_y, 0, 0, xSize, ySize);
  }
}
