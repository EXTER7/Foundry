package exter.foundry.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabBlocks;

public class BlockMetalSlab extends BlockHalfSlab
{
  private final String[] metals;
  private final String[] icons;

  @SideOnly(Side.CLIENT)
  private Icon[] texture_icons;
  private int other;
  
  public BlockMetalSlab(int id, boolean is_double, int other_id, String[] metal_names,String[] icon_names)
  {
    super(id, is_double, Material.iron);
    other = other_id;
    setCreativeTab(FoundryTabBlocks.tab);
    metals = metal_names;
    icons = icon_names;
    setHardness(5.0F);
    setResistance(10.0F);
    setStepSound(soundMetalFootstep);
    if(!is_double)
    {
      useNeighborBrightness[id] = true;
    }
  }

  public void SetOtherBlockID(int id)
  {
    other = id;
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public Icon getIcon(int side, int meta)
  {
    return texture_icons[meta & 7];
  }

  @Override
  public int idDropped(int id, Random random, int meta)
  {
    return isDoubleSlab ? other : blockID;
  }

  @Override
  public String getFullSlabName(int meta)
  {
    return super.getUnlocalizedName() + "." + metals[meta];
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void getSubBlocks(int id, CreativeTabs tabs, List items)
  {
    if(!isDoubleSlab)
    {
      int i;
      for(i = 0; i < metals.length; i++)
      {
        items.add(new ItemStack(id, 1, i));
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void registerIcons(IconRegister icon_register)
  {
    int i;
    texture_icons = new Icon[icons.length];
    for(i = 0; i < icons.length; i++)
    {
      texture_icons[i] = icon_register.registerIcon(icons[i]);
    }
  }
  
  
  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float px, float py, float pz)
  {
    if(isDoubleSlab)
    {
      return false;
    }
    ItemStack item = player.getCurrentEquippedItem();
    int meta = world.getBlockMetadata(x, y, z);
    int material = meta & 7;
    boolean top = (meta & 8) == 0;
    if( (side == 1 && top || side == 0 && !top) && item != null && item.itemID == blockID && item.getItemDamage() == material)
    {
      //Turn single slab into double slab
      world.setBlock(x, y, z, other,meta,3);
      if(!player.capabilities.isCreativeMode)
      {
        item.stackSize--;
      }
      world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), stepSound.getPlaceSound(), (stepSound.getVolume() + 1.0F) / 2.0F, stepSound.getPitch() * 0.8F);
      return true;
    }
    return false;
  }
  
  @Override
  public boolean canSilkHarvest()
  {
    return false;
  }
  
  private static boolean isBlockSingleSlab(int id)
  {
    Block block = Block.blocksList[id];
    if(block instanceof BlockHalfSlab)
    {
      return !block.isOpaqueCube();
    }
    return false;
  }

  
  @SideOnly(Side.CLIENT)
  @Override
  public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
  {
    if(this.isDoubleSlab)
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
      return bottom ? (side == 0 ? true : (side == 1 && super.shouldSideBeRendered(access, x, y, z, side) ? true : !isBlockSingleSlab(access.getBlockId(x, y, z)) || (access.getBlockMetadata(x, y, z) & 8) == 0)) : (side == 1 ? true : (side == 0 && super.shouldSideBeRendered(access, x, y, z, side) ? true : !isBlockSingleSlab(access.getBlockId(x, y, z)) || (access.getBlockMetadata(x, y, z) & 8) != 0));
    }
  }

}
