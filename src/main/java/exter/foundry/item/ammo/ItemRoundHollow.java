package exter.foundry.item.ammo;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.creativetab.FoundryTabFirearms;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemRoundHollow extends Item implements IFirearmRound
{
  public IIcon icon;
  
  public ItemRoundHollow()
  {
    super();
    setCreativeTab(FoundryTabFirearms.tab);
    setUnlocalizedName("roundHollow");
  }


  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    icon = register.registerIcon("foundry:round_hollow");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int dmg)
  {
    return icon;
  }


  @Override
  public void OnHitBlock(ItemStack ammo, EntityPlayer player, Vec3 from, World world, int x, int y, int z, ForgeDirection side)
  {

  }

  @Override
  public void OnHitEntity(ItemStack ammo, EntityPlayer player, Vec3 from, Entity entity)
  {
    Vec3 end = Vec3.createVectorHelper( entity.posX, entity.posY, entity.posZ); 
    float distance = (float)end.distanceTo(from);
    float damage = 31 - distance / 2;
    if(damage > 16)
    {
      damage = 16;
    }
    if(damage >= 1)
    {
      entity.attackEntityFrom((new EntityDamageSourceIndirect("bullet", entity, player)).setProjectile(), damage);
    }
  }

  @Override
  public int getItemStackLimit(ItemStack stack)
  {
    return 16;
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean par4)
  {
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(EnumChatFormatting.BLUE + "Base Damage: 16");
      list.add(EnumChatFormatting.BLUE + "Base Range: 30");
      list.add(EnumChatFormatting.BLUE + "Fallof Range: 32");
    }
  }
}