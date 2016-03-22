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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import exter.foundry.creativetab.FoundryTabMachines;
import exter.foundry.proxy.CommonFoundryProxy;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityFoundry;
import exter.foundry.tileentity.TileEntityInductionCrucibleFurnace;
import exter.foundry.tileentity.TileEntityMaterialRouter;
import exter.foundry.tileentity.TileEntityMetalAtomizer;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityMetalInfuser;

public class BlockFoundryMachine extends Block implements ITileEntityProvider,IBlockVariants
{
  private Random rand = new Random();

  static public enum EnumMachine implements IStringSerializable
  {
    ICF(0, "icf", "machineICF"),
    CASTER(1, "caster", "machineCaster"),
    ALLOYMIXER(2, "alloymixer", "machineAlloyMixer"),
    INFUSER(3, "infuser", "machineInfuser"),
    MATERIALROUTER(4, "router", "machineMaterialRouter"),
    ATOMIZER(5, "atomizer", "machineAtomizer");

    public final int id;
    public final String name;
    public final String model;

    private EnumMachine(int id, String name,String model)
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

    static public EnumMachine fromID(int num)
    {
      for(EnumMachine m : values())
      {
        if(m.id == num)
        {
          return m;
        }
      }
      return null;
    }
  }

  public static final PropertyEnum<EnumMachine> MACHINE = PropertyEnum.create("machine", EnumMachine.class);

  public BlockFoundryMachine()
  {
    super(Material.iron);
    setHardness(1.0F);
    setResistance(8.0F);
    setSoundType(SoundType.STONE);
    setUnlocalizedName("machine");
    setCreativeTab(FoundryTabMachines.tab);
  }

  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, MACHINE);
  }


  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(MACHINE, EnumMachine.fromID(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return ((EnumMachine)state.getValue(MACHINE)).id;
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
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack item,EnumFacing side, float hit_x, float hit_y, float hit_z)
  {
    if(world.isRemote)
    {
      return true;
    } else
    {
      switch((EnumMachine)state.getValue(MACHINE))
      {
        case ICF:
          player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_ICF, world, pos.getX(), pos.getY(), pos.getZ());
          break;
        case CASTER:
          player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_CASTER, world, pos.getX(), pos.getY(), pos.getZ());
          break;
        case ALLOYMIXER:
          player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_ALLOYMIXER, world, pos.getX(), pos.getY(), pos.getZ());
          break;
        case INFUSER:
          player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_INFUSER, world, pos.getX(), pos.getY(), pos.getZ());
          break;
        case MATERIALROUTER:
          player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_MATERIALROUTER, world, pos.getX(), pos.getY(), pos.getZ());
          break;
        case ATOMIZER:
          player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_ATOMIZER, world, pos.getX(), pos.getY(), pos.getZ());
          break;
      }
      return true;
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
    switch((EnumMachine)state.getValue(MACHINE))
    {
      case ICF:
        return new TileEntityInductionCrucibleFurnace();
      case CASTER:
        return new TileEntityMetalCaster();
      case ALLOYMIXER:
        return new TileEntityAlloyMixer();
      case INFUSER:
        return new TileEntityMetalInfuser();
      case MATERIALROUTER:
        return new TileEntityMaterialRouter();
      case ATOMIZER:
        return new TileEntityMetalAtomizer();
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
    for(EnumMachine m:EnumMachine.values())
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
    return getUnlocalizedName() + "." + ((EnumMachine)getStateFromMeta(meta).getValue(MACHINE)).name;
  }
  
  public ItemStack asItemStack(EnumMachine machine)
  {
    return new ItemStack(this,1,getMetaFromState(getDefaultState().withProperty(MACHINE, machine)));
  }
}
