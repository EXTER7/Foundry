package exter.foundry.block;


import java.util.List;

import exter.foundry.creativetab.FoundryTabMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class BlockComponent extends Block implements IBlockVariants
{

  static public enum EnumVariant implements IStringSerializable
  {
    CASING(0, "casing_refractory", "componentBlockCasingRefractory"),
    CLAYBLOCK(1, "block_refractoryclay", "componentBlockRefractoryClay");

    public final int id;
    public final String name;
    public final String model;

    private EnumVariant(int id, String name,String model)
    {
      this.id = id;
      this.name = name;
      this.model = model;
    }

    @Override
    public String getName()
    {
      return name;
    }

    @Override
    public String toString()
    {
      return getName();
    }

    static public EnumVariant fromID(int num)
    {
      for(EnumVariant m : values())
      {
        if(m.id == num)
        {
          return m;
        }
      }
      return null;
    }
  }

  public static final PropertyEnum<EnumVariant> VARIANT = PropertyEnum.create("variant", EnumVariant.class);

  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, VARIANT);
  }


  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(VARIANT, EnumVariant.fromID(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return ((EnumVariant)state.getValue(VARIANT)).id;
  }

  @Override
  public int damageDropped(IBlockState state)
  {
    return getMetaFromState(state);
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
  {
    for(EnumVariant m:EnumVariant.values())
    {
      list.add(new ItemStack(item, 1, m.id));
    }
  }
    
  public ItemStack asItemStack(EnumVariant variant)
  {
    return new ItemStack(this,1,getMetaFromState(getDefaultState().withProperty(VARIANT, variant)));
  }
  
  public BlockComponent()
  {
    super(Material.rock);
    setHardness(1.0F);
    setResistance(8.0F);
    setSoundType(SoundType.STONE);
    setUnlocalizedName("foundry.componentBlock");
    setCreativeTab(FoundryTabMaterials.tab);
    setRegistryName("componentBlock");
  }


  @Override
  public String getUnlocalizedName(int meta)
  {
    return "tile.foundry." + getStateFromMeta(meta).getValue(VARIANT).model;
  }
}
