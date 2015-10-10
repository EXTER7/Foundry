package exter.foundry.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
  
  private class PropertyVariant implements IProperty
  {
    private List<Variant> variants;
    
    public PropertyVariant(Variant[] variants)
    {
      int i = 0;
      this.variants = new ArrayList<Variant>();
      for (Variant v : variants)
      {
        v.id = i++;
        this.variants.add(v);
      }
    }
    
    @Override
    public String getName()
    {
      return "metal";
    }

    @Override
    public Collection<Variant> getAllowedValues()
    {
      return variants;
    }

    @Override
    public Class<?> getValueClass()
    {
      return Variant.class;
    }

    @Override
    public String getName(@SuppressWarnings("rawtypes") Comparable value)
    {
      return ((Variant)value).state;
    }
  }

  private PropertyVariant property_variant;

  public abstract Variant[] getVariants();
  
  public BlockMetalSlab()
  {
    super(Material.iron);
    setCreativeTab(FoundryTabBlocks.tab);
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
    for(Variant v:getVariants())
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
    return getUnlocalizedName() + "." + ((Variant)getStateFromMeta(meta).getValue(property_variant)).metal;
  }

  @Override
  public boolean isDouble()
  {
    return false;
  }

  @Override
  public IProperty getVariantProperty()
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
    if(property_variant == null)
    {
      property_variant = new PropertyVariant(getVariants());
    }
    return new BlockState(this, new IProperty[] { HALF, property_variant });
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState()
        .withProperty(property_variant, getVariants()[meta & 7])
        .withProperty(HALF, ((meta >>> 3) & 1) == 1 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    Variant var = (Variant) state.getValue(property_variant);
    EnumBlockHalf half = (EnumBlockHalf) state.getValue(HALF);
    return var.id & 7 | ((half == EnumBlockHalf.TOP ? 1 : 0) << 3);
  }

  public IBlockState getBottomVariant(Variant v)
  {
    return this.getDefaultState()
        .withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM)
        .withProperty(property_variant, v);
  }

  public int getBottomVariantMeta(Variant v)
  {
    return getMetaFromState(getBottomVariant(v));
  }
}
