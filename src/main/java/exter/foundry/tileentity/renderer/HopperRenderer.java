package exter.foundry.tileentity.renderer;

import org.lwjgl.opengl.GL11;

import exter.foundry.tileentity.TileEntityRefractoryHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HopperRenderer extends TileEntitySpecialRenderer<TileEntityRefractoryHopper>
{

  @Override
  public void renderTileEntityAt(TileEntityRefractoryHopper te, double x, double y, double z, float partialTicks, int destroyStage)
  {
    FluidStack fluid = te.getTank(0).getFluid();
    BlockPos pos = te.getPos().up();
    World world = te.getWorld();
    if(fluid != null && fluid.amount > 80 && pos.getY() < world.getHeight() && !world.getBlockState(pos).doesSideBlockRendering(world, pos, EnumFacing.DOWN))
    {
      GL11.glPushMatrix();
      GlStateManager.disableLighting();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GlStateManager.disableAlpha();
      bindTexture(TextureMap.locationBlocksTexture);
      GlStateManager.resetColor();
      GL11.glTranslatef((float) x, (float) y, (float) z);
      TextureAtlasSprite texture = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());

      int color = fluid.getFluid().getColor();
      int light = te.getWorld().getCombinedLight(te.getPos(), fluid.getFluid().getLuminosity());

      float alpha = (color >> 24 & 255) / 255.0F;
      float red = (color >> 16 & 255) / 255.0F;
      float green = (color >> 8 & 255) / 255.0F;
      float blue = (color & 255) / 255.0F;
      int l1 = light >> 0x10 & 0xFFFF;
      int l2 = light & 0xFFFF;
      
      double min_u = texture.getInterpolatedU(1.995);
      double min_v = texture.getInterpolatedV(1.995);
      double max_u = texture.getInterpolatedU(14.005);
      double max_v = texture.getInterpolatedV(14.005);
      double fluid_z = (double) (fluid.amount - 80) / (te.getTank(0).getCapacity() - 80) * 4.75 + 11;
      VertexBuffer tessellator = Tessellator.getInstance().getBuffer();
      tessellator.begin(7, DefaultVertexFormats.BLOCK);
      tessellator.pos(1.995 / 16, fluid_z / 16, 14.005 / 16).color(red, green, blue, alpha).tex(min_u, max_v).lightmap(l1, l2).endVertex();
      tessellator.pos(14.005 / 16, fluid_z / 16, 14.005 / 16).color(red, green, blue, alpha).tex(max_u, max_v).lightmap(l1, l2).endVertex();
      tessellator.pos(14.005 / 16, fluid_z / 16, 1.995 / 16).color(red, green, blue, alpha).tex(max_u, min_v).lightmap(l1, l2).endVertex();
      tessellator.pos(1.995 / 16, fluid_z / 16, 1.995 / 16).color(red, green, blue, alpha).tex(min_u, min_v).lightmap(l1, l2).endVertex();
      Tessellator.getInstance().draw();
      GlStateManager.enableAlpha();
      GlStateManager.enableLighting();
      GlStateManager.disableBlend();
      GL11.glPopMatrix();
    }
  }
}
