package exter.foundry.block;

import java.util.List;
import java.util.Random;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import exter.foundry.creativetab.FoundryTabMachines;
import exter.foundry.proxy.CommonFoundryProxy;
import exter.foundry.tileentity.TileEntityFoundry;
import exter.foundry.tileentity.TileEntityRefractoryHopper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRefractoryHopper extends BlockContainer
{

  public enum EnumHopperFacing implements IStringSerializable
  {
    NORTH(0, "north", EnumFacing.NORTH),
    SOUTH(1, "south", EnumFacing.SOUTH),
    EAST(2, "east", EnumFacing.EAST),
    WEST(3, "west", EnumFacing.WEST),
    DOWN(4, "down", EnumFacing.DOWN);

    public final int id;
    public final String name;
    public final EnumFacing facing;

    private EnumHopperFacing(int id, String name, EnumFacing target)
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

    static public EnumHopperFacing fromID(int num)
    {
      for(EnumHopperFacing m : values())
      {
        if(m.id == num)
        {
          return m;
        }
      }
      return null;
    }
  }

  public static final PropertyEnum<EnumHopperFacing> FACING = PropertyEnum.create("facing", EnumHopperFacing.class);

  protected static final AxisAlignedBB AABB_SIDES = new AxisAlignedBB(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);

  private Random rand = new Random();
  
  protected static final AxisAlignedBB[] BOUNDS = new AxisAlignedBB[]
  {
    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D),
    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D),
    new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D),
    new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
    new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D)
  };

  public BlockRefractoryHopper()
  {
    super(Material.iron);
    setCreativeTab(FoundryTabMachines.tab);
    setHardness(1.0F);
    setResistance(8.0F);
    setUnlocalizedName("foundry.refractoryHopper");
    setDefaultState(blockState.getBaseState().withProperty(FACING, EnumHopperFacing.DOWN));
    setRegistryName("refractoryHopper");
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
  {
      return state.getValue(FACING) == EnumHopperFacing.DOWN ? FULL_BLOCK_AABB : AABB_SIDES;
  }

  public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn)
  {
    for(AxisAlignedBB box:BOUNDS)
    {
      addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
    }
  }


  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, FACING);
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(FACING, EnumHopperFacing.fromID(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return ((EnumHopperFacing) state.getValue(FACING)).id;
  }


  @Override
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
  {
    switch(facing)
    {
      case EAST:
        return getDefaultState().withProperty(FACING, EnumHopperFacing.WEST);
      case NORTH:
        return getDefaultState().withProperty(FACING, EnumHopperFacing.SOUTH);
      case SOUTH:
        return getDefaultState().withProperty(FACING, EnumHopperFacing.NORTH);
      case WEST:
        return getDefaultState().withProperty(FACING, EnumHopperFacing.EAST);
      default:
        return getDefaultState().withProperty(FACING, EnumHopperFacing.DOWN);
      
    }
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta)
  {
    return new TileEntityRefractoryHopper();
  }

  @Override
  public boolean onBlockEventReceived(World world, BlockPos pos, IBlockState state, int par5, int par6)
  {
    super.onBlockEventReceived(world, pos, state, par5, par6);
    TileEntity tileentity = world.getTileEntity(pos);
    return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
  }

  @Override
  public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block)
  {
    TileEntityFoundry te = (TileEntityFoundry) world.getTileEntity(pos);

    if(te != null)
    {
      te.updateRedstone();
    }
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack item, EnumFacing side, float hitx, float hity, float hitz)
  {
    if(world.isRemote)
    {
      return true;
    } else
    {
      player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_REFRACTORYHOPPER, world, pos.getX(), pos.getY(), pos.getZ());
      return true;
    }
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

  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
  {
    return true;
  }
}
