package exter.foundry.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockCauldronBronze extends BlockCauldron
{
  public BlockCauldronBronze()
  {
    setRegistryName("bronzeCauldron");
    setUnlocalizedName("foundry.bronzeCauldron");
    setHardness(1.8F);
    setCreativeTab(CreativeTabs.BREWING);
  }

  @Nullable
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune)
  {
      return Item.getItemFromBlock(this);
  }

  @Override
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
  {
    Item item = Item.getItemFromBlock(this);
    return new ItemStack(item, 1, 0);
  }
  
  @Override
  public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
  {
    Item item = Item.getItemFromBlock(this);
    return new ItemStack(item, 1, 0);
  }
}
