package exter.foundry.block;

import exter.foundry.creativetab.FoundryTabBlocks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;


public class BlockMetalStairs extends BlockStairs
{
  // Make BlockStairs's constructor accessible.
  public BlockMetalStairs(IBlockState modelState,String metal)
  {
    super(modelState);
    setCreativeTab(FoundryTabBlocks.tab);
    setUnlocalizedName("metalStairs." + metal);
    useNeighborBrightness = true;
  }

}
