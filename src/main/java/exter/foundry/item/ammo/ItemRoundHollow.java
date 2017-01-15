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

public class ItemRoundHollow extends Item
{
  public ItemRoundHollow()
  {
    setCreativeTab(FoundryTabFirearms.tab);
    setUnlocalizedName("round_hollow");
    setRegistryName("round_hollow");
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list,boolean advanced)
  {
    super.addInformation(stack, player, list, advanced);
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(TextFormatting.BLUE + "Base Damage: 16");
      list.add(TextFormatting.BLUE + "Base Range: 30");
      list.add(TextFormatting.BLUE + "Falloff Range: 25");
    }
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
  {
    return new FirearmRoundBase(16,30,25);
  }
}