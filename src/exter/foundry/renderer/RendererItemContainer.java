package exter.foundry.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryContainer;
import exter.foundry.util.FoundryContainerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

@SideOnly(Side.CLIENT)
public class RendererItemContainer implements IItemRenderer
{
  private static RenderItem renderItem = new RenderItem();
  private static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;
  private static final ResourceLocation ITEM_TEXTURE = TextureMap.locationItemsTexture;
  
  @Override
  public boolean handleRenderType(ItemStack itemStack, ItemRenderType type)
  {
    return type == ItemRenderType.INVENTORY;
  }

  @Override
  public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
  {
    return false;
  }

  private void renderIconPartial(int x, int y, Icon icon, int width, int height, int icon_x, int icon_y)
  {
    Tessellator tessellator = Tessellator.instance;

    double min_u = icon.getInterpolatedU(icon_x);
    double min_v = icon.getInterpolatedV(icon_y);
    double max_u = icon.getInterpolatedU(icon_x + width);
    double max_v = icon.getInterpolatedV(icon_y + height);

    tessellator.startDrawingQuads();
    tessellator.addVertexWithUV(x, y + height, 0, min_u, max_v);
    tessellator.addVertexWithUV(x + width, y + height, 0, max_u, max_v);
    tessellator.addVertexWithUV(x + width, y, 0, max_u, min_v);
    tessellator.addVertexWithUV(x, y, 0, min_u, min_v);
    tessellator.draw();
  }

  @Override
  public void renderItem(ItemRenderType type, ItemStack stack, Object... data)
  {
    ItemFoundryContainer item = (ItemFoundryContainer) stack.getItem();
    FluidStack fluid_stack = FoundryContainerHandler.instance.GetFluidStack(stack);
    
    renderItem.renderIcon(0, 0, item.icon_bg, 16, 16);
    if(fluid_stack != null)
    {
      Icon fluid_icon = fluid_stack.getFluid().getStillIcon();
      if(fluid_icon != null)
      {
        int h = fluid_stack.amount * 10 / FluidContainerRegistry.BUCKET_VOLUME;
        if(h > 0)
        {
          if(type == type.ENTITY || type == ItemRenderType.EQUIPPED_FIRST_PERSON)
          {
            GL11.glScalef(1, 1, 0.9f);
          }
          Minecraft mc = Minecraft.getMinecraft();
          mc.renderEngine.bindTexture(BLOCK_TEXTURE);
          renderIconPartial(4, 16 - 3 - h, fluid_icon, 8, h, 4, 16 - 3 - h);
          mc.renderEngine.bindTexture(ITEM_TEXTURE);
        }
      }
    }
    renderItem.renderIcon(0, 0, item.icon_fg, 16, 16);
  }
}
