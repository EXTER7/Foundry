package exter.foundry.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFoundryContainer extends Item
{

  @SideOnly(Side.CLIENT)
  public Icon icon_fg;
  @SideOnly(Side.CLIENT)
  public Icon icon_bg;

  @SideOnly(Side.CLIENT)
  public Icon icon_def_empty;
  @SideOnly(Side.CLIENT)
  public Icon icon_def_partial;
  @SideOnly(Side.CLIENT)
  public Icon icon_def_full;

  private Fluid fluid;

  public static final int AMOUNT_MAX = 1000;

  public ItemFoundryContainer(int id, Fluid container_fluid)
  {
    super(id);
    fluid = container_fluid;
    setMaxDamage(AMOUNT_MAX);
    setCreativeTab(CreativeTabs.tabMisc);
    maxStackSize = 1;
    LanguageRegistry.addName(this, "Foundry Container");
  }

  public Fluid GetFluid()
  {
    return fluid;
  }

  @Override
  public String getUnlocalizedName()
  {
    return "foundryContainer";
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IconRegister register)
  {
    icon_fg = register.registerIcon("foundry:container_foreground");
    icon_bg = register.registerIcon("foundry:container_background");
    icon_def_empty = register.registerIcon("foundry:container_def_empty");
    icon_def_partial = register.registerIcon("foundry:container_def_partial");
    icon_def_full = register.registerIcon("foundry:container_def_full");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public Icon getIconFromDamage(int dmg)
  {
    if(fluid == null || dmg < AMOUNT_MAX / 3)
    {
      return icon_def_empty;
    } else if (dmg > AMOUNT_MAX * 2 / 3)
    {
      return icon_def_full;
    } else
    {
      return icon_def_partial;
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(int id, CreativeTabs tabs, List list)
  {
    int i;
    ItemStack itemstack;
    if(fluid == null)
    {
      itemstack = new ItemStack(id, 1, 0);
    } else
    {
      itemstack = new ItemStack(id, 1, AMOUNT_MAX);
    }
    list.add(itemstack);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
    if(fluid == null)
    {
      list.add(EnumChatFormatting.BLUE + "Empty");
    } else
    {
      list.add(EnumChatFormatting.BLUE + fluid.getLocalizedName());
      list.add(EnumChatFormatting.BLUE + String.valueOf(stack.getItemDamage()) + " / 1000 mB");
    }
  }
}
