package exter.foundry.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;


public abstract class BlockMetal extends Block implements IBlockVariants
{
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


  public abstract Variant[] getVariants();
  
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
    return state.getValue(property_variant).id;
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
