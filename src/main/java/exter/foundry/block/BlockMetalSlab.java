package exter.foundry.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabBlocks;

public class BlockMetalSlab extends BlockSlab
{



  static public class Variant implements IStringSerializable,Comparable<Variant>
  {
    public final String state;
    public final String metal;
    public int id;
    
    public Variant(String state,String metal)
    {
      this.state = state;
      this.metal = metal;
      this.id = -1;
    }

    @Override
    public String getName()
    {
      return state;
    }

    @Override
    public String toString()
    {
      return state;
    }

    @Override
    public int compareTo(Variant other)
    {
      return id - other.id;
    }
  }
  
  public static final PropertyEnum VARIANT = PropertyEnum.create("ore", Variant.class);

  private final Variant[] variants;
  
  public BlockMetalSlab(Variant[] variants)
  {
    super(Material.iron);
    setCreativeTab(FoundryTabBlocks.tab);
    this.variants = variants;
    int i;
    for(i = 0; i < variants.length; i++)
    {
      variants[i].id = i;
    }
    setHardness(5.0F);
    setResistance(10.0F);
    setStepSound(Block.soundTypeMetal);
    setUnlocalizedName("metalSlab");
    useNeighborBrightness = true;
  }
  

  @SuppressWarnings("unchecked")
  @SideOnly(Side.CLIENT)
  @Override
  public void getSubBlocks(Item item, CreativeTabs tabs, @SuppressWarnings("rawtypes") List items)
  {
    for(Variant v:variants)
    {
      items.add(new ItemStack(item, 1, v.id));
    }
  }

  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
  {
    if(isDouble())
    {
      return super.shouldSideBeRendered(worldIn, pos, side);
    } else if(side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(worldIn, pos, side))
    {
      return false;
    } else
    {
      BlockPos blockpos1 = pos.offset(side.getOpposite());
      IBlockState iblockstate = worldIn.getBlockState(pos);
      IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);
      boolean flag = isSlab(iblockstate.getBlock()) && iblockstate.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP;
      boolean flag1 = isSlab(iblockstate1.getBlock()) && iblockstate1.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP;
      return flag1 ? (side == EnumFacing.DOWN ? true : (side == EnumFacing.UP && super.shouldSideBeRendered(worldIn, pos, side) ? true : !isSlab(iblockstate.getBlock()) || !flag)) : (side == EnumFacing.UP ? true : (side == EnumFacing.DOWN && super.shouldSideBeRendered(worldIn, pos, side) ? true : !isSlab(iblockstate.getBlock()) || flag));
    }
  }

  @SideOnly(Side.CLIENT)
  protected static boolean isSlab(Block b)
  {
    if(b instanceof BlockSlab)
    {
      return !((BlockSlab)b).isDouble();
    }
    return false;
  }

  @Override
  public boolean canSilkHarvest()
  {
    return false;
  }
    
  @Override
  public String getUnlocalizedName(int meta)
  {
    return this.getUnlocalizedName();
  }

  @Override
  public boolean isDouble()
  {
    return false;
  }

  @Override
  public IProperty getVariantProperty()
  {
    return VARIANT;
  }

  @Override
  public Object getVariant(ItemStack stack)
  {
    return variants[stack.getMetadata()];
  }
  
  @Override
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { HALF, VARIANT });
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState()
        .withProperty(VARIANT, variants[meta & 7])
        .withProperty(HALF, ((meta >>> 3) & 1) == 1 ? EnumHalf.TOP : EnumHalf.BOTTOM);
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    Variant var = (Variant) state.getValue(VARIANT);
    EnumHalf half = (EnumHalf) state.getValue(HALF);
    return var.id << 3 | (half == EnumHalf.TOP ? 1 : 0);
  }

}
