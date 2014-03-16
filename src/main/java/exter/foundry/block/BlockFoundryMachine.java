package exter.foundry.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import exter.foundry.creativetab.FoundryTabMachines;
import exter.foundry.proxy.CommonFoundryProxy;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityFoundry;
import exter.foundry.tileentity.TileEntityInductionCrucibleFurnace;
import exter.foundry.tileentity.TileEntityMaterialRouter;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityMetalInfuser;

public class BlockFoundryMachine extends Block implements ITileEntityProvider,ISubBlocks
{
  private Random rand = new Random();

  static public final int MACHINE_ICF = 0;
  static public final int MACHINE_CASTER = 1;
  static public final int MACHINE_ALLOYMIXER = 2;
  static public final int MACHINE_INFUSER = 3;
  static public final int MACHINE_MATERIALROUTER = 4;

  static private final String[][] PATHS_ICONS =
  {
    {
      "foundry:metalsmelter_top",
      "foundry:metalsmelter_top",
      "foundry:metalsmelter_sides",
      "foundry:metalsmelter_sides",
      "foundry:metalsmelter_sides",
      "foundry:metalsmelter_sides"
    },
    {
      "foundry:caster_bottom",
      "foundry:caster_top",
      "foundry:caster_sides",
      "foundry:caster_sides",
      "foundry:caster_sides",
      "foundry:caster_sides"
    },
    {
      "foundry:alloymixer_bottom",
      "foundry:alloymixer_top",
      "foundry:alloymixer_sides",
      "foundry:alloymixer_sides",
      "foundry:alloymixer_sides",
      "foundry:alloymixer_sides"
    },
    {
      "foundry:infuser_bottom",
      "foundry:infuser_top",
      "foundry:infuser_sides",
      "foundry:infuser_sides",
      "foundry:infuser_sides",
      "foundry:infuser_sides"
    },
    {
      "foundry:materialrouter_0",
      "foundry:materialrouter_1",
      "foundry:materialrouter_2",
      "foundry:materialrouter_3",
      "foundry:materialrouter_4",
      "foundry:materialrouter_5"
    }
  };
  

  static private final String[] NAMES =
  {
    "ICF",
    "Caster",
    "AlloyMixer",
    "Infuser",
    "MaterialRouter"
  };

  private IIcon[][] icons;

  public BlockFoundryMachine()
  {
    super(Material.iron);
    setHardness(1.0F);
    setResistance(8.0F);
    setStepSound(Block.soundTypeStone);
    setBlockName("machine");
    setCreativeTab(FoundryTabMachines.tab);
  }

  @Override
  public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
  {
    TileEntity te = world.getTileEntity(x, y, z);

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
          EntityItem entityitem = new EntityItem(world, x + drop_x, y + drop_y, z + drop_z, is);
          entityitem.delayBeforeCanPickup = 10;

          world.spawnEntityInWorld(entityitem);
        }
      }
    }
    world.removeTileEntity(x, y, z);
    super.breakBlock(world, x, y, z, par5, par6);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister register)
  {
    int i;
    
    icons = new IIcon[5][6];
    
    for(i = 0; i < 5; i++)
    {
      int j;
      for(j = 0; j < 6; j++)
      {
        icons[i][j] = register.registerIcon(PATHS_ICONS[i][j]);
      }
    }
  }

  @Override
  public IIcon getIcon(int side, int meta)
  {
    return icons[meta][side];
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
  {
    if(world.isRemote)
    {
      return true;
    } else
    {
      switch(world.getBlockMetadata(x, y, z))
      {
        case MACHINE_ICF:
          player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_ICF, world, x, y, z);
          break;
        case MACHINE_CASTER:
          player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_CASTER, world, x, y, z);
          break;
        case MACHINE_ALLOYMIXER:
          player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_ALLOYMIXER, world, x, y, z);
          break;
        case MACHINE_INFUSER:
          player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_INFUSER, world, x, y, z);
          break;
        case MACHINE_MATERIALROUTER:
          player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_MATERIALROUTER, world, x, y, z);
          break;
      }
      return true;
    }
  }

  @Override
  public boolean hasTileEntity(int metadata)
  {
    return true;
  }

  @Override
  public TileEntity createTileEntity(World world, int meta)
  {
    switch(meta)
    {
      case MACHINE_ICF:
        return new TileEntityInductionCrucibleFurnace();
      case MACHINE_CASTER:
        return new TileEntityMetalCaster();
      case MACHINE_ALLOYMIXER:
        return new TileEntityAlloyMixer();
      case MACHINE_INFUSER:
        return new TileEntityMetalInfuser();
      case MACHINE_MATERIALROUTER:
        return new TileEntityMaterialRouter();
    }
    return null;
  }

  @Override
  public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6)
  {
    super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
    TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
    return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
  }

  @Override
  public int damageDropped(int meta)
  {
    return meta;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubBlocks(Item item, CreativeTabs tab, @SuppressWarnings("rawtypes") List list)
  {
    int i;
    for(i = 0; i < 5; i++)
    {
      list.add(new ItemStack(item, 1, i));
    }
  }

  @Override
  public TileEntity createNewTileEntity(World world,int meta)
  {
    return createTileEntity(world, meta);
  }
  
  @Override
  public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
  {
    TileEntityFoundry te = (TileEntityFoundry) world.getTileEntity(x, y, z);

    if(te != null)
    {
      te.UpdateRedstone();
    }
  }

  @Override
  public String[] GetSubNames()
  {
    return NAMES;
  }
}
