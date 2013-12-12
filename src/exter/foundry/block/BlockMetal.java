package exter.foundry.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import exter.foundry.creativetab.FoundryTabBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;


public class BlockMetal extends Block
{
  static public final int BLOCK_COPPER = 0;
  static public final int BLOCK_TIN = 1;
  static public final int BLOCK_BRONZE = 2;
  static public final int BLOCK_ELECTRUM = 3;
  static public final int BLOCK_INVAR = 4;
  static public final int BLOCK_NICKEL = 5;
  static public final int BLOCK_ZINC = 6;
  static public final int BLOCK_BRASS = 7;
  static public final int BLOCK_SILVER = 8;
  static public final int BLOCK_STEEL = 9;
  static public final int BLOCK_LEAD = 10;
  static public final int BLOCK_ALUMINUM = 11;
  static public final int BLOCK_CHROMIUM = 12;
  static public final int BLOCK_PLATINUM = 13;


  static private final String[] ICON_PATHS = 
  {
    "foundry:metalblock_copper",
    "foundry:metalblock_tin",
    "foundry:metalblock_bronze",
    "foundry:metalblock_electrum",
    "foundry:metalblock_invar",
    "foundry:metalblock_nickel",
    "foundry:metalblock_zinc",
    "foundry:metalblock_brass",
    "foundry:metalblock_silver",
    "foundry:metalblock_steel",
    "foundry:metalblock_lead",
    "foundry:metalblock_aluminum",
    "foundry:metalblock_chromium",
    "foundry:metalblock_platinum"
  };
  
  static public final String[] METAL_NAMES = 
  {
    "Copper",
    "Tin",
    "Bronze",
    "Electrum",
    "Invar",
    "Nickel",
    "Zinc",
    "Brass",
    "Silver",
    "Steel",
    "Lead",
    "Aluminum",
    "Chromium",
    "Platinum"
  };

  static public final String[] NAMES = 
  {
    "Block of Copper",
    "Block of Tin",
    "Block of Bronze",
    "Block of Electrum",
    "Block of Invar",
    "Block of Nickel",
    "Block of Zinc",
    "Block of Brass",
    "Block of Silver",
    "Block of Steel",
    "Block of Lead",
    "Block of Aluminum",
    "Block of Chromium",
    "Block of Platinum"
  };

  static public final String[] OREDICT_NAMES = 
  {
    "blockCopper",
    "blockTin",
    "blockBronze",
    "blockElectrum",
    "blockInvar",
    "blockNickel",
    "blockZinc",
    "blockBrass",
    "blockSilver",
    "blockSteel",
    "blockLead",
    "blockAluminum",
    "blockChromium",
    "blockPlatinum"
  };

  @SideOnly(Side.CLIENT)
  private Icon[] icons;
  
  public BlockMetal(int id)
  {
    super(id, Material.iron);
    setHardness(1.0F);
    setResistance(8.0F);
    setUnlocalizedName("foundryMetalBlock");
    setStepSound(Block.soundMetalFootstep);
    setCreativeTab(FoundryTabBlocks.tab);
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
