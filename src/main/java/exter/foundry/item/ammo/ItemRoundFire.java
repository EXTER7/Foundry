package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.creativetab.FoundryTabFirearms;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRoundFire extends Item
{
  static private class FirearmRound extends FirearmRoundBase
  {
    public FirearmRound()
    {
      super(8, 50, 25);
    }
    
    @Override
    public void onBulletHitBlock(EntityLivingBase shooter, Vec3d from, World world, BlockPos pos, EnumFacing side)
    {
      BlockPos front = pos.add(side.getDirectionVec());
      if(world.isAirBlock(front) && !world.isAirBlock(pos))
      {
        world.setBlockState(front, Blocks.FIRE.getDefaultState());
      }
    }
    
    @Override
    public void onBulletDamagedLivingEntity(EntityLivingBase entity,int count)
    {
      if(!entity.isImmuneToFire())
      {
        entity.setFire(5);
      }
    }
  }
  
  public ItemRoundFire()
  {
    setCreativeTab(FoundryTabFirearms.tab);
    setUnlocalizedName("roundFire");
    setRegistryName("roundFire");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4)
  {
    super.addInformation(stack, player, list, par4);
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(TextFormatting.BLUE + "Base Damage: 8");
      list.add(TextFormatting.BLUE + "Base Range: 50");
      list.add(TextFormatting.BLUE + "Falloff Range: 25");
      list.add(TextFormatting.YELLOW + "Sets target on fire.");
    }
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
  {
    return new FirearmRound();
  }
}
