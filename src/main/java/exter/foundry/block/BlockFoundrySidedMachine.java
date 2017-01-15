package exter.foundry.block;

import exter.foundry.creativetab.FoundryTabMachines;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


public abstract class BlockFoundrySidedMachine extends BlockContainer
{
  public enum EnumMachineState implements IStringSerializable
  {
    OFF(0, "off"),
    ON(1, "on");

    public final int id;
    public final String name;

    private EnumMachineState(int id, String name)
    {
      this.id = id;
      this.name = name;
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

    static public EnumMachineState fromID(int num)
    {
      for(EnumMachineState m : values())
      {
        if(m.id == num)
        {
          return m;
        }
      }
      return null;
    }
  }

  public enum EnumMachineFacing implements IStringSerializable
  {
    NORTH(0, "north", EnumFacing.NORTH),
    SOUTH(1, "south", EnumFacing.SOUTH),
    EAST(2, "east", EnumFacing.EAST),
    WEST(3, "west", EnumFacing.WEST);

    public final int id;
    public final String name;
    public final EnumFacing facing;

    private EnumMachineFacing(int id, String name,EnumFacing facing)
    {
      this.id = id;
      this.name = name;
      this.facing = facing;
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

    static public EnumMachineFacing fromID(int num)
    {
      for(EnumMachineFacing m : values())
      {
        if(m.id == num)
        {
          return m;
        }
      }
      return null;
    }
  }

  public static final PropertyEnum<EnumMachineState> STATE = PropertyEnum.create("state", EnumMachineState.class);
  public static final PropertyEnum<EnumMachineFacing> FACING = PropertyEnum.create("facing", EnumMachineFacing.class);

  public BlockFoundrySidedMachine(Material material)
  {
    super(material);
    setCreativeTab(FoundryTabMachines.tab);
  }

  @Override
  public EnumBlockRenderType getRenderType(IBlockState state)
  {
    return EnumBlockRenderType.MODEL;
  }

  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, STATE, FACING );
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(FACING, EnumMachineFacing.fromID(meta & 3)).withProperty(STATE, EnumMachineState.fromID((meta >>> 2) & 1));
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    EnumMachineState fstate = (EnumMachineState) state.getValue(STATE);
    EnumMachineFacing facing = (EnumMachineFacing) state.getValue(FACING);
    return fstate.id << 2 | facing.id;
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state)
  {
    super.onBlockAdded(world, pos, state);
    if(!world.isRemote)
    {
      IBlockState block = world.getBlockState(pos.add(0, 0, -1));
      IBlockState block1 = world.getBlockState(pos.add(0, 0, 1));
      IBlockState block2 = world.getBlockState(pos.add(-1, 0, 0));
      IBlockState block3 = world.getBlockState(pos.add(1, 0, 0));
      EnumMachineFacing facing = EnumMachineFacing.NORTH;

      if(block.isOpaqueCube() && !block1.isOpaqueCube())
      {
        facing = EnumMachineFacing.NORTH;
      }
      if(block1.isOpaqueCube() && !block.isOpaqueCube())
      {
        facing = EnumMachineFacing.SOUTH;
      }
      if(block2.isOpaqueCube() && !block3.isOpaqueCube())
      {
        facing = EnumMachineFacing.EAST;
      }
      if(block3.isOpaqueCube() && !block2.isOpaqueCube())
      {
        facing = EnumMachineFacing.WEST;
      }
      world.setBlockState(pos, state.withProperty(FACING, facing));
    }
  }

  public void setMachineState(World world, BlockPos pos, IBlockState state, boolean is_on)
  {
    world.setBlockState(pos, state.withProperty(STATE, is_on ? EnumMachineState.ON : EnumMachineState.OFF));
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack item)
  {
    int dir = MathHelper.floor((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

    EnumMachineFacing facing = EnumMachineFacing.NORTH;
    if(dir == 0)
    {
      facing = EnumMachineFacing.NORTH;
    }
    if(dir == 1)
    {
      facing = EnumMachineFacing.EAST;
    }
    if(dir == 2)
    {
      facing = EnumMachineFacing.SOUTH;
    }
    if(dir == 3)
    {
      facing = EnumMachineFacing.WEST;
    }
    world.setBlockState(pos, state.withProperty(FACING, facing));
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state)
  {
    FoundryMiscUtils.breakTileEntityBlock(world, pos, state);
    super.breakBlock(world, pos, state);
  }


  @Override
  public boolean hasComparatorInputOverride(IBlockState state)
  {
    return true;
  }

  @Override
  public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos)
  {
    return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(pos));
  }
}
