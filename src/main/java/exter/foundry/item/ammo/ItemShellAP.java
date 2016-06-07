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

public class ItemShellAP extends Item
{
  static private class FirearmRound extends FirearmRoundShell
  {
    public FirearmRound()
    {
      super(4,40,15);
    }
    
    @Override
    public boolean ignoresArmor()
    {
      return true;
    }
  }
  
  public ItemShellAP()
  {
    setCreativeTab(FoundryTabFirearms.tab);
    setUnlocalizedName("shellAP");
    setRegistryName("shellAP");
  }
  
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4)
  {
    super.addInformation(stack, player, list, par4);
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(TextFormatting.BLUE + "Base Damage: 4/pellet");
      list.add(TextFormatting.BLUE + "Base Range: 40");
      list.add(TextFormatting.BLUE + "Falloff Range: 15");
      list.add(TextFormatting.YELLOW + "Bypasses armor.");
    }
  }
  
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
  {
    return new FirearmRound();
  }
}
