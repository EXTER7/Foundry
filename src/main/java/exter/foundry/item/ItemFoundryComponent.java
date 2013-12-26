package exter.foundry.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabMaterials;

public class ItemFoundryComponent extends Item
{
  static public final int COMPONENT_GEAR = 0;
  static public final int COMPONENT_HEATINGCOIL = 1;
  static public final int COMPONENT_FOUNDRYCLAY = 2;
  static public final int COMPONENT_FOUNDRYBRICK = 3;
  static public final int COMPONENT_BLANKMOLD = 4;


  static private final String[] ICON_PATHS = 
  {
    "foundry:gear",
    "foundry:heatingcoil",
    "foundry:foundry_clay",
    "foundry:foundry_brick",
    "foundry:claymold_blank"
  };
  
  static public final String[] NAMES = 
  {
    "Stone Gear",
    "Heating Coil",
    "Refractory Clay",
    "Refractory Brick",
    "Blank Clay Mold"
  };
  
  static public final String[] REGISTRY_NAMES = 
  {
    "itemStoneGear",
    "itemHeatingCoil",
    "itemRefractoryClay",
    "itemRefractoryBrick",
    "itemBlankClayMold"
  };

  
  @SideOnly(Side.CLIENT)
  private Icon[] icons;

  public ItemFoundryComponent(int id) {
    super(id);
    setCreativeTab(FoundryTabMaterials.tab);
    setHasSubtypes(true);
  }
  
  @Override
  public String getUnlocalizedName(ItemStack itemstack) {
    return "foundryComponent" + itemstack.getItemDamage();
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
  
  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(int id, CreativeTabs tabs, @SuppressWarnings("rawtypes") List list)
  {
    int i;
    for (i = 0; i < ICON_PATHS.length; i++)
    {
      ItemStack itemstack = new ItemStack(id, 1, i);
      list.add(itemstack);
    }
  }
}
