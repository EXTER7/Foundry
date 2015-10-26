package exter.foundry.item;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;

public class ItemBlockSlab extends ItemSlab
{
  public ItemBlockSlab(final Block block, final ImmutablePair<BlockSlab,Object> slabdouble)
  {
    super((BlockSlab) block, (BlockSlab) block, slabdouble.left);
  }

}
