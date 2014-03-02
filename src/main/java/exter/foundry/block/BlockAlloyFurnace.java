package exter.foundry.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import exter.foundry.creativetab.FoundryTabMachines;
import exter.foundry.proxy.CommonFoundryProxy;
import exter.foundry.tileentity.TileEntityAlloyFurnace;
import exter.foundry.tileentity.TileEntityFoundryPowered;

public class BlockAlloyFurnace extends BlockContainer
{
  private final Random rand = new Random();

  @SideOnly(Side.CLIENT)
  private IIcon icon_top_bottom;
  @SideOnly(Side.CLIENT)
  private IIcon icon_sides;
  @SideOnly(Side.CLIENT)
  private IIcon icon_front_on;
  @SideOnly(Side.CLIENT)
  private IIcon icon_front_off;

  public BlockAlloyFurnace()
  {
    super(Material.rock);
    setBlockName("alloyFurnace");
    setHardness(1.0F);
    setResistance(8.0F);
    setStepSound(Block.soundTypeStone);
    setCreativeTab(FoundryTabMachines.tab);
  }

  @Override
  public void onBlockAdded(World world, int x, int y, int z)
  {
    super.onBlockAdded(world, x, y, z);
    if(!world.isRemote)
    {
      Block block = world.getBlock(x, y, z - 1);
      Block block1 = world.getBlock(x, y, z + 1);
      Block block2 = world.getBlock(x - 1, y, z);
      Block block3 = world.getBlock(x + 1, y, z);
      byte meta = 0;

      if(block.func_149730_j/* isOpaque */() && !block1.func_149730_j/* isOpaque */())
      {
        meta = 2;
      }

      if(block1.func_149730_j/* isOpaque */() && !block.func_149730_j/* isOpaque */())
      {
        meta = 3;
      }

      if(block2.func_149730_j/* isOpaque */() && !block3.func_149730_j/* isOpaque */())
      {
        meta = 0;
      }
      if(block3.func_149730_j/* isOpaque */() && !block2.func_149730_j/* isOpaque */())
      {
        meta = 1;
      }

      world.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IIcon getIcon(int side, int meta)
  {
    switch(side)
    {
      case 0:
      case 1:
        return icon_top_bottom;
      default:
        return (side == 5 - GetDirection(meta)) ? (IsFurnaceOn(meta) ? icon_front_on : icon_front_off) : icon_sides;
    }
  }

  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister register)
  {
    icon_top_bottom = register.registerIcon("foundry:alloyfurnace_top_bottom");
    icon_front_on = register.registerIcon("foundry:alloyfurnace_front_on");
    icon_front_off = register.registerIcon("foundry:alloyfurnace_front_off");
    icon_sides = register.registerIcon("foundry:alloyfurnace_side");
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
  {
    if(world.isRemote)
    {
      return true;
    } else
    {
      player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_ALLOYFURNACE, world, x, y, z);
      return true;
    }
  }

  public void SetFurnaceState(World world, int x, int y, int z, boolean is_on)
  {
    TileEntity tileentity = world.getTileEntity(x, y, z);

    int meta = world.getBlockMetadata(x, y, z);
    meta = (meta & 3) | (is_on ? 4 : 0);
    world.setBlockMetadataWithNotify(x, y, z, meta, 2);
    if(tileentity != null)
    {
      tileentity.validate();
      world.setTileEntity(x, y, z, tileentity);
    }
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta)
  {
    return new TileEntityAlloyFurnace();
  }

  @Override
  public void onBlockPlacedBy(World world_, int x, int y, int z, EntityLivingBase player, ItemStack item)
  {
    int dir = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

    if(dir == 0)
    {
      world_.setBlockMetadataWithNotify(x, y, z, 3, 2);
    }

    if(dir == 1)
    {
      world_.setBlockMetadataWithNotify(x, y, z, 0, 2);
    }

    if(dir == 2)
    {
      world_.setBlockMetadataWithNotify(x, y, z, 2, 2);
    }

    if(dir == 3)
    {
      world_.setBlockMetadataWithNotify(x, y, z, 1, 2);
    }
  }

  @Override
  public void breakBlock(World world, int x, int y, int z, Block block, int meta)
  {
    TileEntity te = world.getTileEntity(x, y, z);

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
          EntityItem entityitem = new EntityItem(world, x + drop_x, y + drop_y, z + drop_z, is);
          entityitem.delayBeforeCanPickup = 10;

          world.spawnEntityInWorld(entityitem);
        }
      }
    }
    world.removeTileEntity(x, y, z);
    super.breakBlock(world, x, y, z, block, meta);
  }

  public boolean IsFurnaceOn(int metadata)
  {
    return ((metadata >> 2) & 1) == 1;
  }

  public int GetDirection(int metadata)
  {
    return metadata & 3;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void randomDisplayTick(World world, int x, int y, int z, Random random)
  {
    int meta = world.getBlockMetadata(x, y, z);
    if(IsFurnaceOn(meta))
    {
      int direction = GetDirection(meta);
      float f = (float) x + 0.5F;
      float f1 = (float) y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
      float f2 = (float) z + 0.5F;
      float f3 = 0.52F;
      float f4 = random.nextFloat() * 0.6F - 0.3F;

      if(direction == 1)
      {
        world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
        world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
      } else if(direction == 0)
      {
        world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
        world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
      } else if(direction == 3)
      {
        world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
        world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
      } else if(direction == 2)
      {
        world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
        world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
      }
    }
  }

  @Override
  public boolean hasComparatorInputOverride()
  {
    return true;
  }

  @Override
  public int getComparatorInputOverride(World world, int x, int y, int z, int p_149736_5_)
  {
    return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
  }
}
