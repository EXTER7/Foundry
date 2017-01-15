package exter.foundry.tileentity.renderer;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CastingTableRendererBlock extends CastingTableRenderer
{
  
  public CastingTableRendererBlock()
  {
    super(2,14,2,14,2,14,"foundry:blocks/castingtable_top_block");
  }
  
  @Override
  protected int getItemColor(ItemStack stack)
  {
    return 0xFFFFFFFF;
  }
  
  @Override
  protected boolean uvLockItem()
  {
    return false;
  }

  @Override
  protected TextureAtlasSprite getItemTexture(ItemStack stack)
  {
    List<BakedQuad> quads = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, getWorld(), Minecraft.getMinecraft().player).getQuads(null, EnumFacing.UP, 0);
    if(quads != null && quads.size() > 0)
    {
      return quads.get(0).getSprite();
    }
    return super.getItemTexture(stack);
  }
}
