package exter.foundry.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import exter.foundry.creativetab.FoundryTabMachines;
import exter.foundry.proxy.CommonFoundryProxy;
import exter.foundry.tileentity.TileEntityAlloyFurnace;
import exter.foundry.tileentity.TileEntityFoundryPowered;

public class BlockAlloyFurnace extends BlockContainer
{
  private final Random rand = new Random();

  public enum EnumState implements IStringSerializable
  {
    OFF(0, "off"),
    ON(1, "on");

    public final int id;
    public final String name;

    private EnumState(int id, String name)
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

    static public EnumState fromID(int num)
    {
      for(EnumState m : values())
      {
        if(m.id == num)
        {
          return m;
        }
      }
      return null;
    }
  }

  public enum EnumFurnaceFacing implements IStringSerializable
  {
    NORTH(0, "north"),
    SOUTH(1, "south"),
    EAST(2, "east"),
    WEST(3, "west");

    public final int id;
    public final String name;

    private EnumFurnaceFacing(int id, String name)
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

    static public EnumFurnaceFacing fromID(int num)
    {
      for(EnumFurnaceFacing m : values())
      {
        if(m.id == num)
        {
          return m;
        }
      }
      return null;
    }
  }

  public static final PropertyEnum<EnumState> STATE = PropertyEnum.create("state", EnumState.class);
  public static final PropertyEnum<EnumFurnaceFacing> FACING = PropertyEnum.create("facing", EnumFurnaceFacing.class);



  public BlockAlloyFurnace()
  {
    super(Material.rock);
    setUnlocalizedName("alloyFurnace");
    setHardness(1.0F);
    setResistance(8.0F);
    setSoundType(SoundType.STONE);
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
    return getDefaultState().withProperty(FACING, EnumFurnaceFacing.fromID(meta & 3)).withProperty(STATE, EnumState.fromID((meta >>> 2) & 1));
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    EnumState fstate = (EnumState) state.getValue(STATE);
    EnumFurnaceFacing facing = (EnumFurnaceFacing) state.getValue(FACING);
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
      EnumFurnaceFacing facing = EnumFurnaceFacing.NORTH;

      if(block.isOpaqueCube() && !block1.isOpaqueCube())
      {
        facing = EnumFurnaceFacing.NORTH;
      }
      if(block1.isOpaqueCube() && !block.isOpaqueCube())
      {
        facing = EnumFurnaceFacing.SOUTH;
      }
      if(block2.isOpaqueCube() && !block3.isOpaqueCube())
      {
        facing = EnumFurnaceFacing.EAST;
      }
      if(block3.isOpaqueCube() && !block2.isOpaqueCube())
      {
        facing = EnumFurnaceFacing.WEST;
      }
      world.setBlockState(pos, state.withProperty(FACING, facing));
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
      player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_ALLOYFURNACE, world, pos.getX(), pos.getY(), pos.getZ());
      return true;
    }
  }

  public void setFurnaceState(World world, BlockPos pos, IBlockState state, boolean is_on)
  {
    world.setBlockState(pos, state.withProperty(STATE, is_on ? EnumState.ON : EnumState.OFF));
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta)
  {
    return new TileEntityAlloyFurnace();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack item)
  {
    int dir = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

    EnumFurnaceFacing facing = EnumFurnaceFacing.NORTH;
    if(dir == 0)
    {
      facing = EnumFurnaceFacing.NORTH;
    }
    if(dir == 1)
    {
      facing = EnumFurnaceFacing.EAST;
    }
    if(dir == 2)
    {
      facing = EnumFurnaceFacing.SOUTH;
    }
    if(dir == 3)
    {
      facing = EnumFurnaceFacing.WEST;
    }
    world.setBlockState(pos, state.withProperty(FACING, facing));
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state)
  {
    TileEntity te = world.getTileEntity(pos);

    if(te != null && (te instanceof TileEntityFoundryPowered) && !world.isRemote)
    {
      TileEntityFoundryPowered tef = (TileEntityFoundryPowered) te;
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

  @SideOnly(Side.CLIENT)
  @Override
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random)
  {
    if(state.getValue(STATE) == EnumState.ON)
    {
      EnumFurnaceFacing facing = (EnumFurnaceFacing) state.getValue(FACING);
      float f = (float) pos.getX() + 0.5F;
      float f1 = (float) pos.getY() + 0.0F + random.nextFloat() * 6.0F / 16.0F;
      float f2 = (float) pos.getZ() + 0.5F;
      float f3 = 0.52F;
      float f4 = random.nextFloat() * 0.6F - 0.3F;

      switch(facing)
      {
        case NORTH:
          world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
          world.spawnParticle(EnumParticleTypes.FLAME, (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
          break;
        case EAST:
          world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
          world.spawnParticle(EnumParticleTypes.FLAME, (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
          break;
        case WEST:
          world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
          world.spawnParticle(EnumParticleTypes.FLAME, (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
          break;
        case SOUTH:
          world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
          world.spawnParticle(EnumParticleTypes.FLAME, (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
          break;
      }
    }
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
