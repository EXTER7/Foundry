package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.creativetab.FoundryTabFirearms;
import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRoundFire extends ItemRoundBase
{
  
  public ItemRoundFire()
  {
    super(8, 50, 25);
    setCreativeTab(FoundryTabFirearms.tab);
    setUnlocalizedName("roundFire");
  }


  @Override
  public void onBulletHitBlock(ItemStack ammo, EntityLivingBase shooter, Vec3d from, World world, BlockPos pos, EnumFacing side)
  {
    BlockPos front = pos.add(side.getDirectionVec());
    if(world.isAirBlock(front) && !world.isAirBlock(pos))
    {
      world.setBlockState(front, Blocks.fire.getDefaultState());
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean par4)
  {
    super.addInformation(stack, player, list, par4);
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(TextFormatting.YELLOW + "Sets target on fire.");
    }
  }

  @Override
  public void onBulletDamagedLivingEntity(ItemStack round, EntityLivingBase entity,int count)
  {
    if(!entity.isImmuneToFire())
    {
      entity.setFire(5);
    }
  }


  @Override
  public String getRoundType(ItemStack round)
  {
    return ItemRevolver.AMMO_TYPE;
  }
}
