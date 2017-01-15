package exter.foundry.block;


import java.util.Random;
import exter.foundry.creativetab.FoundryTabMaterials;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class BlockRefractoryGlass extends BlockBreakable
{

  public BlockRefractoryGlass()
  {
      super(Material.GLASS, false);
      setCreativeTab(FoundryTabMaterials.tab);
      setHardness(0.4F);
      setSoundType(SoundType.GLASS);
      setUnlocalizedName("foundry.refractory_glass");
      setRegistryName("refractory_glass");
  }

  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer()
  {
      return BlockRenderLayer.TRANSLUCENT;
  }

  /**
   * Returns the quantity of items to drop on block destruction.
   */
  public int quantityDropped(Random random)
  {
      return 0;
  }

  protected boolean canSilkHarvest()
  {
      return true;
  }

  public boolean isFullCube(IBlockState state)
  {
      return false;
  }
}