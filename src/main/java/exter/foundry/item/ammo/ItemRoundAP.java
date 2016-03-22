package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.item.firearm.ItemRevolver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRoundAP extends ItemRoundBase
{
  public ItemRoundAP()
  {
    super(7,50,25);
    setUnlocalizedName("roundAP");
  }

  @Override
  public String getRoundType(ItemStack round)
  {
    return ItemRevolver.AMMO_TYPE;
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
      list.add(TextFormatting.YELLOW + "Bypasses armor.");
    }
  }
}
