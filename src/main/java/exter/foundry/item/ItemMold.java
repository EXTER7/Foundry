package exter.foundry.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exter.foundry.creativetab.FoundryTabMolds;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMold extends Item
{  
  static public enum SubItem
  {
    INGOT(0,"moldIngot"),
    PLATE(1,"moldPlate"),
    GEAR(2,"moldGear"),
    ROD(3,"moldRod"),
    BLOCK(4,"moldBlock"),
    SLAB(5,"moldSlab"),
    STAIRS(6,"moldStairs"),
    PICKAXE(7,"moldPickaxe"),
    AXE(8,"moldAxe"),
    SWORD(9,"moldSword"),
    SHOVEL(10,"moldShovel"),
    HOE(11,"moldHoe"),
    HELMET(12,"moldHelmet"),
    CHESTPLATE(13,"moldChestplate"),
    LEGGINGS(14,"moldLeggings"),
    BOOTS(15,"moldBoots"),
    BULLET(16,"moldBullet"),
    BULLET_HOLLOW(17,"moldBulletHollow"),
    ROUND_CASING(18,"moldRoundCasing"),
    GUN_BARREL(19,"moldGunBarrel"),
    REVOLVER_DRUM(20,"moldRevolverDrum"),
    REVOLVER_FRAME(21,"moldRevolverFrame"),
    PELLET(22,"moldPellet"),
    SHELL_CASING(23,"moldShellCasing"),
    SHOTGUN_PUMP(24,"moldShotgunPump"),
    SHOTGUN_FRAME(25,"moldShotgunFrame");
    
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
  
  public ItemMold()
  {
    super();
    maxStackSize = 1;
    setCreativeTab(FoundryTabMolds.tab);
    setHasSubtypes(true);
    setUnlocalizedName("mold");
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
    for (SubItem m:SubItem.values())
    {
      ItemStack itemstack = new ItemStack(this, 1, m.id);
      list.add(itemstack);
    }
  }
}
