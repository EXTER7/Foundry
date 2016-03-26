package exter.foundry.item;

import java.util.List;

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
    BLANKMOLD(3,"componentBlankMold"),
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
    SHARD_ENERGY_TC(17,"componentShardEnergyTC"),
    SHARD_LIFE_TC(18,"componentShardLifeTC"),
    SHARD_VOID_TC(19,"componentShardVoidTC"),
    AMMO_BULLET_STEEL(20,"componentBulletSteel"),
    AMMO_PELLET_STEEL(21,"componentPelletSteel");
    
    public final int id;
    public final String name;
    
    SubItem(int id,String name)
    {
      this.id = id;
      this.name = name;
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
    return getUnlocalizedName() + itemstack.getItemDamage();
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
