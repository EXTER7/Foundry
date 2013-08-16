package exter.foundry.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFoundryOre extends Block
{
  static public final int ORE_COPPER = 0;
  static public final int ORE_TIN = 1;
  static public final int ORE_NICKEL = 2;
  static public final int ORE_ZINC = 3;
  static public final int ORE_SILVER = 4;


  static private final String[] ICON_PATHS = 
  {
    "foundry:ore_copper",
    "foundry:ore_tin",
    "foundry:ore_nickel",
    "foundry:ore_zinc",
    "foundry:ore_silver"
  };
  
  static public final String[] NAMES = 
  {
    "Copper Ore",
    "Tin Ore",
    "Nickel Ore",
    "Zinc Ore",
    "Silver Ore"
  };

  static public final String[] OREDICT_NAMES = 
  {
    "oreCopper",
    "oreTin",
    "oreNickel",
    "oreZinc",
    "oreSilver"
  };

  @SideOnly(Side.CLIENT)
  private Icon[] icons;
  
  public BlockFoundryOre(int id)
  {
    super(id, Material.rock);
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(Block.soundStoneFootstep);
    setUnlocalizedName("foundryOre");
    setCreativeTab(CreativeTabs.tabMisc);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IconRegister register)
  {
    icons = new Icon[ICON_PATHS.length];

    int i;
    for(i = 0; i < icons.length; i++)
    {
      icons[i] = register.registerIcon(ICON_PATHS[i]);
    }
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public Icon getIcon(int side, int meta)
  {
    return icons[meta];
  }

  @Override
  public int damageDropped(int meta)
  {
    return meta;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(int id, CreativeTabs tab, List list)
  {
    int i;
    for(i = 0; i < ICON_PATHS.length; i++)
    {
      list.add(new ItemStack(id, 1, i));
    }
  }
}
