package exter.foundry.block;

import exter.foundry.creativetab.FoundryTabBlocks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;


public class BlockMetalStairs extends BlockStairs
{
  // Make BlockStairs's constructor accessible.
  public BlockMetalStairs(IBlockState modelState)
  {
    super(modelState);
    setCreativeTab(FoundryTabBlocks.tab);
    useNeighborBrightness = true;
  }

}
