package exter.foundry.block;

import exter.foundry.creativetab.FoundryTabBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;


public class BlockStairsFoundry extends BlockStairs
{
  // Make BlockStairs's constructor accessible.
  public BlockStairsFoundry( Block par2Block, int par3)
  {
    super( par2Block, par3);
    setCreativeTab(FoundryTabBlocks.tab);
    useNeighborBrightness = true;
  }

}
