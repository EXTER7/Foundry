package exter.foundry.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabBlocks;

@Deprecated // Moved to substratum
public abstract class BlockMetalSlab extends BlockSlab implements IBlockVariants
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
  
  private class PropertyVariant extends PropertyHelper<Variant>
  {
    private List<Variant> variants;
    
    public PropertyVariant(Variant[] variants)
    {
      super("metal",Variant.class);
      int i = 0;
      this.variants = new ArrayList<Variant>();
      for (Variant v : variants)
      {
        v.id = i++;
        this.variants.add(v);
      }
    }

    @Override
    public Collection<Variant> getAllowedValues()
    {
      return variants;
    }

    @Override
    public String getName(Variant value)
    {
      return value.state;
    }
  }

  private PropertyVariant property_variant;
  private BlockSlab single;

  public abstract Variant[] getVariants();
  
  
  public BlockMetalSlab(BlockSlab single)
  {
    super(Material.iron);
    this.single = single;
    setCreativeTab(FoundryTabBlocks.tab);
    setHardness(5.0F);
    setResistance(10.0F);
    setStepSound(Block.soundTypeMetal);
    setUnlocalizedName("metalSlab" + (single != null?"Double":""));
    useNeighborBrightness = true;
  }
  

  @Override
  public final int damageDropped(IBlockState state)
  {
    return state.getValue(getVariantProperty()).id;
  }

  @Override
  public final Item getItemDropped(IBlockState blockState, Random random, int unused)
  {
    if(single != null)
    {
      return Item.getItemFromBlock(single);
    } else
    {
      return Item.getItemFromBlock(this);
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public final Item getItem(World world, BlockPos pos)
  {
    if(single != null)
    {
      return Item.getItemFromBlock(single);
    } else
    {
      return Item.getItemFromBlock(this);
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
  {

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
    return getUnlocalizedName() + "." + ((Variant)getStateFromMeta(meta).getValue(getVariantProperty())).metal;
  }

  @Override
  public boolean isDouble()
  {
    return single != null;
  }

  @Override
  public IProperty<Variant> getVariantProperty()
  {
    if(property_variant == null)
    {
      property_variant = new PropertyVariant(getVariants());
    }
    return property_variant;
  }

  @Override
  public Object getVariant(ItemStack stack)
  {
    return getVariants()[stack.getMetadata()];
  }
  
  @Override
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { HALF, getVariantProperty() });
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState()
        .withProperty(getVariantProperty(), getVariants()[meta & 7])
        .withProperty(HALF, ((meta >>> 3) & 1) == 1 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    Variant var = (Variant) state.getValue(getVariantProperty());
    EnumBlockHalf half = (EnumBlockHalf) state.getValue(HALF);
    return var.id & 7 | ((half == EnumBlockHalf.TOP ? 1 : 0) << 3);
  }

  public IBlockState getBottomVariant(Variant v)
  {
    return this.getDefaultState()
        .withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM)
        .withProperty(getVariantProperty(), v);
  }

  public int getBottomVariantMeta(Variant v)
  {
    return getMetaFromState(getBottomVariant(v));
  }
}
