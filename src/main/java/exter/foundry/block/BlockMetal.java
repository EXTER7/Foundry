package exter.foundry.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;


public abstract class BlockMetal extends Block implements IBlockVariants
{
//  static public final int BLOCK_COPPER = 0;
//  static public final int BLOCK_TIN = 1;
//  static public final int BLOCK_BRONZE = 2;
//  static public final int BLOCK_ELECTRUM = 3;
//  static public final int BLOCK_INVAR = 4;
//  static public final int BLOCK_NICKEL = 5;
//  static public final int BLOCK_ZINC = 6;
//  static public final int BLOCK_BRASS = 7;
//  static public final int BLOCK_SILVER = 8;
//  static public final int BLOCK_STEEL = 9;
//  static public final int BLOCK_LEAD = 10;
//  static public final int BLOCK_ALUMINUM = 11;
//  static public final int BLOCK_CHROMIUM = 12;
//  static public final int BLOCK_PLATINUM = 13;
//  static public final int BLOCK_MANGANESE = 14;
//  static public final int BLOCK_TITANIUM = 15;


  static public class Variant implements IStringSerializable,Comparable<Variant>
  {
    public final String state;
    public final String metal;
    public final String oredict;
    public int id;
    
    public Variant(String state,String metal,String oredict)
    {
      this.state = state;
      this.metal = metal;
      this.oredict = oredict;
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
    public int compareTo(Variant o)
    {
      return id - o.id;
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


  protected abstract Variant[] getVariants();
  
  public BlockMetal()
  {
    super( Material.iron );
    setHardness(1.0F);
    setResistance(8.0F);
    setUnlocalizedName("metalBlock");
    setStepSound(Block.soundTypeMetal);
    setCreativeTab(FoundryTabBlocks.tab);
  }
  
  @Override
  protected BlockState createBlockState()
  {
    if(property_variant == null)
    {
      property_variant = new PropertyVariant(getVariants());
    }
    return new BlockState(this, property_variant );
  }

  public IBlockState getVariantState(Variant var)
  {
    return getDefaultState().withProperty(property_variant, var);
  }
  
  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState()
        .withProperty(property_variant, getVariants()[meta]);
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return ((Variant) state.getValue(property_variant)).id;
  }

  @Override
  public int damageDropped(IBlockState state)
  {
    return getMetaFromState(state);
  }
    
  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item item, CreativeTabs tab, @SuppressWarnings("rawtypes") List list)
  {
    for(Variant v:getVariants())
    {
      list.add(new ItemStack(item, 1, v.id));
    }
  }
  
  @Override
  public String getUnlocalizedName(int meta)
  {
    return getUnlocalizedName() + "." + ((Variant)getStateFromMeta(meta).getValue(property_variant)).metal;
  }
}
