package exter.foundry.tileentity.renderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ISpoutPourDepth
{
  @SideOnly(Side.CLIENT)
  public int getSpoutPourDepth(World world,BlockPos pos,IBlockState state);
}
