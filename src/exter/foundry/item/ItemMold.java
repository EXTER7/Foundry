package exter.foundry.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemMold extends Item
{
  
  static public final int MOLD_INGOT = 0;
  static public final int MOLD_INGOT_CLAY = 1;
  static public final int MOLD_CHESTPLATE = 2;
  static public final int MOLD_CHESTPLATE_CLAY = 3;
  static public final int MOLD_PICKAXE = 4;
  static public final int MOLD_PICKAXE_CLAY = 5;
  static public final int MOLD_BLOCK = 6;
  static public final int MOLD_BLOCK_CLAY = 7;
  static public final int MOLD_AXE = 8;
  static public final int MOLD_AXE_CLAY = 9;
  static public final int MOLD_SWORD = 10;
  static public final int MOLD_SWORD_CLAY = 11;
  static public final int MOLD_SHOVEL = 12;
  static public final int MOLD_SHOVEL_CLAY = 13;
  static public final int MOLD_HOE = 14;
  static public final int MOLD_HOE_CLAY = 15;
  static public final int MOLD_LEGGINGS = 16;
  static public final int MOLD_LEGGINGS_CLAY = 17;
  static public final int MOLD_HELMET = 18;
  static public final int MOLD_HELMET_CLAY = 19;
  static public final int MOLD_BOOTS = 20;
  static public final int MOLD_BOOTS_CLAY = 21;
  static public final int MOLD_GEAR = 22;
  static public final int MOLD_GEAR_CLAY = 23;
  static public final int MOLD_CABLE_IC2 = 24;
  static public final int MOLD_CABLE_IC2_CLAY = 25;
  static public final int MOLD_CASING_IC2 = 26;
  static public final int MOLD_CASING_IC2_CLAY = 27;
  static public final int MOLD_SLAB = 28;
  static public final int MOLD_SLAB_CLAY = 29;
  static public final int MOLD_STAIRS = 30;
  static public final int MOLD_STAIRS_CLAY = 31;
  static public final int MOLD_PLATE_IC2 = 32;
  static public final int MOLD_PLATE_IC2_CLAY = 33;


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
    "foundry:claymold_plate_ic2"
  };
  
  static public final String[] NAMES = 
  {
    "Ingot Mold",
    "Clay Ingot Mold",
    "Chestplate Mold",
    "Clay Chestplate Mold",
    "Pickaxe Mold",
    "Clay Pickaxe Mold",
    "Block Mold",
    "Clay Block Mold",    
    "Axe Mold",
    "Clay Axe Mold",
    "Sword Mold",
    "Clay Sword Mold",
    "Shovel Mold",
    "Clay Shovel Mold",
    "Hoe Mold",
    "Clay Hoe Mold",
    "Leggings Mold",
    "Clay Leggings Mold",
    "Helmet Mold",
    "Clay Helmet Mold",
    "Boots Mold",
    "Clay Boots Mold",
    "Gear Mold",
    "Clay Gear Mold",
    "Cable Mold",
    "Clay Cable Mold",
    "Casing Mold",
    "Clay Casing Mold",
    "Slab Mold",
    "Clay Slab Mold",
    "Stairs Mold",
    "Clay Stairs Mold",
    "Plate Mold",
    "Clay Plate Mold"
  };
  
  static public final String[] REGISTRY_NAMES = 
  {
    "itemIngotMold",
    "itemClayIngotMold",
    "itemChestplateMold",
    "itemClayChestplateMold",
    "itemPickaxeMold",
    "itemClayPickaxeMold",
    "itemBlockMold",
    "itemClayBlockMold",    
    "itemAxeMold",
    "itemClayAxeMold",
    "itemSwordMold",
    "itemClaySwordMold",
    "itemShovelMold",
    "itemClayShovelMold",
    "itemHoeMold",
    "itemClayHoeMold",
    "itemLeggingsMold",
    "itemClayLeggingsMold",
    "itemHelmetMold",
    "itemClayHelmetMold",
    "itemBootsMold",
    "itemClayBootsMold",
    "itemGearMold",
    "itemClayGearMold",
    "itemCableMold",
    "itemClayCableMold",
    "itemCasingMold",
    "itemClayCasingMold",
    "itemSlabMold",
    "itemClaySlabMold",
    "itemStairsMold",
    "itemClayStairsMold",
    "itemPlateMold",
    "itemClayPlateMold"
  };

  
  @SideOnly(Side.CLIENT)
  private Icon[] icons;

  public ItemMold(int id) {
    super(id);
    maxStackSize = 1;
    setCreativeTab(CreativeTabs.tabMisc);
    setHasSubtypes(true);
  }
  
  @Override
  public String getUnlocalizedName(ItemStack itemstack) {
    return "mold" + itemstack.getItemDamage();
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IconRegister register)
  {
    icons = new Icon[ICON_PATHS.length];

    int i;
    for(i = 0; i < icons.length; i++)
    {
      icons[i] = register.registerIcon(ICON_PATHS[i]);
    }
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public Icon getIconFromDamage(int dmg)
  {
    return icons[dmg];
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(int id, CreativeTabs tabs, List list)
  {
    int i;
    for (i = 0; i < ICON_PATHS.length; i++)
    {
      ItemStack itemstack = new ItemStack(id, 1, i);
      list.add(itemstack);
    }
  }
}
