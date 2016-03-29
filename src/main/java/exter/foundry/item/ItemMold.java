package exter.foundry.item;

import java.util.List;

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
    @Deprecated INGOT_SOFT(1,"moldSoftIngot"),
    CHESTPLATE(2,"moldChestplate"),
    @Deprecated CHESTPLATE_SOFT(3,"moldSoftChestplate"),
    PICKAXE(4,"moldPickaxe"),
    @Deprecated PICKAXE_SOFT(5,"moldSoftPickaxe"),
    BLOCK(6,"moldBlock"),
    @Deprecated BLOCK_SOFT(7,"moldSoftBlock"),
    AXE(8,"moldAxe"),
    @Deprecated AXE_SOFT(9,"moldSoftAxe"),
    SWORD(10,"moldSword"),
    @Deprecated SWORD_SOFT(11,"moldSoftSword"),
    SHOVEL(12,"moldShovel"),
    @Deprecated SHOVEL_SOFT(13,"moldSoftShovel"),
    HOE(14,"moldHoe"),
    @Deprecated HOE_SOFT(15,"moldSoftHoe"),
    LEGGINGS(16,"moldLeggings"),
    @Deprecated LEGGINGS_SOFT(17,"moldSoftLeggings"),
    HELMET(18,"moldHelmet"),
    @Deprecated HELMET_SOFT(19,"moldSoftHelmet"),
    BOOTS(20,"moldBoots"),
    @Deprecated BOOTS_SOFT(21,"moldSoftBoots"),
    GEAR(22,"moldGear"),
    @Deprecated GEAR_SOFT(23,"moldSoftGear"),
    CABLE_IC2(24,"moldCableIC2"),
    @Deprecated CABLE_IC2_SOFT(25,"moldSoftCableIC2"),
    CASING_IC2(26,"moldCasingIC2"),
    @Deprecated CASING_IC2_SOFT(27,"moldSoftCasingIC2"),
    SLAB(28,"moldSlab"),
    @Deprecated SLAB_SOFT(29,"moldSoftSlab"),
    STAIRS(30,"moldStairs"),
    @Deprecated STAIRS_SOFT(31,"moldSoftStairs"),
    PLATE(32,"moldPlate"),
    @Deprecated PLATE_SOFT(33,"moldSoftPlate"),
    CAP_TC(34,"moldWandCapTC"),
    @Deprecated CAP_TC_SOFT(35,"moldSoftWandCapTC"),
    INSULATED_CABLE_IC2(36,"moldCableInsulatedIC2"),
    @Deprecated INSULATED_CABLE_IC2_SOFT(37,"moldSoftCableInsulatedIC2"),
    SICKLE(38,"moldSickle"),
    @Deprecated SICKLE_SOFT(39,"moldSoftSickle"),
    BOW(40,"moldBow"),
    @Deprecated BOW_SOFT(41,"moldSoftBow"),
    FLUXPLATE(42,"moldFluxplate"),
    @Deprecated FLUXPLATE_SOFT(43,"moldSoftFluxplate"),
    BULLET(44,"moldBullet"),
    @Deprecated BULLET_SOFT(45,"moldSoftBullet"),
    BULLET_HOLLOW(46,"moldBulletHollow"),
    @Deprecated BULLET_HOLLOW_SOFT(47,"moldSoftBulletHollow"),
    BULLET_CASING(48,"moldRoundCasing"),
    @Deprecated BULLET_CASING_SOFT(49,"moldSoftRoundCasing"),
    GUN_BARREL(50,"moldGunBarrel"),
    @Deprecated GUN_BARREL_SOFT(51,"moldSoftGunBarrel"),
    REVOLVER_DRUM(52,"moldRevolverDrum"),
    @Deprecated REVOLVER_DRUM_SOFT(53,"moldSoftRevolverDrum"),
    REVOLVER_FRAME(54,"moldRevolverFrame"),
    @Deprecated REVOLVER_FRAME_SOFT(55,"moldSoftRevolverFrame"),
    WIRE_PR(56,"moldWirePR"),
    @Deprecated WIRE_PR_SOFT(57,"moldSoftWirePR"),
    PELLET(58,"moldPellet"),
    @Deprecated PELLET_SOFT(59,"moldSoftPellet"),
    SHELL_CASING(60,"moldShellCasing"),
    @Deprecated SHELL_CASING_SOFT(61,"moldSoftShellCasing"),
    SHOTGUN_PUMP(62,"moldShotgunPump"),
    @Deprecated SHOTGUN_PUMP_SOFT(63,"moldSoftShotgunPump"),
    SHOTGUN_FRAME(64,"moldShotgunFrame"),
    @Deprecated SHOTGUN_FRAME_SOFT(65,"moldSoftShotgunFrame"),
    SHARD_TC(66,"moldShardTC"),
    @Deprecated SHARD_TC_SOFT(67,"moldSoftShardTC"),
    ROD(68,"moldRod"),
    @Deprecated ROD_SOFT(69,"moldSoftRod");
    
    public final int id;
    public final String name;
    
    SubItem(int id,String name)
    {
      this.id = id;
      this.name = name;
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
    return getUnlocalizedName() + itemstack.getItemDamage();
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
