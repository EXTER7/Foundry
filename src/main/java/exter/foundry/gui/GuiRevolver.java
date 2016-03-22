package exter.foundry.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import exter.foundry.container.ContainerRevolver;
import exter.foundry.container.slot.SlotFirearmAmmo;
import exter.foundry.gui.button.GuiButtonFoundry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GuiRevolver extends GuiContainer
{
  private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/revolver.png");

  private GuiButtonFoundry button_unload;
  
  public GuiRevolver(ItemStack revolver,InventoryPlayer player)
  {
    super(new ContainerRevolver(revolver,player));
    ySize = 222;
  }

  @Override 
  public void initGui()
  {
    super.initGui();
    int window_x = (width - xSize) / 2;
    int window_y = (height - ySize) / 2;
    button_unload = new GuiButtonFoundry(
        1,
        82 + window_x, 67 + window_y,
        12, 12,
        GUI_TEXTURE,
        176,0,
        188, 0);
    buttonList.add(button_unload);
  }

  protected void drawGuiContainerForegroundLayer(int par1, int par2)
  {
    fontRendererObj.drawString("Revolver Ammo", 23, 6, 4210752);
    fontRendererObj.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
  }

  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

    mc.getTextureManager().bindTexture(GUI_TEXTURE);
    int center_x = (width - xSize) / 2;
    int center_y = (height - ySize) / 2;
    drawTexturedModalRect(center_x, center_y, 0, 0, xSize, ySize);
  }
  
  @Override
  protected void actionPerformed(GuiButton button)
  {
    if(button.id == button_unload.id)
    {
      for(Slot slot:((List<Slot>)inventorySlots.inventorySlots))
      {
        if(slot instanceof SlotFirearmAmmo)
        {
          this.handleMouseClick(slot, slot.slotNumber, 0, ClickType.QUICK_MOVE);
        }
      }
    }
  }
}
