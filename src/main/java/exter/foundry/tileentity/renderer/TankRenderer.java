package exter.foundry.tileentity.renderer;

import org.lwjgl.opengl.GL11;

import exter.foundry.tileentity.TileEntityRefractoryTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TankRenderer extends TileEntitySpecialRenderer<TileEntityRefractoryTank>
{
  private void drawQuad(EnumFacing facing,TextureAtlasSprite texture,double x1,double y1,double x2,double y2,double z,int color,int light)
  {
    color = 0xFFFFFFFF;
    float alpha = (color >> 24 & 255) / 255.0F;
    float red = (color >> 16 & 255) / 255.0F;
    float green = (color >> 8 & 255) / 255.0F;
    float blue = (color & 255) / 255.0F;

    double u1 = texture.getInterpolatedU(x1);
    double v1 = texture.getInterpolatedV(y1);
    double u2 = texture.getInterpolatedU(x2);
    double v2 = texture.getInterpolatedV(y2);

    int l1 = light >> 0x10 & 0xFFFF;
    int l2 = light & 0xFFFF;
    
    x1 /= 16;
    y1 /= 16;
    x2 /= 16;
    y2 /= 16;
    z /= 16;

    VertexBuffer tessellator = Tessellator.getInstance().getBuffer();
    tessellator.begin(7, DefaultVertexFormats.BLOCK);
    switch(facing)
    {
      case EAST:
        tessellator.pos(z, y2, x2).color(red, green, blue, alpha).tex(u2, v1).lightmap(l1, l2).endVertex();
        tessellator.pos(z, y2, x1).color(red, green, blue, alpha).tex(u1, v1).lightmap(l1, l2).endVertex();
        tessellator.pos(z, y1, x1).color(red, green, blue, alpha).tex(u1, v2).lightmap(l1, l2).endVertex();
        tessellator.pos(z, y1, x2).color(red, green, blue, alpha).tex(u2, v2).lightmap(l1, l2).endVertex();
        break;
      case WEST:
        tessellator.pos(z, y2, x1).color(red, green, blue, alpha).tex(u1, v1).lightmap(l1, l2).endVertex();
        tessellator.pos(z, y2, x2).color(red, green, blue, alpha).tex(u2, v1).lightmap(l1, l2).endVertex();
        tessellator.pos(z, y1, x2).color(red, green, blue, alpha).tex(u2, v2).lightmap(l1, l2).endVertex();
        tessellator.pos(z, y1, x1).color(red, green, blue, alpha).tex(u1, v2).lightmap(l1, l2).endVertex();
        break;
      case SOUTH:
        tessellator.pos(x2, y2, z).color(red, green, blue, alpha).tex(u1, v1).lightmap(l1, l2).endVertex();
        tessellator.pos(x1, y2, z).color(red, green, blue, alpha).tex(u2, v1).lightmap(l1, l2).endVertex();
        tessellator.pos(x1, y1, z).color(red, green, blue, alpha).tex(u2, v2).lightmap(l1, l2).endVertex();
        tessellator.pos(x2, y1, z).color(red, green, blue, alpha).tex(u1, v2).lightmap(l1, l2).endVertex();
        break;
      case NORTH:
        tessellator.pos(x1, y2, z).color(red, green, blue, alpha).tex(u2, v1).lightmap(l1, l2).endVertex();
        tessellator.pos(x2, y2, z).color(red, green, blue, alpha).tex(u1, v1).lightmap(l1, l2).endVertex();
        tessellator.pos(x2, y1, z).color(red, green, blue, alpha).tex(u1, v2).lightmap(l1, l2).endVertex();
        tessellator.pos(x1, y1, z).color(red, green, blue, alpha).tex(u2, v2).lightmap(l1, l2).endVertex();
        break;
      case UP:
        tessellator.pos(x1, z, y2).color(red, green, blue, alpha).tex(u2, v2).lightmap(l1, l2).endVertex();
        tessellator.pos(x2, z, y2).color(red, green, blue, alpha).tex(u1, v2).lightmap(l1, l2).endVertex();
        tessellator.pos(x2, z, y1).color(red, green, blue, alpha).tex(u1, v1).lightmap(l1, l2).endVertex();
        tessellator.pos(x1, z, y1).color(red, green, blue, alpha).tex(u2, v1).lightmap(l1, l2).endVertex();
        break;
      case DOWN:
        tessellator.pos(x2, z, y2).color(red, green, blue, alpha).tex(u1, v2).lightmap(l1, l2).endVertex();
        tessellator.pos(x1, z, y2).color(red, green, blue, alpha).tex(u2, v2).lightmap(l1, l2).endVertex();
        tessellator.pos(x1, z, y1).color(red, green, blue, alpha).tex(u2, v1).lightmap(l1, l2).endVertex();
        tessellator.pos(x2, z, y1).color(red, green, blue, alpha).tex(u1, v1).lightmap(l1, l2).endVertex();
        break;
    }    
    Tessellator.getInstance().draw();
  }
  
  @Override
  public void renderTileEntityAt(TileEntityRefractoryTank te, double x, double y, double z, float partialTicks, int destroyStage)
  {
    FluidStack fluid = te.getTank(0).getFluid();
    if(fluid != null && fluid.amount > 50)
    {
      GL11.glPushMatrix();
      GlStateManager.disableLighting();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GlStateManager.disableAlpha();
      bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      GlStateManager.resetColor();
      GL11.glTranslatef((float) x, (float) y, (float) z);
      TextureAtlasSprite texture = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());
      double fluid_z = (double)fluid.amount / te.getTank(0).getCapacity() * 11.99 + 2;

      int color = fluid.getFluid().getColor();
      int light = te.getWorld().getCombinedLight(te.getPos(), fluid.getFluid().getLuminosity());

      drawQuad(EnumFacing.UP,texture,0.995,0.995,15.005,15.005,fluid_z,color,light);
      drawQuad(EnumFacing.NORTH,texture,2,2,14,fluid_z,0.995,color,light);
      drawQuad(EnumFacing.SOUTH,texture,2,2,14,fluid_z,15.005,color,light);
      drawQuad(EnumFacing.EAST,texture,2,2,14,fluid_z,0.995,color,light);
      drawQuad(EnumFacing.WEST,texture,2,2,14,fluid_z,15.005,color,light);

      GlStateManager.enableAlpha();
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GL11.glPopMatrix();
    }
  }
}
