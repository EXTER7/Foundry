package exter.foundry.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ItemEmptyFoundryContainer extends Item
{

  public ItemEmptyFoundryContainer(int id)
  {
    super(id);
    setCreativeTab(CreativeTabs.tabMisc);
    maxStackSize = 1;
    LanguageRegistry.addName(this, "Foundry Container");
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
    itemIcon = register.registerIcon("foundry:container_def_empty");
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
  {
    list.add(EnumChatFormatting.BLUE + "Empty");
  }
}
