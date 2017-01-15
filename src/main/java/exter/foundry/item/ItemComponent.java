package exter.foundry.item;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabMaterials;

public class ItemComponent extends Item implements IFuelHandler
{
  static public enum SubItem
  {
    HEATINGCOIL(0,"component_heating_coil"),
    REFRACTORYCLAY(1,"component_refractory_clay"),
    REFRACTORYBRICK(2,"component_refractory_brick"),
    GUN_BARREL(3,"component_gun_barrel"),
    REVOLVER_DRUM(4,"component_revolver_drum"),
    REVOLVER_FRAME(5,"component_revolver_frame"),
    AMMO_CASING(6,"component_round_casing"),
    AMMO_BULLET(7,"component_bullet"),
    AMMO_BULLET_HOLLOW(8,"component_bullet_hollow"),
    AMMO_BULLET_JACKETED(9,"component_bullet_jacketed"),
    AMMO_PELLET(10,"component_pellet"),
    AMMO_CASING_SHELL(11,"component_shell_casing"),
    SHOTGUN_PUMP(12,"component_shotgun_pump"),
    SHOTGUN_FRAME(13,"component_shotgun_frame"),
    AMMO_BULLET_STEEL(14,"component_bullet_steel"),
    AMMO_PELLET_STEEL(15,"component_pellet_steel"),
    REFRACTORYCLAY_SMALL(16,"component_small_refractory_clay"),
    INFERNOCLAY(17,"component_inferno_clay"),
    INFERNOBRICK(18,"component_inferno_brick"),
    AMMO_BULLET_LUMIUM(19,"component_bullet_lumium"),
    AMMO_PELLET_LUMIUM(20,"component_pellet_lumium"),
    COAL_COKE(21,"component_coal_coke");
    
    public final int id;
    public final String name;
    
    static private final Map<Integer,SubItem> value_map = new HashMap<Integer,SubItem>();
    
    SubItem(int id,String name)
    {
      this.id = id;
      this.name = name;
    }
    
    static public SubItem fromId(int id)
    {
      return value_map.get(id);
    }
        
    static
    {
      for(SubItem sub:values())
      {
        value_map.put(sub.id, sub);
      }
    }
  }


  public ItemComponent()
  {
    super();
    setCreativeTab(FoundryTabMaterials.tab);
    setHasSubtypes(true);
    setUnlocalizedName("component");
    setRegistryName("component");
  }
  
  @Override
  public String getUnlocalizedName(ItemStack itemstack)
  {
    return "item.foundry." + SubItem.fromId(itemstack.getItemDamage()).name;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs tabs, NonNullList<ItemStack> list)
  {
    for(SubItem c:SubItem.values())
    {
      list.add(new ItemStack(this, 1, c.id));
    }
  }

  @Override
  public int getBurnTime(ItemStack fuel)
  {
    if(fuel.getItem() == this && fuel.getMetadata() == SubItem.COAL_COKE.id)
    {
      return 3200;
    }      
    return 0;
  }
}
