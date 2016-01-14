package exter.foundry.item;

import java.util.List;

import exter.foundry.creativetab.FoundryTabMaterials;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDust extends Item
{
  static public final int DUST_COPPER = 0;
  static public final int DUST_TIN = 1;
  static public final int DUST_BRONZE = 2;
  static public final int DUST_ELECTRUM = 3;
  static public final int DUST_INVAR = 4;
  static public final int DUST_NICKEL = 5;
  static public final int DUST_ZINC = 6;
  static public final int DUST_BRASS = 7;
  static public final int DUST_SILVER = 8;
  static public final int DUST_STEEL = 9;
  static public final int DUST_LEAD = 10;
  static public final int DUST_ALUMINUM = 11;
  static public final int DUST_CHROMIUM = 12;
  static public final int DUST_PLATINUM = 13;
  static public final int DUST_MANGANESE = 14;
  static public final int DUST_TITANIUM = 15;
  static public final int DUST_CUPRONICKEL = 16;
  static public final int DUST_IRON = 17;
  static public final int DUST_GOLD = 18;

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
    "Cupronickel",
    "Iron",
    "Gold"
  };
  
  static public final String[] OREDICT_NAMES = 
  {
    "dustCopper",
    "dustTin",
    "dustBronze",
    "dustElectrum",
    "dustInvar",
    "dustNickel",
    "dustZinc",
    "dustBrass",
    "dustSilver",
    "dustSteel",
    "dustLead",
    "dustAluminum",
    "dustChromium",
    "dustPlatinum",
    "dustManganese",
    "dustTitanium",
    "dustCupronickel",
    "dustIron",
    "dustGold"
  };
  
  public ItemDust() {
    super();
    setCreativeTab(FoundryTabMaterials.tab);
    setHasSubtypes(true);
    setUnlocalizedName("dust");
  }
  
  @Override
  public String getUnlocalizedName(ItemStack itemstack) {
    return getUnlocalizedName() + itemstack.getItemDamage();
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list)
  {
    int i;
    for (i = 0; i < METAL_NAMES.length; i++)
    {
      ItemStack itemstack = new ItemStack(this, 1, i);
      list.add(itemstack);
    }
  }
}
