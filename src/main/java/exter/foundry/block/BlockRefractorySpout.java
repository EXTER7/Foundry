package exter.foundry.block;

import java.util.Random;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabMachines;
import exter.foundry.tileentity.TileEntityFoundry;
import exter.foundry.tileentity.TileEntityRefractorySpout;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRefractorySpout extends BlockContainer
{

  public enum EnumSpoutFacing implements IStringSerializable
  {
    NORTH(0, "north", EnumFacing.NORTH),
    SOUTH(1, "south", EnumFacing.SOUTH),
    EAST(2, "east", EnumFacing.EAST),
    WEST(3, "west", EnumFacing.WEST);

    public final int id;
    public final String name;
    public final EnumFacing facing;

    private EnumSpoutFacing(int id, String name, EnumFacing target)
    {
      this.id = id;
      this.name = name;
      this.facing = target;
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

    static public EnumSpoutFacing fromID(int num)
    {
      for(EnumSpoutFacing m : values())
      {
        if(m.id == num)
        {
          return m;
        }
      }
      return null;
    }
  }

  public static final PropertyEnum<EnumSpoutFacing> FACING = PropertyEnum.create("facing", EnumSpoutFacing.class);


  private Random rand = new Random();



  

  protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.125, 0.125, 0, 0.875, 0.875, 0.4375);
  protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0, 0.125, 0.125, 0.4375, 0.875, 0.875);
  protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.125, 0.125, 0.5625, 0.875, 0.875,1);
  protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.5625, 0.125, 0.125, 1, 0.875, 0.875);

  
  public BlockRefractorySpout()
  {
    super(Material.iron);
    setCreativeTab(FoundryTabMachines.tab);
    setHardness(1.0F);
    setResistance(8.0F);
    setUnlocalizedName("foundry.refractorySpout");
    setDefaultState(blockState.getBaseState().withProperty(FACING, EnumSpoutFacing.NORTH));
    setRegistryName("refractorySpout");
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
  {
    switch(state.getValue(FACING))
    {
      case EAST:
        return AABB_EAST;
      case NORTH:
        return AABB_NORTH;
      case SOUTH:
        return AABB_SOUTH;
      case WEST:
        return AABB_WEST;
    }
    return null;
  }


  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, FACING);
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(FACING, EnumSpoutFacing.fromID(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return ((EnumSpoutFacing) state.getValue(FACING)).id;
  }


  @Override
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
  {
    switch(facing)
    {
      case EAST:
        return getDefaultState().withProperty(FACING, EnumSpoutFacing.WEST);
      case NORTH:
        return getDefaultState().withProperty(FACING, EnumSpoutFacing.SOUTH);
      case SOUTH:
        return getDefaultState().withProperty(FACING, EnumSpoutFacing.NORTH);
      case WEST:
        return getDefaultState().withProperty(FACING, EnumSpoutFacing.EAST);
      default:
        return null;
    }
  }

  @Override
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
  {
    return side != EnumFacing.UP && side != EnumFacing.DOWN && canPlaceBlockAt(worldIn, pos);
  }
  
  @Override
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
  {
    for(EnumSpoutFacing state : FACING.getAllowedValues())
    {
      BlockPos blockpos = pos.offset(state.facing.getOpposite());
      if(worldIn.isSideSolid(blockpos, state.facing, true))
      {
        return true;
      }
    }

    return false;
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta)
  {
    return new TileEntityRefractorySpout();
  }

  @Override
  public boolean onBlockEventReceived(World world, BlockPos pos, IBlockState state, int par5, int par6)
  {
    super.onBlockEventReceived(world, pos, state, par5, par6);
    TileEntity tileentity = world.getTileEntity(pos);
    return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state)
  {
    TileEntity te = world.getTileEntity(pos);

    if(te != null && (te instanceof TileEntityFoundry) && !world.isRemote)
    {
      TileEntityFoundry tef = (TileEntityFoundry) te;
      int i;
      for(i = 0; i < tef.getSizeInventory(); i++)
      {
        ItemStack is = tef.getStackInSlot(i);

        if(is != null && is.stackSize > 0)
        {
          double drop_x = (rand.nextFloat() * 0.3) + 0.35;
          double drop_y = (rand.nextFloat() * 0.3) + 0.35;
          double drop_z = (rand.nextFloat() * 0.3) + 0.35;
          EntityItem entityitem = new EntityItem(world, pos.getX() + drop_x, pos.getY() + drop_y, pos.getZ() + drop_z, is);
          entityitem.setPickupDelay(10);

          world.spawnEntityInWorld(entityitem);
        }
      }
    }
    world.removeTileEntity(pos);
    super.breakBlock(world, pos, state);
  }

  @Override
  public EnumBlockRenderType getRenderType(IBlockState state)
  {
    return EnumBlockRenderType.MODEL;
  }

  @Override
  public boolean isFullCube(IBlockState state)
  {
    return false;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state)
  {
    return false;
  }
  
  @Override
  public boolean isFullyOpaque(IBlockState state)
  {
    return false;
  }
  
  @Override
  public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
  {
    return false;
  }

  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
  {
    return true;
  }
  
  @Override
  public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighbor)
  {
    EnumFacing facing = state.getValue(FACING).facing;

    if(!world.isSideSolid(pos.offset(facing), facing.getOpposite(), true))
    {
      dropBlockAsItem(world, pos, state, 0);
      world.setBlockToAir(pos);
    }
  }
}
