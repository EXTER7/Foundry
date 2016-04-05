package exter.foundry.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import exter.foundry.ModFoundry;
import exter.foundry.proxy.CommonFoundryProxy;
import exter.foundry.tileentity.TileEntityBurnerHeater;

public class BlockBurnerHeater extends BlockFoundrySidedMachine
{
  public BlockBurnerHeater()
  {
    super(Material.rock);
    setUnlocalizedName("foundry.burnerHeater");
    setHardness(1.0F);
    setResistance(8.0F);
    setSoundType(SoundType.STONE);
    setRegistryName("burnerHeater");
  }


  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hit_x, float hit_y, float hit_z)
  {
    if(world.isRemote)
    {
      return true;
    } else
    {
      //TODO
      player.openGui(ModFoundry.instance, CommonFoundryProxy.GUI_BURNERHEATER, world, pos.getX(), pos.getY(), pos.getZ());
      return true;
    }
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta)
  {
    return new TileEntityBurnerHeater();
  }
}
