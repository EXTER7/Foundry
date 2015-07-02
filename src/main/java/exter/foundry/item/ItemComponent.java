package exter.foundry.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabMaterials;

public class ItemComponent extends Item
{
  static public final int COMPONENT_GEAR = 0;
  static public final int COMPONENT_HEATINGCOIL = 1;
  static public final int COMPONENT_REFRACTORYCLAY = 2;
  static public final int COMPONENT_REFRACTORYBRICK = 3;
  static public final int COMPONENT_BLANKMOLD = 4;
  static public final int COMPONENT_GUN_BARREL = 5;
  static public final int COMPONENT_REVOLVER_DRUM = 6;
  static public final int COMPONENT_REVOLVER_FRAME = 7;
  static public final int COMPONENT_AMMO_CASING = 8;
  static public final int COMPONENT_AMMO_BULLET = 9;
  static public final int COMPONENT_AMMO_BULLET_HOLLOW = 10;
  static public final int COMPONENT_AMMO_BULLET_JACKETED = 11;
  static public final int COMPONENT_GUNPOWDER_SMALL = 12;
  static public final int COMPONENT_BLAZEPOWDER_SMALL = 13;


  static private final String[] ICON_PATHS = 
  {
    "foundry:gear",
    "foundry:heatingcoil",
    "foundry:foundry_clay",
    "foundry:foundry_brick",
    "foundry:claymold_blank",
    "foundry:gun_barrel",
    "foundry:revolver_drum",
    "foundry:revolver_frame",
    "foundry:ammo_casing",
    "foundry:ammo_bullet",
    "foundry:ammo_bulletHollow",
    "foundry:ammo_bulletJacketed",
    "foundry:gunpowderSmall",
    "foundry:blazePowderSmall"
  };
  
  static public final String[] REGISTRY_NAMES = 
  {
    "itemStoneGear",
    "itemHeatingCoil",
    "itemRefractoryClay",
    "itemRefractoryBrick",
    "itemBlankClayMold",
    "itemGunBarrel",
    "itemRevolverDrum",
    "itemRevolverFrame",
    "itemAmmoCasing",
    "itemAmmoBullet",
    "itemAmmoBulletHollow",
    "itemAmmoBulletJacketed",
    "itemGunpowderSmall",
    "itemBlazePowderSmall"
  };
  
  
  @SideOnly(Side.CLIENT)
  private IIcon[] icons;

  public ItemComponent() {
    super();
    setCreativeTab(FoundryTabMaterials.tab);
    setHasSubtypes(true);
    setUnlocalizedName("component");
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
