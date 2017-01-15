package exter.foundry.item;

import java.util.HashMap;
import java.util.Map;

import exter.foundry.creativetab.FoundryTabMolds;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMold extends Item
{  
  static public enum SubItem
  {
    INGOT(0,"mold_ingot"),
    PLATE(1,"mold_plate"),
    GEAR(2,"mold_gear"),
    ROD(3,"mold_rod"),
    BLOCK(4,"mold_block"),
    SLAB(5,"mold_slab"),
    STAIRS(6,"mold_stairs"),
    PICKAXE(7,"mold_pickaxe"),
    AXE(8,"mold_axe"),
    SWORD(9,"mold_sword"),
    SHOVEL(10,"mold_shovel"),
    HOE(11,"mold_hoe"),
    HELMET(12,"mold_helmet"),
    CHESTPLATE(13,"mold_chestplate"),
    LEGGINGS(14,"mold_leggings"),
    BOOTS(15,"mold_boots"),
    BULLET(16,"mold_bullet"),
    BULLET_HOLLOW(17,"mold_bullet_hollow"),
    ROUND_CASING(18,"mold_round_casing"),
    GUN_BARREL(19,"mold_gun_barrel"),
    REVOLVER_DRUM(20,"mold_revolver_drum"),
    REVOLVER_FRAME(21,"mold_revolver_frame"),
    PELLET(22,"mold_pellet"),
    SHELL_CASING(23,"mold_shell_casing"),
    SHOTGUN_PUMP(24,"mold_shotgun_pump"),
    SHOTGUN_FRAME(25,"mold_shotgun_frame");
    
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
    setRegistryName("mold");
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
    for (SubItem m:SubItem.values())
    {
      ItemStack itemstack = new ItemStack(this, 1, m.id);
      list.add(itemstack);
    }
  }
}
