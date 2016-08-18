package exter.foundry.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import exter.foundry.creativetab.FoundryTabMachines;
import exter.foundry.tileentity.TileEntityRefractorySpout;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRefractorySpout extends BlockFoundrySidedMachine
{
  private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.125, 0.125, 0, 0.875, 0.875, 0.4375);
  private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0, 0.125, 0.125, 0.4375, 0.875, 0.875);
  private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.125, 0.125, 0.5625, 0.875, 0.875,1);
  private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.5625, 0.125, 0.125, 1, 0.875, 0.875);

  private static final AxisAlignedBB[] COLLISION_NORTH = new AxisAlignedBB[]
  {
      new AxisAlignedBB(0.125,  0.125,  0,      0.875,  0.875, 0.1875),
      new AxisAlignedBB(0.3125, 0.4375, 0.1875, 0.6875, 0.6875, 0.4375)
  };
  
  private static final AxisAlignedBB[] COLLISION_WEST = new AxisAlignedBB[]
  {
      new AxisAlignedBB(0,      0.125,  0.125,  0.1875, 0.875, 0.875),
      new AxisAlignedBB(0.1875, 0.4375, 0.3125, 0.6875, 0.6875, 0.6875)
  };
  
  private static final AxisAlignedBB[] COLLISION_SOUTH = new AxisAlignedBB[]
  {
      new AxisAlignedBB(0.3125, 0.4375, 0.5625, 0.6875, 0.6875, 0.8125),
      new AxisAlignedBB(0.125,  0.125,  0.8125, 0.875,  0.875,  1)
  };
  
  private static final AxisAlignedBB[] COLLISION_EAST = new AxisAlignedBB[]
  {
      new AxisAlignedBB(0.5625, 0.4375, 0.3125, 0.8125, 0.6875, 0.6875),
      new AxisAlignedBB(0.6875, 0.125,  0.125,  1,      0.875, 0.875)
  };

  public BlockRefractorySpout()
  {
    super(Material.ROCK);
    setCreativeTab(FoundryTabMachines.tab);
    setHardness(1.0F);
    setResistance(8.0F);
    setUnlocalizedName("foundry.refractorySpout");
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
  
  
  public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn)
  {
    AxisAlignedBB[] bounds = null;
    switch(state.getValue(FACING))
    {
      case EAST:
        bounds = COLLISION_EAST;
        break;
      case NORTH:
        bounds = COLLISION_NORTH;
        break;
      case SOUTH:
        bounds = COLLISION_SOUTH;
        break;
      case WEST:
        bounds = COLLISION_WEST;
        break;
    }
    for(AxisAlignedBB box:bounds)
    {
      addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
    }
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state)
  {
    
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack item)
  {
    
  }

  @Override
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
  {
    switch(facing)
    {
      case EAST:
        return getDefaultState().withProperty(FACING, EnumMachineFacing.WEST).withProperty(STATE, EnumMachineState.ON);
      case SOUTH:
        return getDefaultState().withProperty(FACING, EnumMachineFacing.NORTH).withProperty(STATE, EnumMachineState.ON);
      case NORTH:
        return getDefaultState().withProperty(FACING, EnumMachineFacing.SOUTH).withProperty(STATE, EnumMachineState.ON);
      case WEST:
        return getDefaultState().withProperty(FACING, EnumMachineFacing.EAST).withProperty(STATE, EnumMachineState.ON);
      default:
        return null;
    }
  }

  @Override
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
  {
    if(side == EnumFacing.UP || side == EnumFacing.DOWN)
    {
      return false;
    }
    BlockPos blockpos = pos.offset(side.getOpposite());
    return worldIn.isSideSolid(blockpos, side, true);
  }
  
  @Override
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
  {
    for(EnumMachineFacing state : FACING.getAllowedValues())
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
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
  {
    EnumFacing facing = state.getValue(FACING).facing;

    if(!world.isSideSolid(pos.offset(facing), facing.getOpposite(), true))
    {
      dropBlockAsItem(world, pos, state, 0);
      world.setBlockToAir(pos);
    }
  }
  
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hit_x, float hit_y, float hit_z)
  {
    if(world.isRemote)
    {
      return true;
    } else
    {
      switch(state.getValue(STATE))
      {
        case OFF:
          setMachineState(world, pos, state, true);
          break;
        case ON:
          setMachineState(world, pos, state, false);
          break;
      }
      return true;
    }
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
  {
    FoundryMiscUtils.localizeTooltip("tooltip.foundry.refractorySpout", tooltip);
  }
}
