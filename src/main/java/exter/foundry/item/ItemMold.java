package exter.foundry.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabMolds;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemMold extends Item
{
  
  static public final int MOLD_INGOT = 0;
  static public final int MOLD_INGOT_SOFT = 1;
  static public final int MOLD_CHESTPLATE = 2;
  static public final int MOLD_CHESTPLATE_SOFT = 3;
  static public final int MOLD_PICKAXE = 4;
  static public final int MOLD_PICKAXE_SOFT = 5;
  static public final int MOLD_BLOCK = 6;
  static public final int MOLD_BLOCK_SOFT = 7;
  static public final int MOLD_AXE = 8;
  static public final int MOLD_AXE_SOFT = 9;
  static public final int MOLD_SWORD = 10;
  static public final int MOLD_SWORD_SOFT = 11;
  static public final int MOLD_SHOVEL = 12;
  static public final int MOLD_SHOVEL_SOFT = 13;
  static public final int MOLD_HOE = 14;
  static public final int MOLD_HOE_SOFT = 15;
  static public final int MOLD_LEGGINGS = 16;
  static public final int MOLD_LEGGINGS_SOFT = 17;
  static public final int MOLD_HELMET = 18;
  static public final int MOLD_HELMET_SOFT = 19;
  static public final int MOLD_BOOTS = 20;
  static public final int MOLD_BOOTS_SOFT = 21;
  static public final int MOLD_GEAR = 22;
  static public final int MOLD_GEAR_SOFT = 23;
  static public final int MOLD_CABLE_IC2 = 24;
  static public final int MOLD_CABLE_IC2_SOFT = 25;
  static public final int MOLD_CASING_IC2 = 26;
  static public final int MOLD_CASING_IC2_SOFT = 27;
  static public final int MOLD_SLAB = 28;
  static public final int MOLD_SLAB_SOFT = 29;
  static public final int MOLD_STAIRS = 30;
  static public final int MOLD_STAIRS_SOFT = 31;
  static public final int MOLD_PLATE_IC2 = 32;
  static public final int MOLD_PLATE_IC2_SOFT = 33;
  static public final int MOLD_CAP_TC = 34;
  static public final int MOLD_CAP_TC_SOFT = 35;


  static private final String[] ICON_PATHS = 
  {
    "foundry:mold_ingot",
    "foundry:claymold_ingot",
    "foundry:mold_chestplate",
    "foundry:claymold_chestplate",
    "foundry:mold_pickaxe",
    "foundry:claymold_pickaxe",
    "foundry:mold_block",
    "foundry:claymold_block",
    "foundry:mold_axe",
    "foundry:claymold_axe",
    "foundry:mold_sword",
    "foundry:claymold_sword",
    "foundry:mold_shovel",
    "foundry:claymold_shovel",
    "foundry:mold_hoe",
    "foundry:claymold_hoe",
    "foundry:mold_leggings",
    "foundry:claymold_leggings",
    "foundry:mold_helmet",
    "foundry:claymold_helmet",
    "foundry:mold_boots",
    "foundry:claymold_boots",
    "foundry:mold_gear",
    "foundry:claymold_gear",
    "foundry:mold_cable_ic2",
    "foundry:claymold_cable_ic2",
    "foundry:mold_casing_ic2",
    "foundry:claymold_casing_ic2",
    "foundry:mold_slab",
    "foundry:claymold_slab",
    "foundry:mold_stairs",
    "foundry:claymold_stairs",
    "foundry:mold_plate_ic2",
    "foundry:claymold_plate_ic2",
    "foundry:mold_cap_tc",
    "foundry:claymold_cap_tc"
  };
  
  static public final String[] REGISTRY_NAMES = 
  {
    "itemIngotMold",
    "itemSoftIngotMold",
    "itemChestplateMold",
    "itemSoftChestplateMold",
    "itemPickaxeMold",
    "itemSoftPickaxeMold",
    "itemBlockMold",
    "itemSoftBlockMold",    
    "itemAxeMold",
    "itemSoftAxeMold",
    "itemSwordMold",
    "itemSoftSwordMold",
    "itemShovelMold",
    "itemSoftShovelMold",
    "itemHoeMold",
    "itemSoftHoeMold",
    "itemLeggingsMold",
    "itemSoftLeggingsMold",
    "itemHelmetMold",
    "itemSoftHelmetMold",
    "itemBootsMold",
    "itemSoftBootsMold",
    "itemGearMold",
    "itemSoftGearMold",
    "itemCableMold",
    "itemSoftCableMold",
    "itemCasingMold",
    "itemSoftCasingMold",
    "itemSlabMold",
    "itemSoftSlabMold",
    "itemStairsMold",
    "itemSoftStairsMold",
    "itemPlateMold",
    "itemSoftPlateMold",
    "itemCapMold",
    "itemSoftCapMold"
  };

  
  @SideOnly(Side.CLIENT)
  private IIcon[] icons;

  public ItemMold() {
    super();
    maxStackSize = 1;
    setCreativeTab(FoundryTabMolds.tab);
    setHasSubtypes(true);
    setUnlocalizedName("mold");
  }
  
  @Override
  public String getUnlocalizedName(ItemStack itemstack) {
    return getUnlocalizedName() + itemstack.getItemDamage();
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    icons = new IIcon[ICON_PATHS.length];

    int i;
    for(i = 0; i < icons.length; i++)
    {
      icons[i] = register.registerIcon(ICON_PATHS[i]);
    }
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int dmg)
  {
    return icons[dmg];
  }
  
  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs tabs, @SuppressWarnings("rawtypes") List list)
  {
    int i;
    for (i = 0; i < ICON_PATHS.length; i++)
    {
      ItemStack itemstack = new ItemStack(this, 1, i);
      list.add(itemstack);
    }
  }
}
