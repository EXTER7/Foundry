package exter.foundry.block;

import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;


public class BlockMetal extends Block
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
    public int compareTo(Variant other)
    {
      return id - other.id;
    }
  }
  

//  static public final String[] METAL_NAMES = 
//  {
//    "Copper",
//    "Tin",
//    "Bronze",
//    "Electrum",
//    "Invar",
//    "Nickel",
//    "Zinc",
//    "Brass",
//    "Silver",
//    "Steel",
//    "Lead",
//    "Aluminum",
//    "Chromium",
//    "Platinum",
//    "Manganese",
//    "Titanium"
//  };
//
//  static public final String[] OREDICT_NAMES = 
//  {
//    "blockCopper",
//    "blockTin",
//    "blockBronze",
//    "blockElectrum",
//    "blockInvar",
//    "blockNickel",
//    "blockZinc",
//    "blockBrass",
//    "blockSilver",
//    "blockSteel",
//    "blockLead",
//    "blockAluminum",
//    "blockChromium",
//    "blockPlatinum",
//    "blockManganese",
//    "blockTitanium"
//  };

  public static final PropertyEnum VARIANT = PropertyEnum.create("metal", Variant.class);

  private final Variant[] variants;

  public BlockMetal(Variant[] variants)
  {
    super( Material.iron);
    setHardness(1.0F);
    setResistance(8.0F);
    this.variants = variants;
    int i;
    for(i = 0; i < variants.length; i++)
    {
      variants[i].id = i;
    }
    setUnlocalizedName("metalBlock");
    setStepSound(Block.soundTypeMetal);
    setCreativeTab(FoundryTabBlocks.tab);
  }

  @Override
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { VARIANT });
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState()
        .withProperty(VARIANT, variants[meta]);
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return ((Variant) state.getValue(VARIANT)).id;
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
    for(Variant v:variants)
    {
      list.add(new ItemStack(item, 1, v.id));
    }
  }
}
