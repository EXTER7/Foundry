package exter.foundry.renderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.block.BlockRefractoryHopper;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.proxy.ClientFoundryProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

@SideOnly(Side.CLIENT)
public class RendererRefractoryHopper implements ISimpleBlockRenderingHandler
{
  public boolean RenderHopper(RenderBlocks rb, IBlockAccess world, int x, int y, int z, int meta, boolean item)
  {
    Tessellator tessellator = Tessellator.instance;
    int side = BlockHopper.getDirectionFromMetadata(meta);
    double d0 = 0.625D;
    rb.setRenderBounds(0.0D, d0, 0.0D, 1.0D, 1.0D, 1.0D);

    BlockRefractoryHopper hopper = FoundryBlocks.block_refractory_hopper;

    if(item)
    {
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, -1.0F, 0.0F);
      rb.renderFaceYNeg(hopper, 0.0D, 0.0D, 0.0D, rb.getBlockIconFromSideAndMetadata(hopper, 0, meta));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      rb.renderFaceYPos(hopper, 0.0D, 0.0D, 0.0D, rb.getBlockIconFromSideAndMetadata(hopper, 1, meta));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, -1.0F);
      rb.renderFaceZNeg(hopper, 0.0D, 0.0D, 0.0D, rb.getBlockIconFromSideAndMetadata(hopper, 2, meta));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      rb.renderFaceZPos(hopper, 0.0D, 0.0D, 0.0D, rb.getBlockIconFromSideAndMetadata(hopper, 3, meta));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(-1.0F, 0.0F, 0.0F);
      rb.renderFaceXNeg(hopper, 0.0D, 0.0D, 0.0D, rb.getBlockIconFromSideAndMetadata(hopper, 4, meta));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(1.0F, 0.0F, 0.0F);
      rb.renderFaceXPos(hopper, 0.0D, 0.0D, 0.0D, rb.getBlockIconFromSideAndMetadata(hopper, 5, meta));
      tessellator.draw();
    } else
    {
      rb.renderStandardBlock(hopper, x, y, z);
    }

    if(!item)
    {
      tessellator.setBrightness(hopper.getMixedBrightnessForBlock(world, x, y, z));
      int color = hopper.colorMultiplier(world, x, y, z);
      float r = (float) (color >> 16 & 255) / 255.0F;
      float g = (float) (color >> 8 & 255) / 255.0F;
      float b = (float) (color & 255) / 255.0F;

      if(EntityRenderer.anaglyphEnable)
      {
        float rr = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
        float gg = (r * 30.0F + g * 70.0F) / 100.0F;
        float bb = (r * 30.0F + b * 70.0F) / 100.0F;
        r = rr;
        g = gg;
        b = bb;
      }

      tessellator.setColorOpaque_F(r, g, b);
    }

    float f = 0.125F;

    if(item)
    {
      tessellator.startDrawingQuads();
      tessellator.setNormal(1.0F, 0.0F, 0.0F);
      rb.renderFaceXPos(hopper, (double) (-1.0F + f), 0.0D, 0.0D, hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(-1.0F, 0.0F, 0.0F);
      rb.renderFaceXNeg(hopper, (double) (1.0F - f), 0.0D, 0.0D, hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      rb.renderFaceZPos(hopper, 0.0D, 0.0D, (double) (-1.0F + f), hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, -1.0F);
      rb.renderFaceZNeg(hopper, 0.0D, 0.0D, (double) (1.0F - f), hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      rb.renderFaceYPos(hopper, 0.0D, -1.0D + d0, 0.0D, hopper.icon_inside);
      tessellator.draw();
    } else
    {
      rb.renderFaceXPos(hopper, (double) ((float) x - 1.0F + f), (double) y, (double) z, hopper.icon_outside);
      rb.renderFaceXNeg(hopper, (double) ((float) x + 1.0F - f), (double) y, (double) z, hopper.icon_outside);
      rb.renderFaceZPos(hopper, (double) x, (double) y, (double) ((float) z - 1.0F + f), hopper.icon_outside);
      rb.renderFaceZNeg(hopper, (double) x, (double) y, (double) ((float) z + 1.0F - f), hopper.icon_outside);
      rb.renderFaceYPos(hopper, (double) x, (double) ((float) y - 1.0F) + d0, (double) z, hopper.icon_inside);
    }

    //rb.setOverrideBlockTexture(hopper.icon_outside);
    double rx = 0.25D;
    double ry = 0.25D;
    rb.setRenderBounds(rx, ry, rx, 1.0D - rx, d0 - 0.002D, 1.0D - rx);
    if(item)
    {
      tessellator.startDrawingQuads();
      tessellator.setNormal(1.0F, 0.0F, 0.0F);
      rb.renderFaceXPos(hopper, 0.0D, 0.0D, 0.0D, hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(-1.0F, 0.0F, 0.0F);
      rb.renderFaceXNeg(hopper, 0.0D, 0.0D, 0.0D, hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      rb.renderFaceZPos(hopper, 0.0D, 0.0D, 0.0D, hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, -1.0F);
      rb.renderFaceZNeg(hopper, 0.0D, 0.0D, 0.0D, hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      rb.renderFaceYPos(hopper, 0.0D, 0.0D, 0.0D, hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, -1.0F, 0.0F);
      rb.renderFaceYNeg(hopper, 0.0D, 0.0D, 0.0D, hopper.icon_outside_bottom);
      tessellator.draw();
    } else
    {
      rb.renderStandardBlock(hopper, x, y, z);
    }

    double d1 = 0.375D;
    double d2 = 0.25D;
    if(item)
    {
      rb.setRenderBounds(d1, 0.0D, d1, 1.0D - d1, 0.25D, 1.0D - d1);
      tessellator.startDrawingQuads();
      tessellator.setNormal(1.0F, 0.0F, 0.0F);
      rb.renderFaceXPos(hopper, 0.0D, 0.0D, 0.0D, hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(-1.0F, 0.0F, 0.0F);
      rb.renderFaceXNeg(hopper, 0.0D, 0.0D, 0.0D, hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      rb.renderFaceZPos(hopper, 0.0D, 0.0D, 0.0D, hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, -1.0F);
      rb.renderFaceZNeg(hopper, 0.0D, 0.0D, 0.0D, hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      rb.renderFaceYPos(hopper, 0.0D, 0.0D, 0.0D, hopper.icon_outside);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, -1.0F, 0.0F);
      rb.renderFaceYNeg(hopper, 0.0D, 0.0D, 0.0D, hopper.icon_outside_bottom);
      tessellator.draw();
    } else
    {
      rb.setOverrideBlockTexture(hopper.icon_outside_bottom);

      switch(side)
      {
        case 0:
          rb.setRenderBounds(d1, 0.0D, d1, 1.0D - d1, 0.25D, 1.0D - d1);
          rb.renderStandardBlock(hopper, x, y, z);
          break;
        case 2:
          rb.setRenderBounds(d1, ry, 0.0D, 1.0D - d1, ry + d2, rx);
          rb.renderStandardBlock(hopper, x, y, z);
          break;
        case 3:
          rb.setRenderBounds(d1, ry, 1.0D - rx, 1.0D - d1, ry + d2, 1.0D);
          rb.renderStandardBlock(hopper, x, y, z);
          break;
        case 4:
          rb.setRenderBounds(0.0D, ry, d1, rx, ry + d2, 1.0D - d1);
          rb.renderStandardBlock(hopper, x, y, z);
          break;
        case 5:
          rb.setRenderBounds(1.0D - rx, ry, d1, 1.0D, ry + d2, 1.0D - d1);
          rb.renderStandardBlock(hopper, x, y, z);
          break;
      }
    }

    rb.clearOverrideBlockTexture();
    return true;
  }

  @Override
  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
  {
    RenderHopper(renderer, null, 0, 0, 0, 0, true);
  }

  @Override
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
  {
    Tessellator tessellator = Tessellator.instance;
    int color = FoundryBlocks.block_refractory_hopper.colorMultiplier(world, x, y, z);
    tessellator.setBrightness(FoundryBlocks.block_refractory_hopper.getMixedBrightnessForBlock(world, x, y, z));
    float r = (float) (color >> 16 & 255) / 255.0F;
    float g = (float) (color >> 8 & 255) / 255.0F;
    float b = (float) (color & 255) / 255.0F;

    if(EntityRenderer.anaglyphEnable)
    {
      float rr = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
      float gg = (r * 30.0F + g * 70.0F) / 100.0F;
      float bb = (r * 30.0F + b * 70.0F) / 100.0F;
      r = rr;
      g = gg;
      b = bb;
    }
    tessellator.setColorOpaque_F(r, g, b);
    return RenderHopper(renderer, world, x, y, z, world.getBlockMetadata(x, y, z), false);
  }

  @Override
  public boolean shouldRender3DInInventory(int modelId)
  {
    return true;
  }

  @Override
  public int getRenderId()
  {
    return ClientFoundryProxy.hopper_renderer_id;
  }

}
