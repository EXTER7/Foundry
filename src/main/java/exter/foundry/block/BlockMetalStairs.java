package exter.foundry.block;

import java.util.List;

import exter.foundry.creativetab.FoundryTabBlocks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Deprecated // Moved to substratum
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

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
  {

  }
}
