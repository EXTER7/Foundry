package exter.foundry.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabMachines;
import exter.foundry.tileentity.TileEntityCastingTableBase;
import exter.foundry.tileentity.TileEntityCastingTableIngot;
import exter.foundry.tileentity.TileEntityCastingTablePlate;
import exter.foundry.tileentity.TileEntityCastingTableRod;
import exter.foundry.tileentity.TileEntityFoundry;


public class BlockCastingTable extends Block implements ITileEntityProvider,IBlockVariants
{
  private Random rand = new Random();

  protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

  static public enum EnumTable implements IStringSerializable
  {
    INGOT(0, "ingot", "castingTableIngot"),
    PLATE(1, "plate", "castingTablePlate"),
    ROD(2, "rod", "castingTableRod");

    public final int id;
    public final String name;
    public final String model;

    private EnumTable(int id, String name,String model)
    {
      this.id = id;
      this.name = name;
      this.model = model;
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

    static public EnumTable fromID(int num)
    {
      for(EnumTable m : values())
      {
        if(m.id == num)
        {
          return m;
        }
      }
      return null;
    }
  }

  public static final PropertyEnum<EnumTable> TABLE = PropertyEnum.create("type", EnumTable.class);

  public BlockCastingTable()
  {
    super(Material.iron);
    setHardness(1.0F);
    setResistance(8.0F);
    setSoundType(SoundType.STONE);
    setUnlocalizedName("foundry.castingTable");
    setCreativeTab(FoundryTabMachines.tab);
    setRegistryName("castingTable");
  }

  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, TABLE);
  }


  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(TABLE, EnumTable.fromID(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return ((EnumTable)state.getValue(TABLE)).id;
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
  {
    return AABB;
  }

  @Override
  public boolean isFullyOpaque(IBlockState state)
  {
    return false;
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
  public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
  {
    return face == EnumFacing.DOWN;
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
  
  private void dropCastingTableOutput(World world, BlockPos pos, IBlockState state)
  {
    TileEntity te = world.getTileEntity(pos);

    if(te != null && (te instanceof TileEntityCastingTableBase) && !world.isRemote)
    {
      TileEntityCastingTableBase te_ct = (TileEntityCastingTableBase) te;
      if(te_ct.getProgress() == 0)
      {
        ItemStack is = te_ct.getStackInSlot(0);

        if(is != null && is.stackSize > 0)
        {
          EntityItem entityitem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, is);
          entityitem.setPickupDelay(1);
  
          world.spawnEntityInWorld(entityitem);
          te_ct.setInventorySlotContents(0, null);
        }
      }
    }
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack item,EnumFacing side, float hit_x, float hit_y, float hit_z)
  {
    if(world.isRemote)
    {
      return true;
    } else
    {
      dropCastingTableOutput(world, pos, state);
      return false;
    }
  }

  @Override
  public boolean hasTileEntity(IBlockState state)
  {
    return true;
  }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state)
  {
    switch(state.getValue(TABLE))
    {
      case INGOT:
        return new TileEntityCastingTableIngot();
      case PLATE:
        return new TileEntityCastingTablePlate();
      case ROD:
        return new TileEntityCastingTableRod();
    }
    return null;
  }

  @Override
  public boolean onBlockEventReceived(World world, BlockPos pos, IBlockState state, int par5, int par6)
  {
    super.onBlockEventReceived(world, pos, state, par5, par6);
    TileEntity tileentity = world.getTileEntity(pos);
    return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
  }

  @Override
  public int damageDropped(IBlockState state)
  {
    return getMetaFromState(state);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item item, CreativeTabs tab, @SuppressWarnings("rawtypes") List list)
  {
    for(EnumTable m:EnumTable.values())
    {
      list.add(new ItemStack(item, 1, m.id));
    }
  }
  
  @Override
  public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neigbor_block)
  {
    TileEntityFoundry te = (TileEntityFoundry) world.getTileEntity(pos);

    if(te != null)
    {
      te.updateRedstone();
    }
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta)
  {
    return this.createTileEntity(world, getStateFromMeta(meta));
  }

  @Override
  public String getUnlocalizedName(int meta)
  {
    return getUnlocalizedName() + "." + getStateFromMeta(meta).getValue(TABLE).name;
  }
  
  public ItemStack asItemStack(EnumTable machine)
  {
    return new ItemStack(this,1,getMetaFromState(getDefaultState().withProperty(TABLE, machine)));
  }
}
