package exter.foundry.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabMaterials;

public class BlockFoundryOre extends Block implements ISubBlocks
{
  static public final int ORE_COPPER = 0;
  static public final int ORE_TIN = 1;
  static public final int ORE_NICKEL = 2;
  static public final int ORE_ZINC = 3;
  static public final int ORE_SILVER = 4;
  static public final int ORE_LEAD = 5;


  static private final String[] ICON_PATHS = 
  {
    "foundry:ore_copper",
    "foundry:ore_tin",
    "foundry:ore_nickel",
    "foundry:ore_zinc",
    "foundry:ore_silver",
    "foundry:ore_lead"
  };

  static public final String[] ORE_NAMES = 
  {
    "Copper",
    "Tin",
    "Nickel",
    "Zinc",
    "Silver",
    "Lead"
  };

  static public final String[] OREDICT_NAMES = 
  {
    "oreCopper",
    "oreTin",
    "oreNickel",
    "oreZinc",
    "oreSilver",
    "oreLead"
  };

  @SideOnly(Side.CLIENT)
  private IIcon[] icons;
  
  public BlockFoundryOre()
  {
    super(Material.rock);
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundTypeStone);
    setBlockName("ore");
    setCreativeTab(FoundryTabMaterials.tab);
    setHarvestLevel("pickaxe", 1);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister register)
  {
    icons = new IIcon[ICON_PATHS.length];

    int i;
    for(i = 0; i < icons.length; i++)
    {
      icons[i] = register.registerIcon(ICON_PATHS[i]);
    }
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int meta)
  {
    return icons[meta];
  }

  @Override
  public int damageDropped(int meta)
  {
    return meta;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item item, CreativeTabs tab, @SuppressWarnings("rawtypes") List list)
  {
    int i;
    for(i = 0; i < ICON_PATHS.length; i++)
    {
      list.add(new ItemStack(item, 1, i));
    }
  }

  @Override
  public String[] GetSubNames()
  {
    return ORE_NAMES;
  }
}
