package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.firearm.ItemShotgun;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemShellAP extends ItemRoundBase
{
  public ItemShellAP()
  {
    super(4,40,15);
    setUnlocalizedName("shellAP");
  }
  
  @Override
  public String getRoundType(ItemStack round)
  {
    return ItemShotgun.AMMO_TYPE;
  }

  @Override
  public ItemStack getCasing(ItemStack round)
  {
    return FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING_SHELL);
  }
  
  @Override
  public boolean ignoresArmor(ItemStack round)
  {
    return true;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean par4)
  {
    super.addInformation(stack, player, list, par4);
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(EnumChatFormatting.YELLOW + "Bypasses armor.");
    }
  }
}
