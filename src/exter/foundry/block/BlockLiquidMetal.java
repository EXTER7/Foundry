package exter.foundry.block;

import java.util.Random;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import exter.foundry.creativetab.FoundryTabBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityDropParticleFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockLiquidMetal extends BlockFluidClassic
{
  private String texture_name;
  private int solid_meta;
  private int solid;

  public BlockLiquidMetal(int id, Fluid fluid, Material material,String texture,int solid_block,int solid_block_meta)
  {
    super(id, fluid, material);
    setLightOpacity(0);
    setLightValue(1.0f);
    texture_name = texture;
    solid = solid_block;
    solid_meta = solid_block_meta;
    setCreativeTab(FoundryTabBlocks.tab);
  }

  @SideOnly(Side.CLIENT)
  protected Icon[] icons;
  
  private static final int ICON_STILL = 0;
  private static final int ICON_FLOWING = 1;

  @Override
  public Icon getIcon(int side, int meta)
  {
    return side != 0 && side != 1 ? icons[ICON_FLOWING] : icons[ICON_STILL];
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IconRegister iconRegister)
  {
    icons = new Icon[2];
    ModFoundry.instance.log.info(fluidName);
    icons[ICON_STILL] = iconRegister.registerIcon("foundry:" + texture_name + "_still");
    icons[ICON_FLOWING] = iconRegister.registerIcon("foundry:" + texture_name + "_flow");
  }

  @Override
  public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face)
  {
    return 300;
  }

  @Override
  public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
  {
    return 0;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World world, int x, int y, int z, Random rand)
  {
    super.randomDisplayTick(world, x, y, z, rand);
  }

  @Override
  public boolean canDisplace(IBlockAccess world, int x, int y, int z)
  {
    if(world.getBlockMaterial(x, y, z).isLiquid())
      return false;
    return super.canDisplace(world, x, y, z);
  }

  @Override
  public boolean displaceIfPossible(World world, int x, int y, int z)
  {
    if(world.getBlockMaterial(x, y, z).isLiquid())
      return false;
    return super.displaceIfPossible(world, x, y, z);
  }

  @Override
  public void onBlockAdded(World world, int x, int y, int z)
  {
    super.onBlockAdded(world, x, y, z);
    CheckForHarden(world, x, y, z);
  }

  @Override
  public void onNeighborBlockChange(World world, int x, int y, int z, int neighbor_id)
  {
    super.onNeighborBlockChange(world, x, y, z, neighbor_id);
    CheckForHarden(world, x, y, z);
  }
 

  public void CheckForHarden(World world, int x, int y, int z)
  {
    if (isSourceBlock(world, x, y, z))
    {
      if(TryToHarden(world, x, y, z, x - 1, y, z))
      {
        return;
      }
      if(TryToHarden(world, x, y, z, x + 1, y, z))
      {
        return;
      }
      if(TryToHarden(world, x, y, z, x, y - 1, z))
      {
        return;
      }
      if(TryToHarden(world, x, y, z, x, y + 1, z))
      {
        return;
      }
      if(TryToHarden(world, x, y, z, x, y, z - 1))
      {
        return;
      }
      if(TryToHarden(world, x, y, z, x, y, z + 1))
      {
        return;
      }
    }
  }

  private boolean TryToHarden(World world, int x, int y, int z, int nx, int ny, int nz)
  {
    if(solid < 0)
    {
      return false;
    }
    //Check if block is in contact with water.
    if(world.getBlockMaterial(nx, ny, nz) == Material.water)
    {
      int i;
      //Turn the block solid.
      world.setBlock(x, y, z, solid, solid_meta, 3);
      world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
      for (i = 0; i < 8; i++)
      {
        world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + 1.2D, (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
      }
      return true;
    }
    return false;
  }
  
  @Override
  public void onEntityCollidedWithBlock(World wWorld, int x, int y, int z, Entity entity)
  {
    if(entity instanceof EntityLivingBase)
    {
      entity.motionX *= 0.5;
      entity.motionZ *= 0.5;
    }
    if(!entity.isImmuneToFire())
    {
      if(!(entity instanceof EntityItem))
      {
        entity.attackEntityFrom(DamageSource.lava, 4);
      }
      entity.setFire(15);
    }
  }
}
