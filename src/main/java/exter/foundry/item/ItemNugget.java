package exter.foundry.item;

import java.util.List;

import exter.foundry.creativetab.FoundryTabMaterials;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Deprecated // Moved to substratum
public class ItemNugget extends Item
{
  static public final int NUGGET_COPPER = 0;
  static public final int NUGGET_TIN = 1;
  static public final int NUGGET_BRONZE = 2;
  static public final int NUGGET_ELECTRUM = 3;
  static public final int NUGGET_INVAR = 4;
  static public final int NUGGET_NICKEL = 5;
  static public final int NUGGET_ZINC = 6;
  static public final int NUGGET_BRASS = 7;
  static public final int NUGGET_SILVER = 8;
  static public final int NUGGET_STEEL = 9;
  static public final int NUGGET_LEAD = 10;
  static public final int NUGGET_ALUMINUM = 11;
  static public final int NUGGET_CHROMIUM = 12;
  static public final int NUGGET_PLATINUM = 13;
  static public final int NUGGET_MANGANESE = 14;
  static public final int NUGGET_TITANIUM = 15;
  static public final int NUGGET_CUPRONICKEL = 16;
  static public final int NUGGET_IRON = 17;

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
    "Iron"
  };
  
  static public final String[] OREDICT_NAMES = 
  {
    "nuggetCopper",
    "nuggetTin",
    "nuggetBronze",
    "nuggetElectrum",
    "nuggetInvar",
    "nuggetNickel",
    "nuggetZinc",
    "nuggetBrass",
    "nuggetSilver",
    "nuggetSteel",
    "nuggetLead",
    "nuggetAluminum",
    "nuggetChromium",
    "nuggetPlatinum",
    "nuggetManganese",
    "nuggetTitanium",
    "nuggetCupronickel",
    "nuggetIron"
  };
  
  public ItemNugget() {
    super();
    setCreativeTab(FoundryTabMaterials.tab);
    setHasSubtypes(true);
    setUnlocalizedName("nugget");
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
