package exter.foundry.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabBlocks;

public class BlockMetalSlab extends BlockSlab implements ISubBlocks
{
  private final String[] metals;
  private final String[] icons;

  @SideOnly(Side.CLIENT)
  private IIcon[] texture_icons;
  private Block other;
  
  public BlockMetalSlab(boolean is_double, Block other_block, String[] metal_names,String[] icon_names)
  {
    super(is_double, Material.iron);
    other = other_block;
    setCreativeTab(FoundryTabBlocks.tab);
    metals = metal_names;
    icons = icon_names;
    setHardness(5.0F);
    setResistance(10.0F);
    setStepSound(Block.soundTypeMetal);
    setBlockName("metalSlab");
    if(!is_double)
    {
      useNeighborBrightness = true;
    }
  }

  public void SetOtherBlock(Block block)
  {
    other = block;
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public IIcon getIcon(int side, int meta)
  {
    return texture_icons[meta & 7];
  }

  @Override
  public Item getItemDropped(int par1, Random random, int meta)
  {
    return field_150004_a ? Item.getItemFromBlock(other) : Item.getItemFromBlock(this);
  }


  @SuppressWarnings("unchecked")
  @SideOnly(Side.CLIENT)
  @Override
  public void getSubBlocks(Item item, CreativeTabs tabs, @SuppressWarnings("rawtypes") List items)
  {
    if(!field_150004_a)
    {
      int i;
      for(i = 0; i < metals.length; i++)
      {
        items.add(new ItemStack(item, 1, i));
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void registerBlockIcons(IIconRegister icon_register)
  {
    int i;
    texture_icons = new IIcon[icons.length];
    for(i = 0; i < icons.length; i++)
    {
      texture_icons[i] = icon_register.registerIcon(icons[i]);
    }
  }
  
  
  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float px, float py, float pz)
  {
    if(field_150004_a)
    {
      return false;
    }
    ItemStack item = player.getCurrentEquippedItem();
    int meta = world.getBlockMetadata(x, y, z);
    int material = meta & 7;
    boolean top = (meta & 8) == 0;
    if( (side == 1 && top || side == 0 && !top) && item != null && (item.getItem() instanceof ItemBlock && ((ItemBlock)(item.getItem())).field_150939_a == this) && item.getItemDamage() == material)
    {
      //Turn single slab into double slab
      world.setBlock(x, y, z, other,meta,3);
      if(!player.capabilities.isCreativeMode)
      {
        item.stackSize--;
      }
      world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), stepSound.getBreakSound(), (stepSound.getVolume() + 1.0F) / 2.0F, stepSound.getPitch() * 0.8F);
      return true;
    }
    return false;
  }
  
  @Override
  public boolean canSilkHarvest()
  {
    return false;
  }
  
  private static boolean isBlockSingleSlab(Block b)
  {
    if(b instanceof BlockSlab)
    {
      return !b.isOpaqueCube();
    }
    return false;
  }

  
  @SideOnly(Side.CLIENT)
  @Override
  public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
  {
    if(field_150004_a)
    {
      return super.shouldSideBeRendered(access, x, y, z, side);
    }
    if(side != 1 && side != 0 && !super.shouldSideBeRendered(access, x, y, z, side))
    {
      return false;
    }
    {
      int xx = x + Facing.offsetsXForSide[Facing.oppositeSide[side]];
      int yy = y + Facing.offsetsYForSide[Facing.oppositeSide[side]];
      int zz = z + Facing.offsetsZForSide[Facing.oppositeSide[side]];
      boolean bottom = (access.getBlockMetadata(xx, yy, zz) & 8) != 0;
      return bottom ? (side == 0 ? true : (side == 1 && super.shouldSideBeRendered(access, x, y, z, side) ? true : !isBlockSingleSlab(access.getBlock(x, y, z)) || (access.getBlockMetadata(x, y, z) & 8) == 0)) : (side == 1 ? true : (side == 0 && super.shouldSideBeRendered(access, x, y, z, side) ? true : !isBlockSingleSlab(access.getBlock(x, y, z)) || (access.getBlockMetadata(x, y, z) & 8) != 0));
    }
  }

  @Override
  public String func_150002_b(int meta)
  {
    return super.getUnlocalizedName() + "." + metals[meta];
  }

  @Override
  public String[] GetSubNames()
  {
    return metals;
  }
}
