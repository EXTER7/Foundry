package exter.foundry.block;

import java.util.Random;

import exter.foundry.creativetab.FoundryTabFluids;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class BlockLiquidMetal extends BlockFluidClassic
{
  private Object solid;
  
  private IBlockState solid_state = null;

  public BlockLiquidMetal(Fluid fluid, String name, Object solid_block)
  {
    super(fluid, Material.lava);
    setLightOpacity(0);
    setLightLevel(1.0f);
    solid = solid_block;
    setUnlocalizedName(name);
    setCreativeTab(FoundryTabFluids.tab);
  }
  
  @Override
  public String getUnlocalizedName()
  {
    return stack.getUnlocalizedName();
  }

  @Override
  public String getLocalizedName()
  {
    return stack.getLocalizedName();
  }

  @Override
  public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
  {
    return 300;
  }

  @Override
  public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
  {
    return 0;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
  {
    if(temperature < 1200)
    {
      return;
    }
    double dx;
    double dy;
    double dz;

    if(world.getBlockState(pos.add(0,1,0)).getBlock().getMaterial() == Material.air && !world.getBlockState(pos.add(0,1,0)).getBlock().isOpaqueCube())
    {
      if(rand.nextInt(100) == 0)
      {
        dx = (double) ((float) pos.getX() + rand.nextFloat());
        dy = (double) pos.getY() + this.maxY;
        dz = (double) ((float) pos.getZ() + rand.nextFloat());
        world.spawnParticle(EnumParticleTypes.LAVA, dx, dy, dz, 0.0D, 0.0D, 0.0D);
        world.playSound(dx, dy, dz, "liquid.lavapop", 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
      }

      if(rand.nextInt(200) == 0)
      {
        world.playSound((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), "liquid.lava", 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
      }
    }

    if(rand.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface(world, pos.add(0, -1, 0)) && !world.getBlockState(pos.add(0, -1, 0)).getBlock().getMaterial().blocksMovement())
    {
      dx = (double) ((float) pos.getX() + rand.nextFloat());
      dy = (double) pos.getY() - 1.05D;
      dz = (double) ((float) pos.getZ() + rand.nextFloat());

      world.spawnParticle(EnumParticleTypes.DRIP_LAVA, dx, dy, dz, 0.0D, 0.0D, 0.0D);
    }
  }

  @Override
  public boolean canDisplace(IBlockAccess world, BlockPos pos)
  {
    if(world.getBlockState(pos).getBlock().getMaterial().isLiquid())
    {
      return false;
    }
    return super.canDisplace(world, pos);
  }

  @Override
  public boolean displaceIfPossible(World world, BlockPos pos)
  {
    if(world.getBlockState(pos).getBlock().getMaterial().isLiquid())
    {
      return false;
    }
    return super.displaceIfPossible(world, pos);
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state)
  {
    super.onBlockAdded(world, pos, state);
    checkForHarden(world, pos, state);
  }

  @Override
  public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighbor)
  {
    super.onNeighborBlockChange(world, pos, state, neighbor);
    checkForHarden(world, pos, state);
  }
 
  private IBlockState getBlockStateFromItemStack(ItemStack stack)
  {
    Block block = ((ItemBlock)(stack.getItem())).block;
    int meta = stack.getMetadata();
    for(IBlockState state : block.getBlockState().getValidStates())
    {
      if(state != null && block.damageDropped(state) == meta)
      {
        return state;
      }
    }
    return null;
  }

  public void checkForHarden(World world, BlockPos pos, IBlockState state)
  {
    if(isSourceBlock(world, pos))
    {
      if(solid_state == null && solid != null)
      {
        ItemStack item = null;
        if(solid instanceof ItemStack)
        {
          item = (ItemStack)solid;
        } else if(solid instanceof String)
        {
          for(ItemStack i:OreDictionary.getOres((String)solid))
          {
            if(i.getItem() instanceof ItemBlock)
            {
              item = i;
              break;
            }
          }
        } else
        {
          solid = null;
        }
        
        if(item == null)
        {
          solid = null;
        }
        solid_state = getBlockStateFromItemStack(item);
      }
      if(solid_state == null)
      {
        return;
      }
      if(tryToHarden(world, pos, pos.add(-1, 0, 0)))
      {
        return;
      }
      if(tryToHarden(world, pos, pos.add(1, 0, 0)))
      {
        return;
      }
      if(tryToHarden(world, pos, pos.add(0, -1, 0)))
      {
        return;
      }
      if(tryToHarden(world, pos, pos.add(0, 1, 0)))
      {
        return;
      }
      if(tryToHarden(world, pos, pos.add(0, 0, -1)))
      {
        return;
      }
      if(tryToHarden(world, pos, pos.add(0, 0, 1)))
      {
        return;
      }
    }
  }

  private boolean tryToHarden(World world, BlockPos pos, BlockPos npos)
  {
    //Check if block is in contact with water.
    if(world.getBlockState(npos).getBlock().getMaterial() == Material.water)
    {
      int i;
      world.setBlockState(pos, solid_state);
      world.playSoundEffect((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
      for (i = 0; i < 8; i++)
      {
        world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)pos.getX() + Math.random(), (double)pos.getY() + 1.2D, (double)pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
      }
      return true;
    }
    return false;
  }
  
  @Override
  public void onEntityCollidedWithBlock(World wWorld, BlockPos pos, IBlockState state, Entity entity)
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
