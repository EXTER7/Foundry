package exter.foundry.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabMaterials;

public class BlockFoundryOre extends Block implements IBlockVariants
{

  public enum EnumOre implements IStringSerializable
  {
    COPPER(0, "copper", "Copper", "oreCopper"),
    TIN(1, "tin", "Tin", "oreTin"),
    NICKEL(2, "nickel", "Nickel", "oreNickel"),
    ZINC(3, "zinc", "Zinc", "oreZinc"),
    SILVER(4, "silver", "Silver", "oreSilver"),
    LEAD(5, "lead", "Lead", "oreLead");

    public final int id;
    public final String name;
    public final String material_name;
    public final String oredict_name;

    private EnumOre(int id, String name,String material_name,String oredict_name)
    {
      this.id = id;
      this.name = name;
      this.material_name = material_name;
      this.oredict_name = oredict_name;
      
    }

    @Override
    public String getName()
    {
      return name;
    }

    public int getID()
    {
      return id;
    }

    @Override
    public String toString()
    {
      return getName();
    }

    static public EnumOre fromID(int num)
    {
      for(EnumOre m : values())
      {
        if(m.id == num)
        {
          return m;
        }
      }
      return null;
    }
  }

  public static final PropertyEnum<EnumOre> VARIANT = PropertyEnum.create("ore", EnumOre.class);

  public BlockFoundryOre()
  {
    super(Material.rock);
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundTypeStone);
    setUnlocalizedName("ore");
    setCreativeTab(FoundryTabMaterials.tab);
    setHarvestLevel("pickaxe", 1);
  }

  @Override
  protected BlockState createBlockState()
  {
    return new BlockState(this, VARIANT);
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(VARIANT, EnumOre.fromID(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return ((EnumOre)state.getValue(VARIANT)).id;
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
    for(EnumOre ore:EnumOre.values())
    {
      list.add(new ItemStack(item, 1, ore.id));
    }
  }
  
  public ItemStack asItemStack(EnumOre ore)
  {
    return new ItemStack(this,1,ore.id);
  }

  public IBlockState asState(EnumOre ore)
  {
    return getDefaultState().withProperty(VARIANT, ore);
  }

  @Override
  public String getUnlocalizedName(int meta)
  {
    return getUnlocalizedName() + "." + ((EnumOre)getStateFromMeta(meta).getValue(VARIANT)).material_name;
  }
}
