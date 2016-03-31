package exter.foundry.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.creativetab.FoundryTabMaterials;

public class ItemComponent extends Item
{
  static public enum SubItem
  {
    HEATINGCOIL(0,"componentHeatingCoil"),
    REFRACTORYCLAY(1,"componentRefractoryClay"),
    REFRACTORYBRICK(2,"componentRefractoryBrick"),
    GUN_BARREL(4,"componentGunBarrel"),
    REVOLVER_DRUM(5,"componentRevolverDrum"),
    REVOLVER_FRAME(6,"componentRevolverFrame"),
    AMMO_CASING(7,"componentRoundCasing"),
    AMMO_BULLET(8,"componentBullet"),
    AMMO_BULLET_HOLLOW(9,"componentBulletHollow"),
    AMMO_BULLET_JACKETED(10,"componentBulletJacketed"),
    GUNPOWDER_SMALL(11,"componentDustSmallGunpowder"),
    BLAZEPOWDER_SMALL(12,"componentDustSmallBlaze"),
    AMMO_PELLET(13,"componentPellet"),
    AMMO_CASING_SHELL(14,"componentShellCasing"),
    SHOTGUN_PUMP(15,"componentShotgunPump"),
    SHOTGUN_FRAME(16,"componentShotgunFrame"),
    AMMO_BULLET_STEEL(17,"componentBulletSteel"),
    AMMO_PELLET_STEEL(18,"componentPelletSteel"),
    REFRACTORYCLAY_SMALL(19,"componentSmallRefractoryClay");
    
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
  }
  
  @Override
  public String getUnlocalizedName(ItemStack itemstack)
  {
    return "item.foundry." + SubItem.fromId(itemstack.getItemDamage()).name;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list)
  {
    for(SubItem c:SubItem.values())
    {
      list.add(new ItemStack(this, 1, c.id));
    }
  }
}
