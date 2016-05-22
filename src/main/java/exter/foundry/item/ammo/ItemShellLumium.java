package exter.foundry.item.ammo;

import java.util.List;

import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.firearm.ItemShotgun;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemShellLumium extends ItemRoundBase
{
  public ItemShellLumium()
  {
    super(3,50,20);
    setUnlocalizedName("shellLumium");
    setRegistryName("shellLumium");
  }

  @Override
  public String getRoundType(ItemStack round)
  {
    return ItemShotgun.AMMO_TYPE;
  }

  @Override
  public ItemStack getCasing(ItemStack round)
  {
    return FoundryItems.component(ItemComponent.SubItem.AMMO_CASING_SHELL);
  }
  

  @Override
  public double getBaseDamage(ItemStack round,EntityLivingBase entity_hit)
  {
    double damage = super.getBaseDamage(round, entity_hit);
    if(entity_hit.isEntityUndead())
    {
      damage += 6;
    }
    return damage;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4)
  {
    if(GuiScreen.isShiftKeyDown())
    {
      list.add(TextFormatting.BLUE + "Base Damage: " + base_damage + " (+6.0 to undead creatures)");
      list.add(TextFormatting.BLUE + "Base Range: " + base_range);
      list.add(TextFormatting.BLUE + "Fallof Range: " + falloff_range);
    }
  }
}
