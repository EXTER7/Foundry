package exter.foundry.item;

import java.util.List;

import exter.foundry.creativetab.FoundryTabMaterials;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Deprecated // Moved to substratum
public class ItemIngot extends Item
{
  static public final int INGOT_COPPER = 0;
  static public final int INGOT_TIN = 1;
  static public final int INGOT_BRONZE = 2;
  static public final int INGOT_ELECTRUM = 3;
  static public final int INGOT_INVAR = 4;
  static public final int INGOT_NICKEL = 5;
  static public final int INGOT_ZINC = 6;
  static public final int INGOT_BRASS = 7;
  static public final int INGOT_SILVER = 8;
  static public final int INGOT_STEEL = 9;
  static public final int INGOT_LEAD = 10;
  static public final int INGOT_ALUMINUM = 11;
  static public final int INGOT_CHROMIUM = 12;
  static public final int INGOT_PLATINUM = 13;
  static public final int INGOT_MANGANESE = 14;
  static public final int INGOT_TITANIUM = 15;
  static public final int INGOT_CUPRONICKEL = 16;

  static public final String[] METAL_NAMES = 
  {
    "Copper",
    "Tin",
    "Bronze",
    "Electrum",
    "Invar",
    "Nickel",
    "Zinc",
    "Brass",
    "Silver",
    "Steel",
    "Lead",
    "Aluminum",
    "Chromium",
    "Platinum",
    "Manganese",
    "Titanium",
    "Cupronickel"
  };
  
  static public final String[] OREDICT_NAMES = 
  {
    "ingotCopper",
    "ingotTin",
    "ingotBronze",
    "ingotElectrum",
    "ingotInvar",
    "ingotNickel",
    "ingotZinc",
    "ingotBrass",
    "ingotSilver",
    "ingotSteel",
    "ingotLead",
    "ingotAluminum",
    "ingotChromium",
    "ingotPlatinum",
    "ingotManganese",
    "ingotTitanium",
    "ingotCupronickel"
  };
  
  public ItemIngot() {
    super();
    setCreativeTab(FoundryTabMaterials.tab);
    setHasSubtypes(true);
    setUnlocalizedName("ingot");
  }
  
  @Override
  public String getUnlocalizedName(ItemStack itemstack) {
    return getUnlocalizedName() + itemstack.getItemDamage();
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list)
  {

  }
}
