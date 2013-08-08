package exter.foundry.block;

import java.util.Random;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.ModFoundry;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityDropParticleFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockLiquidMetal extends BlockFluidClassic
{
  private String texture_name;

  public BlockLiquidMetal(int id, Fluid fluid, Material material,String texture)
  {
    super(id, fluid, material);
    setLightOpacity(0);
    setLightValue(1.0f);
    texture_name = texture;
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
  public boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
  {
    return false;
  }

  @Override
  public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side)
  {
    return true;
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
}
