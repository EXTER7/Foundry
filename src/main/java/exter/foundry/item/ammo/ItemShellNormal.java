package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.creativetab.FoundryTabFirearms;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemShellNormal extends Item
{
  public ItemShellNormal()
  {
    setCreativeTab(FoundryTabFirearms.tab);
    setUnlocalizedName("foundry.shell_normal");
    setRegistryName("shell_normal");
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list,boolean advanced)
  {
    super.addInformation(stack, player, list, advanced);
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(TextFormatting.BLUE + "Base Damage: 5/pellet");
      list.add(TextFormatting.BLUE + "Base Range: 50");
      list.add(TextFormatting.BLUE + "Falloff Range: 20");
    }
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
  {
    return new FirearmRoundShell(5,50,20);
  }
}
