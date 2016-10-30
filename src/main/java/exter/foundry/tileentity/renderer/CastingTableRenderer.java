package exter.foundry.tileentity.renderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import exter.foundry.tileentity.TileEntityCastingTableBase;
import exter.foundry.util.hashstack.HashableItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CastingTableRenderer extends TileEntitySpecialRenderer<TileEntityCastingTableBase>
{
  private final double left;
  private final double right;
  private final double top;
  private final double bottom;
  private final double low;
  private final double high;
  private final String item_texture;
  
  public CastingTableRenderer(int left,int right, int top,int bottom,int low,int high,String item_texture)
  {
    this.left = (double)left / 16 - 0.005;
    this.right = (double)right / 16 + 0.005;
    this.top = (double)top / 16 - 0.005;
    this.bottom = (double)bottom / 16 + 0.005;
    this.low = (double)low / 16 + 0.01;
    this.high = (double)(high - 0.1) / 16;
    this.item_texture = item_texture;
    colors = new HashMap<HashableItem,Integer>();
  }
  
  private Map<HashableItem,Integer> colors;

  static private final EnumFacing[] facings = new EnumFacing[] {null, EnumFacing.DOWN, EnumFacing.UP,EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
  protected int getItemColor(ItemStack stack)
  {
    Integer color = HashableItem.getFromMap(colors, stack);
    if(color == null)
    {
      int r = 0;
      int g = 0;
      int b = 0;
      int count = 0;
      for(EnumFacing facing:facings)
      {
        List<BakedQuad> quads = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, getWorld(), Minecraft.getMinecraft().thePlayer).getQuads(null, facing, 0);
        if(quads != null)
        {
          for(BakedQuad q : quads)
          {
            TextureAtlasSprite texture = q.getSprite();
            for(int i = 0; i < texture.getFrameCount(); i++)
            {
              int[] pixels = texture.getFrameTextureData(i)[0];
              int w = texture.getIconWidth();
              int h = texture.getIconHeight();
              for(int y = 1; y < h - 1; y++)
              {
                for(int x = 1; x < w - 1; x++)
                {
                  int j = y * w + x;
                  int p = pixels[j];
                  int a = (p >>> 24) & 0xFF;
                  if(a > 127)
                  {
                    int a1 = (pixels[j - 1] >>> 24) & 0xFF;
                    int a2 = (pixels[j + 1] >>> 24) & 0xFF;
                    int a3 = (pixels[j - w] >>> 24) & 0xFF;
                    int a4 = (pixels[j + w] >>> 24) & 0xFF;
                    if(a1 > 127 && a2 > 127 && a3 > 127 && a4 > 127)
                    {
                      r += (p) & 0xFF;
                      g += (p >>> 8) & 0xFF;
                      b += (p >>> 16) & 0xFF;
                      count++;
                    }
                  }
                }
              }
            }
          }
        }
      }
      r /= count;
      g /= count;
      b /= count;
      color = 0xFF000000 | (r & 0xFF) | ((g & 0xFF) << 8) | ((b & 0xFF) << 16);
      colors.put(new HashableItem(stack), color);
    }
    return color;
  }

  protected TextureAtlasSprite getItemTexture(ItemStack stack)
  {
    return Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(item_texture);
  }
  
  protected boolean uvLockItem()
  {
    return true;
  }

  @Override
  public void renderTileEntityAt(TileEntityCastingTableBase te, double x, double y, double z, float partialTicks, int destroyStage)
  {
    FluidStack fluid = te.getTank(0).getFluid();
    GL11.glPushMatrix();
    GlStateManager.disableLighting();
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GlStateManager.disableAlpha();
    bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    GlStateManager.resetColor();
    GL11.glTranslatef((float) x, (float) y, (float) z);
    if(te.getStackInSlot(0) != null)
    {
      ItemStack stack = te.getStackInSlot(0);
      TextureAtlasSprite texture = getItemTexture(stack);
      int color =  getItemColor(stack);
      float alpha = ((color >> 24 & 255) / 255.0F);
      float red = (color >> 16 & 255) / 255.0F;
      float green = (color >> 8 & 255) / 255.0F;
      float blue = (color & 255) / 255.0F;
      boolean lock = uvLockItem();
      double min_u = texture.getInterpolatedU((lock?left:0) * 16);
      double min_v = texture.getInterpolatedV((lock?top:0) * 16);
      double max_u = texture.getInterpolatedU((lock?right:1) * 16);
      double max_v = texture.getInterpolatedV((lock?bottom:1) * 16);
      if(fluid != null)
      {
        GlStateManager.depthMask(false);
      }
      VertexBuffer tessellator = Tessellator.getInstance().getBuffer();
      tessellator.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      tessellator.pos(left, high, bottom).tex(min_u, max_v).color(red, green, blue, alpha).endVertex();
      tessellator.pos(right, high, bottom).tex(max_u, max_v).color(red, green, blue, alpha).endVertex();
      tessellator.pos(right, high, top).tex(max_u, min_v).color(red, green, blue, alpha).endVertex();
      tessellator.pos(left, high, top).tex(min_u, min_v).color(red, green, blue, alpha).endVertex();
      Tessellator.getInstance().draw();
      if(fluid != null)
      {
        GlStateManager.depthMask(true);
      }
    }
    if(fluid != null)
    {
      TextureAtlasSprite texture = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());

      int color = fluid.getFluid().getColor();
      int light = te.getWorld().getCombinedLight(te.getPos(), fluid.getFluid().getLuminosity());

      double progress = (double) te.getProgress() / TileEntityCastingTableBase.CAST_TIME;
      progress = Math.sqrt(progress);
      if(te.getStackInSlot(0) == null)
      {
        progress = 1.0f;
      }
      float alpha = ((color >> 24 & 255) / 255.0F) * (float)progress;
      float red = (color >> 16 & 255) / 255.0F;
      float green = (color >> 8 & 255) / 255.0F;
      float blue = (color & 255) / 255.0F;
      int l1 = light >> 0x10 & 0xFFFF;
      int l2 = light & 0xFFFF;
      
      double min_u = texture.getInterpolatedU(left * 16);
      double min_v = texture.getInterpolatedV(top * 16);
      double max_u = texture.getInterpolatedU(right * 16);
      double max_v = texture.getInterpolatedV(bottom * 16);
      double fluid_z = (double) fluid.amount / te.getTank(0).getCapacity() * (high - low) + low;
      VertexBuffer tessellator = Tessellator.getInstance().getBuffer();
      tessellator.begin(7, DefaultVertexFormats.BLOCK);
      tessellator.pos(left, fluid_z, bottom).color(red, green, blue, alpha).tex(min_u, max_v).lightmap(l1, l2).endVertex();
      tessellator.pos(right, fluid_z, bottom).color(red, green, blue, alpha).tex(max_u, max_v).lightmap(l1, l2).endVertex();
      tessellator.pos(right, fluid_z, top).color(red, green, blue, alpha).tex(max_u, min_v).lightmap(l1, l2).endVertex();
      tessellator.pos(left, fluid_z, top).color(red, green, blue, alpha).tex(min_u, min_v).lightmap(l1, l2).endVertex();
      Tessellator.getInstance().draw();
    }
    GlStateManager.enableAlpha();
    GlStateManager.enableLighting();
    GlStateManager.disableBlend();
    GL11.glPopMatrix();
  }
}
